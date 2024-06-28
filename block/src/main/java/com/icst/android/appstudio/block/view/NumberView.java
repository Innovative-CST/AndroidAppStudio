/*
 * This file is part of Android AppStudio [https://github.com/TS-Code-Editor/AndroidAppStudio].
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

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.icst.android.appstudio.block.R;
import com.icst.android.appstudio.block.dialog.NumericalValueEditorDialog;
import com.icst.android.appstudio.block.editor.EventEditor;
import com.icst.android.appstudio.block.model.BlockValueFieldModel;
import com.icst.android.appstudio.block.tag.BlockDroppableTag;
import java.util.ArrayList;

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
            if (!blockView.isInsideEditor()) return;
            if (!blockFieldModel.isEnabledEdit()) return;
            if (editor == null) return;
            if (editor.getEvent() != null && !editor.getEvent().getEnableEdit()) return;
            if (editor.getEvent() != null
                && !editor.getEvent().getEnableRootBlocksValueEditing()
                && blockView.isRootBlock()) return;

            NumericalValueEditorDialog dialog =
                new NumericalValueEditorDialog(editor, blockFieldModel, NumberView.this);
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
            FrameLayout.LayoutParams blockParams =
                new FrameLayout.LayoutParams(
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
            if (!blockView.isInsideEditor()) return;
            if (!blockFieldModel.isEnabledEdit()) return;
            if (editor == null) return;
            if (editor.getEvent() != null && !editor.getEvent().getEnableEdit()) return;
            if (editor.getEvent() != null
                && !editor.getEvent().getEnableRootBlocksValueEditing()
                && blockView.isRootBlock()) return;

            NumericalValueEditorDialog dialog =
                new NumericalValueEditorDialog(editor, blockFieldModel, NumberView.this);
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
