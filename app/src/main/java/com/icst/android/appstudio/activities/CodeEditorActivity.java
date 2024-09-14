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

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.databinding.ActivityCodeEditorBinding;
import com.icst.android.appstudio.viewholder.FileTreeViewHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.unnamed.b.atv.view.TreeNodeWrapperView;
import java.io.File;

public class CodeEditorActivity extends BaseActivity {
  private File directory;
  private TreeNode root;
  private ActivityCodeEditorBinding binding;

  public static final int NO_WORKSPACE = 0;
  public static final int WORKSPACE = 1;

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);

    binding = ActivityCodeEditorBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    binding.toolbar.setTitle("Android AppStudio");
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

    /*
     * Info:
     * CodeEditorActivity may be requested to open a files or a folder.
     *
     * How it handles folder?
     *
     * If folder is opened then the folder will path fill be assigned to `directory` variable.
     * File tree of folder will loaded in drawer.
     *
     * How it handles file?
     *
     * If a file is opened then it must be in folder. So open the file in workspace (pane).
     * And assign directory variable to parent folder(e.g. folder in which file is stored) of opened file.
     */
    String path = getIntent().getStringExtra("path");
    if (new File(path).exists()) {
      if (new File(path).isFile()) {
        directory = new File(path).getParentFile();
        openInEditorPane(new File(path));
      } else {
        directory = new File(path);
        switchSection(NO_WORKSPACE);
      }
    } else {
      Toast.makeText(this, "Could not open directory.", Toast.LENGTH_SHORT);
      finish();
    }

    /*
     * Add Menu to navigationRail as following:
     * 1. File Tree
     * Displays the file tree of directory opened in editor.
     *
     * 2. Curretly opened panes: TODO
     * Displays the unfinished editor pane or terminal pane.
     */
    Menu menu = binding.navigationRail.getMenu();
    MenuItem fileTree = menu.add(Menu.NONE, 0, Menu.NONE, "");
    fileTree.setIcon(R.drawable.ic_folder);

    MenuItem workspace = menu.add(Menu.NONE, 1, Menu.NONE, "");
    workspace.setIcon(R.drawable.ic_edit);

    binding.navigationRail.setOnItemSelectedListener(
        (menuItem) -> {
          int id = menuItem.getItemId();
          if (id == 0) {
            binding.fileTreeSection.setVisibility(View.VISIBLE);
            binding.workSpacesListSection.setVisibility(View.GONE);
          } else if (id == 1) {
            binding.fileTreeSection.setVisibility(View.GONE);
            binding.workSpacesListSection.setVisibility(View.VISIBLE);
          }
          return true;
        });

    // Load file tree of directory
    loadFileList();
  }

  public void loadFileTree() {
    // Assign root for file tree
    root = TreeNode.root();

    // Add root folder to start tree structure
    TreeNode child = new TreeNode(directory);
    child.setViewHolder(new FileTreeViewHolder(this));
    root.addChild(child);
    /*
     * Add Tree view
     * Attach it with root(TreeNode)
     */
    AndroidTreeView tView = new AndroidTreeView(this, root);
    tView.setDefaultAnimation(true);
    TreeNodeWrapperView treeView =
        new TreeNodeWrapperView(this, com.unnamed.b.atv.R.style.TreeNodeStyle);
    treeView.getNodeContainer().addView(tView.getView());

    // Add File Tree Widget to drawer
    binding.fileTreeContainer.addView(treeView);
  }

  /*
   * Method for switching the section quickly.
   * All other section will be GONE except the section of which the section code is provided
   */
  public void switchSection(int section) {
    binding.emptyWorkspace.setVisibility(section == NO_WORKSPACE ? View.VISIBLE : View.GONE);
    binding.workspace.setVisibility(section == WORKSPACE ? View.VISIBLE : View.GONE);
  }

  public void openInEditorPane(File file) {
    switchSection(WORKSPACE);
    binding.progressbar.setVisibility(View.VISIBLE);

    // TODO: Open file in editor pane

    binding.progressbar.setVisibility(View.GONE);
  }
}
