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
import com.icst.android.appstudio.adapters.ProjectListAdapter;
import com.icst.android.appstudio.databinding.ActivityProjectManagerBinding;
import com.icst.android.appstudio.dialogs.BootstrapInstallerDialog;
import com.icst.android.appstudio.models.ProjectModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.PermissionUtils;
import com.icst.android.appstudio.utils.serialization.ProjectModelSerializationUtils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

public class ProjectManagerActivity extends BaseActivity {
	private ActivityProjectManagerBinding binding;

	// Contants for showing the section easily
	public static final int LOADING_SECTION = 0;
	public static final int NO_PROJECTS_YET_SECTION = 1;
	public static final int PROJECT_LIST_SECTION = 2;
	public static final int ERROR_SECTION = 3;

	// Result launcher
	public ActivityResultLauncher<Intent> projectListUpdateActivityResultLauncher;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		// Initializing
		binding = ActivityProjectManagerBinding.inflate(getLayoutInflater());
		projectListUpdateActivityResultLauncher = registerForActivityResult(
				new ActivityResultContracts.StartActivityForResult(),
				new ActivityResultCallback<ActivityResult>() {
					@Override
					public void onActivityResult(ActivityResult result) {
						if (result.getResultCode() == Activity.RESULT_OK) {
							tryToLoadProjects();
						}
					}
				});

		// Set layout of activity
		setContentView(binding.getRoot());

