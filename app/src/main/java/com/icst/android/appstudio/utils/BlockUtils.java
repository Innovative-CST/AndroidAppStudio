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

import com.icst.android.appstudio.block.model.BlockHolderModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.tag.BlockModelTag;
import java.util.ArrayList;

public final class BlockUtils {
	public static ArrayList<BlockHolderModel> loadBlockHolders(FileModel file, Event event) {
		ArrayList<BlockHolderModel> holders = ExtensionUtils.extractBlockHoldersFromExtensions();
		ArrayList<BlockModel> blocks = ExtensionUtils.extractBlocksFromExtensions();

		for (int i = 0; i < holders.size(); ++i) {
			BlockHolderModel holder = holders.get(i);
			ArrayList<Object> holderBlocks = new ArrayList<Object>();

			for (int i2 = 0; i2 < blocks.size(); ++i2) {
				BlockModel block = blocks.get(i2);

				if (block.getHolderName() == null) {
					continue;
				}

				if (!block.getHolderName().equals(holder.getName())) {
					continue;
				}

				if (block.getTags() != null) {
					BlockModelTag tag = block.getTags();

					if (tag.getNotSupportedFileExtensions() != null) {
						if (file.getFileExtension() == null) {
							continue;
						}

						if (containsString(tag.getNotSupportedFileExtensions(), file.getFileExtension())) {
							continue;
						}
					}

					if (tag.getSupportedFileExtensions() != null) {
						if (file.getFileExtension() == null) {
							continue;
						}

						if (!containsString(tag.getSupportedFileExtensions(), file.getFileExtension())) {
							continue;
						}
					}

					if (tag.getNotSupportedEvents() != null) {
						if (event.getName() == null) {
							continue;
						}

						if (containsString(tag.getNotSupportedEvents(), event.getName())) {
							continue;
						}
					}

					if (tag.getSupportedEvents() != null) {
						if (event.getName() == null) {
							continue;
						}

						if (!containsString(tag.getSupportedEvents(), event.getName())) {
							continue;
						}
					}
				}

				holderBlocks.add(block);
			}
			holder.setList(holderBlocks);
		}

		ArrayList<BlockHolderModel> output = new ArrayList<BlockHolderModel>();
		for (int i = 0; i < holders.size(); ++i) {
			BlockHolderModel holder = holders.get(i);

			if (holder.getList().size() > 0) {
				output.add(holder);
			}
		}

		return output;
	}

	private static boolean containsString(String[] array, String str) {
		for (int i = 0; i < array.length; ++i) {
			if (str.equals(array[i])) {
				return true;
			}
		}
		return false;
	}
}
