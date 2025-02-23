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
import java.util.HashMap;

import com.icst.android.appstudio.adapters.FilesListAdapter;
import com.icst.android.appstudio.databinding.ActivityFileManagerBinding;

import android.code.editor.common.utils.FileUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

public class FileManagerActivity extends BaseActivity {
	private ActivityFileManagerBinding binding;
	private File initialDir;
	private File currentDir;
	private ArrayList<String> files = new ArrayList<>();
	private ArrayList<HashMap<String, String>> filesMap = new ArrayList<>();
	private FilesListAdapter filesListAdapter;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		binding = ActivityFileManagerBinding.inflate(getLayoutInflater());
		initialDir = new File(getIntent().getStringExtra("path"));
		currentDir = new File(getIntent().getStringExtra("path"));

		setContentView(binding.getRoot());

		ViewCompat.setOnApplyWindowInsetsListener(
				binding.getRoot(),
				(view, windowInsets) -> {
					Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
					view.setPadding(
							view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), insets.bottom);
					return windowInsets;
				});

		binding.toolbar.setTitle("File Manager");
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

		binding.fab.setOnClickListener(
				(v) -> {
					Intent editor = new Intent(FileManagerActivity.this, CodeEditorActivity.class);
					editor.putExtra("path", currentDir.getAbsolutePath());
					startActivity(editor);
				});

		loadFileList(currentDir);
	}

	public void loadFileList(File path) {
		files.clear();
		filesMap.clear();
		currentDir = path;
		Thread thread = new Thread(
				() -> {
					runOnUiThread(
							() -> {
								binding.progressbar.setVisibility(View.VISIBLE);
							});

					// Get file path from intent and list dir in array
					FileUtils.listDir(path.getAbsolutePath(), files);
					FileUtils.setUpFileList(filesMap, files);

					runOnUiThread(
							() -> {
								// Set Data in list
								binding.progressbar.setVisibility(View.GONE);
								filesListAdapter = new FilesListAdapter(FileManagerActivity.this);
								binding.list.setAdapter(filesListAdapter);
								binding.list.setLayoutManager(
										new LinearLayoutManager(FileManagerActivity.this));
							});
				});

		thread.run();
	}

	public File getCurrentDir() {
		return this.currentDir;
	}

	public void setCurrentDir(File currentDir) {
		this.currentDir = currentDir;
	}

	public ArrayList<HashMap<String, String>> getFilesMap() {
		return this.filesMap;
	}

	public void setFilesMap(ArrayList<HashMap<String, String>> filesMap) {
		this.filesMap = filesMap;
	}

	@Override
	public void onBackPressed() {
		if (initialDir.getAbsolutePath().equals(currentDir.getAbsolutePath())) {
			finish();
		} else {
			loadFileList(currentDir.getParentFile());
		}
	}
}
