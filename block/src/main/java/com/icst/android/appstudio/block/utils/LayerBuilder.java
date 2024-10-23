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

package com.icst.android.appstudio.block.utils;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.icst.android.appstudio.block.R;
import com.icst.android.appstudio.block.model.BlockFieldLayerModel;
import com.icst.android.appstudio.block.model.BlockHolderLayer;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.tag.BlockDroppableTag;
import com.icst.android.appstudio.block.view.BlockView;
import java.util.ArrayList;

public class LayerBuilder {

	public static void buildBlockFieldLayer(
			BlockView blockView,
			BlockFieldLayerModel layer,
			BlockModel blockModel,
			LinearLayout layerLayout,
			ArrayList<LinearLayout> droppables,
			int layerPosition,
			boolean darkMode) {
		/*
		 * If the block is for defining event then and number of Layout is 1 then:
		 * Add LinearLayout with 3 corner cut drawable(Corner Cut: RT:BL:BR).
		 */
		if (blockModel.getBlockType() == BlockModel.Type.defaultBlock) {
			if (blockModel.getBlockLayerModel().size() == 1 && blockModel.isFirstBlock()) {
				setDrawable(
						layerLayout,
						R.drawable.block_default_cut_rt_bl_br,
						ColorPalleteUtils.transformColor(blockModel.getColor(), darkMode));
			}
			if (blockModel.getBlockLayerModel().size() == 1 && !blockModel.isFirstBlock()) {
				setDrawable(
						layerLayout,
						R.drawable.block_default_cut_bl_br,
						ColorPalleteUtils.transformColor(blockModel.getColor(), darkMode));
			}
			if (blockModel.getBlockLayerModel().size() > 1
					&& layerPosition != (blockModel.getBlockLayerModel().size() - 1)) {
				setDrawable(
						layerLayout,
						R.drawable.block_no_cut,
						ColorPalleteUtils.transformColor(blockModel.getColor(), darkMode));
			}
		}

		// Load block content layer...
		layerLayout.addView(
				BlockFieldLayerHandler.getBlockFieldLayerView(
						layerLayout.getContext(),
						layer,
						blockView.getEditor(),
						blockModel,
						blockView,
						droppables,
						darkMode));
	}

