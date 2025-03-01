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

package com.icst.android.appstudio.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.icst.android.appstudio.beans.utils.BeanArrayCloneUtils;
import com.icst.android.appstudio.beans.utils.BeansUIDConstants;

public class DatatypeBean implements CloneableBean<DatatypeBean>, Serializable {

	public static final long serialVersionUID = BeansUIDConstants.DATATYPE_BEAN_BEAN;

	private String fullClassName;
	private String className;
	private ArrayList<DatatypeBean> superTypes;

	public DatatypeBean(String fullClassName, String className) {
		this.fullClassName = fullClassName;
		this.className = className;
		superTypes = new ArrayList<>();
	}

	public DatatypeBean(String fullClassName, String className, ArrayList<DatatypeBean> superTypes) {
		this.fullClassName = fullClassName;
		this.className = className;
		this.superTypes = superTypes;
	}

	public String getFullClassName() {
		return fullClassName;
	}

	public ArrayList<DatatypeBean> getSuperTypes() {
		return superTypes;
	}

	public void addSuperType(DatatypeBean superType) {
		superTypes.add(superType);
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isSuperTypeOrDatatype(DatatypeBean datatype) {
		return isSuperTypeHelperOrDatatype(datatype, new HashSet<>());
	}

	private boolean isSuperTypeHelperOrDatatype(DatatypeBean datatype, Set<DatatypeBean> visited) {
		if (datatype == null || visited.contains(datatype)) {
			return false;
		}

		if (equals(datatype)) {
			return true;
		}
		visited.add(datatype);

		for (DatatypeBean mDatatype : superTypes) {
			if (mDatatype.isSuperTypeOrDatatype(datatype)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DatatypeBean datatype = (DatatypeBean) o;
		return fullClassName.equals(datatype.fullClassName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fullClassName);
	}

	@Override
	public DatatypeBean cloneBean() {
		String mClassName = className == null ? null : new String(className);
		String mFullClassName = fullClassName == null ? null : new String(fullClassName);
		ArrayList<DatatypeBean> clonedSuperTypes = BeanArrayCloneUtils.clone(superTypes);
		return new DatatypeBean(mFullClassName, mClassName, clonedSuperTypes);
	}
}
