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

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.databinding.BottomsheetLayoutVariableModelEditorBinding;
import com.icst.android.appstudio.listener.LayoutVariableModelChangeListener;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.FileModelUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.vieweditor.models.LayoutModel;
import com.icst.android.appstudio.vieweditor.models.LayoutVariableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class EditLayoutVariableModelBottomSheet extends BottomSheetDialog {

  private LayoutVariableModel model;
  private ModuleModel module;

  public EditLayoutVariableModelBottomSheet(
      Context context,
      ModuleModel module,
      LayoutVariableModel model,
      LayoutVariableModelChangeListener listener) {
    super(context);
    this.model = model;
    this.module = module;

    BottomsheetLayoutVariableModelEditorBinding binding =
        BottomsheetLayoutVariableModelEditorBinding.inflate(LayoutInflater.from(context));

    setContentView(binding.getRoot());

    ArrayAdapter adapter =
        new ArrayAdapter<String>(
            getContext(), R.layout.autocomplete_adapter_layout_chooser, R.id.variableName);
    ArrayList<String> layoutNames = new ArrayList<String>();
    addLayoutNamesToList(layoutNames, adapter);

    binding.layoutName.setAdapter(adapter);

    if (this.model == null) {
      binding.delete.setText("Cancel");
    }

    binding.delete.setOnClickListener(
        v -> {
          if (this.model != null) {
            listener.onLayoutVariableModelDelete();
          }
          dismiss();
        });
    binding.done.setOnClickListener(
        v -> {
          if (this.model == null) {
            this.model = new LayoutVariableModel();
          }
          this.model.setLayoutName(binding.layoutName.getText().toString());
          this.model.setVariableName(binding.layoutVariableName.getText().toString());
          listener.onLayoutVariableModelUpdate(this.model);
          dismiss();
        });
  }

  public void addLayoutNamesToList(ArrayList<String> layoutNames, ArrayAdapter<String> adapter) {
    File resListDirectory = new File(module.resourceDirectory, EnvironmentUtils.FILES);
    ArrayList<FileModel> resFolders = FileModelUtils.getFileModelList(resListDirectory);

    if (resFolders == null) return;

    for (int position = 0; position < resFolders.size(); ++position) {

      if (Pattern.compile("^layout(?:-[a-zA-Z0-9]+)?$")
          .matcher(resFolders.get(position).getName())
          .matches()) {

        File layoutsDir =
            new File(
                resListDirectory,
                resFolders
                    .get(position)
                    .getName()
                    .concat(File.separator)
                    .concat(EnvironmentUtils.FILES));

        if (!layoutsDir.exists()) return;

        for (File layoutFile : layoutsDir.listFiles()) {
          LayoutModel layoutFileModel =
              DeserializerUtils.deserialize(layoutFile, LayoutModel.class);

          if (layoutFileModel != null) {
            boolean alreadyPresentLayout = false;
            for (int i = 0; i < layoutNames.size(); ++i) {
              if (layoutNames.get(i).equals(layoutFileModel.getLayoutName())) {
                alreadyPresentLayout = true;
              }
            }

            if (!alreadyPresentLayout) {
              layoutNames.add(layoutFileModel.getLayoutName());
              adapter.add(layoutFileModel.getLayoutName());
            }
          }
        }
      }
    }
  }
}
