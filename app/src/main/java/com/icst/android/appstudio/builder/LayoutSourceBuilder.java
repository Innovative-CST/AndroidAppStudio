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

package com.icst.android.appstudio.builder;

import android.code.editor.common.utils.FileUtils;
import com.icst.android.appstudio.helper.ProjectCodeBuilderCancelToken;
import com.icst.android.appstudio.listener.ProjectCodeBuildListener;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.vieweditor.models.LayoutModel;
import java.io.File;

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
