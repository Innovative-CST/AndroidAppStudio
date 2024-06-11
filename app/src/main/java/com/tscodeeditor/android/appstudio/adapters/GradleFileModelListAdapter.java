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

package com.tscodeeditor.android.appstudio.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.tscodeeditor.android.appstudio.R;
import com.tscodeeditor.android.appstudio.activities.EventsActivity;
import com.tscodeeditor.android.appstudio.activities.JavaFileManagerActivity;
import com.tscodeeditor.android.appstudio.activities.ModulesActivity;
import com.tscodeeditor.android.appstudio.activities.resourcemanager.ResourceManagerActivity;
import com.tscodeeditor.android.appstudio.block.model.FileModel;
import com.tscodeeditor.android.appstudio.databinding.AdapterFileModelListItemBinding;
import com.tscodeeditor.android.appstudio.databinding.LayoutProjectEditorNavigationBinding;
import com.tscodeeditor.android.appstudio.utils.EnvironmentUtils;
import java.io.File;
import java.util.ArrayList;

public class GradleFileModelListAdapter
    extends RecyclerView.Adapter<GradleFileModelListAdapter.ViewHolder> {
  private ArrayList<FileModel> fileList;
  private ModulesActivity modulesActivity;

  public GradleFileModelListAdapter(
      ArrayList<FileModel> fileList, ModulesActivity modulesActivity) {
    this.fileList = fileList;
    this.modulesActivity = modulesActivity;
    for (int i = 0; i < fileList.size(); ++i) {
      FileModel file = fileList.get(i);
      if (file.getName().equals(EnvironmentUtils.SOURCE_DIR) && file.isFolder()) {
        fileList.remove(i);
      }
    }
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View v) {
      super(v);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup arg0, int viewType) {
    if (viewType == 0) {
      AdapterFileModelListItemBinding item =
          AdapterFileModelListItemBinding.inflate(LayoutInflater.from(arg0.getContext()));
      View _v = item.getRoot();
      RecyclerView.LayoutParams _lp =
          new RecyclerView.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      _v.setLayoutParams(_lp);
      return new ViewHolder(_v);
    } else {
      LayoutProjectEditorNavigationBinding item =
          LayoutProjectEditorNavigationBinding.inflate(LayoutInflater.from(arg0.getContext()));
      View _v = item.getRoot();
      RecyclerView.LayoutParams _lp =
          new RecyclerView.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      _v.setLayoutParams(_lp);
      return new ViewHolder(_v);
    }
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int pos) {
    if (getItemViewType(pos) == 0) {
      final int position = pos - getExtraItemCount();
      AdapterFileModelListItemBinding binding =
          AdapterFileModelListItemBinding.bind(holder.itemView);
      binding.title.setText(fileList.get(position).getName());
      if (fileList.get(position).isFolder()) {
        if (fileList.get(position).isAndroidLibrary()
            || fileList.get(position).isAndroidAppModule()) {
          if (fileList.get(position).isAndroidLibrary())
            binding.icon.setImageResource(R.drawable.ic_alpha_l);
          else binding.icon.setImageResource(R.drawable.ic_alpha_a);

        } else binding.icon.setImageResource(R.drawable.ic_folder);

        binding.cardView.setOnClickListener(
            v -> {
              Intent modules = new Intent(modulesActivity, ModulesActivity.class);
              modules.putExtra(
                  "projectRootDirectory", modulesActivity.projectRootDirectory.getAbsolutePath());
              modules.putExtra(
                  "currentDir",
                  new File(
                          modulesActivity.currentDir,
                          new File(
                                  new File(fileList.get(position).getName()),
                                  EnvironmentUtils.FILES)
                              .getAbsolutePath())
                      .getAbsolutePath());
              modules.putExtra(
                  "isInsideModule",
                  fileList.get(position).isAndroidAppModule()
                      || fileList.get(position).isAndroidLibrary());
              modules.putExtra(
                  "outputPath",
                  new File(modulesActivity.outputDir, fileList.get(position).getName())
                      .getAbsolutePath());
              modules.putExtra(
                  "module",
                  modulesActivity.module.concat(":").concat(fileList.get(position).getName()));
              modulesActivity.startActivity(modules);
            });
      } else {
        if (fileList.get(position).getFileExtension() != null) {
          if (fileList.get(position).getFileExtension().equals("gradle")) {
            binding.icon.setImageResource(R.drawable.ic_gradle);
          }
        }

        binding.cardView.setOnClickListener(
            v -> {
              Intent eventsActivity = new Intent(modulesActivity, EventsActivity.class);
              eventsActivity.putExtra(
                  "projectRootDirectory", modulesActivity.projectRootDirectory.getAbsolutePath());
              eventsActivity.putExtra(
                  "fileModelDirectory",
                  new File(
                          new File(modulesActivity.currentDir, fileList.get(position).getName()),
                          EnvironmentUtils.FILE_MODEL)
                      .getAbsolutePath());
              eventsActivity.putExtra("module", modulesActivity.module);
              modulesActivity.startActivity(eventsActivity);
            });
      }
    } else {
      LayoutProjectEditorNavigationBinding binding =
          LayoutProjectEditorNavigationBinding.bind(holder.itemView);
      binding.programEditor.setOnClickListener(
          v -> {
            Intent javaManager = new Intent(modulesActivity, JavaFileManagerActivity.class);
            javaManager.putExtra("module", modulesActivity.module);
            javaManager.putExtra(
                "projectRootDirectory", modulesActivity.projectRootDirectory.getAbsolutePath());
            modulesActivity.startActivity(javaManager);
          });
      binding.resourceEditor.setOnClickListener(
          v -> {
            Intent resourceManager = new Intent(modulesActivity, ResourceManagerActivity.class);
            resourceManager.putExtra("module", modulesActivity.module);
            resourceManager.putExtra(
                "projectRootDirectory", modulesActivity.projectRootDirectory.getAbsolutePath());
            modulesActivity.startActivity(resourceManager);
          });
    }
  }

  @Override
  public int getItemCount() {
    return fileList.size() + getExtraItemCount();
  }

  @Override
  public int getItemViewType(int position) {
    if (getExtraItemCount() == 1) {
      return position == 0 ? 1 : 0;
    } else return 0;
  }

  public int getExtraItemCount() {
    if (modulesActivity.isInsideModule) {
      return 1;
    } else return 0;
  }
}
