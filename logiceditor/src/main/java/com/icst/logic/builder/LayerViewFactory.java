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

package com.icst.logic.builder;

import com.icst.android.appstudio.beans.ActionBlockLayerBean;
import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.BlockElementLayerBean;
import com.icst.android.appstudio.beans.ExpressionBlockBean;
import com.icst.android.appstudio.beans.LabelBlockElementBean;
import com.icst.android.appstudio.beans.LayerBean;
import com.icst.android.appstudio.beans.StringBlockElementBean;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.UnitUtils;
import com.icst.logic.view.ActionBlockLayerView;
import com.icst.logic.view.BlockElementLayerBeanView;
import com.icst.logic.view.LayerBeanView;
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
			LayerBean layerBean,
			LogicEditorView logicEdtitor,
			LogicEditorConfiguration configuration) {
		if (layerBean instanceof BlockElementLayerBean mBlockElementLayerBean) {
			return buildBlockElementLayerView(
					context, blockBean, mBlockElementLayerBean, logicEdtitor, configuration);
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
		actionBlockLayerView.addActionBlocksBeans(actionBlockLayerBean.getActionBlockBean(), 0);
		actionBlockLayerView.setBlock(blockBean);
		return actionBlockLayerView;
	}

	// Build the block element layer
	public static BlockElementLayerBeanView buildBlockElementLayerView(
			Context context,
			BlockBean blockBean,
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
								BlockElementLayerBeanView.LayoutParams lp = new BlockElementLayerBeanView.LayoutParams(
										BlockElementLayerBeanView.LayoutParams.WRAP_CONTENT,
										BlockElementLayerBeanView.LayoutParams.WRAP_CONTENT);
								lp.setMargins(
										UnitUtils.dpToPx(context, 2),
										0,
										UnitUtils.dpToPx(context, 2),
										0);
								mView.setLayoutParams(lp);
							} else if (element instanceof ExpressionBlockBean mExpressionBlockBean) {
								View mView = buildExpressionBlockBeanView(
										mExpressionBlockBean, context, configuration);
								view.addView(mView);
								BlockElementLayerBeanView.LayoutParams lp = new BlockElementLayerBeanView.LayoutParams(
										BlockElementLayerBeanView.LayoutParams.WRAP_CONTENT,
										BlockElementLayerBeanView.LayoutParams.WRAP_CONTENT);
								lp.setMargins(
										UnitUtils.dpToPx(context, 2),
										0,
										UnitUtils.dpToPx(context, 2),
										0);
								mView.setLayoutParams(lp);
							} else if (element instanceof StringBlockElementBean mStringBlockElement) {
								View mView = buildStringFieldView(
										mStringBlockElement,
										blockBean,
										context,
										logicEdtitor,
										configuration);
								view.addView(mView);
								BlockElementLayerBeanView.LayoutParams lp = new BlockElementLayerBeanView.LayoutParams(
										BlockElementLayerBeanView.LayoutParams.WRAP_CONTENT,
										BlockElementLayerBeanView.LayoutParams.WRAP_CONTENT);
								lp.setMargins(
										UnitUtils.dpToPx(context, 2),
										0,
										UnitUtils.dpToPx(context, 2),
										0);
								mView.setLayoutParams(lp);
							}
						});

		return view;
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
			Context context,
			LogicEditorView logicEdtitor,
			LogicEditorConfiguration configuration) {
		StringBlockElementBeanView fieldView = new StringBlockElementBeanView(context, field, configuration,
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
