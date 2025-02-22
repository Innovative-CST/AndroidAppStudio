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

package com.icst.logic.block.view;

import java.util.ArrayList;

import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.BlockElementLayerBean;
import com.icst.android.appstudio.beans.ExpressionBlockBean;
import com.icst.android.appstudio.beans.GeneralExpressionBlockBean;
import com.icst.logic.builder.LayerViewFactory;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.utils.CanvaMathUtils;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.UnitUtils;
import com.icst.logic.view.BlockElementLayerBeanView;
import com.icst.logic.view.LayerBeanView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.widget.LinearLayout;

public class GeneralExpressionBlockView extends ExpressionBlockBeanView {

	private GeneralExpressionBlockBean generalExpressionBlockBean;
	private Context context;
	private ArrayList<BlockElementLayerBeanView> layers;
	private LinearLayout layersView;
	private Paint mPaint;
	private Path mPath;

	public GeneralExpressionBlockView(
			Context context,
			GeneralExpressionBlockBean generalExpressionBlockBean,
			LogicEditorConfiguration logicEditorConfiguration,
			LogicEditorView logicEditor) {
		super(context, logicEditorConfiguration, logicEditor);
		this.generalExpressionBlockBean = generalExpressionBlockBean;
		this.context = context;
		layersView = new LinearLayout(context);
		layers = new ArrayList<>();
		setWillNotDraw(false);
		init();
	}

	private void init() {
		int color = Color.parseColor(ColorUtils.harmonizeHexColor(getContext(), generalExpressionBlockBean.getColor()));

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(color);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(5));
		mPath = new Path();
		addLayers();
		setOnTouchListener(getLogicEditor().getDraggableTouchListener());
	}

	private void addLayers() {
		ArrayList<BlockElementLayerBean> layers = generalExpressionBlockBean.getElementsLayers();

		for (int i = 0; i < layers.size(); ++i) {

			LinearLayout.LayoutParams mLayoutParam = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			BlockElementLayerBeanView layerView = LayerViewFactory.buildBlockElementLayerView(
					context,
					generalExpressionBlockBean,
					this,
					layers.get(i),
					getLogicEditor(),
					getLogicEditorConfiguration());
			layerView.setLayerPosition(i);
			layerView.setFirstLayer(i == 0);
			layerView.setLastLayer(i == (layers.size() - 1));
			layersView.addView(layerView.getView());
			layerView.getView().setLayoutParams(mLayoutParam);
			this.layers.add(layerView);
		}

		GeneralExpressionBlockView.LayoutParams lp = new GeneralExpressionBlockView.LayoutParams(
				GeneralExpressionBlockView.LayoutParams.WRAP_CONTENT,
				GeneralExpressionBlockView.LayoutParams.WRAP_CONTENT);
		addView(layersView);
		layersView.setLayoutParams(lp);
	}

	// Method to calculate the maximum width from the list of layers
	private int getMaxLayerWidth() {
		int maxWidth = 0;

		for (BlockElementLayerBeanView layer : layers) {
			int width = layer.getWrapContentDimension()[0];
			maxWidth = Math.max(width, maxWidth);
		}
		maxWidth += getPaddingLeft() + getPaddingRight();

		return maxWidth;
	}

	public int getLayersHeight() {
		return layersView.getMeasuredHeight();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int maxLayerWidth = getMaxLayerWidth();

		// Draw a simple rectangle
		mPath = new Path();
		mPath.lineTo(maxLayerWidth, 0);
		mPath.lineTo(maxLayerWidth, getMeasuredHeight());
		mPath.lineTo(0, getMeasuredHeight());
		mPath.close();
		canvas.drawPath(mPath, mPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int totalHeight = 0;
		int maxWidth = 0;
		int maxLayerWidth = getMaxLayerWidth();

		for (LayerBeanView layer : layers) {
			if (layer instanceof BlockElementLayerBeanView mBlockElementLayerBeanView) {
				mBlockElementLayerBeanView.setMaxLayerWidth(maxLayerWidth);
			}
		}

		measureChild(layersView, widthMeasureSpec, heightMeasureSpec);
		totalHeight += layersView.getMeasuredHeight();
		maxWidth = Math.max(maxWidth, layersView.getMeasuredWidth());

		totalHeight += UnitUtils.dpToPx(getContext(), 4) + UnitUtils.dpToPx(getContext(), 4);
		maxWidth += getPaddingLeft() + getPaddingRight() + totalHeight;
		setMeasuredDimension(
				resolveSize(maxWidth, widthMeasureSpec),
				resolveSize(totalHeight, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int totalWidth = 0;
		int currentTop = UnitUtils.dpToPx(getContext(), 4);

		View child = layersView;
		int left = getPaddingLeft();
		int top = currentTop;
		int right = left + child.getMeasuredWidth();
		int bottom = top + child.getMeasuredHeight();

		child.layout(left, top, right, bottom);

		currentTop += child.getMeasuredHeight();
	}

	@Override
	public boolean canDrop(BlockBean block, float x, float y) {
		for (int i = 0; i < layers.size(); ++i) {
			if (CanvaMathUtils.isCoordinatesInsideTargetView(
					layers.get(i).getView(), getLogicEditor().getEditorSectionView(), x, y)) {
				return layers.get(i).canDrop(block, getLogicEditor().getEditorSectionView(), x, y);
			}
		}
		return false;
	}

	@Override
	public void highlightNearestTarget(BlockBean block, float x, float y) {
		for (int i = 0; i < layers.size(); ++i) {
			if (CanvaMathUtils.isCoordinatesInsideTargetView(
					layers.get(i).getView(), getLogicEditor().getEditorSectionView(), x, y)) {
				layers.get(i)
						.highlightNearestTargetIfAllowed(
								block, getLogicEditor().getEditorSectionView(), x, y);
				return;
			}
		}
	}

	@Override
	public void drop(BlockBean block, float x, float y) {
		for (int i = 0; i < layers.size(); ++i) {
			if (CanvaMathUtils.isCoordinatesInsideTargetView(
					layers.get(i).getView(), getLogicEditor().getEditorSectionView(), x, y)) {
				layers.get(i)
						.dropToNearestTargetIfAllowed(
								block, getLogicEditor().getEditorSectionView(), x, y);
				return;
			}
		}
	}

	@Override
	public ExpressionBlockBean getExpressionBlockBean() {
		return generalExpressionBlockBean;
	}

	public GeneralExpressionBlockBean getGeneralExpressionBlockBean() {
		return generalExpressionBlockBean;
	}

}
