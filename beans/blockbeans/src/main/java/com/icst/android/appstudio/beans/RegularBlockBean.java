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
import com.icst.android.appstudio.beans.utils.BlockBeansUIDConstants;

/** RegularBlockBean: Can hold RegularBlockBean (nested blocks), BlockElementBean */
public class RegularBlockBean extends ActionBlockBean<RegularBlockBean> implements Serializable {

	public static final long serialVersionUID = BlockBeansUIDConstants.BLOCK_BEAN;

	private String codeSyntax;

	@Override
	public String getCodeSyntax() {
		return this.codeSyntax;
	}

	public void setCodeSyntax(String codeSyntax) {
		this.codeSyntax = codeSyntax;
	}

	@Override
	public RegularBlockBean cloneBean() {
		RegularBlockBean clone = new RegularBlockBean();
		clone.setBlockBeanKey(getBlockBeanKey() == null ? null : new String(getBlockBeanKey()));
		clone.setCodeSyntax(getCodeSyntax() == null ? null : new String(getCodeSyntax()));
		clone.setColor(getColor() == null ? null : new String(getColor()));
		clone.setDragAllowed(new Boolean(isDragAllowed()));
		clone.setLayers(BeanArrayCloneUtils.clone(getLayers()));
		clone.setValueReadOnly(new Boolean(isValueReadOnly()));
		clone.setBeanManifest(getBeanManifest() == null ? null : getBeanManifest());
		return clone;
	}
}
