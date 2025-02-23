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

package com.icst.android.appstudio.adapters.xml;

import com.icst.android.appstudio.activities.manifest.AttributesManagerActivity;
import com.icst.android.appstudio.bottomsheet.XmlAttributeOperationBottomSheet;
import com.icst.android.appstudio.bottomsheet.XmlElementOperationBottomSheet;
import com.icst.android.appstudio.databinding.AdapterXmlAttributeBinding;
import com.icst.android.appstudio.databinding.AdapterXmlElementBinding;
import com.icst.android.appstudio.xml.XmlAttributeModel;
import com.icst.android.appstudio.xml.XmlModel;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

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
