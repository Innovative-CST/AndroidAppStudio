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

import com.icst.android.appstudio.block.tag.AdditionalCodeHelperTag;
import com.icst.android.appstudio.block.tag.BlockModelTag;
import com.icst.android.appstudio.block.utils.ArrayUtils;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;

public class BlockModel extends BlockValueFieldModel implements Serializable, Cloneable {
	public static final long serialVersionUID = 3L;

	private String color;
	private String rawCode;
	private String replacerKey;
	private String holderName;
	private String[] returns;
	private BlockModelTag tags;
	private int blockType;
	private boolean isLastBlock;
	private boolean isFirstBlock;
	private boolean isDragAllowed;
	private ArrayList<BlockLayerModel> blockLayerModel;

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getRawCode() {
		return this.rawCode;
	}

	public void setRawCode(String rawCode) {
		this.rawCode = rawCode;
	}

	public String getReplacerKey() {
		return this.replacerKey;
	}

	public void setReplacerKey(String replacerKey) {
		this.replacerKey = replacerKey;
	}

	public String[] getReturns() {
		return this.returns;
	}

	public void setReturns(String[] returns) {
		this.returns = returns;
	}

	public BlockModelTag getTags() {
		return this.tags;
	}

	public void setTags(BlockModelTag tags) {
		this.tags = tags;
	}

	public final class Type {
		public static final int defaultBlock = 0;
		public static final int defaultBoolean = 1;
		public static final int number = 2;
		public static final int variable = 3;
	}

	public int getBlockType() {
		return this.blockType;
	}

	public void setBlockType(int blockType) {
		this.blockType = blockType;
	}

	public boolean isLastBlock() {
		return this.isLastBlock;
	}

	public void setLastBlock(boolean isLastBlock) {
		this.isLastBlock = isLastBlock;
	}

	public boolean isFirstBlock() {
		return this.isFirstBlock;
	}

	public void setFirstBlock(boolean isFirstBlock) {
		this.isFirstBlock = isFirstBlock;
	}

	public ArrayList<BlockLayerModel> getBlockLayerModel() {
		return this.blockLayerModel;
	}

	public void setBlockLayerModel(ArrayList<BlockLayerModel> blockLayerModel) {
		this.blockLayerModel = blockLayerModel;
	}

	public ArrayList<AdditionalCodeHelperTag> getAdditionalTagsOfBlock() {
		ArrayList<AdditionalCodeHelperTag> tags = new ArrayList<AdditionalCodeHelperTag>();

		// Add this block code helper tag to tags
		if (getTags() != null) {
			if (getTags().getAdditionalTags() != null) {
				for (int i = 0; i < getTags().getAdditionalTags().length; ++i) {
					tags.add(getTags().getAdditionalTags()[i]);
				}
			}
		}

		// Add implements and extends import

		if (getBlockLayerModel() == null)
			return tags;

		for (int layerCount = 0; layerCount < getBlockLayerModel().size(); ++layerCount) {
			if (getBlockLayerModel().get(layerCount) instanceof BlockFieldLayerModel) {
				for (int blockFieldCount = 0; blockFieldCount < ((BlockFieldLayerModel) getBlockLayerModel()
						.get(layerCount))
						.getBlockFields()
						.size(); ++blockFieldCount) {
					if (((BlockFieldLayerModel) getBlockLayerModel().get(layerCount))
							.getBlockFields()
							.get(blockFieldCount) instanceof BlockValueFieldModel) {
						BlockValueFieldModel field = ((BlockValueFieldModel) ((BlockFieldLayerModel) getBlockLayerModel()
								.get(layerCount))
								.getBlockFields()
								.get(blockFieldCount));

						if (field.getBlockModel() != null) {
							tags.addAll(field.getBlockModel().getAdditionalTagsOfBlock());
						} else if (field.getAdditionalTags() != null) {
							for (int i = 0; i < field.getAdditionalTags().length; ++i) {
								tags.add(field.getAdditionalTags()[i]);
							}
						}
					}
				}
			} else if (getBlockLayerModel().get(layerCount) instanceof BlockHolderLayer) {
				BlockHolderLayer layer = (BlockHolderLayer) getBlockLayerModel().get(layerCount);

				if (layer.getBlocks() == null) {
					continue;
				}

				for (int i = 0; i < layer.getBlocks().size(); ++i) {
					tags.addAll(layer.getBlocks().get(i).getAdditionalTagsOfBlock());
				}
			}
		}

		return tags;
	}

