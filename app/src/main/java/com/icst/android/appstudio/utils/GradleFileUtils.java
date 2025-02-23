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

import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.builtin.filemodels.BuiltInGradleFileModel;
import com.icst.android.appstudio.builtin.xml.BuiltInAndroidManifest;
import com.icst.android.appstudio.models.EventHolder;
import com.icst.android.appstudio.models.ProjectModel;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;

public class GradleFileUtils {

	private static EventHolder getGradleEventHolder() {
		EventHolder eventHolder = new EventHolder();
		eventHolder.setBuiltInEvents(true);
		eventHolder.setHolderName("Config");
		eventHolder.setDisableNewEvents(true);
		return eventHolder;
	}

	public static void installNewModule(
			File projectRootDirectory, FileModel module, File moduleFolderRootDir) {
		File projectDataDir = EnvironmentUtils.getProjectDataDir(projectRootDirectory);

		if (!projectDataDir.exists())
			projectDataDir.mkdirs();

		if (!moduleFolderRootDir.exists())
			moduleFolderRootDir.mkdirs();

		File moduleFolderDir = new File(moduleFolderRootDir, module.getName());

		if (!moduleFolderDir.exists())
			moduleFolderDir.mkdirs();

		File moduleFileModel = new File(moduleFolderDir, EnvironmentUtils.FILE_MODEL);

		SerializerUtil.serialize(
				module,
				moduleFileModel,
				new SerializerUtil.SerializerCompletionListener() {

					@Override
					public void onSerializeComplete() {
					}

					@Override
					public void onFailedToSerialize(Exception exception) {
					}
				});

		File moduleFolderFilesDir = new File(moduleFolderDir, EnvironmentUtils.FILES);

		if (!moduleFolderFilesDir.exists())
			moduleFolderFilesDir.mkdirs();

		File buildGradleDir = new File(moduleFolderFilesDir, EnvironmentUtils.GRADLE_FILE);

		if (!buildGradleDir.exists())
			buildGradleDir.mkdirs();

		File buildGradleFileModel = new File(buildGradleDir, EnvironmentUtils.FILE_MODEL);

		FileModel buildGradleFile = null;

		if (module.isAndroidAppModule()) {
			buildGradleFile = BuiltInGradleFileModel.getAppModuleGradleFileModel();
		} else {
			buildGradleFile = BuiltInGradleFileModel.getLibraryModuleGradleFileModel();
		}

		if (!buildGradleFileModel.exists()) {
			SerializerUtil.serialize(
					buildGradleFile,
					buildGradleFileModel,
					new SerializerUtil.SerializerCompletionListener() {

						@Override
						public void onSerializeComplete() {
						}

						@Override
						public void onFailedToSerialize(Exception exception) {
						}
					});
		}

		File buildGradleFileEventsDir = new File(buildGradleDir, EnvironmentUtils.EVENTS_DIR);
		if (buildGradleFileEventsDir.exists())
			buildGradleFileEventsDir.mkdirs();

		File buildGradleConfigEventsDir = new File(buildGradleFileEventsDir,
				EnvironmentUtils.APP_GRADLE_CONFIG_EVENT_HOLDER);

		if (!buildGradleConfigEventsDir.exists())
			buildGradleConfigEventsDir.mkdirs();

		File buildGradleConfigEventsHolder = new File(buildGradleConfigEventsDir, EnvironmentUtils.EVENTS_HOLDER);

		if (!buildGradleConfigEventsHolder.exists()) {
			SerializerUtil.serialize(
					getGradleEventHolder(),
					buildGradleConfigEventsHolder,
					new SerializerUtil.SerializerCompletionListener() {
						@Override
						public void onSerializeComplete() {
						}

						@Override
						public void onFailedToSerialize(Exception exception) {
						}
					});
		}

		File configEventsDir = new File(buildGradleConfigEventsDir, EnvironmentUtils.EVENTS_DIR);

		if (!configEventsDir.exists())
			configEventsDir.mkdirs();

		EventUtils.installEvents(buildGradleFile.getDefaultBuiltInEvents(), configEventsDir);

		File mainFileDir = FileModelUtils.generateFolderTreeIfNotExists(
				new String[] { "src", "main" }, moduleFolderFilesDir);

		File javaFileDir = FileModelUtils.generateFolderTreeIfNotExists(new String[] { "java" }, mainFileDir);

		ProjectModel projectModel = DeserializerUtils.deserialize(
				new File(projectRootDirectory, EnvironmentUtils.PROJECT_CONFIGRATION),
				ProjectModel.class);

		if (projectModel != null) {
			FileModelUtils.generateFolderTreeIfNotExists(
					projectModel.getPackageName().split("\\."), javaFileDir);
		}

		File resFilesDire = FileModelUtils.generateFolderTreeIfNotExists(new String[] { "res" }, mainFileDir);
		FileModelUtils.generateFolders(
				new String[] {
						"drawable",
						"drawable-hdpi",
						"drawable-xhdpi",
						"drawable-xxhdpi",
						"drawable-xxxhdpi",
						"layout",
						"layout-land",
						"menu",
						"mipmap",
						"mipmap-hdpi",
						"mipmap-xhdpi",
						"mipmap-xxhdpi",
						"mipmap-xxxhdpi"
				},
				resFilesDire);

		if (!new File(mainFileDir, "AndroidManifest.xml").exists()) {

			SerializerUtil.serialize(
					BuiltInAndroidManifest.get("com"),
					new File(mainFileDir, "AndroidManifest.xml"),
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

	public static void createGradleFilesIfDoNotExists(File projectRootDirectory) {
		FileModel moduleFolder = FileModelUtils.getFolderModel("app");
		moduleFolder.setAndroidAppModule(true);
		installNewModule(
				projectRootDirectory,
				moduleFolder,
				EnvironmentUtils.getProjectDataDir(projectRootDirectory));
	}

	public static void createLibraryModule(
			File projectRootDirectory, File rootDirToInstall, String moduleName) {
		FileModel moduleFolder = FileModelUtils.getFolderModel(moduleName);
		moduleFolder.setAndroidLibrary(true);
		installNewModule(projectRootDirectory, moduleFolder, rootDirToInstall);
	}

	public static boolean canCreateModule(File rootDir, String moduleName) {
		if (!rootDir.exists())
			return true;

		File moduleDir = new File(rootDir, moduleName);

		if (moduleDir.exists())
			return false;

		ArrayList<FileModel> files = FileModelUtils.getFileModelList(rootDir);
		for (int position = 0; position < files.size(); ++position) {
			if (files.get(position).isFolder()) {
				if (files.get(position).getName().equals(moduleName))
					return false;
			}
		}

		return true;
	}
}
