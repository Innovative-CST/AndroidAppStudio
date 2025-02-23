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

package com.icst.android.appstudio.extensions.commentextension;

import com.icst.android.appstudio.models.ExtensionBundle;

public class CommentExtension {

	public static ExtensionBundle getExtensionBundle() {
		ExtensionBundle extension = new ExtensionBundle();
		extension.setName("Comment Blocks Extension");
		extension.setVersion(1);
		extension.setHolders(CommentBlocksHolder.getHolders());
		extension.setBlocks(CommentBlocks.getBlocks());
		return extension;
	}
}
