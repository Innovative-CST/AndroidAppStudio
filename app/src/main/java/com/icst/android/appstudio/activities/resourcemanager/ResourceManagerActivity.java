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

package com.icst.android.appstudio.activities.resourcemanager;

import java.io.File;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.BaseActivity;
import com.icst.android.appstudio.adapters.resourcemanager.ResourceManagerAdapter;
import com.icst.android.appstudio.databinding.ActivityResourceManagerBinding;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.models.ProjectModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.FileModelUtils;
import com.icst.android.appstudio.utils.serialization.ProjectModelSerializationUtils;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ResourceManagerActivity extends BaseActivity {
	// SECTION Constants
	public static final int RESOURCES_SECTION = 0;
	public static final int INFO_SECTION = 1;
	public static final int LOADING_SECTION = 2;

	private ActivityResourceManagerBinding binding;

	private ModuleModel module;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityResourceManagerBinding.inflate(getLayoutInflater());

		setContentView(binding.getRoot());

		binding.toolbar.setTitle(R.string.app_name);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		module = new ModuleModel();
		module.init(
				getIntent().getStringExtra("module"),
				new File(getIntent().getStringExtra("projectRootDirectory")));
		switchSection(LOADING_SECTION);

		ProjectModelSerializationUtils.deserialize(
				new File(module.projectRootDirectory, EnvironmentUtils.PROJECT_CONFIGRATION),
				new ProjectModelSerializationUtils.DeserializerListener() {

					@Override
					public void onSuccessfullyDeserialized(ProjectModel object) {
						switchSection(RESOURCES_SECTION);
						binding.resList.setAdapter(
								new ResourceManagerAdapter(
										FileModelUtils.getFileModelList(
												new File(module.resourceDirectory, EnvironmentUtils.FILES)),
										ResourceManagerActivity.this,
										module));
						binding.resList.setLayoutManager(new LinearLayoutManager(ResourceManagerActivity.this));
					}

					@Override
					public void onFailed(int errorCode, Exception e) {
						setError(e.getMessage());
					}
				});
	}

	public void switchSection(int section) {
		binding.resourceView.setVisibility(section == RESOURCES_SECTION ? View.VISIBLE : View.GONE);
		binding.infoSection.setVisibility(section == INFO_SECTION ? View.VISIBLE : View.GONE);
		binding.loading.setVisibility(section == LOADING_SECTION ? View.VISIBLE : View.GONE);
	}

	public void setError(String error) {
		switchSection(INFO_SECTION);
		binding.infoText.setText(error);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		binding = null;
	}
}
