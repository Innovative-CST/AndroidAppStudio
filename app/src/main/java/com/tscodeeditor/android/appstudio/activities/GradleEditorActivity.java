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
import com.tscodeeditor.android.appstudio.R;
import com.tscodeeditor.android.appstudio.databinding.ActivityGradleEditorBinding;
import com.tscodeeditor.android.appstudio.models.ProjectModel;
import com.tscodeeditor.android.appstudio.utils.EnvironmentUtils;
import com.tscodeeditor.android.appstudio.utils.builtin.GradleFilesInitializer;
import com.tscodeeditor.android.appstudio.utils.serialization.ProjectModelSerializationUtils;
import com.tscodeeditor.android.appstudio.utils.serialization.SerializerUtil;
import java.io.File;

public class GradleEditorActivity extends BaseActivity {

  private ActivityGradleEditorBinding binding;

  private File projectRootDirectory;

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

    ProjectModelSerializationUtils.deserialize(
        new File(projectRootDirectory, EnvironmentUtils.PROJECT_CONFIGRATION),
        new ProjectModelSerializationUtils.DeserializerListener() {

          @Override
          public void onSuccessfullyDeserialized(ProjectModel mProjectModel) {
            /*
             * Creates app module gradle file if it doesn't seems to exists
             */
            if (!EnvironmentUtils.getAppGradleFile(projectRootDirectory).exists()) {
              if (!EnvironmentUtils.getAppGradleFile(projectRootDirectory)
                  .getParentFile()
                  .exists()) {
                EnvironmentUtils.getAppGradleFile(projectRootDirectory).getParentFile().mkdirs();
                SerializerUtil.serialize(
                    GradleFilesInitializer.getAppModuleGradleFileModule(mProjectModel),
                    EnvironmentUtils.getAppGradleFile(projectRootDirectory),
                    new SerializerUtil.SerializerCompletionListener() {

                      @Override
                      public void onSerializeComplete() {}

                      @Override
                      public void onFailedToSerialize(Exception exception) {}
                    });
              }
            }
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
}
