/*
 *  This file is part of AndroidAppStudio.
 *
 *  AndroidAppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidAppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidAppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.android.appstudio.block.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.icst.android.appstudio.block.enums.InputTypes;
import com.icst.android.appstudio.block.utils.NumberRangeValidator;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class InputTypeValidatorEditText extends TextInputEditText {

	private static final Pattern VALID_STRING_PATTERN = Pattern.compile("^(?:[^\"\\\\]|\\\\.)*$");
	private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#?([A-Fa-f0-9]{6})$");

	private Context context;
	public TextInputLayout textInputLayout;
	private boolean isValid;
	private InputTypes inputType;

	public InputTypeValidatorEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void setInputType(InputTypes inputType) {
		this.inputType = inputType;
		addTextChangedListener(
				new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						if (isValid(inputType)) {
							textInputLayout.setErrorEnabled(false);
						} else {
							textInputLayout.setErrorEnabled(true);
							textInputLayout.setError(getError(inputType));
						}
					}

					@Override
					public void afterTextChanged(Editable s) {
					}
				});
	}

	public String getError(InputTypes inputType) {
		if (inputType == InputTypes.INPUT_TYPE_BYTE) {
			return "Out of Range!";
		} else if (inputType == InputTypes.INPUT_TYPE_SHORT) {
			return "Out of Range!";
		} else if (inputType == InputTypes.INPUT_TYPE_INT) {
			return "Out of Range!";
		} else if (inputType == InputTypes.INPUT_TYPE_LONG) {
			return "Out of Range!";
		} else if (inputType == InputTypes.INPUT_TYPE_FLOAT) {
			return "Out of Range!";
		} else if (inputType == InputTypes.INPUT_TYPE_DOUBLE) {
			return "Out of Range!";
		} else if (inputType == InputTypes.INPUT_TYPE_STRING) {
			return "This input is invalid string please make sure that string is properly escaped.";
		} else if (inputType == InputTypes.INPUT_TYPE_COLOR) {
			return "Invalid hexadecimal color code";
		} else if (inputType == InputTypes.UNKNOWN) {
			return "";
		}
		return "";
	}

	private boolean isValid(InputTypes inputType) {
		if (inputType == InputTypes.INPUT_TYPE_BYTE) {
			return NumberRangeValidator.isValidByte(getText().toString());
		} else if (inputType == InputTypes.INPUT_TYPE_SHORT) {
			return NumberRangeValidator.isValidShort(getText().toString());
		} else if (inputType == InputTypes.INPUT_TYPE_INT) {
			return NumberRangeValidator.isValidInt(getText().toString());
		} else if (inputType == InputTypes.INPUT_TYPE_LONG) {
			return NumberRangeValidator.isValidLong(getText().toString());
		} else if (inputType == InputTypes.INPUT_TYPE_FLOAT) {
			return NumberRangeValidator.isValidFloat(getText().toString());
		} else if (inputType == InputTypes.INPUT_TYPE_DOUBLE) {
			return NumberRangeValidator.isValidDouble(getText().toString());
		} else if (inputType == InputTypes.INPUT_TYPE_STRING) {
			return isValidString(getText().toString());
		} else if (inputType == InputTypes.INPUT_TYPE_COLOR) {
			return isValidHexColor(getText().toString());
		} else if (inputType == InputTypes.UNKNOWN) {
			return true;
		}
		return true;
	}

	public void setError() {
		if (isValid(inputType)) {
			textInputLayout.setErrorEnabled(false);
		} else {
			textInputLayout.setErrorEnabled(false);
			textInputLayout.setError(getError(inputType));
		}
	}

	private static boolean isValidString(String input) {
		Matcher matcher = VALID_STRING_PATTERN.matcher(input);
		return matcher.matches();
	}

	public static boolean isValidHexColor(String input) {
		Matcher matcher = HEX_COLOR_PATTERN.matcher(input);
		return matcher.matches();
	}

	public boolean isValid() {
		return isValid(inputType);
	}
}
