/*
 *  This file is part of Android AppStudio.
 *
 *  Android AppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Android AppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Android AppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.tscodeeditor.android.appstudio.activities;

import android.os.Bundle;
import android.view.View;
import com.tscodeeditor.android.appstudio.R;
import com.tscodeeditor.android.appstudio.databinding.ActivityGradleEditorBinding;
import com.tscodeeditor.android.appstudio.models.ProjectModel;
import com.tscodeeditor.android.appstudio.utils.EnvironmentUtils;
import com.tscodeeditor.android.appstudio.utils.builtin.GradleFilesUtils;
import com.tscodeeditor.android.appstudio.utils.serialization.ProjectModelSerializationUtils;
import java.io.File;
import java.util.concurrent.Executors;

public class GradleEditorActivity extends BaseActivity {

  private ActivityGradleEditorBinding binding;

  private File projectRootDirectory;

  private static final int LOADING_SECTION = 0;
  private static final int GRADLE_FILE_LIST_SECTION = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityGradleEditorBinding.inflate(getLayoutInflater());

    setContentView(binding.getRoot());

    // SetUp the toolbar
    binding.toolbar.setTitle(R.string.app_name);
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    projectRootDirectory = new File(getIntent().getStringExtra("projectRootDirectory"));

    switchSection(LOADING_SECTION);

    Executors.newSingleThreadExecutor()
        .execute(
            () -> {
              ProjectModelSerializationUtils.deserialize(
                  new File(projectRootDirectory, EnvironmentUtils.PROJECT_CONFIGRATION),
                  new ProjectModelSerializationUtils.DeserializerListener() {

                    @Override
                    public void onSuccessfullyDeserialized(ProjectModel mProjectModel) {
                      /*
                       * Creates app module gradle file if it doesn't seems to exists
                       */
                      GradleFilesUtils.createGradleFilesIfDoNotExists(projectRootDirectory);
                      runOnUiThread(
                          () -> {
                            switchSection(GRADLE_FILE_LIST_SECTION);
                          });
                    }

                    @Override
                    public void onFailed(int errorCode, Exception e) {
                      finish();
                    }
                  });
            });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }

  private void switchSection(int section) {
    binding.loadingSection.setVisibility(section == LOADING_SECTION ? View.VISIBLE : View.GONE);
    binding.gradleFileListSection.setVisibility(
        section == GRADLE_FILE_LIST_SECTION ? View.VISIBLE : View.GONE);
  }
}
