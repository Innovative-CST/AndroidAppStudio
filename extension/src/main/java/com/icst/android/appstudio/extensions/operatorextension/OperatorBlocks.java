/*
 * This file is part of Android AppStudio [https://github.com/Innovative-CST/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
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
		testDeps.setDependencyGroup("group");
		testDeps.setDependencyName("name");
		testDeps.setVersion("version");

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
