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
import com.icst.android.appstudio.beans.ExpressionBlockBean;
import com.icst.android.appstudio.beans.GeneralExpressionBlockBean;
import com.icst.android.appstudio.beans.GeneralExpressionBlockElementBean;
import com.icst.android.appstudio.beans.NumericBlockBean;
import com.icst.android.appstudio.beans.StringBlockBean;
import com.icst.logic.block.view.ActionBlockBeanView;
import com.icst.logic.block.view.BlockBeanView;
import com.icst.logic.block.view.BooleanBlockView;
import com.icst.logic.block.view.ExpressionBlockBeanView;
import com.icst.logic.block.view.GeneralExpressionBlockView;
import com.icst.logic.block.view.NumericBlockBeanView;
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

public class GeneralBlockElementView extends LinearLayout {

	private Context context;
	private ExpressionBlockBeanView expressionBlockView;
	private LogicEditorSpinner spinner;
	private BlockBeanView blockView;
	private GeneralExpressionBlockElementBean mGeneralExpressionBlockElementBean;
	private LogicEditorConfiguration logicEditorConfiguration;
	private LogicEditorView logicEditor;
	private LinearLayout layout;
	private boolean enableHighlighterColor;

	public GeneralBlockElementView(
			Context context,
			BlockBeanView blockView,
			GeneralExpressionBlockElementBean mGeneralExpressionBlockElementBean,
			LogicEditorConfiguration configuration,
			LogicEditorView logicEditor) {
		super(context);
		this.context = context;
		this.mGeneralExpressionBlockElementBean = mGeneralExpressionBlockElementBean;
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
				mGeneralExpressionBlockElementBean,
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
			BlockShapesUtils.drawGeneralExpressionBlockHighlighter(
					canvas, getContext(), getMeasuredWidth(), getMeasuredHeight(), Color.BLACK);
		} else {
			boolean isBlockViewNull = expressionBlockView == null;
			boolean blockAttached = false;
			if (!isBlockViewNull) {
				blockAttached = expressionBlockView.getParent() != null;
			}
			if (!blockAttached) {
				BlockShapesUtils.drawGeneralBlockElement(
						canvas, getContext(), getMeasuredWidth(), getMeasuredHeight(), Color.BLACK);
			} else {
				ExpressionBlockBean currentBlockBean = mGeneralExpressionBlockElementBean.getExpressionBlockBean();
				if (currentBlockBean instanceof StringBlockBean stringBlockBean) {
					// Not done yet!
				} else if (currentBlockBean instanceof BooleanBlockBean booleanBlockBean) {
					BlockShapesUtils.drawBooleanBlock(
							canvas, getContext(), getMeasuredWidth(), getMeasuredHeight(), Color.BLACK);
				} else if (currentBlockBean instanceof NumericBlockBean numericBlockBean) {
					BlockShapesUtils.drawNumericBlock(
							canvas, getContext(), getMeasuredWidth(), getMeasuredHeight(), Color.BLACK);
				} else if (currentBlockBean instanceof GeneralExpressionBlockBean generalExpressionBlockBean) {
					BlockShapesUtils.drawGeneralBlockElement(
							canvas, getContext(), getMeasuredWidth(), getMeasuredHeight(), Color.BLACK);
				}
			}
		}
	}

	private void showSpinnerItems() {
		// TODO: Show spinner items
	}

	private void init() {
		layout.removeAllViews();
		if (mGeneralExpressionBlockElementBean.getExpressionBlockBean() != null) {
			createExpressionBlockView();
			configureExpressionBlockView();
			addExpressionBlockViewToLayout();
			showExpressionBlock();
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
		layout.removeView(expressionBlockView);
	}

	private void showSpinner() {
		spinner.setVisibility(VISIBLE);
	}

	private void refreshSpinner() {
		spinner.requestLayout();
	}

	public void setValue(String value) {
		mGeneralExpressionBlockElementBean.setValue(value);
		expressionBlockView = null;
		init();
	}

	public void setValue(ExpressionBlockBean expressionBlockBean) {
		if (expressionBlockBean == null)
			return;
		if (expressionBlockView != null) {
			expressionBlockView.setOnTouchListener(null);
		}
		mGeneralExpressionBlockElementBean.setValue(expressionBlockBean);
		init();
	}

	private void createExpressionBlockView() {
		ExpressionBlockBean expressionBlockBean = mGeneralExpressionBlockElementBean.getExpressionBlockBean();
		if (expressionBlockBean instanceof StringBlockBean stringBlockBean) {
			expressionBlockView = new StringBlockBeanView(context, stringBlockBean, logicEditorConfiguration,
					logicEditor) {
				@Override
				public void setVisibility(int arg0) {
					super.setVisibility(arg0);
					handleExpressionBlockVisibilityChange();
				}
			};
		} else if (expressionBlockBean instanceof BooleanBlockBean booleanBlockBean) {
			expressionBlockView = new BooleanBlockView(context, booleanBlockBean, logicEditorConfiguration,
					logicEditor) {
				@Override
				public void setVisibility(int arg0) {
					super.setVisibility(arg0);
					handleExpressionBlockVisibilityChange();
				}
			};
		} else if (expressionBlockBean instanceof NumericBlockBean numericBlockBean) {
			expressionBlockView = new NumericBlockBeanView(context, numericBlockBean, logicEditorConfiguration,
					logicEditor) {
				@Override
				public void setVisibility(int arg0) {
					super.setVisibility(arg0);
					handleExpressionBlockVisibilityChange();
				}
			};
		} else if (expressionBlockBean instanceof GeneralExpressionBlockBean generalExpressionBlockBean) {
			expressionBlockView = new GeneralExpressionBlockView(context, generalExpressionBlockBean,
					logicEditorConfiguration, logicEditor) {
				@Override
				public void setVisibility(int arg0) {
					super.setVisibility(arg0);
					handleExpressionBlockVisibilityChange();
				}
			};
		}
	}

	public void handleExpressionBlockVisibilityChange() {
		int visibility = expressionBlockView.getVisibility();
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

	private void configureExpressionBlockView() {
		LayoutParams lp = generateDefaultLayoutParams();
		lp.setMargins(0, 0, 0, 0);
		expressionBlockView.setLayoutParams(lp);
		expressionBlockView.setInsideCanva(true);
	}

	private void addExpressionBlockViewToLayout() {
		if (expressionBlockView.getParent() == null) {
			layout.addView(expressionBlockView);
		}
	}

	private void showExpressionBlock() {
		expressionBlockView.setVisibility(VISIBLE);
	}

	private void showExpressionBlockIfInvisible() {
		int visibility = expressionBlockView.getVisibility();
		if (visibility == INVISIBLE) {
			expressionBlockView.setVisibility(VISIBLE);
		}
	}

	public boolean canDrop(BlockBean block, float x, float y) {
		if (block == null)
			return false;
		if (block != mGeneralExpressionBlockElementBean.getExpressionBlockBean()) {
			if (expressionBlockView != null) {
				if (expressionBlockView.canDrop(block, x, y))
					return true;
			}
		}
		if (block instanceof ExpressionBlockBean expressionBlock) {
			return expressionBlock.getReturnDatatype()
					.isSuperTypeOrDatatype(mGeneralExpressionBlockElementBean.getAcceptedReturnType());
		}
		return false;
	}

	public void highlight(BlockBean block, float x, float y) {
		if (block != mGeneralExpressionBlockElementBean.getExpressionBlockBean()) {
			if (mGeneralExpressionBlockElementBean.getExpressionBlockBean() != null) {
				if (expressionBlockView.canDrop(block, x, y)) {
					expressionBlockView.highlightNearestTarget(block, x, y);
					return;
				}
			}
		}
		if (block instanceof ExpressionBlockBean expressionBlock) {
			if (expressionBlockView != null) {
				if (expressionBlockView.getVisibility() != GONE) {
					expressionBlockView.setVisibility(INVISIBLE);
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
		if (block != mGeneralExpressionBlockElementBean.getExpressionBlockBean()) {
			if (mGeneralExpressionBlockElementBean.getExpressionBlockBean() != null) {
				if (expressionBlockView.canDrop(block, x, y)) {
					expressionBlockView.drop(block, x, y);
					return;
				}
			}
		}
		if (block instanceof ExpressionBlockBean expressionBlock) {
			setValue(expressionBlock);
		}
	}

	private void handleHighlighterRemoval() {
		if (mGeneralExpressionBlockElementBean.getExpressionBlockBean() != null) {
			showExpressionBlockIfInvisible();
			if (expressionBlockView.getVisibility() == GONE) {
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
