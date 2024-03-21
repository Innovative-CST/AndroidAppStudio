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

package com.tscodeeditor.android.appstudio.utils;

import com.tscodeeditor.android.appstudio.R;
import com.tscodeeditor.android.appstudio.block.model.BlockContentLayerModel;
import com.tscodeeditor.android.appstudio.block.model.BlockLayerModel;
import com.tscodeeditor.android.appstudio.block.model.BlockModel;
import com.tscodeeditor.android.appstudio.block.model.Event;
import com.tscodeeditor.android.appstudio.block.model.FileModel;
import com.tscodeeditor.android.appstudio.block.utils.RawCodeReplacer;
import com.tscodeeditor.android.appstudio.models.EventHolder;
import com.tscodeeditor.android.appstudio.utils.serialization.SerializerUtil;
import java.io.File;
import java.util.ArrayList;

public class GradleFileUtils {
  private static FileModel getAppModuleGradleFileModule() {
    FileModel appModuleGradleFile = new FileModel();
    appModuleGradleFile.setFileName("build");
    appModuleGradleFile.setFileExtension("gradle");
    appModuleGradleFile.setFolder(false);

    StringBuilder appModuleGradleFileRawCode = new StringBuilder();
    appModuleGradleFileRawCode.append("plugins {\n\tid 'com.android.application'\n}\n");
    appModuleGradleFileRawCode.append(RawCodeReplacer.getReplacer("androidBlock"));
    appModuleGradleFileRawCode.append(RawCodeReplacer.getReplacer("dependenciesBlock"));

    appModuleGradleFile.setRawCode(appModuleGradleFileRawCode.toString());

    ArrayList<Event> builtinEvents = new ArrayList<Event>();

    Event androidBlockEvent = new Event();
    androidBlockEvent.setTitle("App Configration");
    androidBlockEvent.setName("androidBlock");
    androidBlockEvent.setDescription("Contains basic defination of your app");
    androidBlockEvent.setEventReplacer("blockCode");
    androidBlockEvent.setRawCode("android {\n" + RawCodeReplacer.getReplacer("blockCode") + "\n}");
    androidBlockEvent.setEnableEdit(true);
    androidBlockEvent.setEnableRootBlocksDrag(false);

    BlockModel androidBlockEventBlockModel = new BlockModel();
    androidBlockEventBlockModel.setColor("#884400");
    androidBlockEventBlockModel.setFirstBlock(true);
    androidBlockEventBlockModel.setBlockType(BlockModel.Type.defaultBlock);

    ArrayList<BlockLayerModel> androidBlockEventBlockLayerModel = new ArrayList<BlockLayerModel>();

    BlockContentLayerModel androidBlockEventTextLayer = new BlockContentLayerModel();

    androidBlockEventBlockLayerModel.add(androidBlockEventTextLayer);
    androidBlockEventBlockModel.setBlockLayerModel(androidBlockEventBlockLayerModel);

    androidBlockEvent.setEventTopBlock(androidBlockEventBlockModel);
    androidBlockEvent.setEnableRootBlocksValueEditing(false);

    Event dependenciesBlockEvent = new Event();
    dependenciesBlockEvent.setTitle("App Libraries");
    dependenciesBlockEvent.setName("dependenciesBlock");
    dependenciesBlockEvent.setDescription("Contains library used by your app");
    dependenciesBlockEvent.setEventReplacer("blockCode");
    dependenciesBlockEvent.setRawCode(
        "dependencies {\n" + RawCodeReplacer.getReplacer("blockCode") + "\n}");
    dependenciesBlockEvent.setEnableEdit(true);
    dependenciesBlockEvent.setEnableRootBlocksDrag(false);
    dependenciesBlockEvent.setEnableRootBlocksValueEditing(false);

    builtinEvents.add(androidBlockEvent);
    builtinEvents.add(dependenciesBlockEvent);

    appModuleGradleFile.setDefaultBuiltInEvents(builtinEvents);

    return appModuleGradleFile;
  }

  private static FileModel getLibraryModuleGradleFileModule() {
    FileModel libraryModuleGradleFile = new FileModel();
    libraryModuleGradleFile.setFileName("build");
    libraryModuleGradleFile.setFileExtension("gradle");
    libraryModuleGradleFile.setFolder(false);

    StringBuilder libraryModuleGradleFileRawCode = new StringBuilder();
    libraryModuleGradleFileRawCode.append("plugins {\n\tid 'com.android.library'\n}\n");
    libraryModuleGradleFileRawCode.append(RawCodeReplacer.getReplacer("androidBlock"));
    libraryModuleGradleFileRawCode.append(RawCodeReplacer.getReplacer("dependenciesBlock"));

    libraryModuleGradleFile.setRawCode(libraryModuleGradleFileRawCode.toString());

    ArrayList<Event> builtinEvents = new ArrayList<Event>();

    Event androidBlockEvent = new Event();
    androidBlockEvent.setTitle("Library Configration");
    androidBlockEvent.setName("androidBlock");
    androidBlockEvent.setDescription("Contains basic defination of your library");
    androidBlockEvent.setEventReplacer("blockCode");
    androidBlockEvent.setRawCode("android {\n" + RawCodeReplacer.getReplacer("blockCode") + "\n}");
    androidBlockEvent.setEnableEdit(true);
    androidBlockEvent.setEnableRootBlocksDrag(false);
    androidBlockEvent.setEnableRootBlocksValueEditing(false);

    Event dependenciesBlockEvent = new Event();
    dependenciesBlockEvent.setTitle("Library dependencies");
    dependenciesBlockEvent.setName("dependenciesBlock");
    dependenciesBlockEvent.setDescription("Contains library used by your app");
    dependenciesBlockEvent.setEventReplacer("blockCode");
    dependenciesBlockEvent.setRawCode(
        "dependencies {\n" + RawCodeReplacer.getReplacer("blockCode") + "\n}");
    dependenciesBlockEvent.setEnableEdit(true);
    dependenciesBlockEvent.setEnableRootBlocksDrag(false);
    dependenciesBlockEvent.setEnableRootBlocksValueEditing(false);

    builtinEvents.add(androidBlockEvent);
    builtinEvents.add(dependenciesBlockEvent);

    libraryModuleGradleFile.setDefaultBuiltInEvents(builtinEvents);

    return libraryModuleGradleFile;
  }

