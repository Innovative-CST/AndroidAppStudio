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

package com.icst.android.appstudio.block.dialog.variables;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.icst.android.appstudio.block.databinding.DialogEditVariableBinding;
import com.icst.android.appstudio.block.databinding.InputTypeValidatorEdittextBinding;
import com.icst.android.appstudio.block.enums.InputTypes;
import com.icst.android.appstudio.block.model.VariableModel;
import com.icst.android.appstudio.block.utils.ColorUtils;
import com.icst.android.appstudio.block.view.InputTypeValidatorEditText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditVariableDialog extends MaterialAlertDialogBuilder {
  private Context context;
  private VariableModel variable;
  private AlertDialog dialog;
  private ArrayList<InputTypeValidatorEditText> variablesInputEditText;

  public EditVariableDialog(Context context, VariableModel variable) {
    super(context);
    this.context = context;
    this.variable = variable;

    variablesInputEditText = new ArrayList<InputTypeValidatorEditText>();

    setTitle("Edit Variable Values");
    setCancelable(false);
    DialogEditVariableBinding binding =
        DialogEditVariableBinding.inflate(LayoutInflater.from(context));
    setView(binding.getRoot());

    if (variable.getVariableName() != null) {
      binding.variableName.setText(variable.getVariableName());
    }

    if (variable.getVariableTitle() != null) {
      binding.title.setText(variable.getVariableTitle());
    }
    if (variable.getIcon() != null) {
      binding.icon.setImageBitmap(
          BitmapFactory.decodeByteArray(variable.getIcon(), 0, variable.getIcon().length));
    } else {
      binding.icon.setImageBitmap(
          textToBitmap(
              variable.getVariableTitle(),
              16,
              ColorUtils.getColor(
                  binding.getRoot().getContext(), com.google.android.material.R.attr.colorPrimary),
              binding.getRoot().getContext()));
    }

    if (variable.getIsInitializedGlobally()) {
      binding.initVarCheckbox.setChecked(true);
      binding.fields.setVisibility(View.VISIBLE);
    } else {
      binding.initVarCheckbox.setChecked(false);
      binding.fields.setVisibility(View.GONE);
    }
    binding.initVarCheckbox.setOnCheckedChangeListener(
        (button, state) -> {
          if (state) {
            binding.fields.setVisibility(View.VISIBLE);
          } else {
            binding.fields.setVisibility(View.GONE);
          }
        });

    if (variable.getMustBeGloballyIntialized()) {
      binding.initVarCheckbox.setChecked(true);
      binding.initVarCheckbox.setEnabled(false);
    } else {
      binding.initVarCheckbox.setEnabled(true);
    }

    binding.variableName.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isValidVariableName(binding.variableName.getText().toString())) {
              binding.variableNameTextInputLayout.setErrorEnabled(false);
            } else {
              binding.variableNameTextInputLayout.setErrorEnabled(true);
              binding.variableNameTextInputLayout.setError("Not a valid variable name");
            }
          }

          @Override
          public void afterTextChanged(Editable s) {}
        });

    if (variable.getVariableTitles() != null) {
      for (Map.Entry<String, String> entry : variable.getVariableTitles().entrySet()) {
        String key = entry.getKey();

        InputTypeValidatorEdittextBinding inputEdittext =
            InputTypeValidatorEdittextBinding.inflate(LayoutInflater.from(getContext()));
        inputEdittext.mInputTypeValidatorEditText.textInputLayout = inputEdittext.mTextInputLayout;
        inputEdittext.mInputTypeValidatorEditText.setInputType(
            InputTypes.fromInt(variable.getInputType().get(key)));
        inputEdittext.mInputTypeValidatorEditText.setTag(key);
        inputEdittext.mTextInputLayout.setHint(variable.getVariableTitles().get(key));

        if (variable.getVariableValues() != null) {
          if (variable.getVariableValues().get(key) != null) {
            inputEdittext.mInputTypeValidatorEditText.setText(
                variable.getVariableValues().get(key));
          }
        }

        binding.fields.addView(inputEdittext.getRoot());
        variablesInputEditText.add(inputEdittext.mInputTypeValidatorEditText);
      }
    }

    binding.done.setOnClickListener(
        v -> {
          boolean isValid = true;

          if (!isValidVariableName(binding.variableName.getText().toString())) {
            binding.variableNameTextInputLayout.setErrorEnabled(true);
            binding.variableNameTextInputLayout.setError("Not a valid variable name");
            isValid = false;
          } else {
            variable.setVariableName(binding.variableName.getText().toString());
          }

          variable.setIsInitializedGlobally(binding.initVarCheckbox.isChecked());

          if (binding.initVarCheckbox.isChecked()) {
            for (int i = 0; i < variablesInputEditText.size(); ++i) {
              if (!variablesInputEditText.get(i).isValid()) {
                isValid = false;
              }
            }
          }

          if (isValid) {
            if (binding.initVarCheckbox.isChecked()) {
              for (int i = 0; i < variablesInputEditText.size(); ++i) {
                if (variablesInputEditText.get(i).getTag() != null) {
                  if (variablesInputEditText.get(i).getTag() instanceof String key) {
                    if (variable.getVariableValues() == null) {
                      HashMap<String, String> variableValues = new HashMap<String, String>();
                      variable.setVariableValues(variableValues);
                    }
                    variable
                        .getVariableValues()
                        .put(key, variablesInputEditText.get(i).getText().toString());
                  }
                }
              }
            }
            onVariableModified(variable);
            dialog.dismiss();
          } else {
            for (int i = 0; i < variablesInputEditText.size(); ++i) {
              variablesInputEditText.get(i).setError();
            }
            Toast.makeText(
                    getContext(),
                    "Can't save variable, please fill the fields properly",
                    Toast.LENGTH_SHORT)
                .show();
          }
        });
    binding.cancel.setOnClickListener(
        v -> {
          dialog.dismiss();
        });
    dialog = show();
  }

  private Bitmap textToBitmap(String text, int textSize, int textColor, Context context) {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setTextSize(
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, textSize, context.getResources().getDisplayMetrics()));
    paint.setColor(textColor);
    paint.setTextAlign(Paint.Align.LEFT);

    float baseline = -paint.ascent(); // ascent() is negative
    int width = (int) (paint.measureText(text) + 0.5f); // round
    int height = (int) (baseline + paint.descent() + 0.5f);

    Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(image);
    canvas.drawText(text, 0, baseline, paint);
    return image;
  }

  public boolean isValidVariableName(String varName) {
    if (varName == null || varName.isEmpty()) {
      return false;
    }

    String[] javaKeywords = {
      "abstract",
      "assert",
      "boolean",
      "break",
      "byte",
      "case",
      "catch",
      "char",
      "class",
      "const",
      "continue",
      "default",
      "do",
      "double",
      "else",
      "enum",
      "extends",
      "final",
      "finally",
      "float",
      "for",
      "goto",
      "if",
      "implements",
      "import",
      "instanceof",
      "int",
      "interface",
      "long",
      "native",
      "new",
      "null",
      "package",
      "private",
      "protected",
      "public",
      "return",
      "short",
      "static",
      "strictfp",
      "super",
      "switch",
      "synchronized",
      "this",
      "throw",
      "throws",
      "transient",
      "try",
      "void",
      "volatile",
      "while",
      "true",
      "false"
    };
    for (String keyword : javaKeywords) {
      if (varName.equals(keyword)) {
        return false;
      }
    }

    char firstChar = varName.charAt(0);
    if (!Character.isLetter(firstChar) && firstChar != '$' && firstChar != '_') {
      return false;
    }

    for (int i = 1; i < varName.length(); i++) {
      char ch = varName.charAt(i);
      if (!Character.isLetterOrDigit(ch) && ch != '$' && ch != '_') {
        return false;
      }
    }

    return true;
  }

  public void onVariableModified(VariableModel variable) {}
}
