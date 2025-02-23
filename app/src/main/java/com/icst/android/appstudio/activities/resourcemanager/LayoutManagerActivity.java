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
import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.BaseActivity;
import com.icst.android.appstudio.adapters.resourcemanager.LayoutManagerAdapter;
import com.icst.android.appstudio.databinding.ActivityLayoutManagerBinding;
import com.icst.android.appstudio.dialogs.resourcemanager.ManageLayoutDialog;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.vieweditor.models.LayoutModel;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

public class LayoutManagerActivity extends BaseActivity {
	// SECTION Constants
	public static final int LAYOUT_SECTION = 0;
	public static final int INFO_SECTION = 1;
	public static final int LOADING_SECTION = 2;

	private ActivityLayoutManagerBinding binding;

	private ModuleModel module;
	private File layoutDirectory;
	private File layoutDirectoryOutput;
	private String layoutDirectoryName;
	private ArrayList<LayoutModel> layoutsList;
	private ArrayList<File> filesList;

	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		binding = ActivityLayoutManagerBinding.inflate(getLayoutInflater());

		setContentView(binding.getRoot());

		binding.toolbar.setTitle(R.string.app_name);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			module = getIntent().getParcelableExtra("module", ModuleModel.class);
		} else {
			module = (ModuleModel) getIntent().getParcelableExtra("module");
		}

		layoutDirectoryName = getIntent().getStringExtra("layoutDirectoryName");
		layoutDirectory = new File(
				new File(
						new File(module.resourceDirectory, EnvironmentUtils.FILES), layoutDirectoryName),
				EnvironmentUtils.FILES);
		layoutDirectoryOutput = new File(module.resourceOutputDirectory, layoutDirectoryName);

		switchSection(LOADING_SECTION);
		loadLayouts();
		binding.fab.setOnClickListener(
				v -> {
					loadLayoutModelsList();
					ManageLayoutDialog createLayoutDialog = new ManageLayoutDialog(
							LayoutManagerActivity.this, layoutsList, filesList, layoutDirectory);
					createLayoutDialog.create().show();
				});
	}

	public void loadLayouts() {
		Executors.newSingleThreadExecutor()
				.execute(
						() -> {
							loadLayoutModelsList();
							runOnUiThread(
									() -> {
										if (layoutsList.size() == 0) {
											setInfo(getString(R.string.no_layouts_yet));
										} else {
											LayoutManagerAdapter layoutsAdapter = new LayoutManagerAdapter(
													LayoutManagerActivity.this,
													layoutsList,
													filesList,
													module,
													layoutDirectoryName);
											binding.layoutList.setAdapter(layoutsAdapter);
											binding.layoutList.setLayoutManager(
													new LinearLayoutManager(LayoutManagerActivity.this));
											switchSection(LAYOUT_SECTION);
										}
									});
						});
	}

	private void loadLayoutModelsList() {
		layoutsList = new ArrayList<LayoutModel>();
		filesList = new ArrayList<File>();

		if (layoutDirectory.exists()) {
			File[] layoutsFilePath = layoutDirectory.listFiles();
			for (int layouts = 0; layouts < layoutsFilePath.length; ++layouts) {
				LayoutModel layout = DeserializerUtils.deserialize(layoutsFilePath[layouts], LayoutModel.class);
				if (layout != null) {
					layoutsList.add(layout);
					filesList.add(layoutsFilePath[layouts]);
				}
			}
		}
	}

	public void switchSection(int section) {
		binding.resourceView.setVisibility(section == LAYOUT_SECTION ? View.VISIBLE : View.GONE);
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
