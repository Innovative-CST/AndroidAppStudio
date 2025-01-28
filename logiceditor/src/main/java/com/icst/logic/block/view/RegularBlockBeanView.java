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

import com.icst.android.appstudio.beans.ActionBlockBean;
import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.LayerBean;
import com.icst.android.appstudio.beans.RegularBlockBean;
import com.icst.logic.builder.LayerViewFactory;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.utils.BlockShapesUtils;
import com.icst.logic.utils.CanvaMathUtils;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.UnitUtils;
import com.icst.logic.view.ActionBlockLayerView;
import com.icst.logic.view.LayerBeanView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class RegularBlockBeanView extends ActionBlockBeanView {
	private Context context;
	private RegularBlockBean regularBlockBean;
	private ArrayList<LayerBeanView> layers;
	private LinearLayout layersView;
	private ViewGroup.LayoutParams headerLayoutParam;
	private ViewGroup.LayoutParams footerLayoutParam;

	public RegularBlockBeanView(Context context, RegularBlockBean regularBlockBean,
			LogicEditorConfiguration logicEditorConfiguration, LogicEditorView logicEditor) {
		super(context, logicEditorConfiguration, logicEditor);
		this.context = context;
		this.regularBlockBean = regularBlockBean;
		layers = new ArrayList<LayerBeanView>();
		layersView = new LinearLayout(context);
		layersView.setOrientation(LinearLayout.VERTICAL);
		init();
	}

	private void init() {
		setWillNotDraw(false);
		addLayers();
		setOnTouchListener(getLogicEditor().getDraggableTouchListener());
	}

	private void addLayers() {
		ArrayList<LayerBean> layers = regularBlockBean.getLayers();

		for (int i = 0; i < layers.size(); ++i) {

			LinearLayout.LayoutParams mLayoutParam = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			LayerBeanView layerView = LayerViewFactory.buildBlockLayerView(
					context, regularBlockBean, this, layers.get(i), getLogicEditor(), getLogicEditorConfiguration());
			layerView.setLayerPosition(i);
			layerView.setFirstLayer(i == 0);
			layerView.setLastLayer(i == (layers.size() - 1));
			layerView.setColor(regularBlockBean.getColor());

			layersView.addView(layerView.getView());
			layerView.getView().setLayoutParams(mLayoutParam);
			this.layers.add(layerView);

			layerView.getView()
					.getViewTreeObserver()
					.addOnGlobalLayoutListener(
							() -> {
								updateLayerWidthsToMax();
							});
		}

		RegularBlockBeanView.LayoutParams lp = new RegularBlockBeanView.LayoutParams(
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT,
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT);
		addView(layersView);
		layersView.setLayoutParams(lp);
	}

	private void updateLayerWidthsToMax() {
		int maxWidth = getMaxLayerWidth();

		for (LayerBeanView layer : layers) {
			layer.getView().setMinimumWidth(maxWidth);
		}
	}

	// Method to calculate the maximum width from the list of layers
	private int getMaxLayerWidth() {
		int maxWidth = 0;
		for (LayerBeanView layer : layers) {
			if (layer instanceof ActionBlockLayerView) {
				layer.getView().setMinimumWidth(100);
				continue;
			}
			layer.getView().setMinimumWidth(100);
			maxWidth = Math.max(layer.getView().getMeasuredWidth(), maxWidth);
		}
		return maxWidth;
	}

	public void setRegularBlockBean(RegularBlockBean regularBlockBean) {
		this.regularBlockBean = regularBlockBean;
		layers = new ArrayList<LayerBeanView>();
		removeAllViews();
		init();
	}

	public RegularBlockBean getRegularBlockBean() {
		return regularBlockBean;
	}

	@Override
	public boolean canDrop(BlockBean block, float x, float y) {
		if (block instanceof ActionBlockBean mActionBlockBean) {
			ArrayList<ActionBlockBean> blocks = new ArrayList<>();
			blocks.add(mActionBlockBean);
			return canDrop(blocks, x, y);
		}
		return false;
	}

	@Override
	public boolean canDrop(ArrayList<ActionBlockBean> blocks, float x, float y) {
		for (int i = 0; i < layers.size(); ++i) {
			if (!CanvaMathUtils.isCoordinatesInsideTargetView(layers.get(i).getView(),
					getLogicEditor().getEditorSectionView(), x, y)) {
				continue;
			}
			if (layers.get(i) instanceof ActionBlockLayerView layerView) {
				return layerView.canDrop(blocks, x, y);
			}
		}
		return false;
	}

	@Override
	public void highlightNearestTarget(BlockBean block, float x, float y) {
		if (block instanceof ActionBlockBean mActionBlockBean) {
			ArrayList<ActionBlockBean> blocks = new ArrayList<>();
			blocks.add(mActionBlockBean);
			highlightNearestTarget(blocks, x, y);
		}
	}

	@Override
	public void highlightNearestTarget(ArrayList<ActionBlockBean> blocks, float x, float y) {
		for (int i = 0; i < layers.size(); ++i) {
			if (!CanvaMathUtils.isCoordinatesInsideTargetView(layers.get(i).getView(),
					getLogicEditor().getEditorSectionView(), x, y)) {
				continue;
			}
			if (layers.get(i) instanceof ActionBlockLayerView layerView) {
				layerView.highlightNearestTargetIfAllowed(blocks, x, y);
			}
		}
	}

	@Override
	public void drop(BlockBean block, float x, float y) {
		if (block instanceof ActionBlockBean mActionBlockBean) {
			ArrayList<ActionBlockBean> blocks = new ArrayList<>();
			blocks.add(mActionBlockBean);
			drop(blocks, x, y);
		}
	}

	@Override
	public void drop(ArrayList<ActionBlockBean> blocks, float x, float y) {
		for (int i = 0; i < layers.size(); ++i) {
			if (!CanvaMathUtils.isCoordinatesInsideTargetView(layers.get(i).getView(),
					getLogicEditor().getEditorSectionView(), x, y)) {
				continue;
			}
			if (layers.get(i) instanceof ActionBlockLayerView layerView) {
				layerView.dropBlockIfAllowed(blocks, x, y);
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int currentTop = UnitUtils.dpToPx(getContext(), 7) - 1;

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

		totalHeight += UnitUtils.dpToPx(getContext(), 7) + UnitUtils.dpToPx(getContext(), 12) - 2;

		setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec), resolveSize(totalHeight, heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		BlockShapesUtils.drawActionBlockHeader(canvas, getContext(), 0, 0, getMaxLayerWidth(),
				Color.parseColor(ColorUtils.harmonizeHexColor(getContext(), regularBlockBean.getColor())));

		int totalHeight = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			totalHeight += child.getMeasuredHeight();
		}
		totalHeight += UnitUtils.dpToPx(getContext(), 7);
		BlockShapesUtils.drawRegularBlockFooter(canvas, getContext(), 0, totalHeight - 1, getMaxLayerWidth(),
				Color.parseColor(ColorUtils.harmonizeHexColor(getContext(), regularBlockBean.getColor())));
	}
}
