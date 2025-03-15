/*
 *  This file is part of Block IDLE.
 *
 *  Block IDLE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Block IDLE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Block IDLE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.logic.block.view;

import java.util.ArrayList;

import com.icst.blockidle.beans.BlockBean;
import com.icst.blockidle.beans.BlockElementLayerBean;
import com.icst.blockidle.beans.EventBlockBean;
import com.icst.logic.builder.LayerViewFactory;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.utils.BlockShapesUtils;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.UnitUtils;
import com.icst.logic.view.BlockElementLayerBeanView;
import com.icst.logic.view.LayerBeanView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

public class EventBlockBeanView extends BlockBeanView {
	private Context context;
	private EventBlockBean eventBlockBean;
	private LogicEditorConfiguration configuration = new LogicEditorConfiguration();
	private ArrayList<LayerBeanView> layers;
	private LinearLayout layersView;

	public EventBlockBeanView(
			Context context,
			EventBlockBean eventBlockBean,
			LogicEditorConfiguration configuration,
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
		setWillNotDraw(false);
		addLayers();
	}

	private void addLayers() {
		ArrayList<BlockElementLayerBean> layers = eventBlockBean.getElementsLayers();

		for (int i = 0; i < layers.size(); ++i) {

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			BlockElementLayerBean elementLayer = layers.get(i);
			LayerBeanView layerView = LayerViewFactory.buildBlockLayerView(
					context,
					eventBlockBean,
					this,
					elementLayer,
					getLogicEditor(),
					configuration);
			layerView.setLayerPosition(i);
			layerView.setFirstLayer(i == 0);
			layerView.setLastLayer(i == (layers.size() - 1));
			layerView.setColor(eventBlockBean.getColor());

			layersView.addView(layerView.getView());
			layerView.getView().setLayoutParams(lp);
			this.layers.add(layerView);
		}

		EventBlockBeanView.LayoutParams lp = new EventBlockBeanView.LayoutParams(
				EventBlockBeanView.LayoutParams.WRAP_CONTENT,
				EventBlockBeanView.LayoutParams.WRAP_CONTENT);
		addView(layersView);
		layersView.setLayoutParams(lp);
	}

	// Method to calculate the maximum width from the list of layers
	private int getMaxLayerWidth() {
		int maxWidth = 0;

		for (LayerBeanView layer : layers) {
			if (layer instanceof BlockElementLayerBeanView mBlockElementLayerBeanView) {
				int width = mBlockElementLayerBeanView.getWrapContentDimension()[0];
				maxWidth = Math.max(width, maxWidth);
			}
		}
		maxWidth += getPaddingLeft() + getPaddingRight();

		return maxWidth;
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
		int currentTop = UnitUtils.dpToPx(getContext(), 12) - 1;

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
		int maxLayerWidth = getMaxLayerWidth();

		for (LayerBeanView layer : layers) {
			if (layer instanceof BlockElementLayerBeanView mBlockElementLayerBeanView) {
				mBlockElementLayerBeanView.setMaxLayerWidth(maxLayerWidth);
			}
		}

		measureChild(layersView, widthMeasureSpec, heightMeasureSpec);
		totalHeight += layersView.getMeasuredHeight();
		maxWidth = Math.max(maxWidth, layersView.getMeasuredWidth());

		totalHeight += UnitUtils.dpToPx(getContext(), 12) + UnitUtils.dpToPx(getContext(), 12) - 2;

		setMeasuredDimension(
				resolveSize(maxWidth, widthMeasureSpec),
				resolveSize(totalHeight, heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int maxLayerWidth = getMaxLayerWidth();
		BlockShapesUtils.drawEventBlockHeader(
				canvas,
				getContext(),
				0,
				0,
				maxLayerWidth,
				Color.parseColor(
						ColorUtils.harmonizeHexColor(getContext(), eventBlockBean.getColor())));
		int totalHeight = UnitUtils.dpToPx(getContext(), 12);
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			totalHeight += child.getMeasuredHeight();
		}
		BlockShapesUtils.drawRegularBlockFooter(
				canvas,
				getContext(),
				0,
				totalHeight - 1,
				maxLayerWidth,
				Color.parseColor(
						ColorUtils.harmonizeHexColor(getContext(), eventBlockBean.getColor())));
	}
}
