/*
 *  This file is part of Block IDLE.
 *
 *  Block IDLE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Block IDLE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Block IDLE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.blockidle.javablocks;

import java.util.ArrayList;

import com.icst.blockidle.beans.ActionBlockLayerBean;
import com.icst.blockidle.beans.BlockBean;
import com.icst.blockidle.beans.BlockElementBean;
import com.icst.blockidle.beans.BlockElementLayerBean;
import com.icst.blockidle.beans.BlockPaletteBean;
import com.icst.blockidle.beans.BooleanBlockElementBean;
import com.icst.blockidle.beans.LabelBlockElementBean;
import com.icst.blockidle.beans.LayerBean;
import com.icst.blockidle.beans.RegularBlockBean;
import com.icst.blockidle.beans.utils.BuiltInDatatypes;
import com.icst.blockidle.beans.utils.CodeFormatterUtils;

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
		inputBoolean.setAcceptedReturnType(BuiltInDatatypes.getPrimitiveBooleanDatatype());
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
		inputBoolean.setAcceptedReturnType(BuiltInDatatypes.getPrimitiveBooleanDatatype());
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
