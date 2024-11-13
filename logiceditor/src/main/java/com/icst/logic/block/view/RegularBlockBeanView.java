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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.icst.android.appstudio.beans.LayerBean;
import com.icst.android.appstudio.beans.RegularBlockBean;
import com.icst.logic.lib.builder.LayerBuilder;
import com.icst.logic.lib.config.LogicEditorConfiguration;
import com.icst.logic.lib.view.LayerBeanView;
import com.icst.logic.utils.BlockImageUtils;
import com.icst.logic.utils.ImageViewUtils;
import java.util.ArrayList;

public class RegularBlockBeanView extends ActionBlockBeanView {
	private Context context;
	private RegularBlockBean regularBlockBean;
	private LogicEditorConfiguration configuration = new LogicEditorConfiguration();
	private ArrayList<LayerBeanView> layers;
	private LinearLayout layersView;
	private View header;
	private ViewGroup.LayoutParams headerLayoutParam;

	public RegularBlockBeanView(Context context, RegularBlockBean regularBlockBean) {
		super(context);
		this.context = context;
		this.regularBlockBean = regularBlockBean;
		layers = new ArrayList<LayerBeanView>();
		layersView = new LinearLayout(context);
		setOrientation(VERTICAL);
		init();
	}

	private void init() {
		addHeader();
		addLayers();
		addFooter();
	}

	private void addHeader() {
		headerLayoutParam = new RegularBlockBeanView.LayoutParams(
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT,
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT);

		header = new LinearLayout(context);
		int res = BlockImageUtils.getImage(BlockImageUtils.Image.ACTION_BLOCK_TOP);
		Drawable headerDrawable = ImageViewUtils.getImageView(context, regularBlockBean.getColor(), res);
		header.setBackgroundDrawable(headerDrawable);
		addView(header);
		header.setLayoutParams(headerLayoutParam);
	}

	private void addLayers() {
		ArrayList<LayerBean> layers = regularBlockBean.getLayers();

		for (int i = 0; i < layers.size(); ++i) {

			LinearLayout.LayoutParams mLayoutParam = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			LayerBeanView layerView = LayerBuilder.buildBlockLayerView(context, layers.get(i), configuration);
			layerView.setLayerPosition(i);
			layerView.setFirstLayer(i == 0);
			layerView.setLastLayer(i == (layers.size() - 1));
			layerView.setColor(regularBlockBean.getColor());

			layersView.addView(layerView);
			layerView.setLayoutParams(mLayoutParam);
			this.layers.add(layerView);

			if (i == 0 && header != null) {
				layerView
						.getViewTreeObserver()
						.addOnGlobalLayoutListener(
								() -> {
									headerLayoutParam = header.getLayoutParams();
									headerLayoutParam.width = layerView.getWidth();
									header.setLayoutParams(headerLayoutParam);
								});
			}
		}

		RegularBlockBeanView.LayoutParams lp = new RegularBlockBeanView.LayoutParams(
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT,
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT);
		addView(layersView);
		layersView.setLayoutParams(lp);
	}

	private void addFooter() {

		RegularBlockBeanView.LayoutParams lp = new RegularBlockBeanView.LayoutParams(
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT,
				RegularBlockBeanView.LayoutParams.WRAP_CONTENT);

		View footer = new LinearLayout(context);
		int res = BlockImageUtils.getImage(BlockImageUtils.Image.BLOCK_BOTTOM);
		Drawable footerDrawable = ImageViewUtils.getImageView(context, regularBlockBean.getColor(), res);
		footer.setBackgroundDrawable(footerDrawable);
		addView(footer);
		footer.setLayoutParams(lp);
	}

	private void setEventBlockBean(RegularBlockBean regularBlockBean) {
		this.regularBlockBean = regularBlockBean;
		layers = new ArrayList<LayerBeanView>();
		removeAllViews();
		init();
	}
}
