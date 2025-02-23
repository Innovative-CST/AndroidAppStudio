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
import com.icst.android.appstudio.adapters.JavaFileManagerAdpater;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.model.JavaFileModel;
import com.icst.android.appstudio.databinding.ActivityJavaFileManagerBinding;
import com.icst.android.appstudio.dialogs.JavaFileManagerDialog;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

public class JavaFileManagerActivity extends BaseActivity {
	// SECTION Constants
	public static final int FILES_SECTION = 0;
	public static final int INFO_SECTION = 1;
	public static final int LOADING_SECTION = 2;

	private ActivityJavaFileManagerBinding binding;

	private ModuleModel module;
	private String packageName;
	private ArrayList<FileModel> folderList;
	private ArrayList<JavaFileModel> javaFilesList;
	private ArrayList<File> pathList;

	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		binding = ActivityJavaFileManagerBinding.inflate(getLayoutInflater());

		setContentView(binding.getRoot());

		binding.toolbar.setTitle(R.string.app_name);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		if (getIntent().hasExtra("projectRootDirectory")) {
			module = new ModuleModel();
			module.init(
					getIntent().getStringExtra("module"),
					new File(getIntent().getStringExtra("projectRootDirectory")));
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				module = getIntent().getParcelableExtra("module", ModuleModel.class);
			} else {
				module = (ModuleModel) getIntent().getParcelableExtra("module");
			}
		}

		if (getIntent().hasExtra("packageName")) {
			packageName = getIntent().getStringExtra("packageName");
		} else {
			packageName = new String("");
		}
		loadFiles();
		binding.fab.setOnClickListener(
				v -> {
					JavaFileManagerDialog dialog = new JavaFileManagerDialog(
							JavaFileManagerActivity.this,
							folderList,
							javaFilesList,
							pathList,
							module,
							packageName);
					dialog.show();
				});
	}

	public void loadFiles() {
		switchSection(LOADING_SECTION);
		Executors.newSingleThreadExecutor()
				.execute(
						() -> {
							loadFilesList();
							runOnUiThread(
									() -> {
										JavaFileManagerAdpater adapter = new JavaFileManagerAdpater(
												JavaFileManagerActivity.this,
												folderList,
												javaFilesList,
												pathList,
												module,
												packageName);
										binding.filesList.setAdapter(adapter);
										binding.filesList.setLayoutManager(
												new LinearLayoutManager(JavaFileManagerActivity.this));
										switchSection(FILES_SECTION);
									});
						});
	}

	private void loadFilesList() {
		pathList = new ArrayList<File>();
		File javaFilesDir = EnvironmentUtils.getJavaDirectory(module, packageName);

		javaFilesList = new ArrayList<JavaFileModel>();
		folderList = new ArrayList<FileModel>();
		if (!javaFilesDir.exists())
			return;
		for (File file : javaFilesDir.listFiles()) {
			if (file.isFile())
				continue;

			if (!new File(file, EnvironmentUtils.JAVA_FILE_MODEL).exists())
				continue;

			DeserializerUtils.deserialize(
					new File(file, EnvironmentUtils.JAVA_FILE_MODEL),
					new DeserializerUtils.DeserializerListener() {

						@Override
						public void onSuccessfullyDeserialized(Object object) {
							if (object instanceof JavaFileModel) {
								javaFilesList.add((JavaFileModel) object);
								pathList.add(file);
							}
						}

						@Override
						public void onFailed(int errorCode, Exception e) {
						}
					});
		}

		for (File file : javaFilesDir.listFiles()) {
			if (file.isFile())
				continue;

			if (!new File(file, EnvironmentUtils.FILE_MODEL).exists())
				continue;

			DeserializerUtils.deserialize(
					new File(file, EnvironmentUtils.FILE_MODEL),
					new DeserializerUtils.DeserializerListener() {

						@Override
						public void onSuccessfullyDeserialized(Object object) {
							if (object instanceof FileModel) {
								folderList.add((FileModel) object);
								pathList.add(file);
							}
						}

						@Override
						public void onFailed(int errorCode, Exception e) {
						}
					});
		}
	}

	public void switchSection(int section) {
		binding.resourceView.setVisibility(section == FILES_SECTION ? View.VISIBLE : View.GONE);
		binding.fab.setVisibility(section != LOADING_SECTION ? View.VISIBLE : View.GONE);
		binding.infoSection.setVisibility(section == INFO_SECTION ? View.VISIBLE : View.GONE);
		binding.loading.setVisibility(section == LOADING_SECTION ? View.VISIBLE : View.GONE);
	}

	public void setInfo(String error) {
		switchSection(INFO_SECTION);
		binding.infoText.setText(error);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		binding = null;
	}
}
