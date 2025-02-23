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

package com.icst.android.appstudio.block.tag;

public class BlockDroppableTag {
	public static final int DEFAULT_BLOCK_DROPPER = 0;
	public static final int BLOCK_BOOLEAN_DROPPER = 1;
	public static final int BLOCK_NUMBER_DROPPER = 2;
	public static final int BLOCK_VARIABLE_DROPPER = 3;
	private int blockDroppableType;
	private Object dropProperty;

	public int getBlockDroppableType() {
		return this.blockDroppableType;
	}

	public void setBlockDroppableType(int blockDroppableType) {
		this.blockDroppableType = blockDroppableType;
	}

	public <T> T getDropProperty(Class<T> dropPropertyClass) {
		return dropPropertyClass.cast(dropProperty);
	}

	public void setDropProperty(Object dropProperty) {
		this.dropProperty = dropProperty;
	}
}
