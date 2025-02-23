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

import java.io.File;

import com.icst.android.appstudio.models.SettingModel;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;

public final class SettingUtils {
	public static final String DARK_MODE = "darkMode";
	public static final String DYNAMIC_THEME = "DynamicTheme";

	public static SettingModel readSettings(File file) {
		return DeserializerUtils.deserialize(file, SettingModel.class);
	}

	public static boolean getBooleanPreference(String key, SettingModel setting) {
		switch (key) {
			case DARK_MODE:
				return setting.isEnabledDarkMode();
			case DYNAMIC_THEME:
				return setting.isEnabledDynamicTheme();
		}
		return false;
	}

	public static void setBooleanPreference(String key, boolean value, SettingModel setting) {
		switch (key) {
			case DARK_MODE:
				setting.setEnableDarkMode(value);
				break;
			case DYNAMIC_THEME:
				setting.setEnabledDynamicTheme(value);
				break;
		}
	}
}
