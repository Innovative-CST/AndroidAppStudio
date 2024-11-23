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
