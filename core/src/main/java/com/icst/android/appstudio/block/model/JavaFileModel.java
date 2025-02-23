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

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.icst.android.appstudio.block.tag.AdditionalCodeHelperTag;
import com.icst.android.appstudio.block.tag.ImportTag;
import com.icst.android.appstudio.block.utils.ArrayUtils;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;
import com.icst.android.appstudio.vieweditor.models.LayoutModel;

public class JavaFileModel extends FileModel implements Serializable {
	public static final long serialVersionUID = 20L;

	private int classType;
	private String extendingClass;
	private String extendingClassImport;
	private String[] implementingInterface;
	private String[] implementingInterfaceImports;
	private ArrayList<VariableModel> layouts;
	private ArrayList<String> layoutsName;

	public static final int SIMPLE_JAVA_CLASS = 0;
	public static final int JAVA_ACTIVITY = 1;

	public String getCode(
			String packageName,
			File layoutDirectory,
			ArrayList<Object> builtInEvents,
			ArrayList<Object> events,
			ArrayList<VariableModel> instanceVariables,
			ArrayList<VariableModel> staticVariables,
			HashMap<String, Object> projectVariables) {
		String resultCode = getRawCode() != null ? new String(getRawCode()) : null;
		if (resultCode == null) {
			return "";
		}

		resultCode = resultCode.replace(
				RawCodeReplacer.getReplacer(getReplacerKey(), "imports"),
				getImportsCode(builtInEvents, events));
		resultCode = resultCode.replace(
				RawCodeReplacer.getReplacer(getReplacerKey(), "inheritence"), getInheritenceCode());

		if (instanceVariables != null) {
			StringBuilder instanceVariablesCode = new StringBuilder();
			for (int i = 0; i < instanceVariables.size(); ++i) {
				if (i != 0) {
					instanceVariablesCode.append("\n");
				}
				instanceVariablesCode.append(instanceVariables.get(i).getDefCode());
			}

			String formatter = null;
			String[] lines = getRawCode().split("\n");
			for (String line : lines) {
				if (line.contains(RawCodeReplacer.getReplacer(getReplacerKey(), "variables"))) {
					formatter = line.substring(
							0, line.indexOf(RawCodeReplacer.getReplacer(getReplacerKey(), "variables")));
				}
			}

			StringBuilder formattedGeneratedCode = new StringBuilder();

			String[] generatedCodeLines = instanceVariablesCode.toString().split("\n");

			for (int generatedCodeLinePosition = 0; generatedCodeLinePosition < generatedCodeLines.length; ++generatedCodeLinePosition) {

				if (formatter != null) {
					if (generatedCodeLinePosition != 0)
						formattedGeneratedCode.append(formatter);
				}

				formattedGeneratedCode.append(generatedCodeLines[generatedCodeLinePosition]);
				if (generatedCodeLinePosition != (generatedCodeLines.length - 1)) {
					formattedGeneratedCode.append("\n");
				}
			}

			resultCode = resultCode.replace(
					RawCodeReplacer.getReplacer(getReplacerKey(), "variables"),
					formattedGeneratedCode.toString());
		}

		if (layouts != null && layoutsName != null) {
			StringBuilder layoutVariablesCode = new StringBuilder();
			for (int i = 0; i < layouts.size(); ++i) {

				/*
				 * Find the layout with layout name at index i.
				 * And pass it to generate variables
				 */

				if (!new File(layoutDirectory, layoutsName.get(i)).exists())
					continue;

				if (new File(layoutDirectory, layoutsName.get(i)).isDirectory())
					continue;

				LayoutModel layout = null;

				try {
					FileInputStream mFileInputStream = new FileInputStream(
							new File(layoutDirectory, layoutsName.get(i)));
					ObjectInputStream mObjectInputStream = new ObjectInputStream(mFileInputStream);
					Object mObject = mObjectInputStream.readObject();
					mFileInputStream.close();
					mObjectInputStream.close();
					if (LayoutModel.class.isInstance(mObject)) {
						layout = LayoutModel.class.cast(mObject);
					}

				} catch (Exception e) {
					continue;
				}

				if (layout == null) {
					continue;
				}

				if (i != 0) {
					layoutVariablesCode.append("\n");
				}
				layoutVariablesCode.append(layouts.get(i).getLayoutDefCode(layout));
			}

			String formatter = null;
			String[] lines = getRawCode().split("\n");
			for (String line : lines) {
				if (line.contains(RawCodeReplacer.getReplacer(getReplacerKey(), "layoutVariables"))) {
					formatter = line.substring(
							0, line.indexOf(RawCodeReplacer.getReplacer(getReplacerKey(), "layoutVariables")));
				}
			}

			StringBuilder formattedGeneratedCode = new StringBuilder();

			String[] generatedCodeLines = layoutVariablesCode.toString().split("\n");

			for (int generatedCodeLinePosition = 0; generatedCodeLinePosition < generatedCodeLines.length; ++generatedCodeLinePosition) {

				if (formatter != null) {
					if (generatedCodeLinePosition != 0)
						formattedGeneratedCode.append(formatter);
				}

				formattedGeneratedCode.append(generatedCodeLines[generatedCodeLinePosition]);
				if (generatedCodeLinePosition != (generatedCodeLines.length - 1)) {
					formattedGeneratedCode.append("\n");
				}
			}

			resultCode = resultCode.replace(
					RawCodeReplacer.getReplacer(getReplacerKey(), "variables"),
					formattedGeneratedCode.toString());
		}

		if (staticVariables != null) {
			StringBuilder staticVariablesCode = new StringBuilder();
			for (int i = 0; i < staticVariables.size(); ++i) {
				if (i != 0) {
					staticVariablesCode.append("\n");
				}
				staticVariablesCode.append(staticVariables.get(i).getDefCode());
			}

			String formatter = null;
			String[] lines = getRawCode().split("\n");
			for (String line : lines) {
				if (line.contains(RawCodeReplacer.getReplacer(getReplacerKey(), "static-variables"))) {
					formatter = line.substring(
							0,
							line.indexOf(RawCodeReplacer.getReplacer(getReplacerKey(), "static-variables")));
				}
			}

			StringBuilder formattedGeneratedCode = new StringBuilder();

			String[] generatedCodeLines = staticVariablesCode.toString().split("\n");

			for (int generatedCodeLinePosition = 0; generatedCodeLinePosition < generatedCodeLines.length; ++generatedCodeLinePosition) {

				if (formatter != null) {
					if (generatedCodeLinePosition != 0)
						formattedGeneratedCode.append(formatter);
				}

				formattedGeneratedCode.append(generatedCodeLines[generatedCodeLinePosition]);
				if (generatedCodeLinePosition != (generatedCodeLines.length - 1)) {
					formattedGeneratedCode.append("\n");
				}
			}

			resultCode = resultCode.replace(
					RawCodeReplacer.getReplacer(getReplacerKey(), "static-variables"),
					formattedGeneratedCode.toString());
		}

		boolean[] ignoreEvents = null;
		if (events != null) {
			ignoreEvents = new boolean[events.size()];
			for (int i = 0; i < events.size(); ++i) {
				if (events.get(i) instanceof Event) {
					Event event = (Event) events.get(i);

					if (event.isDirectFileEvent()) {
						String formatter = null;
						String[] lines = resultCode.split("\n");
						for (String line : lines) {
							if (line.contains(RawCodeReplacer.getReplacer(getReplacerKey(), "directEvents"))) {
								formatter = line.substring(
										0,
										line.indexOf(
												RawCodeReplacer.getReplacer(getReplacerKey(), "directEvents")));
							}
						}

						StringBuilder formattedGeneratedCode = new StringBuilder();

						String[] generatedCodeLines = event.getCode(projectVariables).toString().split("\n");

						for (int generatedCodeLinePosition = 0; generatedCodeLinePosition < generatedCodeLines.length; ++generatedCodeLinePosition) {

							if (formatter != null) {
								if (generatedCodeLinePosition != 0)
									formattedGeneratedCode.append(formatter);
							}

							formattedGeneratedCode.append(generatedCodeLines[generatedCodeLinePosition]);
							if (generatedCodeLinePosition != (generatedCodeLines.length - 1)) {
								formattedGeneratedCode.append("\n");
							}
						}

						resultCode = resultCode.replace(
								RawCodeReplacer.getReplacer(getReplacerKey(), "directEvents"),
								formattedGeneratedCode
										.toString()
										.concat("\n\n")
										.concat(formatter)
										.concat(RawCodeReplacer.getReplacer(getReplacerKey(), "directEvents")));
						ignoreEvents[i] = true;
					}
				}
			}
		}

		if (builtInEvents != null) {

			for (int eventCount = 0; eventCount < builtInEvents.size(); ++eventCount) {
				if (builtInEvents.get(eventCount) instanceof Event) {
					Event event = (Event) builtInEvents.get(eventCount);
					resultCode = resultCode.replace(
							RawCodeReplacer.getReplacer(getReplacerKey(), event.getName()),
							event.getCode(projectVariables));
				}
			}
		}

		if (events != null) {
			for (int eventCount = 0; eventCount < events.size(); ++eventCount) {
				if (!ignoreEvents[eventCount]) {
					if (events.get(eventCount) instanceof Event) {
						Event event = (Event) events.get(eventCount);
						resultCode = resultCode.replace(
								RawCodeReplacer.getReplacer(event.getEventReplacerKey(), event.getName()),
								event.getCode(projectVariables));
					}
				}
			}
		}

		resultCode = resultCode.replace(RawCodeReplacer.getReplacer("$FileName"), getFileName());
		resultCode = resultCode.replace(
				RawCodeReplacer.getReplacer(getReplacerKey(), "filePackage name"), packageName);
		resultCode = RawCodeReplacer.removeAndroidAppStudioString(getReplacerKey(), resultCode);
		return resultCode;
	}

