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

public class StringBlockElementBean
		implements ValueInputBlockElementBean<StringBlockElementBean>, Serializable {
	public static final long serialVersionUID = BlockBeansUIDConstants.STRING_BLOCK_ELEMENT_BEAN;

	private String string;
	private StringBlockBean stringBlock;
	private String key;

	public String getString() {
		return this.string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String str) {
		if (str == null) {
			return;
		}
		stringBlock = null;
		string = str;
	}

	public void setValue(StringBlockBean strBlock) {
		if (strBlock == null) {
			return;
		}
		stringBlock = strBlock;
		string = null;
	}

	public StringBlockBean getStringBlock() {
		return this.stringBlock;
	}

	public void setStringBlock(StringBlockBean stringBlock) {
		this.stringBlock = stringBlock;
	}

	@Override
	public String getValue() {
		if (getStringBlock() == null) {
			if (getString() != null) {
				return new String("\"").concat(getString()).concat("\"");
			}
		} else {
			if (getStringBlock().getCodeSyntax() != null) {
				return getStringBlock().getProcessedCode();
			}
		}
		return new String("\"").concat("\"");
	}

	@Override
	public <K extends BeanMetadata> ArrayList<K> getAllMetadata(Class<K> classType) {

		ArrayList<K> blocksMetadata = new ArrayList<K>();

		if (getStringBlock() != null) {
			blocksMetadata.addAll(getStringBlock().getAllMetadata(classType));
		}

		return blocksMetadata;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public DatatypeBean getAcceptedReturnType() {
		DatatypeBean acceptedReturnType = new DatatypeBean();
		acceptedReturnType.setImportNecessary(false);
		acceptedReturnType.setClassImport("java.lang.String");
		acceptedReturnType.setClassName("String");
		return acceptedReturnType;
	}

	@Override
	public StringBlockElementBean cloneBean() {
		StringBlockElementBean clone = new StringBlockElementBean();
		clone.setString(getString() == null ? null : new String(getString()));
		clone.setStringBlock(stringBlock == null ? null : stringBlock.cloneBean());
		clone.setKey(getKey() == null ? null : new String(getKey()));
		return clone;
	}
}
