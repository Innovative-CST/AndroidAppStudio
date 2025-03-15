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

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

public final class ImageViewUtils {

	public static Drawable getImageView(Context context, int res) {
		return ContextCompat.getDrawable(context, res);
	}

	public static Drawable getImageView(Context context, String color, int res) {
		Drawable mDrawable = getImageView(context, res);
		mDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
		mDrawable.setTint(Color.parseColor(color));
		return mDrawable;
	}
}
