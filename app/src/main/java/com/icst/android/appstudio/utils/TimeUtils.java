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

package com.icst.android.appstudio.utils;

public class TimeUtils {
	public static String convertTime(long milliseconds) {
		long minutes = milliseconds / (60 * 1000);
		milliseconds %= (60 * 1000);

		long seconds = milliseconds / 1000;
		milliseconds %= 1000;

		StringBuilder result = new StringBuilder();
		if (minutes > 0) {
			result.append(minutes).append(" minute");
			if (minutes > 1)
				result.append("s");
			result.append(" ");
		}
		if (seconds > 0) {
			result.append(seconds).append(" second");
			if (seconds > 1)
				result.append("s");
			result.append(" ");
		}
		if (milliseconds > 0) {
			result.append(milliseconds).append(" millisecond");
			if (milliseconds > 1)
				result.append("s");
		}

		return result.toString();
	}
}
