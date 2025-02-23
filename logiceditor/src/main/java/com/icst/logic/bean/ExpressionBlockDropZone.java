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

package com.icst.logic.bean;

import com.icst.android.appstudio.beans.DatatypeBean;
import com.icst.android.appstudio.beans.ExpressionBlockBean;
import com.icst.android.appstudio.beans.utils.BlockBeanUtils;

/** ExpressionBlockBean drop zone supposed to be
 * used within the LogicEditor. */
public abstract class ExpressionBlockDropZone extends BlockDropZone {

	/** Marks whether ExpressionBlockDropZone is empty or occupied by a
	 * ExpressionBlockBean. */
	private boolean isOccupied;

	/** ExpressionBlockBean that occupied this ExpressionBlockDropZone. */
	private ExpressionBlockBean expressionBlockBean;

	private DatatypeBean datatypeBean;

	/** Execute when the ExpressionBlockBean is dropped inside BlockDropZoneBean and
	 * BlockDropZoneBean
	 * was not previously occupied.
	 *
	 * @param expressionBlockBean
	 *            The ExpressionBlockBean which is just dropped. */
	protected abstract void onExpressionBlockBeanDropped(ExpressionBlockBean expressionBlockBean);

	/** Execute when the ExpressionBlockBean is dropped inside BlockDropZoneBean and
	 * BlockDropZoneBean
	 * was previously occupied.
	 *
	 * @param droppedExpressionBlockBean
	 *            The ExpressionBlockBean which is just dropped.
	 * @param oldExpressionBlockBean
	 *            The previously ExpressionBlockBean which is replaced by a new new
	 *            one. */
	protected abstract void onExpressionBlockBeanDropped(
			ExpressionBlockBean droppedExpressionBlockBean, ExpressionBlockBean oldExpressionBlockBean);

	/** Use this method to know whether a ExpressionBlockBean is droppable inside a
	 * ExpressionBlockDropZone or not.
	 *
	 * @param mExpressionBlockBean
	 *            Instance of ExpressionBlockBean to check if it is droppable or
	 *            not.
	 * @return return true if @mExpressionBlockBean is droppable inside
	 *         ExpressionBlockDropZone or
	 *         not. */
	protected boolean canDropBlockBean(ExpressionBlockBean mExpressionBlockBean) {
		return BlockBeanUtils.arrayContainsDatatypeBeans(
				mExpressionBlockBean.getReturnDatatypes(), datatypeBean);
	}

	/** Use this method to drop the ExpressionBlockBean in ExpressionBlockDropZone.
	 *
	 * @param expressionBlockBean
	 *            Instance of ExpressionBlockBean to drop inside the
	 *            ExpressionBlockDropZone.
	 * @return return true if expressionBlockBean is dropped, otherwise false. */
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

	/** Use this method to know whether ExpressionBlockDropZone already has block or
	 * not.
	 *
	 * @return return true ExpressionBlockDropZone has not ExpressionBlockBean. */
	public boolean isOccupied() {
		return this.isOccupied;
	}

	private void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	/** @return returns the current expressionBlockBean which has occupied block. */
	public ExpressionBlockBean getExpressionBlockBean() {
		return this.expressionBlockBean;
	}

	/** Sets the value of expressionBlockBean for internal use only, it is not
	 * supposed to be used as
	 * api because this is for internal use of this class only and hence private. */
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
