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

package com.icst.android.appstudio.javablocks;

import java.util.ArrayList;

import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.BlockElementBean;
import com.icst.android.appstudio.beans.BlockElementLayerBean;
import com.icst.android.appstudio.beans.BlockPaletteBean;
import com.icst.android.appstudio.beans.DatatypeBean;
import com.icst.android.appstudio.beans.GeneralExpressionBlockBean;
import com.icst.android.appstudio.beans.GeneralExpressionBlockElementBean;
import com.icst.android.appstudio.beans.LabelBlockElementBean;
import com.icst.android.appstudio.beans.utils.BuiltInDatatypes;
import com.icst.android.appstudio.beans.utils.CodeFormatterUtils;

public class ClassBlockBeans {

	public static BlockPaletteBean getClassBlockPalette() {
		BlockPaletteBean mClassBlockPalette = new BlockPaletteBean();
		mClassBlockPalette.setColor("#0061FE");
		mClassBlockPalette.setName("Class");
		ArrayList<BlockBean> blocks = new ArrayList<>();
		blocks.add(getClassBlock());
		blocks.add(getObjectClassBlock());
		mClassBlockPalette.setBlocks(blocks);
		return mClassBlockPalette;
	}

	private static GeneralExpressionBlockBean getClassBlock() {
		GeneralExpressionBlockBean block = new GeneralExpressionBlockBean();
		block.setColor("#0061FE");

		ArrayList<BlockElementLayerBean> layers = new ArrayList<BlockElementLayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean getClassLabel = new LabelBlockElementBean();
		getClassLabel.setLabel("getClass");
		layer1Elements.add(getClassLabel);

		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		block.setElementsLayers(layers);

		block.setCodeSyntax("getClass()");
		block.setReturnDatatype(BuiltInDatatypes.getClassDatatype());

		return block;
	}

	private static GeneralExpressionBlockBean getObjectClassBlock() {
		GeneralExpressionBlockBean block = new GeneralExpressionBlockBean();
		block.setColor("#0061FE");

		ArrayList<BlockElementLayerBean> layers = new ArrayList<BlockElementLayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		DatatypeBean objDatatype = new DatatypeBean("java.lang.Object", "Object");

		GeneralExpressionBlockElementBean objectInput = new GeneralExpressionBlockElementBean();
		objectInput.setKey("object");
		objectInput.setAcceptedReturnType(objDatatype);
		layer1Elements.add(objectInput);

		LabelBlockElementBean getClassLabel = new LabelBlockElementBean();
		getClassLabel.setLabel("getClass");
		layer1Elements.add(getClassLabel);

		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		block.setElementsLayers(layers);

		StringBuilder code = new StringBuilder();
		code.append(CodeFormatterUtils.getKeySyntaxString("object"));
		code.append(".getClass()");

		block.setCodeSyntax(code.toString());
		block.setReturnDatatype(BuiltInDatatypes.getClassDatatype());

		return block;
	}

}
