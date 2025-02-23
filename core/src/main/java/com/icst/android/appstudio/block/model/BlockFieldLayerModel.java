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
import java.util.ArrayList;

public class BlockFieldLayerModel extends BlockLayerModel implements Serializable, Cloneable {
	public static final long serialVersionUID = 5L;

	private ArrayList<BlockFieldModel> blockFields;

	public ArrayList<BlockFieldModel> getBlockFields() {
		return this.blockFields;
	}

	public void setBlockFields(ArrayList<BlockFieldModel> blockFields) {
		this.blockFields = blockFields;
	}

	@Override
	public BlockFieldLayerModel clone() {
		BlockFieldLayerModel blockFieldLayerModel = new BlockFieldLayerModel();

		if (getBlockFields() != null) {
			ArrayList<BlockFieldModel> clonedBlockFieldModel = new ArrayList<BlockFieldModel>();
			for (int pos = 0; pos < getBlockFields().size(); ++pos) {
				if (getBlockFields().get(pos) instanceof BlockValueFieldModel) {
					clonedBlockFieldModel.add(((BlockValueFieldModel) getBlockFields().get(pos)).clone());
				} else {
					clonedBlockFieldModel.add(getBlockFields().get(pos).clone());
				}
			}
			blockFieldLayerModel.setBlockFields(clonedBlockFieldModel);
		} else {
			blockFieldLayerModel.setBlockFields(null);
		}
		return blockFieldLayerModel;
	}
}
