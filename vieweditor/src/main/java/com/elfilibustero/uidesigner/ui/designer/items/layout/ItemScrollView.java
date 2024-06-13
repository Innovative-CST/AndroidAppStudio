package com.elfilibustero.uidesigner.ui.designer.items.layout;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.elfilibustero.uidesigner.lib.utils.Utils;
import com.elfilibustero.uidesigner.ui.designer.DesignerItem;

public class ItemScrollView extends ScrollView implements DesignerItem {

    private float lastMotionY = -1.0F;
    private boolean isScrollEnabled = true;
    private final Rect drawingRect = new Rect();
    private final Rect clipRect = new Rect();

    public ItemScrollView(Context context) {
        super(context);
        initialize(context);
    }
    
    private void initialize(Context context) {
        setMinimumWidth((int) Utils.getDip(context, 32.0f));
        setMinimumHeight((int) Utils.getDip(context, 32.0f));
    }

    private String className;

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public Class<?> getClassType() {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return ScrollView.class;
        }
    }

    @Override
    public void addView(View child, int index) {
        int childCount = getChildCount();
        if (index > childCount) {
            super.addView(child);
        } else {
            int hiddenIndex = -1;
            for (int i = 0; i < childCount; i++) {
                if (getChildAt(i).getVisibility() == View.GONE) {
                    hiddenIndex = i;
                    break;
                }
            }
            if (hiddenIndex >= 0 && index >= hiddenIndex) {
                super.addView(child, index + 1);
            } else {
                super.addView(child, index);
            }
        }
    }

    @Override
    public void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
        parentWidthMeasureSpec =
                FrameLayout.getChildMeasureSpec(
                        parentWidthMeasureSpec,
                        getPaddingLeft() + getPaddingRight(),
                        layoutParams.width);
        child.measure(
                parentWidthMeasureSpec,
                MeasureSpec.makeMeasureSpec(
                        Math.max(
                                0,
                                MeasureSpec.getSize(parentHeightMeasureSpec)
                                        - (getPaddingTop() + getPaddingBottom())),
                        MeasureSpec.UNSPECIFIED));
    }

    @Override
    public void measureChildWithMargins(
            View child,
            int parentWidthMeasureSpec,
            int widthUsed,
            int parentHeightMeasureSpec,
            int heightUsed) {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int childMeasureSpec =
                FrameLayout.getChildMeasureSpec(
                        parentWidthMeasureSpec,
                        getPaddingLeft()
                                + getPaddingRight()
                                + layoutParams.leftMargin
                                + layoutParams.rightMargin
                                + widthUsed,
                        layoutParams.width);
        parentWidthMeasureSpec = getPaddingTop();
        widthUsed = getPaddingBottom();
        child.measure(
                childMeasureSpec,
                MeasureSpec.makeMeasureSpec(
                        Math.max(
                                0,
                                MeasureSpec.getSize(parentHeightMeasureSpec)
                                        - (parentWidthMeasureSpec
                                                + widthUsed
                                                + layoutParams.topMargin
                                                + layoutParams.bottomMargin
                                                + heightUsed)),
                        MeasureSpec.UNSPECIFIED));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isScrollEnabled) {
            return false;
        } else if (getChildCount() <= 0) {
            return false;
        } else {
            View firstChild = getChildAt(0);
            int action = event.getAction();
            float motionY = event.getY();
            if (action != 0) {
                if (action != 1) {
                    if (action == MotionEvent.ACTION_MOVE) {
                        if (lastMotionY < 0.0F) {
                            lastMotionY = motionY;
                        }
                        int deltaY = (int) (lastMotionY - motionY);
                        lastMotionY = motionY;
                        if (deltaY <= 0) {
                            if (getScrollY() <= 0) {
                                deltaY = 0;
                            }

                            deltaY = Math.max(-getScrollY(), deltaY);
                        } else {
                            int bottomEdge =
                                    firstChild.getBottom()
                                            - getScrollY()
                                            - getHeight()
                                            + getPaddingRight();
                            if (bottomEdge > 0) {
                                deltaY = Math.min(bottomEdge, deltaY);
                            } else {
                                deltaY = 0;
                            }
                        }

                        if (deltaY != 0) {
                            scrollBy(0, deltaY);
                        }
                    }
                } else {
                    lastMotionY = -1.0F;
                }
            } else {
                lastMotionY = motionY;
            }

            return false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED) {
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
                int paddingLeft = getPaddingLeft();
                int measuringSize = getMeasuredHeight() - (getPaddingTop() + getPaddingBottom());
                if (child.getMeasuredHeight() < measuringSize) {
                    child.measure(
                            FrameLayout.getChildMeasureSpec(
                                    widthMeasureSpec,
                                    paddingLeft + getPaddingRight(),
                                    layoutParams.width),
                            MeasureSpec.makeMeasureSpec(measuringSize, MeasureSpec.EXACTLY));
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        View focusedView = findFocus();
        if (focusedView != null
                && this != focusedView
                && isViewVisible(focusedView, 0, oldHeight)) {
            focusedView.getDrawingRect(drawingRect);
            offsetDescendantRectToMyCoords(focusedView, drawingRect);
            scrollBy(getVerticalScrollRange(drawingRect));
        }
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        setScrollY(0);
    }

    public void setScrollEnabled(boolean isScrollEnabled) {
        this.isScrollEnabled = isScrollEnabled;
    }
    
    private void scrollBy(int position) {
        if (position != 0) {
            scrollBy(0, position);
        }
    }

    private boolean isViewVisible(View view, int var2, int var3) {
        view.getDrawingRect(drawingRect);
        offsetDescendantRectToMyCoords(view, drawingRect);
        return drawingRect.bottom + var2 >= getScrollY()
                && drawingRect.top - var2 <= getScrollY() + var3;
    }
    
    private int getVerticalScrollRange(Rect rect) {
        int childCount = getChildCount();
        int verticalRange = 0;
        if (childCount == 0) {
            return 0;
        } else {
            int viewHeight = getHeight();
            int scrollY = getScrollY();
            int bottomEdge = scrollY + viewHeight;
            int verticalFadingEdge = getVerticalFadingEdgeLength();
            int topEdge = scrollY;
            if (rect.top > 0) {
                topEdge = scrollY + verticalFadingEdge;
            }

            int bottomEdgeAdjusted = bottomEdge;
            if (rect.bottom < getChildAt(0).getHeight()) {
                bottomEdgeAdjusted = bottomEdge - verticalFadingEdge;
            }

            if (rect.bottom > bottomEdgeAdjusted && rect.top > topEdge) {
                if (rect.height() > viewHeight) {
                    verticalRange = rect.top - topEdge;
                } else {
                    verticalRange = rect.bottom - bottomEdgeAdjusted;
                }

                verticalRange =
                        Math.min(verticalRange, getChildAt(0).getBottom() - bottomEdgeAdjusted);
            } else if (rect.top < topEdge) {
                if (rect.bottom < bottomEdgeAdjusted) {
                    if (rect.height() > viewHeight) {
                        verticalRange = -(bottomEdgeAdjusted - rect.bottom);
                    } else {
                        verticalRange = -(topEdge - rect.top);
                    }

                    verticalRange = Math.max(verticalRange, -getScrollY());
                }
            }

            return verticalRange;
        }
    }
}
