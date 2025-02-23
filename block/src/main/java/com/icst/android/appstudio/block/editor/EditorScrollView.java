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

package com.icst.android.appstudio.block.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class EditorScrollView extends FrameLayout {
	private int validScroll;
	private boolean isMinimumScrollValueAcheived;
	private float initialX;
	private float initialY;
	private float initialXScrollTouch;
	private float initialYScrollTouch;
	private boolean allowScroll;
	private boolean forceDisableScroll;

	public EditorScrollView(final Context context, final AttributeSet set) {
		super(context, set);
		/*
		 * Saves initial X and Y coordinates for onInterceptTouchEvent calculations.
		 * Setting -1.0f in variable represent that user is not touching the view
		 * currently
		 * and ACTION_DOWN is not performed yet.
		 */
		initialX = -1.0f;
		initialY = -1.0f;
		initialXScrollTouch = -1.0f;
		initialYScrollTouch = -1.0f;

		// Get the value to compare a difference when it should be considered as a
		// scroll.
		validScroll = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		/*
		 * Force disable scroll
		 */
		if (isDisabledScrollForcefully())
			return false;

		/*
		 * Checks if scroll is allowed or not.
		 * It scroll is not allowed then check if view is not scrolled.
		 * If view is scrolled and scroll is not allowed then allow to scroll
		 * to go back at 0 in X and Y direction.
		 */
		if (!getAllowScroll()) {
			if ((getScrollX() == 0) || (getScrollY() == 0))
				return false;
		}

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			/*
			 * Initial X and Y point should be kept for calculation on scroll change.
			 * These initial values of X and Y will be saved in initialX and initialY.
			 */
			initialX = event.getX();
			initialY = event.getY();
			isMinimumScrollValueAcheived = false;
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			initialX = -1.0f;
			initialY = -1.0f;
			isMinimumScrollValueAcheived = false;
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			/*
			 * Checks whether minimum drag is performed on screen that can be considered as
			 * scroll.
			 * It check the minimum scroll value is acheived on X axis or not.
			 */
			if (Math.abs(event.getX() - initialX) > validScroll) {
				isMinimumScrollValueAcheived = true;
			}
			/*
			 * Checks whether minimum drag is performed on screen that can be considered as
			 * scroll.
			 * It check the minimum scroll value is acheived on Y axis or not.
			 */
			if (Math.abs(event.getY() - initialY) > validScroll) {
				isMinimumScrollValueAcheived = true;
			}
		}

		return isMinimumScrollValueAcheived;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!getAllowScroll()) {
			if ((getScrollX() == 0) || (getScrollY() == 0))
				return false;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			/*
			 * Initial X and Y point should be kept for calculation on scroll change.
			 * These initial values of X and Y will be saved in initialXScrollTouch and
			 * initialYScrollTouch.
			 */
			initialXScrollTouch = event.getX();
			initialYScrollTouch = event.getY();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			initialXScrollTouch = -1.0f;
			initialYScrollTouch = -1.0f;
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			/*
			 * If ACTION_DOWN didn't setted initialXScrollTouch and initialYScrollTouch then
			 * set it now.
			 */
			if (initialXScrollTouch < 0.0f) {
				initialXScrollTouch = event.getX();
			}
			if (initialYScrollTouch < 0.0f) {
				initialYScrollTouch = event.getY();
			}
			// Calculate the scroll in scrollX and scrollY
			float scrolledX = initialXScrollTouch - event.getX();
			float scrolledY = initialYScrollTouch - event.getY();

			// Valid scroll difference
			float scrollX = 0f;
			float scrollY = 0f;

			/*
			 * Set initialXScrollTouch and initialYScrollTouch to current motion
			 * as scroll is calculated
			 */
			initialXScrollTouch = event.getX();
			initialYScrollTouch = event.getY();

			if (scrolledX < 0) {
				/*
				 * Scroll direction is negative in x axis.
				 * Prevent from scrolling this direction in negative.
				 */
				if (Math.abs(scrolledX) <= getScrollX()) {
					scrollX = scrolledX;
				}
			} else {
				if (getParent() != null) {
					if (getScrollX() + ((View) getParent()).getWidth() < getWidth()) {
						scrollX = scrolledX;
					}
				}
			}
			if (scrolledY < 0) {
				/*
				 * Scroll direction is negative in y axis.
				 * Prevent from scrolling this direction in negative.
				 */
				if (Math.abs(scrolledY) <= getScrollY()) {
					scrollY = scrolledY;
				}
			} else {
				if (getParent() != null) {
					if (getScrollY() + ((View) getParent()).getHeight() < getHeight()) {
						scrollY = scrolledY;
					}
				}
			}
			/*
			 * Scroll the editor with valid scroll values
			 */
			scrollBy((int) scrollX, (int) scrollY);
		}
		return true;
	}

	public boolean getAllowScroll() {
		return this.allowScroll;
	}

	public void setAllowScroll(boolean allowScroll) {
		this.allowScroll = allowScroll;
	}

	public boolean isDisabledScrollForcefully() {
		return this.forceDisableScroll;
	}

	public void setDisableScrollForcefully(boolean forceDisableScroll) {
		this.forceDisableScroll = forceDisableScroll;
	}
}
