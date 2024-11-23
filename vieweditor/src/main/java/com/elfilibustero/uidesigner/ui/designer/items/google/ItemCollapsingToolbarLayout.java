package com.elfilibustero.uidesigner.ui.designer.items.google;

import java.lang.reflect.Field;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import android.content.Context;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ItemCollapsingToolbarLayout extends CollapsingToolbarLayout {

	public ItemCollapsingToolbarLayout(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (ViewCompat.getFitsSystemWindows(this)) {
			int mode = MeasureSpec.getMode(heightMeasureSpec);
			int topInset = getTopSystemInset();

			if (mode == MeasureSpec.UNSPECIFIED && topInset > 0) {
				boolean hasFitsSystemWindowsFlagInChild = false;
				for (int i = 0; i < getChildCount(); i++) {
					View child = getChildAt(i);
					if (ViewCompat.getFitsSystemWindows(child)) {
						hasFitsSystemWindowsFlagInChild = true;
						break;
					}
				}
				if (hasFitsSystemWindowsFlagInChild) {
					int heightSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - topInset, MeasureSpec.EXACTLY);
					super.onMeasure(widthMeasureSpec, heightSpec);
				}
			}
		}
	}

	private int getTopSystemInset() {
		try {
			Field field = CollapsingToolbarLayout.class.getDeclaredField("lastInsets");
			field.setAccessible(true);
			WindowInsetsCompat insetsCompat = (WindowInsetsCompat) field.get(this);
			return insetsCompat != null ? insetsCompat.getSystemWindowInsetTop() : 0;
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
