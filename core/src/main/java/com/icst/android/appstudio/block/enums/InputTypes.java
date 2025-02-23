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

package com.icst.android.appstudio.block.enums;

public enum InputTypes {
	UNKNOWN(0), INPUT_TYPE_BYTE(1), INPUT_TYPE_SHORT(2), INPUT_TYPE_INT(3), INPUT_TYPE_LONG(4), INPUT_TYPE_FLOAT(
			5), INPUT_TYPE_DOUBLE(6), INPUT_TYPE_STRING(7), INPUT_TYPE_COLOR(8);

	private final int inputType;

	InputTypes(int inputType) {
		this.inputType = inputType;
	}

	public int getInputType() {
		return inputType;
	}

	public static InputTypes fromInt(int inputType) {
		for (InputTypes type : InputTypes.values()) {
			if (type.getInputType() == inputType) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
