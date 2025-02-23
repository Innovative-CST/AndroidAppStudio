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
import com.icst.android.appstudio.block.editor.EventEditor;
import com.icst.android.appstudio.block.model.BlockModel;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class BlockPreview extends LinearLayout {
	private BlockModel block;
	private EventEditor editor;

	public BlockPreview(EventEditor editor) {
		super(editor.getContext());
		this.editor = editor;
	}

	public void setBlock(BlockModel block) {
		this.block = block;
		drawPreview();
	}

	public void drawPreview() {
		String previewColor = editor.isDarkMode() ? "#FFFFFF" : "#000000";
		removeAllViews();
		setOrientation(LinearLayout.VERTICAL);
		if (block.getBlockType() == BlockModel.Type.defaultBlock) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			LinearLayout top = new LinearLayout(getContext());
			BlockView.setDrawable(top, R.drawable.block_default_top, Color.parseColor(previewColor));
			addView(top);
			LinearLayout body = new LinearLayout(getContext());
			BlockView.setDrawable(
					body, R.drawable.block_default_cut_bl_br, Color.parseColor(previewColor));
			addView(body);
			LinearLayout bottom = new LinearLayout(getContext());
			BlockView.setDrawable(
					bottom, R.drawable.block_default_bottom_joint, Color.parseColor(previewColor));
			addView(bottom);
			top.setLayoutParams(layoutParams);
			body.setLayoutParams(layoutParams);
			bottom.setLayoutParams(layoutParams);
		} else if (block.getBlockType() == BlockModel.Type.defaultBoolean) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			LinearLayout body = new LinearLayout(getContext());
			BlockView.setDrawable(
					body, R.drawable.block_boolean_backdrop, Color.parseColor(previewColor));
			addView(body);
			body.setLayoutParams(layoutParams);
		} else if (block.getBlockType() == BlockModel.Type.number) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			LinearLayout body = new LinearLayout(getContext());
			BlockView.setDrawable(body, R.drawable.block_number, Color.parseColor("#000000"));
			addView(body);
			body.setLayoutParams(layoutParams);
		} else if (block.getBlockType() == BlockModel.Type.variable) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			LinearLayout body = new LinearLayout(getContext());
			BlockView.setDrawable(body, R.drawable.block_variable, Color.parseColor("#000000"));
			addView(body);
			body.setLayoutParams(layoutParams);
		}
	}

	public void removePreview() {
		if (getParent() == null)
			return;
		((ViewGroup) getParent()).removeView(this);
	}
}
