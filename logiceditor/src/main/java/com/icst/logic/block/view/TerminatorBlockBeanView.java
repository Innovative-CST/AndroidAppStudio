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

package com.icst.logic.block.view;

import java.util.ArrayList;

import com.icst.android.appstudio.beans.ActionBlockBean;
import com.icst.android.appstudio.beans.LayerBean;
import com.icst.android.appstudio.beans.TerminatorBlockBean;
import com.icst.logic.builder.LayerViewFactory;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.utils.BlockShapesUtils;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.UnitUtils;
import com.icst.logic.view.ActionBlockLayerView;
import com.icst.logic.view.BlockElementLayerBeanView;
import com.icst.logic.view.LayerBeanView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

public class TerminatorBlockBeanView extends ActionBlockBeanView {
	private Context context;
	private TerminatorBlockBean terminatorBlockBean;
	private ArrayList<LayerBeanView> layers;
	private LinearLayout layersView;

	public TerminatorBlockBeanView(
			Context context,
			TerminatorBlockBean terminatorBlockBean,
			LogicEditorConfiguration logicEditorConfiguration,
			LogicEditorView logicEditor) {
		super(context, logicEditorConfiguration, logicEditor);
		this.context = context;
		this.terminatorBlockBean = terminatorBlockBean;
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
		ArrayList<LayerBean> layers = terminatorBlockBean.getLayers();

		for (int i = 0; i < layers.size(); ++i) {

			LinearLayout.LayoutParams mLayoutParam = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			LayerBeanView layerView = LayerViewFactory.buildBlockLayerView(
					context,
					terminatorBlockBean,
					this,
					layers.get(i),
					getLogicEditor(),
					getLogicEditorConfiguration());
			layerView.setLayerPosition(i);
			layerView.setFirstLayer(i == 0);
			layerView.setLastLayer(i == (layers.size() - 1));
			layerView.setColor(terminatorBlockBean.getColor());

			layersView.addView(layerView.getView());
			layerView.getView().setLayoutParams(mLayoutParam);
			this.layers.add(layerView);
		}

		TerminatorBlockBeanView.LayoutParams lp = new TerminatorBlockBeanView.LayoutParams(
				TerminatorBlockBeanView.LayoutParams.WRAP_CONTENT,
				TerminatorBlockBeanView.LayoutParams.WRAP_CONTENT);
		addView(layersView);
		layersView.setLayoutParams(lp);
	}

	// Method to calculate the maximum width from the list of layers
	private int getMaxLayerWidth() {
		int maxWidth = 0;

		for (LayerBeanView layer : layers) {
			if (layer instanceof ActionBlockLayerView) {
				continue;
			} else if (layer instanceof BlockElementLayerBeanView mBlockElementLayerBeanView) {
				int width = mBlockElementLayerBeanView.getWrapContentDimension()[0];
				maxWidth = Math.max(width, maxWidth);
			}
		}
		maxWidth += getPaddingLeft() + getPaddingRight();

		return maxWidth;
	}

	public void setTerminatorBlockBean(TerminatorBlockBean terminatorBlockBean) {
		this.terminatorBlockBean = terminatorBlockBean;
		layers = new ArrayList<LayerBeanView>();
		removeAllViews();
		init();
	}

	public TerminatorBlockBean getTerminatorBlockBean() {
		return terminatorBlockBean;
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
		int maxLayerWidth = getMaxLayerWidth();

		for (LayerBeanView layer : layers) {
			if (layer instanceof ActionBlockLayerView actionBlockLayerView) {
				actionBlockLayerView.setMaxLayerWidth(maxLayerWidth);
			} else if (layer instanceof BlockElementLayerBeanView mBlockElementLayerBeanView) {
				mBlockElementLayerBeanView.setMaxLayerWidth(maxLayerWidth);
			}
		}

		measureChild(layersView, widthMeasureSpec, heightMeasureSpec);
		totalHeight += layersView.getMeasuredHeight();
		maxWidth = Math.max(maxWidth, layersView.getMeasuredWidth());

		totalHeight += UnitUtils.dpToPx(getContext(), 7) + UnitUtils.dpToPx(getContext(), 5) - 2;
		maxWidth += getPaddingLeft() + getPaddingRight();
		setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec), resolveSize(totalHeight, heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int maxLayerWidth = getMaxLayerWidth();
		BlockShapesUtils.drawActionBlockHeader(canvas, getContext(), 0, 0, maxLayerWidth,
				Color.parseColor(ColorUtils.harmonizeHexColor(getContext(), terminatorBlockBean.getColor())));

		int totalHeight = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			totalHeight += child.getMeasuredHeight();
		}
		totalHeight += UnitUtils.dpToPx(getContext(), 7);
		BlockShapesUtils.drawTerminatorBlockFooter(canvas, getContext(), 0, totalHeight - 1,
				maxLayerWidth + getPaddingLeft() + getPaddingRight(),
				Color.parseColor(ColorUtils.harmonizeHexColor(getContext(), terminatorBlockBean.getColor())));
	}

	@Override
	public ArrayList<LayerBeanView> getLayers() {
		return layers;
	}

	@Override
	public ActionBlockBean getActionBlockBean() {
		return terminatorBlockBean;
	}
}
