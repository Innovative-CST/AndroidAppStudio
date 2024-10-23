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

package com.icst.android.appstudio.block.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.icst.android.appstudio.block.R;
import com.icst.android.appstudio.block.model.BlockFieldLayerModel;
import com.icst.android.appstudio.block.model.BlockModel;

public class BlockDragView extends LinearLayout {
	private Context context;
	private LinearLayout blockPreview;
	public ImageView notAllowed;

	public BlockDragView(Context context, BlockModel block) {
		super(context);
		this.context = context;
		setBlock(block);
	}

	public void setAllowed(boolean isAllowed) {
		if (notAllowed == null) {
			notAllowed = new ImageView(context);
			notAllowed.setImageResource(R.drawable.ic_not_allowed);
			addView(notAllowed, 0);
		}
		notAllowed.setVisibility(isAllowed ? View.INVISIBLE : View.VISIBLE);
	}

	public void setBlock(BlockModel block) {
		if (blockPreview == null) {
			blockPreview = new LinearLayout(context);
			addView(blockPreview);
		} else {
			blockPreview.removeAllViews();
		}
		blockPreview.setOrientation(LinearLayout.VERTICAL);

		if (block == null)
			return;

		if (block.getBlockType() == BlockModel.Type.defaultBlock) {

			/*
			 * Add top of blocks as it it define event block.
			 */
			if (block.isFirstBlock()) {
				LinearLayout firstBlockTop = new LinearLayout(getContext());
				ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				firstBlockTop.setLayoutParams(layoutParams);
				Drawable firstBlockTopDrawable = ContextCompat.getDrawable(getContext(), R.drawable.block_first_top);
				firstBlockTopDrawable.setTint(Color.parseColor(block.getColor()));
				firstBlockTopDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
				firstBlockTop.setBackground(firstBlockTopDrawable);
				blockPreview.addView(firstBlockTop);
			} else {
				LinearLayout firstBlockTop = new LinearLayout(getContext());
				ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				firstBlockTop.setLayoutParams(layoutParams);
				Drawable firstBlockTopDrawable = ContextCompat.getDrawable(getContext(), R.drawable.block_default_top);
				firstBlockTopDrawable.setTint(Color.parseColor(block.getColor()));
				firstBlockTopDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
				firstBlockTop.setBackground(firstBlockTopDrawable);
				blockPreview.addView(firstBlockTop);
			}

			/*
			 * Add all layers to BlockView.
			 */
			for (int layerCount = 0; layerCount < block.getBlockLayerModel().size(); ++layerCount) {
				/*
				 * Check if current(LOOP) layer is BlockContentLayerModel
				 */
				boolean hasNextLayer = layerCount != (block.getBlockLayerModel().size() - 1);
				boolean hasPreviousLayer = layerCount == 0;

				if (block.getBlockLayerModel().get(layerCount) instanceof BlockFieldLayerModel) {
					if (layerCount == 0) {
						if (hasNextLayer) {
							LinearLayout layerLayout = new LinearLayout(getContext());
							ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
									ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
							layerLayout.setLayoutParams(layoutParams);

							BlockView.setDrawable(
									layerLayout, R.drawable.block_no_cut, Color.parseColor(block.getColor()));
							blockPreview.addView(layerLayout);
						} else {
							LinearLayout layerLayout = new LinearLayout(getContext());
							ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
									ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
							layerLayout.setLayoutParams(layoutParams);

							BlockView.setDrawable(
									layerLayout,
									R.drawable.block_default_cut_rt_bl_br,
									Color.parseColor(block.getColor()));
							blockPreview.addView(layerLayout);
						}
					} else {
						if (hasNextLayer) {
							LinearLayout layerLayout = new LinearLayout(getContext());
							ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
									ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
							layerLayout.setLayoutParams(layoutParams);

							BlockView.setDrawable(
									layerLayout, R.drawable.block_no_cut, Color.parseColor(block.getColor()));
							blockPreview.addView(layerLayout);
						} else {
							LinearLayout layerLayout = new LinearLayout(getContext());
							ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
									ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
							layerLayout.setLayoutParams(layoutParams);

							BlockView.setDrawable(
									layerLayout,
									R.drawable.block_default_cut_bl_br,
									Color.parseColor(block.getColor()));
							blockPreview.addView(layerLayout);
						}
					}
				} else {

					LinearLayout layerLayoutTop = new LinearLayout(getContext());
					ViewGroup.LayoutParams layoutParamsTop = new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					layerLayoutTop.setLayoutParams(layoutParamsTop);

					BlockView.setDrawable(
							layerLayoutTop,
							R.drawable.block_holder_layer_top,
							Color.parseColor(block.getColor()));
					blockPreview.addView(layerLayoutTop);

					LinearLayout layerLayoutJoint = new LinearLayout(getContext());
					ViewGroup.LayoutParams layoutParamsJoint = new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					layerLayoutJoint.setLayoutParams(layoutParamsJoint);

					BlockView.setDrawable(
							layerLayoutJoint, R.drawable.block_holder_joint, Color.parseColor(block.getColor()));
					blockPreview.addView(layerLayoutJoint);

					LinearLayout layerLayoutBottom = new LinearLayout(getContext());
					ViewGroup.LayoutParams layoutParamsBottom = new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					layerLayoutBottom.setLayoutParams(layoutParamsBottom);

					BlockView.setDrawable(
							layerLayoutBottom,
							R.drawable.block_holder_layer_bottom,
							Color.parseColor(block.getColor()));
					blockPreview.addView(layerLayoutBottom);

					if (!hasNextLayer) {
						LinearLayout blockBottomLayer = new LinearLayout(getContext());
						ViewGroup.LayoutParams layoutParamsBottomLayer = new ViewGroup.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						blockBottomLayer.setLayoutParams(layoutParamsBottomLayer);

						BlockView.setDrawable(
								blockBottomLayer,
								R.drawable.block_holder_last_layer,
								Color.parseColor(block.getColor()));
						blockPreview.addView(blockBottomLayer);
					}
				}
			}

			if (!block.isLastBlock()) {
				LinearLayout blockBottomJoint = new LinearLayout(getContext());
				ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				blockBottomJoint.setLayoutParams(layoutParams);
				Drawable blockBottomJointDrawable = ContextCompat.getDrawable(getContext(),
						R.drawable.block_default_bottom_joint);
				blockBottomJointDrawable.setTint(Color.parseColor(block.getColor()));
				blockBottomJointDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
				blockBottomJoint.setBackground(blockBottomJointDrawable);
				blockPreview.addView(blockBottomJoint);
			}
		} else if (block.getBlockType() == BlockModel.Type.defaultBoolean) {
			LinearLayout booleanLayout = new LinearLayout(getContext());
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			booleanLayout.setLayoutParams(layoutParams);
			Drawable booleanDrawable = ContextCompat.getDrawable(getContext(), R.drawable.block_boolean_backdrop);
			booleanDrawable.setTint(Color.parseColor(block.getColor()));
			booleanDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
			booleanLayout.setBackground(booleanDrawable);
			blockPreview.addView(booleanLayout);
		} else if (block.getBlockType() == BlockModel.Type.number) {
			LinearLayout numberLayout = new LinearLayout(getContext());
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			numberLayout.setLayoutParams(layoutParams);
			Drawable numberDrawable = ContextCompat.getDrawable(getContext(), R.drawable.block_number);
			numberDrawable.setTint(Color.parseColor(block.getColor()));
			numberDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
			numberLayout.setBackground(numberDrawable);
			blockPreview.addView(numberLayout);
		} else if (block.getBlockType() == BlockModel.Type.variable) {
			LinearLayout variableLayout = new LinearLayout(getContext());
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			variableLayout.setLayoutParams(layoutParams);
			Drawable variableDrawable = ContextCompat.getDrawable(getContext(), R.drawable.block_variable);
			variableDrawable.setTint(Color.parseColor(block.getColor()));
			variableDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
			variableLayout.setBackground(variableDrawable);
			blockPreview.addView(variableLayout);
		}
	}
}
