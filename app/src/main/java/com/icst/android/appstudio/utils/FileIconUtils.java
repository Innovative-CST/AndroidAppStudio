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

import com.icst.android.appstudio.R;

import android.content.Context;
import android.graphics.drawable.Drawable;

public final class FileIconUtils {

	public static final Drawable getFileIcon(File file, Context context) {
		String fileExtension = getFileExtension(file);

		if (fileExtension != null) {
			switch (fileExtension.toLowerCase()) {
				case "jpg":
				case "jpeg":
				case "png":
				case "gif":
				case "bmp":
					return context.getDrawable(R.drawable.ic_imageview);
				case "mp3":
				case "wav":
				case "ogg":
					return context.getDrawable(R.drawable.ic_music);
				case "mp4":
				case "mkv":
				case "avi":
					return context.getDrawable(R.drawable.ic_video);
				case "html":
				case "htm":
					return context.getDrawable(R.drawable.language_html);
				case "css":
					return context.getDrawable(R.drawable.language_css);
				case "js":
					return context.getDrawable(R.drawable.language_javascript);
				case "json":
					return context.getDrawable(R.drawable.language_json);
				case "kt":
					return context.getDrawable(R.drawable.ic_language_kotlin);
				case "java":
					return context.getDrawable(R.drawable.ic_language_java);
				case "xml":
					return context.getDrawable(R.drawable.ic_xml);
				case "sh":
					return context.getDrawable(R.drawable.language_shell);
				case "txt":
					return context.getDrawable(R.drawable.file_outline);
				default:
					return context.getDrawable(R.drawable.file_outline);
			}
		}

		return context.getDrawable(R.drawable.file_outline);
	}

	private static String getFileExtension(File file) {
		String name = file.getName();
		int lastDotIndex = name.lastIndexOf('.');
		return (lastDotIndex == -1) ? null : name.substring(lastDotIndex + 1);
	}
}
