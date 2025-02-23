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

package com.icst.android.appstudio.block.utils;

import java.util.ArrayList;

import com.icst.android.appstudio.block.editor.EventEditor;
import com.icst.android.appstudio.block.model.BlockFieldLayerModel;
import com.icst.android.appstudio.block.model.BlockFieldModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.BlockValueFieldModel;
import com.icst.android.appstudio.block.view.BlockFieldExtensionViewOnly;
import com.icst.android.appstudio.block.view.BlockFieldInputOnlyView;
import com.icst.android.appstudio.block.view.BlockFieldView;
import com.icst.android.appstudio.block.view.BlockVariableFieldView;
import com.icst.android.appstudio.block.view.BlockView;
import com.icst.android.appstudio.block.view.BooleanView;
import com.icst.android.appstudio.block.view.NumberView;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class BlockFieldLayerHandler {
	public static ViewGroup getBlockFieldLayerView(
			Context context,
			BlockFieldLayerModel blockFieldLayerModel,
			EventEditor editor,
			BlockModel blockModel,
			BlockView blockView,
			ArrayList<LinearLayout> droppables,
			boolean isDarkMode) {
		LinearLayout root = new LinearLayout(context);

		for (int position = 0; position < blockFieldLayerModel.getBlockFields().size(); ++position) {
			BlockFieldModel content = blockFieldLayerModel.getBlockFields().get(position);

			if (content instanceof BlockValueFieldModel) {
				BlockValueFieldModel blockValueFieldModel = (BlockValueFieldModel) content;
				if (blockValueFieldModel.getFieldType() == BlockValueFieldModel.FieldType.FIELD_INPUT_ONLY) {
					BlockFieldInputOnlyView inputField = new BlockFieldInputOnlyView(context, blockValueFieldModel,
							blockView, editor);
					root.addView(inputField);
				} else if (blockValueFieldModel
						.getFieldType() == BlockValueFieldModel.FieldType.FIELD_EXTENSION_VIEW_ONLY) {
					BlockFieldExtensionViewOnly extensionFieldViewOnly = new BlockFieldExtensionViewOnly(context,
							blockView, blockValueFieldModel, editor);
					root.addView(extensionFieldViewOnly);
				} else if (blockValueFieldModel.getFieldType() == BlockValueFieldModel.FieldType.FIELD_BOOLEAN) {
					BooleanView booleanField = new BooleanView(
							context, blockValueFieldModel, blockView, droppables, editor, isDarkMode);
					root.addView(booleanField);
				} else if (blockValueFieldModel.getFieldType() == BlockValueFieldModel.FieldType.FIELD_NUMBER) {
					NumberView numberView = new NumberView(
							context, blockValueFieldModel, blockView, droppables, editor, isDarkMode);
					root.addView(numberView);
				} else if (blockValueFieldModel.getFieldType() == BlockValueFieldModel.FieldType.FIELD_TYPE_NOT_SET) {
					if (blockValueFieldModel instanceof BlockModel block) {
						BlockView mBlockView = new BlockView(editor, context, block, isDarkMode);
						mBlockView.setInsideEditor(false);
						mBlockView.setEnableDragDrop(true);
						mBlockView.setEnableEditing(true);
						root.addView(mBlockView);
					} else {
						BlockVariableFieldView variableView = new BlockVariableFieldView(
								context, blockValueFieldModel, blockView, droppables, editor, isDarkMode);
						root.addView(variableView);
					}
				}
			} else if (content instanceof BlockFieldModel) {
				/*
				 * BlockFieldModel just contains text to display.
				 * Using BlockContentView for displaying text.
				 */
				BlockFieldView textField = new BlockFieldView(context, content, blockModel, isDarkMode);
				textField.setPadding(UnitUtils.dpToPx(context, 4), 0, UnitUtils.dpToPx(context, 4), 0);
				root.addView(textField);
			}
		}
		return root;
	}
}
