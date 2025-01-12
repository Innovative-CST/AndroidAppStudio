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

package com.icst.logic.lib.view;

import com.icst.android.appstudio.beans.ActionBlockBean;
import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.RegularBlockBean;
import com.icst.logic.editor.R;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.utils.BlockImageUtils;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.ImageViewUtils;
import com.icst.logic.utils.UnitUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DraggingBlockDummy extends LinearLayout {

	private boolean draggedFromCanva;
	private BlockBean block;
	private View header;
	private View footer;
	private ViewGroup.LayoutParams headerLayoutParam;
	private ViewGroup.LayoutParams footerLayoutParam;
	private LogicEditorConfiguration logicEditorConfiguration;
	private LinearLayout layerView;
	private LinearLayout blockDummy;
	private ImageView cursorIcon;

	public DraggingBlockDummy(Context context, BlockBean block, boolean allowDrop) {
		super(context);
		this.block = block;
		init(allowDrop);
	}

	private void init(boolean allowDrop) {
		cursorIcon = new ImageView(getContext());
		LayoutParams cursorIconLp = new LayoutParams(
				UnitUtils.dpToPx(getContext(), 16), UnitUtils.dpToPx(getContext(), 16));
		cursorIcon.setLayoutParams(cursorIconLp);
		setAllowedDropIcon(allowDrop);
		addView(cursorIcon);
		blockDummy = new LinearLayout(getContext());
		blockDummy.setOrientation(VERTICAL);
		addView(blockDummy);
		if (block instanceof ActionBlockBean) {
			addHeader();
			addDummyLayer();
			addFooter();

			if (block instanceof RegularBlockBean) {
				LinearLayout regularBlockFooter = new LinearLayout(getContext());
				int regularBlockFooterRes = BlockImageUtils.getImage(BlockImageUtils.Image.REGULAR_BLOCK_BOTTOM);
				Drawable regularBlockFooterDrawable = ImageViewUtils.getImageView(
						getContext(),
						ColorUtils.harmonizeHexColor(getContext(), block.getColor()),
						regularBlockFooterRes);
				regularBlockFooter.setBackgroundDrawable(regularBlockFooterDrawable);
				blockDummy.addView(regularBlockFooter);

				LayoutParams footerLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				regularBlockFooter.setLayoutParams(footerLp);
			}
		}
	}

	public void addDummyLayer() {
		layerView = new LinearLayout(getContext());
		int res = BlockImageUtils.getImage(BlockImageUtils.Image.BLOCK_ELEMENT_LAYER_BACKDROP);
		Drawable drawable = ImageViewUtils.getImageView(
				getContext(),
				ColorUtils.harmonizeHexColor(getContext(), block.getColor()),
				res);
		layerView.setBackgroundDrawable(drawable);
		blockDummy.addView(layerView);

		layerView
				.getViewTreeObserver()
				.addOnGlobalLayoutListener(
						() -> {
							headerLayoutParam = header.getLayoutParams();
							headerLayoutParam.width = getMaxLayerWidth();
							header.setLayoutParams(headerLayoutParam);

							footerLayoutParam = footer.getLayoutParams();
							footerLayoutParam.width = getMaxLayerWidth();
							footer.setLayoutParams(footerLayoutParam);
						});
		LayoutParams lp = new LayoutParams(160, 40);
		layerView.setLayoutParams(lp);
	}

	private void addFooter() {
		footerLayoutParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		footer = new LinearLayout(getContext());
		int footerBackDropRes = BlockImageUtils.getImage(BlockImageUtils.Image.ACTION_BLOCK_BOTTOM);
		Drawable footerRes = ImageViewUtils.getImageView(
				getContext(),
				ColorUtils.harmonizeHexColor(getContext(), block.getColor()),
				footerBackDropRes);
		footer.setBackgroundDrawable(footerRes);
		blockDummy.addView(footer);
		footer.setLayoutParams(footerLayoutParam);
	}

	// Method to calculate the maximum width from the list of layers
	private int getMaxLayerWidth() {
		int maxWidth = 0;
		maxWidth = Math.max(layerView.getWidth(), maxWidth);
		return maxWidth;
	}

	public void addHeader() {
		headerLayoutParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		header = new LinearLayout(getContext());
		int res = BlockImageUtils.getImage(BlockImageUtils.Image.ACTION_BLOCK_TOP);
		Drawable headerDrawable = ImageViewUtils.getImageView(
				getContext(),
				ColorUtils.harmonizeHexColor(getContext(), block.getColor()),
				res);
		header.setBackgroundDrawable(headerDrawable);
		blockDummy.addView(header);
		header.setLayoutParams(headerLayoutParam);
	}

	public void setBlockBean(BlockBean blockBean, boolean allowDrop) {
		this.block = blockBean;
		removeAllViews();
		init(allowDrop);
	}

	public boolean isDraggedFromCanva() {
		return this.draggedFromCanva;
	}

	public void setDraggedFromCanva(boolean draggedFromCanva) {
		this.draggedFromCanva = draggedFromCanva;
	}

	public void setAllowedDropIcon(boolean allow) {
		if (cursorIcon != null) {
			cursorIcon.setImageResource(
					allow ? R.drawable.cursor : R.drawable.cursor_drop_not_allowed);
		}
	}
}
