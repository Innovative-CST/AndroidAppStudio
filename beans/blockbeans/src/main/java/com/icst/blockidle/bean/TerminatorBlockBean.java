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
import com.icst.blockidle.beans.utils.BlockBeansUIDConstants;

/** TerminatorBlockBean: Can hold RegularBlockBean (nested blocks),
 * BlockElementBean but differs from
 * RegularBlockBean because this must be the last block bean and after that no
 * action block can be
 * placed. */
public class TerminatorBlockBean extends ActionBlockBean<TerminatorBlockBean>
		implements Serializable {

	public static final long serialVersionUID = BlockBeansUIDConstants.BLOCK_BEAN;

	private String codeSyntax;

	public String getCodeSyntax() {
		return this.codeSyntax;
	}

	public void setCodeSyntax(String codeSyntax) {
		this.codeSyntax = codeSyntax;
	}

	@Override
	public TerminatorBlockBean cloneBean() {
		TerminatorBlockBean clone = new TerminatorBlockBean();
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