	public String getInheritenceCode() {
		StringBuilder inheritenceCode = new StringBuilder();
		if (getExtendingClass() != null) {
			inheritenceCode.append(" ");
			inheritenceCode.append("extends ");
			inheritenceCode.append(getExtendingClass());
		}
		if (getImplementingInterface() != null) {
			for (int i = 0; i < getImplementingInterface().length; ++i) {
				if (i == 0) {
					inheritenceCode.append(" ");
					inheritenceCode.append("implements");
				}

				if (i != 0 && (i != (getImplementingInterface().length - 1))) {
					inheritenceCode.append(", ");
				}

				inheritenceCode.append(getImplementingInterface()[i]);
			}
		}
		inheritenceCode.append(" ");
		return inheritenceCode.toString();
	}

	public String getImportsCode(ArrayList<Object> builtInEvents, ArrayList<Object> events) {
		ArrayList<ImportTag> usedImports = getUsedImports(builtInEvents, events);
		StringBuilder importsCode = new StringBuilder();

		ArrayList<String> imported = new ArrayList<String>();

		if (getExtendingClassImport() != null) {
			imported.add(getExtendingClassImport());
			importsCode.append("import ");
			importsCode.append(getExtendingClassImport());
			importsCode.append(";");
		}

		if (getImplementingInterfaceImports() != null) {
			for (int i = 0; i < getImplementingInterfaceImports().length; ++i) {
				imported.add(getImplementingInterfaceImports()[i]);
				importsCode.append("\n");
				importsCode.append("import ");
				importsCode.append(getImplementingInterfaceImports()[i]);
				importsCode.append(";");
			}
		}

		for (int i = 0; i < usedImports.size(); ++i) {
			if (usedImports.get(i).getImportClass() != null) {
				if (!imported.contains(usedImports.get(i).getImportClass())) {
					importsCode.append("\n");
					importsCode.append("import ");
					importsCode.append(usedImports.get(i).getImportClass());
					importsCode.append(";");
					imported.add(usedImports.get(i).getImportClass());
				}
			}
		}

		return importsCode.toString();
	}

