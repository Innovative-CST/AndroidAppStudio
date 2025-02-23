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

public class BlockVariableFieldView extends LinearLayout {
	private BlockView mBlock;
	private BlockValueFieldModel blockFieldModel;
	private EventEditor editor;

	public BlockVariableFieldView(
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
			Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.block_variable_field);
			drawable.setTint(Color.parseColor("#ffffff"));
			drawable.setTintMode(PorterDuff.Mode.MULTIPLY);
			setBackground(drawable);
		} else {
			setBackground(null);
			mBlock = new BlockView(editor, context, blockFieldModel.getBlockModel(), darkMode);
			mBlock.setInsideEditor(true);
			mBlock.setEnableEditing(true);
			mBlock.setEnableDragDrop(true);
			addView(mBlock);
		}

		if (blockFieldModel.isEnabledEdit()) {
			BlockDroppableTag tag = new BlockDroppableTag();
			tag.setDropProperty(blockFieldModel);
			tag.setBlockDroppableType(BlockDroppableTag.BLOCK_VARIABLE_DROPPER);
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
			setBlock((BlockView) view);
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
		if (view instanceof BlockView) {
			setBlock(null);
		}
		ensureFieldBackground();
	}

	public void ensureFieldBackground() {
		boolean hasVisibleChild = false;
		for (int i = 0; i < getChildCount(); ++i) {
			if (getChildAt(i).getVisibility() == View.VISIBLE) {
				if (getChildAt(i) instanceof BlockView) {
					hasVisibleChild = true;
				}
				if (getChildAt(i) instanceof BlockPreview) {
					hasVisibleChild = true;
				}
			}
		}
		if (hasVisibleChild) {
			setBackground(null);
			setPadding(0, 0, 0, 0);
		} else {
			BlockView.setDrawable(this, R.drawable.block_variable_field, Color.parseColor("#ffffff"));
		}
	}

	public BlockView getBlock() {
		return this.mBlock;
	}

	public void setBlock(BlockView mBlock) {
		this.mBlock = mBlock;
		if (mBlock != null) {
			blockFieldModel.setBlockModel(mBlock.getBlockModel());
		} else {
			blockFieldModel.setBlockModel(null);
		}
	}
}
