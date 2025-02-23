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
