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

package com.icst.android.appstudio.utils;

import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.model.EventGroupModel;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.builtin.events.GradleBuiltInEvents;
import com.icst.android.appstudio.models.EventHolder;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;
import java.io.File;
import java.util.ArrayList;

public class EventUtils {
	public static ArrayList<Object> getEvents(File file) {
		ArrayList<Object> events = new ArrayList<Object>();

		if (!file.exists())
			return events;

		for (File path : file.listFiles()) {
			DeserializerUtils.deserialize(
					path,
					new DeserializerUtils.DeserializerListener() {

						@Override
						public void onSuccessfullyDeserialized(Object object) {
							if (object instanceof Event) {
								events.add((Event) object);
							} else if (object instanceof EventGroupModel) {
								events.add((EventGroupModel) object);
							}
						}

						@Override
						public void onFailed(int errorCode, Exception e) {
						}
					});
		}
		return events;
	}

	public static ArrayList<Object> getEventsObject(File file) {
		ArrayList<Object> events = new ArrayList<Object>();

		if (!file.exists())
			return events;

		for (File path : file.listFiles()) {
			DeserializerUtils.deserialize(
					path,
					new DeserializerUtils.DeserializerListener() {

						@Override
						public void onSuccessfullyDeserialized(Object object) {
							if (object instanceof Event) {
								events.add((Event) object);
							} else if (object instanceof EventGroupModel) {
								events.add((EventGroupModel) object);
							}
						}

						@Override
						public void onFailed(int errorCode, Exception e) {
						}
					});
		}
		return events;
	}

	public static void installEvents(ArrayList<Object> events, File directory) {
		for (int position = 0; position < events.size(); ++position) {
			Object event = null;
			File eventPath = null;
			if (events.get(position) instanceof Event) {
				event = ((Event) events.get(position)).clone();
				eventPath = new File(directory, ((Event) events.get(position)).getName());
			} else if (events.get(position) instanceof EventGroupModel) {
				event = ((EventGroupModel) events.get(position)).clone();
				eventPath = new File(directory, ((EventGroupModel) events.get(position)).getTitle());
			}
			if (event == null)
				continue;
			if (eventPath == null)
				continue;

			if (eventPath.exists())
				continue;
			SerializerUtil.serialize(
					event,
					eventPath,
					new SerializerUtil.SerializerCompletionListener() {

						@Override
						public void onSerializeComplete() {
						}

						@Override
						public void onFailedToSerialize(Exception exception) {
						}
					});
		}
	}

	public static void installEvents(ArrayList<Event> events, File holdersDir, boolean createHolder) {
		for (int position = 0; position < events.size(); ++position) {
			Event event = (events.get(position)).clone();
			File eventHolderDirectory = new File(holdersDir, events.get(position).getCreateInHolderName());
			if (event == null)
				continue;
			if (!eventHolderDirectory.exists())
				eventHolderDirectory.mkdirs();

			EventHolder holder = EventsHolderUtils.getEventHolderByName(event.getCreateInHolderName());

			if (holder == null)
				continue;

			SerializerUtil.serialize(
					holder,
					new File(eventHolderDirectory, EnvironmentUtils.EVENTS_HOLDER),
					new SerializerUtil.SerializerCompletionListener() {

						@Override
						public void onSerializeComplete() {
						}

						@Override
						public void onFailedToSerialize(Exception exception) {
						}
					});
			if (!new File(eventHolderDirectory, EnvironmentUtils.EVENTS_DIR).exists())
				new File(eventHolderDirectory, EnvironmentUtils.EVENTS_DIR).mkdirs();

			SerializerUtil.serialize(
					event,
					new File(new File(eventHolderDirectory, EnvironmentUtils.EVENTS_DIR), event.getName()),
					new SerializerUtil.SerializerCompletionListener() {

						@Override
						public void onSerializeComplete() {
						}

						@Override
						public void onFailedToSerialize(Exception exception) {
						}
					});
		}
	}

	public static ArrayList<Event> getAllEvents(ArrayList<Event> additionalEvent) {
		ArrayList<Event> output = new ArrayList<Event>();

		if (additionalEvent != null) {
			output.addAll(additionalEvent);
		}
		output.addAll(GradleBuiltInEvents.getAllGradleEvents());
		output.addAll(ExtensionUtils.extractEventsFromExtensions());

		return output;
	}

	public static ArrayList<Event> getAllEventsFromHolders(File eventsDir) {
		ArrayList<Event> output = new ArrayList<Event>();

		if (!eventsDir.exists()) {
			return output;
		}

		for (File holderDir : eventsDir.listFiles()) {
			if (holderDir.isDirectory() && new File(holderDir, EnvironmentUtils.EVENTS_DIR).exists()) {

				for (File eventFile : new File(holderDir, EnvironmentUtils.EVENTS_DIR).listFiles()) {
					Event event = DeserializerUtils.deserialize(eventFile, Event.class);
					if (event != null) {
						output.add(event);
					}
				}
			}
		}

		return output;
	}

	public static ArrayList<Event> filterEvents(
			ArrayList<Event> additionalEvent,
			ArrayList<Event> removeEvents,
			ArrayList<String> superClasses,
			ArrayList<String> superClassesImports,
			FileModel file) {
		ArrayList<Event> output = new ArrayList<Event>();

		ArrayList<Event> allEvents = getAllEvents(additionalEvent);

		for (int i = 0; i < allEvents.size(); ++i) {
			Event event = allEvents.get(i);
			if (event.getClassesImports() != null) {
				if (!containsString(event.getClassesImports(), superClassesImports)) {
					continue;
				}
			}

			if (event.getClasses() != null) {
				if (!containsString(event.getClasses(), superClasses)) {
					continue;
				}
			}

			if (event.getExtension() != null) {
				if (!containsString(event.getExtension(), file.getFileExtension())) {
					continue;
				}
			}

			boolean removeEvent = false;
			if (removeEvents != null) {
				for (int i2 = 0; i2 < removeEvents.size(); ++i2) {
					if (removeEvents.get(i2).getName().equals(event.getName())) {
						removeEvent = true;
					}
				}
			}

			if (removeEvent) {
				continue;
			}

			output.add(event.clone());
		}

		return output;
	}

	public static boolean containsString(String[] array, ArrayList<String> arrayList) {
		for (int i = 0; i < array.length; ++i) {
			for (int i2 = 0; i2 < arrayList.size(); ++i2) {
				if (array[i].equals(arrayList.get(i2))) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean containsString(String[] array, String str) {
		for (int i = 0; i < array.length; ++i) {
			if (str.equals(array[i])) {
				return true;
			}
		}
		return false;
	}
}
