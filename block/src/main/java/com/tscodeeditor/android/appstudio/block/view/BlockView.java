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

package com.tscodeeditor.android.appstudio.block.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.tscodeeditor.android.appstudio.block.R;
import com.tscodeeditor.android.appstudio.block.editor.EventEditor;
import com.tscodeeditor.android.appstudio.block.model.BlockFieldLayerModel;
import com.tscodeeditor.android.appstudio.block.model.BlockModel;
import com.tscodeeditor.android.appstudio.block.utils.BlockFieldLayerHandler;
import com.tscodeeditor.android.appstudio.block.utils.UnitUtils;

public class BlockView extends LinearLayout {
  private EventEditor editor;
  private Context context;
  private BlockModel blockModel;
  private boolean enableDragDrop;
  private boolean enableEditing;
  private boolean isInsideEditor;
  private float x, y;

  public BlockView(EventEditor editor, Context context, BlockModel blockModel) {
    super(context);
    this.editor = editor;
    this.context = context;
    this.blockModel = blockModel.clone();
    updateBlock();
  }

  public BlockView(EventEditor editor, Context context) {
    super(context);
    this.editor = editor;
    this.context = context;
  }

  public void updateBlock() {
    removeAllViews();
    setOrientation(LinearLayout.VERTICAL);

    if (getBlockModel() == null) return;

    if (getBlockModel().getBlockType() == BlockModel.Type.defaultBlock) {

      /*
       * Add top of blocks as it it define event block.
       */
      if (getBlockModel().isFirstBlock()) {
        LinearLayout firstBlockTop = new LinearLayout(getContext());
        ViewGroup.LayoutParams layoutParams =
            new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        firstBlockTop.setLayoutParams(layoutParams);
        Drawable firstBlockTopDrawable =
            ContextCompat.getDrawable(getContext(), R.drawable.block_first_top);
        firstBlockTopDrawable.setTint(Color.parseColor(getBlockModel().getColor()));
        firstBlockTopDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
        firstBlockTop.setBackground(firstBlockTopDrawable);
        addView(firstBlockTop);
      }

      /*
       * Add all layers to BlockView.
       */
      for (int layerCount = 0;
          layerCount < getBlockModel().getBlockLayerModel().size();
          ++layerCount) {
        LinearLayout layerLayout = new LinearLayout(getContext());
        ViewGroup.LayoutParams layoutParams =
            new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layerLayout.setLayoutParams(layoutParams);
        /*
         * Check if current(LOOP) layer is BlockContentLayerModel
         */
        if (getBlockModel().getBlockLayerModel().get(layerCount) instanceof BlockFieldLayerModel) {
          /*
           * If the block is for defining event then and number of Layout is 1 then:
           * Add LinearLayout with 3 corner cut drawable(Corner Cut: RT:BL:BR).
           */
          if (getBlockModel().getBlockLayerModel().size() == 1 && getBlockModel().isFirstBlock()) {
            setDrawable(
                layerLayout,
                R.drawable.block_default_cut_rt_bl_br,
                Color.parseColor(getBlockModel().getColor()));
          }
          if (getBlockModel().getBlockLayerModel().size() == 1 && !getBlockModel().isFirstBlock()) {
            setDrawable(
                layerLayout,
                R.drawable.block_default_cut_bl_br,
                Color.parseColor(getBlockModel().getColor()));
          }

          if (layerCount == 0) {
            if (!getBlockModel().isFirstBlock()) {
              LinearLayout firstBlockTop = new LinearLayout(getContext());
              ViewGroup.LayoutParams _lp =
                  new ViewGroup.LayoutParams(
                      ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
              firstBlockTop.setLayoutParams(_lp);
              Drawable firstBlockTopDrawable =
                  ContextCompat.getDrawable(getContext(), R.drawable.block_default_top);
              firstBlockTopDrawable.setTint(Color.parseColor(getBlockModel().getColor()));
              firstBlockTopDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
              firstBlockTop.setBackground(firstBlockTopDrawable);
              addView(firstBlockTop, 0);
              layerLayout
                  .getViewTreeObserver()
                  .addOnGlobalLayoutListener(
                      () -> {
                        ViewGroup.LayoutParams lp = firstBlockTop.getLayoutParams();
                        lp.width = layerLayout.getWidth();
                        firstBlockTop.setLayoutParams(lp);
                      });
            }
          }

          // Load block content layer...
          layerLayout.addView(
              BlockFieldLayerHandler.getBlockFieldLayerView(
                  context,
                  (BlockFieldLayerModel) getBlockModel().getBlockLayerModel().get(layerCount),
                  editor,
                  getBlockModel(),
                  this));
        }

        addView(layerLayout);
      }

      if (!getBlockModel().isLastBlock()) {
        LinearLayout blockBottomJoint = new LinearLayout(getContext());
        ViewGroup.LayoutParams layoutParams =
            new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        blockBottomJoint.setLayoutParams(layoutParams);
        Drawable blockBottomJointDrawable =
            ContextCompat.getDrawable(getContext(), R.drawable.block_default_bottom_joint);
        blockBottomJointDrawable.setTint(Color.parseColor(getBlockModel().getColor()));
        blockBottomJointDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
        blockBottomJoint.setBackground(blockBottomJointDrawable);
        addView(blockBottomJoint);
      }
    }

    Runnable dragStartRunnable =
        new Runnable() {
          @Override
          public void run() {
            int[] blockViewCoordinate = new int[2];
            int[] editorViewCoordinate = new int[2];
            BlockView.this.getLocationInWindow(blockViewCoordinate);
            editor.getLocationInWindow(editorViewCoordinate);

            editor.startBlockDrag(
                BlockView.this,
                BlockView.this.getBlockModel(),
                x
                    + blockViewCoordinate[0]
                    - editorViewCoordinate[0]
                    - UnitUtils.dpToPx(getContext(), 60),
                y
                    + blockViewCoordinate[1]
                    - editorViewCoordinate[1]
                    - UnitUtils.dpToPx(getContext(), 80));
          }
        };
    Handler dragHandler = new Handler();

    setOnTouchListener(
        new View.OnTouchListener() {

          @Override
          public boolean onTouch(View view, MotionEvent event) {
            x = event.getX();
            y = event.getY();
            if (getEnableDragDrop()
                && getBlockModel().isDragAllowed()
                && editor.getEvent() != null
                && editor.getEvent().getEnableEdit()) {
              if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (!editor.isDragging) {
                  dragHandler.postDelayed(
                      dragStartRunnable, ViewConfiguration.getLongPressTimeout());
                }
              }
              if (event.getAction() == MotionEvent.ACTION_UP) {
                dragHandler.removeCallbacks(dragStartRunnable);
                if (editor.isDragging) {
                  int[] blockViewCoordinate = new int[2];
                  int[] editorViewCoordinate = new int[2];
                  getLocationInWindow(blockViewCoordinate);
                  editor.getLocationInWindow(editorViewCoordinate);
                  editor.stopDrag(
                      x
                          + blockViewCoordinate[0]
                          - editorViewCoordinate[0]
                          - UnitUtils.dpToPx(getContext(), 60),
                      y
                          + blockViewCoordinate[1]
                          - editorViewCoordinate[1]
                          - UnitUtils.dpToPx(getContext(), 80));
                }
              }
              if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                dragHandler.removeCallbacks(dragStartRunnable);
                if (editor.isDragging) {
                  int[] blockViewCoordinate = new int[2];
                  int[] editorViewCoordinate = new int[2];
                  getLocationInWindow(blockViewCoordinate);
                  editor.getLocationInWindow(editorViewCoordinate);
                  editor.stopDrag(
                      x
                          + blockViewCoordinate[0]
                          - editorViewCoordinate[0]
                          - UnitUtils.dpToPx(getContext(), 60),
                      y
                          + blockViewCoordinate[1]
                          - editorViewCoordinate[1]
                          - UnitUtils.dpToPx(getContext(), 80));
                }
              }

              if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (editor.isDragging) {
                  int[] blockViewCoordinate = new int[2];
                  int[] editorViewCoordinate = new int[2];
                  getLocationInWindow(blockViewCoordinate);
                  editor.getLocationInWindow(editorViewCoordinate);

                  editor.moveFloatingBlockView(
                      x
                          + blockViewCoordinate[0]
                          - editorViewCoordinate[0]
                          - UnitUtils.dpToPx(getContext(), 60),
                      y
                          + blockViewCoordinate[1]
                          - editorViewCoordinate[1]
                          - UnitUtils.dpToPx(getContext(), 80));
                }
              }
            }
            return true;
          }
        });
  }

  public static void setDrawable(View view, int res, int color) {
    Drawable drawable = ContextCompat.getDrawable(view.getContext(), res);
    drawable.setTint(color);
    drawable.setTintMode(PorterDuff.Mode.MULTIPLY);
    view.setBackground(drawable);
  }

  public EventEditor getEditor() {
    return this.editor;
  }

  public void setEditor(EventEditor editor) {
    this.editor = editor;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public BlockModel getBlockModel() {
    return this.blockModel;
  }

  public void setBlockModel(BlockModel blockModel) {
    this.blockModel = blockModel.clone();
  }

  public boolean getEnableDragDrop() {
    return this.enableDragDrop;
  }

  public void setEnableDragDrop(boolean enableDragDrop) {
    this.enableDragDrop = enableDragDrop;
  }

  public boolean getEnableEditing() {
    return this.enableEditing;
  }

  public void setEnableEditing(boolean enableEditing) {
    this.enableEditing = enableEditing;
  }

  public boolean isInsideEditor() {
    return this.isInsideEditor;
  }

  public void setInsideEditor(boolean isInsideEditor) {
    this.isInsideEditor = isInsideEditor;
  }
}
