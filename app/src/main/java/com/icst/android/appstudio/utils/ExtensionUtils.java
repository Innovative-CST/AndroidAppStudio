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

package com.icst.android.appstudio.utils;

import java.io.File;
import java.util.ArrayList;

import com.icst.android.appstudio.block.model.BlockHolderModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.model.VariableModel;
import com.icst.android.appstudio.models.EventHolder;
import com.icst.android.appstudio.models.ExtensionBundle;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;

public final class ExtensionUtils {
	public static ArrayList<ExtensionBundle> getInstalledExtensions() {
		ArrayList<ExtensionBundle> extensions = new ArrayList<ExtensionBundle>();

		if (!EnvironmentUtils.EXTENSION_DIR.exists()) {
			return extensions;
		}

		for (File file : EnvironmentUtils.EXTENSION_DIR.listFiles()) {
			ExtensionBundle extension = DeserializerUtils.deserialize(file, ExtensionBundle.class);
			if (extension != null) {
				extensions.add(extension);
			}
		}

		return extensions;
	}

	public static ArrayList<Event> extractEventsFromExtensions() {
		ArrayList<Event> events = new ArrayList<Event>();
		ArrayList<ExtensionBundle> extensions = getInstalledExtensions();

		for (int i = 0; i < extensions.size(); ++i) {
			if (extensions.get(i).getEvents() != null) {
				events.addAll(extensions.get(i).getEvents());
			}
		}

		return events;
	}

	public static ArrayList<BlockHolderModel> extractBlockHoldersFromExtensions() {
		ArrayList<BlockHolderModel> holder = new ArrayList<BlockHolderModel>();
		ArrayList<ExtensionBundle> extensions = getInstalledExtensions();

		for (int i = 0; i < extensions.size(); ++i) {
			if (extensions.get(i).getHolders() != null) {
				holder.addAll(extensions.get(i).getHolders());
			}
		}

		return holder;
	}

	public static ArrayList<EventHolder> extractEventHoldersFromExtensions() {
		ArrayList<EventHolder> holder = new ArrayList<EventHolder>();
		ArrayList<ExtensionBundle> extensions = getInstalledExtensions();

		for (int i = 0; i < extensions.size(); ++i) {
			if (extensions.get(i).getEventHolders() != null) {
				holder.addAll(extensions.get(i).getEventHolders());
			}
		}

		return holder;
	}

	public static ArrayList<BlockModel> extractBlocksFromExtensions() {
		ArrayList<BlockModel> blocks = new ArrayList<BlockModel>();
		ArrayList<ExtensionBundle> extensions = getInstalledExtensions();

		for (int i = 0; i < extensions.size(); ++i) {
			if (extensions.get(i).getBlocks() != null) {
				blocks.addAll(extensions.get(i).getBlocks());
			}
		}

		return blocks;
	}

	public static ArrayList<VariableModel> extractVariablesFromExtensions() {
		ArrayList<VariableModel> holders = new ArrayList<VariableModel>();
		ArrayList<ExtensionBundle> extensions = getInstalledExtensions();

		for (int i = 0; i < extensions.size(); ++i) {
			if (extensions.get(i).getVariables() != null) {
				holders.addAll(extensions.get(i).getVariables());
			}
		}

		return holders;
	}
}
