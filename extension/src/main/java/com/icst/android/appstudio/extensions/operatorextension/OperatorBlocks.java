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
import com.icst.android.appstudio.block.model.BlockLayerModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.BlockValueFieldModel;
import com.icst.android.appstudio.block.tag.AdditionalCodeHelperTag;
import com.icst.android.appstudio.block.tag.BlockModelTag;
import com.icst.android.appstudio.block.tag.DependencyTag;
import com.icst.android.appstudio.block.tag.ImportTag;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;

public final class OperatorBlocks {
	public static ArrayList<BlockModel> getBlocks() {
		ArrayList<BlockModel> blocks = new ArrayList<BlockModel>();
		blocks.add(getTrueBlock());
		blocks.add(getFalseBlock());
		blocks.add(getNumberEqualBlock());
		return blocks;
	}

	private static BlockModel getTrueBlock() {
		BlockModel block = new BlockModel();
		block.setColor("#3DE25B");
		block.setBlockType(BlockModel.Type.defaultBoolean);
		block.setDragAllowed(true);
		block.setHolderName("Operator");
		block.setReplacerKey("true");

		ArrayList<BlockLayerModel> layers = new ArrayList<BlockLayerModel>();
		BlockFieldLayerModel layer1 = new BlockFieldLayerModel();

		ArrayList<BlockFieldModel> fieldsLayer1 = new ArrayList<BlockFieldModel>();
		BlockFieldModel trueText = new BlockFieldModel();

		trueText.setValue("true");

		fieldsLayer1.add(trueText);
		layer1.setBlockFields(fieldsLayer1);

		layers.add(layer1);
		block.setBlockLayerModel(layers);

		BlockModelTag tag = new BlockModelTag();
		tag.setSupportedFileExtensions(new String[] { "java" });

		ImportTag booleanImport = new ImportTag();
		booleanImport.setImportClass("java.lang.Boolean");

		tag.setAdditionalTags(new AdditionalCodeHelperTag[] { booleanImport });
		block.setTags(tag);

		block.setRawCode("true");

		return block;
	}

	private static BlockModel getFalseBlock() {
		BlockModel block = new BlockModel();
		block.setColor("#FE4E66");
		block.setBlockType(BlockModel.Type.defaultBoolean);
		block.setDragAllowed(true);
		block.setHolderName("Operator");
		block.setReplacerKey("false");

		ArrayList<BlockLayerModel> layers = new ArrayList<BlockLayerModel>();
		BlockFieldLayerModel layer1 = new BlockFieldLayerModel();

		ArrayList<BlockFieldModel> fieldsLayer1 = new ArrayList<BlockFieldModel>();
		BlockFieldModel falseText = new BlockFieldModel();

		falseText.setValue("false");

		fieldsLayer1.add(falseText);
		layer1.setBlockFields(fieldsLayer1);

		layers.add(layer1);
		block.setBlockLayerModel(layers);

		BlockModelTag tag = new BlockModelTag();
		tag.setSupportedFileExtensions(new String[] { "java" });

		ImportTag booleanImport = new ImportTag();
		booleanImport.setImportClass("java.lang.Boolean");

		tag.setAdditionalTags(new AdditionalCodeHelperTag[] { booleanImport });
		block.setTags(tag);

		block.setRawCode("false");

		return block;
	}

	private static BlockModel getNumberEqualBlock() {
		BlockModel block = new BlockModel();
		block.setColor("#4DB732");
		block.setBlockType(BlockModel.Type.defaultBoolean);
		block.setDragAllowed(true);
		block.setHolderName("Operator");
		block.setReplacerKey("condition");
		block.setReturns(new String[] { "boolean", "Boolean" });

		ArrayList<BlockLayerModel> layers = new ArrayList<BlockLayerModel>();
		BlockFieldLayerModel layer1 = new BlockFieldLayerModel();

		ArrayList<BlockFieldModel> fieldsLayer1 = new ArrayList<BlockFieldModel>();

		BlockValueFieldModel number1 = new BlockValueFieldModel();
		number1.setAcceptors(new String[] { "int", "Integer" });
		number1.setEnableEdit(true);
		number1.setFieldType(BlockValueFieldModel.FieldType.FIELD_NUMBER);
		number1.setReplacer("number1");
		fieldsLayer1.add(number1);

		BlockFieldModel equalText = new BlockFieldModel();
		equalText.setValue("=");
		fieldsLayer1.add(equalText);

		BlockValueFieldModel number2 = new BlockValueFieldModel();
		number2.setAcceptors(new String[] { "int", "Integer" });
		number2.setEnableEdit(true);
		number2.setFieldType(BlockValueFieldModel.FieldType.FIELD_NUMBER);
		number2.setReplacer("number2");
		fieldsLayer1.add(number2);

		layer1.setBlockFields(fieldsLayer1);

		layers.add(layer1);
		block.setBlockLayerModel(layers);

		BlockModelTag tag = new BlockModelTag();
		tag.setSupportedFileExtensions(new String[] { "java" });

		DependencyTag testDeps = new DependencyTag();
		testDeps.setDependencyGroup("com.google.android.material");
		testDeps.setDependencyName("material");
		testDeps.setVersion("1.13.0-alpha03");

		tag.setAdditionalTags(new AdditionalCodeHelperTag[] { testDeps });
		block.setTags(tag);

		StringBuilder rawCode = new StringBuilder();
		rawCode.append(RawCodeReplacer.getReplacer(block.getReplacerKey(), "number1"));
		rawCode.append(" == ");
		rawCode.append(RawCodeReplacer.getReplacer(block.getReplacerKey(), "number2"));

		block.setRawCode(rawCode.toString());

		return block;
	}
}
