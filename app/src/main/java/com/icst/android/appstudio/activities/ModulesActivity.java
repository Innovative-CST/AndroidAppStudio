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

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.adapters.GradleFileModelListAdapter;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.databinding.ActivityModulesBinding;
import com.icst.android.appstudio.dialogs.ProjectBuilderDialog;
import com.icst.android.appstudio.models.ProjectModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.FileModelUtils;
import com.icst.android.appstudio.utils.GradleFileUtils;
import com.icst.android.appstudio.utils.serialization.ProjectModelSerializationUtils;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ModulesActivity extends BaseActivity {
	public ActivityModulesBinding binding;

	public File projectRootDirectory;
	public File currentDir;
	public File outputDir;
	public String module;

	public static final int LOADING_SECTION = 0;
	public static final int GRADLE_FILE_LIST_SECTION = 1;
	public boolean isInsideModule = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityModulesBinding.inflate(getLayoutInflater());

		setContentView(binding.getRoot());

		// SetUp the toolbar
		binding.toolbar.setTitle(R.string.app_name);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		projectRootDirectory = new File(getIntent().getStringExtra("projectRootDirectory"));
		currentDir = new File(getIntent().getStringExtra("currentDir"));
		isInsideModule = getIntent().getBooleanExtra("isInsideModule", false);

		if (getIntent().hasExtra("module")) {
			module = getIntent().getStringExtra("module");
		} else {
			module = "";
		}

		if (getIntent().hasExtra("outputPath")) {
			outputDir = new File(getIntent().getStringExtra("outputPath"));
		} else {
			outputDir = EnvironmentUtils.getBuildDir(projectRootDirectory);
		}

		switchSection(LOADING_SECTION);

		/*
		 * Creates app module gradle file if it doesn't seems to exists
		 */
		GradleFileUtils.createGradleFilesIfDoNotExists(projectRootDirectory);

		ProjectModelSerializationUtils.deserialize(
				new File(projectRootDirectory, EnvironmentUtils.PROJECT_CONFIGRATION),
				new ProjectModelSerializationUtils.DeserializerListener() {

					@Override
					public void onSuccessfullyDeserialized(ProjectModel mProjectModel) {
						Executors.newSingleThreadExecutor()
								.execute(
										() -> {
											ArrayList<FileModel> fileList = FileModelUtils.getFileModelList(currentDir);

											runOnUiThread(
													() -> {
														binding.list.setAdapter(
																new GradleFileModelListAdapter(fileList,
																		ModulesActivity.this));
														binding.list.setLayoutManager(
																new LinearLayoutManager(ModulesActivity.this));
														switchSection(GRADLE_FILE_LIST_SECTION);
													});
										});
					}

					@Override
					public void onFailed(int errorCode, Exception e) {
						finish();
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		binding = null;
	}

	public void switchSection(int section) {
		binding.loadingSection.setVisibility(section == LOADING_SECTION ? View.VISIBLE : View.GONE);
		binding.gradleFileListSection.setVisibility(
				section == GRADLE_FILE_LIST_SECTION ? View.VISIBLE : View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_modules_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		if (menuItem.getItemId() == R.id.run) {
			ProjectBuilderDialog buildDialog = new ProjectBuilderDialog(this, projectRootDirectory, module);
			buildDialog.create().show();
		}
		return super.onOptionsItemSelected(menuItem);
	}
}
