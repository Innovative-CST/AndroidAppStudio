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

package com.icst.app.compiler;

import java.io.File;
import java.util.ArrayList;

import com.blankj.utilcode.util.FileUtils;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.app.compiler.progress.BuildEventProgressListener;

public class AAPT2Compiler {
	private ModuleModel module;
	private File binDir;
	private File genDir;
	private File outputPath;
	private File resPath;
	private BuildEventProgressListener listener;
	private CommandExecutor executer;

	public AAPT2Compiler(ModuleModel module, BuildEventProgressListener listener) {
		this.module = module;
		this.listener = listener;
		binDir = new File(module.moduleOutputDirectory, "build/bin");
		genDir = new File(module.moduleOutputDirectory, "build/gen");

		// Clean bin directory before compilation except for resource directory
		File[] childs = binDir.listFiles();
		if (childs != null) {
			for (File child : childs) {
				if (child.getName().equals("res")) {
					continue;
				}
				child.delete();
			}
		}

		// Make binDir and genDir directories
		binDir.mkdirs();
		genDir.mkdirs();
	}

	public void compile() {
		ArrayList<String> arguments = new ArrayList<String>();

		if (listener != null) {
			listener.onProgress("AAPT2 > Compiling resources...");
		}

		arguments.add(new File(EnvironmentUtils.BIN_DIR, "aapt2").getAbsolutePath());
		arguments.add("compile");
		arguments.add("--dir");
		arguments.add(module.resourceOutputDirectory.getAbsolutePath());
		arguments.add("-o");

		new File(binDir, "res").mkdirs();
		File outputFile = new File(new File(binDir, "res"), "project.zip");
		FileUtils.createOrExistsFile(new File(new File(binDir, "res"), "project.zip"));

		arguments.add(outputFile.getAbsolutePath());

		arguments.add("-v");

		executer = new CommandExecutor(listener);
		executer.setCommands(arguments);

		if (listener != null) {
			listener.onProgress(executer.execute());
		}
	}
}
