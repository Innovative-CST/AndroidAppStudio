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

package com.icst.android.appstudio.block.model;

import com.icst.android.appstudio.block.tag.AdditionalCodeHelperTag;
import com.icst.android.appstudio.block.utils.ArrayUtils;
import java.io.Serializable;
import java.util.HashMap;

public class BlockValueFieldModel extends BlockFieldModel implements Serializable, Cloneable {
  public static final long serialVersionUID = 11L;

  private boolean enableEdit;
  private String acceptors[];
  private String replacer;
  private BlockModel blockModel;
  private AdditionalCodeHelperTag[] additionalTags;
  private int fieldType;
  private String pattern; // Only works if fieldType is pattern validator.

  public final class FieldType {
    public static final int FIELD_TYPE_NOT_SET = 0;
    public static final int FIELD_INPUT_ONLY = 1;
    public static final int FIELD_EXTENSION_VIEW_ONLY = 2;
    public static final int FIELD_BOOLEAN = 3;
	public static final int FIELD_NUMBER = 4;
  }

  public String[] getAcceptors() {
    return this.acceptors;
  }

  public void setAcceptors(String[] acceptors) {
    this.acceptors = acceptors;
  }

  public boolean isEnabledEdit() {
    return this.enableEdit;
  }

  public void setEnableEdit(boolean enableEdit) {
    this.enableEdit = enableEdit;
  }

  public BlockModel getBlockModel() {
    return this.blockModel;
  }

  public void setBlockModel(BlockModel blockModel) {
    if (blockModel != null) {
      setValue(null);
    }
    this.blockModel = blockModel;
  }

  public String getPattern() {
    return this.pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public String getReplacer() {
    return this.replacer;
  }

  public void setReplacer(String replacer) {
    this.replacer = replacer;
  }

  public int getFieldType() {
    return this.fieldType;
  }

  public void setFieldType(int fieldType) {
    this.fieldType = fieldType;
  }

  @Override
  public void setValue(String value) {
    super.setValue(value);
    if (value != null) {
      setBlockModel(null);
    }
  }

  public String getCode(HashMap<String, Object> variables) {
    return getBlockModel() != null ? getBlockModel().getCode(variables) : getValue();
  }

  public AdditionalCodeHelperTag[] getAdditionalTags() {
    return this.additionalTags;
  }

  public void setAdditionalTags(AdditionalCodeHelperTag[] additionalTags) {
    this.additionalTags = additionalTags;
  }

  @Override
  public BlockValueFieldModel clone() {
    BlockValueFieldModel blockValueFieldModel = new BlockValueFieldModel();
    blockValueFieldModel.setValue(getValue() != null ? new String(getValue()) : null);
    blockValueFieldModel.setReplacer(getReplacer() != null ? getReplacer() : null);
    blockValueFieldModel.setEnableEdit(new Boolean(isEnabledEdit()));
    blockValueFieldModel.setBlockModel(getBlockModel() != null ? getBlockModel() : null);
    blockValueFieldModel.setFieldType(new Integer(getFieldType()));
    blockValueFieldModel.setPattern(getPattern() != null ? new String(getPattern()) : null);
	blockValueFieldModel.setAdditionalTags(ArrayUtils.clone(getAdditionalTags()));
    if (getAcceptors() != null) {
      String[] acceptors = new String[getAcceptors().length];

      for (int position = 0; position < getAcceptors().length; ++position) {
        acceptors[position] = new String(getAcceptors()[position]);
      }
      blockValueFieldModel.setAcceptors(acceptors);
    }
    return blockValueFieldModel;
  }
}
