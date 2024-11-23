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

package com.icst.android.appstudio.block.view;

import java.util.ArrayList;

import com.icst.android.appstudio.block.R;
import com.icst.android.appstudio.block.editor.EventEditor;
import com.icst.android.appstudio.block.model.BlockValueFieldModel;
import com.icst.android.appstudio.block.tag.BlockDroppableTag;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

public class BooleanView extends LinearLayout {
	private BlockView booleanBlock;
	private BlockValueFieldModel blockFieldModel;
	private EventEditor editor;

	public BooleanView(
			Context context,
			BlockValueFieldModel blockFieldModel,
			BlockView blockView,
			ArrayList<LinearLayout> droppables,
			EventEditor editor,
			boolean darkMode) {
		super(context);
		this.blockFieldModel = blockFieldModel;
		this.editor = editor;
		if (blockFieldModel.getBlockModel() == null) {
			Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.block_boolean_backdrop);
			drawable.setTint(Color.parseColor("#000000"));
			drawable.setTintMode(PorterDuff.Mode.MULTIPLY);
			setBackground(drawable);
			setAlpha(0.3F);
		} else {
			setBackground(null);
			booleanBlock = new BlockView(editor, context, blockFieldModel.getBlockModel(), darkMode);
			booleanBlock.setInsideEditor(true);
			booleanBlock.setEnableEditing(true);
			booleanBlock.setEnableDragDrop(true);
			addView(booleanBlock);
			setAlpha(1F);
		}

		if (blockFieldModel.isEnabledEdit()) {
			BlockDroppableTag tag = new BlockDroppableTag();
			tag.setDropProperty(blockFieldModel);
			tag.setBlockDroppableType(BlockDroppableTag.BLOCK_BOOLEAN_DROPPER);
			setTag(tag);
			droppables.add(this);
		}
	}

	@Override
	public void addView(View view) {
		if (view instanceof BlockView) {
			if (getChildCount() == 1) {
				if (getChildAt(0) instanceof BlockView oldBlockView) {
					if (editor != null) {
						FrameLayout.LayoutParams blockParams = new FrameLayout.LayoutParams(
								FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

						blockParams.setMargins(
								editor.binding.canva.getScrollX() - editor.binding.codeEditor.getPaddingStart(),
								editor.binding.canva.getScrollY() - editor.binding.codeEditor.getPaddingTop(),
								0,
								0);

						if (oldBlockView.getParent() != null) {
							((ViewGroup) oldBlockView.getParent()).removeView(oldBlockView);
						}

						editor.binding.canva.addView(oldBlockView);
						oldBlockView.setLayoutParams(blockParams);
					}
				}
			}
			super.removeAllViews();
		}
		if (view instanceof BlockPreview) {
			if (getChildCount() == 1) {
				getChildAt(0).setVisibility(View.GONE);
			}
		}
		super.addView(view);
		ensureFieldBackground();
		if (view instanceof BlockView) {
			setBooleanBlock((BlockView) view);
		}
	}

	@Override
	public void removeView(View view) {
		super.removeView(view);
		if (view instanceof BlockPreview) {
			if (getChildCount() == 1) {
				getChildAt(0).setVisibility(View.VISIBLE);
			}
			if (editor != null) {
				if (editor.draggingBlock != null) {
					if (editor.draggingBlock.isInsideEditor()) {
						editor.draggingBlock.setVisibility(View.GONE);
					}
				}
			}
		}
		ensureFieldBackground();
		if (view instanceof BlockView) {
			setBooleanBlock(null);
		}
	}

	public void ensureFieldBackground() {
		boolean hasVisibleChild = false;
		for (int i = 0; i < getChildCount(); ++i) {
			if (getChildAt(i).getVisibility() == View.VISIBLE) {
				hasVisibleChild = true;
			}
		}
		if (hasVisibleChild) {
			setBackground(null);
			setAlpha(1F);
		} else {
			Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.block_boolean_backdrop);
			drawable.setTint(Color.parseColor("#000000"));
			drawable.setTintMode(PorterDuff.Mode.MULTIPLY);
			setBackground(drawable);
			setAlpha(0.3F);
		}
	}

	public BlockView getBooleanBlock() {
		return this.booleanBlock;
	}

	public void setBooleanBlock(BlockView booleanBlock) {
		this.booleanBlock = booleanBlock;
		if (booleanBlock != null) {
			blockFieldModel.setBlockModel(booleanBlock.getBlockModel());
		} else {
			blockFieldModel.setBlockModel(null);
		}
	}
}
