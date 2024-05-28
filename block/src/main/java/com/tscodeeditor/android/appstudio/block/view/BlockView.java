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
import com.tscodeeditor.android.appstudio.block.model.BlockHolderLayer;
import com.tscodeeditor.android.appstudio.block.model.BlockModel;
import com.tscodeeditor.android.appstudio.block.tag.BlockDroppableTag;
import com.tscodeeditor.android.appstudio.block.utils.BlockMarginConstants;
import com.tscodeeditor.android.appstudio.block.utils.ColorPalleteUtils;
import com.tscodeeditor.android.appstudio.block.utils.LayerBuilder;
import com.tscodeeditor.android.appstudio.block.utils.TargetUtils;
import com.tscodeeditor.android.appstudio.block.utils.UnitUtils;
import java.util.ArrayList;

public class BlockView extends LinearLayout {
  private EventEditor editor;
  private Context context;
  private BlockModel blockModel;
  private ArrayList<LinearLayout> droppables;
  private LinearLayout blockTop;
  private boolean enableDragDrop;
  private boolean enableEditing;
  private boolean isInsideEditor;
  private boolean isRootBlock;
  private float x, y;

  public BlockView(EventEditor editor, Context context, BlockModel blockModel) {
    super(context);
    this.editor = editor;
    this.context = context;
    this.blockModel = blockModel;
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
    droppables = new ArrayList<LinearLayout>();

    if (getBlockModel() == null) return;

    if (getBlockModel().getBlockType() == BlockModel.Type.defaultBlock) {

      addBlockTopView();

      for (int layerCount = 0;
          layerCount < getBlockModel().getBlockLayerModel().size();
          ++layerCount) {
        LinearLayout layerLayout = new LinearLayout(getContext());
        layerLayout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams layoutParams =
            new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layerLayout.setLayoutParams(layoutParams);

        if (getBlockModel().getBlockLayerModel().get(layerCount) instanceof BlockFieldLayerModel) {

          LayerBuilder.buildBlockFieldLayer(
              this,
              ((BlockFieldLayerModel) getBlockModel().getBlockLayerModel().get(layerCount)),
              getBlockModel(),
              layerLayout,
              layerCount,
              editor.isDarkMode());

          if (blockTop != null) {
            layerLayout
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(
                    () -> {
                      ViewGroup.LayoutParams lp = blockTop.getLayoutParams();
                      lp.width = layerLayout.getWidth();
                      blockTop.setLayoutParams(lp);
                    });
          }

        } else if (getBlockModel().getBlockLayerModel().get(layerCount)
            instanceof BlockHolderLayer) {
          LayerBuilder.buildBlockHolderLayer(
              this,
              ((BlockHolderLayer) getBlockModel().getBlockLayerModel().get(layerCount)),
              getBlockModel(),
              layerLayout,
              droppables,
              layerCount,
              editor.isDarkMode());
        }
        addView(layerLayout);
      }

      addBlockBottomView();
    }

    setDragListener();
  }

  public void addBlockTopView() {
    if (!(getBlockModel().getBlockType() == BlockModel.Type.defaultBlock)) return;
    if (getBlockModel().isFirstBlock()) {
      LinearLayout firstBlockTop = new LinearLayout(getContext());
      ViewGroup.LayoutParams layoutParams =
          new ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      firstBlockTop.setLayoutParams(layoutParams);
      Drawable firstBlockTopDrawable =
          ContextCompat.getDrawable(getContext(), R.drawable.block_first_top);
      firstBlockTopDrawable.setTint(
          ColorPalleteUtils.transformColor(getBlockModel().getColor(), editor.isDarkMode()));
      firstBlockTopDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
      firstBlockTop.setBackground(firstBlockTopDrawable);
      addView(firstBlockTop);
    } else {
      blockTop = new LinearLayout(getContext());
      ViewGroup.LayoutParams _lp =
          new ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      blockTop.setLayoutParams(_lp);
      Drawable blockTopDrawable =
          ContextCompat.getDrawable(getContext(), R.drawable.block_default_top);
      blockTopDrawable.setTint(
          ColorPalleteUtils.transformColor(getBlockModel().getColor(), editor.isDarkMode()));
      blockTopDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
      blockTop.setBackground(blockTopDrawable);
      addView(blockTop);
    }
  }

  public void addBlockBottomView() {
    if (!(getBlockModel().getBlockType() == BlockModel.Type.defaultBlock)) return;
    if (!getBlockModel().isLastBlock()) {
      LinearLayout blockBottomJoint = new LinearLayout(getContext());
      ViewGroup.LayoutParams layoutParams =
          new ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      blockBottomJoint.setLayoutParams(layoutParams);
      Drawable blockBottomJointDrawable =
          ContextCompat.getDrawable(getContext(), R.drawable.block_default_bottom_joint);
      blockBottomJointDrawable.setTint(
          ColorPalleteUtils.transformColor(getBlockModel().getColor(), editor.isDarkMode()));
      blockBottomJointDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
      blockBottomJoint.setBackground(blockBottomJointDrawable);
      addView(blockBottomJoint);
    }
  }

