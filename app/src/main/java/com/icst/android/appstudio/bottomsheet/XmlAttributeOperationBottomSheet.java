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

package com.icst.android.appstudio.bottomsheet;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.icst.android.appstudio.databinding.BottomsheetXmlAttributeOperationBinding;
import com.icst.android.appstudio.xml.XmlAttributeModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class XmlAttributeOperationBottomSheet extends BottomSheetDialog {
	private Context context;
	private XmlAttributeOperation operationListener;
	private XmlAttributeModel attr;

	public XmlAttributeOperationBottomSheet(
			Context context, XmlAttributeOperation operationListener, XmlAttributeModel attribute) {
		super(context);
		this.context = context;
		this.operationListener = operationListener;
		this.attr = attribute;

		BottomsheetXmlAttributeOperationBinding binding = BottomsheetXmlAttributeOperationBinding
				.inflate(LayoutInflater.from(context));

		setContentView(binding.getRoot());

		if (attr == null) {
			binding.delete.setVisibility(View.GONE);
		}

		if (attr != null) {

			if (attr.getAttribute() != null) {
				binding.attrName.setText(attr.getAttribute());
			}

			if (attr.getAttributeValue() != null) {
				binding.attrValue.setText(String.valueOf(attr.getAttributeValue()));
			}
		}

		binding.delete.setOnClickListener(
				v -> {
					operationListener.onDeleteAttribute();
					dismiss();
				});
		binding.done.setOnClickListener(
				v -> {
					if (attr == null) {
						attr = new XmlAttributeModel();
					}
					attr.setAttribute(binding.attrName.getText().toString());
					attr.setAttributeValue(binding.attrValue.getText().toString());
					operationListener.onModifyAttribute(attr);
					dismiss();
				});
	}

	public interface XmlAttributeOperation {
		void onDeleteAttribute();

		void onModifyAttribute(XmlAttributeModel xmlAttributeModel);
	}
}
