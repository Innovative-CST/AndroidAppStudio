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

package com.icst.android.appstudio.builtin.events;

import java.util.ArrayList;

import com.icst.android.appstudio.block.model.BlockFieldLayerModel;
import com.icst.android.appstudio.block.model.BlockFieldModel;
import com.icst.android.appstudio.block.model.BlockLayerModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;
import com.icst.android.appstudio.builtin.blocks.GradleDepedencyBlocks;

public class GradleBuiltInEvents {

	public static ArrayList<Event> getAllGradleEvents() {
		ArrayList<Event> output = new ArrayList<Event>();

		output.add(getAppModuleAndroidBlockEvent());
		output.add(getAppModuleDependenciesBlockEvent());
		output.add(getLibraryModuleAndroidBlockEvent());
		output.add(getLibraryModuleDependenciesBlockEvent());

		return output;
	}

	/*
	 * App configration event of app module build.gradle file.
	 */
	public static Event getAppModuleAndroidBlockEvent() {
		Event androidBlockEvent = new Event();
		androidBlockEvent.setTitle("App Configration");
		androidBlockEvent.setName("androidBlock");
		androidBlockEvent.setDescription("Contains basic defination of your app");
		androidBlockEvent.setExtension(new String[] { "gradle" });
		androidBlockEvent.setEventReplacer("blockCode");
		androidBlockEvent.setEventReplacerKey("AppModuleAndroidBlockEventCode");
		androidBlockEvent.setRawCode(
				"android {\n\t"
						+ RawCodeReplacer.getReplacer(
								androidBlockEvent.getEventReplacerKey(), androidBlockEvent.getEventReplacer())
						+ "\n}");
		androidBlockEvent.setEnableEdit(true);
		androidBlockEvent.setEnableRootBlocksDrag(true);
		androidBlockEvent.setEnableRootBlocksValueEditing(true);

		BlockModel androidBlockEventBlockModel = new BlockModel();
		androidBlockEventBlockModel.setColor("#884400");
		androidBlockEventBlockModel.setFirstBlock(true);
		androidBlockEventBlockModel.setBlockType(BlockModel.Type.defaultBlock);

		ArrayList<BlockLayerModel> androidBlockEventBlockLayerModel = new ArrayList<BlockLayerModel>();

		BlockFieldLayerModel androidBlockEventTextLayer = new BlockFieldLayerModel();

		ArrayList<BlockFieldModel> defineTextLayerContent = new ArrayList<BlockFieldModel>();
		BlockFieldModel defineEventText = new BlockFieldModel();
		defineEventText.setValue("Your app configration");

		defineTextLayerContent.add(defineEventText);
		androidBlockEventTextLayer.setBlockFields(defineTextLayerContent);

		androidBlockEventBlockLayerModel.add(androidBlockEventTextLayer);
		androidBlockEventBlockModel.setBlockLayerModel(androidBlockEventBlockLayerModel);

		androidBlockEvent.setEventTopBlock(androidBlockEventBlockModel);
		androidBlockEvent.setEnableRootBlocksValueEditing(false);

		ArrayList<BlockModel> defaultInstalledBlocks = new ArrayList<BlockModel>();
		defaultInstalledBlocks.add(GradleDepedencyBlocks.getDefaultConfigBlock(false));
		androidBlockEvent.setBlockModels(defaultInstalledBlocks);
		return androidBlockEvent;
	}