	public String getCode(HashMap<String, Object> variables) {
		String generatedCode = new String(getRawCode());

		for (int layerCount = 0; layerCount < getBlockLayerModel().size(); ++layerCount) {
			if (getBlockLayerModel().get(layerCount) instanceof BlockFieldLayerModel) {
				BlockFieldLayerModel layer = (BlockFieldLayerModel) getBlockLayerModel().get(layerCount);
				for (int blockFieldCount = 0; blockFieldCount < layer.getBlockFields().size(); ++blockFieldCount) {
					if ((layer.getBlockFields().get(blockFieldCount) instanceof BlockValueFieldModel)) {
						generatedCode = generatedCode.replace(
								RawCodeReplacer.getReplacer(
										getReplacerKey(),
										((BlockValueFieldModel) layer.getBlockFields().get(blockFieldCount))
												.getReplacer()),
								((BlockValueFieldModel) layer.getBlockFields().get(blockFieldCount))
										.getCode(variables));
					}
				}
			}

			if (getBlockLayerModel().get(layerCount) instanceof BlockHolderLayer) {
				BlockHolderLayer layer = (BlockHolderLayer) getBlockLayerModel().get(layerCount);

				// Get space for formatting....
				String formatter = null;
				String[] lines = getRawCode().split("\n");
				for (String line : lines) {
					if (line.contains(RawCodeReplacer.getReplacer(getReplacerKey(), layer.getReplacer()))) {
						formatter = line.substring(
								0,
								line.indexOf(
										RawCodeReplacer.getReplacer(getReplacerKey(), layer.getReplacer())));
					}
				}

				StringBuilder layerCode = new StringBuilder();

				String[] layerCodeLines = layer.getCode(variables).split("\n");
				for (int layerCodeLinePosition = 0; layerCodeLinePosition < layerCodeLines.length; ++layerCodeLinePosition) {
					if (layerCodeLinePosition != 0)
						layerCode.append("\n");
					if (layerCodeLinePosition != 0)
						layerCode.append(formatter);
					layerCode.append(layerCodeLines[layerCodeLinePosition]);
				}

				generatedCode = generatedCode.replace(
						RawCodeReplacer.getReplacer(getReplacerKey(), layer.getReplacer()),
						layerCode.toString());
			}
		}

		generatedCode = RawCodeReplacer.removeAndroidAppStudioString(getReplacerKey(), generatedCode);

		return generatedCode;
	}

	public boolean isDragAllowed() {
		return this.isDragAllowed;
	}

	public void setDragAllowed(boolean isDragAllowed) {
		this.isDragAllowed = isDragAllowed;
	}

	@Override
	public BlockModel clone() {
		BlockModel block = new BlockModel();
		block.setColor(getColor() == null ? null : new String(getColor()));
		block.setRawCode(getRawCode() == null ? null : new String(getRawCode()));
		block.setReplacerKey(getReplacerKey() == null ? null : new String(getReplacerKey()));
		block.setBlockType(new Integer(getBlockType()));
		block.setDragAllowed(new Boolean(isDragAllowed()));
		block.setFirstBlock(new Boolean(isFirstBlock()));
		block.setLastBlock(new Boolean(isLastBlock()));
		block.setTags(getTags() == null ? null : getTags().clone());
		block.setReturns(ArrayUtils.clone(getReturns()));
		block.setHolderName(getHolderName() == null ? null : new String(getHolderName()));

		if (getBlockLayerModel() != null) {
			ArrayList<BlockLayerModel> cloneBlockLayerModel = new ArrayList<BlockLayerModel>();
			for (int layerCount = 0; layerCount < getBlockLayerModel().size(); ++layerCount) {
				if (getBlockLayerModel().get(layerCount) instanceof BlockFieldLayerModel) {
					cloneBlockLayerModel.add(
							((BlockFieldLayerModel) getBlockLayerModel().get(layerCount)).clone());
				}
				if (getBlockLayerModel().get(layerCount) instanceof BlockHolderLayer) {
					cloneBlockLayerModel.add(
							((BlockHolderLayer) getBlockLayerModel().get(layerCount)).clone());
				}
			}

			block.setBlockLayerModel(cloneBlockLayerModel);
		} else
			block.setBlockLayerModel(null);

		return block;
	}

	public String getHolderName() {
		return this.holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}
}
