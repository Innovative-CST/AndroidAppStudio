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