	public ArrayList<ImportTag> getUsedImports(
			ArrayList<Object> builtInEvents, ArrayList<Object> events) {
		ArrayList<ImportTag> usedImports = new ArrayList<ImportTag>();

		if (builtInEvents != null) {

			for (int eventCount = 0; eventCount < builtInEvents.size(); ++eventCount) {
				if (builtInEvents.get(eventCount) instanceof Event) {
					Event event = (Event) builtInEvents.get(eventCount);

					for (int tags = 0; tags < event.getAdditionalTagsUsed().size(); ++tags) {
						AdditionalCodeHelperTag tag = event.getAdditionalTagsUsed().get(tags);

						if (tag instanceof ImportTag) {
							usedImports.add((ImportTag) tag);
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

						if (tag instanceof ImportTag) {
							usedImports.add((ImportTag) tag);
						}
					}
				}
			}
		}

		return usedImports;
	}

	@Override
	protected FileModel clone() {
		JavaFileModel fileModel = new JavaFileModel();
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
		fileModel.setAndroidAppModule(new Boolean(isAndroidAppModule()));
		fileModel.setClassType(new Integer(getClassType()));
		fileModel.setExtendingClass(
				getExtendingClass() != null ? new String(getExtendingClass()) : null);
		fileModel.setImplementingInterface(ArrayUtils.clone(getImplementingInterface()));
		fileModel.setExtendingClassImport(
				getExtendingClassImport() != null ? new String(getExtendingClassImport()) : null);
		fileModel.setImplementingInterfaceImports(ArrayUtils.clone(getImplementingInterfaceImports()));
		return fileModel;
	}

	public int getClassType() {
		return this.classType;
	}

	public void setClassType(int classType) {
		this.classType = classType;
	}

	public String getExtendingClass() {
		return this.extendingClass;
	}

	public void setExtendingClass(String extendingClass) {
		this.extendingClass = extendingClass;
	}

	public String[] getImplementingInterface() {
		return this.implementingInterface;
	}

	public void setImplementingInterface(String[] implementingInterface) {
		this.implementingInterface = implementingInterface;
	}

	public String getExtendingClassImport() {
		return this.extendingClassImport;
	}

	public void setExtendingClassImport(String extendingClassImport) {
		this.extendingClassImport = extendingClassImport;
	}

	public String[] getImplementingInterfaceImports() {
		return this.implementingInterfaceImports;
	}

	public void setImplementingInterfaceImports(String[] implementingInterfaceImports) {
		this.implementingInterfaceImports = implementingInterfaceImports;
	}

	public ArrayList<VariableModel> getLayouts() {
		return this.layouts;
	}

	public void setLayouts(ArrayList<VariableModel> layouts) {
		this.layouts = layouts;
	}
}
