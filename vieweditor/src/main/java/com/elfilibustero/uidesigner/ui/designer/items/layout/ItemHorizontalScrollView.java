package com.elfilibustero.uidesigner.ui.designer.items.layout;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

public class ItemHorizontalScrollView extends HorizontalScrollView {

	private float lastMotionX = -1.0F;
	private boolean isScrollEnabled = true;
	private final Rect drawingRect = new Rect();
	private final Rect clipRect = new Rect();

	public ItemHorizontalScrollView(Context context) {
		super(context);
		initialize(context);
	}

	private int computeHorizontalScrollRange(Rect rect) {
		int childCount = getChildCount();
		int horizontalRange = 0;
		if (childCount == 0) {
			return 0;
		} else {
			int viewWidth = getWidth();
			int scrollX = getScrollX();
			int rightEdge = scrollX + viewWidth;
			int horizontalFadingEdge = getHorizontalFadingEdgeLength();
			int leftEdge = scrollX;
			if (rect.left > 0) {
				leftEdge = scrollX + horizontalFadingEdge;
			}

			int rightEdgeAdjusted = rightEdge;
			if (rect.right < getChildAt(0).getWidth()) {
				rightEdgeAdjusted = rightEdge - horizontalFadingEdge;
			}

			if (rect.right > rightEdgeAdjusted && rect.left > leftEdge) {
				if (rect.width() > viewWidth) {
					horizontalRange = rect.left - leftEdge;
				} else {
					horizontalRange = rect.right - rightEdgeAdjusted;
				}

				horizontalRange = Math.min(horizontalRange, getChildAt(0).getRight() - rightEdgeAdjusted);
			} else if (rect.left < leftEdge) {
				if (rect.right < rightEdgeAdjusted) {
					if (rect.width() > viewWidth) {
						horizontalRange = -(rightEdgeAdjusted - rect.right);
					} else {
						horizontalRange = -(leftEdge - rect.left);
					}

					horizontalRange = Math.max(horizontalRange, -getScrollX());
				}
			}

			return horizontalRange;
		}
	}

	private void scrollIfNeeded(int position) {
		if (position != 0) {
			scrollBy(position, 0);
		}
	}

	private void initialize(Context context) {
	}

	private boolean isViewPartiallyVisible(View view, int var2, int var3) {
		view.getDrawingRect(drawingRect);
		offsetDescendantRectToMyCoords(view, drawingRect);
		return drawingRect.right + var2 >= getScrollX() && drawingRect.left - var2 <= getScrollX() + var3;
	}

	@Override
	protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
		return computeHorizontalScrollRange(rect);
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
			float motionX = event.getX();
			if (action != 0) {
				if (action != 1) {
					if (action == MotionEvent.ACTION_MOVE) {
						if (lastMotionX < 0.0F) {
							lastMotionX = motionX;
						}

						int deltaX = (int) (lastMotionX - motionX);
						lastMotionX = motionX;
						if (deltaX <= 0) {
							if (getScrollX() <= 0) {
								deltaX = 0;
							}

							deltaX = Math.max(-getScrollX(), deltaX);
						} else {
							int rightEdge = firstChild.getRight() - getScrollX() - getWidth() + getPaddingBottom();
							if (rightEdge > 0) {
								deltaX = Math.min(rightEdge, deltaX);
							} else {
								deltaX = 0;
							}
						}

						if (deltaX != 0) {
							scrollBy(deltaX, 0);
						}
					}
				} else {
					lastMotionX = -1.0F;
				}
			} else {
				lastMotionX = motionX;
			}

			return false;
		}
	}
}