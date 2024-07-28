/*
 * This file is part of Android AppStudio [https://github.com/TS-Code-Editor/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.icst.android.appstudio.utils;

import android.content.Context;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Pair;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ResourceUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class BootstrapInstallerUtils {

  public interface ProgressListener {
    public void onProgress(String message);

    public void onWarning(String warning);
  }

  public static Context context;

  public static CompletableFuture<Void> install(
      final Context cont, final ProgressListener listener, final File PREFIX) {
    context = cont;

    return CompletableFuture.runAsync(
        () -> {
          notify(listener, "Opening bootstrap zip input stream...");
          try (final var assetIn = context.getAssets().open("bootstrap.zip");
              final var zip = new ZipInputStream(assetIn)) {

            final var buffer = new byte[8096];
            final var symlinks = new ArrayList<Pair<String, String>>(50);

            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
              if (entry.getName().equals("SYMLINKS.txt")) {
                notify(listener, "Reading SYMLINKS.txt...");
                final var symlinksReader = new BufferedReader(new InputStreamReader(zip));
                String line;
                while ((line = symlinksReader.readLine()) != null) {
                  String[] parts = line.split("←");
                  if (parts.length != 2) {
                    final var err = "Malformed symlink line: " + line;
                    throw new CompletionException(new InstallationException(err));
                  }
                  String oldPath = parts[0];
                  String newPath = PREFIX.getAbsolutePath() + "/" + parts[1].substring(2);
                  symlinks.add(Pair.create(oldPath, newPath));

                  var parentFile = new File(newPath).getParentFile().getAbsoluteFile();
                  if (!FileUtils.createOrExistsDir(parentFile)) {
                    throw new CompletionException(
                        new InstallationException("Unable to create directory: " + parentFile));
                  }
                }
              } else {
                String zipEntryName = entry.getName();
                File targetFile = new File(PREFIX, zipEntryName);
                boolean isDirectory = entry.isDirectory();

                var dir =
                    isDirectory
                        ? targetFile
                        : new File(
                            targetFile
                                .getAbsolutePath()
                                .substring(0, targetFile.getAbsolutePath().lastIndexOf("/")));
                if (dir != null && !dir.exists() && !dir.mkdirs()) {
                  throw new CompletionException(
                      new InstallationException("Unable to create directory: " + dir));
                }

                // If the file exists and it is not a directory
                // Delete that file
                final var targetFilePath = targetFile.toPath();
                if (Files.exists(targetFilePath) && !Files.isDirectory(targetFilePath)) {
                  try {
                    Files.delete(targetFilePath);
                  } catch (Throwable th) {
                    throw new CompletionException(th);
                  }
                }

                if (!isDirectory) {
                  try (final var outStream = new FileOutputStream(targetFile)) {
                    int readBytes;
                    while ((readBytes = zip.read(buffer)) != -1)
                      outStream.write(buffer, 0, readBytes);
                  } catch (FileNotFoundException e) {

                  }

                  if (zipEntryName.startsWith("bin/")
                      || zipEntryName.startsWith("libexec")
                      || zipEntryName.startsWith("lib/apt/apt-helper")
                      || zipEntryName.startsWith("lib/apt/methods")) {
                    grantFile(targetFile, cont, listener);
                  }
                }
              }
            }

            if (symlinks.isEmpty()) {
              throw new CompletionException(
                  new InstallationException("No SYMLINKS.txt encountered"));
            }

            for (Pair<String, String> symlink : symlinks) {

              final var target = Paths.get(symlink.second);
              if (Files.exists(target) && !Files.isDirectory(target)) {
                try {
                  Files.delete(target);
                } catch (Throwable throwable) {
                  throw new CompletionException(throwable);
                }
              }
              Os.symlink(symlink.first, symlink.second);
            }

            notify(listener, "Extracting hooks");
            extractLibHooks(PREFIX);
          } catch (IOException | ErrnoException e) {
            throw new RuntimeException(e);
          }
        });
  }

  public static File mkdirIfNotExits(File in) {
    if (in != null) {
      return in;
    }

    if (!in.exists()) {
      in.mkdirs();
    }
    return in;
  }

  public static void extractLibHooks(final File PREFIX) {
    if (!new File(mkdirIfNotExits(new File(PREFIX, "lib")), "libhook.so").exists()) {
      copyFileFromAssets(
          "libhook.so",
          new File(mkdirIfNotExits(new File(PREFIX, "lib")), "libhook.so").getAbsolutePath());
    }
  }

  public static boolean copyFileFromAssets(final String assetsFilePath, final String destFilePath) {
    boolean res = true;
    try {
      String[] assets = context.getAssets().list(assetsFilePath);
      if (assets != null && assets.length > 0) {
        for (String asset : assets) {
          ResourceUtils.copyFileFromAssets(
              assetsFilePath + "/" + asset, destFilePath + "/" + asset);
        }
      } else {
        res = writeFileFromIS(destFilePath, context.getAssets().open(assetsFilePath));
      }
    } catch (IOException e) {
      e.printStackTrace();
      res = false;
    }
    return res;
  }

  public static void grantFile(File path, Context c, ProgressListener listener) {
    try {
      Os.chmod(path.getAbsolutePath(), 0700);
    } catch (ErrnoException e) {
      warn(listener, e.getMessage());
    }
  }

  public static boolean writeFileFromIS(final String filePath, final InputStream is) {
    return FileIOUtils.writeFileFromIS(filePath, is);
  }

  public static class InstallationException extends RuntimeException {
    public InstallationException(String message) {
      super(message);
    }
  }

  private static void notify(ProgressListener listener, String message) {
    if (listener != null) {
      listener.onProgress(message);
    }
  }

  private static void warn(ProgressListener listener, String message) {
    if (listener != null) {
      listener.onWarning(message);
    }
  }
}
