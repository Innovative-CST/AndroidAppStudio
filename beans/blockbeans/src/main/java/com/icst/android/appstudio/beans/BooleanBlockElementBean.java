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

import com.icst.android.appstudio.beans.utils.BlockBeansUIDConstants;

public class BooleanBlockElementBean
		implements ValueInputBlockElementBean<BooleanBlockElementBean>, Serializable {

	public static final long serialVersionUID = BlockBeansUIDConstants.BOOLEAN_BLOCK_ELEMENT_BEAN;

	private boolean bool;
	private BooleanBlockBean booleanBlock;
	private String key;
	private DatatypeBean acceptedReturnType;

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}

	public boolean getBool() {
		return this.bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public BooleanBlockBean getBooleanBlock() {
		return this.booleanBlock;
	}

	public void setBooleanBlock(BooleanBlockBean booleanBlock) {
		this.booleanBlock = booleanBlock;
	}

	public void setValue(boolean booleanVal) {
		this.booleanBlock = null;
		this.bool = booleanVal;
	}

	public void setValue(BooleanBlockBean boolBlock) {
		this.booleanBlock = boolBlock;
		this.bool = false;
	}

	@Override
	public String getValue() {
		if (getBooleanBlock() == null) {
			return String.valueOf(getBool());
		} else {
			if (getBooleanBlock().getCodeSyntax() != null) {
				return getBooleanBlock().getProcessedCode();
			}
		}
		return String.valueOf(getBool());
	}

	@Override
	public <K extends BeanMetadata> ArrayList<K> getAllMetadata(Class<K> classType) {

		ArrayList<K> blocksMetadata = new ArrayList<K>();

		if (getBooleanBlock() != null) {
			blocksMetadata.addAll(getBooleanBlock().getAllMetadata(classType));
		}

		return blocksMetadata;
	}

	@Override
	public DatatypeBean getAcceptedReturnType() {
		return acceptedReturnType;
	}

	public void setAcceptedReturnType(DatatypeBean acceptedReturnType) {
		this.acceptedReturnType = acceptedReturnType;
	}

	@Override
	public BooleanBlockElementBean cloneBean() {
		BooleanBlockElementBean clone = new BooleanBlockElementBean();
		clone.setBool(new Boolean(getBool()));
		clone.setBooleanBlock(getBooleanBlock() == null ? null : getBooleanBlock().cloneBean());
		clone.setKey(getKey() == null ? null : new String(getKey()));
		clone.setAcceptedReturnType(getAcceptedReturnType() == null ? null : getAcceptedReturnType().cloneBean());
		return clone;
	}
}
