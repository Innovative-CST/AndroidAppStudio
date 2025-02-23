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

package com.icst.logic.utils;

import com.icst.logic.editor.R;

public final class BlockImageUtils {

	public static int getImage(Image image) {
		if (Image.EVENT_BLOCK_ROUND_EDGE_TOP.ordinal() == image.ordinal()) {
			return R.drawable.event_blockbean_top;
		} else if (Image.BLOCK_ELEMENT_LAYER_BACKDROP.ordinal() == image.ordinal()) {
			return R.drawable.block_element_layer_backdrop;
		} else if (Image.REGULAR_BLOCK_BOTTOM.ordinal() == image.ordinal()) {
			return R.drawable.regular_block_bottom;
		} else if (Image.ACTION_BLOCK_LAYER_TOP.ordinal() == image.ordinal()) {
			return R.drawable.action_block_layer_top;
		} else if (Image.ACTION_BLOCK_LAYER_BACKDROP.ordinal() == image.ordinal()) {
			return R.drawable.action_block_layer_backdrop;
		} else if (Image.ACTION_BLOCK_LAYER_BOTTOM.ordinal() == image.ordinal()) {
			return R.drawable.action_block_layer_bottom;
		} else if (Image.ACTION_BLOCK_TOP.ordinal() == image.ordinal()) {
			return R.drawable.action_block_top;
		} else if (Image.ACTION_BLOCK_BOTTOM.ordinal() == image.ordinal()) {
			return R.drawable.action_block_bottom_backdrop;
		} else
			return 0;
	}

	public enum Image {
		ACTION_BLOCK_LAYER_TOP, ACTION_BLOCK_LAYER_BACKDROP, ACTION_BLOCK_LAYER_BOTTOM, ACTION_BLOCK_TOP, ACTION_BLOCK_BOTTOM, EVENT_BLOCK_ROUND_EDGE_TOP, BLOCK_ELEMENT_LAYER_BACKDROP, REGULAR_BLOCK_BOTTOM;
	}
}
