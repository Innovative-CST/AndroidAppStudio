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

package com.icst.logic.editor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * Custom scrollable view for block-based editor, allowing controlled scrolling
 * behavior with
 * options to restrict or force scroll actions.
 */
public class LogicEditorScrollView extends FrameLayout {

	private int scrollThreshold; // Minimum scroll distance required to consider scroll
	private float initialX; // Initial X coordinate for touch event
	private float initialY; // Initial Y coordinate for touch event
	private boolean allowScroll; // Flag to allow or restrict scrolling

	/* After scroll threshold is met */
	private float initialScrollX; // Initial X coordinate for touch event
	private float initialScrollY; // Initial Y coordinate for touch event

	public LogicEditorScrollView(final Context context, final AttributeSet set) {
		super(context, set);
		/*
		 * Saves initial X and Y coordinates for onInterceptTouchEvent calculations.
		 * Setting -1.0f in variable represent that user has had not touched a view yet.
		 * as ACTION_DOWN is not performed yet.
		 */
		initialX = -1.0f;
		initialY = -1.0f;

		// Defining scroll threshold a values.
		scrollThreshold = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	/**
	 * Return false if you want the event to transfer to its child. Return true if
	 * you do not want the
	 * event to transfer to its child.
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
		// Transfer event to children if scroll is disabled.
		if (!getAllowScroll())
			return false;

		// A boolean that will store if change in motion x, y is greater than scroll
		// thresh hold.
		boolean isScrollThresholdAchieved = false;

		if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
			// Store initial x, y coordinates of touches.
			initialX = motionEvent.getX();
			initialY = motionEvent.getY();
		}

		if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
			// Store initial x, y coordinates of touches to be -1 as view is released.
			initialX = -1.0f;
			initialY = -1.0f;
			isScrollThresholdAchieved = false;
		}

		if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
			// Calculate change in x, y coordinates respect with initial coordinates.
			float deltaX = motionEvent.getX() - initialX;
			float deltaY = motionEvent.getY() - initialY;

			// Store |deltaX| and |deltaY|

			float deltaXMagnitude = Math.abs(deltaX);
			float deltaYMagnitude = Math.abs(deltaY);

			// Compare |deltaX| and |deltaY| with scroll threshold.
			if (deltaXMagnitude > scrollThreshold || deltaYMagnitude > scrollThreshold) {
				isScrollThresholdAchieved = true;
			}
		}
		return isScrollThresholdAchieved;
	}

	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
			// Store initial x, y coordinates of touches(after threshold scroll).
			initialScrollX = motionEvent.getX();
			initialScrollY = motionEvent.getY();
		}

		if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
			/**
			 * Store initial x, y coordinates of touches to be -1 as view is released(after
			 * threshold
			 * scroll was met).
			 */
			initialScrollX = -1.0f;
			initialScrollY = -1.0f;
		}

		if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
			/*
			 * Calculate change in x, y coordinates respect with initial coordinates after
			 * threshold scroll was met.
			 */
			float deltaX = initialScrollX - motionEvent.getX();
			float deltaY = initialScrollY - motionEvent.getY();

			// Store |deltaX| and |deltaY|

			float deltaXMagnitude = Math.abs(deltaX);
			float deltaYMagnitude = Math.abs(deltaY);

			float finalScrollX = 0;
			float finalScrollY = 0;

			if (deltaX < 0) {
				// From left to right is scrolled
				// Make sure user does not scroll greater than current scrollX

				if (deltaXMagnitude <= getScrollX()) {
					finalScrollX = deltaX;
				} else {
					finalScrollX -= getScrollX();
				}
			} else {
				// From down to up is scrolled
				// Scroll if there is more content
				if (getParent() != null) {
					if (getParent() instanceof View parent) {
						if (getScrollX() + parent.getWidth() < getWidth()) {
							finalScrollX = deltaX;
						} else {
							finalScrollX = 0;
						}
					}
				}
			}

			if (deltaY < 0) {
				// From up to down is scrolled
				// Make sure user does not scroll greater than current scrollY

				if (deltaYMagnitude <= getScrollY()) {
					finalScrollY = deltaY;
				} else {
					finalScrollY -= getScrollY();
				}
			} else {
				// From down to up is scrolled
				// Scroll if there is more content
				if (getParent() != null) {
					if (getParent() instanceof View parent) {
						if (getScrollY() + parent.getHeight() < getHeight()) {
							finalScrollY = deltaY;
						} else {
							finalScrollY = 0;
						}
					}
				}
			}

			// Reset initial touch
			initialScrollX = motionEvent.getX();
			initialScrollY = motionEvent.getY();
			// Scroll to calculated delta coordinates
			scrollBy((int) (finalScrollX), (int) (finalScrollY));
		}
		return true;
	}

	public boolean getAllowScroll() {
		return this.allowScroll;
	}

	public void setAllowScroll(boolean allowScroll) {
		this.allowScroll = allowScroll;
	}
}
