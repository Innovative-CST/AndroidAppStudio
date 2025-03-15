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

import com.icst.blockidle.beans.utils.BlockBeansUIDConstants;

/** Abstract class representing the base block supposed to be used within the LogicEditor. */
public abstract class BlockBean<T> implements Serializable, CloneableBean<T> {

	public static final long serialVersionUID = BlockBeansUIDConstants.BLOCK_BEAN;

	private String blockBeanKey;

	// The color of the block
	private String color;

	// Whether the block can be dragged
	private boolean dragAllowed;

	// Whether the value of the block is read-only
	private boolean valueReadOnly;

	private BeanManifest beanManifest;

	public String getBlockBeanKey() {
		return this.blockBeanKey;
	}

	public void setBlockBeanKey(String blockBeanKey) {
		this.blockBeanKey = blockBeanKey;
	}

	/**
	 * Gets the color of the block.
	 *
	 * @return the color of the block as a String in hexadecimal format.
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Sets the color of the block.
	 *
	 * @param color the new color for the block.
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Checks if dragging the block is allowed.
	 *
	 * @return true if dragging is allowed, false otherwise.
	 */
	public boolean isDragAllowed() {
		return dragAllowed;
	}

	/**
	 * Sets whether dragging the block is allowed. Supposed to be used only by internal library and
	 * for purpose of pre-build EventBean. You must see its implementation somewhere in app, guess
	 * its usage because it is not designed mainly for consumers.
	 *
	 * @param dragAllowed true to allow dragging, false otherwise.
	 */
	public void setDragAllowed(boolean dragAllowed) {
		this.dragAllowed = dragAllowed;
	}

	/**
	 * Checks if the block's value is read-only.
	 *
	 * @return true if the value is read-only, false otherwise.
	 */
	public boolean isValueReadOnly() {
		return valueReadOnly;
	}

	/**
	 * Sets whether the block's value is read-only.
	 *
	 * @param valueReadOnly true to make the value read-only, false otherwise.
	 */
	public void setValueReadOnly(boolean valueReadOnly) {
		this.valueReadOnly = valueReadOnly;
	}

	public BeanManifest getBeanManifest() {
		return this.beanManifest;
	}

	public void setBeanManifest(BeanManifest beanManifest) {
		this.beanManifest = beanManifest;
	}
}
