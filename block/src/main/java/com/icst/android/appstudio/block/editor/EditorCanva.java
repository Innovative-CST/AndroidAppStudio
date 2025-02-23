/*
 *  This file is part of AndroidAppStudio.
 *
 *  AndroidAppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidAppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidAppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.android.appstudio.block.editor;

import com.icst.android.appstudio.block.R;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.utils.BlockMarginConstants;
import com.icst.android.appstudio.block.utils.UnitUtils;
import com.icst.android.appstudio.block.view.BlockView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class EditorCanva extends EditorScrollView {
	private boolean b1 = false;

	private Event event;
	public LinearLayout attachedBlockLayout;

	public EditorCanva(final Context context, final AttributeSet set) {
		super(context, set);
		setAllowScroll(true);
		setClipChildren(true);
	}

	public void setUpDimension() {
		getLayoutParams().width = getRight() - getLeft();
		getLayoutParams().height = getBottom() - getTop();

		final int childCount = getChildCount();
		int width = 0;
		int width2 = 0;
		width = getLayoutParams().width;
		width2 = getLayoutParams().height;
		int max;
		int max2;

		for (int i = 0; i < getChildCount(); ++i) {
			final View child = getChildAt(i);
			final float x = child.getX();
			max = Math.max(
					(int) (child.getX()
							+ child.getPaddingLeft()
							+ child.getWidth()
							+ child.getPaddingRight()
							+ 150),
					width);
			width = max;
		}
		for (int i = 0; i < getChildCount(); ++i) {
			final View child = getChildAt(i);
			final float x = child.getX();
			max2 = Math.max(
					(int) (child.getY()
							+ child.getPaddingTop()
							+ child.getHeight()
							+ child.getPaddingBottom()
							+ 150),
					width2);
			width2 = max2;
		}
		getLayoutParams().width = width;
		getLayoutParams().height = width2;
		requestLayout();
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		super.onLayout(arg0, arg1, arg2, arg3, arg4);
		setUpDimension();
	}

	@Override
	protected void onScrollChanged(int arg0, int arg1, int arg2, int arg3) {
		super.onScrollChanged(arg0, arg1, arg2, arg3);
		setUpDimension();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent motion) {
		if (!super.onInterceptTouchEvent(motion)) {
			setUpDimension();
		}
		return super.onInterceptTouchEvent(motion);
	}

	@Override
	public boolean onTouchEvent(MotionEvent motion) {
		if (!super.onTouchEvent(motion)) {
			setUpDimension();
		}
		return super.onTouchEvent(motion);
	}

	public void initEditor(Event event, EventEditor editor) {
		this.event = event;
		attachedBlockLayout = new LinearLayout(getContext()) {

			@Override
			public void removeView(View view) {
				if (this.indexOfChild(view) == 0) {
					if (super.getChildAt(1) != null) {
						LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);

						super.getChildAt(1).setLayoutParams(blockParams);
					}
				}
				super.removeView(view);
			}

			@Override
			public void addView(View view) {
				super.addView(view);
				if (super.getChildCount() > 1) {
					LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);

					blockParams.setMargins(
							0, UnitUtils.dpToPx(getContext(), BlockMarginConstants.regularBlockMargin), 0, 0);
					view.setLayoutParams(blockParams);
				} else {
					view.setLayoutParams(
							new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.LayoutParams.WRAP_CONTENT));
				}
			}

			@Override
			public void addView(View view, int index) {
				super.addView(view, index);
				if (index != 0) {
					LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);

					blockParams.setMargins(
							0, UnitUtils.dpToPx(getContext(), BlockMarginConstants.regularBlockMargin), 0, 0);
					view.setLayoutParams(blockParams);
				} else {
					view.setLayoutParams(
							new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.LayoutParams.WRAP_CONTENT));

					if (super.getChildCount() > 1) {
						LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);

						blockParams.setMargins(
								0,
								UnitUtils.dpToPx(getContext(), BlockMarginConstants.regularBlockMargin),
								0,
								0);
						super.getChildAt(1).setLayoutParams(blockParams);
					}
				}
			}
		};
		attachedBlockLayout.setLayoutParams(
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		attachedBlockLayout.setOrientation(LinearLayout.VERTICAL);
		attachedBlockLayout.setId(R.id.attachedBlockLayout);

		BlockView defineBlock = new BlockView(editor, getContext(), event.getEventTopBlock(), editor.isDarkMode());
		defineBlock.setLayoutParams(
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		attachedBlockLayout.addView(defineBlock);
		addView(attachedBlockLayout);
		loadBlock(editor);
	}

	public void loadBlock(EventEditor editor) {
		if (event.getBlockModels() == null)
			return;
		if (attachedBlockLayout == null)
			return;

		for (int i = 0; i < event.getBlockModels().size(); ++i) {
			BlockView blockView = new BlockView(editor, getContext(), event.getBlockModels().get(i),
					editor.isDarkMode());
			LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			blockParams.setMargins(
					0, UnitUtils.dpToPx(getContext(), BlockMarginConstants.regularBlockMargin), 0, 0);

			blockView.setLayoutParams(blockParams);
			blockView.setEnableDragDrop(true);
			blockView.setEnableEditing(true);
			blockView.setInsideEditor(true);
			blockView.setRootBlock(true);
			attachedBlockLayout.addView(blockView);
		}
	}

	public Event getEvent() {
		return this.event;
	}
}
