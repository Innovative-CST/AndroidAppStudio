package com.elfilibustero.uidesigner.ui.designer.items.androidx;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.elfilibustero.uidesigner.ui.designer.DesignerItem;

public class ItemCoordinatorLayout extends CoordinatorLayout implements DesignerItem {

	private View dummyAnchor;

	public ItemCoordinatorLayout(@NonNull Context context) {
		super(context);
		init();
	}

	private void init() {
		dummyAnchor = new View(getContext());
		dummyAnchor.setVisibility(View.GONE);
		addView(dummyAnchor);
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
			return CoordinatorLayout.class;
		}
	}

	public static class LayoutParams extends CoordinatorLayout.LayoutParams {

		private int anchorId = View.NO_ID;

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		@Override
		public int getAnchorId() {
			return anchorId;
		}

		@Override
		public void setAnchorId(int id) {
			anchorId = id;
			super.setAnchorId(id);
		}
	}
}
