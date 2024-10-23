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

package com.icst.android.appstudio.block.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import java.util.ArrayList;

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
