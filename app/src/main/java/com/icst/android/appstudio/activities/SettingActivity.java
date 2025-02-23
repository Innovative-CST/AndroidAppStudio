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

import com.icst.android.appstudio.MyApplication;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.databinding.ActivitySettingBinding;
import com.icst.android.appstudio.databinding.LayoutPreferenceBinding;
import com.icst.android.appstudio.databinding.LayoutPreferenceSwitchBinding;
import com.icst.android.appstudio.models.SettingModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.SettingUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;
import com.quickersilver.themeengine.ThemeChooserDialogBuilder;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.viewbinding.ViewBinding;

public class SettingActivity extends BaseActivity {

	private ActivitySettingBinding binding;
	private SettingModel settings;
	private ViewBinding appThemePreference;
	private ViewBinding dynamicThemePreferenceBinding;
	private ViewBinding darkModePreferenceBinding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivitySettingBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		binding.toolbar.setTitle(R.string.settings);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
		settings = SettingUtils.readSettings(EnvironmentUtils.SETTING_FILE);
		if (settings == null) {
			settings = new SettingModel();
		}

		LayoutPreferenceBinding appThemePreference = LayoutPreferenceBinding.inflate(getLayoutInflater());
		appThemePreference.preferenceIcon.setImageResource(R.drawable.ic_palette);
		appThemePreference.primaryText.setText("App Theme");
		appThemePreference.secondaryText.setText(
				"Choose appropriate theme color of app. (Requires Restart)");
		appThemePreference
				.getRoot()
				.setOnClickListener(
						v -> {
							ThemeChooserDialogBuilder dialog = new ThemeChooserDialogBuilder(this);
							dialog.setPositiveButton(
									"OK",
									(position, theme) -> {
										MyApplication.getThemeEngine().setStaticTheme(theme);
									});
							dialog.setNegativeButton("Cancel");
							dialog.setNeutralButton(
									"Default",
									(param1, param2) -> {
										MyApplication.getThemeEngine().resetTheme();
									});
							dialog.create().show();
						});
		binding.content.addView(appThemePreference.getRoot());
		this.appThemePreference = appThemePreference;

		if (settings.isEnabledDynamicTheme()) {
			appThemePreference.getRoot().setVisibility(View.GONE);
		}

		dynamicThemePreferenceBinding = addBooleanPreference(
				"Dynamic Theme",
				"Sets Material 3 Dynamic theme. (Requires Restart)",
				R.drawable.material_design,
				SettingUtils.DYNAMIC_THEME);

		darkModePreferenceBinding = addBooleanPreference(
				"Dark Mode",
				"Choose dark mode if you eye feels comfort. (Overrides App Theme and Requires Restart)",
				R.drawable.ic_light_dark,
				SettingUtils.DARK_MODE);
	}

	private ViewBinding addBooleanPreference(String title, String desc, int icon, String key) {
		LayoutPreferenceSwitchBinding preferenceLayout = LayoutPreferenceSwitchBinding.inflate(getLayoutInflater());
		if (icon == 0) {
			preferenceLayout.preferenceIcon.setVisibility(View.GONE);
		} else {
			preferenceLayout.preferenceIcon.setImageResource(icon);
		}
		preferenceLayout.primaryText.setText(title);
		if (desc == null) {
			preferenceLayout.secondaryText.setVisibility(View.GONE);
		} else {
			preferenceLayout.secondaryText.setText(desc);
		}
		preferenceLayout.check.setChecked(SettingUtils.getBooleanPreference(key, settings));
		preferenceLayout
				.getRoot()
				.setOnClickListener(
						(v) -> {
							preferenceLayout.check.setChecked(!preferenceLayout.check.isChecked());
						});
		preferenceLayout.check.setOnCheckedChangeListener(
				(button, state) -> {
					SettingUtils.setBooleanPreference(key, preferenceLayout.check.isChecked(), settings);
					saveSettings();
					onSettingChange(key);
				});
		binding.content.addView(preferenceLayout.getRoot());
		return preferenceLayout;
	}

	public void onSettingChange(String key) {
		switch (key) {
			case SettingUtils.DYNAMIC_THEME:
				if (settings.isEnabledDynamicTheme()) {
					appThemePreference.getRoot().setVisibility(View.GONE);
				} else {
					appThemePreference.getRoot().setVisibility(View.VISIBLE);
				}
				break;
		}
	}

	public void saveSettings() {
		if (!EnvironmentUtils.SETTING_FILE.getParentFile().exists()) {
			EnvironmentUtils.SETTING_FILE.getParentFile().mkdirs();
		}
		SerializerUtil.serialize(
				settings,
				EnvironmentUtils.SETTING_FILE,
				new SerializerUtil.SerializerCompletionListener() {

					@Override
					public void onSerializeComplete() {
					}

					@Override
					public void onFailedToSerialize(Exception exception) {
						Toast.makeText(SettingActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		binding = null;
	}
}
