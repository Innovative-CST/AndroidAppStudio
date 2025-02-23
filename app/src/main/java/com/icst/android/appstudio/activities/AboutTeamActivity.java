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
