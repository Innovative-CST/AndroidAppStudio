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

import java.util.HashMap;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.databinding.ActivityAboutTeamBinding;
import com.icst.android.appstudio.utils.TeamMemberDataParser;

import android.code.editor.utils.RequestNetwork;
import android.code.editor.utils.RequestNetworkController;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

public class AboutTeamActivity extends BaseActivity {
	private ActivityAboutTeamBinding binding;

	public static final int SECTION_LOADING = 0;
	public static final int SECTION_MEMBERS_LIST = 1;
	public static final int SECTION_ERROR = 3;

	private String contributorsData;
	private String contributorsAdditionalData;

	private boolean isContributorsDataLoaded;
	private boolean isContributorsAdditionalDataLoaded;

	public static final String contributorsDataUrl = "https://api.github.com/repos/Innovative-CST/AndroidAppStudio/contributors";
	public String contributorsAdditionalDataUrl = "https://raw.githubusercontent.com/Innovative-CST/AndroidAppStudio/main/assets/contributors.json";

	public RequestNetwork contributorsDataReqNetwork;
	public RequestNetwork contributorsAdditionalDataReqNetwork;

	public RequestNetwork.RequestListener contributorsDataReqListener;
	public RequestNetwork.RequestListener contributorsAdditionalDataReqListener;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		binding = ActivityAboutTeamBinding.inflate(getLayoutInflater());

		setContentView(binding.getRoot());

		Toolbar toolbar = findViewById(R.id.toolbar);
		binding.toolbar.setTitle(R.string.about_team);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

		binding.main.setVisibility(View.GONE);
		binding.loading.setVisibility(View.VISIBLE);

		contributorsDataReqNetwork = new RequestNetwork(this);
		contributorsAdditionalDataReqNetwork = new RequestNetwork(this);

		contributorsDataReqListener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(
					String tag, String response, HashMap<String, Object> responseHeaders) {
				isContributorsDataLoaded = true;
				contributorsData = response;
				mergeResponseIfLoaded();
			}

			@Override
			public void onErrorResponse(String tag, String message) {
				handleRequestFailed();
			}
		};

		contributorsAdditionalDataReqListener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(
					String tag, String response, HashMap<String, Object> responseHeaders) {
				isContributorsAdditionalDataLoaded = true;
				contributorsAdditionalData = response;
				mergeResponseIfLoaded();
			}

			@Override
			public void onErrorResponse(String tag, String message) {
				handleRequestFailed();
			}
		};

		fetchTeamData();
	}

	public void fetchTeamData() {
		switchSection(SECTION_LOADING);
		contributorsDataReqNetwork.startRequestNetwork(
				RequestNetworkController.GET,
				contributorsDataUrl,
				"Contributors",
				contributorsDataReqListener);

		contributorsAdditionalDataReqNetwork.startRequestNetwork(
				RequestNetworkController.GET,
				contributorsAdditionalDataUrl,
				"Contributors",
				contributorsAdditionalDataReqListener);
	}

	private void mergeResponseIfLoaded() {
		if (isContributorsDataLoaded && isContributorsAdditionalDataLoaded) {
			binding.loading.setVisibility(View.GONE);
			binding.main.setVisibility(View.VISIBLE);
			binding.list.setAdapter(
					new AboutTeamMemberListAdapter(
							TeamMemberDataParser.getMembers(contributorsData, contributorsAdditionalData),
							AboutTeamActivity.this));
			binding.list.setLayoutManager(new LinearLayoutManager(AboutTeamActivity.this));
		}
	}

	public void switchSection(int section) {
		binding.loading.setVisibility(section == SECTION_LOADING ? View.VISIBLE : View.GONE);
		binding.main.setVisibility(section == SECTION_MEMBERS_LIST ? View.VISIBLE : View.GONE);
		binding.error.setVisibility(section == SECTION_ERROR ? View.VISIBLE : View.GONE);
	}

	public void setError(String error) {
		switchSection(SECTION_ERROR);
		binding.errorText.setText(error);
	}

	public void handleRequestFailed() {
		setError("Failed to load team data");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		binding = null;
	}
}
