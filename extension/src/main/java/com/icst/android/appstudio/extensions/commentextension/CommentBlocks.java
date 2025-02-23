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

package com.icst.android.appstudio.extensions.commentextension;

import java.util.ArrayList;

import com.icst.android.appstudio.block.model.BlockFieldLayerModel;
import com.icst.android.appstudio.block.model.BlockFieldModel;
import com.icst.android.appstudio.block.model.BlockLayerModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.BlockValueFieldModel;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;

public class CommentBlocks {

	public static ArrayList<BlockModel> getBlocks() {
		ArrayList<BlockModel> blocks = new ArrayList<BlockModel>();
		blocks.add(getCommentBlock());
		return blocks;
	}

	private static BlockModel getCommentBlock() {
		BlockModel block = new BlockModel();
		block.setColor("#29CC57");
		block.setBlockType(BlockModel.Type.defaultBlock);
		block.setDragAllowed(true);
		block.setHolderName("Comment");
		block.setReplacerKey("comment");

		ArrayList<BlockLayerModel> layers = new ArrayList<BlockLayerModel>();
		BlockFieldLayerModel layer1 = new BlockFieldLayerModel();

		ArrayList<BlockFieldModel> fieldsLayer1 = new ArrayList<BlockFieldModel>();
		BlockFieldModel ifText = new BlockFieldModel();

		ifText.setValue("comment");

		fieldsLayer1.add(ifText);

		BlockValueFieldModel commentField = new BlockValueFieldModel();
		commentField.setEnableEdit(true);
		commentField.setFieldType(BlockValueFieldModel.FieldType.FIELD_INPUT_ONLY);
		commentField.setReplacer("comment");

		fieldsLayer1.add(commentField);

		layer1.setBlockFields(fieldsLayer1);

		layers.add(layer1);

		block.setBlockLayerModel(layers);

		StringBuilder rawCode = new StringBuilder();
		rawCode.append("// ");
		rawCode.append(RawCodeReplacer.getReplacer(block.getReplacerKey(), "comment"));

		block.setRawCode(rawCode.toString());

		return block;
	}
}
