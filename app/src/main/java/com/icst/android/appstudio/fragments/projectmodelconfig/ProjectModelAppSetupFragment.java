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

package com.icst.android.appstudio.fragments.projectmodelconfig;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import com.icst.android.appstudio.databinding.FragmentProjectModelAppsetupLayoutBinding;
import com.icst.android.appstudio.models.ProjectModel;
import com.icst.android.appstudio.utils.Validator;

public class ProjectModelAppSetupFragment extends ProjectModelConfigBaseFragment {
  private FragmentProjectModelAppsetupLayoutBinding binding;

  // Validators
  private Validator projectNameValidator;
  private Validator packageNameValidator;

  public ProjectModelAppSetupFragment(boolean isNewProject, ProjectModel mProjectModel) {
    super(isNewProject, mProjectModel);
  }

  @Override
  @MainThread
  @Nullable
  public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
    // Intializing the binding.
    binding = FragmentProjectModelAppsetupLayoutBinding.inflate(layoutInflater);

    /*
     * Initialize the Validators for project name and package name
     * Specifing the maximum length, minimum length, patter etc...
     */
    projectNameValidator = new Validator();
    projectNameValidator.setEnableMinimumCharacterRequired(true);
    projectNameValidator.setMinimumCharacterRequired(1);
    projectNameValidator.setEnableMaximumCharacterLimit(true);
    projectNameValidator.setMaxmumCharacterLimit(50);
    projectNameValidator.setIsRequired(true);
    projectNameValidator.setEnablePatternValidator(true);
    projectNameValidator.setPattern("^[a-zA-Z0-9\\s-]{1,50}$");

    packageNameValidator = new Validator();
    packageNameValidator.setEnableMinimumCharacterRequired(true);
    packageNameValidator.setMinimumCharacterRequired(1);
    packageNameValidator.setEnableMaximumCharacterLimit(true);
    packageNameValidator.setMaxmumCharacterLimit(255);
    packageNameValidator.setIsRequired(true);
    packageNameValidator.setEnablePatternValidator(true);
    packageNameValidator.setPattern("^[a-zA-Z]+[a-zA-Z0-9_]*(\\.[a-zA-Z]+[a-zA-Z0-9_]*)*$");

    /*
     * Setting all the fields to work in single line.
     */
    binding.projectName.setSingleLine(true);
    binding.packageName.setSingleLine(true);

    /*************************************************************************************************
     * Adding a TextWatcher to all TextFields to update the error status of fields while editing.    *
     *************************************************************************************************/

    binding.projectName.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateProjectNameField();
          }

          @Override
          public void afterTextChanged(Editable s) {}
        });
    binding.packageName.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            validatePackageNameField();
          }

          @Override
          public void afterTextChanged(Editable s) {}
        });
    /*
     * Load project model data in fields
     */
    if (!getIsNewProject()) {
      if (getMProjectModel() != null) {
        setProjectModelValueIntoFields();
      }
    }
    return binding.getRoot();
  }

  /*
   * This method tells whether this page fragment fields has errors or not.
   * This method returns true or false based on user input is correct or not on this fragment.
   */
  @Override
  public boolean getIsRequiredFieldsProperlyFilled() {
    boolean isRequiredFieldsProperlyFilled = true;
    if (!projectNameValidator.isValid(binding.projectName.getText().toString())) {
      validateProjectNameField();
      isRequiredFieldsProperlyFilled = false;
    }
    if (!packageNameValidator.isValid(binding.packageName.getText().toString())) {
      validatePackageNameField();
      isRequiredFieldsProperlyFilled = false;
    }
    return isRequiredFieldsProperlyFilled;
  }

  /*
   * Method to update the error UI of text fields whenever called.
   * Remove the error is it the field is valid.
   * Show error if it is not valid.
   */
  public void validateProjectNameField() {
    if (projectNameValidator.isValid(binding.projectName.getText().toString())) {
      binding.projectNameTextInputLayout.setErrorEnabled(false);
    } else {
      binding.projectNameTextInputLayout.setError(
          projectNameValidator.getInValidError(binding.projectName.getText().toString()));
      binding.projectNameTextInputLayout.setErrorEnabled(true);
    }
  }

  /*
   * Method to update the error UI of text fields whenever called.
   * Remove the error is it the field is valid.
   * Show error if it is not valid.
   */
  public void validatePackageNameField() {
    if (packageNameValidator.isValid(binding.packageName.getText().toString())) {
      binding.packageNameTextInputLayout.setErrorEnabled(false);
    } else {
      binding.packageNameTextInputLayout.setError(
          packageNameValidator.getInValidError(binding.packageName.getText().toString()));
      binding.packageNameTextInputLayout.setErrorEnabled(true);
    }
  }

  @Override
  public void addValueInProjectModelOfFragment() {
    getMProjectModel().setProjectName(binding.projectName.getText().toString());
    getMProjectModel().setPackageName(binding.packageName.getText().toString());
  }

  @Override
  public void setProjectModelValueIntoFields() {
    binding.projectName.setText(getMProjectModel().getProjectName());
    binding.packageName.setText(getMProjectModel().getPackageName());
  }
}
