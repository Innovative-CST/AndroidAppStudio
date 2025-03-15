/*
 *  This file is part of Block IDLE.
 *
 *  Block IDLE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Block IDLE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Block IDLE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.blockidle.beans;

import java.io.Serializable;

import com.icst.blockidle.beans.utils.BeanArrayCloneUtils;

public class StringBlockBean extends ExpressionBlockBean<StringBlockBean> implements Serializable {

	@Override
	public DatatypeBean getReturnDatatype() {
		DatatypeBean obj = new DatatypeBean("java.lang.Object", "Object");
		DatatypeBean string = new DatatypeBean("java.lang.String", "String");
		string.addSuperType(obj);
		return string;
	}

	@Override
	public StringBlockBean cloneBean() {
		StringBlockBean clone = new StringBlockBean();
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
