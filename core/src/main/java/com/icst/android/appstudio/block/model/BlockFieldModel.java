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

package com.icst.android.appstudio.block.model;

import java.io.Serializable;

public class BlockFieldModel extends BlockLayerModel implements Serializable, Cloneable {
	public static final long serialVersionUID = 7L;

	private String value;

	public String getValue() {
		return this.value == null ? "" : this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public BlockFieldModel clone() {
		BlockFieldModel blockFieldModel = new BlockFieldModel();
		blockFieldModel.setValue(getValue() != null ? new String(getValue()) : null);
		return blockFieldModel;
	}
}
