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

package com.icst.android.appstudio.extensions.activityextension;

import java.io.IOException;
import java.util.ArrayList;

import com.icst.android.appstudio.ImageUtils;
import com.icst.android.appstudio.models.EventHolder;

public class ActivityEventHolders {
	public static ArrayList<EventHolder> getHolders() {
		ArrayList<EventHolder> result = new ArrayList<EventHolder>();
		result.add(getActivityHolder());
		return result;
	}

	public static EventHolder getActivityHolder() {
		EventHolder holder = new EventHolder();
		holder.setHolderName("Activity");
		holder.setBuiltInEvents(true);

		try {
			holder.setIcon(ImageUtils.convertImageToByteArray("images/android.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return holder;
	}
}
