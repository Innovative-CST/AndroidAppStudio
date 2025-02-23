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

package com.icst.android.appstudio.builder;

import java.io.File;

import com.icst.android.appstudio.helper.ProjectCodeBuilderCancelToken;
import com.icst.android.appstudio.listener.ProjectCodeBuildListener;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.vieweditor.models.LayoutModel;

import android.code.editor.common.utils.FileUtils;

public class LayoutSourceBuilder {
	private ModuleModel module;
	private boolean rebuild;
	private String layoutDirName;
	private ProjectCodeBuildListener listener;
	private ProjectCodeBuilderCancelToken cancelToken;

	public void build() {

		if (module == null || layoutDirName == null) {
			if (listener != null) {
				listener.onBuildProgressLog("Null values are passed to LayoutSourceBuilder");
			}
			return;
		}

		if (listener != null) {
			listener.onBuildProgressLog(
					"> Task " + module.module + ":generateLayoutsFile[" + layoutDirName + "]");
		}

		File layoutsDir = new File(
				new File(new File(module.resourceDirectory, EnvironmentUtils.FILES), layoutDirName),
				EnvironmentUtils.FILES);
		if (!layoutsDir.exists())
			return;

		for (File layoutFile : layoutsDir.listFiles()) {
			LayoutModel layout = DeserializerUtils.deserialize(layoutFile, LayoutModel.class);
			if (layout == null) {
				continue;
			}

			FileUtils.writeFile(
					new File(
							new File(module.resourceOutputDirectory, layoutDirName),
							layout.getLayoutName().concat(".xml"))
							.getAbsolutePath(),
					layout.getCode());
		}
	}

	public ModuleModel getModule() {
		return this.module;
	}

	public void setModule(ModuleModel module) {
		this.module = module;
	}

	public boolean getRebuild() {
		return this.rebuild;
	}

	public void setRebuild(boolean rebuild) {
		this.rebuild = rebuild;
	}

	public String getLayoutDirName() {
		return this.layoutDirName;
	}

	public void setLayoutDirName(String layoutDirName) {
		this.layoutDirName = layoutDirName;
	}

	public ProjectCodeBuildListener getListener() {
		return this.listener;
	}

	public void setListener(ProjectCodeBuildListener listener) {
		this.listener = listener;
	}

	public ProjectCodeBuilderCancelToken getCancelToken() {
		return this.cancelToken;
	}

	public void setCancelToken(ProjectCodeBuilderCancelToken cancelToken) {
		this.cancelToken = cancelToken;
	}
}
