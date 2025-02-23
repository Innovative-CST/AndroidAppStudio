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

import java.io.Serializable;
import java.util.ArrayList;

public class ProjectModel implements Serializable {
	public static final long serialVersionUID = 9L;

	private String packageName;
	private String projectName;
	private String projectVersionName;
	private String targetSdkVersion;
	private String minimumSdkVersion;
	private String javaVersion;
	private String dexer;
	private ArrayList<BuildConfigFieldModel> buildConfigFields;
	private int versionCode;

	public String getPackageName() {
		return this.packageName == null ? "" : this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getProjectName() {
		return this.projectName == null ? "" : this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectVersionName() {
		return this.projectVersionName == null ? "" : this.projectVersionName;
	}

	public void setProjectVersionName(String projectVersionName) {
		this.projectVersionName = projectVersionName;
	}

	public String getTargetSdkVersion() {
		return this.targetSdkVersion == null ? "" : this.targetSdkVersion;
	}

	public void setTargetSdkVersion(String targetSdkVersion) {
		this.targetSdkVersion = targetSdkVersion;
	}

	public String getMinimumSdkVersion() {
		return this.minimumSdkVersion == null ? "" : this.minimumSdkVersion;
	}

	public void setMinimumSdkVersion(String minimumSdkVersion) {
		this.minimumSdkVersion = minimumSdkVersion;
	}

	public String getJavaVersion() {
		return this.javaVersion == null ? "" : this.javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public String getDexer() {
		return this.dexer == null ? "" : this.dexer;
	}

	public void setDexer(String dexer) {
		this.dexer = dexer;
	}

	public ArrayList<BuildConfigFieldModel> getBuildConfigFields() {
		return this.buildConfigFields == null
				? new ArrayList<BuildConfigFieldModel>()
				: this.buildConfigFields;
	}

	public void setBuildConfigFields(ArrayList<BuildConfigFieldModel> buildConfigFields) {
		this.buildConfigFields = buildConfigFields;
	}

	public int getVersionCode() {
		return this.versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
}
