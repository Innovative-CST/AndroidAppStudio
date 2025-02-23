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

package com.icst.android.appstudio.builtin.filemodels;

import java.util.ArrayList;

import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;
import com.icst.android.appstudio.builtin.events.GradleBuiltInEvents;

public class BuiltInGradleFileModel {
	/*
	 * Built-in file model of build.gradle of application module.
	 */
	public static FileModel getAppModuleGradleFileModel() {
		FileModel appModuleGradleFile = new FileModel();
		appModuleGradleFile.setFileName("build");
		appModuleGradleFile.setFileExtension("gradle");
		appModuleGradleFile.setFolder(false);
		appModuleGradleFile.setReplacerKey("applicationModuleGradleEvent");
		appModuleGradleFile.setBuiltInEventsName("Config");

		StringBuilder appModuleGradleFileRawCode = new StringBuilder();
		appModuleGradleFileRawCode.append("plugins {\n\tid 'com.android.application'\n}\n\n");
		appModuleGradleFileRawCode.append(
				RawCodeReplacer.getReplacer(appModuleGradleFile.getReplacerKey(), "androidBlock"));
		appModuleGradleFileRawCode.append("\n\n");
		appModuleGradleFileRawCode.append(
				RawCodeReplacer.getReplacer(appModuleGradleFile.getReplacerKey(), "dependenciesBlock"));
		appModuleGradleFileRawCode.append("\n");

		appModuleGradleFile.setRawCode(appModuleGradleFileRawCode.toString());

		ArrayList<Object> builtinEvents = new ArrayList<Object>();
		builtinEvents.add(GradleBuiltInEvents.getAppModuleAndroidBlockEvent());
		builtinEvents.add(GradleBuiltInEvents.getAppModuleDependenciesBlockEvent());

		appModuleGradleFile.setDefaultBuiltInEvents(builtinEvents);

		return appModuleGradleFile;
	}

	/*
	 * Built-in file model of build.gradle of library module.
	 */
	public static FileModel getLibraryModuleGradleFileModel() {
		FileModel libraryModuleGradleFile = new FileModel();
		libraryModuleGradleFile.setFileName("build");
		libraryModuleGradleFile.setFileExtension("gradle");
		libraryModuleGradleFile.setFolder(false);
		libraryModuleGradleFile.setReplacerKey("libraryModuleGradleEvent");
		libraryModuleGradleFile.setBuiltInEventsName("Config");

		StringBuilder libraryModuleGradleFileRawCode = new StringBuilder();
		libraryModuleGradleFileRawCode.append("plugins {\n\tid 'com.android.library'\n}\n");
		libraryModuleGradleFileRawCode.append(
				RawCodeReplacer.getReplacer(libraryModuleGradleFile.getReplacerKey(), "androidBlock"));
		libraryModuleGradleFileRawCode.append("\n\n");
		libraryModuleGradleFileRawCode.append(
				RawCodeReplacer.getReplacer(libraryModuleGradleFile.getReplacerKey(), "dependenciesBlock"));
		libraryModuleGradleFileRawCode.append("\n");

		libraryModuleGradleFile.setRawCode(libraryModuleGradleFileRawCode.toString());

		ArrayList<Object> builtinEvents = new ArrayList<Object>();

		builtinEvents.add(GradleBuiltInEvents.getLibraryModuleAndroidBlockEvent());
		builtinEvents.add(GradleBuiltInEvents.getLibraryModuleDependenciesBlockEvent());

		libraryModuleGradleFile.setDefaultBuiltInEvents(builtinEvents);

		return libraryModuleGradleFile;
	}
}
