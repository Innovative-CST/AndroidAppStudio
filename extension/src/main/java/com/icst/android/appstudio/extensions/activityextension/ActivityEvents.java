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

package com.icst.android.appstudio.extensions.activityextension;

import com.icst.android.appstudio.ImageUtils;
import com.icst.android.appstudio.MethodEventUtils;
import com.icst.android.appstudio.block.model.BlockFieldLayerModel;
import com.icst.android.appstudio.block.model.BlockFieldModel;
import com.icst.android.appstudio.block.model.BlockLayerModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.tag.AdditionalCodeHelperTag;
import com.icst.android.appstudio.block.tag.ImportTag;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;
import java.io.IOException;
import java.util.ArrayList;

public class ActivityEvents {
  public static ArrayList<Event> getAllEvents() {
    ArrayList<Event> output = new ArrayList<Event>();

    output.add(getOnCreateEvent());
    output.add(getOnDestroyEvent());
    output.add(getOnBackPressedEvent());
    output.add(getOnNightModeChangedEvent());

    return output;
  }

  public static Event getOnBackPressedEvent() {
    BlockModel defineEvent = new BlockModel();
    defineEvent.setBlockType(BlockModel.Type.defaultBlock);
    defineEvent.setColor("#C88330");
    defineEvent.setFirstBlock(true);

    ArrayList<BlockLayerModel> blockLayerModels = new ArrayList<BlockLayerModel>();

    BlockFieldLayerModel eventDefinationLayer = new BlockFieldLayerModel();

    ArrayList<BlockFieldModel> eventDefinationLayerTextLayer = new ArrayList<BlockFieldModel>();

    BlockFieldModel defineEventText = new BlockFieldModel();
    defineEventText.setValue("onBackPressed");

    eventDefinationLayerTextLayer.add(defineEventText);
    eventDefinationLayer.setBlockFields(eventDefinationLayerTextLayer);

    blockLayerModels.add(eventDefinationLayer);
    defineEvent.setBlockLayerModel(blockLayerModels);
    return MethodEventUtils.buildMethodEvent(
        "onBackPressed",
        "onBackPressed",
        "Executes when back key is pressed",
        "onBackPressedEvent",
        "onBackPressedEventCode",
        "Activity",
        "images/android.png",
        "protected",
        "void",
        "onBackPressed",
        null,
        new String[] {"AppCompatActivity"},
        new String[] {"androidx.appcompat.app.AppCompatActivity"},
        new String[] {"@Override", "@Deprecated"},
        defineEvent,
        null,
        true,
        true,
        true,
        true,
        true,
        false,
        false);
  }

  public static Event getOnNightModeChangedEvent() {
    BlockModel defineEvent = new BlockModel();
    defineEvent.setBlockType(BlockModel.Type.defaultBlock);
    defineEvent.setColor("#C88330");
    defineEvent.setFirstBlock(true);

    ArrayList<BlockLayerModel> blockLayerModels = new ArrayList<BlockLayerModel>();

    BlockFieldLayerModel eventDefinationLayer = new BlockFieldLayerModel();

    ArrayList<BlockFieldModel> eventDefinationLayerTextLayer = new ArrayList<BlockFieldModel>();

    BlockFieldModel defineEventText = new BlockFieldModel();
    defineEventText.setValue("onNightModeChanged");

    eventDefinationLayerTextLayer.add(defineEventText);
    eventDefinationLayer.setBlockFields(eventDefinationLayerTextLayer);

    blockLayerModels.add(eventDefinationLayer);
    defineEvent.setBlockLayerModel(blockLayerModels);
    return MethodEventUtils.buildMethodEvent(
        "onNightModeChanged",
        "onNightModeChanged",
        "Called when the night mode has changed.",
        "onNightModeChangedEvent",
        "onNightModeChangedEventCode",
        "Activity",
        "images/android.png",
        "protected",
        "void",
        "onNightModeChanged",
        null,
        new String[] {"AppCompatActivity"},
        new String[] {"androidx.appcompat.app.AppCompatActivity"},
        new String[] {"@Override"},
        defineEvent,
        null,
        true,
        true,
        true,
        true,
        true,
        false,
        false);
  }

