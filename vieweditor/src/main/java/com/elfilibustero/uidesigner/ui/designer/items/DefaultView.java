package com.elfilibustero.uidesigner.ui.designer.items;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;

import com.elfilibustero.uidesigner.lib.tool.DynamicViewFactory;
import com.elfilibustero.uidesigner.lib.utils.Utils;
import com.elfilibustero.uidesigner.ui.designer.DesignerItem;
import com.google.android.material.textview.MaterialTextView;

public class DefaultView extends MaterialTextView implements DesignerItem {

    private String className;

    public DefaultView(Context context) {
        super(context);
        setEllipsize(TruncateAt.MIDDLE);
        setGravity(Gravity.CENTER);
        int color = Utils.isDarkMode(context) ? Color.WHITE : Color.BLACK;
        setTextColor(color);
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
        setText(DynamicViewFactory.getNameFromTag(className));
    }

    public String getClassName() {
        return className;
    }

    @Override
    public Class<?> getClassType() {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return View.class;
        }
    }
}
