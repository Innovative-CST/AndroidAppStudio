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
import java.util.HashMap;

public class BlockHolderLayer extends BlockLayerModel implements Cloneable, Serializable {
	public static final long serialVersionUID = 6L;

	private ArrayList<BlockModel> blocks;
	private String replacer;

	public ArrayList<BlockModel> getBlocks() {
		return this.blocks;
	}

	public void setBlocks(ArrayList<BlockModel> blocks) {
		this.blocks = blocks;
	}

	public String getReplacer() {
		return this.replacer;
	}

	public void setReplacer(String replacer) {
		this.replacer = replacer;
	}

	public String getCode(HashMap<String, Object> variables) {
		if (getBlocks() == null)
			return "";
		StringBuilder code = new StringBuilder();
		for (int blocksCount = 0; blocksCount < getBlocks().size(); ++blocksCount) {
			if (blocksCount != 0)
				code.append("\n");
			code.append(getBlocks().get(blocksCount).getCode(variables));
		}
		return code.toString();
	}

	@Override
	public BlockHolderLayer clone() {
		BlockHolderLayer clone = new BlockHolderLayer();

		if (getBlocks() != null) {
			ArrayList<BlockModel> cloneBlockHolderLayer = new ArrayList<BlockModel>();
			for (int position = 0; position < getBlocks().size(); ++position) {
				cloneBlockHolderLayer.add(getBlocks().get(position).clone());
			}
			clone.setBlocks(cloneBlockHolderLayer);
		} else {
			clone.setBlocks(null);
		}

		clone.setReplacer(getReplacer() != null ? new String(getReplacer()) : null);

		return clone;
	}
}
