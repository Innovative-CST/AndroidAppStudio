/*
 *  This file is part of Android Code Editor.
 *
 *  Android Code Editor is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Android Code Editor is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Android Code Editor.  If not, see <https://www.gnu.org/licenses/>.
 */

package android.code.editor.common.utils;

import android.content.Context;
import android.util.TypedValue;
import androidx.annotation.ColorInt;

public class ColorUtils {
	public static String materialIntToHexColor(Context context, int res) {
		return String.format("#%06X", (0xFFFFFF & getColor(context, res)));
	}

	public static @ColorInt int getColor(Context context, int res) {
		TypedValue typedValue = new TypedValue();
		context.getTheme().resolveAttribute(res, typedValue, true);
		return typedValue.data;
	}
}
