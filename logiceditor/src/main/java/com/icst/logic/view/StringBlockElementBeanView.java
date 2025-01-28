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
import com.icst.android.appstudio.beans.StringBlockBean;
import com.icst.android.appstudio.beans.StringBlockElementBean;
import com.icst.logic.block.view.BlockBeanView;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.sheet.InputFieldBottomSheet;
import com.icst.logic.utils.UnitUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

public class StringBlockElementBeanView extends BlockDropZoneView {
	private TextView label;

	// Block in which this StringBlockElementBeanView exists.
	private BlockBeanView blockView;
	private StringBlockElementBean mStringBlockElementBean;

	public StringBlockElementBeanView(
			Context context,
			BlockBeanView blockView,
			StringBlockElementBean mStringBlockElementBean,
			LogicEditorConfiguration configuration,
			LogicEditorView logicEditor) {
		super(context, configuration, logicEditor);
		this.mStringBlockElementBean = mStringBlockElementBean;
		this.blockView = blockView;
		setMinimumWidth(UnitUtils.dpToPx(getContext(), 20));
		setBackgroundColor(Color.WHITE);
		setGravity(Gravity.CENTER_VERTICAL);
		label = new TextView(context);
		label.setTextSize(configuration.getTextSize().getTextSize());
		setOnClickListener(
				v -> {
					if (blockView.isInsideCanva() && label.getParent() != null) {
						InputFieldBottomSheet sheet = new InputFieldBottomSheet(
								getContext(),
								mStringBlockElementBean,
								new InputFieldBottomSheet.ValueListener() {
									@Override
									public void onChange(String value) {
										setValue(value);
									}
								});
						sheet.show();
					}
				});
		if (mStringBlockElementBean.getStringBlock() != null) {
			StringBlockBeanView strBlockView = new StringBlockBeanView(
					getContext(),
					mStringBlockElementBean.getStringBlock(),
					getConfiguration(),
					getLogicEditor());
			addView(strBlockView);
		} else {
			label.setText(
					mStringBlockElementBean.getString() == null
							? ""
							: mStringBlockElementBean.getString());
			addView(label);
		}
	}

	public void setValue(String str) {
		if (str == null) {
			return;
		}
		if (label.getParent() == null) {
			mStringBlockElementBean.setString(str);
			label.setText(str);
			removeAllViews();
			addView(label);
		} else {
			mStringBlockElementBean.setString(str);
			label.setText(str);
		}
	}

	public void setValue(StringBlockBean strBlock) {
		if (strBlock == null) {
			return;
		}
		removeAllViews();
		StringBlockBeanView blockView = new StringBlockBeanView(
				getContext(), strBlock, getConfiguration(), getLogicEditor());
		addView(blockView);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(UnitUtils.dpToPx(getContext(), 4), 0, UnitUtils.dpToPx(getContext(), 4), 0);
		return lp;
	}

	public StringBlockElementBean getStringBlockElementBean() {
		return this.mStringBlockElementBean;
	}

	@Override
	public boolean canDrop(BlockBean block, float x, float y) {
		return false;
	}

	@Override
	public void highlightNearestTargetIfAllowed(BlockBean block, float x, float y) {
	}

	@Override
	public void dropBlockIfAllowed(BlockBean block, float x, float y) {
	}
}
