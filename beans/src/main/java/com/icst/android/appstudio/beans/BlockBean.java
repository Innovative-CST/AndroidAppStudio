/*
 * This file is part of Android AppStudio [https://github.com/Innovative-CST/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.icst.android.appstudio.beans;

import com.icst.android.appstudio.beans.utils.SerializationUIDConstants;
import java.io.Serializable;

/**
 * Abstract class representing the base block supposed to be used within the
 * LogicEditor.
 */
public abstract class BlockBean implements Serializable {

	public static final long serialVersionUID = SerializationUIDConstants.BLOCK_BEAN;

	private String blockBeanKey;

	// The color of the block
	private String color;

	// Whether the block can be dragged
	private boolean dragAllowed;

	// Whether the value of the block is read-only
	private boolean valueReadOnly;

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
	 * @param color
	 *            the new color for the block.
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
	 * Sets whether dragging the block is allowed. Supposed to be used only by
	 * internal library and
	 * for purpose of pre-build EventBean. You must see its implementation somewhere
	 * in app, guess
	 * its usage because it is not designed mainly for consumers.
	 *
	 * @param dragAllowed
	 *            true to allow dragging, false otherwise.
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
	 * @param valueReadOnly
	 *            true to make the value read-only, false otherwise.
	 */
	public void setValueReadOnly(boolean valueReadOnly) {
		this.valueReadOnly = valueReadOnly;
	}
}
