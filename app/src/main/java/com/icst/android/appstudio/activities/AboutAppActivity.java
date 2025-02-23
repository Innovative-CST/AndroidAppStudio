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

import com.icst.android.appstudio.BuildConfig;
import com.icst.android.appstudio.MyApplication;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.databinding.ActivityAboutAppBinding;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class AboutAppActivity extends BaseActivity {

	private ActivityAboutAppBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityAboutAppBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		binding.toolbar.setTitle(R.string.app_name);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

		binding.versionName.setText(BuildConfig.VERSION_NAME);

		binding.versionName.setOnLongClickListener(
				v -> {
					ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					ClipData clip = ClipData.newPlainText("label", BuildConfig.VERSION_NAME);
					clipboard.setPrimaryClip(clip);
					Toast.makeText(AboutAppActivity.this, R.string.version_name_copied, Toast.LENGTH_SHORT)
							.show();
					return true;
				});

		binding.commitSha.setText(BuildConfig.commitSha);

		binding.commitSha.setOnLongClickListener(
				v -> {
					ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					ClipData clip = ClipData.newPlainText("label", BuildConfig.commitSha);
					clipboard.setPrimaryClip(clip);
					Toast.makeText(AboutAppActivity.this, R.string.commit_sha_copied, Toast.LENGTH_SHORT)
							.show();
					return true;
				});

		binding.appName.setOnClickListener(
				v -> {
					Intent appGithub = new Intent();
					appGithub.setAction(Intent.ACTION_VIEW);
					appGithub.setData(Uri.parse(MyApplication.GITHUB_APP));
					startActivity(appGithub);
				});

		binding.orgName.setOnClickListener(
				v -> {
					Intent orgGithub = new Intent();
					orgGithub.setAction(Intent.ACTION_VIEW);
					orgGithub.setData(Uri.parse(MyApplication.GITHUB_ORG));
					startActivity(orgGithub);
				});

		binding.github.setOnClickListener(
				v -> {
					Intent orgGithub = new Intent();
					orgGithub.setAction(Intent.ACTION_VIEW);
					orgGithub.setData(Uri.parse(MyApplication.GITHUB_ORG));
					startActivity(orgGithub);
				});

		binding.discord.setOnClickListener(
				v -> {
					Intent discord = new Intent();
					discord.setAction(Intent.ACTION_VIEW);
					discord.setData(Uri.parse(MyApplication.DISCORD));
					startActivity(discord);
				});

		binding.youtube.setOnClickListener(
				v -> {
					Intent youtube = new Intent();
					youtube.setAction(Intent.ACTION_VIEW);
					youtube.setData(Uri.parse(MyApplication.YOUTUBE));
					startActivity(youtube);
				});

		binding.instagram.setOnClickListener(
				v -> {
					Intent instagram = new Intent();
					instagram.setAction(Intent.ACTION_VIEW);
					instagram.setData(Uri.parse(MyApplication.INSTAGRAM));
					startActivity(instagram);
				});

		binding.x.setOnClickListener(
				v -> {
					Intent x = new Intent();
					x.setAction(Intent.ACTION_VIEW);
					x.setData(Uri.parse(MyApplication.X));
					startActivity(x);
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		binding = null;
	}
}
