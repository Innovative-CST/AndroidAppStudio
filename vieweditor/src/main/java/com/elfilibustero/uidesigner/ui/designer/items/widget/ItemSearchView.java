package com.elfilibustero.uidesigner.ui.designer.items.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.SearchView;

import com.elfilibustero.uidesigner.ui.designer.DesignerItem;

public class ItemSearchView extends SearchView implements DesignerItem {

    public ItemSearchView(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        setFocusable(false);
        setFocusableInTouchMode(false);
        setIconifiedByDefault(false);
        setClickable(false);
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
            return SearchView.class;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return true;
    }
}
