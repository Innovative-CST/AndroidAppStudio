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

package com.icst.logic.utils;

import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.MaterialColors;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;

public class ColorUtils {
	public static String harmonizeHexColor(Context context, String hexColor) {
		int colorInt = Color.parseColor(hexColor);
		int harmonizedColorInt = MaterialColors.harmonizeWithPrimary(context, colorInt);
		return String.format("#%06X", (0xFFFFFF & harmonizedColorInt));
	}

	public static int getTextColorForColor(int color) {
		double brightness = androidx.core.graphics.ColorUtils.calculateLuminance(color);
		return brightness > 0.5 ? Color.BLACK : Color.WHITE;
	}

	public static int getColor(Context context, int res) {
		int color;

		if (DynamicColors.isDynamicColorAvailable()) {
			Resources.Theme theme = context.getTheme();
			TypedArray typedArray = theme.obtainStyledAttributes(new int[] { res });
			color = typedArray.getColor(0, 0);
			typedArray.recycle();
			if (color != 0) {
				return color;
			}
		}
		return MaterialColors.getColor(context, res, 0);
	}
}
