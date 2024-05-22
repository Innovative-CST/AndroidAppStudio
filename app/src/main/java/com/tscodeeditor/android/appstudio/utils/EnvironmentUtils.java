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

package com.tscodeeditor.android.appstudio.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import com.tscodeeditor.android.appstudio.BuildConfig;
import java.io.File;

public final class EnvironmentUtils {
  public static File STORAGE;
  public static File IDEDIR;
  public static File PROJECTS;
  public static final String PROJECT_CONFIGRATION = "ProjectConfig";
  public static final String FILE_MODEL = "FileModel";
  public static final String FILES = "files";
  public static final String EVENTS_DIR = "Events";
  public static final String EVENTS_HOLDER = "EventsHolder";
  public static final String SOURCE_DIR = "src";
  public static final String MAIN_DIR = "main";
  public static final String JAVA_DIR = "java";
  public static final String RES_DIR = "res";
  public static final String GRADLE_FILE = "build.gradle";
  public static final String APP_GRADLE_CONFIG_EVENT_HOLDER = "Config";
  private static final String PROJECT_DATA_DIR = "data";

  public static void init(Context context) {
    String IDEDIRECTORY;
    if (BuildConfig.isDeveloperMode) {
      STORAGE =
          BuildConfig.STORAGE.getAbsolutePath().equals("NOT_PROVIDED")
              ? Environment.getExternalStorageDirectory()
              : BuildConfig.STORAGE;
      IDEDIRECTORY = ".AndroidAppBuilder";
    } else {
      STORAGE = new File(getDataDir(context), "files");
      IDEDIRECTORY = "home";
    }

    IDEDIR = new File(STORAGE, IDEDIRECTORY);
    PROJECTS = new File(IDEDIR, "Projects");
  }

  public static String getDataDir(Context context) {
    PackageManager pm = context.getPackageManager();
    String packageName = context.getPackageName();
    PackageInfo packageInfo;
    try {
      packageInfo = pm.getPackageInfo(packageName, 0);
      return packageInfo.applicationInfo.dataDir;
    } catch (PackageManager.NameNotFoundException e) {
      return "";
    }
  }

  public static File getProjectDataDir(File projectRootDirectory) {
    return new File(projectRootDirectory, PROJECT_DATA_DIR);
  }

  public static File getBuildDir(File projectRootDirectory) {
    return new File(projectRootDirectory, "build");
  }

  public static File getModuleDirectory(File projectRootDirectory, String modules) {
    if (modules == null) return null;

    String[] module = modules.split(":");
    File modulePath = getProjectDataDir(projectRootDirectory);
    for (int i = 0; i < module.length; ++i) {
      if (i == 0) continue;
      modulePath = new File(modulePath, module[i]);
      if (i != (module.length - 1)) {
        modulePath = new File(modulePath, FILES);
      }
    }

    return modulePath;
  }
}
