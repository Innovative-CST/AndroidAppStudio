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

package com.icst.android.appstudio.block.dialog.variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.block.databinding.DialogEditVariableBinding;
import com.icst.android.appstudio.block.databinding.InputTypeValidatorEdittextBinding;
import com.icst.android.appstudio.block.enums.InputTypes;
import com.icst.android.appstudio.block.model.VariableModel;
import com.icst.android.appstudio.block.utils.ColorUtils;
import com.icst.android.appstudio.block.view.InputTypeValidatorEditText;

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
		DialogEditVariableBinding binding = DialogEditVariableBinding.inflate(LayoutInflater.from(context));
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
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

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
					public void afterTextChanged(Editable s) {
					}
				});

		if (variable.getVariableTitles() != null) {
			for (Map.Entry<String, String> entry : variable.getVariableTitles().entrySet()) {
				String key = entry.getKey();

				InputTypeValidatorEdittextBinding inputEdittext = InputTypeValidatorEdittextBinding
						.inflate(LayoutInflater.from(getContext()));
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

	public void onVariableModified(VariableModel variable) {
	}
}
