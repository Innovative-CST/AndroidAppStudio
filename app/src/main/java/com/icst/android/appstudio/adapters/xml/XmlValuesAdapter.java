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

package com.icst.android.appstudio.adapters.xml;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.icst.android.appstudio.activities.manifest.AttributesManagerActivity;
import com.icst.android.appstudio.bottomsheet.XmlAttributeOperationBottomSheet;
import com.icst.android.appstudio.bottomsheet.XmlElementOperationBottomSheet;
import com.icst.android.appstudio.databinding.AdapterXmlAttributeBinding;
import com.icst.android.appstudio.databinding.AdapterXmlElementBinding;
import com.icst.android.appstudio.xml.XmlAttributeModel;
import com.icst.android.appstudio.xml.XmlModel;

public class XmlValuesAdapter extends RecyclerView.Adapter<XmlValuesAdapter.ViewHolder> {
	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
		}
	}

	private XmlModel xml;
	private String tag;
	private AttributesManagerActivity activity;

	public XmlValuesAdapter(XmlModel xml, String tag, AttributesManagerActivity activity) {
		this.xml = xml;
		this.tag = tag;
		this.activity = activity;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == 0) {
			AdapterXmlAttributeBinding binding = AdapterXmlAttributeBinding
					.inflate(LayoutInflater.from(parent.getContext()));
			RecyclerView.LayoutParams layoutParam = new RecyclerView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			binding.getRoot().setLayoutParams(layoutParam);
			return new ViewHolder(binding.getRoot());
		} else {
			AdapterXmlElementBinding binding = AdapterXmlElementBinding
					.inflate(LayoutInflater.from(parent.getContext()));
			RecyclerView.LayoutParams layoutParam = new RecyclerView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			binding.getRoot().setLayoutParams(layoutParam);
			return new ViewHolder(binding.getRoot());
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		if (getItemViewType(position) == 0) {
			AdapterXmlAttributeBinding binding = AdapterXmlAttributeBinding.bind(holder.itemView);

			if (xml.getAttributes() != null) {
				binding.attributeName.setText(xml.getAttributes().get(position).getAttribute());
				binding.attributeValue.setText(
						"=\""
								.concat(String.valueOf(xml.getAttributes().get(position).getAttributeValue()))
								.concat("\""));
				binding
						.getRoot()
						.setOnClickListener(
								v -> {
									XmlAttributeOperationBottomSheet sheet = new XmlAttributeOperationBottomSheet(
											activity,
											new XmlAttributeOperationBottomSheet.XmlAttributeOperation() {
												@Override
												public void onDeleteAttribute() {
													xml.getAttributes().remove(position);
													activity.load();
												}

												@Override
												public void onModifyAttribute(XmlAttributeModel xmlAttributeModel) {
													xml.getAttributes().set(position, xmlAttributeModel);
													activity.load();
												}
											},
											xml.getAttributes().get(position));
									sheet.show();
								});
			}
		} else {
			int pos = position;
			if (xml.getAttributes() != null) {
				pos = position - xml.getAttributes().size();
			}

			AdapterXmlElementBinding binding = AdapterXmlElementBinding.bind(holder.itemView);

			if (xml.getChildren() != null) {
				binding.name.setText(xml.getChildren().get(pos).getName());
				if (xml.getChildren().get(pos).getAttributes() != null) {
					for (int i = 0; i < xml.getChildren().get(pos).getAttributes().size(); ++i) {
						if (xml.getChildren().get(pos).getAttributes().get(i).getAttribute().equals(tag)) {
							binding.tag.setText(
									String.valueOf(
											xml.getChildren().get(pos).getAttributes().get(i).getAttributeValue()));
						}
					}
				}

				binding
						.getRoot()
						.setOnClickListener(
								v -> {
									int arrPos = position;
									if (xml.getAttributes() != null) {
										arrPos = position - xml.getAttributes().size();
									}
									Intent attributes = new Intent(activity, AttributesManagerActivity.class);

									if (xml.getChildren().get(arrPos).getName().equals("activity")) {
										attributes.putExtra("xmlModel", xml.getChildren().get(arrPos));
										attributes.putExtra("tag", "android:name");
									} else {
										attributes.putExtra("xmlModel", xml.getChildren().get(arrPos));
										attributes.putExtra("tag", "android:name");
									}
									activity.position = arrPos;
									activity.changesCallback.launch(attributes);
								});

				binding
						.getRoot()
						.setOnLongClickListener(
								v -> {
									int arrPos = position;
									if (xml.getAttributes() != null) {
										arrPos = position - xml.getAttributes().size();
									}
									XmlElementOperationBottomSheet sheet = new XmlElementOperationBottomSheet(
											activity,
											new XmlElementOperationBottomSheet.XmlElementOperation() {
												@Override
												public void onDelete() {
													int arrPosition = position;
													if (xml.getAttributes() != null) {
														arrPosition = position - xml.getAttributes().size();
													}
													xml.getChildren().remove(arrPosition);
													activity.load();
												}

												@Override
												public void onModify(XmlModel xmlModel) {
													int arrPosition = position;
													if (xml.getAttributes() != null) {
														arrPosition = position - xml.getAttributes().size();
													}
													xml.getChildren().set(arrPosition, xmlModel);
													activity.load();
												}
											},
											xml.getChildren().get(arrPos));
									sheet.show();
									return true;
								});
			}
		}
	}

	@Override
	public int getItemCount() {
		if (xml.getAttributes() != null && xml.getChildren() != null) {
			return xml.getAttributes().size() + xml.getChildren().size();
		} else if (xml.getAttributes() != null) {
			return xml.getAttributes().size();
		} else if (xml.getChildren() != null) {
			return xml.getChildren().size();
		}

		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		if (xml.getAttributes() != null) {
			if ((position + 1) > xml.getAttributes().size()) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 1;
		}
	}

	public XmlModel getXml() {
		return this.xml;
	}

	public void setXml(XmlModel xml) {
		this.xml = xml;
	}
}
