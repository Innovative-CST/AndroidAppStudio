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

package com.tscodeeditor.android.appstudio.activities.resourcemanager;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import com.elfilibustero.uidesigner.ui.designer.LayoutDesigner;
import com.tscodeeditor.android.appstudio.dialogs.LayoutSourceViewerDialog;
import com.tscodeeditor.android.appstudio.models.ModuleModel;
import com.tscodeeditor.android.appstudio.utils.EnvironmentUtils;
import com.tscodeeditor.android.appstudio.utils.serialization.DeserializerUtils;
import com.tscodeeditor.android.appstudio.utils.serialization.SerializerUtil;
import com.tscodeeditor.android.appstudio.vieweditor.R;
import com.tscodeeditor.android.appstudio.activities.BaseActivity;
import com.tscodeeditor.android.appstudio.databinding.ActivityLayoutEditorBinding;
import com.tscodeeditor.android.appstudio.vieweditor.editor.ViewEditor;
import com.tscodeeditor.android.appstudio.vieweditor.models.LayoutModel;
import java.io.File;

public class LayoutEditorActivity extends BaseActivity {
  // Contants for showing the section easily
  public static final int LOADING_SECTION = 0;
  public static final int EDITOR_SECTION = 1;
  public static final int ERROR_SECTION = 2;

  private ActivityLayoutEditorBinding binding;
  private ViewEditor editor;
  private ModuleModel module;
  private LayoutModel layout;
  private File layoutDirectory;
  private File layoutFileDirectory;
  private File layoutDirectoryOutput;

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      module = getIntent().getParcelableExtra("module", ModuleModel.class);
    } else {
      module = (ModuleModel) getIntent().getParcelableExtra("module");
    }

    String layoutDirectoryName = getIntent().getStringExtra("layoutDirectoryName");
    String layoutFile = getIntent().getStringExtra("layoutFileName");
    layoutDirectory =
        new File(
            new File(
                new File(module.resourceDirectory, EnvironmentUtils.FILES), layoutDirectoryName),
            EnvironmentUtils.FILES);
    layoutFileDirectory = new File(layoutFile);
    layoutDirectoryOutput = new File(module.resourceOutputDirectory, layoutDirectoryName);

    binding = ActivityLayoutEditorBinding.inflate(getLayoutInflater());
    editor = binding.editor;
    layout = DeserializerUtils.deserialize(layoutFileDirectory, LayoutModel.class);

    if (layout == null) {
      Toast.makeText(LayoutEditorActivity.this, layoutFileDirectory.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    } else editor.setLayoutModel(layout);
	
    setContentView(binding.getRoot());
    // SetUp the toolbar
    binding.toolbar.setTitle(R.string.app_name);
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    switchSection(EDITOR_SECTION);
    addMenuProvider(
        new MenuProvider() {
          @Override
          public void onCreateMenu(Menu menu, MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.project, menu);
          }

          @Override
          public boolean onMenuItemSelected(MenuItem menuItem) {
            var id = menuItem.getItemId();
            if (id == R.id.device_size) {
              selectDeviceSize(findViewById(id));
            } else if (id == R.id.source_code) {
              LayoutModel layout = editor.getLayoutModel();
              LayoutSourceViewerDialog dialog =
                  new LayoutSourceViewerDialog(LayoutEditorActivity.this, layout.getCode());
              dialog.show();
              return true;
            }
            return false;
          }
        },
        this,
        Lifecycle.State.RESUMED);
  }

  /*
   * Method for switching the section quickly.
   * All other section will be GONE except the section of which the section code is provided
   */
  public void switchSection(int section) {
    binding.loading.setVisibility(section == LOADING_SECTION ? View.VISIBLE : View.GONE);
    binding.layoutEditorSection.setVisibility(section == EDITOR_SECTION ? View.VISIBLE : View.GONE);
    binding.errorSection.setVisibility(section == ERROR_SECTION ? View.VISIBLE : View.GONE);
  }

  public void showError(String errorText) {
    switchSection(ERROR_SECTION);
    binding.errorText.setText(errorText);
  }

  private void selectDeviceSize(View view) {
    final PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
    popupMenu.inflate(R.menu.menu_size);
    popupMenu.setOnMenuItemClickListener(
        item -> {
          var id = item.getItemId();
          if (id == R.id.device_size_small) {
            editor.setSize(LayoutDesigner.Size.SMALL);
          } else if (id == R.id.device_size_default) {
            editor.setSize(LayoutDesigner.Size.DEFAULT);
          } else if (id == R.id.device_size_large) {
            editor.setSize(LayoutDesigner.Size.LARGE);
          }
          return true;
        });

    popupMenu.show();
  }

  @Override
  @Deprecated
  @MainThread
  @CallSuper
  public void onBackPressed() {
    SerializerUtil.serialize(
        editor.getLayoutModel(),
        layoutFileDirectory,
        new SerializerUtil.SerializerCompletionListener() {
          @Override
          public void onFailedToSerialize(Exception exception) {
            Toast.makeText(LayoutEditorActivity.this, "Failed to save", Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onSerializeComplete() {
            LayoutEditorActivity.super.onBackPressed();
          }
        });
  }
}
