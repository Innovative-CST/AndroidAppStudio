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

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.block.model.FileModel;

public class IconUtils {
	public static int getResourceManagerFileModelIcon(FileModel file) {
		switch (file.getName()) {
			case "drawable", "drawable-hdpi", "drawable-xhdpi", "drawable-xxhdpi", "drawable-xxxhdpi":
				return R.drawable.ic_imageview;
			case "layout", "layout-land":
				return R.drawable.ic_layout;
			case "menu":
				return R.drawable.ic_menu;
			// case "mipmap", "mipmap-hdpi", "mipmap-xhdpi", "mipmap-xxhdpi",
			// "mipmap-xxxhdpi":
		}
		return 0;
	}
}
