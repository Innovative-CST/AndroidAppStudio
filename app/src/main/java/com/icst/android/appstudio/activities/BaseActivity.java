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

package com.icst.android.appstudio.activities;

import com.icst.android.appstudio.models.SettingModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.SettingUtils;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
	/*
	 * BaseActivity contains configration for all activities
	 */
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		EdgeToEdge.enable(this);
	}

	public SettingModel getSetting() {
		SettingModel settings = SettingUtils.readSettings(EnvironmentUtils.SETTING_FILE);
		if (settings == null) {
			settings = new SettingModel();
		}
		return settings;
	}
}
