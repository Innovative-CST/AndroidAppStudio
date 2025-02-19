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
