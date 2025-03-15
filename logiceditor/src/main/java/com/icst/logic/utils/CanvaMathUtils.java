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

import android.view.View;

public class CanvaMathUtils {
	public static int[] getRelativeCoordinates(View view, View relativeOf) {
		int[] location1 = new int[2];
		int[] location2 = new int[2];

		relativeOf.getLocationOnScreen(location1);
		view.getLocationOnScreen(location2);

		int relativeX = location2[0] - location1[0];
		int relativeY = location2[1] - location1[1];

		return new int[] { relativeX, relativeY };
	}

	public static boolean isCoordinatesInsideTargetView(
			View target, View relativeTo, float x, float y) {
		int[] relativePosition = getRelativeCoordinates(target, relativeTo);

		return isCoordinatesInsideRectangle(
				x,
				y,
				relativePosition[0],
				relativePosition[1],
				relativePosition[0] + target.getWidth(),
				relativePosition[1] + target.getHeight());
	}

	public static boolean isCoordinatesInsideRectangle(
			float x, float y, float a, float b, float c, float d) {
		float left = Math.min(a, c);
		float right = Math.max(a, c);
		float top = Math.min(b, d);
		float bottom = Math.max(b, d);

		return x >= left && x <= right && y >= top && y <= bottom;
	}
}
