/*
 *  This file is part of Android AppStudio.
 *
 *  Android AppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Android AppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Android AppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.tscodeeditor.android.appstudio.block.editor;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tscodeeditor.android.appstudio.block.R;

public class EditorCanva extends EditorScrollView {
  private boolean b1 = false;

  public EditorCanva(final Context context, final AttributeSet set) {
    super(context, set);
    setAllowScroll(true);
    initEditor(Color.parseColor("#000000"), Color.parseColor("#ffffff"));
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

  public void initEditor(int defineEventColor, int textColor) {
    LinearLayout blockListEditorArea = new LinearLayout(getContext());
    blockListEditorArea.setLayoutParams(
        new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    blockListEditorArea.setOrientation(LinearLayout.VERTICAL);

    LinearLayout defineBlockLayout = new LinearLayout(getContext());
    defineBlockLayout.setLayoutParams(
        new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    defineBlockLayout.setBackgroundResource(R.drawable.define_block);
    defineBlockLayout.setBackgroundTintList(ColorStateList.valueOf(defineEventColor));

    LinearLayout innerLayout = new LinearLayout(getContext());
    innerLayout.setLayoutParams(
        new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

    TextView defineTextView = new TextView(getContext());
    defineTextView.setLayoutParams(
        new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    defineTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
    defineTextView.setText("Define your event here");
    defineTextView.setTextColor(textColor);

    innerLayout.addView(defineTextView);
    defineBlockLayout.addView(innerLayout);
    blockListEditorArea.addView(defineBlockLayout);
    addView(blockListEditorArea);
  }
}
