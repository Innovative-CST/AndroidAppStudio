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

package com.icst.android.appstudio.block.tag;

import java.io.Serializable;

public class DependencyTag implements AdditionalCodeHelperTag, Serializable {
	public static final long serialVersionUID = 19L;

	public String dependencyGroup;
	public String dependencyName;
	public String version;

	public String getDependencyGroup() {
		return this.dependencyGroup;
	}

	public void setDependencyGroup(String dependencyGroup) {
		this.dependencyGroup = dependencyGroup;
	}

	public String getDependencyName() {
		return this.dependencyName;
	}

	public void setDependencyName(String dependencyName) {
		this.dependencyName = dependencyName;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public AdditionalCodeHelperTag clone() {
		DependencyTag clone = new DependencyTag();
		clone.setDependencyGroup(
				getDependencyGroup() == null ? null : new String(getDependencyGroup()));
		clone.setVersion(getVersion() == null ? null : new String(getVersion()));
		clone.setDependencyName(getDependencyName() == null ? null : new String(getDependencyName()));
		return clone;
	}
}
