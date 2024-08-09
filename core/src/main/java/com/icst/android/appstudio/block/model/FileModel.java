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
import com.icst.android.appstudio.block.tag.DependencyTag;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class FileModel implements Serializable, Cloneable {
  public static final long serialVersionUID = 2L;

  private String fileName;
  private String fileExtension;
  private String replacerKey;
  private String rawCode;
  private String builtInEventsName;
  private ArrayList<Object> defaultBuiltInEvents;
  private boolean isFolder;
  private boolean isAndroidAppModule;
  private boolean isAndroidLibrary;

  public String getFileName() {
    return this.fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileExtension() {
    return this.fileExtension;
  }

  public void setFileExtension(String fileExtension) {
    this.fileExtension = fileExtension;
  }

  public String getRawCode() {
    return this.rawCode;
  }

  public void setRawCode(String rawCode) {
    this.rawCode = rawCode;
  }

  public ArrayList<Object> getDefaultBuiltInEvents() {
    return this.defaultBuiltInEvents;
  }

  public void setDefaultBuiltInEvents(ArrayList<Object> defaultBuiltInEvents) {
    this.defaultBuiltInEvents = defaultBuiltInEvents;
  }

  public String getBuiltInEventsName() {
    return this.builtInEventsName;
  }

  public void setBuiltInEventsName(String builtInEventsName) {
    this.builtInEventsName = builtInEventsName;
  }

  public boolean isFolder() {
    return this.isFolder;
  }

  public void setFolder(boolean isFolder) {
    this.isFolder = isFolder;
  }

  public boolean isAndroidAppModule() {
    return this.isAndroidAppModule;
  }

  public void setAndroidAppModule(boolean isAndroidAppModule) {
    this.isAndroidAppModule = isAndroidAppModule;
  }

  public boolean isAndroidLibrary() {
    return this.isAndroidLibrary;
  }

  public void setAndroidLibrary(boolean isAndroidLibrary) {
    this.isAndroidLibrary = isAndroidLibrary;
  }

  public String getReplacerKey() {
    return this.replacerKey;
  }

  public void setReplacerKey(String replacerKey) {
    this.replacerKey = replacerKey;
  }

  public ArrayList<DependencyTag> getUsedDependency(
      ArrayList<Object> builtInEvents, ArrayList<Object> events) {
    ArrayList<DependencyTag> usedDependency = new ArrayList<DependencyTag>();

    if (builtInEvents != null) {

      for (int eventCount = 0; eventCount < builtInEvents.size(); ++eventCount) {
        if (builtInEvents.get(eventCount) instanceof Event) {
          Event event = (Event) builtInEvents.get(eventCount);

          for (int tags = 0; tags < event.getAdditionalTagsUsed().size(); ++tags) {
            AdditionalCodeHelperTag tag = event.getAdditionalTagsUsed().get(tags);

            if (tag instanceof DependencyTag) {
              usedDependency.add((DependencyTag) tag);
            }
          }
        }
      }
    }

    if (events != null) {
      for (int eventCount = 0; eventCount < events.size(); ++eventCount) {
        if (events.get(eventCount) instanceof Event) {
          Event event = (Event) events.get(eventCount);

          for (int tags = 0; tags < event.getAdditionalTagsUsed().size(); ++tags) {
            AdditionalCodeHelperTag tag = event.getAdditionalTagsUsed().get(tags);

            if (tag instanceof DependencyTag) {
              usedDependency.add((DependencyTag) tag);
            }
          }
        }
      }
    }

    return usedDependency;
  }

  public String getCode(
      ArrayList<Object> builtInEvents,
      ArrayList<Object> events,
      HashMap<String, Object> variables) {
    String resultCode = getRawCode() != null ? new String(getRawCode()) : null;
    if (resultCode == null) {
      return "";
    }

    if (builtInEvents != null) {

      for (int eventCount = 0; eventCount < builtInEvents.size(); ++eventCount) {
        if (builtInEvents.get(eventCount) instanceof Event) {
          Event event = (Event) builtInEvents.get(eventCount);
          resultCode =
              resultCode.replace(
                  RawCodeReplacer.getReplacer(getReplacerKey(), event.getName()),
                  event.getCode(variables));
        }
      }
    }

    if (events != null) {
      for (int eventCount = 0; eventCount < events.size(); ++eventCount) {
        if (events.get(eventCount) instanceof Event) {
          Event event = (Event) events.get(eventCount);
          resultCode =
              resultCode.replace(
                  RawCodeReplacer.getReplacer(event.getEventReplacerKey(), event.getName()),
                  event.getCode(variables));
        }
      }
    }

    resultCode = resultCode.replace(RawCodeReplacer.getReplacer("$FileName"), getFileName());

    resultCode = RawCodeReplacer.removeAndroidAppStudioString(getReplacerKey(), resultCode);
    return resultCode;
  }

  @Override
  protected FileModel clone() {
    FileModel fileModel = new FileModel();
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
    fileModel.setAndroidAppModule(new Boolean(isAndroidAppModule));
    return fileModel;
  }

  public String getName() {
    StringBuilder fileName = new StringBuilder();
    if (getFileName() != null) fileName.append(getFileName());
    if (!isFolder()) if (getFileExtension() != null) fileName.append("." + getFileExtension());
    return fileName.toString();
  }
}
