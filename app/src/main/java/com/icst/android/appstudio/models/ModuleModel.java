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

package com.icst.android.appstudio.models;

import java.io.File;

import com.icst.android.appstudio.utils.EnvironmentUtils;

import android.os.Parcel;
import android.os.Parcelable;

public class ModuleModel implements Parcelable, Cloneable {
	/*
	 * Example: :module:submodule
	 */
	public String module;
	public File moduleDirectory;
	public File moduleOutputDirectory;
	public File projectRootDirectory;
	public File javaSourceDirectory;
	public File javaSourceOutputDirectory;
	public File resourceDirectory;
	public File resourceOutputDirectory;
	public File gradleFileDirectory;
	public File gradleOutputFile;
	public File manifestFile;
	public File manifestOutputFile;
	public File moduleLibsDirectory;

	public ModuleModel() {
	}

	protected ModuleModel(Parcel in) {
		module = in.readString();
		moduleDirectory = new File(in.readString());
		moduleOutputDirectory = new File(in.readString());
		projectRootDirectory = new File(in.readString());
		javaSourceDirectory = new File(in.readString());
		javaSourceOutputDirectory = new File(in.readString());
		resourceDirectory = new File(in.readString());
		resourceOutputDirectory = new File(in.readString());
		gradleFileDirectory = new File(in.readString());
		gradleOutputFile = new File(in.readString());
		manifestFile = new File(in.readString());
		manifestOutputFile = new File(in.readString());
		moduleLibsDirectory = new File(in.readString());
	}

	public void init(String module, File projectRootDirectory) {
		this.module = module;
		this.projectRootDirectory = projectRootDirectory;
		this.moduleDirectory = EnvironmentUtils.getModuleDirectory(projectRootDirectory, module);
		moduleOutputDirectory = EnvironmentUtils.getModuleOutputDirectory(projectRootDirectory, module);

		javaSourceDirectory = getJavaDirectory();
		javaSourceOutputDirectory = getJavaOutputDirectory();

		resourceDirectory = getResourceDirectory();
		resourceOutputDirectory = getResourceOutputDirectory();

		gradleFileDirectory = getGradleFileDirectory();
		gradleOutputFile = getGradleOutputDirectory();

		manifestFile = getManifestFile();
		manifestOutputFile = getManifestOutputFile();

		moduleLibsDirectory = getModuleLibsDirectory();
	}

	private File getManifestFile() {
		return new File(
				new File(
						new File(
								new File(
										new File(
												new File(moduleDirectory, EnvironmentUtils.FILES),
												EnvironmentUtils.SOURCE_DIR),
										EnvironmentUtils.FILES),
								EnvironmentUtils.MAIN_DIR),
						EnvironmentUtils.FILES),
				EnvironmentUtils.MANIFEST);
	}

	private File getManifestOutputFile() {
		return new File(
				new File(
						new File(
								EnvironmentUtils.getModuleOutputDirectory(projectRootDirectory, module),
								EnvironmentUtils.SOURCE_DIR),
						EnvironmentUtils.MAIN_DIR),
				EnvironmentUtils.MANIFEST);
	}

	private File getJavaDirectory() {
		return new File(
				new File(
						new File(
								new File(
										new File(
												new File(moduleDirectory, EnvironmentUtils.FILES),
												EnvironmentUtils.SOURCE_DIR),
										EnvironmentUtils.FILES),
								EnvironmentUtils.MAIN_DIR),
						EnvironmentUtils.FILES),
				EnvironmentUtils.JAVA_DIR);
	}

	private File getJavaOutputDirectory() {
		return new File(
				new File(
						new File(
								EnvironmentUtils.getModuleOutputDirectory(projectRootDirectory, module),
								EnvironmentUtils.SOURCE_DIR),
						EnvironmentUtils.MAIN_DIR),
				EnvironmentUtils.JAVA_DIR);
	}

	private File getResourceDirectory() {
		return new File(
				new File(
						new File(
								new File(
										new File(
												new File(moduleDirectory, EnvironmentUtils.FILES),
												EnvironmentUtils.SOURCE_DIR),
										EnvironmentUtils.FILES),
								EnvironmentUtils.MAIN_DIR),
						EnvironmentUtils.FILES),
				EnvironmentUtils.RES_DIR);
	}

	private File getResourceOutputDirectory() {
		return new File(
				new File(
						new File(
								EnvironmentUtils.getModuleOutputDirectory(projectRootDirectory, module),
								EnvironmentUtils.SOURCE_DIR),
						EnvironmentUtils.MAIN_DIR),
				EnvironmentUtils.RES_DIR);
	}

	private File getGradleFileDirectory() {
		return new File(
				new File(
						EnvironmentUtils.getModuleDirectory(projectRootDirectory, module),
						EnvironmentUtils.FILES),
				EnvironmentUtils.GRADLE_FILE);
	}

	private File getGradleOutputDirectory() {
		return new File(
				EnvironmentUtils.getModuleOutputDirectory(projectRootDirectory, module),
				EnvironmentUtils.GRADLE_FILE);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		dest.writeString(module);
		dest.writeString(moduleDirectory.getAbsolutePath());
		dest.writeString(moduleOutputDirectory.getAbsolutePath());
		dest.writeString(projectRootDirectory.getAbsolutePath());
		dest.writeString(javaSourceDirectory.getAbsolutePath());
		dest.writeString(javaSourceOutputDirectory.getAbsolutePath());
		dest.writeString(resourceDirectory.getAbsolutePath());
		dest.writeString(resourceOutputDirectory.getAbsolutePath());
		dest.writeString(gradleFileDirectory.getAbsolutePath());
		dest.writeString(gradleOutputFile.getAbsolutePath());
		dest.writeString(manifestFile.getAbsolutePath());
		dest.writeString(manifestOutputFile.getAbsolutePath());
		dest.writeString(moduleLibsDirectory.getAbsolutePath());
	}

	public static final Creator<ModuleModel> CREATOR = new Creator<ModuleModel>() {
		@Override
		public ModuleModel createFromParcel(Parcel in) {
			return new ModuleModel(in);
		}

		@Override
		public ModuleModel[] newArray(int size) {
			return new ModuleModel[size];
		}
	};

	public File getModuleLibsDirectory() {
		return new File(
				EnvironmentUtils.getModuleDirectory(projectRootDirectory, module),
				EnvironmentUtils.PROJECT_DEPENDENCIES);
	}

	public void setModuleLibsDirectory(File moduleLibsDirectory) {
		this.moduleLibsDirectory = moduleLibsDirectory;
	}
}
