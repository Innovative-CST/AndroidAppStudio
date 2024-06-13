package com.elfilibustero.uidesigner.ui.designer.items.widget;

import android.content.Context;
import android.widget.Button;

import com.elfilibustero.uidesigner.ui.designer.DesignerItem;

public class ItemButton extends Button implements DesignerItem {

    public ItemButton(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        setFocusable(false);
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
            return Button.class;
        }
    }
}
