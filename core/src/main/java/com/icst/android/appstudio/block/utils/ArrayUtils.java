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

import com.icst.android.appstudio.block.tag.AdditionalCodeHelperTag;
import com.icst.android.appstudio.block.tag.DependencyTag;
import com.icst.android.appstudio.block.tag.ImportTag;

public final class ArrayUtils {
	public static final String[] clone(String[] stringArr) {

		if (stringArr == null) {
			return null;
		}

		String[] clone = new String[stringArr.length];

		for (int position = 0; position < stringArr.length; ++position) {
			clone[position] = stringArr[position] == null ? null : new String(stringArr[position]);
		}

		return clone;
	}

	public static final AdditionalCodeHelperTag[] clone(
			AdditionalCodeHelperTag[] additionalCodeHelperTagArr) {

		if (additionalCodeHelperTagArr == null) {
			return null;
		}

		AdditionalCodeHelperTag[] clone = new AdditionalCodeHelperTag[additionalCodeHelperTagArr.length];

		for (int position = 0; position < additionalCodeHelperTagArr.length; ++position) {
			if (additionalCodeHelperTagArr[position] instanceof DependencyTag) {
				clone[position] = additionalCodeHelperTagArr[position] == null
						? null
						: additionalCodeHelperTagArr[position].clone();
			} else if (additionalCodeHelperTagArr[position] instanceof ImportTag) {
				clone[position] = additionalCodeHelperTagArr[position] == null
						? null
						: additionalCodeHelperTagArr[position].clone();
			}
		}

		return clone;
	}

	public static boolean ifContains(String[] arg0, String arg1) {
		if (arg0 == null) {
			return false;
		}

		for (int i = 0; i < arg0.length; ++i) {
			if (arg0[i].equals(arg1)) {
				return true;
			}
		}

		return false;
	}

	public static boolean ifContainAnyElement(String[] arg0, String[] arg1) {
		if (arg0 == null || arg1 == null) {
			return false;
		}

		for (int i = 0; i < arg0.length; ++i) {
			for (int j = i; j < arg1.length; ++j) {
				if (arg0[i].equals(arg1[j])) {
					return true;
				}
			}
		}

		return false;
	}
}
