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

package com.icst.android.appstudio.interfaces;

import android.graphics.drawable.Drawable;

/*
 * Usage:
 *
 * It is a interface that will be inherited by the terminal pane, code editor pane
 * in order retreview information of working pane e.g. Opened terminals, Opened files.
 */

public interface WorkSpacePane {

	Drawable getWorkSpacePaneIcon();

	String getWorkSpacePaneName();

	Drawable getWorkSpaceStatus();

	void onReleaseRequest();

	void onRelease();
}
