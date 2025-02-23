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

package com.icst.android.appstudio.block.dialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.block.databinding.DialogInputNumberBinding;
import com.icst.android.appstudio.block.editor.EventEditor;
import com.icst.android.appstudio.block.model.BlockValueFieldModel;
import com.icst.android.appstudio.block.utils.ArrayUtils;
import com.icst.android.appstudio.block.utils.ColorUtils;
import com.icst.android.appstudio.block.utils.NumberRangeValidator;
import com.icst.android.appstudio.block.view.NumberView;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

public class NumericalValueEditorDialog extends MaterialAlertDialogBuilder {
	public static final int INPUT_TYPE_UNKNOWN = 0;

	public static final int INPUT_TYPE_BYTE = 1;
	public static final int INPUT_TYPE_SHORT = 2;
	public static final int INPUT_TYPE_INT = 3;
	public static final int INPUT_TYPE_LONG = 4;

	public static final int INPUT_TYPE_FLOAT = 5;
	public static final int INPUT_TYPE_DOUBLE = 6;

	public DialogInputNumberBinding binding;
	private AlertDialog dialog;

	public NumericalValueEditorDialog(
			EventEditor editor, BlockValueFieldModel field, NumberView numberView) {
		super(editor.getContext());
		binding = DialogInputNumberBinding.inflate(LayoutInflater.from(editor.getContext()));
		binding.title.setText("Enter Numerical Value");
		setView(binding.getRoot());
		if (field == null) {
			binding.title.setTextColor(
					ColorUtils.getColor(editor.getContext(), com.google.android.material.R.attr.colorError));
			Drawable background = getBackground();
			background.setTint(
					ColorUtils.getColor(
							editor.getContext(), com.google.android.material.R.attr.colorErrorContainer));
			setBackground(background);
			binding.errorLayout.setVisibility(View.VISIBLE);
			return;
		}

		int inputType = getInputType(field.getAcceptors());
		if (inputType == INPUT_TYPE_UNKNOWN) {
			binding.title.setTextColor(
					ColorUtils.getColor(
							editor.getContext(), com.google.android.material.R.attr.colorOnErrorContainer));
			Drawable background = getBackground();
			background.setTint(
					ColorUtils.getColor(
							editor.getContext(), com.google.android.material.R.attr.colorErrorContainer));
			setBackground(background);
			binding.errorLayout.setVisibility(View.VISIBLE);
			return;
		}

		if (field.getFieldType() != BlockValueFieldModel.FieldType.FIELD_NUMBER) {
			binding.title.setTextColor(
					ColorUtils.getColor(
							editor.getContext(), com.google.android.material.R.attr.colorOnErrorContainer));
			Drawable background = getBackground();
			background.setTint(
					ColorUtils.getColor(
							editor.getContext(), com.google.android.material.R.attr.colorErrorContainer));
			setBackground(background);
			binding.errorLayout.setVisibility(View.VISIBLE);
			return;
		}
		binding.inputLayout.setVisibility(View.VISIBLE);
		binding.value.setSingleLine(true);

		if (field.getValue() != null) {
			if (NumberRangeValidator.isValidNumber(field.getValue(), inputType)) {
				binding.value.setText(field.getValue());
			}
		}

		switch (inputType) {
			case INPUT_TYPE_DOUBLE:
			case INPUT_TYPE_FLOAT:
				binding.value.setInputType(
						InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
				break;
			case INPUT_TYPE_LONG:
			case INPUT_TYPE_INT:
			case INPUT_TYPE_SHORT:
			case INPUT_TYPE_BYTE:
				binding.value.setInputType(InputType.TYPE_CLASS_NUMBER);
				break;
		}

		binding.value.addTextChangedListener(
				new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						if (NumberRangeValidator.isValidNumber(binding.value.getText().toString(), inputType)) {
							binding.valueInputLayout.setErrorEnabled(false);
						} else {
							binding.valueInputLayout.setErrorEnabled(false);
							binding.valueInputLayout.setError("Out of range!!");
						}
					}

					@Override
					public void afterTextChanged(Editable s) {
					}
				});

		binding.done.setOnClickListener(
				v -> {
					if (NumberRangeValidator.isValidNumber(binding.value.getText().toString(), inputType)) {
						numberView.setFieldValue(binding.value.getText().toString());
						dialog.dismiss();
					}
				});
		binding.cancel.setOnClickListener(
				v -> {
					dialog.dismiss();
				});

		dialog = show();
	}

	private int getInputType(String[] acceptors) {
		if (ArrayUtils.ifContains(acceptors, "double")) {
			return INPUT_TYPE_DOUBLE;
		} else if (ArrayUtils.ifContains(acceptors, "float")) {
			return INPUT_TYPE_FLOAT;
		} else if (ArrayUtils.ifContains(acceptors, "long")) {
			return INPUT_TYPE_LONG;
		} else if (ArrayUtils.ifContains(acceptors, "int")) {
			return INPUT_TYPE_INT;
		} else if (ArrayUtils.ifContains(acceptors, "short")) {
			return INPUT_TYPE_SHORT;
		} else if (ArrayUtils.ifContains(acceptors, "byte")) {
			return INPUT_TYPE_BYTE;
		}
		return 0;
	}
}
