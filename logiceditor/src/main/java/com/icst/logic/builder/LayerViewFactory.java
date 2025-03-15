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

package com.icst.logic.builder;

import com.icst.blockidle.beans.ActionBlockLayerBean;
import com.icst.blockidle.beans.BlockBean;
import com.icst.blockidle.beans.BlockElementLayerBean;
import com.icst.blockidle.beans.BooleanBlockElementBean;
import com.icst.blockidle.beans.ExpressionBlockBean;
import com.icst.blockidle.beans.GeneralExpressionBlockElementBean;
import com.icst.blockidle.beans.LabelBlockElementBean;
import com.icst.blockidle.beans.LayerBean;
import com.icst.blockidle.beans.NumericBlockElementBean;
import com.icst.blockidle.beans.StringBlockElementBean;
import com.icst.logic.block.view.BlockBeanView;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.view.ActionBlockLayerView;
import com.icst.logic.view.BlockElementLayerBeanView;
import com.icst.logic.view.BooleanBlockElementBeanView;
import com.icst.logic.view.GeneralBlockElementView;
import com.icst.logic.view.LayerBeanView;
import com.icst.logic.view.NumericBlockElementBeanView;
import com.icst.logic.view.StringBlockElementBeanView;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class LayerViewFactory {

	public static LayerBeanView buildBlockLayerView(
			Context context,
			BlockBean blockBean,
			BlockBeanView blockView,
			LayerBean layerBean,
			LogicEditorView logicEdtitor,
			LogicEditorConfiguration configuration) {
		if (layerBean instanceof BlockElementLayerBean mBlockElementLayerBean) {
			return buildBlockElementLayerView(
					context, blockBean, blockView, mBlockElementLayerBean, logicEdtitor, configuration);
		} else if (layerBean instanceof ActionBlockLayerBean actionBlockLayerBean) {
			return buildActionBlockLayerView(
					context, blockBean, actionBlockLayerBean, logicEdtitor, configuration);
		} else
			return null;
	}

	private static LayerBeanView buildActionBlockLayerView(
			Context context,
			BlockBean blockBean,
			ActionBlockLayerBean actionBlockLayerBean,
			LogicEditorView logicEdtitor,
			LogicEditorConfiguration configuration) {
		ActionBlockLayerView actionBlockLayerView = new ActionBlockLayerView(
				context, actionBlockLayerBean, configuration, logicEdtitor);
		actionBlockLayerView.setBlock(blockBean);
		return actionBlockLayerView;
	}

	// Build the block element layer
	public static BlockElementLayerBeanView buildBlockElementLayerView(
			Context context,
			BlockBean blockBean,
			BlockBeanView blockView,
			BlockElementLayerBean mBlockElementLayerBean,
			LogicEditorView logicEdtitor,
			LogicEditorConfiguration configuration) {

		BlockElementLayerBeanView view = new BlockElementLayerBeanView(context);
		view.setBlock(blockBean);
		mBlockElementLayerBean
				.getBlockElementBeans()
				.forEach(
						element -> {
							if (element instanceof LabelBlockElementBean labelBean) {
								View mView = buildLabelView(
										labelBean, blockBean, context, configuration);
								view.addView(mView);
							} else if (element instanceof ExpressionBlockBean mExpressionBlockBean) {
								View mView = buildExpressionBlockBeanView(
										mExpressionBlockBean, context, configuration);
								view.addView(mView);
							} else if (element instanceof StringBlockElementBean mStringBlockElement) {
								View mView = buildStringFieldView(
										mStringBlockElement,
										blockBean,
										blockView,
										context,
										logicEdtitor,
										configuration);
								view.addView(mView);
							} else if (element instanceof BooleanBlockElementBean mBooleanBlockElementBean) {
								View mView = buildBooleanFieldView(
										mBooleanBlockElementBean,
										blockBean,
										blockView,
										context,
										logicEdtitor,
										configuration);
								view.addView(mView);
							} else if (element instanceof NumericBlockElementBean mNumericBlockElementBean) {
								View mView = buildNumericFieldView(
										mNumericBlockElementBean,
										blockBean,
										blockView,
										context,
										logicEdtitor,
										configuration);
								view.addView(mView);
							} else if (element instanceof GeneralExpressionBlockElementBean mGeneralExpressionBlockElementBean) {
								View mView = buildGeneralExpressionFieldView(
										mGeneralExpressionBlockElementBean,
										blockBean,
										blockView,
										context,
										logicEdtitor,
										configuration);
								view.addView(mView);
							}
						});

		return view;
	}

	private static View buildNumericFieldView(
			NumericBlockElementBean mNumericBlockElementBean,
			BlockBean blockBean,
			BlockBeanView blockView,
			Context context,
			LogicEditorView logicEdtitor,
			LogicEditorConfiguration configuration) {
		NumericBlockElementBeanView fieldView = new NumericBlockElementBeanView(context, blockView,
				mNumericBlockElementBean,
				configuration,
				logicEdtitor);
		LinearLayout.LayoutParams layerLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		fieldView.setLayoutParams(layerLayoutParams);
		return fieldView;
	}

	private static View buildLabelView(
			LabelBlockElementBean labelBean,
			BlockBean blockBean,
			Context context,
			LogicEditorConfiguration configuration) {

		TextView labelTextView = new TextView(context);
		labelTextView.setText(labelBean.getLabel());
		labelTextView.setTextSize(configuration.getTextSize().getTextSize());
		LinearLayout.LayoutParams layerLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, // Width
				LinearLayout.LayoutParams.WRAP_CONTENT // Height
		);
		labelTextView.setTextColor(
				ColorUtils.getTextColorForColor(
						Color.parseColor(
								ColorUtils.harmonizeHexColor(context, blockBean.getColor()))));
		labelTextView.setLayoutParams(layerLayoutParams);
		return labelTextView;
	}

	private static View buildStringFieldView(
			StringBlockElementBean field,
			BlockBean blockBean,
			BlockBeanView blockView,
			Context context,
			LogicEditorView logicEdtitor,
			LogicEditorConfiguration configuration) {
		StringBlockElementBeanView fieldView = new StringBlockElementBeanView(context, blockView, field, configuration,
				logicEdtitor);
		LinearLayout.LayoutParams layerLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		fieldView.setLayoutParams(layerLayoutParams);
		return fieldView;
	}

	private static View buildBooleanFieldView(
			BooleanBlockElementBean field,
			BlockBean blockBean,
			BlockBeanView blockView,
			Context context,
			LogicEditorView logicEdtitor,
			LogicEditorConfiguration configuration) {
		BooleanBlockElementBeanView fieldView = new BooleanBlockElementBeanView(context, blockView, field,
				configuration,
				logicEdtitor);
		LinearLayout.LayoutParams layerLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		fieldView.setLayoutParams(layerLayoutParams);
		return fieldView;
	}

	private static View buildGeneralExpressionFieldView(
			GeneralExpressionBlockElementBean field,
			BlockBean blockBean,
			BlockBeanView blockView,
			Context context,
			LogicEditorView logicEdtitor,
			LogicEditorConfiguration configuration) {
		GeneralBlockElementView fieldView = new GeneralBlockElementView(context, blockView, field,
				configuration,
				logicEdtitor);
		LinearLayout.LayoutParams layerLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		fieldView.setLayoutParams(layerLayoutParams);
		return fieldView;
	}

	private static View buildExpressionBlockBeanView(
			ExpressionBlockBean mExpressionBlockBean,
			Context context,
			LogicEditorConfiguration configuration) {
		// TODO: Need to write this method....
		return null;
	}
}
