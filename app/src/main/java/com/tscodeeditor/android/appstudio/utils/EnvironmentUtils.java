/*
 *  This file is part of Android AppStudio.
 *
 *  Android AppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Android AppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Android AppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

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
  public static final String PROJECT_CONFIGRATION = "ProjectConfig";
  public static final String FILE_MODEL = "FileModel";
  private static final String APP_MODULE_GRADLE =
      "gradle"
          + File.separator
          + "app"
          + File.separator
          + "files"
          + File.separator
          + "build.gradle";

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

  public static File getAppGradleFile(File projectRootDirectory) {
    return new File(projectRootDirectory, APP_MODULE_GRADLE);
  }
}
