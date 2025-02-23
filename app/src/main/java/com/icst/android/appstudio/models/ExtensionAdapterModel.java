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

public class ExtensionAdapterModel {
	private String title;
	private int latestVersion;
	private boolean isInstalled;
	private int installedVersion;
	private String authors;
	private String childKey;

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLatestVersion() {
		return this.latestVersion;
	}

	public void setLatestVersion(int latestVersion) {
		this.latestVersion = latestVersion;
	}

	public boolean getIsInstalled() {
		return this.isInstalled;
	}

	public void setIsInstalled(boolean isInstalled) {
		this.isInstalled = isInstalled;
	}

	public int getInstalledVersion() {
		return this.installedVersion;
	}

	public void setInstalledVersion(int installedVersion) {
		this.installedVersion = installedVersion;
	}

	public String getAuthors() {
		return this.authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getChildKey() {
		return this.childKey;
	}

	public void setChildKey(String childKey) {
		this.childKey = childKey;
	}
}
