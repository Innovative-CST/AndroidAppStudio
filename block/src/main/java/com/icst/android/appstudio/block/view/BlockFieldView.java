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

import com.icst.android.appstudio.block.model.BlockFieldModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.utils.ColorPalleteUtils;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TextView;

public class BlockFieldView extends TextView {
	private Context context;
	private BlockFieldModel blockFieldModel;
	private BlockModel blockModel;
	private boolean isDarkMode;

	public BlockFieldView(
			Context context, BlockFieldModel blockFieldModel, BlockModel blockModel, boolean isDarkMode) {
		super(context);
		this.context = context;
		this.blockFieldModel = blockFieldModel;
		this.blockModel = blockModel;
		this.isDarkMode = isDarkMode;
		setSingleLine(true);
		setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		setTextColor(
				ColorPalleteUtils.getTextColorForColor(
						ColorPalleteUtils.transformColor(blockModel.getColor(), isDarkMode)));
		setText(blockFieldModel.getValue() != null ? blockFieldModel.getValue() : "");
	}

	public BlockFieldModel getBlockFieldModel() {
		return this.blockFieldModel;
	}

	public void setBlockContentModel(BlockFieldModel blockFieldModel) {
		this.blockFieldModel = blockFieldModel;
		setText(blockFieldModel.getValue());
	}

	public BlockModel getBlockModel() {
		return this.blockModel;
	}

	public void setBlockModel(BlockModel blockModel) {
		this.blockModel = blockModel;
		setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		setTextColor(
				ColorPalleteUtils.getTextColorForColor(
						ColorPalleteUtils.transformColor(blockModel.getColor(), isDarkMode)));
		setText(blockFieldModel.getValue());
	}
}
