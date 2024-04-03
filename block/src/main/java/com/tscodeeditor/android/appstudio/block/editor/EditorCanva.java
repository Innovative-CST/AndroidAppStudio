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
import android.widget.LinearLayout;
import com.tscodeeditor.android.appstudio.block.R;
import com.tscodeeditor.android.appstudio.block.model.Event;
import com.tscodeeditor.android.appstudio.block.utils.BlockMarginConstants;
import com.tscodeeditor.android.appstudio.block.utils.UnitUtils;
import com.tscodeeditor.android.appstudio.block.view.BlockView;

public class EditorCanva extends EditorScrollView {
  private boolean b1 = false;

  private Event event;
  public LinearLayout attachedBlockLayout;

  public EditorCanva(final Context context, final AttributeSet set) {
    super(context, set);
    setAllowScroll(true);
    setClipChildren(true);
  }

  public void setUpDimension() {
    getLayoutParams().width = getRight() - getLeft();
    getLayoutParams().height = getBottom() - getTop();

    final int childCount = getChildCount();
    int width = 0;
    int width2 = 0;
    width = getLayoutParams().width;
    width2 = getLayoutParams().height;
    int max;
    int max2;

    for (int i = 0; i < getChildCount(); ++i) {
      final View child = getChildAt(i);
      final float x = child.getX();
      max =
          Math.max(
              (int)
                  (child.getX()
                      + child.getPaddingLeft()
                      + child.getWidth()
                      + child.getPaddingRight()
                      + 150),
              width);
      width = max;
    }
    for (int i = 0; i < getChildCount(); ++i) {
      final View child = getChildAt(i);
      final float x = child.getX();
      max2 =
          Math.max(
              (int)
                  (child.getY()
                      + child.getPaddingTop()
                      + child.getHeight()
                      + child.getPaddingBottom()
                      + 150),
              width2);
      width2 = max2;
    }
    getLayoutParams().width = width;
    getLayoutParams().height = width2;
    requestLayout();
  }

  @Override
  protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    super.onLayout(arg0, arg1, arg2, arg3, arg4);
    setUpDimension();
  }

  @Override
  protected void onScrollChanged(int arg0, int arg1, int arg2, int arg3) {
    super.onScrollChanged(arg0, arg1, arg2, arg3);
    setUpDimension();
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent motion) {
    if (!super.onInterceptTouchEvent(motion)) {
      setUpDimension();
    }
    return super.onInterceptTouchEvent(motion);
  }

  @Override
  public boolean onTouchEvent(MotionEvent motion) {
    if (!super.onTouchEvent(motion)) {
      setUpDimension();
    }
    return super.onTouchEvent(motion);
  }

  public void initEditor(Event event, EventEditor editor) {
    this.event = event;
    attachedBlockLayout = new LinearLayout(getContext());
    attachedBlockLayout.setLayoutParams(
        new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    attachedBlockLayout.setOrientation(LinearLayout.VERTICAL);
    attachedBlockLayout.setId(R.id.attachedBlockLayout);

    BlockView defineBlock = new BlockView(null, getContext(), event.getEventTopBlock());
    defineBlock.setLayoutParams(
        new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    attachedBlockLayout.addView(defineBlock);
    addView(attachedBlockLayout);
    loadBlock(editor);
  }

  public void loadBlock(EventEditor editor) {
    if (event.getBlockModels() == null) return;
    if (attachedBlockLayout == null) return;

    for (int i = 0; i < event.getBlockModels().size(); ++i) {
      BlockView blockView = new BlockView(editor, getContext(), event.getBlockModels().get(i));
      LinearLayout.LayoutParams blockParams =
          new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

      blockParams.setMargins(
          0, UnitUtils.dpToPx(getContext(), BlockMarginConstants.regularBlockMargin), 0, 0);

      blockView.setLayoutParams(blockParams);
      blockView.setEnableDragDrop(true);
      blockView.setEnableEditing(true);
      blockView.setInsideEditor(true);
      attachedBlockLayout.addView(blockView);
    }
  }

  public Event getEvent() {
    return this.event;
  }
}
