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

import com.icst.android.appstudio.beans.utils.BeansUIDConstants;

/** A class of Datatype, to compare that the two Datatypes are different or not
 * by comparing class
 * name and import. This Bean can also be used to store data. */
public class DatatypeBean implements CloneableBean<DatatypeBean>, Serializable {

	public static final long serialVersionUID = BeansUIDConstants.DATATYPE_BEAN_BEAN;

	private String className;
	private String classImport;
	private boolean isImportNecessary;

	/** Compare two DatatypeBean by checking that its import and class name is same
	 * or not with other.
	 *
	 * <p>
	 * <b>Note</b>: Comparision is not done by memory pointer, it is checked whether
	 * the className
	 * and classImoort of datatype is equal or not.
	 *
	 * @param mDatatypeBean
	 *            DatatypeBean to compare with.
	 * @return True if @mDatatypeBean is equal. */
	public boolean equals(DatatypeBean mDatatypeBean) {
		if (mDatatypeBean == null) {
			return false;
		}
		boolean isClassNameEqual = false;
		if (className == null) {
			isClassNameEqual = mDatatypeBean.className == null ? true : false;
		} else {
			if (mDatatypeBean.className == null)
				return false;
			isClassNameEqual = mDatatypeBean.className.equals(className);
		}

		boolean isClassImportEqual = false;
		if (classImport == null) {
			isClassImportEqual = mDatatypeBean.classImport == null ? true : false;
		} else {
			if (mDatatypeBean.classImport == null)
				return false;
			isClassImportEqual = mDatatypeBean.classImport.equals(classImport);
		}

		return isClassNameEqual && isClassImportEqual;
	}

	@Override
	public DatatypeBean cloneBean() {
		DatatypeBean clone = new DatatypeBean();
		clone.setClassName(getClassName() == null ? null : new String(getClassName()));
		clone.setClassImport(getClassImport() == null ? null : new String(getClassImport()));
		clone.setImportNecessary(new Boolean(isImportNecessary()));
		return clone;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassImport() {
		return this.classImport;
	}

	public void setClassImport(String classImport) {
		this.classImport = classImport;
	}

	public boolean isImportNecessary() {
		return this.isImportNecessary;
	}

	public void setImportNecessary(boolean isImportNecessary) {
		this.isImportNecessary = isImportNecessary;
	}
}