  public void setDragListener() {
    Runnable dragStartRunnable =
        new Runnable() {
          @Override
          public void run() {
            editor.startBlockDrag(
                BlockView.this,
                BlockView.this.getBlockModel(),
                x
                    + TargetUtils.getRelativePosition(BlockView.this, editor)[0]
                    - UnitUtils.dpToPx(getContext(), 60),
                y
                    + TargetUtils.getRelativePosition(BlockView.this, editor)[1]
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
                  editor.stopDrag(
                      x
                          + TargetUtils.getRelativePosition(BlockView.this, editor)[0]
                          - UnitUtils.dpToPx(getContext(), 60),
                      y
                          + TargetUtils.getRelativePosition(BlockView.this, editor)[1]
                          - UnitUtils.dpToPx(getContext(), 80));
                }
              }
              if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                dragHandler.removeCallbacks(dragStartRunnable);
                if (editor.isDragging) {
                  editor.stopDrag(
                      x
                          + TargetUtils.getRelativePosition(BlockView.this, editor)[0]
                          - UnitUtils.dpToPx(getContext(), 60),
                      y
                          + TargetUtils.getRelativePosition(BlockView.this, editor)[1]
                          - UnitUtils.dpToPx(getContext(), 80));
                }
              }

              if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (editor.isDragging) {
                  editor.moveFloatingBlockView(
                      x
                          + TargetUtils.getRelativePosition(BlockView.this, editor)[0]
                          - UnitUtils.dpToPx(getContext(), 60),
                      y
                          + TargetUtils.getRelativePosition(BlockView.this, editor)[1]
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

  public boolean drop(float x, float y, BlockModel toDrop) {

    for (int i = 0; i < droppables.size(); ++i) {
      if (toDrop.getBlockType() == BlockModel.Type.defaultBlock) {
        if (droppables.get(i).getTag() != null) {
          if (droppables.get(i).getTag() instanceof BlockDroppableTag) {

            BlockDroppableTag tag = (BlockDroppableTag) droppables.get(i).getTag();

            if (tag.getBlockDroppableType() == BlockDroppableTag.DEFAULT_BLOCK_DROPPER) {
              if (toDrop.getBlockType() == BlockModel.Type.defaultBlock) {
                if (TargetUtils.isDragInsideTargetView(droppables.get(i), editor, x, y)) {

                  int index = 0;
                  for (int ind = 0; ind < droppables.get(i).getChildCount(); ind++) {
                    View child = droppables.get(i).getChildAt(ind);
                    int[] relativePos =
                        TargetUtils.getRelativePosition(droppables.get(i).getChildAt(ind), editor);
                    if (y > relativePos[1] + (child.getHeight() / 2)) {
                      index = ind + 1;
                    } else {
                      break;
                    }
                  }
                  dropBlockView(index, x, y, droppables.get(i), tag, toDrop);
                  return true;
                }
              }
            }
          }
        }
      }
    }

    return false;
  }

  public void dropBlockView(
      int index,
      float x,
      float y,
      ViewGroup target,
      BlockDroppableTag targetTag,
      BlockModel blockModel) {
    boolean isDropConsumed = false;
    if (target.getChildCount() > index) {
      if (target.getChildAt(index) instanceof BlockView) {
        BlockView blockView = (BlockView) target.getChildAt(index);
        if (blockView.drop(x, y, blockModel)) {
          isDropConsumed = true;
        }
      }
    }

    if (!isDropConsumed) {
      if (target.getChildCount() > (index - 1)) {
        if (target.getChildAt((index - 1)) instanceof BlockView) {
          BlockView blockView = (BlockView) target.getChildAt((index - 1));
          if (blockView.drop(x, y, blockModel)) {
            isDropConsumed = true;
          }
        }
      }
    }

    if (!isDropConsumed) {
      if (target.getChildCount() > (index + 1)) {
        if (target.getChildAt((index + 1)) instanceof BlockView) {
          BlockView blockView = (BlockView) target.getChildAt((index + 1));
          if (blockView.drop(x, y, blockModel)) {
            isDropConsumed = true;
          }
        }
      }
    }

    if (!isDropConsumed) {
      BlockView block = new BlockView(editor, getContext(), blockModel.clone());

      block.setEnableDragDrop(true);
      block.setEnableEditing(true);
      block.setInsideEditor(true);
      if (targetTag.getDropProperty(BlockHolderLayer.class).getBlocks() == null) {
        ArrayList<BlockModel> blocks = new ArrayList<BlockModel>();
        targetTag.getDropProperty(BlockHolderLayer.class).setBlocks(blocks);
      }
      target.addView(block, index);
      targetTag
          .getDropProperty(BlockHolderLayer.class)
          .getBlocks()
          .add(index, block.getBlockModel());
      if (editor.draggingBlock.isInsideEditor()) {
        if (((ViewGroup) editor.draggingBlock.getParent()).getTag() != null) {
          if (((ViewGroup) editor.draggingBlock.getParent()).getTag()
              instanceof BlockDroppableTag) {
            if (((BlockDroppableTag) ((ViewGroup) editor.draggingBlock.getParent()).getTag())
                    .getBlockDroppableType()
                == BlockDroppableTag.DEFAULT_BLOCK_DROPPER) {
              ((BlockDroppableTag) ((ViewGroup) editor.draggingBlock.getParent()).getTag())
                  .getDropProperty(BlockHolderLayer.class)
                  .getBlocks()
                  .remove(
                      ((ViewGroup) editor.draggingBlock.getParent())
                          .indexOfChild(editor.draggingBlock));
            }
          }
        }
      }
      if (editor.draggingBlock.isInsideEditor()) {
        ((ViewGroup) editor.draggingBlock.getParent()).removeView(editor.draggingBlock);
      }
    }
  }

  public boolean preview(float x, float y, BlockModel toDrop) {
    for (int i = 0; i < droppables.size(); ++i) {
      if (toDrop.getBlockType() == BlockModel.Type.defaultBlock) {
        if (droppables.get(i).getTag() != null) {
          if (droppables.get(i).getTag() instanceof BlockDroppableTag) {

            BlockDroppableTag tag = (BlockDroppableTag) droppables.get(i).getTag();

            if (tag.getBlockDroppableType() == BlockDroppableTag.DEFAULT_BLOCK_DROPPER) {
              if (toDrop.getBlockType() == BlockModel.Type.defaultBlock) {
                if (TargetUtils.isDragInsideTargetView(droppables.get(i), editor, x, y)) {
                  editor.blockPreview.removePreview();
                  editor.blockPreview.setBlock(toDrop);

                  int index = 0;
                  for (int ind = 0; ind < droppables.get(i).getChildCount(); ind++) {
                    View child = droppables.get(i).getChildAt(ind);
                    int[] relativePos =
                        TargetUtils.getRelativePosition(droppables.get(i).getChildAt(ind), editor);
                    if (y > relativePos[1] + (child.getHeight() / 2)) {
                      index = ind + 1;
                    } else {
                      break;
                    }
                  }

                  setBlockPreview(index, x, y, droppables.get(i), toDrop);
                  return true;
                }
              }
            }
          }
        }
      }
    }
    return false;
  }

  public void setBlockPreview(
      int index, float x, float y, ViewGroup target, BlockModel blockModel) {
    boolean isPreviewConsumed = false;
    if (target.getChildCount() > index) {
      if (target.getChildAt(index) instanceof BlockView) {
        BlockView blockView = (BlockView) target.getChildAt(index);
        if (blockView.preview(x, y, blockModel)) {
          isPreviewConsumed = true;
        }
      }
    }

    if (!isPreviewConsumed) {
      if (target.getChildCount() > (index - 1)) {
        if (target.getChildAt((index - 1)) instanceof BlockView) {
          BlockView blockView = (BlockView) target.getChildAt((index - 1));
          if (blockView.preview(x, y, blockModel)) {
            isPreviewConsumed = true;
          }
        }
      }
    }

    if (!isPreviewConsumed) {
      if (target.getChildCount() > (index + 1)) {
        if (target.getChildAt((index + 1)) instanceof BlockView) {
          BlockView blockView = (BlockView) target.getChildAt((index + 1));
          if (blockView.preview(x, y, blockModel)) {
            isPreviewConsumed = true;
          }
        }
      }
    }

    if (!isPreviewConsumed) {
      target.addView(editor.blockPreview, index);
    }
  }

  @Override
  public void setVisibility(int arg) {
    super.setVisibility(arg);
    if (getParent() == null) return;
    if (!(getParent() instanceof LinearLayout)) return;
    LinearLayout parent = ((LinearLayout) getParent());
    if (parent.indexOfChild(this) != 0) return;
    if (parent.getTag() == null) return;
    if (!(parent.getTag() instanceof BlockDroppableTag)) return;
    BlockDroppableTag tag = ((BlockDroppableTag) parent.getTag());
    if (tag.getBlockDroppableType() != BlockDroppableTag.DEFAULT_BLOCK_DROPPER) return;
    if (parent.getChildAt(1) == null) return;
    if (arg == View.GONE) {
      parent
          .getChildAt(1)
          .setLayoutParams(
              new LinearLayout.LayoutParams(
                  LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    } else if (arg == View.VISIBLE) {
      LinearLayout.LayoutParams blockParams =
          new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

      blockParams.setMargins(
          0, UnitUtils.dpToPx(getContext(), BlockMarginConstants.regularBlockMargin), 0, 0);
      parent.getChildAt(1).setLayoutParams(blockParams);
    }
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

  public boolean isRootBlock() {
    return this.isRootBlock;
  }

  public void setRootBlock(boolean isRootBlock) {
    this.isRootBlock = isRootBlock;
  }
}
