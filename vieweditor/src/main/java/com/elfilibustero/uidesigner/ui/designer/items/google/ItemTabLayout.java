package com.elfilibustero.uidesigner.ui.designer.items.google;

import android.content.Context;
import android.view.MotionEvent;
import com.elfilibustero.uidesigner.ui.designer.DesignerItem;
import com.google.android.material.tabs.TabLayout;

public class ItemTabLayout extends TabLayout implements DesignerItem {

	public ItemTabLayout(Context context) {
		super(context);
		init();
	}

	private void init() {
		setClickable(false);
		setFocusable(false);
		addTab(newTab().setText("Tab 1"), true);
		addTab(newTab().setText("Tab 2"));
		addTab(newTab().setText("Tab 3"));
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
			return TabLayout.class;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return true;
	}
}
