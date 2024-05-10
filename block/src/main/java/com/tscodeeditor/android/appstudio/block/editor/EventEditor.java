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

package com.tscodeeditor.android.appstudio.block.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.tscodeeditor.android.appstudio.block.adapter.BlocksHolderAdapter;
import com.tscodeeditor.android.appstudio.block.databinding.EventEditorLayoutBinding;
import com.tscodeeditor.android.appstudio.block.model.BlockHolderLayer;
import com.tscodeeditor.android.appstudio.block.model.BlockHolderModel;
import com.tscodeeditor.android.appstudio.block.model.BlockModel;
import com.tscodeeditor.android.appstudio.block.model.Event;
import com.tscodeeditor.android.appstudio.block.tag.BlockDroppableTag;
import com.tscodeeditor.android.appstudio.block.utils.BlockMarginConstants;
import com.tscodeeditor.android.appstudio.block.utils.TargetUtils;
import com.tscodeeditor.android.appstudio.block.utils.UnitUtils;
import com.tscodeeditor.android.appstudio.block.view.BlockDragView;
import com.tscodeeditor.android.appstudio.block.view.BlockPreview;
import com.tscodeeditor.android.appstudio.block.view.BlockView;
import java.util.ArrayList;
import java.util.HashMap;

public class EventEditor extends RelativeLayout {

  public EventEditorLayoutBinding binding;
  public BlockDragView blockFloatingView;
  public BlockView draggingBlock;
  public BlockPreview blockPreview;
  public HashMap<String, Object> variables;

  public boolean isDragging = false;
  private boolean isBlockPallateVisible = false;

  // Contants for showing the section easily
  public static final int LOADING_SECTION = 0;
  public static final int INFO_SECTION = 1;
  public static final int EDITOR_SECTION = 2;
  public static final int VALUE_EDITOR_SECTION = 3;

  public EventEditor(final Context context, final AttributeSet set) {
    super(context, set);

    binding = EventEditorLayoutBinding.inflate(LayoutInflater.from(context));
    blockFloatingView = new BlockDragView(context, null);
    blockPreview = new BlockPreview(context);
    binding.getRoot().setClipChildren(true);
    addView(binding.getRoot());
    setMatchParent(binding.getRoot());
    switchSection(EDITOR_SECTION);
    binding.fab.setOnClickListener(
        v -> {
          isBlockPallateVisible = !isBlockPallateVisible;
          showBlocksPallete(isBlockPallateVisible);
        });
    invalidate();
  }

  private void setMatchParent(View view) {
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    view.setLayoutParams(layoutParams);
  }

  /*
   * Method for switching the section quickly.
   */
  public void switchSection(int section) {
    binding.loading.setVisibility(section == LOADING_SECTION ? View.VISIBLE : View.GONE);
    binding.info.setVisibility(section == INFO_SECTION ? View.VISIBLE : View.GONE);
    binding.editorSection.setVisibility(section == EDITOR_SECTION ? View.VISIBLE : View.GONE);
    binding.valueEditorSection.setVisibility(
        section == VALUE_EDITOR_SECTION ? View.VISIBLE : View.GONE);
    if (section == VALUE_EDITOR_SECTION) {
      showBlocksPallete(false);
      binding.fab.setVisibility(View.GONE);
    } else {
      showBlocksPallete(isBlockPallateVisible);
      binding.fab.setVisibility(View.VISIBLE);
    }
  }

  public void showBlocksPallete(boolean show) {
    binding.blockArea.setVisibility(show ? View.VISIBLE : View.GONE);
  }

  public void initEditor(Event event, HashMap<String, Object> variables) {
    setVariables(variables);
    binding.canva.initEditor(event, this);
  }

  public void setHolder(ArrayList<BlockHolderModel> holderList) {
    binding.blocksHolderList.setAdapter(new BlocksHolderAdapter(holderList, this));
    binding.blocksHolderList.setLayoutManager(new LinearLayoutManager(getContext()));
  }

  public void startBlockDrag(BlockView draggingBlock, BlockModel block, float x, float y) {
    if (draggingBlock.isInsideEditor()) {
      draggingBlock.setVisibility(View.GONE);
    }
    int notAllowedIconWidth = 0;
    if (blockFloatingView.notAllowed != null) {
      if (blockFloatingView.notAllowed.getParent() != null) {
        notAllowedIconWidth = blockFloatingView.notAllowed.getWidth();
      }
    }

    binding.canva.setDisableScrollForcefully(true);
    binding.blockList.requestDisallowInterceptTouchEvent(true);
    isDragging = true;
    this.draggingBlock = draggingBlock;
    blockFloatingView.setBlock(block);
    addView(blockFloatingView);
    RelativeLayout.LayoutParams blockFloatingViewParam =
        new RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    blockFloatingView.setLayoutParams(blockFloatingViewParam);
    blockFloatingView.setX(x - notAllowedIconWidth);
    blockFloatingView.setY(y);
    blockFloatingView.setAllowed(isBlockFloatingViewInsideCanva(x, y));
  }

