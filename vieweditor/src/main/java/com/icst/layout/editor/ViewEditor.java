/*
 *  This file is part of Block IDLE.
 *
 *  Block IDLE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Block IDLE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Block IDLE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.layout.editor;

import com.icst.blockidle.beans.LayoutBean;
import com.icst.blockidle.beans.ViewAttributeBean;
import com.icst.blockidle.beans.ViewBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.elfilibustero.uidesigner.lib.builder.LayoutBuilder;
import com.elfilibustero.uidesigner.lib.handler.AttributeSetHandler;
import com.elfilibustero.uidesigner.lib.utils.Constants;
import com.elfilibustero.uidesigner.lib.view.ShadowView;
import com.elfilibustero.uidesigner.ui.designer.LayoutDesigner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class ViewEditor extends LayoutDesigner {

	private LayoutBean layoutBean;

	public ViewEditor(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LayoutBean getLayoutBean() {
		return getPreparedLayoutBean();
	}

	public void setLayoutBean(LayoutBean layoutBean) {
		this.layoutBean = layoutBean;
		setLayoutFromXml(layoutBean.getCode());
	}

	private LayoutBean getPreparedLayoutBean() {
		for (int i = 0; i < getEditor().getChildCount(); ++i) {
			if (!(getEditor().getChildAt(i) instanceof ShadowView)) {

				ViewBean viewBean = new ViewBean();
				viewBean.setRootElement(true);

				HashMap<String, String> identifiableView = new HashMap<String, String>();

				viewBean.setClass(LayoutBuilder.getClassName(getEditor().getChildAt(i)));

				AttributeSetHandler handler = getAttributeSetHandler();
				Map<String, Object> map = handler.get(getEditor().getChildAt(i));

				if (map != null) {
					ArrayList<ViewAttributeBean> attributes = new ArrayList<ViewAttributeBean>();
					for (Map.Entry<String, Object> entry : map.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();

						ViewAttributeBean attr = new ViewAttributeBean();
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
					viewBean.setAttributes(attributes);
				}

				if (getEditor().getChildAt(i) instanceof ViewGroup viewGroup) {
					if (!(Constants.isExcludedViewGroup(viewGroup))) {
						viewBean.setChilds(prepareLayoutBean(viewGroup, identifiableView));
					}
				}

				LayoutBean layout = layoutBean;
				layout.setView(viewBean);
				layout.setIdentifiableView(identifiableView);
				layout.setAndroidNameSpaceUsed(
						LayoutBuilder.hasNamespace(handler.getViewMap(), "android:"));
				layout.setAppNameSpaceUsed(LayoutBuilder.hasNamespace(handler.getViewMap(), "app:"));
				layout.setToolsNameSpaceUsed(LayoutBuilder.hasNamespace(handler.getViewMap(), "tools:"));

				return layout;
			}
		}
		LayoutBean layout = layoutBean;
		layout.setView(null);
		return layout;
	}

	private ArrayList<ViewBean> prepareLayoutBean(
			ViewGroup view, HashMap<String, String> identifiableView) {
		ArrayList<ViewBean> result = new ArrayList<ViewBean>();
		for (int i = 0; i < view.getChildCount(); ++i) {
			if (!(view.getChildAt(i) instanceof ShadowView)) {
				ViewBean viewBean = new ViewBean();

				viewBean.setClass(LayoutBuilder.getClassName(view.getChildAt(i)));

				AttributeSetHandler handler = getAttributeSetHandler();
				Map<String, Object> map = handler.get(view.getChildAt(i));

				if (map != null) {
					ArrayList<ViewAttributeBean> attributes = new ArrayList<ViewAttributeBean>();
					for (Map.Entry<String, Object> entry : map.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();

						ViewAttributeBean attr = new ViewAttributeBean();
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
					viewBean.setAttributes(attributes);
				}
				if (view.getChildAt(i) instanceof ViewGroup viewGroup) {
					if (!(Constants.isExcludedViewGroup(viewGroup))) {
						viewBean.setChilds(prepareLayoutBean(viewGroup, identifiableView));
					}
				}
				result.add(viewBean);
			}
		}
		return result.size() == 0 ? null : result;
	}
}
