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

import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.model.EventGroupModel;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.builtin.events.GradleBuiltInEvents;
import com.icst.android.appstudio.models.EventHolder;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;

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
