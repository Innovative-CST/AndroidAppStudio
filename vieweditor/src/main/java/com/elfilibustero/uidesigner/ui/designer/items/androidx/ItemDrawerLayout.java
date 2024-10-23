package com.elfilibustero.uidesigner.ui.designer.items.androidx;

import android.content.Context;
import androidx.drawerlayout.widget.DrawerLayout;
import com.elfilibustero.uidesigner.ui.designer.DesignerItem;

public class ItemDrawerLayout extends DrawerLayout implements DesignerItem {

	public ItemDrawerLayout(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
			return DrawerLayout.class;
		}
	}
}
