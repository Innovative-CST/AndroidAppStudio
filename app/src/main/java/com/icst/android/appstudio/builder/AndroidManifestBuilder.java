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

import com.icst.android.appstudio.beans.XmlBean;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.helper.ProjectCodeBuilderCancelToken;
import com.icst.android.appstudio.listener.ProjectCodeBuildListener;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;

import android.code.editor.common.utils.FileUtils;

public class AndroidManifestBuilder {
	private ModuleModel module;
	private boolean rebuild;
	private ProjectCodeBuildListener listener;
	private ProjectCodeBuilderCancelToken cancelToken;

	public void build() {
		if (module == null) {
			if (listener != null) {
				listener.onBuildProgressLog("Null values are passed to Android Manifest Builder");
			}
			return;
		}

		FileModel moduleFileModel = DeserializerUtils.deserialize(
				new File(module.moduleDirectory, EnvironmentUtils.FILE_MODEL), FileModel.class);

		if (moduleFileModel == null) {
			if (listener != null) {
				listener.onBuildProgressLog(
						"Module is not an android app or android library.\n\tCannot add manifest file to that.");
			}
			return;
		}

		XmlBean manifest = DeserializerUtils.deserialize(module.manifestFile, XmlBean.class);

		if (manifest == null) {
			if (listener != null) {
				listener.onBuildProgressLog(
						"Manifest file object is null. Aborting to generate manifest file");
			}
			return;
		}

		FileUtils.writeFile(module.manifestOutputFile.getAbsolutePath(), manifest.getCode(""));
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
