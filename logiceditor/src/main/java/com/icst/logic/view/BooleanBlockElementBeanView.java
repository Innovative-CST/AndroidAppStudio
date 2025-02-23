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

package com.icst.logic.view;

import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.BooleanBlockBean;
import com.icst.android.appstudio.beans.BooleanBlockElementBean;
import com.icst.android.appstudio.beans.ExpressionBlockBean;
import com.icst.android.appstudio.beans.utils.BlockBeanUtils;
import com.icst.logic.block.view.ActionBlockBeanView;
import com.icst.logic.block.view.BlockBeanView;
import com.icst.logic.block.view.BooleanBlockView;
import com.icst.logic.block.view.ExpressionBlockBeanView;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.utils.BlockShapesUtils;
import com.icst.logic.utils.UnitUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

public class BooleanBlockElementBeanView extends LinearLayout {

	private BooleanBlockView booleanBlockView;
	private LogicEditorSpinner spinner;
	private BlockBeanView blockView;
	private BooleanBlockElementBean mBooleanBlockElementBean;
	private LogicEditorConfiguration logicEditorConfiguration;
	private LogicEditorView logicEditor;
	private LinearLayout layout;
	private boolean enableHighlighterColor;

	public BooleanBlockElementBeanView(
			Context context,
			BlockBeanView blockView,
			BooleanBlockElementBean mBooleanBlockElementBean,
			LogicEditorConfiguration configuration,
			LogicEditorView logicEditor) {
		super(context);
		this.mBooleanBlockElementBean = mBooleanBlockElementBean;
		this.blockView = blockView;
		this.logicEditorConfiguration = configuration;
		this.logicEditor = logicEditor;

		layout = new LinearLayout(context) {
			@Override
			protected LayoutParams generateDefaultLayoutParams() {
				LayoutParams lp = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.setMargins(
						UnitUtils.dpToPx(getContext(), 4),
						0,
						UnitUtils.dpToPx(getContext(), 4),
						0);
				return lp;
			}
		};
		layout.setGravity(Gravity.CENTER_VERTICAL);
		layout.setPadding(2, 2, 2, 2);

		setMinimumWidth(UnitUtils.dpToPx(getContext(), 30));
		setGravity(Gravity.CENTER_VERTICAL);
		setWillNotDraw(false);
		setOnClickListener(
				v -> {
					showSpinnerItems();
				});
		BlockBean block = null;
		if (blockView instanceof ExpressionBlockBeanView eBlockView) {
			block = eBlockView.getExpressionBlockBean();
		} else if (blockView instanceof ActionBlockBeanView aBlockView) {
			block = aBlockView.getActionBlockBean();
		}
		spinner = new LogicEditorSpinner(
				context,
				mBooleanBlockElementBean,
				blockView,
				Color.parseColor(block.getColor()));
		addView(layout);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int totalHeight = 0;
		int maxWidth = 0;

		// Measure each child
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			totalHeight += child.getMeasuredHeight();
			maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
		}

