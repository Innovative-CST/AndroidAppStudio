package com.elfilibustero.uidesigner.ui.designer.items.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.CalendarView;
import com.elfilibustero.uidesigner.ui.designer.DesignerItem;

public class ItemCalendarView extends CalendarView implements DesignerItem {

	public ItemCalendarView(Context context) {
		super(context);
		initialize();
	}

	private void initialize() {
		setFocusable(false);
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
			return CalendarView.class;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return true;
	}
}
