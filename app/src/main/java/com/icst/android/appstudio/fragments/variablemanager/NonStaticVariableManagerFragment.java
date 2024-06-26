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

package com.icst.android.appstudio.fragments.variablemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.icst.android.appstudio.adapters.VariableListAdapter;
import com.icst.android.appstudio.block.dialog.variables.ChooseVariablesDialog;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.model.VariableModel;
import com.icst.android.appstudio.databinding.FragmentNonStaticVariableBinding;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.VariablesUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import java.io.File;
import java.util.ArrayList;

public class NonStaticVariableManagerFragment extends Fragment {
  private FragmentNonStaticVariableBinding binding;
  private ModuleModel module;
  private String packageName;
  private String className;
  private FileModel file;
  private ArrayList<VariableModel> variables;

  private static final int LOADING_SECTION = 0;
  private static final int LIST_SECTION = 1;
  private static final int INFO_SECTION = 2;

  public NonStaticVariableManagerFragment(
      ModuleModel module, String packageName, String className) {
    this.module = module;
    this.packageName = packageName;
    this.className = className;
    file =
        DeserializerUtils.deserialize(
            new File(
                new File(
                    EnvironmentUtils.getJavaDirectory(module, packageName),
                    className.concat(".java")),
                EnvironmentUtils.JAVA_FILE_MODEL),
            FileModel.class);
    variables =
        DeserializerUtils.deserialize(
            new File(
                new File(
                    EnvironmentUtils.getJavaDirectory(module, packageName),
                    className.concat(".java")),
                EnvironmentUtils.VARIABLES),
            ArrayList.class);

    if (variables == null) {
      variables = new ArrayList<VariableModel>();
    }
  }

  @Override
  @MainThread
  @Nullable
  public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle bundle) {
    binding = FragmentNonStaticVariableBinding.inflate(inflator);
    loadList();
    binding.fab.setOnClickListener(
        v -> {
          ChooseVariablesDialog dialog =
              new ChooseVariablesDialog(getContext(), VariablesUtils.getAllVariables(file)) {
                @Override
                public void onSelectedVariable(VariableModel selectedVariable) {
                  // Create Variable add dialog
                }
              };
        });

    return binding.getRoot();
  }

  public void loadList() {
    variables =
        DeserializerUtils.deserialize(
            new File(
                new File(
                    EnvironmentUtils.getJavaDirectory(module, packageName),
                    className.concat(".java")),
                EnvironmentUtils.VARIABLES),
            ArrayList.class);

    if (variables == null) {
      variables = new ArrayList<VariableModel>();
    }
    if (variables.size() > 0) {
      binding.list.setAdapter(new VariableListAdapter(variables, this));
      binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
      switchSection(LIST_SECTION);
    } else {
      switchSection(INFO_SECTION);
      binding.info.setText("No variables yet");
    }
  }

  private void switchSection(int section) {
    binding.loadingSection.setVisibility(LOADING_SECTION == section ? View.VISIBLE : View.GONE);
    binding.listSection.setVisibility(LIST_SECTION == section ? View.VISIBLE : View.GONE);
    binding.infoSection.setVisibility(INFO_SECTION == section ? View.VISIBLE : View.GONE);
  }

  private void showInfo(int info) {
    switchSection(INFO_SECTION);
    binding.info.setText(info);
  }
}
