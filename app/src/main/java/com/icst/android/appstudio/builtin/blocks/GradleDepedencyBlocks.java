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

package com.icst.android.appstudio.builtin.blocks;

import java.util.ArrayList;

import com.icst.android.appstudio.block.model.BlockFieldLayerModel;
import com.icst.android.appstudio.block.model.BlockFieldModel;
import com.icst.android.appstudio.block.model.BlockHolderLayer;
import com.icst.android.appstudio.block.model.BlockHolderModel;
import com.icst.android.appstudio.block.model.BlockLayerModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.BlockValueFieldModel;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;
import com.icst.android.appstudio.builtin.blockfield.PackageNameBlockField;

public class GradleDepedencyBlocks {
	public static ArrayList<BlockHolderModel> getGradleDepedencyBlocks() {
		ArrayList<BlockHolderModel> holders = new ArrayList<BlockHolderModel>();
		BlockHolderModel holder = new BlockHolderModel();
		holder.setColor("#E27625");
		holder.setName("dependency");

		ArrayList<Object> dependencyBlocksList = new ArrayList<Object>();
		dependencyBlocksList.add(getImplementationBlock(true, true, null));

		holder.setList(dependencyBlocksList);

		holders.add(holder);
		return holders;
	}

	public static ArrayList<BlockHolderModel> getGradleAndroidBlocks() {
		ArrayList<BlockHolderModel> holders = new ArrayList<BlockHolderModel>();
		BlockHolderModel holder = new BlockHolderModel();
		holder.setColor("#1B0B54");
		holder.setName("Android");

		ArrayList<Object> androidBlocksList = new ArrayList<Object>();

		androidBlocksList.add(getNameSpaceBlock(true, null));

		holder.setList(androidBlocksList);

		holders.add(holder);
		return holders;
	}

	public static BlockModel getNameSpaceBlock(boolean allowDrag, String nameSpace) {
		BlockModel block = new BlockModel();
		block.setBlockType(BlockModel.Type.defaultBlock);
		block.setColor("#0061FE");
		block.setReplacerKey("namespaceBlock");
		block.setDragAllowed(allowDrag);

		StringBuilder rawCode = new StringBuilder();
		rawCode.append("namespace \"");
		rawCode.append(RawCodeReplacer.getReplacer(block.getReplacerKey(), "namespace"));
		rawCode.append("\"");

		block.setRawCode(rawCode.toString());

		ArrayList<BlockLayerModel> blockLayers = new ArrayList<BlockLayerModel>();

		BlockFieldLayerModel layer1 = new BlockFieldLayerModel();

		ArrayList<BlockFieldModel> layer1Fields = new ArrayList<BlockFieldModel>();

		BlockFieldModel nameSpaceText = new BlockFieldModel();
		nameSpaceText.setValue("namespace");

		PackageNameBlockField inputNameSpaceField = new PackageNameBlockField();
		inputNameSpaceField.setReplacer("namespace");
		if (nameSpace != null) {
			inputNameSpaceField.setValue(nameSpace);
		}

		layer1Fields.add(nameSpaceText);
		layer1Fields.add(inputNameSpaceField);

		layer1.setBlockFields(layer1Fields);
		blockLayers.add(layer1);
		block.setBlockLayerModel(blockLayers);
		return block;
	}

	public static BlockModel getImplementationBlock(
			boolean allowDrag, boolean allowLibraryChange, String libraryValue) {
		BlockModel implementationBlock = new BlockModel();
		implementationBlock.setBlockType(BlockModel.Type.defaultBlock);
		implementationBlock.setColor("#E27625");
		implementationBlock.setRawCode("implementation");
		implementationBlock.setReplacerKey("implementationBlock");
		implementationBlock.setDragAllowed(allowDrag);

		StringBuilder rawCode = new StringBuilder();
		rawCode.append("implementation \"");
		rawCode.append(RawCodeReplacer.getReplacer(implementationBlock.getReplacerKey(), "library"));
		rawCode.append("\"");

		implementationBlock.setRawCode(rawCode.toString());

		ArrayList<BlockLayerModel> implementationBlockLayers = new ArrayList<BlockLayerModel>();

		BlockFieldLayerModel implementationBlockLayer1 = new BlockFieldLayerModel();

		ArrayList<BlockFieldModel> implementationBlockLayer1Fields = new ArrayList<BlockFieldModel>();

		BlockFieldModel implementationText = new BlockFieldModel();
		implementationText.setValue("implementation");

		BlockValueFieldModel inputDependencyField = new BlockValueFieldModel();
		inputDependencyField.setFieldType(BlockValueFieldModel.FieldType.FIELD_INPUT_ONLY);
		inputDependencyField.setReplacer("library");
		inputDependencyField.setEnableEdit(allowLibraryChange);
		if (libraryValue != null) {
			inputDependencyField.setValue(libraryValue);
		}

		implementationBlockLayer1Fields.add(implementationText);
		implementationBlockLayer1Fields.add(inputDependencyField);

		implementationBlockLayer1.setBlockFields(implementationBlockLayer1Fields);

		implementationBlockLayers.add(implementationBlockLayer1);

		implementationBlock.setBlockLayerModel(implementationBlockLayers);

		return implementationBlock;
	}

	public static BlockModel getDefaultConfigBlock(boolean allowDrag) {
		BlockModel defaultConfigBlock = new BlockModel();
		defaultConfigBlock.setBlockType(BlockModel.Type.defaultBlock);
		defaultConfigBlock.setColor("#4759B8");
		defaultConfigBlock.setReplacerKey("defaultConfigBlock");
		defaultConfigBlock.setDragAllowed(allowDrag);

		StringBuilder rawCode = new StringBuilder();
		rawCode.append("defaultConfig {\n\t");
		rawCode.append(RawCodeReplacer.getReplacer(defaultConfigBlock.getReplacerKey(), "config"));
		rawCode.append("\n}");

		defaultConfigBlock.setRawCode(rawCode.toString());

		ArrayList<BlockLayerModel> defaultConfigBlockLayers = new ArrayList<BlockLayerModel>();

		// Layer 1st
		BlockFieldLayerModel defaultConfigBlockLayer1 = new BlockFieldLayerModel();

		ArrayList<BlockFieldModel> defaultConfigBlockLayer1Fields = new ArrayList<BlockFieldModel>();

		BlockFieldModel defaultConfigText = new BlockFieldModel();
		defaultConfigText.setValue("defaultConfig");

		defaultConfigBlockLayer1Fields.add(defaultConfigText);

		defaultConfigBlockLayer1.setBlockFields(defaultConfigBlockLayer1Fields);

		// Layer 2nd
		BlockHolderLayer defaultConfigBlockLayer2 = new BlockHolderLayer();
		defaultConfigBlockLayer2.setReplacer("config");

		defaultConfigBlockLayers.add(defaultConfigBlockLayer1);
		defaultConfigBlockLayers.add(defaultConfigBlockLayer2);

		defaultConfigBlock.setBlockLayerModel(defaultConfigBlockLayers);

		return defaultConfigBlock;
	}
}
