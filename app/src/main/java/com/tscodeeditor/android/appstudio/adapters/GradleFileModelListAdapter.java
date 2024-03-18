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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tscodeeditor.android.appstudio.R;
import com.tscodeeditor.android.appstudio.activities.EventsActivity;
import com.tscodeeditor.android.appstudio.activities.GradleEditorActivity;
import com.tscodeeditor.android.appstudio.block.model.FileModel;
import com.tscodeeditor.android.appstudio.databinding.AdapterFileModelListItemBinding;
import com.tscodeeditor.android.appstudio.utils.EnvironmentUtils;
import com.tscodeeditor.android.appstudio.utils.FileModelUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class GradleFileModelListAdapter
    extends RecyclerView.Adapter<GradleFileModelListAdapter.ViewHolder> {
  private ArrayList<FileModel> fileList;
  private GradleEditorActivity gradleEditorActivity;

  public GradleFileModelListAdapter(
      ArrayList<FileModel> fileList, GradleEditorActivity gradleEditorActivity) {
    this.fileList = fileList;
    this.gradleEditorActivity = gradleEditorActivity;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View v) {
      super(v);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
    AdapterFileModelListItemBinding item =
        AdapterFileModelListItemBinding.inflate(LayoutInflater.from(arg0.getContext()));
    View _v = item.getRoot();
    RecyclerView.LayoutParams _lp =
        new RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    _v.setLayoutParams(_lp);
    return new ViewHolder(_v);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    AdapterFileModelListItemBinding binding = AdapterFileModelListItemBinding.bind(holder.itemView);
    binding.title.setText(fileList.get(position).getName());
    if (fileList.get(position).isFolder()) {
      binding.icon.setImageResource(R.drawable.ic_folder);
      binding
          .getRoot()
          .setOnClickListener(
              v -> {
                Executors.newSingleThreadExecutor()
                    .execute(
                        () -> {
                          gradleEditorActivity.currentDir =
                              new File(
                                  gradleEditorActivity.currentDir,
                                  new File(fileList.get(position).getFileName(), "files")
                                      .getAbsolutePath());

                          ArrayList<FileModel> files =
                              FileModelUtils.getFileModelList(gradleEditorActivity.currentDir);

                          gradleEditorActivity.runOnUiThread(
                              () -> {
                                gradleEditorActivity.binding.list.setAdapter(
                                    new GradleFileModelListAdapter(files, gradleEditorActivity));
                                gradleEditorActivity.binding.list.setLayoutManager(
                                    new LinearLayoutManager(gradleEditorActivity));
                                gradleEditorActivity.switchSection(
                                    GradleEditorActivity.GRADLE_FILE_LIST_SECTION);
                              });
                        });
              });
    } else {
      if (fileList.get(position).getFileExtension() != null) {
        if (fileList.get(position).getFileExtension().equals("gradle")) {
          binding.icon.setImageResource(R.drawable.ic_gradle);
        }
      }

      binding
          .getRoot()
          .setOnClickListener(
              v -> {
                Intent eventsActivity = new Intent(gradleEditorActivity, EventsActivity.class);
                eventsActivity.putExtra(
                    "projectRootDirectory",
                    gradleEditorActivity.projectRootDirectory.getAbsolutePath());
                eventsActivity.putExtra(
                    "fileModelDirectory",
                    new File(gradleEditorActivity.currentDir, fileList.get(position).getName())
                        .getAbsolutePath());
                gradleEditorActivity.startActivity(eventsActivity);
              });
    }
  }

  @Override
  public int getItemCount() {
    return fileList.size();
  }
}
