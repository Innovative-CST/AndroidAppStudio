package com.tscodeeditor.android.appstudio.fragments.projectmodelconfig;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import com.tscodeeditor.android.appstudio.databinding.FragmentProjectModelAppConfigrationLayoutBinding;
import com.tscodeeditor.android.appstudio.utils.Validator;

public class ProjectModelAppConfigrationFragment extends ProjectModelConfigBaseFragment {
  private FragmentProjectModelAppConfigrationLayoutBinding binding;

  // Validators
  private Validator versionNameValidator;
  private Validator versionCodeValidator;

  @Override
  @MainThread
  @Nullable
  public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
    // Intializing the binding.
    binding = FragmentProjectModelAppConfigrationLayoutBinding.inflate(layoutInflater);

    /*
     * Initialize the Validators for version name, minimum sdk version,
     * target sdk version, version code.
     * Specifing the maximum length, minimum length, patter etc...
     */
    versionNameValidator = new Validator();
    versionNameValidator.setEnableMinimumCharacterRequired(true);
    versionNameValidator.setMinimumCharacterRequired(1);
    versionNameValidator.setIsRequired(true);
    versionNameValidator.setEnablePatternValidator(true);
    versionNameValidator.setPattern("^[a-zA-Z0-9\\.\\-_]+$");

    versionCodeValidator = new Validator();
    versionCodeValidator.setEnableMinimumCharacterRequired(true);
    versionCodeValidator.setMinimumCharacterRequired(1);
    versionCodeValidator.setEnableMaximumCharacterLimit(true);
    versionCodeValidator.setMaxmumCharacterLimit(10);
    versionCodeValidator.setIsRequired(true);
    versionCodeValidator.setEnablePatternValidator(false);

    /*
     * Setting versionName field to work in single line and make versionCode,
     * minimumSdkVersion, targetSdkVersion numeric fields to numeric fields.
     */
    binding.versionName.setSingleLine(true);
    binding.versionCode.setInputType(InputType.TYPE_CLASS_NUMBER);
    binding.minimumSdkVersion.setInputType(InputType.TYPE_CLASS_NUMBER);
    binding.targetSdkVersion.setInputType(InputType.TYPE_CLASS_NUMBER);

    /*************************************************************************************************
     * Adding a TextWatcher to all TextFields to update the error status of fields while editing.    *
     *************************************************************************************************/

    binding.versionName.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateVersionNameField();
          }

          @Override
          public void afterTextChanged(Editable s) {}
        });
    binding.versionCode.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateVersionCodeField();
          }

          @Override
          public void afterTextChanged(Editable s) {}
        });
    return binding.getRoot();
  }

  /*
   * This method tells whether this page fragment fields has errors or not.
   * This method returns true or false based on user input is correct or not on this fragment.
   */
  @Override
  public boolean getIsRequiredFieldsProperlyFilled() {
    boolean isRequiredFieldsProperlyFilled = true;
    if (!versionCodeValidator.isValid(binding.versionCode.getText().toString())) {
      validateVersionCodeField();
      isRequiredFieldsProperlyFilled = false;
    }
    if (!versionNameValidator.isValid(binding.versionName.getText().toString())) {
      validateVersionNameField();
      isRequiredFieldsProperlyFilled = false;
    }
    return isRequiredFieldsProperlyFilled;
  }

  /*
   * Method to update the error UI of text fields whenever called.
   * Remove the error is it the field is valid.
   * Show error if it is not valid.
   */
  public void validateVersionCodeField() {
    if (versionCodeValidator.isValid(binding.versionCode.getText().toString())) {
      if (Long.parseLong(binding.versionCode.getText().toString()) > 2147483647) {
        binding.versionCodeTextInputLayout.setError(
            "Out of range please keep the version code under 2147483647");
        binding.versionCodeTextInputLayout.setErrorEnabled(true);
      } else {
        binding.versionCodeTextInputLayout.setErrorEnabled(false);
      }
    } else {
      binding.versionCodeTextInputLayout.setError(
          versionCodeValidator.getInValidError(binding.versionCode.getText().toString()));
      binding.versionCodeTextInputLayout.setErrorEnabled(true);
    }
  }

  /*
   * Method to update the error UI of text fields whenever called.
   * Remove the error is it the field is valid.
   * Show error if it is not valid.
   */
  public void validateVersionNameField() {
    if (versionNameValidator.isValid(binding.versionName.getText().toString())) {
      binding.versionNameTextInputLayout.setErrorEnabled(false);
    } else {
      binding.versionNameTextInputLayout.setError(
          versionNameValidator.getInValidError(binding.versionName.getText().toString()));
      binding.versionNameTextInputLayout.setErrorEnabled(true);
    }
  }
}
