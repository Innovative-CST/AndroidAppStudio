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
import com.icst.android.appstudio.beans.utils.CodeFormatterUtils;

public abstract class ExpressionBlockBean<T> extends BaseBlockBean<T>
		implements BlockElementBean<T>, CodeProcessorBean, Serializable {

	public static final long serialVersionUID = BlockBeansUIDConstants.EXPRESSION_BLOCK_BEAN;

	private String codeSyntax;

	@Override
	public String getCodeSyntax() {
		return this.codeSyntax;
	}

	public void setCodeSyntax(String codeSyntax) {
		this.codeSyntax = codeSyntax;
	}

	public abstract DatatypeBean[] getReturnDatatypes();

	@Override
	public String getProcessedCode() {
		String code = getCodeSyntax();

		for (int i = 0; i < getElementsLayers().size(); ++i) {
			BlockElementLayerBean layerBean = getElementsLayers().get(i);
			code = processElementLayerCode(code, layerBean);
		}

		return code;
	}

	private String processElementLayerCode(
			String code, BlockElementLayerBean blockElementLayerBean) {
		for (int i = 0; i < blockElementLayerBean.getBlockElementBeans().size(); ++i) {
			BlockElementBean blockElementBean = blockElementLayerBean.getBlockElementBeans().get(i);
			if (blockElementBean instanceof ValueInputBlockElementBean valueInputBlockElementBean) {
				code = processValueInputBlockElementCode(code, valueInputBlockElementBean);
			}
		}
		return code;
	}

	private String processValueInputBlockElementCode(
			String code, ValueInputBlockElementBean valueInputBlockElementBean) {
		return CodeFormatterUtils.formatCode(code, valueInputBlockElementBean);
	}

	public <T extends BeanMetadata> ArrayList<T> getAllMetadata(Class<T> classType) {

		ArrayList<T> blocksMetadata = new ArrayList<T>();

		for (int i = 0; i < getElementsLayers().size(); ++i) {
			blocksMetadata.addAll(getElementsLayers().get(i).getAllMetadata(classType));
		}

		if (getBeanManifest() != null) {
			if (getBeanManifest().getMetadata() != null) {
				blocksMetadata.addAll(getBeanManifest().get(classType));
			}
		}

		return blocksMetadata;
	}
}
