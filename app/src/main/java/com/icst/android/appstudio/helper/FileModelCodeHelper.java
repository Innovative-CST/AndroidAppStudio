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

package com.icst.android.appstudio.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.model.EventGroupModel;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.model.JavaFileModel;
import com.icst.android.appstudio.block.model.VariableModel;
import com.icst.android.appstudio.block.tag.DependencyTag;
import com.icst.android.appstudio.models.EventHolder;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.models.ProjectModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.EventUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;

public class FileModelCodeHelper {
	private File eventsDirectory;
	private FileModel fileModel;
	private File projectRootDirectory;
	private ModuleModel module;

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

	public ModuleModel getModule() {
		return this.module;
	}

	public void setModule(ModuleModel module) {
		this.module = module;
	}

	public String getCode() {
		ArrayList<Object> builtInEvents = null;
		ArrayList<Object> dirEvents = new ArrayList<Object>();

		if (fileModel == null)
			return null;
		if (eventsDirectory == null)
			return null;
		if (eventsDirectory.isFile())
			return null;

		if (eventsDirectory.exists()) {

			for (File file : eventsDirectory.listFiles()) {
				if (file.isFile())
					continue;

				File eventsHolderFile = new File(file, EnvironmentUtils.EVENTS_HOLDER);

				if (!eventsHolderFile.exists())
					continue;

				EventHolder holder = null;

				Object deserializedObject = DeserializerUtils.deserialize(eventsHolderFile);

				if (deserializedObject == null)
					continue;

				if (!(deserializedObject instanceof EventHolder))
					continue;

				holder = (EventHolder) deserializedObject;

				if (fileModel.getBuiltInEventsName().equals(holder.getHolderName())) {
					builtInEvents = EventUtils.getEventsObject(new File(file, EnvironmentUtils.EVENTS_DIR));
				} else {
					ArrayList<Object> extraEvents = EventUtils
							.getEventsObject(new File(file, EnvironmentUtils.EVENTS_DIR));

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
					public void onFailed(int errorCode, Exception e) {
					}
				});

		return fileModel.getCode(builtInEvents, dirEvents, variables);
	}

	public String getCode(String packageName, File instanceVariablesDir, File staticVariableDir) {
		ArrayList<Object> builtInEvents = null;
		ArrayList<Object> dirEvents = new ArrayList<Object>();

		if (fileModel == null)
			return null;
		if (eventsDirectory == null)
			return null;
		if (eventsDirectory.isFile())
			return null;

		if (eventsDirectory.exists()) {

			for (File file : eventsDirectory.listFiles()) {
				if (file.isFile())
					continue;

				File eventsHolderFile = new File(file, EnvironmentUtils.EVENTS_HOLDER);

				if (!eventsHolderFile.exists())
					continue;

				EventHolder holder = null;

				Object deserializedObject = DeserializerUtils.deserialize(eventsHolderFile);

				if (deserializedObject == null)
					continue;

				if (!(deserializedObject instanceof EventHolder))
					continue;

				holder = (EventHolder) deserializedObject;

				if (fileModel.getBuiltInEventsName() != null) {
					if (fileModel.getBuiltInEventsName().equals(holder.getHolderName())) {
						builtInEvents = EventUtils.getEventsObject(new File(file, EnvironmentUtils.EVENTS_DIR));
					}
				} else {
					ArrayList<Object> extraEvents = EventUtils
							.getEventsObject(new File(file, EnvironmentUtils.EVENTS_DIR));

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
					public void onFailed(int errorCode, Exception e) {
					}
				});

		ArrayList<VariableModel> instanceVariables = DeserializerUtils.deserialize(instanceVariablesDir,
				ArrayList.class);

		ArrayList<VariableModel> staticVariables = DeserializerUtils.deserialize(staticVariableDir, ArrayList.class);

		return ((JavaFileModel) fileModel)
				.getCode(
						packageName,
						new File(new File(module.resourceDirectory, EnvironmentUtils.FILES), "layout"),
						builtInEvents,
						dirEvents,
						instanceVariables,
						staticVariables,
						variables);
	}

	public ArrayList<DependencyTag> getUsedDependency() {
		ArrayList<Object> builtInEvents = null;
		ArrayList<Object> dirEvents = new ArrayList<Object>();

		if (fileModel == null)
			return null;
		if (eventsDirectory == null)
			return null;
		if (eventsDirectory.isFile())
			return null;

		if (eventsDirectory.exists()) {

			for (File file : eventsDirectory.listFiles()) {
				if (file.isFile())
					continue;

				File eventsHolderFile = new File(file, EnvironmentUtils.EVENTS_HOLDER);

				if (!eventsHolderFile.exists())
					continue;

				EventHolder holder = null;

				Object deserializedObject = DeserializerUtils.deserialize(eventsHolderFile);

				if (deserializedObject == null)
					continue;

				if (!(deserializedObject instanceof EventHolder))
					continue;

				holder = (EventHolder) deserializedObject;

				if (fileModel.getBuiltInEventsName() != null) {
					if (fileModel.getBuiltInEventsName().equals(holder.getHolderName())) {
						builtInEvents = EventUtils.getEventsObject(new File(file, EnvironmentUtils.EVENTS_DIR));
					}
				} else {
					ArrayList<Object> extraEvents = EventUtils
							.getEventsObject(new File(file, EnvironmentUtils.EVENTS_DIR));

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

		return ((JavaFileModel) fileModel).getUsedDependency(builtInEvents, dirEvents);
	}
}
