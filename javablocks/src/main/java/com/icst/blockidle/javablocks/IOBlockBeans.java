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

import com.icst.blockidle.beans.BlockBean;
import com.icst.blockidle.beans.BlockElementBean;
import com.icst.blockidle.beans.BlockElementLayerBean;
import com.icst.blockidle.beans.BlockPaletteBean;
import com.icst.blockidle.beans.BooleanBlockElementBean;
import com.icst.blockidle.beans.LabelBlockElementBean;
import com.icst.blockidle.beans.LayerBean;
import com.icst.blockidle.beans.RegularBlockBean;
import com.icst.blockidle.beans.StringBlockElementBean;
import com.icst.blockidle.beans.utils.BuiltInDatatypes;
import com.icst.blockidle.beans.utils.CodeFormatterUtils;

public final class IOBlockBeans {
	public static BlockPaletteBean getIOBlockPalette() {
		BlockPaletteBean mIOBlockPalette = new BlockPaletteBean();
		mIOBlockPalette.setColor("#a360ff");
		mIOBlockPalette.setName("IO Blocks");
		ArrayList<BlockBean> blocks = new ArrayList<>();
		blocks.add(print());
		blocks.add(printBoolean());
		mIOBlockPalette.setBlocks(blocks);
		return mIOBlockPalette;
	}

	private static RegularBlockBean print() {
		RegularBlockBean block = new RegularBlockBean();
		block.setColor("#a360ff");

		ArrayList<LayerBean> layers = new ArrayList<LayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean print = new LabelBlockElementBean();
		print.setLabel("print");
		layer1Elements.add(print);

		StringBlockElementBean inputString = new StringBlockElementBean();
		inputString.setKey("mString");
		layer1Elements.add(inputString);

		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		block.setLayers(layers);

		StringBuilder code = new StringBuilder();
		code.append("System.out.println(");
		code.append(CodeFormatterUtils.getKeySyntaxString("mString"));
		code.append(");");

		block.setCodeSyntax(code.toString());

		return block;
	}

	private static RegularBlockBean printBoolean() {
		RegularBlockBean block = new RegularBlockBean();
		block.setColor("#a360ff");

		ArrayList<LayerBean> layers = new ArrayList<LayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean print = new LabelBlockElementBean();
		print.setLabel("print");
		layer1Elements.add(print);

		BooleanBlockElementBean inputBoolean = new BooleanBlockElementBean();
		inputBoolean.setKey("mBoolean");
		inputBoolean.setAcceptedReturnType(BuiltInDatatypes.getPrimitiveBooleanDatatype());
		layer1Elements.add(inputBoolean);

		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		block.setLayers(layers);

		StringBuilder code = new StringBuilder();
		code.append("System.out.println(");
		code.append(CodeFormatterUtils.getKeySyntaxString("mBoolean"));
		code.append(");");

		block.setCodeSyntax(code.toString());

		return block;
	}
}
