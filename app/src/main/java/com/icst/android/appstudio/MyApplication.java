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

package com.icst.android.appstudio;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.elfilibustero.uidesigner.AppLoader;
import com.icst.android.appstudio.activities.CrashHandlerActivity;
import com.icst.android.appstudio.models.SettingModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.SettingUtils;
import com.quickersilver.themeengine.ThemeEngine;
import com.quickersilver.themeengine.ThemeMode;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

public class MyApplication extends Application {
	// Social links
	public static final String YOUTUBE = "https://www.youtube.com/@Innovative-CST";
	public static final String DISCORD = "https://discord.com/invite/RM5qaZs4kd";
	public static final String INSTAGRAM = "https://www.instagram.com/innovative_cst";
	public static final String X = "https://x.com/Innovative_cst";
	public static final String GITHUB_APP = "https://github.com/Innovative-CST/AndroidAppStudio";
	public static final String GITHUB_ORG = "https://github.com/Innovative-CST";

	private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
	private static Context mApplicationContext;
	private static ThemeEngine themeEngine;

	public static Context getContext() {
		return mApplicationContext;
	}

	public static ThemeEngine getThemeEngine() {
		return themeEngine;
	}

	@Override
	public void onCreate() {
		mApplicationContext = getApplicationContext();
		themeEngine = ThemeEngine.getInstance(this);
		AppLoader.setContext(getApplicationContext());
		EnvironmentUtils.init(this);

		PRDownloaderConfig config = PRDownloaderConfig.newBuilder().setDatabaseEnabled(true).setConnectTimeout(30_000)
				.build();

		PRDownloader.initialize(getApplicationContext(), config);

		this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

		Thread.setDefaultUncaughtExceptionHandler(
				new Thread.UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread thread, Throwable throwable) {
						Intent intent = new Intent(getApplicationContext(), CrashHandlerActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

						StringBuilder error = new StringBuilder();
						error
								.append("App Version: ")
								.append(BuildConfig.VERSION_NAME)
								.append("\n")
								.append("CommitSHA: ")
								.append(BuildConfig.commitSha)
								.append("\n")
								.append("Build Type: ")
								.append(BuildConfig.BUILD_TYPE)
								.append("\n")
								.append("DeveloperMode: ")
								.append(BuildConfig.isDeveloperMode)
								.append("\n")
								.append("SDK: ")
								.append(Build.VERSION.SDK_INT)
								.append("\n")
								.append("Android: ")
								.append(Build.VERSION.RELEASE)
								.append("\n")
								.append("Model: ")
								.append(Build.VERSION.INCREMENTAL)
								.append("\n")
								.append("Base OS: ")
								.append(Build.VERSION.BASE_OS)
								.append("\n")
								.append("CPU ABI: ")
								.append(Build.CPU_ABI)
								.append("\n")
								.append("CPU ABI2: ")
								.append(Build.CPU_ABI2)
								.append("\n")
								.append("Manufacturer: ")
								.append(Build.MANUFACTURER)
								.append("\n")
								.append("App Storage: ")
								.append(EnvironmentUtils.STORAGE.getAbsolutePath())
								.append("\n")
								.append("Device External Storage: ")
								.append(Environment.getExternalStorageDirectory())
								.append("\n\n")
								.append(Log.getStackTraceString(throwable));

						intent.putExtra("error", error.toString());
						PendingIntent pendingIntent = PendingIntent.getActivity(
								getApplicationContext(), 11111, intent, PendingIntent.FLAG_ONE_SHOT);

						AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, pendingIntent);

						Process.killProcess(Process.myPid());
						System.exit(1);

						uncaughtExceptionHandler.uncaughtException(thread, throwable);
					}
				});

		SettingModel settings = SettingUtils.readSettings(EnvironmentUtils.SETTING_FILE);
		if (settings == null) {
			settings = new SettingModel();
		}
		if (settings.isEnabledDarkMode()) {
			themeEngine.setThemeMode(ThemeMode.DARK);
		} else {
			themeEngine.setThemeMode(ThemeMode.LIGHT);
		}

		themeEngine.setDynamicTheme(settings.isEnabledDynamicTheme());
		ThemeEngine.applyToActivities(this);
		super.onCreate();
	}
}
