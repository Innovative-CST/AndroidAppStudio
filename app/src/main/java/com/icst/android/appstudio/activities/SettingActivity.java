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
