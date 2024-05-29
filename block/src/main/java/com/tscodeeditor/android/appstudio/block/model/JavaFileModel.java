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

import com.tscodeeditor.android.appstudio.block.utils.ArrayUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class JavaFileModel extends FileModel implements Serializable {
  public static final long serialVersionUID = 20L;

  private int classType;
  private String extendingClass;
  private String[] implementingInterface;

  public static final int SIMPLE_JAVA_CLASS = 0;
  public static final int JAVA_ACTIVITY = 1;

  @Override
  public String getCode(
      ArrayList<Object> builtInEvents,
      ArrayList<Object> events,
      HashMap<String, Object> variables) {
    return super.getCode(builtInEvents, events, variables);
  }

  @Override
  protected FileModel clone() {
    JavaFileModel fileModel = new JavaFileModel();
    fileModel.setFileName(getFileName() != null ? new String(getFileName()) : null);
    fileModel.setFileExtension(getFileExtension() != null ? new String(getFileExtension()) : null);
    fileModel.setBuiltInEventsName(
        getBuiltInEventsName() != null ? new String(getBuiltInEventsName()) : null);

    ArrayList<Object> clonedBuildInEvents = new ArrayList<Object>();
    for (int position = 0; position < getDefaultBuiltInEvents().size(); ++position) {
      if (getDefaultBuiltInEvents().get(position) instanceof Event) {
        clonedBuildInEvents.add(((Event) getDefaultBuiltInEvents().get(position)).clone());
      } else if (getDefaultBuiltInEvents().get(position) instanceof Event) {
        clonedBuildInEvents.add(
            ((EventGroupModel) getDefaultBuiltInEvents().get(position)).clone());
      }
    }
    fileModel.setReplacerKey(getReplacerKey() != null ? new String(getReplacerKey()) : null);
    fileModel.setDefaultBuiltInEvents(clonedBuildInEvents);
    fileModel.setRawCode(getRawCode() != null ? new String(getRawCode()) : null);
    fileModel.setFolder(new Boolean(isFolder()));
    fileModel.setAndroidLibrary(new Boolean(isAndroidLibrary()));
    fileModel.setAndroidAppModule(new Boolean(isAndroidAppModule()));
    fileModel.setClassType(new Integer(getClassType()));
    fileModel.setExtendingClass(
        getExtendingClass() != null ? new String(getExtendingClass()) : null);
    fileModel.setImplementingInterface(ArrayUtils.clone(getImplementingInterface()));
    return fileModel;
  }

  public int getClassType() {
    return this.classType;
  }

  public void setClassType(int classType) {
    this.classType = classType;
  }

  public String getExtendingClass() {
    return this.extendingClass;
  }

  public void setExtendingClass(String extendingClass) {
    this.extendingClass = extendingClass;
  }

  public String[] getImplementingInterface() {
    return this.implementingInterface;
  }

  public void setImplementingInterface(String[] implementingInterface) {
    this.implementingInterface = implementingInterface;
  }
}
