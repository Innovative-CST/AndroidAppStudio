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

import com.icst.android.appstudio.beans.utils.BeanArrayCloneUtils;

public class BooleanBlockBean extends ExpressionBlockBean<BooleanBlockBean> implements Serializable {
	@Override
	public DatatypeBean[] getReturnDatatypes() {
		DatatypeBean obj = new DatatypeBean();
		obj.setClassImport("java.lang.Object");
		obj.setClassName("Object");
		obj.setImportNecessary(false);

		DatatypeBean string = new DatatypeBean();
		string.setClassImport("java.lang.Boolean");
		string.setClassName("Boolean");
		string.setImportNecessary(false);
		return new DatatypeBean[] { obj, string };
	}

	@Override
	public BooleanBlockBean cloneBean() {
		BooleanBlockBean clone = new BooleanBlockBean();
		clone.setBlockBeanKey(getBlockBeanKey() == null ? null : new String(getBlockBeanKey()));
		clone.setColor(getColor() == null ? null : new String(getColor()));
		clone.setDragAllowed(new Boolean(isValueReadOnly()));
		clone.setValueReadOnly(new Boolean(isValueReadOnly()));
		clone.setElementsLayers(BeanArrayCloneUtils.clone(getElementsLayers()));
		clone.setCodeSyntax(getCodeSyntax() == null ? null : new String(getCodeSyntax()));
		clone.setBeanManifest(getBeanManifest() == null ? null : getBeanManifest());
		return clone;
	}
}
