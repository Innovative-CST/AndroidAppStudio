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

package com.tscodeeditor.android.appstudio.helper;

import com.tscodeeditor.android.appstudio.activities.ProjectManagerActivity;
import com.tscodeeditor.android.appstudio.block.model.Event;
import com.tscodeeditor.android.appstudio.block.model.EventGroupModel;
import com.tscodeeditor.android.appstudio.block.model.FileModel;
import com.tscodeeditor.android.appstudio.models.EventHolder;
import com.tscodeeditor.android.appstudio.models.ProjectModel;
import com.tscodeeditor.android.appstudio.utils.EnvironmentUtils;
import com.tscodeeditor.android.appstudio.utils.EventUtils;
import com.tscodeeditor.android.appstudio.utils.serialization.DeserializerUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FileModelCodeHelper {
  private File eventsDirectory;
  private FileModel fileModel;
  private File projectRootDirectory;

  public File getEventsDirectory() {
    return this.eventsDirectory;
  }

  public void setEventsDirectory(File eventsDirectory) {
    this.eventsDirectory = eventsDirectory;
  }

  public FileModel getFileModel() {
    return this.fileModel;
  }

  public void setFileModel(FileModel fileModel) {
    this.fileModel = fileModel;
  }

  public File getProjectRootDirectory() {
    return this.projectRootDirectory;
  }

  public void setProjectRootDirectory(File projectRootDirectory) {
    this.projectRootDirectory = projectRootDirectory;
  }

  public String getCode() {
    ArrayList<Object> builtInEvents = null;
    ArrayList<Object> dirEvents = new ArrayList<Object>();

    if (fileModel == null) return null;
    if (eventsDirectory == null) return null;
    if (eventsDirectory.isFile()) return null;

    if (eventsDirectory.exists()) {

      for (File file : eventsDirectory.listFiles()) {
        if (file.isFile()) continue;

        File eventsHolderFile = new File(file, EnvironmentUtils.EVENTS_HOLDER);

        if (!eventsHolderFile.exists()) continue;

        EventHolder holder = null;

        Object deserializedObject = DeserializerUtils.deserialize(eventsHolderFile);

        if (deserializedObject == null) continue;

        if (!(deserializedObject instanceof EventHolder)) continue;

        holder = (EventHolder) deserializedObject;

        if (fileModel.getBuiltInEventsName().equals(holder.getHolderName())) {
          builtInEvents = EventUtils.getEventsObject(new File(file, EnvironmentUtils.EVENTS_DIR));
        } else {
          ArrayList<Object> extraEvents =
              EventUtils.getEventsObject(new File(file, EnvironmentUtils.EVENTS_DIR));

          for (int extraEventCount = 0; extraEventCount < extraEvents.size(); ++extraEventCount) {
            if (extraEvents.get(extraEventCount) instanceof Event) {
              dirEvents.add((Event) extraEvents.get(extraEventCount));
            } else if (extraEvents.get(extraEventCount) instanceof EventGroupModel) {
              dirEvents.add((EventGroupModel) extraEvents.get(extraEventCount));
            }
          }
        }
      }
    }

    HashMap<String, Object> variables = new HashMap<String, Object>();

    File projectConfig = new File(getProjectRootDirectory(), EnvironmentUtils.PROJECT_CONFIGRATION);
    DeserializerUtils.deserialize(
        projectConfig,
        new DeserializerUtils.DeserializerListener() {

          @Override
          public void onSuccessfullyDeserialized(Object deserializedObject) {
            if (deserializedObject instanceof ProjectModel) {
              variables.put("ProjectModel", (ProjectModel) deserializedObject);
            }
          }

          @Override
          public void onFailed(int errorCode, Exception e) {}
        });

    return fileModel.getCode(builtInEvents, dirEvents, variables);
  }
}