	/*
	 * App Libraries event of app module build.gradle file.
	 */
	public static Event getAppModuleDependenciesBlockEvent() {
		Event dependenciesBlockEvent = new Event();
		dependenciesBlockEvent.setTitle("App Libraries");
		dependenciesBlockEvent.setName("dependenciesBlock");
		dependenciesBlockEvent.setDescription("Contains library used by your app");
		dependenciesBlockEvent.setExtension(new String[] { "gradle" });
		dependenciesBlockEvent.setEventReplacer("blockCode");
		dependenciesBlockEvent.setEventReplacerKey("AppModuleDependenciesBlockEventCode");
		dependenciesBlockEvent.setRawCode(
				"dependencies {\n\t"
						+ RawCodeReplacer.getReplacer(
								dependenciesBlockEvent.getEventReplacerKey(),
								dependenciesBlockEvent.getEventReplacer())
						+ "\n}");
		dependenciesBlockEvent.setEnableEdit(true);
		dependenciesBlockEvent.setEnableRootBlocksDrag(true);
		dependenciesBlockEvent.setEnableRootBlocksValueEditing(true);

		BlockModel defineEvent = new BlockModel();
		defineEvent.setBlockType(BlockModel.Type.defaultBlock);
		defineEvent.setColor("#CCEDA4");
		defineEvent.setFirstBlock(true);

		ArrayList<BlockLayerModel> blockLayerModels = new ArrayList<BlockLayerModel>();

		BlockFieldLayerModel eventDefinationLayer = new BlockFieldLayerModel();

		ArrayList<BlockFieldModel> defineDependenciesTextLayer = new ArrayList<BlockFieldModel>();

		BlockFieldModel defineDependenciesText = new BlockFieldModel();
		defineDependenciesText.setValue("Define the libraries of your app");

		defineDependenciesTextLayer.add(defineDependenciesText);
		eventDefinationLayer.setBlockFields(defineDependenciesTextLayer);

		blockLayerModels.add(eventDefinationLayer);
		defineEvent.setBlockLayerModel(blockLayerModels);
		dependenciesBlockEvent.setEventTopBlock(defineEvent);

		BlockModel materialLibraryImplementationBlock = GradleDepedencyBlocks.getImplementationBlock(
				false, false, "com.google.android.material:material:1.12.0-alpha03");
		BlockModel appCompatImplementationBlock = GradleDepedencyBlocks.getImplementationBlock(
				false, false, "androidx.appcompat:appcompat:1.6.1");

		ArrayList<BlockModel> builtInDepedency = new ArrayList<BlockModel>();
		builtInDepedency.add(materialLibraryImplementationBlock);
		builtInDepedency.add(appCompatImplementationBlock);
		dependenciesBlockEvent.setBlockModels(builtInDepedency);
		return dependenciesBlockEvent;
	}

	/*
	 * Library Configration event of library module build.gradle file.
	 */
	public static Event getLibraryModuleAndroidBlockEvent() {
		Event androidBlockEvent = new Event();
		androidBlockEvent.setTitle("Library Configration");
		androidBlockEvent.setName("androidBlock");
		androidBlockEvent.setDescription("Contains basic defination of your library");
		androidBlockEvent.setExtension(new String[] { "gradle" });
		androidBlockEvent.setEventReplacer("blockCode");
		androidBlockEvent.setEventReplacerKey("LibraryModuleAndroidBlockEventCode");
		androidBlockEvent.setRawCode(
				"android {\n\t"
						+ RawCodeReplacer.getReplacer(
								androidBlockEvent.getEventReplacerKey(), androidBlockEvent.getEventReplacer())
						+ "\n}");
		androidBlockEvent.setEnableEdit(true);
		androidBlockEvent.setEnableRootBlocksDrag(true);
		androidBlockEvent.setEnableRootBlocksValueEditing(true);

		return androidBlockEvent;
	}

	/*
	 * Dependencies event of library module build.gradle file.
	 */
	public static Event getLibraryModuleDependenciesBlockEvent() {
		Event dependenciesBlockEvent = new Event();
		dependenciesBlockEvent.setTitle("Library dependencies");
		dependenciesBlockEvent.setName("dependenciesBlock");
		dependenciesBlockEvent.setDescription("Contains library used by your app");
		dependenciesBlockEvent.setExtension(new String[] { "gradle" });
		dependenciesBlockEvent.setEventReplacer("blockCode");
		dependenciesBlockEvent.setEventReplacerKey("LibraryModuleDependenciesBlockEventCode");
		dependenciesBlockEvent.setRawCode(
				"dependencies {\n\t"
						+ RawCodeReplacer.getReplacer(
								dependenciesBlockEvent.getEventReplacerKey(),
								dependenciesBlockEvent.getEventReplacer())
						+ "\n}");
		dependenciesBlockEvent.setEnableEdit(true);
		dependenciesBlockEvent.setEnableRootBlocksDrag(true);
		dependenciesBlockEvent.setEnableRootBlocksValueEditing(true);

		return dependenciesBlockEvent;
	}
}
