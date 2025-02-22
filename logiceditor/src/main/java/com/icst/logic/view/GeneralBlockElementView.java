/*
 * This file is part of Android AppStudio [https://github.com/Innovative-CST/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.icst.logic.view;

import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.BooleanBlockBean;
import com.icst.android.appstudio.beans.ExpressionBlockBean;
import com.icst.android.appstudio.beans.GeneralExpressionBlockBean;
import com.icst.android.appstudio.beans.GeneralExpressionBlockElementBean;
import com.icst.android.appstudio.beans.NumericBlockBean;
import com.icst.android.appstudio.beans.StringBlockBean;
import com.icst.android.appstudio.beans.utils.BlockBeanUtils;
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
			BlockShapesUtils.drawGeneralBlockElement(
					canvas, getContext(), getMeasuredWidth(), getMeasuredHeight(), Color.BLACK);
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
			return BlockBeanUtils.arrayContainsDatatypeBeans(
					expressionBlock.getReturnDatatypes(),
					mGeneralExpressionBlockElementBean.getAcceptedReturnType());
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
