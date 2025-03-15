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
import java.util.ArrayList;

public class GeneralExpressionBlockElementBean
		implements ValueInputBlockElementBean<GeneralExpressionBlockElementBean>, Serializable {

	private String mValue;
	private ExpressionBlockBean expressionBlockBean;
	private String key;
	private DatatypeBean acceptedReturnType;

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}

	public void setValue(String value) {
		this.expressionBlockBean = null;
		this.mValue = value;
	}

	public void setValue(ExpressionBlockBean expressionBlockBean) {
		this.expressionBlockBean = expressionBlockBean;
		this.mValue = null;
	}

	@Override
	public String getValue() {
		if (expressionBlockBean != null) {
			if (expressionBlockBean.getCodeSyntax() != null) {
				return expressionBlockBean.getProcessedCode();
			}
		}

		if (mValue == null) {
			return "";
		}
		return mValue;
	}

	@Override
	public <K extends BeanMetadata> ArrayList<K> getAllMetadata(Class<K> classType) {

		ArrayList<K> blocksMetadata = new ArrayList<K>();

		if (getExpressionBlockBean() != null) {
			blocksMetadata.addAll(getExpressionBlockBean().getAllMetadata(classType));
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
	public GeneralExpressionBlockElementBean cloneBean() {
		GeneralExpressionBlockElementBean clone = new GeneralExpressionBlockElementBean();
		clone.setValue(mValue == null ? null : new String(mValue));
		clone.setExpressionBlockBean(
				getExpressionBlockBean() == null
						? null
						: ExpressionBlockBean.class.cast(getExpressionBlockBean().cloneBean()));
		clone.setKey(getKey() == null ? null : new String(getKey()));
		clone.setAcceptedReturnType(
				getAcceptedReturnType() == null ? null : getAcceptedReturnType().cloneBean());
		return clone;
	}

	public ExpressionBlockBean getExpressionBlockBean() {
		return this.expressionBlockBean;
	}

	public void setExpressionBlockBean(ExpressionBlockBean expressionBlockBean) {
		this.expressionBlockBean = expressionBlockBean;
	}
}
