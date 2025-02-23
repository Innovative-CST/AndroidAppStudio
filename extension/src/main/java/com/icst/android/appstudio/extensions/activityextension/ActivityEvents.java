/*
 *  This file is part of AndroidAppStudio.
 *
 *  AndroidAppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidAppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidAppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.android.appstudio.extensions.activityextension;

import java.io.IOException;
import java.util.ArrayList;

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
				new String[] { "AppCompatActivity" },
				new String[] { "androidx.appcompat.app.AppCompatActivity" },
				new String[] { "@Override", "@Deprecated" },
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
				new String[] { "AppCompatActivity" },
				new String[] { "androidx.appcompat.app.AppCompatActivity" },
				new String[] { "@Override" },
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
				"@Override\npublic void onCreate(Bundle savedInstanceState) {\n\tsuper.onCreate(savedInstanceState);\n\t"
						+ RawCodeReplacer.getReplacer(event.getEventReplacerKey(), event.getEventReplacer())
						+ "\n}");
		event.setEnableEdit(true);
		event.setCreateInHolderName("Activity");
		event.setClasses(new String[] { "AppCompatActivity" });
		event.setClassesImports(new String[] { "androidx.appcompat.app.AppCompatActivity" });
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
		event.setAdditionalTags(new AdditionalCodeHelperTag[] { bundleImport });

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
		bundle.setReturns(new String[] { "android.os.Bundle" });
		bundle.setDragAllowed(true);
		bundle.setBlockType(BlockModel.Type.variable);

		ArrayList<BlockLayerModel> bundleBlockLayerModels = new ArrayList<BlockLayerModel>();

		BlockFieldLayerModel bundleBlockDefinationLayer = new BlockFieldLayerModel();

		ArrayList<BlockFieldModel> bundleBlockDefinationLayerTextLayer = new ArrayList<BlockFieldModel>();

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
		event.setClasses(new String[] { "AppCompatActivity" });
		event.setClassesImports(new String[] { "androidx.appcompat.app.AppCompatActivity" });
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
