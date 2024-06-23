/*
 * This file is part of Android AppStudio [https://github.com/TS-Code-Editor/AndroidAppStudio].
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

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;

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
                                new GradleFileModelListAdapter(fileList, ModulesActivity.this));
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
      ProjectBuilderDialog buildDialog =
          new ProjectBuilderDialog(this, projectRootDirectory, module);
      buildDialog.create().show();
    }
    return super.onOptionsItemSelected(menuItem);
  }
}
