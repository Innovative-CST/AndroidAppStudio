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

package com.icst.android.appstudio.block.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.icst.android.appstudio.block.enums.InputTypes;
import com.icst.android.appstudio.block.utils.NumberRangeValidator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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
          public void afterTextChanged(Editable s) {}
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
