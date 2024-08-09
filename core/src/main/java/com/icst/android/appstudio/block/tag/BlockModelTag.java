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

package com.icst.android.appstudio.block.tag;

import com.icst.android.appstudio.block.tag.AdditionalCodeHelperTag;
import com.icst.android.appstudio.block.utils.ArrayUtils;
import java.io.Serializable;

public class BlockModelTag implements Serializable {
  public static final long serialVersionUID = 13L;

  public String[] supportedFileExtensions;
  public String[] notSupportedFileExtensions;
  public String[] supportedEvents;
  public String[] notSupportedEvents;
  public AdditionalCodeHelperTag[] additionalTags;
  public boolean showInAllBlocksDeveloperMode;
  public boolean isForDeveloperOnly;

  public String[] getSupportedFileExtensions() {
    return this.supportedFileExtensions;
  }

  public void setSupportedFileExtensions(String[] supportedFileExtensions) {
    this.supportedFileExtensions = supportedFileExtensions;
  }

  public String[] getNotSupportedFileExtensions() {
    return this.notSupportedFileExtensions;
  }

  public void setNotSupportedFileExtensions(String[] notSupportedFileExtensions) {
    this.notSupportedFileExtensions = notSupportedFileExtensions;
  }

  public String[] getSupportedEvents() {
    return this.supportedEvents;
  }

  public void setSupportedEvents(String[] supportedEvents) {
    this.supportedEvents = supportedEvents;
  }

  public String[] getNotSupportedEvents() {
    return this.notSupportedEvents;
  }

  public void setNotSupportedEvents(String[] notSupportedEvents) {
    this.notSupportedEvents = notSupportedEvents;
  }

  public AdditionalCodeHelperTag[] getAdditionalTags() {
    return this.additionalTags;
  }

  public void setAdditionalTags(AdditionalCodeHelperTag[] additionalTags) {
    this.additionalTags = additionalTags;
  }

  public boolean getShowInAllBlocksDeveloperMode() {
    return this.showInAllBlocksDeveloperMode;
  }

  public void setShowInAllBlocksDeveloperMode(boolean showInAllBlocksDeveloperMode) {
    this.showInAllBlocksDeveloperMode = showInAllBlocksDeveloperMode;
  }

  public boolean isForDeveloperOnly() {
    return this.isForDeveloperOnly;
  }

  public void setForDeveloperOnly(boolean isForDeveloperOnly) {
    this.isForDeveloperOnly = isForDeveloperOnly;
  }

  public BlockModelTag clone() {
    BlockModelTag clone = new BlockModelTag();

    clone.setSupportedFileExtensions(
        getSupportedFileExtensions() == null
            ? null
            : ArrayUtils.clone(getSupportedFileExtensions()));

    clone.setNotSupportedFileExtensions(
        getNotSupportedFileExtensions() == null
            ? null
            : ArrayUtils.clone(getNotSupportedFileExtensions()));

    clone.setSupportedEvents(
        getSupportedEvents() == null ? null : ArrayUtils.clone(getSupportedEvents()));

    clone.setNotSupportedEvents(
        getNotSupportedEvents() == null ? null : ArrayUtils.clone(getNotSupportedEvents()));

    clone.setShowInAllBlocksDeveloperMode(new Boolean(getShowInAllBlocksDeveloperMode()));

    clone.setForDeveloperOnly(new Boolean(isForDeveloperOnly()));

    clone.setAdditionalTags(ArrayUtils.clone(getAdditionalTags()));
    return clone;
  }
}
