/*
 *  This file is part of Block IDLE.
 *
 *  Block IDLE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Block IDLE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Block IDLE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.logic.view;

import com.icst.blockidle.beans.ActionBlockBean;
import com.icst.blockidle.beans.BlockBean;
import com.icst.blockidle.beans.GeneralExpressionBlockBean;
import com.icst.blockidle.beans.NumericBlockBean;
import com.icst.blockidle.beans.StringBlockBean;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.utils.BlockImageUtils;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.ImageViewUtils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NearestTargetHighlighterView extends LinearLayout {

	private BlockBean block;
	private View header;
	private View footer;
	private ViewGroup.LayoutParams headerLayoutParam;
	private ViewGroup.LayoutParams footerLayoutParam;
	private LogicEditorConfiguration logicEditorConfiguration;
	private LinearLayout layerView;
	private LinearLayout blockDummy;
	private ImageView cursorIcon;

	public NearestTargetHighlighterView(Context context, BlockBean block) {
		super(context);
		this.block = block;
		init();
	}

	private void init() {
		blockDummy = new LinearLayout(getContext());
		blockDummy.setOrientation(VERTICAL);
		addView(blockDummy);
		if (block instanceof ActionBlockBean) {
			addHeader();
			addFooter();
			setBackgroundColor(Color.TRANSPARENT);
		} else if (block instanceof StringBlockBean) {
			setBackgroundColor(Color.BLACK);
		} else if (block instanceof NumericBlockBean) {
			setBackgroundColor(Color.BLACK);
		} else if (block instanceof GeneralExpressionBlockBean) {
			setBackgroundColor(Color.BLACK);
		}
	}

	@Override
	protected void onMeasure(int arg0, int arg1) {
		if (block instanceof StringBlockBean) {
			setMeasuredDimension(0, 0);
		} else if (block instanceof NumericBlockBean) {
			setMeasuredDimension(0, 0);
		} else if (block instanceof GeneralExpressionBlockBean) {
			setMeasuredDimension(0, 0);
		} else {
			super.onMeasure(arg0, arg1);
		}
	}

	private void addFooter() {
		footerLayoutParam = new LayoutParams(160, LayoutParams.WRAP_CONTENT);

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

	private void addHeader() {
		headerLayoutParam = new LayoutParams(160, LayoutParams.WRAP_CONTENT);

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

	public void setBlockBean(BlockBean blockBean) {
		this.block = blockBean;
		removeAllViews();
		init();
	}
}
