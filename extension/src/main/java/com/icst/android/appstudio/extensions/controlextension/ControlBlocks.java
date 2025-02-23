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

package com.icst.android.appstudio.extensions.controlextension;

import java.util.ArrayList;

import com.icst.android.appstudio.block.model.BlockFieldLayerModel;
import com.icst.android.appstudio.block.model.BlockFieldModel;
import com.icst.android.appstudio.block.model.BlockHolderLayer;
import com.icst.android.appstudio.block.model.BlockLayerModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.BlockValueFieldModel;
import com.icst.android.appstudio.block.tag.BlockModelTag;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;

public final class ControlBlocks {
	public static ArrayList<BlockModel> getBlocks() {
		ArrayList<BlockModel> blocks = new ArrayList<BlockModel>();
		blocks.add(getIfBlock());
		blocks.add(getLoopBlock1());
		return blocks;
	}

	private static BlockModel getIfBlock() {
		BlockModel block = new BlockModel();
		block.setColor("#17A2AF");
		block.setBlockType(BlockModel.Type.defaultBlock);
		block.setDragAllowed(true);
		block.setHolderName("Control");
		block.setReplacerKey("if");

		ArrayList<BlockLayerModel> layers = new ArrayList<BlockLayerModel>();
		BlockFieldLayerModel layer1 = new BlockFieldLayerModel();

		ArrayList<BlockFieldModel> fieldsLayer1 = new ArrayList<BlockFieldModel>();
		BlockFieldModel ifText = new BlockFieldModel();

		ifText.setValue("if");

		fieldsLayer1.add(ifText);

		BlockValueFieldModel booleanField = new BlockValueFieldModel();
		booleanField.setAcceptors(new String[] { "boolean" });
		booleanField.setEnableEdit(true);
		booleanField.setFieldType(BlockValueFieldModel.FieldType.FIELD_BOOLEAN);
		booleanField.setReplacer("boolean");

		fieldsLayer1.add(booleanField);

		layer1.setBlockFields(fieldsLayer1);

		layers.add(layer1);

		BlockHolderLayer layer2 = new BlockHolderLayer();
		layer2.setReplacer("ifConditonCode");

		layers.add(layer2);
		block.setBlockLayerModel(layers);

		BlockModelTag tag = new BlockModelTag();
		tag.setSupportedFileExtensions(new String[] { "java" });
		block.setTags(tag);

		StringBuilder rawCode = new StringBuilder();
		rawCode.append("if (");
		rawCode.append(RawCodeReplacer.getReplacer(block.getReplacerKey(), "boolean"));
		rawCode.append(") {\n\t");
		rawCode.append(RawCodeReplacer.getReplacer(block.getReplacerKey(), "ifConditonCode"));
		rawCode.append("\n}");

		block.setRawCode(rawCode.toString());

		return block;
	}

	private static BlockModel getLoopBlock1() {
		BlockModel block = new BlockModel();
		block.setColor("#17A2AF");
		block.setBlockType(BlockModel.Type.defaultBlock);
		block.setDragAllowed(true);
		block.setHolderName("Control");
		block.setReplacerKey("for-each-loop");

		ArrayList<BlockLayerModel> layers = new ArrayList<BlockLayerModel>();
		BlockFieldLayerModel layer1 = new BlockFieldLayerModel();

		ArrayList<BlockFieldModel> fieldsLayer1 = new ArrayList<BlockFieldModel>();

		BlockFieldModel forVarText = new BlockFieldModel();
		forVarText.setValue("for variable > ");
		fieldsLayer1.add(forVarText);

		BlockValueFieldModel variable = new BlockValueFieldModel();
		variable.setEnableEdit(true);
		variable.setFieldType(BlockValueFieldModel.FieldType.FIELD_INPUT_ONLY);
		variable.setReplacer("variable");
		fieldsLayer1.add(variable);

		BlockFieldModel equalText = new BlockFieldModel();
		equalText.setValue("=");
		fieldsLayer1.add(equalText);

		BlockValueFieldModel variableValue = new BlockValueFieldModel();
		variableValue.setAcceptors(new String[] { "int", "Integer" });
		variableValue.setEnableEdit(true);
		variableValue.setFieldType(BlockValueFieldModel.FieldType.FIELD_NUMBER);
		variableValue.setReplacer("variableValue");
		fieldsLayer1.add(variableValue);

		BlockFieldModel conditionText = new BlockFieldModel();
		conditionText.setValue("if");
		fieldsLayer1.add(conditionText);

		BlockValueFieldModel booleanField = new BlockValueFieldModel();
		booleanField.setAcceptors(new String[] { "boolean" });
		booleanField.setEnableEdit(true);
		booleanField.setFieldType(BlockValueFieldModel.FieldType.FIELD_BOOLEAN);
		booleanField.setReplacer("condition");

		fieldsLayer1.add(booleanField);

		BlockFieldModel text = new BlockFieldModel();
		text.setValue("then variable +1 then repeat");
		fieldsLayer1.add(text);

		layer1.setBlockFields(fieldsLayer1);

		layers.add(layer1);

		BlockHolderLayer layer2 = new BlockHolderLayer();
		layer2.setReplacer("loopCode");

		layers.add(layer2);
		block.setBlockLayerModel(layers);

		BlockModelTag tag = new BlockModelTag();
		tag.setSupportedFileExtensions(new String[] { "java" });
		block.setTags(tag);

		StringBuilder rawCode = new StringBuilder();
		rawCode.append("for (");
		rawCode.append(RawCodeReplacer.getReplacer(block.getReplacerKey(), "variable"));
		rawCode.append(" = ");
		rawCode.append(RawCodeReplacer.getReplacer(block.getReplacerKey(), "variableValue"));
		rawCode.append("; ");
		rawCode.append(RawCodeReplacer.getReplacer(block.getReplacerKey(), "condition"));
		rawCode.append("; ");
		rawCode.append(RawCodeReplacer.getReplacer(block.getReplacerKey(), "variable"));
		rawCode.append("++) {\n\t");
		rawCode.append(RawCodeReplacer.getReplacer(block.getReplacerKey(), "loopCode"));
		rawCode.append("\n}");

		block.setRawCode(rawCode.toString());

		return block;
	}
}
