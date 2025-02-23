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

import com.icst.android.appstudio.beans.utils.BlockBeansUIDConstants;

/** A abstract class, to be inherited by types Layer, It provides a interface
 * for
 * types of Layer of
 * Block. Layer is a meant to used to prevent long block from being so long and
 * hence can be
 * rendered below from point of new layer (e.g: Like we press enter to put long
 * text on next line) */
public abstract class LayerBean<T> implements Serializable, CloneableBean<T> {
	public static final long serialVersionUID = BlockBeansUIDConstants.LAYER_BEAN;
}
