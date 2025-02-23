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
import java.util.ArrayList;

import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.model.JavaFileModel;
import com.icst.android.appstudio.block.tag.DependencyTag;
import com.icst.android.appstudio.helper.FileModelCodeHelper;
import com.icst.android.appstudio.helper.ProjectCodeBuilderCancelToken;
import com.icst.android.appstudio.listener.ProjectCodeBuildListener;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;

import android.code.editor.common.utils.FileUtils;

public class JavaSourceBuilder {
	private ModuleModel module;
	private boolean rebuild;
	private String packageName;
	private File inputDir;
	private File outputDir;
	private ProjectCodeBuildListener listener;
	private ProjectCodeBuilderCancelToken cancelToken;
	private ArrayList<DependencyTag> dependencies;

	public void build() {
		dependencies = new ArrayList<DependencyTag>();

		if (module == null || packageName == null || inputDir == null || outputDir == null) {
			if (listener != null) {
				listener.onBuildProgressLog("Null values are passed to JavaSourceBuilder");
			}
			return;
		}

		if (!inputDir.exists()) {
			return;
		}

		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		for (File files : new File(inputDir, getJavaDirectoryForPackage(packageName)).listFiles()) {

			if (new File(files, EnvironmentUtils.FILE_MODEL).exists()) {
				FileModel fileModel = DeserializerUtils.deserialize(
						new File(files, EnvironmentUtils.FILE_MODEL), FileModel.class);

				if (fileModel == null) {
					return;
				}

				if (fileModel.isFolder()) {

					new File(outputDir, getJavaOutputDirectoryForPackage(packageName)).mkdirs();

					if (packageName.isEmpty()) {

						JavaSourceBuilder mainJavaSrcBuilder = new JavaSourceBuilder();
						mainJavaSrcBuilder.setModule(module);
						mainJavaSrcBuilder.setRebuild(rebuild);
						mainJavaSrcBuilder.setPackageName(fileModel.getFileName());
						mainJavaSrcBuilder.setInputDir(inputDir);
						mainJavaSrcBuilder.setOutputDir(outputDir);
						mainJavaSrcBuilder.setListener(listener);
						mainJavaSrcBuilder.setCancelToken(cancelToken);
						mainJavaSrcBuilder.build();
						dependencies.addAll(mainJavaSrcBuilder.getDependencies());
					} else {

						JavaSourceBuilder mainJavaSrcBuilder = new JavaSourceBuilder();
						mainJavaSrcBuilder.setModule(module);
						mainJavaSrcBuilder.setRebuild(rebuild);
						mainJavaSrcBuilder.setPackageName(
								packageName.concat(".").concat(fileModel.getFileName()));
						mainJavaSrcBuilder.setInputDir(inputDir);
						mainJavaSrcBuilder.setOutputDir(outputDir);
						mainJavaSrcBuilder.setListener(listener);
						mainJavaSrcBuilder.setCancelToken(cancelToken);
						mainJavaSrcBuilder.build();
						dependencies.addAll(mainJavaSrcBuilder.getDependencies());
					}
				}
			} else if (new File(files, EnvironmentUtils.JAVA_FILE_MODEL).exists()) {

				JavaFileModel javaFileModel = DeserializerUtils.deserialize(
						new File(files, EnvironmentUtils.JAVA_FILE_MODEL), JavaFileModel.class);

				if (javaFileModel == null) {
					return;
				}

				if (packageName.isEmpty()) {
					if (listener != null) {
						listener.onBuildProgressLog("  " + javaFileModel.getFileName());
					}
				} else {
					if (listener != null) {
						listener.onBuildProgressLog(
								"  " + packageName.concat(".") + javaFileModel.getFileName());
					}
				}

				FileModelCodeHelper fileGenerator = new FileModelCodeHelper();

				fileGenerator.setEventsDirectory(new File(files, EnvironmentUtils.EVENTS_DIR));
				fileGenerator.setProjectRootDirectory(module.projectRootDirectory);
				fileGenerator.setModule(module);
				fileGenerator.setFileModel(javaFileModel);
				String code = fileGenerator.getCode(
						packageName,
						new File(files, EnvironmentUtils.VARIABLES),
						new File(files, EnvironmentUtils.STATIC_VARIABLES));

				FileUtils.writeFile(
						new File(
								new File(outputDir, getJavaOutputDirectoryForPackage(packageName)),
								javaFileModel.getName())
								.getAbsolutePath(),
						code);
				dependencies.addAll(fileGenerator.getUsedDependency());
			}
		}
	}

	public static String getJavaDirectoryForPackage(String packageName) {

		StringBuilder packagePath = new StringBuilder();

		String[] packageBreakdown = packageName.split("\\.");
		for (int i = 0; i < packageBreakdown.length; ++i) {
			if (packageBreakdown[i].length() != 0) {
				packagePath.append(packageBreakdown[i]);
				packagePath.append(File.separator);
				packagePath.append(EnvironmentUtils.FILES);
				packagePath.append(File.separator);
			}
		}
		if (packageName.length() != 0) {
			if (packageBreakdown.length == 0) {
				packagePath.append(packageName);
				packagePath.append(File.separator);
				packagePath.append(EnvironmentUtils.FILES);
			}
		}

		return packagePath.toString();
	}

	public static String getJavaOutputDirectoryForPackage(String packageName) {

		StringBuilder packagePath = new StringBuilder();

		String[] packageBreakdown = packageName.split("\\.");
		for (int i = 0; i < packageBreakdown.length; ++i) {
			if (packageBreakdown[i].length() != 0) {
				packagePath.append(packageBreakdown[i]);
				packagePath.append(File.separator);
			}
		}
		if (packageName.length() != 0) {
			if (packageBreakdown.length == 0) {
				packagePath.append(packageName);
			}
		}

		return packagePath.toString();
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

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public File getInputDir() {
		return this.inputDir;
	}

	public void setInputDir(File inputDir) {
		this.inputDir = inputDir;
	}

	public File getOutputDir() {
		return this.outputDir;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
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

	public ArrayList<DependencyTag> getDependencies() {
		return this.dependencies;
	}
}
