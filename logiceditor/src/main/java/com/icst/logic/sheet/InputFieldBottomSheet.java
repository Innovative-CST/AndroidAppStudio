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

package com.icst.logic.sheet;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.icst.android.appstudio.beans.DatatypeBean;
import com.icst.android.appstudio.beans.NumericBlockElementBean;
import com.icst.android.appstudio.beans.StringBlockElementBean;
import com.icst.android.appstudio.beans.ValueInputBlockElementBean;
import com.icst.logic.editor.databinding.BottomsheetInputFieldBinding;
import com.icst.logic.view.BlockElementInputEditText;

import android.content.Context;
import android.view.LayoutInflater;

public class InputFieldBottomSheet extends BottomSheetDialog {

	private BottomsheetInputFieldBinding binding;

	public InputFieldBottomSheet(
			Context context,
			ValueInputBlockElementBean mValueInputBlockElementBean,
			ValueListener valueListener) {
		super(context);

		binding = BottomsheetInputFieldBinding.inflate(LayoutInflater.from(context));
		if (mValueInputBlockElementBean.getAcceptedReturnType().equals(getStringDatatype())) {
			if (mValueInputBlockElementBean instanceof StringBlockElementBean mStringBlockElementBean) {
				binding.dialogTitle.setText("Enter String");
				binding.message.setText(
						"Please make sure you escape the String, otherwise you will encounter error.");
				binding.mBlockElementInputEditText.setText(
						mStringBlockElementBean.getString() == null ? "" : mStringBlockElementBean.getString());
				binding.mBlockElementInputEditText.setInputType(
						BlockElementInputEditText.InputType.STRING,
						binding.mTextInputLayout,
						new BlockElementInputEditText.EditTextValueListener() {

							@Override
							public void onValueChange(String value) {
								binding.done.setEnabled(binding.mBlockElementInputEditText.isValid());
							}
						});
			}
		} else if (mValueInputBlockElementBean.getAcceptedReturnType().equals(getIntegerDatatype())) {
			if (mValueInputBlockElementBean instanceof NumericBlockElementBean mNumericBlockElementBean) {
				binding.dialogTitle.setText("Enter your Integer");
				binding.message.setText(
						"Please make sure you enter a valid integer value, otherwise you will encounter error.");
				binding.mBlockElementInputEditText.setText(
						mNumericBlockElementBean.getNumericalValue() == null ? ""
								: mNumericBlockElementBean.getNumericalValue());
				binding.mBlockElementInputEditText.setInputType(
						BlockElementInputEditText.InputType.INT,
						binding.mTextInputLayout,
						new BlockElementInputEditText.EditTextValueListener() {

							@Override
							public void onValueChange(String value) {
								binding.done.setEnabled(binding.mBlockElementInputEditText.isValid());
							}
						});
			}
		}
		setContentView(binding.getRoot());
		binding.done.setOnClickListener(v -> {
			valueListener.onChange(binding.mBlockElementInputEditText.getText().toString());
			dismiss();
		});
	}

	public DatatypeBean getStringDatatype() {
		DatatypeBean stringDatatype = new DatatypeBean();
		stringDatatype.setImportNecessary(false);
		stringDatatype.setClassImport("java.lang.String");
		stringDatatype.setClassName("String");
		return stringDatatype;
	}

	public DatatypeBean getIntegerDatatype() {
		DatatypeBean intDatatype = new DatatypeBean();
		intDatatype.setClassImport("java.lang.Integer");
		intDatatype.setClassName("Integer");
		intDatatype.setImportNecessary(false);
		return intDatatype;
	}

	public interface ValueListener {
		void onChange(String value);
	}
}
