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

import java.util.ArrayList;

import com.icst.android.appstudio.block.model.BlockHolderModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.tag.BlockModelTag;

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