  private static EventHolder getGradleEventHolder() {
    EventHolder eventHolder = new EventHolder();
    eventHolder.setBuiltInEvents(true);
    eventHolder.setHolderName("Config");
    eventHolder.setIcon(R.drawable.ic_gradle);
    eventHolder.setDisableNewEvents(true);
    return eventHolder;
  }

  public static void installNewModule(
      File projectRootDirectory, FileModel module, File moduleFolderRootDir) {
    File gradleDir = EnvironmentUtils.getGradleDirectory(projectRootDirectory);

    if (!gradleDir.exists()) gradleDir.mkdirs();

    if (!moduleFolderRootDir.exists()) moduleFolderRootDir.mkdirs();

    File moduleFolderDir = new File(moduleFolderRootDir, module.getName());

    if (!moduleFolderDir.exists()) moduleFolderDir.mkdirs();

    File moduleFileModel = new File(moduleFolderDir, EnvironmentUtils.FILE_MODEL);

    SerializerUtil.serialize(
        module,
        moduleFileModel,
        new SerializerUtil.SerializerCompletionListener() {

          @Override
          public void onSerializeComplete() {}

          @Override
          public void onFailedToSerialize(Exception exception) {}
        });

    File moduleFolderFilesDir = new File(moduleFolderDir, EnvironmentUtils.FILES);

    if (!moduleFolderFilesDir.exists()) moduleFolderFilesDir.mkdirs();

    File buildGradleDir = new File(moduleFolderFilesDir, EnvironmentUtils.GRADLE_FILE);

    if (!buildGradleDir.exists()) buildGradleDir.mkdirs();

    File buildGradleFileModel = new File(buildGradleDir, EnvironmentUtils.FILE_MODEL);

    FileModel buildGradleFile = null;

    if (module.isAndroidAppModule()) {
      buildGradleFile = getAppModuleGradleFileModule();
    } else {
      buildGradleFile = getLibraryModuleGradleFileModule();
    }

    if (!buildGradleFileModel.exists()) {
      SerializerUtil.serialize(
          buildGradleFile,
          buildGradleFileModel,
          new SerializerUtil.SerializerCompletionListener() {

            @Override
            public void onSerializeComplete() {}

            @Override
            public void onFailedToSerialize(Exception exception) {}
          });
    }

    File buildGradleFileEventsDir = new File(buildGradleDir, EnvironmentUtils.EVENTS_DIR);
    if (buildGradleFileEventsDir.exists()) buildGradleFileEventsDir.mkdirs();

    File buildGradleConfigEventsDir =
        new File(buildGradleFileEventsDir, EnvironmentUtils.APP_GRADLE_CONFIG_EVENT_HOLDER);

    if (!buildGradleConfigEventsDir.exists()) buildGradleConfigEventsDir.mkdirs();

    File buildGradleConfigEventsHolder =
        new File(buildGradleConfigEventsDir, EnvironmentUtils.EVENTS_HOLDER);

    if (!buildGradleConfigEventsHolder.exists()) {
      SerializerUtil.serialize(
          getGradleEventHolder(),
          buildGradleConfigEventsHolder,
          new SerializerUtil.SerializerCompletionListener() {
            @Override
            public void onSerializeComplete() {}

            @Override
            public void onFailedToSerialize(Exception exception) {}
          });
    }

    File configEventsDir = new File(buildGradleConfigEventsDir, EnvironmentUtils.EVENTS_DIR);

    if (!configEventsDir.exists()) configEventsDir.mkdirs();

    EventUtils.installEvents(buildGradleFile.getDefaultBuiltInEvents(), configEventsDir);
  }

  public static void createGradleFilesIfDoNotExists(File projectRootDirectory) {
    FileModel moduleFolder = FileModelUtils.getFolderModel("app");
    moduleFolder.setAndroidAppModule(true);
    installNewModule(
        projectRootDirectory,
        moduleFolder,
        EnvironmentUtils.getGradleDirectory(projectRootDirectory));
  }

  public static void createLibraryModule(
      File projectRootDirectory, File rootDirToInstall, String moduleName) {
    FileModel moduleFolder = FileModelUtils.getFolderModel(moduleName);
    moduleFolder.setAndroidLibrary(true);
    installNewModule(projectRootDirectory, moduleFolder, rootDirToInstall);
  }

  public static boolean canCreateModule(File rootDir, String moduleName) {
    if (!rootDir.exists()) return true;

    File moduleDir = new File(rootDir, moduleName);

    if (moduleDir.exists()) return false;

    ArrayList<FileModel> files = FileModelUtils.getFileModelList(rootDir);
    for (int position = 0; position < files.size(); ++position) {
      if (files.get(position).isFolder()) {
        if (files.get(position).getName().equals(moduleName)) return false;
      }
    }

    return true;
  }
}
