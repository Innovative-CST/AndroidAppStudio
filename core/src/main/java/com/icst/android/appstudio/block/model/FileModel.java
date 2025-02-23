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

package com.icst.android.appstudio.block.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.icst.android.appstudio.block.tag.AdditionalCodeHelperTag;
import com.icst.android.appstudio.block.tag.DependencyTag;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;

public class FileModel implements Serializable, Cloneable {
	public static final long serialVersionUID = 2L;

	private String fileName;
	private String fileExtension;
	private String replacerKey;
	private String rawCode;
	private String builtInEventsName;
	private ArrayList<Object> defaultBuiltInEvents;
	private boolean isFolder;
	private boolean isAndroidAppModule;
	private boolean isAndroidLibrary;

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExtension() {
		return this.fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getRawCode() {
		return this.rawCode;
	}

	public void setRawCode(String rawCode) {
		this.rawCode = rawCode;
	}

	public ArrayList<Object> getDefaultBuiltInEvents() {
		return this.defaultBuiltInEvents;
	}

	public void setDefaultBuiltInEvents(ArrayList<Object> defaultBuiltInEvents) {
		this.defaultBuiltInEvents = defaultBuiltInEvents;
	}

	public String getBuiltInEventsName() {
		return this.builtInEventsName;
	}

	public void setBuiltInEventsName(String builtInEventsName) {
		this.builtInEventsName = builtInEventsName;
	}

	public boolean isFolder() {
		return this.isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public boolean isAndroidAppModule() {
		return this.isAndroidAppModule;
	}

	public void setAndroidAppModule(boolean isAndroidAppModule) {
		this.isAndroidAppModule = isAndroidAppModule;
	}

	public boolean isAndroidLibrary() {
		return this.isAndroidLibrary;
	}

	public void setAndroidLibrary(boolean isAndroidLibrary) {
		this.isAndroidLibrary = isAndroidLibrary;
	}

	public String getReplacerKey() {
		return this.replacerKey;
	}

	public void setReplacerKey(String replacerKey) {
		this.replacerKey = replacerKey;
	}

	public ArrayList<DependencyTag> getUsedDependency(
			ArrayList<Object> builtInEvents, ArrayList<Object> events) {
		ArrayList<DependencyTag> usedDependency = new ArrayList<DependencyTag>();

		if (builtInEvents != null) {

			for (int eventCount = 0; eventCount < builtInEvents.size(); ++eventCount) {
				if (builtInEvents.get(eventCount) instanceof Event) {
					Event event = (Event) builtInEvents.get(eventCount);

					for (int tags = 0; tags < event.getAdditionalTagsUsed().size(); ++tags) {
						AdditionalCodeHelperTag tag = event.getAdditionalTagsUsed().get(tags);

						if (tag instanceof DependencyTag) {
							usedDependency.add((DependencyTag) tag);
						}
					}
				}
			}
		}

		if (events != null) {
			for (int eventCount = 0; eventCount < events.size(); ++eventCount) {
				if (events.get(eventCount) instanceof Event) {
					Event event = (Event) events.get(eventCount);

					for (int tags = 0; tags < event.getAdditionalTagsUsed().size(); ++tags) {
						AdditionalCodeHelperTag tag = event.getAdditionalTagsUsed().get(tags);

						if (tag instanceof DependencyTag) {
							usedDependency.add((DependencyTag) tag);
						}
					}
				}
			}
		}

		return usedDependency;
	}

	public String getCode(
			ArrayList<Object> builtInEvents,
			ArrayList<Object> events,
			HashMap<String, Object> variables) {
		String resultCode = getRawCode() != null ? new String(getRawCode()) : null;
		if (resultCode == null) {
			return "";
		}

		if (builtInEvents != null) {

			for (int eventCount = 0; eventCount < builtInEvents.size(); ++eventCount) {
				if (builtInEvents.get(eventCount) instanceof Event) {
					Event event = (Event) builtInEvents.get(eventCount);
					resultCode = resultCode.replace(
							RawCodeReplacer.getReplacer(getReplacerKey(), event.getName()),
							event.getCode(variables));
				}
			}
		}

		if (events != null) {
			for (int eventCount = 0; eventCount < events.size(); ++eventCount) {
				if (events.get(eventCount) instanceof Event) {
					Event event = (Event) events.get(eventCount);
					resultCode = resultCode.replace(
							RawCodeReplacer.getReplacer(event.getEventReplacerKey(), event.getName()),
							event.getCode(variables));
				}
			}
		}

		resultCode = resultCode.replace(RawCodeReplacer.getReplacer("$FileName"), getFileName());

		resultCode = RawCodeReplacer.removeAndroidAppStudioString(getReplacerKey(), resultCode);
		return resultCode;
	}

	@Override
	protected FileModel clone() {
		FileModel fileModel = new FileModel();
		fileModel.setFileName(getFileName() != null ? new String(getFileName()) : null);
		fileModel.setFileExtension(getFileExtension() != null ? new String(getFileExtension()) : null);
		fileModel.setBuiltInEventsName(
				getBuiltInEventsName() != null ? new String(getBuiltInEventsName()) : null);

		ArrayList<Object> clonedBuildInEvents = new ArrayList<Object>();
		for (int position = 0; position < getDefaultBuiltInEvents().size(); ++position) {
			if (getDefaultBuiltInEvents().get(position) instanceof Event) {
				clonedBuildInEvents.add(((Event) getDefaultBuiltInEvents().get(position)).clone());
			} else if (getDefaultBuiltInEvents().get(position) instanceof Event) {
				clonedBuildInEvents.add(
						((EventGroupModel) getDefaultBuiltInEvents().get(position)).clone());
			}
		}
		fileModel.setReplacerKey(getReplacerKey() != null ? new String(getReplacerKey()) : null);
		fileModel.setDefaultBuiltInEvents(clonedBuildInEvents);
		fileModel.setRawCode(getRawCode() != null ? new String(getRawCode()) : null);
		fileModel.setFolder(new Boolean(isFolder()));
		fileModel.setAndroidLibrary(new Boolean(isAndroidLibrary()));
		fileModel.setAndroidAppModule(new Boolean(isAndroidAppModule));
		return fileModel;
	}

	public String getName() {
		StringBuilder fileName = new StringBuilder();
		if (getFileName() != null)
			fileName.append(getFileName());
		if (!isFolder())
			if (getFileExtension() != null)
				fileName.append("." + getFileExtension());
		return fileName.toString();
	}
}
