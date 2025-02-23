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

public class NumberRangeValidator {

	public static final int INPUT_TYPE_BYTE = 1;
	public static final int INPUT_TYPE_SHORT = 2;
	public static final int INPUT_TYPE_INT = 3;
	public static final int INPUT_TYPE_LONG = 4;

	public static final int INPUT_TYPE_FLOAT = 5;
	public static final int INPUT_TYPE_DOUBLE = 6;

	public static boolean isValidNumber(String value, int type) {
		if (type == INPUT_TYPE_BYTE) {
			return isValidByte(value);
		}
		if (type == INPUT_TYPE_SHORT) {
			return isValidShort(value);
		}
		if (type == INPUT_TYPE_INT) {
			return isValidInt(value);
		}
		if (type == INPUT_TYPE_LONG) {
			return isValidLong(value);
		}
		if (type == INPUT_TYPE_FLOAT) {
			return isValidFloat(value);
		}
		if (type == INPUT_TYPE_DOUBLE) {
			return isValidDouble(value);
		}
		return false;
	}

	public static boolean isValidByte(String value) {
		try {
			byte parsedValue = Byte.parseByte(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isValidShort(String value) {
		try {
			short parsedValue = Short.parseShort(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isValidInt(String value) {
		try {
			int parsedValue = Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isValidLong(String value) {
		try {
			long parsedValue = Long.parseLong(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isValidFloat(String value) {
		try {
			float parsedValue = Float.parseFloat(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isValidDouble(String value) {
		try {
			double parsedValue = Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
