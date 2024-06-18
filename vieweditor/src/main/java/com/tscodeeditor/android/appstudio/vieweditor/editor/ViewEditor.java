/*
 * This file is part of Android AppStudio [https://github.com/TS-Code-Editor/AndroidAppStudio].
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

package com.tscodeeditor.android.appstudio.vieweditor.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.elfilibustero.uidesigner.lib.builder.LayoutBuilder;
import com.elfilibustero.uidesigner.lib.handler.AttributeSetHandler;
import com.elfilibustero.uidesigner.lib.view.ShadowView;
import com.elfilibustero.uidesigner.ui.designer.DesignerItem;
import com.elfilibustero.uidesigner.ui.designer.DesignerListItem;
import com.elfilibustero.uidesigner.ui.designer.LayoutDesigner;
import com.elfilibustero.uidesigner.ui.designer.items.DefaultView;
import com.tscodeeditor.android.appstudio.vieweditor.R;
import com.tscodeeditor.android.appstudio.vieweditor.models.AttributesModel;
import com.tscodeeditor.android.appstudio.vieweditor.models.LayoutModel;
import com.tscodeeditor.android.appstudio.vieweditor.models.ViewModel;
import java.util.ArrayList;
import java.util.Map;

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

        if (getEditor().getChildAt(i) instanceof DesignerItem designerItem) {
          viewModel.setClass(designerItem.getClassType().getName());
        } else {
          viewModel.setClass(getEditor().getChildAt(i).getClass().getName());
        }

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
          }
          viewModel.setAttributes(attributes);
        }

        if (!(getEditor().getChildAt(i) instanceof DesignerListItem)) {
          if (getEditor().getChildAt(i) instanceof ViewGroup viewGroup) {
            viewModel.setChilds(prepareLayoutModel(viewGroup));
          }
        }

        LayoutModel layout = layoutModel;
        layout.setView(viewModel);
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

  private ArrayList<ViewModel> prepareLayoutModel(ViewGroup view) {
    ArrayList<ViewModel> result = new ArrayList<ViewModel>();
    for (int i = 0; i < view.getChildCount(); ++i) {
      if (!(view.getChildAt(i) instanceof ShadowView)) {
        ViewModel viewModel = new ViewModel();

        if (view.getChildAt(i) instanceof DesignerItem designerItem) {
          viewModel.setClass(designerItem.getClassType().getName());
        } else {
          viewModel.setClass(view.getChildAt(i).getClass().getName());
        }

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
          }
          viewModel.setAttributes(attributes);
        }
        if (!(view.getChildAt(i) instanceof DesignerListItem)) {
          if (view.getChildAt(i) instanceof ViewGroup viewGroup) {
            viewModel.setChilds(prepareLayoutModel(viewGroup));
          }
        }
        result.add(viewModel);
      }
    }
    return result.size() == 0 ? null : result;
  }
}
