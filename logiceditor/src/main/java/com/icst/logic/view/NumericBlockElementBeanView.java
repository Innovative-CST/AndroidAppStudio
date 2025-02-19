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
import com.icst.android.appstudio.beans.ExpressionBlockBean;
import com.icst.android.appstudio.beans.NumericBlockBean;
import com.icst.android.appstudio.beans.NumericBlockElementBean;
import com.icst.android.appstudio.beans.utils.BlockBeanUtils;
import com.icst.logic.block.view.BlockBeanView;
import com.icst.logic.block.view.NumericBlockBeanView;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.sheet.InputFieldBottomSheet;
import com.icst.logic.utils.BlockShapesUtils;
import com.icst.logic.utils.UnitUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NumericBlockElementBeanView extends LinearLayout {

	private NumericBlockBeanView numericBlockBeanView;
	private TextView numericTextView;
	private BlockBeanView blockView;
	private NumericBlockElementBean mNumericBlockElementBean;
	private LogicEditorConfiguration logicEditorConfiguration;
	private LogicEditorView logicEditor;
	private LinearLayout layout;
	private InputFieldBottomSheet currentSheet;
	private boolean enableHighlighterColor;

	public NumericBlockElementBeanView(
			Context context,
			BlockBeanView blockView,
			NumericBlockElementBean mNumericBlockElementBean,
			LogicEditorConfiguration configuration,
			LogicEditorView logicEditor) {
		super(context);
		this.mNumericBlockElementBean = mNumericBlockElementBean;
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
					showNumericInput();
				});
		numericTextView = new TextView(context);
		numericTextView.setTextSize(configuration.getTextSize().getTextSize());
		int padding8dp = UnitUtils.dpToPx(context, 8);
		numericTextView.setPadding(padding8dp, 0, padding8dp, 0);
		addView(layout);
		init();
	}

	private void showNumericInput() {
		if (blockView.isInsideCanva()) {
			currentSheet = new InputFieldBottomSheet(
					getContext(),
					mNumericBlockElementBean,
					new InputFieldBottomSheet.ValueListener() {
						@Override
						public void onChange(String value) {
							setValue(value);
						}
					});
			currentSheet.show();
		}
	}

	private void init() {
		layout.removeAllViews();
		if (mNumericBlockElementBean.getNumericBlock() != null) {
			createNumericBlockView();
			configureNumericBlockView();
			addNumericBlockViewToLayout();
			showNumericBlock();
		} else {
			addNumericTextViewToLayout();
			showNumericTextView();
			refreshNumericText();
		}
		invalidate();
	}

	private void createNumericBlockView() {
		numericBlockBeanView = new NumericBlockBeanView(
				getContext(),
				mNumericBlockElementBean.getNumericBlock(),
				logicEditorConfiguration,
				logicEditor) {
			@Override
			public void setVisibility(int arg0) {
				super.setVisibility(arg0);
				handleNumericBlockVisibilityChange();
			}
		};
	}

	public void handleNumericBlockVisibilityChange() {
		int visibility = numericBlockBeanView.getVisibility();
		if (visibility == GONE) {
			if (numericTextView.getVisibility() == GONE) {
				numericTextView.setVisibility(VISIBLE);
			}
			if (numericTextView.getParent() == null) {
				layout.addView(numericTextView);
			}
			refreshNumericText();
		} else if (visibility == VISIBLE) {
			layout.removeView(numericTextView);
		}
	}

	private void configureNumericBlockView() {
		LayoutParams lp = generateDefaultLayoutParams();
		lp.setMargins(0, 0, 0, 0);
		numericBlockBeanView.setLayoutParams(lp);
		numericBlockBeanView.setInsideCanva(true);
	}

	private void addNumericBlockViewToLayout() {
		if (numericBlockBeanView.getParent() == null) {
			layout.addView(numericBlockBeanView);
		}
	}

	private void showNumericBlock() {
		numericBlockBeanView.setVisibility(VISIBLE);
	}

	public void setValue(String numericalValue) {
		mNumericBlockElementBean.setNumericBlock(null);
		mNumericBlockElementBean.setNumericalValue(numericalValue);
		numericBlockBeanView = null;
		init();
	}

	public void setValue(NumericBlockBean numBlock) {
		if (numBlock == null)
			return;
		mNumericBlockElementBean.setNumericalValue(null);
		if (numericBlockBeanView != null) {
			numericBlockBeanView.setOnTouchListener(null);
		}
		mNumericBlockElementBean.setNumericBlock(numBlock);
		init();
	}

	private void addNumericTextViewToLayout() {
		if (numericTextView.getParent() == null) {
			layout.addView(numericTextView);
		}
		layout.removeView(numericBlockBeanView);
	}

	private void showNumericTextView() {
		numericTextView.setVisibility(VISIBLE);
	}

	private void refreshNumericText() {
		if (mNumericBlockElementBean.getNumericalValue() == null) {
			numericTextView.setText("");
		}
		numericTextView.setText(mNumericBlockElementBean.getNumericalValue());
	}

	private void showNumericBlockIfInvisible() {
		int visibility = numericBlockBeanView.getVisibility();
		if (visibility == INVISIBLE) {
			numericBlockBeanView.setVisibility(VISIBLE);
		}
	}

	private void handleHighlighterRemoval() {
		if (mNumericBlockElementBean.getNumericBlock() != null) {
			showNumericBlockIfInvisible();
			if (numericBlockBeanView.getVisibility() == GONE) {
				showNumericTextView();
				refreshNumericText();
			}
		} else {
			showNumericTextView();
			refreshNumericText();
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

	public boolean canDrop(BlockBean block, float x, float y) {
		if (block == null)
			return false;
		if (block != mNumericBlockElementBean.getNumericBlock()) {
			if (numericBlockBeanView != null) {
				if (numericBlockBeanView.canDrop(block, x, y))
					return true;
			}
		}
		if (block instanceof ExpressionBlockBean expressionBlock) {
			return BlockBeanUtils.arrayContainsDatatypeBeans(
					expressionBlock.getReturnDatatypes(),
					mNumericBlockElementBean.getAcceptedReturnType());
		}
		return false;
	}

	public void highlight(BlockBean block, float x, float y) {
		if (block != mNumericBlockElementBean.getNumericBlock()) {
			if (mNumericBlockElementBean.getNumericBlock() != null) {
				if (numericBlockBeanView.canDrop(block, x, y)) {
					numericBlockBeanView.highlightNearestTarget(block, x, y);
					return;
				}
			}
		}
		if (block instanceof NumericBlockBean numericBlockBean) {
			if (numericBlockBeanView != null) {
				if (numericBlockBeanView.getVisibility() != GONE) {
					numericBlockBeanView.setVisibility(INVISIBLE);
				}
			}

			if (numericTextView != null) {
				numericTextView.setVisibility(INVISIBLE);
			}
			NearestTargetHighlighterView highlighter = new NearestTargetHighlighterView(getContext(), block);
			logicEditor.setDummyHighlighter(highlighter);
			addView(highlighter);
			enableHighlighterColor = true;
			invalidate();
		}
	}

	public void dropBlockIfAllowed(BlockBean block, float x, float y) {
		if (block != mNumericBlockElementBean.getNumericBlock()) {
			if (mNumericBlockElementBean.getNumericBlock() != null) {
				if (numericBlockBeanView.canDrop(block, x, y)) {
					numericBlockBeanView.drop(block, x, y);
					return;
				}
			}
		}
		if (block instanceof NumericBlockBean numericBlockBean) {
			setValue(numericBlockBean);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureChild(layout, widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(
				resolveSize(layout.getMeasuredWidth(), widthMeasureSpec),
				resolveSize(layout.getMeasuredHeight(), heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int left = getPaddingLeft();
		int top = 0;
		int right = left + layout.getMeasuredWidth();
		int bottom = top + layout.getMeasuredHeight();

		layout.layout(left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (enableHighlighterColor) {
			BlockShapesUtils.drawNumericBlockHighlighter(
					canvas, getContext(), getMeasuredWidth(), getMeasuredHeight(), Color.BLACK);
		} else {
			BlockShapesUtils.drawNumericBlockElement(
					canvas, getContext(), getMeasuredWidth(), getMeasuredHeight(), Color.WHITE);
		}
	}

}