  public void stopDrag(float x, float y) {
    isDragging = false;
    binding.canva.setDisableScrollForcefully(false);
    binding.blockList.requestDisallowInterceptTouchEvent(false);
    blockPreview.removePreview();
    removeView(blockFloatingView);
    drop(x, y);
    draggingBlock = null;
  }

  public void drop(float x, float y) {
    if (isBlockFloatingViewInsideCanva(x, y)) {
      if (binding.canva.getEvent().getEnableEdit()) {
        if (binding.canva.attachedBlockLayout != null) {

          if (TargetUtils.isDragInsideTargetView(
              binding.canva.attachedBlockLayout, binding.getRoot(), (int) x, (int) y)) {

            int index = 0;
            for (int i = 0; i < binding.canva.attachedBlockLayout.getChildCount(); i++) {
              View child = binding.canva.attachedBlockLayout.getChildAt(i);
              if (y + binding.canva.getScrollY() > child.getY() + (child.getHeight() / 2)) {
                index = i + 1;
              } else {
                break;
              }
            }

            if (index == 0) {
              index = 1;
            }
            dropBlockView(index, x, y);
          } else {
            BlockView block =
                new BlockView(this, getContext(), draggingBlock.getBlockModel().clone());

            FrameLayout.LayoutParams blockParams =
                new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

            blockParams.setMargins(
                (int) x + binding.canva.getScrollX() - binding.codeEditor.getPaddingStart(),
                (int) y + binding.canva.getScrollY() - binding.codeEditor.getPaddingTop(),
                0,
                0);

            block.setEnableDragDrop(true);
            block.setEnableEditing(true);
            block.setInsideEditor(true);
            binding.canva.addView(block);
            block.setLayoutParams(blockParams);

            if (((ViewGroup) draggingBlock.getParent()).getTag() != null) {
              if (((ViewGroup) draggingBlock.getParent()).getTag() instanceof BlockDroppableTag) {
                BlockDroppableTag tag =
                    ((BlockDroppableTag) ((ViewGroup) draggingBlock.getParent()).getTag());

                if (tag.getBlockDroppableType() == BlockDroppableTag.DEFAULT_BLOCK_DROPPER
                    && block.getBlockModel().getBlockType() == BlockModel.Type.defaultBlock) {
                  tag.getDropProperty(BlockHolderLayer.class)
                      .getBlocks()
                      .remove(((ViewGroup) draggingBlock.getParent()).indexOfChild(draggingBlock));
                }
              }
            }

            if (draggingBlock.isInsideEditor()) {
              ((ViewGroup) draggingBlock.getParent()).removeView(draggingBlock);
            }
          }
        }
      }
      if (draggingBlock.isInsideEditor()) {
        if (((ViewGroup) draggingBlock.getParent()) != null) {
          ((ViewGroup) draggingBlock.getParent()).removeView(draggingBlock);
        }
      }
    } else {
      if (draggingBlock.isInsideEditor()) {
        draggingBlock.setVisibility(View.VISIBLE);
      }
    }
  }

  public void dropBlockView(int index, float x, float y) {
    boolean isDropConsumed = false;
    if (binding.canva.attachedBlockLayout.getChildCount() > index) {
      if (binding.canva.attachedBlockLayout.getChildAt(index) instanceof BlockView) {
        BlockView blockView = (BlockView) binding.canva.attachedBlockLayout.getChildAt(index);
        if (blockView.drop(x, y, draggingBlock.getBlockModel())) {
          isDropConsumed = true;
        }
      }
    }

    if (!isDropConsumed) {
      if (binding.canva.attachedBlockLayout.getChildCount() > (index - 1)) {
        if (binding.canva.attachedBlockLayout.getChildAt(index - 1) instanceof BlockView) {
          BlockView blockView = (BlockView) binding.canva.attachedBlockLayout.getChildAt(index - 1);
          if (blockView.drop(x, y, draggingBlock.getBlockModel())) {
            isDropConsumed = true;
          }
        }
      }
    }

    if (!isDropConsumed) {
      if (binding.canva.attachedBlockLayout.getChildCount() > (index + 1)) {
        if (binding.canva.attachedBlockLayout.getChildAt(index + 1) instanceof BlockView) {
          BlockView blockView = (BlockView) binding.canva.attachedBlockLayout.getChildAt(index + 1);
          if (blockView.drop(x, y, draggingBlock.getBlockModel())) {
            isDropConsumed = true;
          }
        }
      }
    }

    if (!isDropConsumed) {
      BlockView block = new BlockView(this, getContext(), draggingBlock.getBlockModel().clone());

      block.setEnableDragDrop(true);
      block.setEnableEditing(true);
      block.setInsideEditor(true);
      if (((ViewGroup) draggingBlock.getParent()).getTag() != null) {
        if (((ViewGroup) draggingBlock.getParent()).getTag() instanceof BlockDroppableTag) {
          BlockDroppableTag tag =
              ((BlockDroppableTag) ((ViewGroup) draggingBlock.getParent()).getTag());

          if (tag.getBlockDroppableType() == BlockDroppableTag.DEFAULT_BLOCK_DROPPER
              && block.getBlockModel().getBlockType() == BlockModel.Type.defaultBlock) {
            tag.getDropProperty(BlockHolderLayer.class)
                .getBlocks()
                .remove(((ViewGroup) draggingBlock.getParent()).indexOfChild(draggingBlock));
          }
        }
      }

      binding.canva.attachedBlockLayout.addView(block, index);
      if (draggingBlock.isInsideEditor()) {
        ((ViewGroup) draggingBlock.getParent()).removeView(draggingBlock);
      }
    }
  }

