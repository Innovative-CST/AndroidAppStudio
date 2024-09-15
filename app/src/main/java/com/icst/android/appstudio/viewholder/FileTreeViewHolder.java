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

package com.icst.android.appstudio.viewholder;

import android.code.editor.common.utils.FileUtils;
import android.transition.ChangeImageTransform;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.CodeEditorActivity;
import com.icst.android.appstudio.databinding.ViewHolderFileTreeBinding;
import com.icst.android.appstudio.utils.FileIconUtils;
import com.unnamed.b.atv.model.TreeNode;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Executors;

public class FileTreeViewHolder extends TreeNode.BaseNodeViewHolder<File> {
  private CodeEditorActivity activity;
  private ViewHolderFileTreeBinding binding;
  private TreeNode node;

  public FileTreeViewHolder(CodeEditorActivity activity) {
    super(activity);
    this.activity = activity;
  }

  @Override
  public View createNodeView(TreeNode node, File file) {
    this.node = node;
    binding = ViewHolderFileTreeBinding.inflate(activity.getLayoutInflater());
    binding.expandCollapse.setVisibility(file.isFile() ? View.GONE : View.VISIBLE);
    binding.path.setText(FileUtils.getLatSegmentOfFilePath(file.getAbsolutePath()));

    applyPadding(node, 16);

    if (file.isDirectory()) {
      binding.icon.setImageResource(R.drawable.ic_folder);
      updateExpandCollapseIcon(node.isExpanded());
    } else {
      binding.icon.setImageDrawable(FileIconUtils.getFileIcon(file, activity));
    }

    binding
        .getRoot()
        .setOnClickListener(
            v -> {
              if (file.isDirectory()) {
                if (node.isExpanded()) {
                  getTreeView().collapseNode(node);
                  updateExpandCollapseIcon(node.isExpanded());
                } else {
                  Executors.newSingleThreadExecutor()
                      .execute(
                          () -> {
                            activity.runOnUiThread(
                                () -> {
                                  binding.viewFlipper.setDisplayedChild(1);
                                });

                            listDirInNode(node, file);

                            activity.runOnUiThread(
                                () -> {
                                  binding.viewFlipper.setDisplayedChild(0);
                                  getTreeView().expandNode(node);
                                  updateExpandCollapseIcon(node.isExpanded());
                                });
                          });
                }
              } else {
                activity.openInEditorPane(file);
              }
            });

    return binding.getRoot();
  }

  /*
   * Loads the child nodes in paramter @node.
   * Child will be the list of files in folder @file.
   */
  public void listDirInNode(TreeNode node, File file) {
    node.children.clear();
    ArrayList<File> list = new ArrayList<File>();
    for (File dir : file.listFiles()) {
      list.add(dir);
    }
    Collections.sort(list, new FileComparator());
    for (int pos = 0; pos < list.size(); pos++) {
      TreeNode child = new TreeNode(list.get(pos));
      child.setViewHolder(new FileTreeViewHolder(activity));
      node.addChild(child);
    }
  }

  /*
   * Changes the image of node corresponding icon according to state of node.
   */
  public void updateExpandCollapseIcon(boolean isExpanded) {
    TransitionManager.beginDelayedTransition(binding.getRoot(), new ChangeImageTransform());
    binding.expandCollapse.setImageResource(
        isExpanded ? R.drawable.chevron_down : R.drawable.chevron_right);
  }

  /*
   * Applies the padding according to node level.
   * To give it tree like view.
   */
  public LinearLayout applyPadding(final TreeNode node, final int padding) {
    binding
        .getRoot()
        .setPaddingRelative(
            binding.getRoot().getPaddingLeft() + (padding * (node.getLevel() - 1)),
            binding.getRoot().getPaddingTop(),
            binding.getRoot().getPaddingRight(),
            binding.getRoot().getPaddingBottom());
    return binding.getRoot();
  }

  /*
   * A FileComparator class which just put the files in order of abcd....
   * like other file manager with folders first and files at end.
   */
  final class FileComparator implements Comparator<File> {
    public int compare(File f1, File f2) {
      if (f1 == f2) return 0;
      if (f1.isDirectory() && f2.isFile()) return -1;
      if (f1.isFile() && f2.isDirectory()) return 1;
      return f1.getAbsolutePath().compareToIgnoreCase(f2.getAbsolutePath());
    }
  }
}
