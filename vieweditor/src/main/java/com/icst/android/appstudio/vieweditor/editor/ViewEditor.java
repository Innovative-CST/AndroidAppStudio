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

package com.icst.android.appstudio.vieweditor.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.elfilibustero.uidesigner.lib.builder.LayoutBuilder;
import com.elfilibustero.uidesigner.lib.handler.AttributeSetHandler;
import com.elfilibustero.uidesigner.lib.utils.Constants;
import com.elfilibustero.uidesigner.lib.view.ShadowView;
import com.elfilibustero.uidesigner.ui.designer.LayoutDesigner;
import com.icst.android.appstudio.vieweditor.models.AttributesModel;
import com.icst.android.appstudio.vieweditor.models.LayoutModel;
import com.icst.android.appstudio.vieweditor.models.ViewModel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class ViewEditor extends LayoutDesigner {

	private LayoutModel layoutModel;

	public ViewEditor(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LayoutModel getLayoutModel() {
		return getPreparedLayoutModel();
	}

	public void setLayoutModel(LayoutModel layoutModel) {
		this.layoutModel = layoutModel;
		setLayoutFromXml(layoutModel.getCode());
	}

	private LayoutModel getPreparedLayoutModel() {
		for (int i = 0; i < getEditor().getChildCount(); ++i) {
			if (!(getEditor().getChildAt(i) instanceof ShadowView)) {

				ViewModel viewModel = new ViewModel();
				viewModel.setRootElement(true);

				HashMap<String, String> identifiableView = new HashMap<String, String>();

				viewModel.setClass(LayoutBuilder.getClassName(getEditor().getChildAt(i)));

				AttributeSetHandler handler = getAttributeSetHandler();
				Map<String, Object> map = handler.get(getEditor().getChildAt(i));

				if (map != null) {
					ArrayList<AttributesModel> attributes = new ArrayList<AttributesModel>();
					for (Map.Entry<String, Object> entry : map.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();

						AttributesModel attr = new AttributesModel();
						attr.setAttribute(key);
						attr.setAttributeValue(value);
						attributes.add(attr);

						if (key.equals("android:id")) {
							String id = getViewIdentifier().getIdFromView(getEditor().getChildAt(i));

							if (!identifiableView.containsKey(id)) {
								identifiableView.put(id, LayoutBuilder.getClassName(getEditor().getChildAt(i)));
							}
						}
					}
					viewModel.setAttributes(attributes);
				}

				if (getEditor().getChildAt(i) instanceof ViewGroup viewGroup) {
					if (!(Constants.isExcludedViewGroup(viewGroup))) {
						viewModel.setChilds(prepareLayoutModel(viewGroup, identifiableView));
					}
				}

				LayoutModel layout = layoutModel;
				layout.setView(viewModel);
				layout.setIdentifiableView(identifiableView);
				layout.setAndroidNameSpaceUsed(
						LayoutBuilder.hasNamespace(handler.getViewMap(), "android:"));
				layout.setAppNameSpaceUsed(LayoutBuilder.hasNamespace(handler.getViewMap(), "app:"));
				layout.setToolsNameSpaceUsed(LayoutBuilder.hasNamespace(handler.getViewMap(), "tools:"));

				return layout;
			}
		}
		LayoutModel layout = layoutModel;
		layout.setView(null);
		return layout;
	}

	private ArrayList<ViewModel> prepareLayoutModel(
			ViewGroup view, HashMap<String, String> identifiableView) {
		ArrayList<ViewModel> result = new ArrayList<ViewModel>();
		for (int i = 0; i < view.getChildCount(); ++i) {
			if (!(view.getChildAt(i) instanceof ShadowView)) {
				ViewModel viewModel = new ViewModel();

				viewModel.setClass(LayoutBuilder.getClassName(view.getChildAt(i)));

				AttributeSetHandler handler = getAttributeSetHandler();
				Map<String, Object> map = handler.get(view.getChildAt(i));

				if (map != null) {
					ArrayList<AttributesModel> attributes = new ArrayList<AttributesModel>();
					for (Map.Entry<String, Object> entry : map.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();

						AttributesModel attr = new AttributesModel();
						attr.setAttribute(key);
						attr.setAttributeValue(value);
						attributes.add(attr);

						if (key.equals("android:id")) {
							String id = getViewIdentifier().getIdFromView(view.getChildAt(i));

							if (!identifiableView.containsKey(id)) {
								identifiableView.put(id, LayoutBuilder.getClassName(view.getChildAt(i)));
							}
						}
					}
					viewModel.setAttributes(attributes);
				}
				if (view.getChildAt(i) instanceof ViewGroup viewGroup) {
					if (!(Constants.isExcludedViewGroup(viewGroup))) {
						viewModel.setChilds(prepareLayoutModel(viewGroup, identifiableView));
					}
				}
				result.add(viewModel);
			}
		}
		return result.size() == 0 ? null : result;
	}
}
