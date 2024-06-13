package com.elfilibustero.uidesigner.ui.designer.items.google;

import android.content.Context;
import android.widget.FrameLayout;

import com.elfilibustero.uidesigner.lib.utils.Utils;
import com.elfilibustero.uidesigner.ui.designer.DesignerItem;
import com.google.android.material.textfield.TextInputLayout;

public class ItemTextInputLayout extends FrameLayout implements DesignerItem {
    
    public ItemTextInputLayout(Context context) {
        super(context);
        init(context);
    }
    
    private void init(Context context) {
        setMinimumWidth((int) Utils.getDip(context, 32.0f));
        setMinimumHeight((int) Utils.getDip(context, 32.0f));
    }
    
    private String className;

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public Class<?> getClassType() {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return TextInputLayout.class;
        }
    }
}
