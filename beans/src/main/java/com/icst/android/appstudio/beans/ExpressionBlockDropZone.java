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

import com.icst.android.appstudio.beans.utils.BlockBeanUtils;

/**
 * Abstract class representing the ExpressionBlockBean drop zone supposed to be
 * used within the
 * LogicEditor.
 */
public abstract class ExpressionBlockDropZone {

	/**
	 * Marks whether ExpressionBlockDropZone is empty or occupied by a
	 * ExpressionBlockBean.
	 */
	private boolean isOccupied;

	/** ExpressionBlockBean that occupied this ExpressionBlockDropZone. */
	private ExpressionBlockBean expressionBlockBean;

	private DatatypeBean datatypeBean;

	/**
	 * Execute when the ExpressionBlockBean is dropped inside BlockDropZoneBean and
	 * BlockDropZoneBean
	 * was not previously occupied.
	 *
	 * @param expressionBlockBean
	 *            The ExpressionBlockBean which is just dropped.
	 */
	protected abstract void onExpressionBlockBeanDropped(ExpressionBlockBean expressionBlockBean);

	/**
	 * Execute when the ExpressionBlockBean is dropped inside BlockDropZoneBean and
	 * BlockDropZoneBean
	 * was previously occupied.
	 *
	 * @param droppedExpressionBlockBean
	 *            The ExpressionBlockBean which is just dropped.
	 * @param oldExpressionBlockBean
	 *            The previously ExpressionBlockBean which is replaced by a new new
	 *            one.
	 */
	protected abstract void onExpressionBlockBeanDropped(
			ExpressionBlockBean droppedExpressionBlockBean, ExpressionBlockBean oldExpressionBlockBean);

	/**
	 * Use this method to know whether a ExpressionBlockBean is droppable inside a
	 * ExpressionBlockDropZone or not.
	 *
	 * @param mExpressionBlockBean
	 *            Instance of ExpressionBlockBean to check if it is droppable or
	 *            not.
	 * @return return true if @mExpressionBlockBean is droppable inside
	 *         ExpressionBlockDropZone or
	 *         not.
	 */
	protected boolean canDropBlockBean(ExpressionBlockBean mExpressionBlockBean) {
		return BlockBeanUtils.arrayContainsDatatypeBeans(
				mExpressionBlockBean.getReturnDatatypes(), datatypeBean);
	}

	/**
	 * Use this method to drop the ExpressionBlockBean in ExpressionBlockDropZone.
	 *
	 * @param expressionBlockBean
	 *            Instance of ExpressionBlockBean to drop inside the
	 *            ExpressionBlockDropZone.
	 * @return return true if expressionBlockBean is dropped, otherwise false.
	 */
	protected boolean dropBlockBean(ExpressionBlockBean expressionBlockBean) {
		if (canDropBlockBean(expressionBlockBean)) {

			// Check if this ExpressionBlockDropZone is already occupied.

			if (isOccupied) {
				// Mark ExpressionBlockDropZone to be occupied
				final ExpressionBlockBean previousExpressionBlockBean = this.expressionBlockBean;
				setExpressionBlockBean(expressionBlockBean);
				setOccupied(true);
				onExpressionBlockBeanDropped(expressionBlockBean, previousExpressionBlockBean);
			} else {
				// Mark ExpressionBlockDropZone to be occupied
				setExpressionBlockBean(expressionBlockBean);
				setOccupied(true);
			}

			return true;
		} else
			return false;
	}

	/**
	 * Use this method to know whether ExpressionBlockDropZone already has block or
	 * not.
	 *
	 * @return return true ExpressionBlockDropZone has not ExpressionBlockBean.
	 */
	public boolean isOccupied() {
		return this.isOccupied;
	}

	private void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	/**
	 * @return returns the current expressionBlockBean which has occupied block.
	 */
	public ExpressionBlockBean getExpressionBlockBean() {
		return this.expressionBlockBean;
	}

	/**
	 * Sets the value of expressionBlockBean for internal use only, it is not
	 * supposed to be used as
	 * api because this is for internal use of this class only and hence private.
	 */
	private void setExpressionBlockBean(ExpressionBlockBean expressionBlockBean) {
		this.expressionBlockBean = expressionBlockBean;
	}

	public DatatypeBean getDatatypeBean() {
		return this.datatypeBean;
	}

	public void setDatatypeBean(DatatypeBean datatypeBean) {
		this.datatypeBean = datatypeBean;
	}
}
