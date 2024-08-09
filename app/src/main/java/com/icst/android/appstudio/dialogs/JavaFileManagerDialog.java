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

package com.icst.android.appstudio.dialogs;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.JavaFileManagerActivity;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.model.JavaFileModel;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;
import com.icst.android.appstudio.builtin.filemodels.BuiltInActivityFileModel;
import com.icst.android.appstudio.databinding.DialogJavaManagerBinding;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.FileModelUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaFileManagerDialog extends MaterialAlertDialogBuilder {
  private JavaFileManagerActivity activity;
  private ArrayList<FileModel> folderList;
  private ArrayList<JavaFileModel> javaFilesList;
  private ArrayList<File> pathList;
  private ModuleModel module;
  private String packageName;

  private DialogJavaManagerBinding binding;

  public JavaFileManagerDialog(
      JavaFileManagerActivity activity,
      ArrayList<FileModel> folderList,
      ArrayList<JavaFileModel> javaFilesList,
      ArrayList<File> pathList,
      ModuleModel module,
      String packageName) {
    super(activity);

    this.activity = activity;
    this.folderList = folderList;
    this.javaFilesList = javaFilesList;
    this.pathList = pathList;
    this.module = module;
    this.packageName = packageName;

    binding = DialogJavaManagerBinding.inflate(activity.getLayoutInflater());
    if (packageName.equals("")) {
      binding.activity.setVisibility(View.GONE);
    }
    setView(binding.getRoot());
    binding.fileName.setSingleLine(true);

    binding.fileName.addTextChangedListener(
        new TextWatcher() {

          @Override
          public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

          @Override
          public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            syncError();
          }

          @Override
          public void afterTextChanged(Editable arg0) {}
        });

    binding.fileTypeChooser.addOnButtonCheckedListener(
        (group, id, isChecked) -> {
          syncError();
        });

    setPositiveButton(
        R.string.done,
        (param1, param2) -> {
          if (binding.fileTypeChooser.getCheckedButtonId() == R.id.activity) {
            if (validateClassNameAndExistance(binding.fileName.getText().toString())) {
              binding.fileNameInputLayout.setErrorEnabled(false);
              File classPath =
                  new File(
                      EnvironmentUtils.getJavaDirectory(module, packageName),
                      binding.fileName.getText().toString().concat(".java"));
              classPath.mkdirs();
              new File(classPath, EnvironmentUtils.EVENTS_DIR).mkdirs();
              SerializerUtil.serialize(
                  BuiltInActivityFileModel.getActivityFileModel(
                      binding.fileName.getText().toString()),
                  new File(classPath, EnvironmentUtils.JAVA_FILE_MODEL),
                  new SerializerUtil.SerializerCompletionListener() {

                    @Override
                    public void onSerializeComplete() {
                      activity.loadFiles();
                    }

                    @Override
                    public void onFailedToSerialize(Exception exception) {}
                  });

            } else {
              Toast.makeText(activity, "Please enter another name...", Toast.LENGTH_SHORT).show();
            }
          } else if (binding.fileTypeChooser.getCheckedButtonId() == R.id.folder) {
            String finalPackageName =
                packageName.length() == 0
                    ? binding.fileName.getText().toString()
                    : packageName.concat(".").concat(binding.fileName.getText().toString());
            if (validatePackageNameAndExistance(
                binding.fileName.getText().toString(), finalPackageName)) {
              binding.fileNameInputLayout.setErrorEnabled(false);
              FileModel folder =
                  FileModelUtils.getFolderModel(binding.fileName.getText().toString());
              File packagePath =
                  new File(
                      EnvironmentUtils.getJavaDirectory(module, packageName),
                      binding.fileName.getText().toString());
              packagePath.mkdirs();
              new File(packagePath, EnvironmentUtils.FILES).mkdirs();
              SerializerUtil.serialize(
                  folder,
                  new File(packagePath, EnvironmentUtils.FILE_MODEL),
                  new SerializerUtil.SerializerCompletionListener() {

                    @Override
                    public void onSerializeComplete() {
                      activity.loadFiles();
                    }

                    @Override
                    public void onFailedToSerialize(Exception exception) {}
                  });

            } else {
              Toast.makeText(activity, "Please enter another package name...", Toast.LENGTH_SHORT)
                  .show();
            }
          }
        });
    setNegativeButton(R.string.cancel, (param1, param2) -> {});
  }

  public void syncError() {
    if (binding.fileTypeChooser.getCheckedButtonId() == R.id.activity) {
      if (validateClassNameAndExistance(binding.fileName.getText().toString())) {
        binding.fileNameInputLayout.setErrorEnabled(false);
      } else {
        binding.fileNameInputLayout.setErrorEnabled(true);
        binding.fileNameInputLayout.setError("Please enter another name...");
      }
    } else if (binding.fileTypeChooser.getCheckedButtonId() == R.id.folder) {
      String finalPackageName =
          packageName.length() == 0
              ? binding.fileName.getText().toString()
              : packageName.concat(".").concat(binding.fileName.getText().toString());
      if (validatePackageNameAndExistance(
          binding.fileName.getText().toString(), finalPackageName)) {
        binding.fileNameInputLayout.setErrorEnabled(false);
      } else {
        binding.fileNameInputLayout.setErrorEnabled(true);
        binding.fileNameInputLayout.setError("Please enter another package...");
      }
    }
  }

  public boolean validateClassNameAndExistance(String fileName) {
    for (int files = 0; files < javaFilesList.size(); ++files) {
      if (javaFilesList.get(files).getFileName().equals(fileName)) {
        return false;
      }
    }

    for (int files = 0; files < folderList.size(); ++files) {
      if (folderList.get(files).getName().equals(fileName.concat(".java"))) {
        return false;
      }
    }

    return validateClassName(fileName);
  }

  public boolean validatePackageNameAndExistance(String packageName, String finalPackageName) {
    for (int files = 0; files < folderList.size(); ++files) {
      if (folderList.get(files).getName().equals(packageName)) {
        return false;
      }
    }

    return validatePackageName(finalPackageName);
  }

  public boolean validateClassName(String fileName) {
    String regex = "^[a-zA-Z_][a-zA-Z0-9_]*$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(fileName);
    return matcher.matches();
  }

  public boolean validatePackageName(String packageName) {
    String regex = "^(?=\\.{0,2}[a-zA-Z_$][\\w$]*(\\.[a-zA-Z_$][\\w$]*)*$)[^\\d][\\w.]*$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(packageName);
    return matcher.matches();
  }
}
