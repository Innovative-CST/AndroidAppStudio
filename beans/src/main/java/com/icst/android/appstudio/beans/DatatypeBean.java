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

package com.icst.android.appstudio.beans;

import java.io.Serializable;

import com.icst.android.appstudio.beans.utils.SerializationUIDConstants;

/** A class of Datatype, to compare that the two Datatypes are different or not
 * by comparing class
 * name and import. This Bean can also be used to store data. */
public class DatatypeBean implements Serializable {

	public static final long serialVersionUID = SerializationUIDConstants.DATATYPE_BEAN_BEAN;

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
			if (mDatatypeBean.className == null)
				return false;
			isClassImportEqual = mDatatypeBean.classImport.equals(classImport);
		}

		return isClassNameEqual && isClassImportEqual;
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
