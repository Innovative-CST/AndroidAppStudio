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
 * 2. **  Consistent Licensing  **
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
 * Copyright © 2022 Dev Kumar
 */

package com.icst.logic.view;

import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.ExpressionBlockBean;
import com.icst.logic.utils.CanvaMathUtils;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.UnitUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

public class BlockElementLayerBeanView extends LinearLayout
		implements LayerBeanView<BlockElementLayerBeanView> {
	private int layerPosition;
	private boolean isFirstLayer;
	private boolean isLastLayer;
	private String color;
	private BlockBean block;
	private int maxLayerWidth;

	public BlockElementLayerBeanView(Context context) {
		super(context);
		setPadding(UnitUtils.dpToPx(context, 2), 0, UnitUtils.dpToPx(context, 2), 0);
	}

	private int[] measureView(int minWidth) {

		int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		int paddingTop = getPaddingTop();
		int paddingBottom = getPaddingBottom();

		int totalWidth = paddingLeft + paddingRight;
		int maxChildHeight = 0;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		// Calculate available height for children
		int availableHeight = (heightMode == MeasureSpec.UNSPECIFIED)
				? Integer.MAX_VALUE
				: heightSize - paddingTop - paddingBottom;

		// First pass to measure children
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (child.getVisibility() == GONE)
				continue;

			LayoutParams lp = (LayoutParams) child.getLayoutParams();
			int childWidthSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
			int childHeightSpec = MeasureSpec.makeMeasureSpec(availableHeight, MeasureSpec.AT_MOST);

			child.measure(childWidthSpec, childHeightSpec);

			totalWidth += child.getMeasuredWidth();

			totalWidth += UnitUtils.dpToPx(getContext(), 4);
			maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());
		}
		totalWidth = Math.max(getMinimumWidth(), totalWidth);
		totalWidth = Math.max(minWidth, totalWidth);

		maxChildHeight = Math.max(maxChildHeight, getMinimumHeight());

		// Resolve parent dimensions
		int resolvedWidth = resolveSize(totalWidth, widthMeasureSpec);
		int resolvedHeight = resolveSize(maxChildHeight + paddingTop + paddingBottom, heightMeasureSpec);

		// Check if height needs adjustment and remeasure children if necessary
		if (heightMode != MeasureSpec.UNSPECIFIED && resolvedHeight < heightSize) {
			availableHeight = resolvedHeight - paddingTop - paddingBottom;
			maxChildHeight = 0;
			totalWidth = paddingLeft + paddingRight;

			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				if (child.getVisibility() == GONE)
					continue;

				LayoutParams lp = (LayoutParams) child.getLayoutParams();
				int childWidthSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
				int childHeightSpec = MeasureSpec.makeMeasureSpec(availableHeight, MeasureSpec.AT_MOST);

				child.measure(childWidthSpec, childHeightSpec);

				totalWidth += child.getMeasuredWidth();
				totalWidth += UnitUtils.dpToPx(getContext(), 4);
				maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());
			}
			resolvedWidth = resolveSize(totalWidth, widthMeasureSpec);
			resolvedHeight = resolveSize(maxChildHeight + paddingTop + paddingBottom, heightMeasureSpec);
		}

		return new int[] { resolvedWidth, resolvedHeight };
	}

	public int[] getWrapContentDimension() {
		return measureView(0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int[] dimensions = measureView(maxLayerWidth);
		setMeasuredDimension(dimensions[0], dimensions[1]);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		int paddingLeft = getPaddingLeft();
		int currentLeft = paddingLeft + UnitUtils.dpToPx(getContext(), 2);
		int parentHeight = getHeight();

		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (child.getVisibility() == GONE)
				continue;

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			// Center child vertically
			int childTop = (parentHeight - childHeight) / 2;

			child.layout(currentLeft, childTop, currentLeft + childWidth, childTop + childHeight);
			currentLeft += childWidth + UnitUtils.dpToPx(getContext(), 4);
		}
	}

	public void highlightNearestTargetIfAllowed(
			BlockBean mBlock, LinearLayout editorSectionView, float x, float y) {
		if (mBlock instanceof ExpressionBlockBean mExpressionBlockBean) {
			for (int i = 0; i < getChildCount(); ++i) {
				View child = getChildAt(i);
				boolean isInsideChild = CanvaMathUtils.isCoordinatesInsideTargetView(
						child, editorSectionView, x, y);
				if (isInsideChild) {
					if (child instanceof StringBlockElementBeanView mStringBlockElementBeanView) {
						if (mStringBlockElementBeanView.canDrop(mBlock, x, y)) {
							mStringBlockElementBeanView.highlight(mBlock, x, y);
						}
					} else if (child instanceof BooleanBlockElementBeanView mBooleanBlockElementBeanView) {
						if (mBooleanBlockElementBeanView.canDrop(mBlock, x, y)) {
							mBooleanBlockElementBeanView.highlight(mBlock, x, y);
						}
					}
					break;
				}
			}
		}
	}

	public void dropToNearestTargetIfAllowed(
			BlockBean mBlock, LinearLayout editorSectionView, float x, float y) {
		if (mBlock instanceof ExpressionBlockBean mExpressionBlockBean) {
			for (int i = 0; i < getChildCount(); ++i) {
				View child = getChildAt(i);
				boolean isInsideChild = CanvaMathUtils.isCoordinatesInsideTargetView(
						child, editorSectionView, x, y);
				if (isInsideChild) {
					if (child instanceof StringBlockElementBeanView mStringBlockElementBeanView) {
						if (mStringBlockElementBeanView.canDrop(mBlock, x, y)) {
							mStringBlockElementBeanView.dropBlockIfAllowed(mBlock, x, y);
						}
					} else if (child instanceof BooleanBlockElementBeanView mBooleanBlockElementBeanView) {
						if (mBooleanBlockElementBeanView.canDrop(mBlock, x, y)) {
							mBooleanBlockElementBeanView.dropBlockIfAllowed(mBlock, x, y);
						}
					}
					break;
				}
			}
		}
	}

	public boolean canDrop(BlockBean mBlock, LinearLayout editorSectionView, float x, float y) {
		if (mBlock instanceof ExpressionBlockBean mExpressionBlockBean) {
			for (int i = 0; i < getChildCount(); ++i) {
				View child = getChildAt(i);
				boolean isInsideChild = CanvaMathUtils.isCoordinatesInsideTargetView(
						child, editorSectionView, x, y);
				if (isInsideChild) {
					if (child instanceof StringBlockElementBeanView mStringBlockElementBeanView) {
						return mStringBlockElementBeanView.canDrop(mBlock, x, y);
					} else if (child instanceof BooleanBlockElementBeanView mBooleanBlockElementBeanView) {
						return mBooleanBlockElementBeanView.canDrop(mBlock, x, y);
					}
					break;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	@Override
	public void setColor(String color) {
		this.color = color;
		setBackgroundColor(
				Color.parseColor(ColorUtils.harmonizeHexColor(getContext(), getColor())));
	}

	@Override
	public BlockBean getBlock() {
		return this.block;
	}

	@Override
	public String getColor() {
		return this.color;
	}

	@Override
	public int getLayerPosition() {
		return this.layerPosition;
	}

	@Override
	public boolean isFirstLayer() {
		return this.isFirstLayer;
	}

	@Override
	public boolean isLastLayer() {
		return this.isLastLayer;
	}

	@Override
	public void setBlock(BlockBean block) {
		this.block = block;
	}

	@Override
	public void setFirstLayer(boolean isFirstLayer) {
		this.isFirstLayer = isFirstLayer;
	}

	@Override
	public void setLastLayer(boolean isLastLayer) {
		this.isLastLayer = isLastLayer;
	}

	@Override
	public void setLayerPosition(int layerPosition) {
		this.layerPosition = layerPosition;
	}

	@Override
	public BlockElementLayerBeanView getView() {
		return this;
	}

	public int getMaxLayerWidth() {
		return this.maxLayerWidth;
	}

	public void setMaxLayerWidth(int maxLayerWidth) {
		this.maxLayerWidth = maxLayerWidth;
		requestLayout();
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new BlockElementLayerBeanView.LayoutParams(
				BlockElementLayerBeanView.LayoutParams.WRAP_CONTENT,
				BlockElementLayerBeanView.LayoutParams.WRAP_CONTENT);
	}
}
