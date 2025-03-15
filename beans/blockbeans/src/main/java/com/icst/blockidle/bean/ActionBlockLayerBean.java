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

import com.icst.blockidle.beans.utils.BeanArrayCloneUtils;
import com.icst.blockidle.beans.utils.BlockBeansUIDConstants;

/**
 * A Bean that holds a group of ActionBlocks and can hold one TerminatorBlockBean. Used to store the
 * nested BlockBeans, and does not hold BlockElementBean directly into it.
 */
public class ActionBlockLayerBean extends LayerBean<ActionBlockLayerBean> implements Serializable {

	public static final long serialVersionUID = BlockBeansUIDConstants.ACTION_ELEMENT_LAYER_BEAN;

	private ArrayList<ActionBlockBean> actionBlockBeans;
	private String key;

	public ArrayList<ActionBlockBean> getActionBlockBeans() {
		return this.actionBlockBeans;
	}

	public void setActionBlockBeans(ArrayList<ActionBlockBean> actionBlockBeans) {
		this.actionBlockBeans = actionBlockBeans;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getProcessedCode() {
		StringBuilder code = new StringBuilder();
		if (actionBlockBeans == null) {
			return "";
		}
		actionBlockBeans.forEach(actionBlockBean -> {
			code.append(actionBlockBean.getProcessedCode());
			code.append("\n");
		});
		return code.toString();
	}

	public <T extends BeanMetadata> ArrayList<T> getAllMetadata(Class<T> classType) {

		ArrayList<T> blocksMetadata = new ArrayList<T>();

		for (int i = 0; i < getActionBlockBeans().size(); ++i) {
			blocksMetadata.addAll(getActionBlockBeans().get(i).getAllMetadata(classType));
		}

		return blocksMetadata;
	}

	@Override
	public ActionBlockLayerBean cloneBean() {
		ActionBlockLayerBean clone = new ActionBlockLayerBean();
		clone.setActionBlockBeans(BeanArrayCloneUtils.clone(getActionBlockBeans()));
		clone.setKey(getKey() == null ? null : new String(getKey()));
		return clone;
	}
}
