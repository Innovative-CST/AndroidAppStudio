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

package com.tscodeeditor.android.appstudio.activities;

import android.os.Build;
import android.os.Bundle;
import com.tscodeeditor.android.appstudio.R;
import com.tscodeeditor.android.appstudio.block.model.Event;
import com.tscodeeditor.android.appstudio.builtin.blocks.GradleDepedencyBlocks;
import com.tscodeeditor.android.appstudio.databinding.ActivityEventEditorBinding;
import com.tscodeeditor.android.appstudio.models.ModuleModel;
import com.tscodeeditor.android.appstudio.models.ProjectModel;
import com.tscodeeditor.android.appstudio.models.SettingModel;
import com.tscodeeditor.android.appstudio.utils.EnvironmentUtils;
import com.tscodeeditor.android.appstudio.utils.SettingUtils;
import com.tscodeeditor.android.appstudio.utils.serialization.DeserializerUtils;
import com.tscodeeditor.android.appstudio.utils.serialization.SerializerUtil;
import java.io.File;
import java.util.HashMap;

public class EventEditorActivity extends BaseActivity {
  private ActivityEventEditorBinding binding;

  private ProjectModel projectModel;
  private ModuleModel module;
  /*
   * Contains the location of currently selected file model.
   * For example: /../../Projects/100/../abc/FileModel
   */
  private File fileModelDirectory;
  /*
   * Contains the location of event list path.
   * For example: /../../Projects/100/../../Events/Config
   */
  private File eventListPath;
  /*
   * Contains the location of event file path.
   * For example: /../../Projects/100/../../Events/Config/ActivityBasics
   */
  private File eventFile;
  /*
   * Main Event Object
   */
  private Event event;

  @Override
  @SuppressWarnings("deprecation")
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityEventEditorBinding.inflate(getLayoutInflater());

    // Initialize the files paths

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      module = getIntent().getParcelableExtra("module", ModuleModel.class);
    } else {
      module = (ModuleModel) getIntent().getParcelableExtra("module");
    }

    fileModelDirectory = new File(getIntent().getStringExtra("fileModelDirectory"));
    eventListPath = new File(getIntent().getStringExtra("eventListPath"));
    eventFile = new File(getIntent().getStringExtra("eventFile"));

    setContentView(binding.getRoot());
    // SetUp the toolbar
    binding.toolbar.setTitle(R.string.app_name);
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

    DeserializerUtils.deserialize(
        eventFile,
        new DeserializerUtils.DeserializerListener() {

          @Override
          public void onSuccessfullyDeserialized(Object deserializedObject) {
            if (deserializedObject instanceof Event) {
              event = (Event) deserializedObject;
            }
          }

          @Override
          public void onFailed(int errorCode, Exception e) {}
        });
    DeserializerUtils.deserialize(
        new File(module.projectRootDirectory, EnvironmentUtils.PROJECT_CONFIGRATION),
        new DeserializerUtils.DeserializerListener() {

          @Override
          public void onSuccessfullyDeserialized(Object deserializedObject) {
            if (deserializedObject instanceof ProjectModel) {
              projectModel = (ProjectModel) deserializedObject;
            }
          }

          @Override
          public void onFailed(int errorCode, Exception e) {}
        });

    if (event == null) {
      finish();
      return;
    }
    if (event.getEventTopBlock() != null) {
      HashMap<String, Object> variables = new HashMap<String, Object>();
      if (projectModel != null) {
        variables.put("ProjectModel", projectModel);
      }
      if (event != null) {
        variables.put("Event", event);
      }
      SettingModel settings = SettingUtils.readSettings(EnvironmentUtils.SETTING_FILE);
      if (settings == null) {
        settings = new SettingModel();
      }
      binding.eventEditor.initEditor(event, settings.isEnabledDarkMode(), variables);
    }

    if (event.getName() != null) {
      if (event.getName().equals("dependenciesBlock")) {
        binding.eventEditor.setHolder(GradleDepedencyBlocks.getGradleDepedencyBlocks());
      }
      if (event.getName().equals("androidBlock")) {
        binding.eventEditor.setHolder(GradleDepedencyBlocks.getGradleAndroidBlocks());
      }
    }
  }

  @Override
  protected void onPause() {
    binding.eventEditor.loadBlocksInEvent();
    SerializerUtil.serialize(
        event,
        eventFile,
        new SerializerUtil.SerializerCompletionListener() {

          @Override
          public void onSerializeComplete() {}

          @Override
          public void onFailedToSerialize(Exception exception) {}
        });
    super.onPause();
  }
}