  public static Event getOnCreateEvent() {
    Event event = new Event();
    event.setTitle("onCreate");
    event.setName("onCreate");
    event.setDescription("Executes when activity is created");
    event.setEventReplacer("onCreateEvent");
    event.setDirectFileEvent(true);
    event.setEventReplacerKey("onCreateEventCode");
    event.setRawCode(
        "@Override\npublic Bundle onCreate(Bundle savedInstanceState) {\n\t"
            + RawCodeReplacer.getReplacer(event.getEventReplacerKey(), event.getEventReplacer())
            + "\n}");
    event.setEnableEdit(true);
    event.setCreateInHolderName("Activity");
    event.setClasses(new String[] {"AppCompatActivity"});
    event.setClassesImports(new String[] {"androidx.appcompat.app.AppCompatActivity"});
    event.setEnableRootBlocksDrag(true);
    event.setEnableRootBlocksValueEditing(true);
    try {
      event.setIcon(ImageUtils.convertImageToByteArray("images/android.png"));
      event.setApplyColorFilter(true);
    } catch (IOException e) {
      e.printStackTrace();
    }

    ImportTag bundleImport = new ImportTag();
    bundleImport.setImportClass("android.os.Bundle");
    event.setAdditionalTags(new AdditionalCodeHelperTag[] {bundleImport});

    BlockModel defineEvent = new BlockModel();
    defineEvent.setBlockType(BlockModel.Type.defaultBlock);
    defineEvent.setColor("#C88330");
    defineEvent.setFirstBlock(true);

    ArrayList<BlockLayerModel> blockLayerModels = new ArrayList<BlockLayerModel>();

    BlockFieldLayerModel eventDefinationLayer = new BlockFieldLayerModel();

    ArrayList<BlockFieldModel> eventDefinationLayerTextLayer = new ArrayList<BlockFieldModel>();

    BlockFieldModel defineEventText = new BlockFieldModel();
    defineEventText.setValue("onActivityCreate");

    eventDefinationLayerTextLayer.add(defineEventText);

    BlockModel bundle = new BlockModel();
    bundle.setColor("#5533ff");
    bundle.setRawCode("savedInstanceState");
    bundle.setReturns(new String[] {"android.os.Bundle"});
    bundle.setDragAllowed(true);
    bundle.setBlockType(BlockModel.Type.variable);

    ArrayList<BlockLayerModel> bundleBlockLayerModels = new ArrayList<BlockLayerModel>();

    BlockFieldLayerModel bundleBlockDefinationLayer = new BlockFieldLayerModel();

    ArrayList<BlockFieldModel> bundleBlockDefinationLayerTextLayer =
        new ArrayList<BlockFieldModel>();

    BlockFieldModel bundleText = new BlockFieldModel();
    bundleText.setValue("bundle");

    bundleBlockDefinationLayerTextLayer.add(bundleText);

    bundleBlockDefinationLayer.setBlockFields(bundleBlockDefinationLayerTextLayer);
    bundleBlockLayerModels.add(bundleBlockDefinationLayer);
    bundle.setBlockLayerModel(bundleBlockLayerModels);

    eventDefinationLayerTextLayer.add(bundle);
    eventDefinationLayer.setBlockFields(eventDefinationLayerTextLayer);

    blockLayerModels.add(eventDefinationLayer);
    defineEvent.setBlockLayerModel(blockLayerModels);
    event.setEventTopBlock(defineEvent);
    return event;
  }

  public static Event getOnDestroyEvent() {
    Event event = new Event();
    event.setTitle("onDestroy");
    event.setName("onDestroy");
    event.setDescription("Executes when activity is destroyed");
    event.setEventReplacer("onDestroyEvent");
    event.setDirectFileEvent(true);
    event.setEventReplacerKey("onDestroyEventCode");
    event.setRawCode(
        "@Override\npublic void onDestroy() {\n\t"
            + RawCodeReplacer.getReplacer(event.getEventReplacerKey(), event.getEventReplacer())
            + "\n}");
    event.setEnableEdit(true);
    event.setCreateInHolderName("Activity");
    event.setClasses(new String[] {"AppCompatActivity"});
    event.setClassesImports(new String[] {"androidx.appcompat.app.AppCompatActivity"});
    event.setEnableRootBlocksDrag(true);
    event.setEnableRootBlocksValueEditing(true);
    try {
      event.setIcon(ImageUtils.convertImageToByteArray("images/android.png"));
      event.setApplyColorFilter(true);
    } catch (IOException e) {
      e.printStackTrace();
    }

    BlockModel defineEvent = new BlockModel();
    defineEvent.setBlockType(BlockModel.Type.defaultBlock);
    defineEvent.setColor("#C88330");
    defineEvent.setFirstBlock(true);

    ArrayList<BlockLayerModel> blockLayerModels = new ArrayList<BlockLayerModel>();

    BlockFieldLayerModel eventDefinationLayer = new BlockFieldLayerModel();

    ArrayList<BlockFieldModel> eventDefinationLayerTextLayer = new ArrayList<BlockFieldModel>();

    BlockFieldModel defineEventText = new BlockFieldModel();
    defineEventText.setValue("onActivityDestroy");

    eventDefinationLayerTextLayer.add(defineEventText);
    eventDefinationLayer.setBlockFields(eventDefinationLayerTextLayer);

    blockLayerModels.add(eventDefinationLayer);
    defineEvent.setBlockLayerModel(blockLayerModels);
    event.setEventTopBlock(defineEvent);
    return event;
  }
}
