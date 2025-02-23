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

import com.icst.android.appstudio.beans.utils.BeanArrayCloneUtils;
import com.icst.android.appstudio.beans.utils.BlockBeansUIDConstants;

/** A Bean that holds a group of Block elements. Used to store the Block items,
 * block elements but
 * does not hold nested blocks. */
public class BlockElementLayerBean extends LayerBean<BlockElementLayerBean>
		implements Serializable {

	public static final long serialVersionUID = BlockBeansUIDConstants.BLOCK_ELEMENT_LAYER_BEAN;

	private ArrayList<BlockElementBean> blockElementBeans;

	public ArrayList<BlockElementBean> getBlockElementBeans() {
		return this.blockElementBeans;
	}

	public void setBlockElementBeans(ArrayList<BlockElementBean> blockElementBeans) {
		this.blockElementBeans = blockElementBeans;
	}

	public <T extends BeanMetadata> ArrayList<T> getAllMetadata(Class<T> classType) {

		ArrayList<T> blocksMetadata = new ArrayList<T>();

		for (int i = 0; i < blockElementBeans.size(); ++i) {
			if (blockElementBeans.get(i) instanceof ValueInputBlockElementBean valueInputBlockElementBean) {
				blocksMetadata.addAll(valueInputBlockElementBean.getAllMetadata(classType));
			}
		}

		return blocksMetadata;
	}

	@Override
	public BlockElementLayerBean cloneBean() {
		BlockElementLayerBean clone = new BlockElementLayerBean();
		clone.setBlockElementBeans(BeanArrayCloneUtils.clone(getBlockElementBeans()));
		return clone;
	}
}
