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

package com.icst.android.appstudio.bottomsheet;

import android.code.editor.common.interfaces.FileDeleteListener;
import android.code.editor.common.utils.FileDeleteUtils;
import android.content.Intent;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.ProjectManagerActivity;
import com.icst.android.appstudio.activities.ProjectModelConfigrationActivity;
import com.icst.android.appstudio.databinding.BottomsheetProjectOptionBinding;
import java.io.File;

public class ProjectOptionsSheet extends BottomSheetDialog {
  private BottomsheetProjectOptionBinding binding;

  public ProjectOptionsSheet(ProjectManagerActivity activity, File projectRootDirectory) {
    super(activity);

    binding = BottomsheetProjectOptionBinding.inflate(activity.getLayoutInflater());

    setContentView(binding.getRoot());
    binding.settings.setOnClickListener(
        v -> {
          Intent modifyProject = new Intent();
          modifyProject.setClass(activity, ProjectModelConfigrationActivity.class);
          modifyProject.putExtra("isNewProject", false);
          modifyProject.putExtra("projectRootDirectory", projectRootDirectory.getAbsolutePath());
          activity.projectListUpdateActivityResultLauncher.launch(modifyProject);
        });
    binding.delete.setOnClickListener(
        v -> {
          MaterialAlertDialogBuilder deleteConfirm = new MaterialAlertDialogBuilder(activity);
          deleteConfirm.setTitle(R.string.warning);
          deleteConfirm.setIcon(R.drawable.ic_alert);
          deleteConfirm.setMessage(R.string.project_delete_warning_text);
          deleteConfirm.setPositiveButton(
              R.string.delete,
              (param1, param2) -> {
                FileDeleteUtils.delete(
                    projectRootDirectory,
                    new FileDeleteListener() {

                      @Override
                      public void onProgressUpdate(int deleteDone) {}

                      @Override
                      public void onTotalCount(int total) {}

                      @Override
                      public void onDeleting(File path) {}

                      @Override
                      public void onDeleteComplete(File path) {}

                      @Override
                      public void onTaskComplete() {
                        ProjectOptionsSheet.this.dismiss();
                        activity.tryToLoadProjects();
                      }
                    },
                    true,
                    activity);
              });

          deleteConfirm.setNegativeButton(R.string.cancel, (param1, param2) -> {});
          deleteConfirm.create().show();
        });
  }
}
