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

import com.icst.android.appstudio.block.model.BlockFieldLayerModel;
import com.icst.android.appstudio.block.model.BlockFieldModel;
import com.icst.android.appstudio.block.model.BlockHolderLayer;
import com.icst.android.appstudio.block.model.BlockLayerModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.BlockValueFieldModel;
import com.icst.android.appstudio.block.tag.BlockModelTag;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;
import java.util.ArrayList;

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
    booleanField.setAcceptors(new String[] {"boolean"});
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
    tag.setSupportedFileExtensions(new String[] {"java"});
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
    variableValue.setAcceptors(new String[] {"int", "Integer"});
    variableValue.setEnableEdit(true);
    variableValue.setFieldType(BlockValueFieldModel.FieldType.FIELD_NUMBER);
    variableValue.setReplacer("variableValue");
    fieldsLayer1.add(variableValue);

    BlockFieldModel conditionText = new BlockFieldModel();
    conditionText.setValue("if");
    fieldsLayer1.add(conditionText);

    BlockValueFieldModel booleanField = new BlockValueFieldModel();
    booleanField.setAcceptors(new String[] {"boolean"});
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
    tag.setSupportedFileExtensions(new String[] {"java"});
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