	public static void buildBlockHolderLayer(
			BlockView blockView,
			BlockHolderLayer layer,
			BlockModel blockModel,
			LinearLayout layerLayout,
			ArrayList<LinearLayout> droppables,
			int layerPosition,
			boolean darkMode) {
		LinearLayout layerLayoutTop = new LinearLayout(layerLayout.getContext());
		LinearLayout.LayoutParams layerLayoutTopParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layerLayoutTop.setLayoutParams(layerLayoutTopParams);
		setDrawable(
				layerLayoutTop,
				R.drawable.block_holder_layer_top,
				ColorPalleteUtils.transformColor(blockModel.getColor(), darkMode));

		layerLayout.addView(layerLayoutTop);

		LinearLayout jointLayout = new LinearLayout(layerLayout.getContext()) {

			@Override
			public void removeView(View view) {
				if (this.indexOfChild(view) == 0) {
					if (super.getChildAt(1) != null) {
						LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);

						super.getChildAt(1).setLayoutParams(blockParams);
					}
				}
				super.removeView(view);
			}

			@Override
			public void addView(View view) {
				super.addView(view);
				if (super.getChildCount() > 1) {
					LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);

					blockParams.setMargins(
							0, UnitUtils.dpToPx(getContext(), BlockMarginConstants.regularBlockMargin), 0, 0);
					view.setLayoutParams(blockParams);
				} else {
					view.setLayoutParams(
							new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.LayoutParams.WRAP_CONTENT));
				}
			}

			@Override
			public void addView(View view, int index) {
				super.addView(view, index);
				if (index != 0) {
					LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);

					blockParams.setMargins(
							0, UnitUtils.dpToPx(getContext(), BlockMarginConstants.regularBlockMargin), 0, 0);
					view.setLayoutParams(blockParams);
				} else {
					view.setLayoutParams(
							new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.LayoutParams.WRAP_CONTENT));

					if (super.getChildCount() > 1) {
						LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);

						blockParams.setMargins(
								0,
								UnitUtils.dpToPx(getContext(), BlockMarginConstants.regularBlockMargin),
								0,
								0);
						super.getChildAt(1).setLayoutParams(blockParams);
					}
				}
			}
		};
		jointLayout.setOrientation(LinearLayout.VERTICAL);

		BlockDroppableTag tag = new BlockDroppableTag();
		tag.setDropProperty(layer);
		tag.setBlockDroppableType(BlockDroppableTag.DEFAULT_BLOCK_DROPPER);

		jointLayout.setTag(tag);
		droppables.add(jointLayout);
		LinearLayout.LayoutParams jointLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		jointLayoutParams.setMargins(
				0,
				UnitUtils.dpToPx(layerLayout.getContext(), BlockMarginConstants.regularBlockMargin),
				0,
				0);
		jointLayout.setLayoutParams(jointLayoutParams);
		setDrawable(
				jointLayout,
				R.drawable.block_holder_joint,
				ColorPalleteUtils.transformColor(blockModel.getColor(), darkMode));

		layerLayout.addView(jointLayout);

		LinearLayout layerLayoutBottom = new LinearLayout(layerLayout.getContext());
		LinearLayout.LayoutParams layerLayoutBottomParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		layerLayoutBottomParams.setMargins(
				0,
				UnitUtils.dpToPx(layerLayout.getContext(), BlockMarginConstants.regularBlockMargin),
				0,
				0);

		layerLayoutBottom.setLayoutParams(layerLayoutBottomParams);
		setDrawable(
				layerLayoutBottom,
				R.drawable.block_holder_layer_bottom,
				ColorPalleteUtils.transformColor(blockModel.getColor(), darkMode));
		layerLayout.addView(layerLayoutBottom);

		/*
		 * Add bottom 2 corner cut if current lyer is last layer of BlockModel
		 */
		if (blockModel.getBlockLayerModel().size() == (layerPosition + 1)) {
			LinearLayout bottom2CornerCut = new LinearLayout(layerLayout.getContext());
			LinearLayout.LayoutParams bottom2CornerCutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			bottom2CornerCut.setLayoutParams(bottom2CornerCutParams);
			setDrawable(
					bottom2CornerCut,
					R.drawable.block_holder_last_layer,
					ColorPalleteUtils.transformColor(blockModel.getColor(), darkMode));
			layerLayout.addView(bottom2CornerCut);
		}

		if (layerPosition == 0) {
			if (!blockModel.isFirstBlock()) {
				LinearLayout firstBlockTop = new LinearLayout(layerLayout.getContext());
				ViewGroup.LayoutParams _lp = new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				firstBlockTop.setLayoutParams(_lp);
				Drawable firstBlockTopDrawable = ContextCompat.getDrawable(layerLayout.getContext(),
						R.drawable.block_default_top);
				firstBlockTopDrawable.setTint(
						ColorPalleteUtils.transformColor(blockModel.getColor(), darkMode));
				firstBlockTopDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
				firstBlockTop.setBackground(firstBlockTopDrawable);
				blockView.addView(firstBlockTop, 0);
			}
		}

		if (layer.getBlocks() == null)
			return;
		for (int blockPosition = 0; blockPosition < layer.getBlocks().size(); ++blockPosition) {
			BlockView block = new BlockView(
					blockView.getEditor(),
					blockView.getContext(),
					layer.getBlocks().get(blockPosition),
					darkMode);

			block.setEnableDragDrop(true);
			block.setEnableEditing(true);
			block.setInsideEditor(true);
			jointLayout.addView(block);
		}
	}

	public static void setDrawable(View view, int res, int color) {
		Drawable drawable = ContextCompat.getDrawable(view.getContext(), res);
		drawable.setTint(color);
		drawable.setTintMode(PorterDuff.Mode.MULTIPLY);
		view.setBackground(drawable);
	}
}
