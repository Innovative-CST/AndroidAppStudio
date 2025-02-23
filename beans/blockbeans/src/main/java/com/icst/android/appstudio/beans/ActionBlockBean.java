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

/** ActionBlockBean, BlockBean that perform action. */
public abstract class ActionBlockBean<T> extends BlockBean<T>
		implements CodeProcessorBean, Serializable {

	public static final long serialVersionUID = BlockBeansUIDConstants.ACTION_BLOCK_BEAN;

	/** All the layers of block that holds content of block. */
	private ArrayList<LayerBean> layers;

	public ArrayList<LayerBean> getLayers() {
		return this.layers;
	}

	public void setLayers(ArrayList<LayerBean> layers) {
		this.layers = layers;
	}

	@Override
	public String getProcessedCode() {
		String code = getCodeSyntax();

		for (int i = 0; i < getLayers().size(); ++i) {
			LayerBean layerBean = getLayers().get(i);
			code = processLayerCode(code, layerBean);
		}

		return code;
	}

	private String processLayerCode(String code, LayerBean layerBean) {
		if (layerBean instanceof BlockElementLayerBean blockElementLayerBean) {
			return processElementLayerCode(code, blockElementLayerBean);
		} else if (layerBean instanceof ActionBlockLayerBean actionBlockLayerBean) {
			return processActionBlockLayerCode(code, actionBlockLayerBean);
		}
		return code;
	}

	private String processActionBlockLayerCode(
			String code, ActionBlockLayerBean actionBlockLayerBean) {

		String key = actionBlockLayerBean.getKey();
		String replacingCode = CodeFormatterUtils.getKeySyntaxString(key);
		int intendation = CodeFormatterUtils.getIntendation(code, replacingCode);

		String layerCode = actionBlockLayerBean.getProcessedCode();
		String layerIntendedCode = CodeFormatterUtils.addIntendation(layerCode, intendation);

		code = code.replace(replacingCode, layerIntendedCode);

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

		for (int i = 0; i < getLayers().size(); ++i) {
			LayerBean layerBean = getLayers().get(i);

			if (layerBean instanceof BlockElementLayerBean blockElementLayerBean) {
				blocksMetadata.addAll(blockElementLayerBean.getAllMetadata(classType));
			} else if (layerBean instanceof ActionBlockLayerBean actionBlockLayerBean) {
				blocksMetadata.addAll(actionBlockLayerBean.getAllMetadata(classType));
			}
		}

		if (getBeanManifest() != null) {
			if (getBeanManifest().getMetadata() != null) {
				blocksMetadata.addAll(getBeanManifest().get(classType));
			}
		}

		return blocksMetadata;
	}
}
