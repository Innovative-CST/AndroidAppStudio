package com.icst.android.appstudio.fragments.projectmodelconfig;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import com.icst.android.appstudio.databinding.FragmentProjectModelAppConfigrationLayoutBinding;
import com.icst.android.appstudio.models.ProjectModel;
import com.icst.android.appstudio.utils.Validator;

public class ProjectModelAppConfigrationFragment extends ProjectModelConfigBaseFragment {
	private FragmentProjectModelAppConfigrationLayoutBinding binding;

	// Validators
	private Validator versionNameValidator;
	private Validator versionCodeValidator;

	public ProjectModelAppConfigrationFragment(boolean isNewProject, ProjectModel mProjectModel) {
		super(isNewProject, mProjectModel);
	}

	@Override
	@MainThread
	@Nullable public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
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
		 * Adding a TextWatcher to all TextFields to update the error status of fields
		 * while editing. *
		 *************************************************************************************************/

		binding.versionName.addTextChangedListener(
				new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						validateVersionNameField();
					}

					@Override
					public void afterTextChanged(Editable s) {
					}
				});
		binding.versionCode.addTextChangedListener(
				new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						validateVersionCodeField();
					}

					@Override
					public void afterTextChanged(Editable s) {
					}
				});
		binding.minimumSdkVersion.addTextChangedListener(
				new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						validateMinimumSdkVersionField();
					}

					@Override
					public void afterTextChanged(Editable s) {
					}
				});
		binding.targetSdkVersion.addTextChangedListener(
				new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						validateTagetSdkVersionField();
					}

					@Override
					public void afterTextChanged(Editable s) {
					}
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
	 * This method returns true or false based on user input is correct or not on
	 * this fragment.
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
		if (!isValidMinimumSdkVersion()) {
			validateMinimumSdkVersionField();
			isRequiredFieldsProperlyFilled = false;
		}
		if (!isValidTargetSdkVersion()) {
			validateTagetSdkVersionField();
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

	/*
	 * Method to update the error UI of text fields whenever called.
	 * Remove the error is it the field is valid.
	 * Show error if it is not valid.
	 */
	public void validateMinimumSdkVersionField() {
		if (binding.minimumSdkVersion.getText().toString().length() == 0) {
			binding.minimumSdkVersionTextInputLayout.setError("Field is required");
			binding.minimumSdkVersionTextInputLayout.setErrorEnabled(true);
			return;
		}
		if (binding.minimumSdkVersion.getText().toString().length() > 2) {
			binding.minimumSdkVersionTextInputLayout.setError("Out of range value!");
			binding.minimumSdkVersionTextInputLayout.setErrorEnabled(true);
			return;
		}
		if (Integer.parseInt(binding.minimumSdkVersion.getText().toString()) < 21) {
			binding.minimumSdkVersionTextInputLayout.setError("Minimum supported version is 21");
			binding.minimumSdkVersionTextInputLayout.setErrorEnabled(true);
			return;
		}
		if (Integer.parseInt(binding.minimumSdkVersion.getText().toString()) > 33) {
			binding.minimumSdkVersionTextInputLayout.setError("You cannot exceed this value above 33");
			binding.minimumSdkVersionTextInputLayout.setErrorEnabled(true);
			return;
		}
		binding.minimumSdkVersionTextInputLayout.setErrorEnabled(false);
	}

	/*
	 * Method to update the error UI of text fields whenever called.
	 * Remove the error is it the field is valid.
	 * Show error if it is not valid.
	 */
	public void validateTagetSdkVersionField() {
		if (binding.targetSdkVersion.getText().toString().length() == 0) {
			binding.targetSdkVersionTextInputLayout.setError("Field is required");
			binding.targetSdkVersionTextInputLayout.setErrorEnabled(true);
			return;
		}
		if (binding.targetSdkVersion.getText().toString().length() > 2) {
			binding.targetSdkVersionTextInputLayout.setError("Out of range value!");
			binding.targetSdkVersionTextInputLayout.setErrorEnabled(true);
			return;
		}
		if (Integer.parseInt(binding.targetSdkVersion.getText().toString()) < 21) {
			binding.targetSdkVersionTextInputLayout.setError("Minimum supported version is 21");
			binding.targetSdkVersionTextInputLayout.setErrorEnabled(true);
			return;
		}
		if (Integer.parseInt(binding.targetSdkVersion.getText().toString()) > 33) {
			binding.targetSdkVersionTextInputLayout.setError("You cannot exceed this value above 33");
			binding.targetSdkVersionTextInputLayout.setErrorEnabled(true);
			return;
		}
		binding.targetSdkVersionTextInputLayout.setErrorEnabled(false);
	}

	/*
	 * Method to check wheather the minimum sdk version is correct
	 * and lies under specified range.
	 */
	public boolean isValidMinimumSdkVersion() {
		if (binding.minimumSdkVersion.getText().toString().length() == 0)
			return false;

		if (binding.minimumSdkVersion.getText().toString().length() > 2)
			return false;

		if (Integer.parseInt(binding.minimumSdkVersion.getText().toString()) < 21)
			return false;

		if (Integer.parseInt(binding.minimumSdkVersion.getText().toString()) > 33)
			return false;

		return true;
	}

	/*
	 * Method to check wheather the target sdk version is correct
	 * and lies under specified range.
	 */
	public boolean isValidTargetSdkVersion() {
		if (binding.targetSdkVersion.getText().toString().length() == 0)
			return false;

		if (binding.targetSdkVersion.getText().toString().length() > 2)
			return false;

		if (Integer.parseInt(binding.targetSdkVersion.getText().toString()) < 21)
			return false;

		if (Integer.parseInt(binding.targetSdkVersion.getText().toString()) > 33)
			return false;

		return true;
	}

	@Override
	public void addValueInProjectModelOfFragment() {
		getMProjectModel().setVersionCode(Integer.parseInt(binding.versionCode.getText().toString()));
		getMProjectModel().setProjectVersionName(binding.versionName.getText().toString());
		getMProjectModel().setMinimumSdkVersion(binding.minimumSdkVersion.getText().toString());
		getMProjectModel().setTargetSdkVersion(binding.targetSdkVersion.getText().toString());
	}

	@Override
	public void setProjectModelValueIntoFields() {
		binding.versionCode.setText(String.valueOf(getMProjectModel().getVersionCode()));
		binding.versionName.setText(String.valueOf(getMProjectModel().getProjectVersionName()));
		binding.minimumSdkVersion.setText(String.valueOf(getMProjectModel().getMinimumSdkVersion()));
		binding.targetSdkVersion.setText(String.valueOf(getMProjectModel().getTargetSdkVersion()));
	}
}
