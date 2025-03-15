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
import com.icst.blockidle.beans.BooleanBlockBean;
import com.icst.blockidle.beans.BooleanBlockElementBean;
import com.icst.blockidle.beans.LabelBlockElementBean;
import com.icst.blockidle.beans.NumericBlockBean;
import com.icst.blockidle.beans.NumericBlockElementBean;
import com.icst.blockidle.beans.StringBlockBean;
import com.icst.blockidle.beans.StringBlockElementBean;
import com.icst.blockidle.beans.utils.BuiltInDatatypes;
import com.icst.blockidle.beans.utils.CodeFormatterUtils;

public class OperatorBlockBeans {

	public static BlockPaletteBean getOperatorBlockPalette() {
		BlockPaletteBean mOperatorBlockPalette = new BlockPaletteBean();
		mOperatorBlockPalette.setColor("#50BE36");
		mOperatorBlockPalette.setName("Operators");
		ArrayList<BlockBean> blocks = new ArrayList<>();
		blocks.add(trim());
		blocks.add(trueBlock());
		blocks.add(notBlock());
		blocks.add(equalNumBool());
		blocks.add(stringToIntegerBlock());
		mOperatorBlockPalette.setBlocks(blocks);
		return mOperatorBlockPalette;
	}

	private static BooleanBlockBean equalNumBool() {
		BooleanBlockBean block = new BooleanBlockBean();
		block.setColor("#50BE36");
		ArrayList<BlockElementLayerBean> layers = new ArrayList<BlockElementLayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		NumericBlockElementBean num1 = new NumericBlockElementBean();
		num1.setKey("number1");
		num1.setAcceptedReturnType(BuiltInDatatypes.getPrimitiveIntegerDatatype());
		layer1Elements.add(num1);

		LabelBlockElementBean isEqualLabel = new LabelBlockElementBean();
		isEqualLabel.setLabel("is equal to");
		layer1Elements.add(isEqualLabel);

		NumericBlockElementBean num2 = new NumericBlockElementBean();
		num2.setKey("number2");
		num2.setAcceptedReturnType(BuiltInDatatypes.getPrimitiveIntegerDatatype());
		layer1Elements.add(num2);

		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		block.setElementsLayers(layers);

		StringBuilder code = new StringBuilder();
		code.append(CodeFormatterUtils.getKeySyntaxString("number1"));
		code.append(" == ");
		code.append(CodeFormatterUtils.getKeySyntaxString("number2"));

		block.setCodeSyntax(code.toString());
		return block;
	}

	private static NumericBlockBean stringToIntegerBlock() {
		NumericBlockBean block = new NumericBlockBean();
		block.setColor("#50BE36");
		ArrayList<BlockElementLayerBean> layers = new ArrayList<BlockElementLayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		StringBlockElementBean stringField = new StringBlockElementBean();
		stringField.setKey("string");
		layer1Elements.add(stringField);

		LabelBlockElementBean toIntTextLabel = new LabelBlockElementBean();
		toIntTextLabel.setLabel("to integer");
		layer1Elements.add(toIntTextLabel);

		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		block.setElementsLayers(layers);
		StringBuilder code = new StringBuilder();
		code.append("Integer.valueOf(");
		code.append(CodeFormatterUtils.getKeySyntaxString("string"));
		code.append(")");

		block.setCodeSyntax(code.toString());
		block.setReturnDatatype(BuiltInDatatypes.getIntegerDatatype());

		return block;
	}

	private static BooleanBlockBean notBlock() {
		BooleanBlockBean block = new BooleanBlockBean();
		block.setColor("#50BE36");
		ArrayList<BlockElementLayerBean> layers = new ArrayList<BlockElementLayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean trueText = new LabelBlockElementBean();
		trueText.setLabel("not");
		layer1Elements.add(trueText);

		BooleanBlockElementBean booleanField = new BooleanBlockElementBean();
		booleanField.setAcceptedReturnType(BuiltInDatatypes.getPrimitiveBooleanDatatype());
		booleanField.setKey("mBool");

		layer1Elements.add(booleanField);

		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		block.setElementsLayers(layers);

		StringBuilder code = new StringBuilder();
		code.append("!");
		code.append(CodeFormatterUtils.getKeySyntaxString("mBool"));

		block.setCodeSyntax(code.toString());
		block.setReturnDatatype(BuiltInDatatypes.getPrimitiveBooleanDatatype());
		return block;
	}

	private static BooleanBlockBean trueBlock() {
		BooleanBlockBean block = new BooleanBlockBean();
		block.setColor("#50BE36");
		ArrayList<BlockElementLayerBean> layers = new ArrayList<BlockElementLayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean trueText = new LabelBlockElementBean();
		trueText.setLabel("true");
		layer1Elements.add(trueText);

		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		block.setElementsLayers(layers);

		StringBuilder code = new StringBuilder();
		code.append("true");

		block.setCodeSyntax(code.toString());
		block.setReturnDatatype(BuiltInDatatypes.getPrimitiveBooleanDatatype());
		return block;
	}

	private static BooleanBlockBean falseBlock() {
		BooleanBlockBean block = new BooleanBlockBean();
		block.setColor("#E30101");
		ArrayList<BlockElementLayerBean> layers = new ArrayList<BlockElementLayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean falseText = new LabelBlockElementBean();
		falseText.setLabel("false");
		layer1Elements.add(falseText);

		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		block.setElementsLayers(layers);

		StringBuilder code = new StringBuilder();
		code.append("false");

		block.setCodeSyntax(code.toString());
		block.setReturnDatatype(BuiltInDatatypes.getPrimitiveBooleanDatatype());
		return block;
	}

	private static StringBlockBean trim() {
		StringBlockBean block = new StringBlockBean();
		block.setColor("#50BE36");
		ArrayList<BlockElementLayerBean> layers = new ArrayList<BlockElementLayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean trim = new LabelBlockElementBean();
		trim.setLabel("trim");
		layer1Elements.add(trim);

		StringBlockElementBean inputString = new StringBlockElementBean();
		inputString.setKey("mString");
		layer1Elements.add(inputString);

		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		block.setElementsLayers(layers);

		StringBuilder code = new StringBuilder();
		code.append(CodeFormatterUtils.getKeySyntaxString("mString"));
		code.append(".trim()");

		block.setCodeSyntax(code.toString());
		return block;
	}

}
