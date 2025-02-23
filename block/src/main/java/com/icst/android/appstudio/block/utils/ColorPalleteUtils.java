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

import android.graphics.Color;

import androidx.core.graphics.ColorUtils;

public final class ColorPalleteUtils {
	public static int getTextColorForColor(int color) {
		double brightness = ColorUtils.calculateLuminance(color);
		return brightness > 0.5 ? Color.BLACK : Color.WHITE;
	}

	private static int darkColor(int color, float factor) {
		int alpha = Color.alpha(color);
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);

		red = (int) Math.floor(red * factor);
		green = (int) Math.floor(green * factor);
		blue = (int) Math.floor(blue * factor);

		return Color.argb(alpha, red, green, blue);
	}

	public static int transformColor(String color, boolean darkMode) {
		if (darkMode) {
			return darkColor(Color.parseColor(color), 0.7F);
		}
		return Color.parseColor(color);
	}
}
