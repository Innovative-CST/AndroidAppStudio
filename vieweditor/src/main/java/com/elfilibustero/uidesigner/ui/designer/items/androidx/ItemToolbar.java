package com.elfilibustero.uidesigner.ui.designer.items.androidx;

import android.content.Context;
import android.view.MotionEvent;

import com.elfilibustero.uidesigner.ui.designer.DesignerItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.tscodeeditor.android.appstudio.vieweditor.R;

public class ItemToolbar extends MaterialToolbar implements DesignerItem {

    public ItemToolbar(Context context) {
        super(context);
        initialize(context);
    }

    private void initialize(Context context) {
        setFocusable(false);
        setClickable(false);
        setTitleTextAppearance(context, R.style.ItemToolbar_TitleText);
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
            return MaterialToolbar.class;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return true;
    }
}
