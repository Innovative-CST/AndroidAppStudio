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

public class NumericBlockElementBean
		implements ValueInputBlockElementBean<NumericBlockElementBean>, Serializable {

	public static final long serialVersionUID = BlockBeansUIDConstants.NUMERIC_BLOCK_ELEMENT_BEAN;

	private String numericalValue;
	private NumericBlockBean numericBlock;
	private DatatypeBean acceptedReturnType;
	private String key;

	@Override
	public <K extends BeanMetadata> ArrayList<K> getAllMetadata(Class<K> classType) {

		ArrayList<K> blocksMetadata = new ArrayList<K>();

		if (getNumericBlock() != null) {
			blocksMetadata.addAll(getNumericBlock().getAllMetadata(classType));
		}

		return blocksMetadata;
	}

	@Override
	public NumericBlockElementBean cloneBean() {
		NumericBlockElementBean clone = new NumericBlockElementBean();
		clone.setNumericalValue(getNumericalValue() == null ? null : new String(getNumericalValue()));
		clone.setNumericBlock(getNumericBlock() == null ? null : getNumericBlock().cloneBean());
		clone.setKey(getKey() == null ? null : new String(getKey()));
		clone.setAcceptedReturnType(getAcceptedReturnType() == null ? null : getAcceptedReturnType().cloneBean());
		return clone;
	}

	@Override
	public DatatypeBean getAcceptedReturnType() {
		return acceptedReturnType;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getValue() {
		if (getNumericBlock() != null) {
			if (getNumericBlock().getCodeSyntax() != null) {
				return getNumericBlock().getProcessedCode();
			}
		}

		if (getNumericalValue() == null) {
			return "0";
		}
		return getNumericalValue();
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getNumericalValue() {
		return this.numericalValue;
	}

	public void setNumericalValue(String numericalValue) {
		this.numericalValue = numericalValue;
	}

	public NumericBlockBean getNumericBlock() {
		return this.numericBlock;
	}

	public void setNumericBlock(NumericBlockBean numericBlock) {
		this.numericBlock = numericBlock;
	}

	public void setAcceptedReturnType(DatatypeBean acceptedReturnType) {
		this.acceptedReturnType = acceptedReturnType;
	}
}
