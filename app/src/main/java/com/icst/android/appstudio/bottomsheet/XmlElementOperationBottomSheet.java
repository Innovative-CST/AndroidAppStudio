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
import com.icst.android.appstudio.databinding.BottomsheetXmlElementOperationBinding;
import com.icst.android.appstudio.xml.XmlModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class XmlElementOperationBottomSheet extends BottomSheetDialog {
	private Context context;
	private XmlElementOperation operationListener;
	private XmlModel xmlModel;

	public XmlElementOperationBottomSheet(
			Context context, XmlElementOperation operationListener, XmlModel xml) {
		super(context);
		this.context = context;
		this.operationListener = operationListener;
		this.xmlModel = xml;

		BottomsheetXmlElementOperationBinding binding = BottomsheetXmlElementOperationBinding
				.inflate(LayoutInflater.from(context));

		setContentView(binding.getRoot());

		if (xmlModel == null) {
			binding.delete.setVisibility(View.GONE);
		}

		if (xmlModel != null) {
			if (xml.getName() != null) {
				binding.elementName.setText(xmlModel.getName());
			}
		}

		binding.delete.setOnClickListener(
				v -> {
					operationListener.onDelete();
					dismiss();
				});
		binding.done.setOnClickListener(
				v -> {
					if (binding.elementName.getText().toString().equals("")) {
						dismiss();
						return;
					}
					if (xml == null) {
						xmlModel = new XmlModel();
					}
					xmlModel.setName(binding.elementName.getText().toString());
					xmlModel.setId(binding.elementName.getText().toString());
					operationListener.onModify(xmlModel);
					dismiss();
				});
	}

	public interface XmlElementOperation {
		void onDelete();

		void onModify(XmlModel xmlModel);
	}
}
