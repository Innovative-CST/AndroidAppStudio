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

import com.icst.android.appstudio.beans.utils.BlockBeansUIDConstants;

/** A simple BlockElement that just display text on block */
public class LabelBlockElementBean
		implements BlockElementBean<LabelBlockElementBean>, Serializable {

	public static final long serialVersionUID = BlockBeansUIDConstants.LABEL_BLOCK_ELEMENT_BEAN;

	private String label;

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public LabelBlockElementBean cloneBean() {
		LabelBlockElementBean clone = new LabelBlockElementBean();
		clone.setLabel(getLabel() == null ? null : new String(getLabel()));
		return clone;
	}
}
