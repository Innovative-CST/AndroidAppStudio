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

import com.icst.blockidle.beans.BlockBean;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.R;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.UnitUtils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DraggingBlockDummy extends LinearLayout {

	private boolean draggedFromCanva;
	private BlockBean block;
	private LogicEditorConfiguration logicEditorConfiguration;
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
		int tintColor = ColorUtils.getColor(getContext(), com.google.android.material.R.attr.colorOnSurface);
		cursorIcon.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
		setAllowedDropIcon(allowDrop);
		addView(cursorIcon);
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
