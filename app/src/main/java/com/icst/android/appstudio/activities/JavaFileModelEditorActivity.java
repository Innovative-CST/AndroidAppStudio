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

import com.google.android.material.tabs.TabLayoutMediator;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.adapters.JavaFileModelEditorTabAdapter;
import com.icst.android.appstudio.block.model.JavaFileModel;
import com.icst.android.appstudio.databinding.ActivityJavaFileModelEditorBinding;
import com.icst.android.appstudio.dialogs.SourceCodeViewerDialog;
import com.icst.android.appstudio.helper.FileModelCodeHelper;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class JavaFileModelEditorActivity extends BaseActivity {

	private ActivityJavaFileModelEditorBinding binding;
	private ModuleModel module;
	private String packageName;
	private String fileName;
	private JavaFileModel fileModel;
	private File variablesFile;
	private File staticVariablesFile;
	private MenuItem showSourceCode;

	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityJavaFileModelEditorBinding.inflate(getLayoutInflater());

		setContentView(binding.getRoot());

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			module = getIntent().getParcelableExtra("module", ModuleModel.class);
		} else {
			module = (ModuleModel) getIntent().getParcelableExtra("module");
		}
		packageName = getIntent().getStringExtra("packageName");
		fileName = getIntent().getStringExtra("fileName");

		binding.toolbar.setTitle(R.string.app_name);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
		binding.viewpager.setAdapter(
				new JavaFileModelEditorTabAdapter(this, module, packageName, fileName, false));
		new TabLayoutMediator(
				binding.tab,
				binding.viewpager,
				(tab, position) -> {
					if (position == 0) {
						tab.setText("Variables");
					} else if (position == 1) {
						tab.setText("Events");
					}
				})
				.attach();
		fileModel = DeserializerUtils.deserialize(
				new File(
						new File(
								EnvironmentUtils.getJavaDirectory(module, packageName),
								fileName.concat(".java")),
						EnvironmentUtils.JAVA_FILE_MODEL),
				JavaFileModel.class);
		variablesFile = new File(
				new File(
						EnvironmentUtils.getJavaDirectory(module, packageName), fileName.concat(".java")),
				EnvironmentUtils.VARIABLES);
		staticVariablesFile = new File(
				new File(
						EnvironmentUtils.getJavaDirectory(module, packageName), fileName.concat(".java")),
				EnvironmentUtils.STATIC_VARIABLES);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		binding = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_show_code, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		showSourceCode = menu.findItem(R.id.show_source_code);
		if (fileModel == null) {
			if (showSourceCode != null) {
				showSourceCode.setVisible(false);
			}
		} else {
			if (showSourceCode != null) {
				showSourceCode.setVisible(true);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		if (menuItem.getItemId() == R.id.show_source_code) {
			if (fileModel != null) {
				FileModelCodeHelper helper = new FileModelCodeHelper();
				helper.setFileModel(fileModel);
				helper.setEventsDirectory(
						new File(
								new File(
										EnvironmentUtils.getJavaDirectory(module, packageName),
										fileName.concat(".java")),
								EnvironmentUtils.EVENTS_DIR));
				helper.setModule(module);
				helper.setProjectRootDirectory(module.projectRootDirectory);
				SourceCodeViewerDialog sourceCodeDialog = new SourceCodeViewerDialog(
						this, fileModel, helper.getCode(packageName, variablesFile, staticVariablesFile));
				sourceCodeDialog.create().show();
			}
		}

		return super.onOptionsItemSelected(menuItem);
	}
}
