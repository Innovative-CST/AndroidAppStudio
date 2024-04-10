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

package com.tscodeeditor.android.appstudio.block.model;

import com.tscodeeditor.android.appstudio.block.utils.RawCodeReplacer;
import java.io.Serializable;
import java.util.ArrayList;

public class BlockModel implements Serializable, Cloneable {
  public static final long serialVersionUID = 3L;

  private String color;
  private String rawCode;
  private String replacerKey;
  private String[] returns;
  private Object[] tags;
  private int blockType;
  private boolean isLastBlock;
  private boolean isFirstBlock;
  private boolean isDragAllowed;
  private ArrayList<BlockLayerModel> blockLayerModel;

  public String getColor() {
    return this.color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getRawCode() {
    return this.rawCode;
  }

  public void setRawCode(String rawCode) {
    this.rawCode = rawCode;
  }

  public String getReplacerKey() {
    return this.replacerKey;
  }

  public void setReplacerKey(String replacerKey) {
    this.replacerKey = replacerKey;
  }

  public String[] getReturns() {
    return this.returns;
  }

  public void setReturns(String[] returns) {
    this.returns = returns;
  }

  public Object[] getTags() {
    return this.tags;
  }

  public void setTags(Object[] tags) {
    this.tags = tags;
  }

  public final class Type {
    public static final int defaultBlock = 0;
  }

  public int getBlockType() {
    return this.blockType;
  }

  public void setBlockType(int blockType) {
    this.blockType = blockType;
  }

  public boolean isLastBlock() {
    return this.isLastBlock;
  }

  public void setLastBlock(boolean isLastBlock) {
    this.isLastBlock = isLastBlock;
  }

  public boolean isFirstBlock() {
    return this.isFirstBlock;
  }

  public void setFirstBlock(boolean isFirstBlock) {
    this.isFirstBlock = isFirstBlock;
  }

  public ArrayList<BlockLayerModel> getBlockLayerModel() {
    return this.blockLayerModel;
  }

  public void setBlockLayerModel(ArrayList<BlockLayerModel> blockLayerModel) {
    this.blockLayerModel = blockLayerModel;
  }

  public String getCode() {
    String generatedCode = new String(getRawCode());

    for (int layerCount = 0; layerCount < getBlockLayerModel().size(); ++layerCount) {
      if (getBlockLayerModel().get(layerCount) instanceof BlockFieldLayerModel) {
        BlockFieldLayerModel layer = (BlockFieldLayerModel) getBlockLayerModel().get(layerCount);
        for (int blockFieldCount = 0;
            blockFieldCount < layer.getBlockFields().size();
            ++blockFieldCount) {
          if (!(layer.getBlockFields().get(blockFieldCount) instanceof BlockValueFieldModel))
            continue;

          generatedCode =
              generatedCode.replace(
                  RawCodeReplacer.getReplacer(
                      getReplacerKey(),
                      ((BlockValueFieldModel) layer.getBlockFields().get(blockFieldCount))
                          .getReplacer()),
                  ((BlockValueFieldModel) layer.getBlockFields().get(blockFieldCount)).getValue());
        }
      }

      if (getBlockLayerModel().get(layerCount) instanceof BlockHolderLayer) {
        BlockHolderLayer layer = (BlockHolderLayer) getBlockLayerModel().get(layerCount);

        generatedCode =
            generatedCode.replace(
                RawCodeReplacer.getReplacer(getReplacerKey(), layer.getReplacer()),
                layer.getCode());
      }
    }

    return generatedCode;
  }

  public boolean isDragAllowed() {
    return this.isDragAllowed;
  }

  public void setDragAllowed(boolean isDragAllowed) {
    this.isDragAllowed = isDragAllowed;
  }

  @Override
  public BlockModel clone() {
    BlockModel block = new BlockModel();
    block.setColor(new String(getColor() == null ? "" : getColor()));
    block.setRawCode(new String(getRawCode() == null ? "" : getRawCode()));
    block.setReplacerKey(new String(getReplacerKey() == null ? "" : getReplacerKey()));
    block.setBlockType(new Integer(getBlockType()));
    block.setDragAllowed(new Boolean(isDragAllowed()));
    block.setFirstBlock(new Boolean(isFirstBlock()));
    block.setLastBlock(new Boolean(isLastBlock()));

    if (getTags() != null) {
      Object[] clonedTags = new Object[] {};
      for (int position = 0; position < getTags().length; ++position) {
        clonedTags[position] = getTags()[position] == null ? null : getTags()[position];
      }
    }

    if (getReturns() != null) {
      String[] clonedReturns = new String[] {};
      for (int position = 0; position < getReturns().length; ++position) {
        clonedReturns[position] =
            new String(getReturns()[position] == null ? "" : getReturns()[position]);
      }
    }

    if (getBlockLayerModel() != null) {
      ArrayList<BlockLayerModel> cloneBlockLayerModel = new ArrayList<BlockLayerModel>();
      for (int layerCount = 0; layerCount < getBlockLayerModel().size(); ++layerCount) {
        if (getBlockLayerModel().get(layerCount) instanceof BlockFieldLayerModel) {
          cloneBlockLayerModel.add(
              ((BlockFieldLayerModel) getBlockLayerModel().get(layerCount)).clone());
        }
        if (getBlockLayerModel().get(layerCount) instanceof BlockHolderLayer) {
          cloneBlockLayerModel.add(
              ((BlockHolderLayer) getBlockLayerModel().get(layerCount)).clone());
        }
      }

      block.setBlockLayerModel(cloneBlockLayerModel);
    } else block.setBlockLayerModel(null);

    return block;
  }
}
