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

import java.util.ArrayList;

import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.BlockElementLayerBean;
import com.icst.android.appstudio.beans.ExpressionBlockBean;
import com.icst.android.appstudio.beans.StringBlockBean;
import com.icst.logic.block.view.ExpressionBlockBeanView;
import com.icst.logic.builder.LayerViewFactory;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.utils.CanvaMathUtils;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.UnitUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

public class StringBlockBeanView extends ExpressionBlockBeanView {

	private StringBlockBean stringBlockBean;
	private Context context;
	private ArrayList<BlockElementLayerBeanView> layers;
	private LinearLayout layersView;

	public StringBlockBeanView(
			Context context,
			StringBlockBean stringBlockBean,
			LogicEditorConfiguration logicEditorConfiguration,
			LogicEditorView logicEditor) {
		super(context, logicEditorConfiguration, logicEditor);
		this.stringBlockBean = stringBlockBean;
		this.context = context;
		layersView = new LinearLayout(context);
		layers = new ArrayList<>();
		init();
	}

	private void init() {
		addLayers();
		setBackgroundColor(Color.parseColor(ColorUtils.harmonizeHexColor(getContext(), stringBlockBean.getColor())));
		setOnTouchListener(getLogicEditor().getDraggableTouchListener());
	}

	private void addLayers() {
		ArrayList<BlockElementLayerBean> layers = stringBlockBean.getElementsLayers();

		for (int i = 0; i < layers.size(); ++i) {

			LinearLayout.LayoutParams mLayoutParam = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			BlockElementLayerBeanView layerView = LayerViewFactory.buildBlockElementLayerView(
					context, stringBlockBean, this, layers.get(i), getLogicEditor(), getLogicEditorConfiguration());
			layerView.setLayerPosition(i);
			layerView.setFirstLayer(i == 0);
			layerView.setLastLayer(i == (layers.size() - 1));
			layersView.addView(layerView.getView());
			layerView.getView().setLayoutParams(mLayoutParam);
			this.layers.add(layerView);
		}

		StringBlockBeanView.LayoutParams lp = new StringBlockBeanView.LayoutParams(
				StringBlockBeanView.LayoutParams.WRAP_CONTENT,
				StringBlockBeanView.LayoutParams.WRAP_CONTENT);
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
		maxWidth += getPaddingLeft() + getPaddingRight();
		setMeasuredDimension(
				resolveSize(maxWidth, widthMeasureSpec),
				resolveSize(totalHeight, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int totalWidth = 0;
		int currentTop = UnitUtils.dpToPx(getContext(), 4);

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
	public boolean canDrop(BlockBean block, float x, float y) {
		for (int i = 0; i < layers.size(); ++i) {
			if (CanvaMathUtils.isCoordinatesInsideTargetView(layers.get(i).getView(),
					getLogicEditor().getEditorSectionView(), x, y)) {
				return layers.get(i).canDrop(block, getLogicEditor().getEditorSectionView(), x, y);
			}
		}
		return false;
	}

	@Override
	public void highlightNearestTarget(BlockBean block, float x, float y) {
		for (int i = 0; i < layers.size(); ++i) {
			if (CanvaMathUtils.isCoordinatesInsideTargetView(layers.get(i).getView(),
					getLogicEditor().getEditorSectionView(), x, y)) {
				layers.get(i).highlightNearestTargetIfAllowed(block, getLogicEditor().getEditorSectionView(), x, y);
				return;
			}
		}
	}

	@Override
	public void drop(BlockBean block, float x, float y) {
		for (int i = 0; i < layers.size(); ++i) {
			if (CanvaMathUtils.isCoordinatesInsideTargetView(layers.get(i).getView(),
					getLogicEditor().getEditorSectionView(), x, y)) {
				layers.get(i).dropToNearestTargetIfAllowed(block, getLogicEditor().getEditorSectionView(), x, y);
				return;
			}
		}
	}

	@Override
	public ExpressionBlockBean getExpressionBlockBean() {
		return stringBlockBean;
	}

	public StringBlockBean getStringBlockBean() {
		return stringBlockBean;
	}
}
