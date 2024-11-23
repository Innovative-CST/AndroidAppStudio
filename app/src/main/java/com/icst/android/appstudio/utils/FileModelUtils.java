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