		// SetUp the toolbar
		binding.toolbar.setTitle(R.string.app_name);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, binding.drawerLayout, binding.toolbar, R.string.app_name, R.string.app_name);
		binding.toolbar.setNavigationOnClickListener(
				v -> {
					if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
						binding.drawerLayout.closeDrawer(GravityCompat.START);
					} else {
						binding.drawerLayout.openDrawer(GravityCompat.START);
					}
				});
		binding.drawerLayout.addDrawerListener(toggle);
		toggle.syncState();

		// SetUp the items click events in drawer
		binding.navigationView.setNavigationItemSelectedListener(
				menuItem -> {
					if (menuItem.getItemId() == R.id.source_License) {
						Intent licenseActivity = new Intent();
						licenseActivity.setClass(this, LicenseActivity.class);
						startActivity(licenseActivity);
					}
					if (menuItem.getItemId() == R.id.about_app) {
						Intent appInfo = new Intent();
						appInfo.setClass(this, AboutAppActivity.class);
						startActivity(appInfo);
					}
					if (menuItem.getItemId() == R.id.extensions) {
						Intent extension = new Intent();
						extension.setClass(this, ExtensionsManagerActivity.class);
						startActivity(extension);
					}
					if (menuItem.getItemId() == R.id.terminal) {
						Intent terminal = new Intent();
						terminal.setClass(this, TerminalActivity.class);
						startActivity(terminal);
					}
					if (menuItem.getItemId() == R.id.settings) {
						Intent settings = new Intent(ProjectManagerActivity.this, SettingActivity.class);
						startActivity(settings);
					}
					if (menuItem.getItemId() == R.id.about_team) {
						Intent aboutTeam = new Intent();
						aboutTeam.setClass(this, AboutTeamActivity.class);
						startActivity(aboutTeam);
					}
					if (menuItem.getItemId() == R.id.java_block_programming) {
						Intent javaBlockProgramming = new Intent();
						javaBlockProgramming.setClass(this, JavaBlockProgrammingActivity.class);
						startActivity(javaBlockProgramming);
					}
					if (menuItem.getItemId() == R.id.fileManagerRoot) {
						Intent fileManager = new Intent();
						fileManager.putExtra("path", "/data/data/com.icst.android.appstudio/files");
						fileManager.setClass(this, FileManagerActivity.class);
						startActivity(fileManager);
					}
					if (menuItem.getItemId() == R.id.fileManager) {
						Intent fileManager = new Intent();
						fileManager.putExtra(
								"path", Environment.getExternalStorageDirectory().getAbsolutePath());
						fileManager.setClass(this, FileManagerActivity.class);
						startActivity(fileManager);
					}
					return true;
				});
		/*
		 * Initialize new project click listener.
		 */
		binding.fab.setOnClickListener(
				v -> {
					createProject();
				});
		binding.createNewProject.setOnClickListener(
				v -> {
					createProject();
				});

		// Install Bootstrap files when required...
		final File bash = new File(EnvironmentUtils.BIN_DIR, "bash");
		if (!(EnvironmentUtils.PREFIX.exists()
				&& EnvironmentUtils.PREFIX.isDirectory()
				&& bash.exists()
				&& bash.isFile()
				&& bash.canExecute())) {
			new BootstrapInstallerDialog(
					this,
					new BootstrapInstallerDialog.BootstrapInstallCompletionListener() {
						@Override
						public void onComplete() {
							/*
							 * Ask for storage permission if not granted.
							 * Load projects if storage permission is granted.
							 * Show storage permission denied error when storage permission is denied and
							 * ask to grant storage permission.
							 */
							if (PermissionUtils.isStoagePermissionGranted(ProjectManagerActivity.this)) {
								tryToLoadProjects();
							} else {
								showError(getString(R.string.storage_permission_denied));
								PermissionUtils.showStoragePermissionDialog(ProjectManagerActivity.this);
							}
						}
					});
		} else {
			/*
			 * Ask for storage permission if not granted.
			 * Load projects if storage permission is granted.
			 * Show storage permission denied error when storage permission is denied and
			 * ask to grant storage permission.
			 */
			if (PermissionUtils.isStoagePermissionGranted(this)) {
				tryToLoadProjects();
			} else {
				showError(getString(R.string.storage_permission_denied));
				PermissionUtils.showStoragePermissionDialog(this);
			}
		}
	}

	/*
	 * Method for switching the section quickly.
	 * All other section will be GONE except the section of which the section code
	 * is provided
	 */
	public void switchSection(int section) {
		binding.loading.setVisibility(section == LOADING_SECTION ? View.VISIBLE : View.GONE);
		binding.noProjectsYet.setVisibility(
				section == NO_PROJECTS_YET_SECTION ? View.VISIBLE : View.GONE);
		binding.projectList.setVisibility(section == PROJECT_LIST_SECTION ? View.VISIBLE : View.GONE);
		binding.errorSection.setVisibility(section == ERROR_SECTION ? View.VISIBLE : View.GONE);
	}

	public void showError(String errorText) {
		switchSection(ERROR_SECTION);
		binding.errorText.setText(errorText);
	}

	/*
	 * Method for creating new project.
	 * Only proceed for creating project when storage permission is granted.
	 * Ask for storage permission when not granted.
	 */
	public void createProject() {
		if (!PermissionUtils.isStoagePermissionGranted(this)) {
			/*
			 * Storage permission is not granted.
			 * Requesting for storage permission.
			 * Aborting new project task.
			 */
			PermissionUtils.showStoragePermissionDialog(this);
			return;
		}
		/*
		 * Go to ProjectModelConfigrationActivity to create new project.
		 */

		Intent createNewProject = new Intent();
		createNewProject.setClass(this, ProjectModelConfigrationActivity.class);
		createNewProject.putExtra("isNewProject", true);
		projectListUpdateActivityResultLauncher.launch(createNewProject);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		binding = null;
	}

	public void tryToLoadProjects() {
		/*
		 * Show LOADING_SECTION.
		 * Then load project in a saparate thread
		 */
		switchSection(LOADING_SECTION);
		Executors.newSingleThreadExecutor()
				.execute(
						() -> {
							/*
							 * Check if the PROJECTS directory exists or not.
							 * If not exists then:
							 * - Create PROJECTS directory now.
							 * - Show NO_PROJECTS_YET_SECTION.
							 * - Stop the thread on further execution.
							 */
							if (!EnvironmentUtils.PROJECTS.exists()) {
								EnvironmentUtils.PROJECTS.mkdirs();
								runOnUiThread(
										() -> {
											switchSection(NO_PROJECTS_YET_SECTION);
										});
								return;
							}
							/*
							 * List all the file list available in PROJECTS.
							 * And check all the paths and add ProjectModel to projectModelList when
							 * confirmed that it is a project directory.
							 */
							ArrayList<ProjectModel> projectModelList = new ArrayList<ProjectModel>();
							ArrayList<File> projectModelFileList = new ArrayList<File>();
							for (File file : EnvironmentUtils.PROJECTS.listFiles()) {
								/*
								 * Only check further if the file is a directory.
								 * If file is a file then skip it.
								 */
								if (file.isFile())
									continue;
								/*
								 * Check if the ProjectModel file exists in the directory(file).
								 * If it does not exists then skip it.
								 */
								if (!new File(file, EnvironmentUtils.PROJECT_CONFIGRATION).exists())
									continue;
								/*
								 * Logic to Deserialize the path as it exists as file.
								 * Add ProjectModel to list once Deserialized
								 */
								ProjectModelSerializationUtils.deserialize(
										new File(file, EnvironmentUtils.PROJECT_CONFIGRATION),
										new ProjectModelSerializationUtils.DeserializerListener() {

											@Override
											public void onSuccessfullyDeserialized(ProjectModel mProjectModel) {
												/*
												 * Add the ProjectModel to ArrayList as it is project
												 */
												projectModelList.add(mProjectModel);
												projectModelFileList.add(file);
											}

											@Override
											public void onFailed(int errorCode, Exception e) {
											}
										});
							}
							/*
							 * Updating the UI according to the loaded project.
							 * - Show ProjectList if the number of ProjectModel in projectModelList is
							 * atleast 1.
							 * - Show no projects yet section if there is no ProjectModel in
							 * projectModelList.
							 */
							runOnUiThread(
									() -> {
										switchSection(
												projectModelList.size() > 0
														? PROJECT_LIST_SECTION
														: NO_PROJECTS_YET_SECTION);
										binding.list.setAdapter(
												new ProjectListAdapter(
														projectModelList, projectModelFileList,
														ProjectManagerActivity.this));
										binding.list.setLayoutManager(
												new LinearLayoutManager(ProjectManagerActivity.this));
									});
						});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (PermissionUtils.isStoagePermissionGranted(this)) {
			tryToLoadProjects();
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				PermissionUtils.showRationaleOfStoragePermissionDialog(this);
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int arg0, String[] arg1, int[] arg2) {
		super.onRequestPermissionsResult(arg0, arg1, arg2);
		switch (arg0) {
			case 1:
			case -1:
			case 10:
				boolean isDenied = false;
				for (int position = 0; position < arg2.length; position++) {
					if (arg2[position] == PackageManager.PERMISSION_DENIED) {
						isDenied = true;
						if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(arg1[position])) {
							if (Build.VERSION.SDK_INT >= 23) {
								if (shouldShowRequestPermissionRationale(arg1[position])) {
									PermissionUtils.showRationaleOfStoragePermissionDialog(this);
								} else {
									PermissionUtils.showStoragePermissionDialogForGoToSettings(this);
								}
							}
						}
					}
				}
				if (!isDenied) {
					tryToLoadProjects();
				}
				break;
		}
	}
}
