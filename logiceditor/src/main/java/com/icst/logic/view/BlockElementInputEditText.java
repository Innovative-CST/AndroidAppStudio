/*
 *  This file is part of Block IDLE.
 *
 *  Block IDLE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Block IDLE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Block IDLE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.logic.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class BlockElementInputEditText extends TextInputEditText {

	private static final Pattern VALID_STRING_PATTERN = Pattern.compile("^(?:[^\"\\\\]|\\\\.)*$");

	public enum InputType {
		STRING, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE;
	}

	private InputType inputType;
	private EditTextValueListener listener;
	private TextInputLayout textInputLayout;
	private Context context;

	public BlockElementInputEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void setInputType(InputType inputType, TextInputLayout textInputLayout, EditTextValueListener listener) {
		this.inputType = inputType;
		this.textInputLayout = textInputLayout;
		this.listener = listener;
		addTextChangedListener(
				new TextWatcher() {
					@Override
					public void beforeTextChanged(
							CharSequence s, int start, int count, int after) {
					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						listener.onValueChange(getText().toString());
						if (BlockElementInputEditText.this.inputType == InputType.STRING) {
							if (isValidString()) {
								BlockElementInputEditText.this.textInputLayout.setErrorEnabled(false);
							} else {
								BlockElementInputEditText.this.textInputLayout.setErrorEnabled(true);
								BlockElementInputEditText.this.textInputLayout.setError(getStringError());
							}
						} else if (BlockElementInputEditText.this.inputType == InputType.INT) {
							if (isValidInteger()) {
								BlockElementInputEditText.this.textInputLayout.setErrorEnabled(false);
							} else {
								BlockElementInputEditText.this.textInputLayout.setErrorEnabled(true);
								BlockElementInputEditText.this.textInputLayout.setError(getIntegerError());
							}
						}
					}

					@Override
					public void afterTextChanged(Editable s) {
					}
				});
	}

	private boolean isValidString() {
		Matcher matcher = VALID_STRING_PATTERN.matcher(getText().toString());
		return matcher.matches();
	}

	public boolean isValidInteger() {
		try {
			Integer.parseInt(getText().toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private String getStringError() {
		return "This input is invalid string please make sure that string is properly escaped.";
	}

	private String getIntegerError() {
		return "This input is invalid integer please make sure that it is a proper Integer and ranges between -2,147,483,648 to 2,147,483,647.";
	}

	public boolean isValid() {
		if (inputType == InputType.STRING) {
			return isValidString();
		} else if (inputType == InputType.INT) {
			return isValidInteger();
		}
		return true;
	}

	public interface EditTextValueListener {
		void onValueChange(String value);
	}
}
