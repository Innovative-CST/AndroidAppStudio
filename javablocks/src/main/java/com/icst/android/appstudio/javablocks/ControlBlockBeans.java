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

package com.icst.android.appstudio.javablocks;

import java.util.ArrayList;

import com.icst.android.appstudio.beans.ActionBlockLayerBean;
import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.BlockElementBean;
import com.icst.android.appstudio.beans.BlockElementLayerBean;
import com.icst.android.appstudio.beans.BlockPaletteBean;
import com.icst.android.appstudio.beans.BooleanBlockElementBean;
import com.icst.android.appstudio.beans.LabelBlockElementBean;
import com.icst.android.appstudio.beans.LayerBean;
import com.icst.android.appstudio.beans.RegularBlockBean;
import com.icst.android.appstudio.beans.utils.CodeFormatterUtils;

public final class ControlBlockBeans {

	public static BlockPaletteBean getControlBlockPalette() {
		BlockPaletteBean mIOBlockPalette = new BlockPaletteBean();
		mIOBlockPalette.setColor("#FFC041");
		mIOBlockPalette.setName("Flow of Control");
		ArrayList<BlockBean> blocks = new ArrayList<>();
		blocks.add(ifBlock());
		blocks.add(ifElseBlock());
		mIOBlockPalette.setBlocks(blocks);
		return mIOBlockPalette;
	}

	private static RegularBlockBean ifBlock() {
		RegularBlockBean block = new RegularBlockBean();
		block.setColor("#FFC041");

		ArrayList<LayerBean> layers = new ArrayList<LayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean ifLabel = new LabelBlockElementBean();
		ifLabel.setLabel("if");
		layer1Elements.add(ifLabel);

		BooleanBlockElementBean inputBoolean = new BooleanBlockElementBean();
		inputBoolean.setKey("mBoolean");
		layer1Elements.add(inputBoolean);

		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		ActionBlockLayerBean statementLayer = new ActionBlockLayerBean();
		statementLayer.setKey("statements");

		layers.add(statementLayer);
		block.setLayers(layers);

		StringBuilder code = new StringBuilder();
		code.append("if (");
		code.append(CodeFormatterUtils.getKeySyntaxString("mBoolean"));
		code.append(") {\n\t");
		code.append(CodeFormatterUtils.getKeySyntaxString("statements"));
		code.append("\n}");

		block.setCodeSyntax(code.toString());

		return block;
	}

	private static RegularBlockBean ifElseBlock() {
		RegularBlockBean block = new RegularBlockBean();
		block.setColor("#FFC041");

		ArrayList<LayerBean> layers = new ArrayList<LayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean ifLabel = new LabelBlockElementBean();
		ifLabel.setLabel("if");
		layer1Elements.add(ifLabel);

		BooleanBlockElementBean inputBoolean = new BooleanBlockElementBean();
		inputBoolean.setKey("mBoolean");
		layer1Elements.add(inputBoolean);

		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		ActionBlockLayerBean statementLayer = new ActionBlockLayerBean();
		statementLayer.setKey("statements");

		layers.add(statementLayer);

		BlockElementLayerBean layer3 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer3Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean elseLabel = new LabelBlockElementBean();
		elseLabel.setLabel("else");
		layer3Elements.add(elseLabel);

		layer3.setBlockElementBeans(layer3Elements);

		layers.add(layer3);

		ActionBlockLayerBean elseStatementLayer = new ActionBlockLayerBean();
		elseStatementLayer.setKey("elseStatement");

		layers.add(elseStatementLayer);

		block.setLayers(layers);

		StringBuilder code = new StringBuilder();
		code.append("if (");
		code.append(CodeFormatterUtils.getKeySyntaxString("mBoolean"));
		code.append(") {\n\t");
		code.append(CodeFormatterUtils.getKeySyntaxString("statements"));
		code.append("\n} else {\n\t");
		code.append(CodeFormatterUtils.getKeySyntaxString("elseStatement"));
		code.append("\n}");

		block.setCodeSyntax(code.toString());

		return block;
	}
}
