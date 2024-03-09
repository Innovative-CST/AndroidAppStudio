/*
 *  This file is part of Android AppStudio.
 *
 *  Android AppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Android AppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Android AppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.tscodeeditor.android.appstudio.fragments.projectmodelconfig;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import com.tscodeeditor.android.appstudio.databinding.FragmentProjectModelAppsetupLayoutBinding;
import com.tscodeeditor.android.appstudio.models.ProjectModel;
import com.tscodeeditor.android.appstudio.utils.Validator;

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
