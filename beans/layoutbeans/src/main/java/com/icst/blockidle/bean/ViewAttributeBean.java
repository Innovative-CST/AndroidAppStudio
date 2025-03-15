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

public class ViewAttributeBean implements CloneableBean<ViewAttributeBean>, Serializable {
	public static final long serialVersionUID = 17L;

	private String attribute;
	private Object attributeValue;

	public String getAttribute() {
		return this.attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public Object getAttributeValue() {
		return this.attributeValue;
	}

	public void setAttributeValue(Object attributeValue) {
		this.attributeValue = attributeValue;
	}

	// Warning: Values are not copied.
	@Override
	public ViewAttributeBean cloneBean() {
		ViewAttributeBean clone = new ViewAttributeBean();
		clone.setAttribute(getAttribute() == null ? null : new String(getAttribute()));
		return clone;
	}
}
