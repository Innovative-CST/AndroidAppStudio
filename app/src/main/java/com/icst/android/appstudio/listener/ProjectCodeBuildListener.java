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

package com.icst.android.appstudio.listener;

import com.icst.android.appstudio.exception.ProjectCodeBuildException;
import com.icst.android.appstudio.helper.ProjectCodeBuildProgress;

public interface ProjectCodeBuildListener {

	void onBuildStart();

	void onBuildComplete(long buildTime);

	void onBuildProgress(ProjectCodeBuildProgress progress);

	void onBuildProgressLog(String log);

	void onBuildCancelled();

	void onBuildFailed(ProjectCodeBuildException e, long buildTime);
}
