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

public class GeneralExpressionBlockBean extends ExpressionBlockBean<GeneralExpressionBlockBean>
		implements Serializable {

	private DatatypeBean[] returnDatatypes;

	public void setReturnDatatypes(DatatypeBean[] returnDatatypes) {
		this.returnDatatypes = returnDatatypes;
	}

	@Override
	public DatatypeBean[] getReturnDatatypes() {
		return returnDatatypes;
	}

	@Override
	public GeneralExpressionBlockBean cloneBean() {
		GeneralExpressionBlockBean clone = new GeneralExpressionBlockBean();
		clone.setBlockBeanKey(getBlockBeanKey() == null ? null : new String(getBlockBeanKey()));
		clone.setColor(getColor() == null ? null : new String(getColor()));
		clone.setDragAllowed(new Boolean(isValueReadOnly()));
		clone.setValueReadOnly(new Boolean(isValueReadOnly()));
		clone.setElementsLayers(BeanArrayCloneUtils.clone(getElementsLayers()));
		clone.setCodeSyntax(getCodeSyntax() == null ? null : new String(getCodeSyntax()));
		clone.setReturnDatatypes(BeanArrayCloneUtils.cloneDatatypeBeanArray(getReturnDatatypes()));
		clone.setBeanManifest(getBeanManifest() == null ? null : getBeanManifest());
		return clone;
	}

}
