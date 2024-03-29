/*
 * This file is part of Android AppStudio [https://github.com/TS-Code-Editor/AndroidAppStudio].
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

package com.tscodeeditor.android.appstudio.block.editor;

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

  public EditorScrollView(final Context context, final AttributeSet set) {
    super(context, set);
    /*
     * Saves initial X and Y coordinates for onInterceptTouchEvent calculations.
     * Setting -1.0f in variable represent that user is not touching the view currently
     * and ACTION_DOWN is not performed yet.
     */
    initialX = -1.0f;
    initialY = -1.0f;
    initialXScrollTouch = -1.0f;
    initialYScrollTouch = -1.0f;

    // Get the value to compare a difference when it should be considered as a scroll.
    validScroll = ViewConfiguration.get(context).getScaledTouchSlop();
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    /*
     * Checks if scroll is allowed or not.
     * It scroll is not allowed then check if view is not scrolled.
     * If view is scrolled and scroll is not allowed then allow to scroll
     * to go back at 0 in X and Y direction.
     */
    if (!getAllowScroll()) {
      if ((getScrollX() == 0) || !(getScrollY() == 0)) return false;
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
       * Checks whether minimum drag is performed on screen that can be considered as scroll.
       * It check the minimum scroll value is acheived on X axis or not.
       */
      if (Math.abs(event.getX() - initialX) > validScroll) {
        isMinimumScrollValueAcheived = true;
      }
      /*
       * Checks whether minimum drag is performed on screen that can be considered as scroll.
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
      if ((getScrollX() == 0) || !(getScrollY() == 0)) return false;
    }
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      /*
       * Initial X and Y point should be kept for calculation on scroll change.
       * These initial values of X and Y will be saved in initialXScrollTouch and initialYScrollTouch.
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
       * If ACTION_DOWN didn't setted initialXScrollTouch and initialYScrollTouch then set it now.
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
}
