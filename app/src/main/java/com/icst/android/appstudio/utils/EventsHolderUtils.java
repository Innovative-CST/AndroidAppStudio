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

import com.icst.android.appstudio.models.EventHolder;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;

public class EventsHolderUtils {
	public static ArrayList<EventHolder> getEventHolder(File eventDir) {
		ArrayList<EventHolder> events = new ArrayList<EventHolder>();

		if (!eventDir.exists())
			return events;

		for (File path : eventDir.listFiles()) {
			DeserializerUtils.deserialize(
					new File(path, EnvironmentUtils.EVENTS_HOLDER),
					new DeserializerUtils.DeserializerListener() {

						@Override
						public void onSuccessfullyDeserialized(Object object) {
							if (object instanceof EventHolder) {
								((EventHolder) object).setFilePath(path);
								events.add((EventHolder) object);
							}
						}

						@Override
						public void onFailed(int errorCode, Exception e) {
						}
					});
		}
		return events;
	}

	private static final ArrayList<EventHolder> getAllEventHolders() {
		ArrayList<EventHolder> holders = new ArrayList<EventHolder>();
		holders.addAll(ExtensionUtils.extractEventHoldersFromExtensions());
		return holders;
	}

	public static final EventHolder getEventHolderByName(String name) {
		ArrayList<EventHolder> holders = getAllEventHolders();
		for (int i = 0; i < holders.size(); ++i) {
			if (holders.get(i) != null) {
				if (holders.get(i).getHolderName().equals(name)) {
					return holders.get(i);
				}
			}
		}

		return null;
	}
}