  public void moveFloatingBlockView(float x, float y) {
    int notAllowedIconWidth = 0;
    if (blockFloatingView.notAllowed != null) {
      if (blockFloatingView.notAllowed.getParent() != null) {
        notAllowedIconWidth = blockFloatingView.notAllowed.getWidth();
      }
    }
    blockFloatingView.setX(x - notAllowedIconWidth);
    blockFloatingView.setY(y);
    blockFloatingView.setAllowed(isBlockFloatingViewInsideCanva(x, y));

    blockPreview.removePreview();
    blockPreview.setBlock(draggingBlock.getBlockModel());
    if (isBlockFloatingViewInsideCanva(x, y)) {
      if (binding.canva.getEvent().getEnableEdit()) {
        if (binding.canva.attachedBlockLayout != null) {

          if (TargetUtils.isDragInsideTargetView(
              binding.canva.attachedBlockLayout, binding.getRoot(), (int) x, (int) y)) {

            int index = 0;
            for (int i = 0; i < binding.canva.attachedBlockLayout.getChildCount(); i++) {
              View child = binding.canva.attachedBlockLayout.getChildAt(i);
              if (y + binding.canva.getScrollY() > child.getY() + (child.getHeight() / 2)) {
                index = i + 1;
              } else {
                break;
              }
            }

            if (index == 0) {
              index = 1;
            }

            setBlockPreview(index, x, y);
          } else {
            blockPreview.removePreview();
          }
        } else {
          blockPreview.removePreview();
        }
      } else {
        blockPreview.removePreview();
      }
    } else {
      blockPreview.removePreview();
    }
  }

  public void setBlockPreview(int index, float x, float y) {
    boolean isPreviewConsumed = false;
    if (binding.canva.attachedBlockLayout.getChildCount() > index) {
      if (binding.canva.attachedBlockLayout.getChildAt(index) instanceof BlockView) {
        BlockView blockView = (BlockView) binding.canva.attachedBlockLayout.getChildAt(index);
        if (blockView.preview(x, y, draggingBlock.getBlockModel())) {
          isPreviewConsumed = true;
        }
      }
    }

    if (!isPreviewConsumed) {
      if (binding.canva.attachedBlockLayout.getChildCount() > (index - 1)) {
        if (binding.canva.attachedBlockLayout.getChildAt(index - 1) instanceof BlockView) {
          BlockView blockView = (BlockView) binding.canva.attachedBlockLayout.getChildAt(index - 1);
          if (blockView.preview(x, y, draggingBlock.getBlockModel())) {
            isPreviewConsumed = true;
          }
        }
      }
    }

    if (!isPreviewConsumed) {
      if (binding.canva.attachedBlockLayout.getChildCount() > (index + 1)) {
        if (binding.canva.attachedBlockLayout.getChildAt(index + 1) instanceof BlockView) {
          BlockView blockView = (BlockView) binding.canva.attachedBlockLayout.getChildAt(index + 1);
          if (blockView.preview(x, y, draggingBlock.getBlockModel())) {
            isPreviewConsumed = true;
          }
        }
      }
    }

    if (!isPreviewConsumed) {
      binding.canva.attachedBlockLayout.addView(blockPreview, index);
    }
  }

  public boolean isBlockFloatingViewInsideCanva(float x, float y) {
    int notAllowedIconWidth = 0;
    if (blockFloatingView.notAllowed != null) {
      if (blockFloatingView.notAllowed.getParent() != null) {
        notAllowedIconWidth = blockFloatingView.notAllowed.getWidth();
      }
    }

    return TargetUtils.isDragInsideTargetView(
        binding.editorSection, this, (int) x + notAllowedIconWidth, (int) y);
  }

  public void loadBlocksInEvent() {
    if (binding.canva.attachedBlockLayout == null) return;
    if (binding.canva.getEvent() == null) return;

    ArrayList<BlockModel> blocks = new ArrayList<BlockModel>();
    for (int i = 0; i < binding.canva.attachedBlockLayout.getChildCount(); ++i) {
      if (i == 0) continue;
      if (binding.canva.attachedBlockLayout.getChildAt(i) instanceof BlockView) {
        blocks.add(((BlockView) binding.canva.attachedBlockLayout.getChildAt(i)).getBlockModel());
      }
    }
    binding.canva.getEvent().setBlockModels(blocks);
  }

  public Event getEvent() {
    return binding.canva.getEvent();
  }

  public HashMap<String, Object> getVariables() {
    return this.variables;
  }

  public void setVariables(HashMap<String, Object> variables) {
    this.variables = variables;
  }
}
