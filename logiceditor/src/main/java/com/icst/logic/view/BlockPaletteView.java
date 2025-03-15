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

import com.icst.blockidle.beans.BlockPaletteBean;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.UnitUtils;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BlockPaletteView extends LinearLayout {

	private BlockPaletteBean palette;
	private LinearLayout color;
	private TextView label;
	private boolean isSelected;

	public BlockPaletteView(Context context) {
		super(context);
		isSelected = false;
	}

	public void setPalette(BlockPaletteBean palette) {
		this.palette = palette;
		init();
	}

	private void init() {
		removeAllViews();
		if (palette == null) {
			return;
		}
		setOrientation(HORIZONTAL);

		color = new LinearLayout(getContext());
		LayoutParams colorLp = new LinearLayout.LayoutParams(
				UnitUtils.dpToPx(getContext(), 5), LayoutParams.MATCH_PARENT);
		colorLp.setMarginEnd(UnitUtils.dpToPx(getContext(), 8));
		color.setLayoutParams(colorLp);
		color.setBackgroundColor(Color.parseColor(palette.getColor()));
		addView(color);

		label = new TextView(getContext());
		label.setText(palette.getName());
		addView(label);
		setPadding(
				UnitUtils.dpToPx(getContext(), 2),
				UnitUtils.dpToPx(getContext(), 2),
				UnitUtils.dpToPx(getContext(), 2),
				UnitUtils.dpToPx(getContext(), 2));
		if (isSelected) {
			setBackgroundColor(Color.parseColor(palette.getColor()));
			label.setTextColor(
					ColorUtils.getTextColorForColor(Color.parseColor(palette.getColor())));
		} else {
			setBackground(null);
		}
	}

	public boolean isSelected() {
		return this.isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;

		if (palette == null) {
			return;
		}
		// TODO: Reset label text color to default or colorOnSurface but can't figure out currently,
		// So reinit again...
		init();
	}

	public BlockPaletteBean getPalette() {
		return this.palette;
	}
}
