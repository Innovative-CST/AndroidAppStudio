package com.tscodeeditor.android.appstudio.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import com.tscodeeditor.android.appstudio.BuildConfig;
import java.io.File;

public final class EnvironmentUtils {
  public static File IDEDIR;
  public static File PROJECTS;

  public static void init(Context context) {
    IDEDIR =
        BuildConfig.isDeveloperMode
            ? new File(Environment.getExternalStorageDirectory(), ".AndroidAppBuilder")
            : new File(getDataDir(context), "files" + File.separator + "home");
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
}
