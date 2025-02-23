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

package com.icst.android.appstudio.block.utils;

import java.util.ArrayList;

import com.icst.android.appstudio.block.R;
import com.icst.android.appstudio.block.model.BlockFieldLayerModel;
import com.icst.android.appstudio.block.model.BlockHolderLayer;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.tag.BlockDroppableTag;
import com.icst.android.appstudio.block.view.BlockView;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

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
