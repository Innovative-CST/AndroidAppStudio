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
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;

public class FileModelUtils {
	public static FileModel getFolderModel(String folderName) {
		FileModel folderModel = new FileModel();
		folderModel.setFileName(folderName);
		folderModel.setFolder(true);
		return folderModel;
	}

	public static ArrayList<FileModel> getFileModelList(File path) {
		ArrayList<FileModel> fileList = new ArrayList<FileModel>();
		for (File file : path.listFiles()) {
			if (file.isFile())
				continue;

			if (!new File(file, EnvironmentUtils.FILE_MODEL).exists())
				continue;

			DeserializerUtils.deserialize(
					new File(file, EnvironmentUtils.FILE_MODEL),
					new DeserializerUtils.DeserializerListener() {

						@Override
						public void onSuccessfullyDeserialized(Object object) {
							if (object instanceof FileModel) {
								fileList.add((FileModel) object);
							}
						}

						@Override
						public void onFailed(int errorCode, Exception e) {
						}
					});
		}
		return fileList;
	}

	public static boolean generateFolders(String[] foldersQuery, File path) {
		int foldersGenerated = 0;
		for (int i = 0; i < foldersQuery.length; ++i) {
			File folderDir = new File(path, foldersQuery[i]);
			File fileModelPath = new File(folderDir, EnvironmentUtils.FILE_MODEL);
			File folderFileDir = new File(folderDir, EnvironmentUtils.FILES);

			if (!folderDir.exists())
				folderDir.mkdirs();
			if (!folderFileDir.exists())
				folderFileDir.mkdirs();

			if (!fileModelPath.exists()) {
				FileModel folder = getFolderModel(foldersQuery[i]);
				SerializerUtil.serialize(
						folder,
						fileModelPath,
						new SerializerUtil.SerializerCompletionListener() {

							@Override
							public void onSerializeComplete() {
							}

							@Override
							public void onFailedToSerialize(Exception exception) {
							}
						});
				foldersGenerated += 1;
			}
		}
		return foldersGenerated != 0;
	}

	public static File generateFolderTreeIfNotExists(String[] foldersQuery, File path) {
		if (!path.exists())
			path.mkdirs();
		File destination = path;

		for (int i = 0; i < foldersQuery.length; ++i) {

			File folderDir = new File(destination, foldersQuery[i]);
			File fileModelPath = new File(folderDir, EnvironmentUtils.FILE_MODEL);
			File folderFileDir = new File(folderDir, EnvironmentUtils.FILES);

			if (!folderDir.exists())
				folderDir.mkdirs();
			if (!folderFileDir.exists())
				folderFileDir.mkdirs();

			FileModel folder = getFolderModel(foldersQuery[i]);

			if (!fileModelPath.exists()) {
				SerializerUtil.serialize(
						folder,
						fileModelPath,
						new SerializerUtil.SerializerCompletionListener() {

							@Override
							public void onSerializeComplete() {
							}

							@Override
							public void onFailedToSerialize(Exception exception) {
							}
						});
			}

			destination = folderFileDir;
		}

		return destination;
	}
}
