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

import android.code.editor.common.utils.FileUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.icst.android.appstudio.adapters.FilesListAdapter;
import com.icst.android.appstudio.databinding.ActivityFileManagerBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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
    Thread thread =
        new Thread(
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
