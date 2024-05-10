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

package com.tscodeeditor.android.appstudio.dialogs;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tscodeeditor.android.appstudio.activities.BaseActivity;
import com.tscodeeditor.android.appstudio.databinding.DialogProjectBuilderBinding;
import com.tscodeeditor.android.appstudio.helper.ProjectCodeBuildProgress;
import com.tscodeeditor.android.appstudio.exception.ProjectCodeBuildException;
import com.tscodeeditor.android.appstudio.helper.ProjectCodeBuilder;
import com.tscodeeditor.android.appstudio.helper.ProjectCodeBuilderCancelToken;
import com.tscodeeditor.android.appstudio.listener.ProjectCodeBuildListener;
import com.tscodeeditor.android.appstudio.utils.TimeUtils;
import editor.tsd.editors.sora.lang.textmate.provider.TextMateProvider;
import editor.tsd.tools.Themes;
import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.provider.AssetsFileResolver;
import java.io.File;

public class ProjectBuilderDialog extends MaterialAlertDialogBuilder {
  private BaseActivity activity;
  private File outpurDir;
  private File file;
  private ProjectCodeBuildListener listener;
  private ProjectCodeBuilderCancelToken cancelToken;
  private DialogProjectBuilderBinding binding;

  public ProjectBuilderDialog(BaseActivity activity, File outpurDir, File file) {
    super(activity);

    this.activity = activity;
    this.outpurDir = outpurDir;
    this.file = file;

    binding = DialogProjectBuilderBinding.inflate(LayoutInflater.from(activity));

    setView(binding.getRoot());

    FileProviderRegistry.getInstance()
        .addFileProvider(new AssetsFileResolver(activity.getAssets()));
    try {
      TextMateProvider.loadGrammars();
    } catch (Exception e) {
      Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
    }
    var editor = binding.editor;
    editor.setEditable(false);
    editor.setTheme(Themes.SoraEditorTheme.Light.Default);
    binding.indicator.setVisibility(View.GONE);

    StringBuilder log = new StringBuilder();
    listener =
        new ProjectCodeBuildListener() {

          @Override
          public void onBuildStart() {
            try {
              Thread.sleep(1000);
            } catch (Exception e) {

            }
            activity.runOnUiThread(
                () -> {
                  binding.indicator.setVisibility(View.VISIBLE);
                  binding.currentLog.setVisibility(View.VISIBLE);
                  binding.editor.setVisibility(View.GONE);
                });
          }

          @Override
          public void onBuildComplete(long buildTime) {
            activity.runOnUiThread(
                () -> {
                  log.append("\n");
                  log.append(
                      "====== Code generated successfully in "
                          .concat(TimeUtils.convertTime(buildTime))
                          .concat(" ======"));
                  log.append("\n");
                  binding.indicator.setVisibility(View.GONE);
                  binding.currentLog.setVisibility(View.GONE);
                  binding.editor.setVisibility(View.VISIBLE);
                  editor.setText(log.toString());
                });
          }

          @Override
          public void onBuildProgress(ProjectCodeBuildProgress progress) {
            log.append(progress.getMessage());
            log.append("\n");
            activity.runOnUiThread(
                () -> {
                  binding.currentLog.setText(progress.getMessage());
                });
          }

          @Override
          public void onBuildProgressLog(String logMessage) {
            log.append(logMessage);
            log.append("\n");
            activity.runOnUiThread(
                () -> {
                  binding.currentLog.setText(logMessage);
                });
          }

          @Override
          public void onBuildCancelled() {
            activity.runOnUiThread(
                () -> {
                  binding.indicator.setVisibility(View.GONE);
                  binding.currentLog.setVisibility(View.GONE);
                  binding.editor.setVisibility(View.VISIBLE);
                  editor.setText(log.toString());
                });
          }

          @Override
          public void onBuildFailed(ProjectCodeBuildException e) {
            log.append(e.getMessage());
            log.append("\n");
            activity.runOnUiThread(
                () -> {
                  binding.indicator.setVisibility(View.GONE);
                  binding.currentLog.setVisibility(View.GONE);
                  binding.editor.setVisibility(View.VISIBLE);
                  editor.setText(log.toString());
                });
          }
        };
    setCancelable(false);
    setPositiveButton("Cancel", (p1, p2) -> {});
    ProjectCodeBuilder.buildProjectCode(outpurDir, file, activity, listener, cancelToken, true);
  }
}
