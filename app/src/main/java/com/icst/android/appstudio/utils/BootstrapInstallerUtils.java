/*
 *  This file is part of AndroidAppStudio.
 *
 *  AndroidAppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidAppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidAppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.android.appstudio.utils;

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

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ResourceUtils;

import android.content.Context;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Pair;

public class BootstrapInstallerUtils {

	public interface ProgressListener {
		void onProgress(String message);

		void onWarning(String warning);
	}

	public static Context context;

	public static CompletableFuture<Void> install(
			final Context cont, final ProgressListener listener, final File PREFIX) {
		context = cont;

		return CompletableFuture.runAsync(
				() -> {
					notify(listener, "Extracting aapt2...");
					extractAapt2(PREFIX, context, listener);
					notify(listener, "Extracting hooks...");
					extractLibHooks(PREFIX, context, listener);
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

								var dir = isDirectory
										? targetFile
										: new File(
												targetFile
														.getAbsolutePath()
														.substring(0, targetFile.getAbsolutePath().lastIndexOf('/')));
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
											|| zipEntryName.startsWith("lib/jvm/java-17-openjdk/bin")
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

	public static void extractLibHooks(
			final File PREFIX, Context context, ProgressListener listener) {
		if (!new File(mkdirIfNotExits(new File(PREFIX, "lib")), "libhook.so").exists()) {
			copyFileFromAssets(
					"libhook.so",
					new File(mkdirIfNotExits(new File(PREFIX, "lib")), "libhook.so").getAbsolutePath());

			grantFile(new File(new File(PREFIX, "lib"), "libhook.so"), context, listener);
		}
	}

	public static void extractAapt2(final File PREFIX, Context context, ProgressListener listener) {
		if (!new File(mkdirIfNotExits(new File(PREFIX, "bin")), "aapt2").exists()) {
			copyFileFromAssets(
					"aapt2", new File(mkdirIfNotExits(new File(PREFIX, "bin")), "aapt2").getAbsolutePath());

			grantFile(new File(new File(PREFIX, "bin"), "aapt2"), context, listener);
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
