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
import com.icst.android.appstudio.beans.EventBlockBean;
import com.icst.logic.builder.LayerViewFactory;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.utils.BlockImageUtils;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.ImageViewUtils;
import com.icst.logic.view.LayerBeanView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class EventBlockBeanView extends BlockBeanView {
	private Context context;
	private EventBlockBean eventBlockBean;
	private LogicEditorConfiguration configuration = new LogicEditorConfiguration();
	private ArrayList<LayerBeanView> layers;
	private LinearLayout layersView;

	private View header;
	private View footer;
	private ViewGroup.LayoutParams headerLayoutParam;
	private ViewGroup.LayoutParams footerLayoutParam;

	public EventBlockBeanView(Context context, EventBlockBean eventBlockBean, LogicEditorConfiguration configuration,
			LogicEditorView logicEditor) {
		super(context, configuration, logicEditor);
		this.context = context;
		this.eventBlockBean = eventBlockBean;
		layers = new ArrayList<LayerBeanView>();
		layersView = new LinearLayout(context);
		layersView.setOrientation(LinearLayout.VERTICAL);
		init();
	}

	private void init() {
		addHeader();
		addLayers();
		addFooter();
	}

	private void addHeader() {
		headerLayoutParam = new EventBlockBeanView.LayoutParams(
				EventBlockBeanView.LayoutParams.WRAP_CONTENT,
				EventBlockBeanView.LayoutParams.WRAP_CONTENT);

		header = new LinearLayout(context);
		int res = BlockImageUtils.getImage(BlockImageUtils.Image.EVENT_BLOCK_ROUND_EDGE_TOP);
		Drawable headerDrawable = ImageViewUtils.getImageView(context,
				ColorUtils.harmonizeHexColor(getContext(), eventBlockBean.getColor()), res);
		header.setBackgroundDrawable(headerDrawable);
		addView(header);
		header.setLayoutParams(headerLayoutParam);
	}

	private void addLayers() {
		ArrayList<BlockElementLayerBean> layers = eventBlockBean.getElementsLayers();

		for (int i = 0; i < layers.size(); ++i) {

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			BlockElementLayerBean elementLayer = layers.get(i);
			LayerBeanView layerView = LayerViewFactory.buildBlockLayerView(
					context, eventBlockBean, elementLayer, getLogicEditor(), configuration);
			layerView.setLayerPosition(i);
			layerView.setFirstLayer(i == 0);
			layerView.setLastLayer(i == (layers.size() - 1));
			layerView.setColor(eventBlockBean.getColor());

			layersView.addView(layerView.getView());
			layerView.getView().setLayoutParams(lp);
			this.layers.add(layerView);

			layerView.getView()
					.getViewTreeObserver()
					.addOnGlobalLayoutListener(
							() -> {
								headerLayoutParam = header.getLayoutParams();
								headerLayoutParam.width = getMaxLayerWidth();
								header.setLayoutParams(headerLayoutParam);

								footerLayoutParam = footer.getLayoutParams();
								footerLayoutParam.width = getMaxLayerWidth();
								footer.setLayoutParams(footerLayoutParam);

								updateLayerWidthsToMax();
							});
		}

		EventBlockBeanView.LayoutParams lp = new EventBlockBeanView.LayoutParams(
				EventBlockBeanView.LayoutParams.WRAP_CONTENT,
				EventBlockBeanView.LayoutParams.WRAP_CONTENT);
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
			layer.getView().getWidth();
			maxWidth = Math.max(layer.getView().getWidth(), maxWidth);
		}
		return maxWidth;
	}

	private void addFooter() {

		footerLayoutParam = new RegularBlockBeanView.LayoutParams(
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT,
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT);

		this.footer = new LinearLayout(context);
		int footerBackDropRes = BlockImageUtils.getImage(BlockImageUtils.Image.ACTION_BLOCK_BOTTOM);
		Drawable footerRes = ImageViewUtils.getImageView(context,
				ColorUtils.harmonizeHexColor(getContext(), eventBlockBean.getColor()), footerBackDropRes);
		this.footer.setBackgroundDrawable(footerRes);
		addView(this.footer);
		this.footer.setLayoutParams(footerLayoutParam);

		RegularBlockBeanView.LayoutParams lp = new RegularBlockBeanView.LayoutParams(
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT,
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT);

		View footer = new LinearLayout(context);
		int res = BlockImageUtils.getImage(BlockImageUtils.Image.REGULAR_BLOCK_BOTTOM);
		Drawable footerDrawable = ImageViewUtils.getImageView(context,
				ColorUtils.harmonizeHexColor(getContext(), eventBlockBean.getColor()), res);
		footer.setBackgroundDrawable(footerDrawable);
		addView(footer);
		footer.setLayoutParams(lp);

		footerLayoutParam = new RegularBlockBeanView.LayoutParams(
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT,
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT);
	}

	private void setEventBlockBean(EventBlockBean eventBlockBean) {
		this.eventBlockBean = eventBlockBean;
		layers = new ArrayList<LayerBeanView>();
		removeAllViews();
		init();
	}

	@Override
	public boolean canDrop(BlockBean block, float x, float y) {
		return false;
	}

	@Override
	public void highlightNearestTarget(BlockBean block, float x, float y) {
	}

	@Override
	public void drop(BlockBean block, float x, float y) {
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
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

		setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec), resolveSize(totalHeight, heightMeasureSpec));
	}
}
