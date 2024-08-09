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
import com.icst.android.appstudio.block.utils.RawCodeReplacer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Event implements Serializable, Cloneable {
  public static final long serialVersionUID = 1L;

  private String name;
  private String title;
  private String description;
  private String rawCode;
  private String eventReplacer;
  private String eventReplacerKey;
  private BlockModel eventTopBlock;
  private ArrayList<BlockModel> blockModels;
  private AdditionalCodeHelperTag[] additionalTags;
  private String[] classes;
  private String[] classesImports;
  private String[] holderName;
  private String[] extension;
  private String createInHolderName;
  private byte[] icon;
  private boolean applyColorFilter;
  private boolean isDirectFileEvent;
  private boolean enableEdit;
  private boolean enableRootBlocksDrag;
  private boolean enableRootBlocksValueEditing;
  private boolean enableRootBlocksSubBlockEditing;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getRawCode() {
    return this.rawCode;
  }

  public void setRawCode(String rawCode) {
    this.rawCode = rawCode;
  }

  public String getEventReplacer() {
    return this.eventReplacer;
  }

  public void setEventReplacer(String eventReplacer) {
    this.eventReplacer = eventReplacer;
  }

  public String getEventReplacerKey() {
    return this.eventReplacerKey;
  }

  public void setEventReplacerKey(String eventReplacerKey) {
    this.eventReplacerKey = eventReplacerKey;
  }

  public boolean getEnableEdit() {
    return this.enableEdit;
  }

  public void setEnableEdit(boolean enableEdit) {
    this.enableEdit = enableEdit;
  }

  public boolean getEnableRootBlocksDrag() {
    return this.enableRootBlocksDrag;
  }

  public void setEnableRootBlocksDrag(boolean enableRootBlocksDrag) {
    this.enableRootBlocksDrag = enableRootBlocksDrag;
  }

  public boolean getEnableRootBlocksValueEditing() {
    return this.enableRootBlocksValueEditing;
  }

  public void setEnableRootBlocksValueEditing(boolean enableRootBlocksValueEditing) {
    this.enableRootBlocksValueEditing = enableRootBlocksValueEditing;
  }

  public BlockModel getEventTopBlock() {
    return this.eventTopBlock;
  }

  public void setEventTopBlock(BlockModel eventTopBlock) {
    this.eventTopBlock = eventTopBlock;
  }

  public ArrayList<BlockModel> getBlockModels() {
    return this.blockModels;
  }

  public void setBlockModels(ArrayList<BlockModel> blockModels) {
    this.blockModels = blockModels;
  }

  public boolean getEnableRootBlocksSubBlockEditing() {
    return this.enableRootBlocksSubBlockEditing;
  }

  public void setEnableRootBlocksSubBlockEditing(boolean enableRootBlocksSubBlockEditing) {
    this.enableRootBlocksSubBlockEditing = enableRootBlocksSubBlockEditing;
  }

  public ArrayList<AdditionalCodeHelperTag> getAdditionalTagsUsed() {
    ArrayList<AdditionalCodeHelperTag> tags = new ArrayList<AdditionalCodeHelperTag>();

    // Add this event code helper tag to tags
    if (getAdditionalTags() != null) {
      for (int i = 0; i < getAdditionalTags().length; ++i) {
        tags.add(getAdditionalTags()[i]);
      }
    }

    if (getBlockModels() == null) {
      return tags;
    }

    for (int blocksCount = 0; blocksCount < getBlockModels().size(); ++blocksCount) {
      BlockModel block = getBlockModels().get(blocksCount);
      tags.addAll(block.getAdditionalTagsOfBlock());
    }

    return tags;
  }

  public String getCode(HashMap<String, Object> variables) {
    StringBuilder generatedCode = new StringBuilder();
    if (getBlockModels() == null) {
      String returnEmptyEventCode = new String(getRawCode());
      returnEmptyEventCode =
          RawCodeReplacer.removeAndroidAppStudioString(getEventReplacerKey(), returnEmptyEventCode);
      return returnEmptyEventCode;
    }

    for (int blocksCount = 0; blocksCount < getBlockModels().size(); ++blocksCount) {
      if (blocksCount != 0) generatedCode.append("\n");
      generatedCode.append(getBlockModels().get(blocksCount).getCode(variables));
    }

    String eventCode = new String(getRawCode());

    // Get space for formatting....
    String formatter = null;
    String[] lines = eventCode.split("\n");
    for (String line : lines) {
      if (line.contains(RawCodeReplacer.getReplacer(getEventReplacerKey(), getEventReplacer()))) {
        formatter =
            line.substring(
                0,
                line.indexOf(
                    RawCodeReplacer.getReplacer(getEventReplacerKey(), getEventReplacer())));
      }
    }

    StringBuilder formattedGeneratedCode = new StringBuilder();

    String[] generatedCodeLines = generatedCode.toString().split("\n");

    for (int generatedCodeLinePosition = 0;
        generatedCodeLinePosition < generatedCodeLines.length;
        ++generatedCodeLinePosition) {

      if (formatter != null) {
        if (generatedCodeLinePosition != 0) formattedGeneratedCode.append(formatter);
      }

      formattedGeneratedCode.append(generatedCodeLines[generatedCodeLinePosition]);
      if (generatedCodeLinePosition != (generatedCodeLines.length - 1)) {
        formattedGeneratedCode.append("\n");
      }
    }

    eventCode =
        eventCode.replace(
            RawCodeReplacer.getReplacer(getEventReplacerKey(), getEventReplacer()),
            formattedGeneratedCode.toString());

    return eventCode;
  }

  @Override
  public Event clone() {
    Event event = new Event();
    event.setName(getName() != null ? new String(getName()) : null);
    event.setTitle(getTitle() != null ? new String(getTitle()) : null);
    event.setDescription(getDescription() != null ? new String(getDescription()) : null);
    event.setRawCode(getRawCode() != null ? new String(getRawCode()) : null);
    event.setEventReplacer(getEventReplacer() != null ? new String(getEventReplacer()) : null);
    event.setCreateInHolderName(
        getCreateInHolderName() != null ? new String(getCreateInHolderName()) : null);
    event.setEventReplacerKey(
        getEventReplacerKey() != null ? new String(getEventReplacerKey()) : null);
    event.setEventTopBlock(getEventTopBlock() != null ? getEventTopBlock().clone() : null);
    event.setAdditionalTags(ArrayUtils.clone(getAdditionalTags()));
    event.setClasses(ArrayUtils.clone(getClasses()));
    event.setClassesImports(ArrayUtils.clone(getClassesImports()));
    event.setHolderName(ArrayUtils.clone(getHolderName()));
    event.setExtension(ArrayUtils.clone(getExtension()));
    event.setDirectFileEvent(new Boolean(isDirectFileEvent()));
    event.setEnableEdit(new Boolean(getEnableEdit()));
    event.setEnableRootBlocksDrag(new Boolean(getEnableRootBlocksDrag()));
    event.setEnableRootBlocksValueEditing(new Boolean(getEnableRootBlocksValueEditing()));
    event.setEnableRootBlocksSubBlockEditing(new Boolean(getEnableRootBlocksSubBlockEditing()));
    event.setApplyColorFilter(new Boolean(getApplyColorFilter()));

    if (getIcon() != null) {
      event.setIcon(new byte[this.icon.length]);
      System.arraycopy(getIcon(), 0, event.getIcon(), 0, getIcon().length);
    }

    if (getBlockModels() != null) {
      ArrayList<BlockModel> clonedBlockModels = new ArrayList<BlockModel>();
      for (int i = 0; i < getBlockModels().size(); ++i) {
        clonedBlockModels.add(getBlockModels().get(i).clone());
      }

      event.setBlockModels(clonedBlockModels);
    } else event.setBlockModels(null);

    return event;
  }

  public AdditionalCodeHelperTag[] getAdditionalTags() {
    return this.additionalTags;
  }

  public void setAdditionalTags(AdditionalCodeHelperTag[] additionalTags) {
    this.additionalTags = additionalTags;
  }

  public String[] getClasses() {
    return this.classes;
  }

  public void setClasses(String[] classes) {
    this.classes = classes;
  }

  public String[] getHolderName() {
    return this.holderName;
  }

  public void setHolderName(String[] holderName) {
    this.holderName = holderName;
  }

  public String[] getExtension() {
    return this.extension;
  }

  public void setExtension(String[] extension) {
    this.extension = extension;
  }

  public String getCreateInHolderName() {
    return this.createInHolderName;
  }

  public void setCreateInHolderName(String createInHolderName) {
    this.createInHolderName = createInHolderName;
  }

  public String[] getClassesImports() {
    return this.classesImports;
  }

  public void setClassesImports(String[] classesImports) {
    this.classesImports = classesImports;
  }

  public boolean isDirectFileEvent() {
    return this.isDirectFileEvent;
  }

  public void setDirectFileEvent(boolean isDirectFileEvent) {
    this.isDirectFileEvent = isDirectFileEvent;
  }

  public byte[] getIcon() {
    return this.icon;
  }

  public void setIcon(byte[] icon) {
    this.icon = icon;
  }

  public boolean getApplyColorFilter() {
    return this.applyColorFilter;
  }

  public void setApplyColorFilter(boolean applyColorFilter) {
    this.applyColorFilter = applyColorFilter;
  }
}
