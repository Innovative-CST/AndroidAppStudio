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

package com.icst.android.appstudio.block.view;

import com.icst.android.appstudio.block.R;
import com.icst.android.appstudio.block.model.BlockFieldLayerModel;
import com.icst.android.appstudio.block.model.BlockModel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

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