		setMeasuredDimension(
				resolveSize(maxWidth, widthMeasureSpec),
				resolveSize(totalHeight, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int totalWidth = 0;
		int currentTop = 0;

		// Layout each child
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			int left = getPaddingLeft();
			int top = currentTop;
			int right = left + child.getMeasuredWidth();
			int bottom = top + child.getMeasuredHeight();

			child.layout(left, top, right, bottom);

			currentTop += child.getMeasuredHeight();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (enableHighlighterColor) {
			BlockShapesUtils.drawBooleanBlockHighlighter(
					canvas, getContext(), getMeasuredWidth(), getMeasuredHeight(), Color.BLACK);
		} else {
			BlockShapesUtils.drawBooleanBlockElement(
					canvas, getContext(), getMeasuredWidth(), getMeasuredHeight(), Color.BLACK);
		}
	}

	private void showSpinnerItems() {
		// TODO: Show spinner items
	}

	private void init() {
		layout.removeAllViews();
		if (mBooleanBlockElementBean.getBooleanBlock() != null) {
			createBooleanBlockView();
			configureBooleanBlockView();
			addBooleanBlockViewToLayout();
			showBooleanBlock();
		} else {
			addSpinnerViewToLayout();
			showSpinner();
			refreshSpinner();
		}
		invalidate();
	}

	private void addSpinnerViewToLayout() {
		if (spinner.getParent() == null) {
			layout.addView(spinner);
		}
		layout.removeView(booleanBlockView);
	}

	private void showSpinner() {
		spinner.setVisibility(VISIBLE);
	}

	private void refreshSpinner() {
		spinner.requestLayout();
	}

	public void setValue(boolean bool) {
		mBooleanBlockElementBean.setValue(bool);
		booleanBlockView = null;
		init();
	}

	public void setValue(BooleanBlockBean boolBlock) {
		if (boolBlock == null)
			return;
		if (booleanBlockView != null) {
			booleanBlockView.setOnTouchListener(null);
		}
		mBooleanBlockElementBean.setValue(boolBlock);
		init();
	}

	private void createBooleanBlockView() {
		booleanBlockView = new BooleanBlockView(
				getContext(),
				mBooleanBlockElementBean.getBooleanBlock(),
				logicEditorConfiguration,
				logicEditor) {
			@Override
			public void setVisibility(int arg0) {
				super.setVisibility(arg0);
				handleBooleanBlockVisibilityChange();
			}
		};
	}

	public void handleBooleanBlockVisibilityChange() {
		int visibility = booleanBlockView.getVisibility();
		if (visibility == GONE) {
			if (spinner.getVisibility() == GONE) {
				spinner.setVisibility(VISIBLE);
			}
			if (spinner.getParent() == null) {
				layout.addView(spinner);
			}
			refreshSpinner();
		} else if (visibility == VISIBLE) {
			layout.removeView(spinner);
		}
	}

	private void configureBooleanBlockView() {
		LayoutParams lp = generateDefaultLayoutParams();
		lp.setMargins(0, 0, 0, 0);
		booleanBlockView.setLayoutParams(lp);
		booleanBlockView.setInsideCanva(true);
	}

	private void addBooleanBlockViewToLayout() {
		if (booleanBlockView.getParent() == null) {
			layout.addView(booleanBlockView);
		}
	}

	private void showBooleanBlock() {
		booleanBlockView.setVisibility(VISIBLE);
	}

	private void showBooleanBlockIfInvisible() {
		int visibility = booleanBlockView.getVisibility();
		if (visibility == INVISIBLE) {
			booleanBlockView.setVisibility(VISIBLE);
		}
	}

	public boolean canDrop(BlockBean block, float x, float y) {
		if (block == null)
			return false;
		if (block != mBooleanBlockElementBean.getBooleanBlock()) {
			if (booleanBlockView != null) {
				if (booleanBlockView.canDrop(block, x, y))
					return true;
			}
		}
		if (block instanceof ExpressionBlockBean expressionBlock) {
			return BlockBeanUtils.arrayContainsDatatypeBeans(
					expressionBlock.getReturnDatatypes(),
					mBooleanBlockElementBean.getAcceptedReturnType());
		}
		return false;
	}

	public void highlight(BlockBean block, float x, float y) {
		if (block != mBooleanBlockElementBean.getBooleanBlock()) {
			if (mBooleanBlockElementBean.getBooleanBlock() != null) {
				if (booleanBlockView.canDrop(block, x, y)) {
					booleanBlockView.highlightNearestTarget(block, x, y);
					return;
				}
			}
		}
		if (block instanceof BooleanBlockBean booleanBlockBean) {
			if (booleanBlockView != null) {
				if (booleanBlockView.getVisibility() != GONE) {
					booleanBlockView.setVisibility(INVISIBLE);
				}
			}

			if (spinner != null) {
				spinner.setVisibility(INVISIBLE);
			}
			NearestTargetHighlighterView highlighter = new NearestTargetHighlighterView(getContext(), block);
			logicEditor.setDummyHighlighter(highlighter);
			addView(highlighter);
			enableHighlighterColor = true;
			invalidate();
		}
	}

	public void dropBlockIfAllowed(BlockBean block, float x, float y) {
		if (block != mBooleanBlockElementBean.getBooleanBlock()) {
			if (mBooleanBlockElementBean.getBooleanBlock() != null) {
				if (booleanBlockView.canDrop(block, x, y)) {
					booleanBlockView.drop(block, x, y);
					return;
				}
			}
		}
		if (block instanceof BooleanBlockBean booleanBlockBean) {
			setValue(booleanBlockBean);
		}
	}

	private void handleHighlighterRemoval() {
		if (mBooleanBlockElementBean.getBooleanBlock() != null) {
			showBooleanBlockIfInvisible();
			if (booleanBlockView.getVisibility() == GONE) {
				showSpinner();
				refreshSpinner();
			}
		} else {
			showSpinner();
			refreshSpinner();
		}
		enableHighlighterColor = false;
		invalidate();
	}

	@Override
	public void removeView(View view) {
		super.removeView(view);
		if (view instanceof NearestTargetHighlighterView) {
			handleHighlighterRemoval();
		}
	}
}
