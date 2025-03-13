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
import com.icst.android.appstudio.beans.XmlBean;
import com.icst.android.appstudio.databinding.BottomsheetXmlElementOperationBinding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class XmlElementOperationBottomSheet extends BottomSheetDialog {
	private Context context;
	private XmlElementOperation operationListener;
	private XmlBean xmlBean;

	public XmlElementOperationBottomSheet(
			Context context, XmlElementOperation operationListener, XmlBean xml) {
		super(context);
		this.context = context;
		this.operationListener = operationListener;
		this.xmlBean = xml;

		BottomsheetXmlElementOperationBinding binding = BottomsheetXmlElementOperationBinding
				.inflate(LayoutInflater.from(context));

		setContentView(binding.getRoot());

		if (xmlBean == null) {
			binding.delete.setVisibility(View.GONE);
		}

		if (xmlBean != null) {
			if (xml.getName() != null) {
				binding.elementName.setText(xmlBean.getName());
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
						xmlBean = new XmlBean();
					}
					xmlBean.setName(binding.elementName.getText().toString());
					xmlBean.setId(binding.elementName.getText().toString());
					operationListener.onModify(xmlBean);
					dismiss();
				});
	}

	public interface XmlElementOperation {
		void onDelete();

		void onModify(XmlBean xmlBean);
	}
}
