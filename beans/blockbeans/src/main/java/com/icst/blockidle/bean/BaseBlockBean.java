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

import com.icst.blockidle.beans.utils.BlockBeansUIDConstants;

/** A basic BlockBean model that just hold fields layer (not nested block) and
 * does not return any
 * code from it. */
public abstract class BaseBlockBean<T> extends BlockBean<T> implements Serializable {

	public static final long serialVersionUID = BlockBeansUIDConstants.BASE_BLOCK_BEAN;

	/** All the layers of block elememts that holds content of block. */
	private ArrayList<BlockElementLayerBean> elementsLayers;

	public ArrayList<BlockElementLayerBean> getElementsLayers() {
		return this.elementsLayers;
	}

	public void setElementsLayers(ArrayList<BlockElementLayerBean> elementsLayers) {
		this.elementsLayers = elementsLayers;
	}
}
