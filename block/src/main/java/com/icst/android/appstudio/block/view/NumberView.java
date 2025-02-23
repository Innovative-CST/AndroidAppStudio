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
import com.icst.android.appstudio.block.dialog.NumericalValueEditorDialog;
import com.icst.android.appstudio.block.editor.EventEditor;
import com.icst.android.appstudio.block.model.BlockValueFieldModel;
import com.icst.android.appstudio.block.tag.BlockDroppableTag;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NumberView extends LinearLayout {
	private BlockView numberBlock;
	private TextView text;
	private BlockValueFieldModel blockFieldModel;
	private EventEditor editor;
	private BlockView blockView;

	public NumberView(
			Context context,
			BlockValueFieldModel blockFieldModel,
			BlockView blockView,
			ArrayList<LinearLayout> droppables,
			EventEditor editor,
			boolean darkMode) {
		super(context);
		this.blockFieldModel = blockFieldModel;
		this.editor = editor;
		this.blockView = blockView;
		setGravity(Gravity.CENTER);
		if (blockFieldModel.getBlockModel() == null) {
			text = new TextView(context);
			text.setText(blockFieldModel.getValue() != null ? blockFieldModel.getValue() : "");
			text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
			text.setSingleLine(true);
			text.setTextColor(Color.BLACK);
			addView(text);
			BlockView.setDrawable(this, R.drawable.block_number, Color.parseColor("#ffffff"));
			setOnClickListener(
					v -> {
						if (!blockView.isInsideEditor())
							return;
						if (!blockFieldModel.isEnabledEdit())
							return;
						if (editor == null)
							return;
						if (editor.getEvent() != null && !editor.getEvent().getEnableEdit())
							return;
						if (editor.getEvent() != null
								&& !editor.getEvent().getEnableRootBlocksValueEditing()
								&& blockView.isRootBlock())
							return;

						NumericalValueEditorDialog dialog = new NumericalValueEditorDialog(editor, blockFieldModel,
								NumberView.this);
					});
		} else {
			setBackground(null);
			numberBlock = new BlockView(editor, context, blockFieldModel.getBlockModel(), darkMode);
			numberBlock.setInsideEditor(true);
			numberBlock.setEnableEditing(true);
			numberBlock.setEnableDragDrop(true);
			addView(numberBlock);
		}

		if (blockFieldModel.isEnabledEdit()) {
			BlockDroppableTag tag = new BlockDroppableTag();
			tag.setDropProperty(blockFieldModel);
			tag.setBlockDroppableType(BlockDroppableTag.BLOCK_NUMBER_DROPPER);
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
			setNumberBlock((BlockView) view);
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
			setNumberBlock(null);
		}
	}

	public void ensureFieldBackground() {
		boolean hasVisibleChild = false;
		for (int i = 0; i < getChildCount(); ++i) {
			if (getChildAt(i).getVisibility() == View.VISIBLE) {
				if (!(getChildAt(i) instanceof TextView)) {
					hasVisibleChild = true;
				}
			}
		}
		if (hasVisibleChild) {
			setBackground(null);
		} else {
			BlockView.setDrawable(this, R.drawable.block_number, Color.parseColor("#ffffff"));
		}
	}

	public BlockView getNumberBlock() {
		return this.numberBlock;
	}

	public void setNumberBlock(BlockView numberBlock) {
		this.numberBlock = numberBlock;
		if (numberBlock != null) {
			blockFieldModel.setBlockModel(numberBlock.getBlockModel());
			setOnClickListener(null);
		} else {
			setOnClickListener(
					v -> {
						if (!blockView.isInsideEditor())
							return;
						if (!blockFieldModel.isEnabledEdit())
							return;
						if (editor == null)
							return;
						if (editor.getEvent() != null && !editor.getEvent().getEnableEdit())
							return;
						if (editor.getEvent() != null
								&& !editor.getEvent().getEnableRootBlocksValueEditing()
								&& blockView.isRootBlock())
							return;

						NumericalValueEditorDialog dialog = new NumericalValueEditorDialog(editor, blockFieldModel,
								NumberView.this);
					});
			blockFieldModel.setBlockModel(null);
		}
	}

	public void setFieldValue(String value) {
		blockFieldModel.setValue(value);
		if (text == null) {
			text = new TextView(getContext());
		}
		if (text.getParent() == null) {
			text.setText(blockFieldModel.getValue() != null ? blockFieldModel.getValue() : "");
			text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
			text.setSingleLine(true);
			text.setTextColor(Color.BLACK);
			addView(text);
		} else {
			text.setText(blockFieldModel.getValue() != null ? blockFieldModel.getValue() : "");
		}
		BlockView.setDrawable(this, R.drawable.block_number, Color.parseColor("#ffffff"));
	}
}
