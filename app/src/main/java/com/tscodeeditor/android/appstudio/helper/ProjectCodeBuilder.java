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

package com.tscodeeditor.android.appstudio.helper;

import android.code.editor.common.utils.FileUtils;
import com.tscodeeditor.android.appstudio.activities.BaseActivity;
import com.tscodeeditor.android.appstudio.block.model.FileModel;
import com.tscodeeditor.android.appstudio.exception.ProjectCodeBuildException;
import com.tscodeeditor.android.appstudio.listener.ProjectCodeBuildListener;
import com.tscodeeditor.android.appstudio.utils.EnvironmentUtils;
import com.tscodeeditor.android.appstudio.utils.serialization.DeserializerUtils;
import java.io.File;
import java.util.concurrent.Executors;

public final class ProjectCodeBuilder {

  public static void buildProjectCode(
      File rootDestination,
      File data,
      BaseActivity activity,
      ProjectCodeBuildListener listener,
      ProjectCodeBuilderCancelToken cancelToken,
      boolean shouldCleanBeforeBuild) {
    Executors.newSingleThreadExecutor()
        .execute(
            () -> {
              if (listener != null) listener.onBuildStart();

              if (rootDestination.exists()) {
                if (rootDestination.isDirectory()) {

                  if (shouldCleanBeforeBuild) {
                    if (listener != null) {
                      listener.onBuildProgressLog("Cleaning destination folder...");
                    }
                    if (!cleanFile(rootDestination, listener, cancelToken)) {
                      if (listener != null) {
                        ProjectCodeBuildException exception = new ProjectCodeBuildException();
                        exception.setMessage("Failed to clean destination folder");
                        listener.onBuildFailed(exception);
                        return;
                      }
                    }
                  }

                } else {

                  if (listener != null) {
                    StringBuilder log = new StringBuilder();
                    log.append(
                        "Destination directory in which file/folder was about to generate is an file but expected folder");
                    log.append("\n");
                    log.append("Removing the file...");
                    listener.onBuildProgressLog(log.toString());
                  }

                  rootDestination.delete();

                  if (listener != null) {
                    listener.onBuildProgressLog("Creating destination folder...");
                  }
                  rootDestination.mkdirs();

                  if (listener != null) {
                    listener.onBuildProgressLog("Created destination folder...");
                  }
                }
              } else {
                if (listener != null) {
                  listener.onBuildProgressLog("Destination folder doesn't exists, Creating new...");
                }
                rootDestination.mkdirs();
                if (listener != null) {
                  listener.onBuildProgressLog("Created Destination folder");
                }
              }

              if (!data.exists()) {
                if (listener != null) {
                  ProjectCodeBuildException exception = new ProjectCodeBuildException();
                  exception.setMessage(
                      "File of which code the app was about to generate doesn't exists.");
                  listener.onBuildFailed(exception);
                  return;
                }
              }

              File fileModel = new File(data, EnvironmentUtils.FILE_MODEL);
              if (fileModel.exists()) {
                // TODO: Generate a single file || Generate a whole folder
              } else {
                File[] files = data.listFiles();
                if (files.length == 0) {
                  if (listener != null) {
                    ProjectCodeBuildException exception = new ProjectCodeBuildException();
                    exception.setMessage("Data directory is empty.");
                    listener.onBuildFailed(exception);
                    return;
                  }
                }

                for (File file : files) {
                  File generatedFile = generateFileModelOutput(data, file, listener, cancelToken);
                }
              }
            });
  }

  public static File generateFileModelOutput(
      File destination,
      File fileModelDirectory,
      ProjectCodeBuildListener listener,
      ProjectCodeBuilderCancelToken cencelToken) {
    FileModel fileModel =
        (FileModel)
            DeserializerUtils.deserialize(
                new File(fileModelDirectory, EnvironmentUtils.FILE_MODEL));

    File output = new File(destination, fileModel.getName());

    if (fileModel.isFolder()) {
      output.mkdirs();
      if (new File(fileModelDirectory, EnvironmentUtils.FILES).exists()) {
        for (File file : new File(fileModelDirectory, EnvironmentUtils.FILES).listFiles()) {
          File generatedFile = generateFileModelOutput(output, file, listener, cencelToken);
        }
      }

    } else {
      FileModelCodeHelper codeGeneratorHelper = new FileModelCodeHelper();
      codeGeneratorHelper.setFileModel(fileModel);
      codeGeneratorHelper.setEventsDirectory(
          new File(fileModelDirectory, EnvironmentUtils.EVENTS_DIR));

      FileUtils.writeFile(output.getAbsolutePath(), codeGeneratorHelper.getCode());
    }
    return output;
  }

  public static void buildProjectCode(
      File rootDestination,
      File data,
      BaseActivity activity,
      ProjectCodeBuilderCancelToken cancelToken,
      boolean shouldCleanBeforeBuild) {
    buildProjectCode(rootDestination, data, activity, null, cancelToken, shouldCleanBeforeBuild);
  }

  private static boolean cleanFile(
      File file, ProjectCodeBuildListener listener, ProjectCodeBuilderCancelToken cancelToken) {
    if (!file.exists()) {
      if (listener != null) {
        listener.onBuildProgressLog("Directory does not exists, no need to clean");
      }
      file.mkdirs();
      return true;
    }
    if (file.isFile()) {
      if (listener != null) {
        listener.onBuildProgressLog("Cleaning " + file.getAbsolutePath());
      }
      return file.delete();
    } else {
      if (file.listFiles().length == 0) {
        if (listener != null) {
          listener.onBuildProgressLog(
              "Directory is already clean, no need to clean " + file.getAbsolutePath());
        }
        return true;
      } else {
        if (listener != null) {
          listener.onBuildProgressLog(
              "Cleaning sub files and folders of " + file.getAbsolutePath());
        }
        for (File subFile : file.listFiles()) {
          if (listener != null) {
            listener.onBuildProgressLog("Deleting " + file.getAbsolutePath());
          }
          if (!cleanFile(subFile, listener, cancelToken)) {
            return false;
          }
        }
        if (listener != null) {
          listener.onBuildProgressLog("Cleaned sub files and folders of " + file.getAbsolutePath());
        }
        return true;
      }
    }
  }
}
