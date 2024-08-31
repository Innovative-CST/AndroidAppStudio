/*
 * This file is part of Android AppStudio [https://github.com/Innovative-CST/AndroidAppStudio].
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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.system.ErrnoException;
import android.system.Os;
import android.widget.Toast;
import com.icst.android.appstudio.BuildConfig;
import com.icst.android.appstudio.MyApplication;
import com.icst.android.appstudio.models.ModuleModel;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EnvironmentUtils {
  public static File STORAGE;
  public static File IDEDIR;
  public static File PROJECTS;
  public static File SETTING_FILE;
  public static File EXTENSION_DIR;
  public static final String PROJECT_DEPENDENCIES = "libs";
  public static final String PROJECT_CONFIGRATION = "ProjectConfig";
  public static final String FILE_MODEL = "FileModel";
  public static final String JAVA_FILE_MODEL = "JavaFileModel";
  public static final String VARIABLES = "Variables";
  public static final String STATIC_VARIABLES = "StaticVariables";
  public static final String LAYOUT_VARIABLE = "LayoutVariable";
  public static final String FILES = "files";
  public static final String EVENTS_DIR = "Events";
  public static final String EVENTS_HOLDER = "EventsHolder";
  public static final String SOURCE_DIR = "src";
  public static final String MAIN_DIR = "main";
  public static final String MANIFEST = "AndroidManifest.xml";
  public static final String JAVA_DIR = "java";
  public static final String RES_DIR = "res";
  public static final String GRADLE_FILE = "build.gradle";
  public static final String APP_GRADLE_CONFIG_EVENT_HOLDER = "Config";
  private static final String PROJECT_DATA_DIR = "data";
  private static final String CONFIG = "config";
  private static final String SETTING = "setting";

  public static File APP_LIBRARIES;
  public static File BIN_DIR;
  public static File PREFIX;
  public static File ROOT;
  public static File HOME;
  public static File IDE_HOME;
  public static File TMP_DIR;
  public static File LIB_DIR;
  public static File IDE_PROPS_FILE;
  public static File LIB_HOOK;
  public static File SHELL;
  public static File LOGIN_SHELL;
  public static File BUSYBOX;
  public static final Map<String, String> IDE_PROPS = new HashMap<>();
  public static final List<String> blacklist = new ArrayList<>();
  public static Map<String, String> ENV_VARS = new HashMap<>();

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
    APP_LIBRARIES = mkdirIfNotExits(new File(IDEDIR, "libraries"));
    SETTING_FILE = new File(new File(IDEDIR, CONFIG), SETTING);
    EXTENSION_DIR = new File(IDEDIR, "Extension");
    if (!EXTENSION_DIR.exists()) EXTENSION_DIR.mkdirs();

    ROOT = mkdirIfNotExits(new File("/data/data/com.icst.android.appstudio/files"));
    PREFIX = mkdirIfNotExits(new File(ROOT, "usr"));
    HOME = mkdirIfNotExits(new File(ROOT, "home"));
    IDE_HOME = mkdirIfNotExits(new File(HOME, ".ide"));
    TMP_DIR = mkdirIfNotExits(new File(PREFIX, "tmp"));
    BIN_DIR = mkdirIfNotExits(new File(PREFIX, "bin"));
    LIB_DIR = mkdirIfNotExits(new File(PREFIX, "lib"));
    SHELL = new File(BIN_DIR, "bash");
    LOGIN_SHELL = new File(BIN_DIR, "login");
    BUSYBOX = mkdirIfNotExits(new File(BIN_DIR, "busybox"));

    BUSYBOX.setExecutable(true);
    SHELL.setExecutable(true);
    LOGIN_SHELL.setExecutable(true);
    grantFile(BUSYBOX);
    grantFile(LOGIN_SHELL);
    grantFile(SHELL);
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

  public static void grantFile(File path) {
    try {
      Os.chmod(path.getAbsolutePath(), 0700);
    } catch (ErrnoException e) {
    }
  }

  public static Map<String, String> getEnvironment() {

    if (!ENV_VARS.isEmpty()) {
      return ENV_VARS;
    }

    ENV_VARS.put("HOME", HOME.getAbsolutePath());
    ENV_VARS.put("ANDROID_USER_HOME", HOME.getAbsolutePath() + "/.android");
    ENV_VARS.put("TMPDIR", TMP_DIR.getAbsolutePath());
    ENV_VARS.put("LANG", "en_US.UTF-8");
    ENV_VARS.put("LC_ALL", "en_US.UTF-8");

    ENV_VARS.put("SYSROOT", PREFIX.getAbsolutePath());

    ENV_VARS.put("BUSYBOX", BUSYBOX.getAbsolutePath());
    ENV_VARS.put("SHELL", SHELL.getAbsolutePath());
    ENV_VARS.put("CONFIG_SHELL", SHELL.getAbsolutePath());
    ENV_VARS.put("TERM", "screen");
    ENV_VARS.put("PATH", BIN_DIR.getAbsolutePath());

    String ld = System.getenv("LD_LIBRARY_PATH");
    if (ld == null || ld.trim().length() <= 0) {
      ld = "";
    } else {
      ld += File.pathSeparator;
    }
    ld += LIB_DIR.getAbsolutePath();
    ENV_VARS.put("LD_LIBRARY_PATH", ld);

    // https://github.com/termux/termux-tools/blob/f2736f7f8232cd19cf52bca9b0ac9afb8ad9e562/scripts/termux-setup-package-manager.in#L3
    ENV_VARS.put("TERMUX_APP_PACKAGE_MANAGER", "apt");
    ENV_VARS.put("TERMUX_PKG_NO_MIRROR_SELECT", "true");

    addToEnvIfPresent(ENV_VARS, "ANDROID_ART_ROOT");
    addToEnvIfPresent(ENV_VARS, "DEX2OATBOOTCLASSPATH");
    addToEnvIfPresent(ENV_VARS, "ANDROID_I18N_ROOT");
    addToEnvIfPresent(ENV_VARS, "ANDROID_RUNTIME_ROOT");
    addToEnvIfPresent(ENV_VARS, "ANDROID_TZDATA_ROOT");
    addToEnvIfPresent(ENV_VARS, "ANDROID_DATA");
    addToEnvIfPresent(ENV_VARS, "ANDROID_ROOT");

    for (String key : IDE_PROPS.keySet()) {
      if (!blacklistedVariables().contains(key.trim())) {
        ENV_VARS.put(key, readProp(key, ""));
      }
    }

    return ENV_VARS;
  }

  private static List<String> blacklistedVariables() {
    if (blacklist.isEmpty()) {
      blacklist.add("HOME");
      blacklist.add("SYSROOT");
    }
    return blacklist;
  }

  public static String readProp(String key, String defaultValue) {
    String value = IDE_PROPS.getOrDefault(key, defaultValue);
    if (value == null) {
      return defaultValue;
    }
    if (value.contains("$HOME")) {
      value = value.replace("$HOME", HOME.getAbsolutePath());
    }
    if (value.contains("$SYSROOT")) {
      value = value.replace("$SYSROOT", PREFIX.getAbsolutePath());
    }
    if (value.contains("$PATH")) {
      value = value.replace("$PATH", BIN_DIR.getAbsolutePath());
    }
    return value;
  }

  public static void addToEnvIfPresent(Map<String, String> environment, String name) {
    String value = System.getenv(name);
    if (value != null) {
      environment.put(name, value);
    }
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

  public static File getModuleOutputDirectory(File projectRootDirectory, String module) {
    if (module == null) return null;

    String[] modules = module.split(":");
    File modulePath = getBuildDir(projectRootDirectory);
    for (int i = 0; i < modules.length; ++i) {
      if (i == 0) continue;
      if (modulePath == null) {
        modulePath = new File(modules[i]);
      } else {
        modulePath = new File(modulePath, modules[i]);
      }
      if (i != (modules.length - 1)) {
        modulePath = new File(modulePath, FILES);
      }
    }

    return modulePath;
  }

  public static File getJavaDirectory(ModuleModel module, String packageName) {
    if (module.projectRootDirectory != null && module.module != null) {
      File javaDir = new File(module.javaSourceDirectory, FILES);

      String[] packageBreakdown = packageName.split("\\.");
      for (int i = 0; i < packageBreakdown.length; ++i) {
        if (packageBreakdown[i].length() != 0) {
          javaDir = new File(javaDir, packageBreakdown[i]);
          javaDir = new File(javaDir, FILES);
        }
      }
      if (packageName.length() != 0) {
        if (packageBreakdown.length == 0) {
          javaDir = new File(javaDir, packageName);
          javaDir = new File(javaDir, FILES);
        }
      }

      return javaDir;
    }
    return null;
  }

  public static File getJavaOutputDirectory(ModuleModel module, String packageName) {
    if (module.projectRootDirectory != null && module.module != null) {
      File javaDir = module.javaSourceOutputDirectory;

      String[] packageBreakdown = packageName.split(".");
      for (String packagePart : packageBreakdown) {
        javaDir = new File(javaDir, packagePart);
      }

      return javaDir;
    }
    return null;
  }
}
