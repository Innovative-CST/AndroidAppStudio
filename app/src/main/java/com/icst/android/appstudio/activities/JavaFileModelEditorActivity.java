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

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import java.io.File;

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
