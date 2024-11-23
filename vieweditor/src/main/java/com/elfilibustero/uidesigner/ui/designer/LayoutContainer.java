package com.elfilibustero.uidesigner.ui.designer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.blankj.utilcode.util.ToastUtils;
import com.elfilibustero.uidesigner.lib.tool.DynamicViewFactory;
import com.elfilibustero.uidesigner.lib.utils.Constants;
import com.elfilibustero.uidesigner.lib.utils.RecylerViewItemAnimator;
import com.elfilibustero.uidesigner.lib.utils.Utils;
import com.elfilibustero.uidesigner.lib.view.ShadowView;
import com.elfilibustero.uidesigner.ui.designer.items.UnknownView;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public class LayoutContainer extends FrameLayout {
	private float defaultWidthHeight;
	private Droppable droppable = null;
	private List<Droppable> droppables = new ArrayList<>();
	private float scaleX = 1.0f;
	private float scaleY = 1.0f;
	private ShadowView shadow;

	public LayoutContainer(Context context) {
		super(context);
		this.init(context);
	}

	public LayoutContainer(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.init(context);
	}

	private void init(Context context) {
		this.shadow = new ShadowView(context);
		this.defaultWidthHeight = Utils.getDip(context, 32.0f);
		this.scaleX = this.getScaleX();
		this.scaleY = this.getScaleY();
	}

	@Override
	public void addView(View view, int index) {
		if (view != shadow && getChildCount() >= 1) {
			ToastUtils.showShort("Editor already has a root.");
			return;
		}
		super.addView(view, index);
	}

	public void setDefaultWidthAndHeight(View view) {
		if (view instanceof ViewGroup && !(view instanceof DesignerItem)) {
			view.setMinimumWidth((int) defaultWidthHeight);
			int minHeight = (view instanceof Toolbar)
					? Utils.getActionBarSize(getContext())
					: (int) defaultWidthHeight;
			view.setMinimumHeight(minHeight);
		}
		if (view instanceof ConstraintLayout) {
			ConstraintLayout constraintLayout = (ConstraintLayout) view;
			constraintLayout.setMinWidth((int) defaultWidthHeight);
			constraintLayout.setMinHeight((int) defaultWidthHeight);
		}
	}

	public void setTransition(View view) {
		try {
			if (view instanceof ViewGroup viewGroup) {
				if (view instanceof RecyclerView recyclerView) {
					recyclerView.setItemAnimator(new RecylerViewItemAnimator());
				} else {
					LayoutTransition layoutTransition = new LayoutTransition();
					layoutTransition.disableTransitionType(LayoutTransition.APPEARING);
					layoutTransition.disableTransitionType(LayoutTransition.DISAPPEARING);
					layoutTransition.disableTransitionType(LayoutTransition.CHANGE_APPEARING);
					layoutTransition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
					layoutTransition.setDuration(150L);
					viewGroup.setLayoutTransition(layoutTransition);
				}
			}
		} catch (Exception ignored) {
			// Ignoring exception
		}
	}

	public boolean addItem(View view) {
		try {
			setDefaultWidthAndHeight(view);
			setTransition(view);

			if (droppable == null) {
				return false;
			}

			ViewGroup parentView = (ViewGroup) this.droppable.getView();
			int index = this.droppable.getIndex();

			var layoutParams = (ViewGroup.LayoutParams) DynamicViewFactory.getLayoutParamsFor(
					parentView,
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(layoutParams);

			parentView.addView(view, index);

			return true;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return false;
		}
	}

	public boolean updateItem(View view) {
		if (droppable == null) {
			return false;
		}

		ViewGroup viewGroup = (ViewGroup) view.getParent();
		ViewGroup targetViewGroup = (ViewGroup) droppable.getView();

		int currentIndex = viewGroup.indexOfChild(view);
		int targetIndex = droppable.getIndex();

		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		int width = layoutParams.width;
		int height = layoutParams.height;

		layoutParams = (ViewGroup.LayoutParams) DynamicViewFactory.getLayoutParamsFor(targetViewGroup, width, height);

		if (viewGroup != null && viewGroup != targetViewGroup) {
			LayoutTransition layoutTransition = viewGroup.getLayoutTransition();
			viewGroup.setLayoutTransition(null);
			viewGroup.removeView(view);
			viewGroup.setLayoutTransition(layoutTransition);
			targetViewGroup.addView(view, targetIndex, layoutParams);
			return true;
		}

		if (currentIndex != targetIndex) {
			viewGroup.removeView(view);
			if (currentIndex < targetIndex) {
				targetIndex--;
			}
			try {
				targetViewGroup.addView(view, targetIndex, layoutParams);
				return true;
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
		return false;
	}

	public void removeItem(View view) {
		try {
			var parent = view.getParent();
			if (parent != null && parent instanceof ViewGroup viewGroup) {
				if (view != null)
					viewGroup.removeView(view);
				viewGroup.requestLayout();
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	public void bringToFront(View view) {
		var parent = (ViewGroup) view.getParent();
		if (parent != null) {
			removeItem(view);
			parent.addView(view, parent.getChildCount());
		}
	}

	public void sendToBack(View view) {
		var parent = (ViewGroup) view.getParent();
		if (parent != null) {
			removeItem(view);
			parent.addView(view, 0);
		}
	}

	private void addDroppable(Droppable droppable) {
		droppables.add(droppable);
	}

	private void addDroppableFor(View view, View draggedView) {
		if (view instanceof UnknownView unknownView) {
			addDroppableFor(unknownView, draggedView);
		} else if (view instanceof LinearLayout linearLayout) {
			addDroppableFor(linearLayout, draggedView);
		} else if ((view instanceof HorizontalScrollView)
				|| (view instanceof ScrollView)
				|| (view instanceof NestedScrollView)) {
			addScrollDroppableFor((ViewGroup) view, draggedView);
		} else if (view instanceof ViewGroup viewGroup
				&& !Constants.isExcludedViewGroup(viewGroup)) {
			addDroppableFor(viewGroup, draggedView);
		}
	}

	private void addDroppableFor(ViewGroup viewGroup, View draggedView) {
		Droppable droppable = new Droppable(getRectFor(viewGroup), viewGroup, -1, getDepth(viewGroup));
		addDroppable(droppable);

		IntStream.range(0, viewGroup.getChildCount())
				.mapToObj(viewGroup::getChildAt)
				.filter(Objects::nonNull)
				.filter(childView -> childView.getVisibility() != View.GONE)
				.forEach(childView -> addDroppableFor(childView, draggedView));
	}

	private void addDroppableFor(LinearLayout linearLayout, View draggedView) {
		Rect parentRect = getRectFor(linearLayout);
		addDroppable(new Droppable(parentRect, linearLayout, -1, getDepth(linearLayout)));
		int parentWidth = (int) (linearLayout.getMeasuredWidth() * scaleX);
		int parentHeight = (int) (linearLayout.getMeasuredHeight() * scaleY);
		int paddingLeft = (int) (linearLayout.getPaddingLeft() * scaleX);
		int paddingTop = (int) (linearLayout.getPaddingTop() * scaleY);

		for (int i = 0; i < linearLayout.getChildCount(); ++i) {
			View childView = linearLayout.getChildAt(i);
			if (childView == null
					|| childView.getVisibility() == View.GONE
					|| childView == draggedView) {
				continue;
			}
			Rect childRect = getRectFor(childView);
			var layoutParams = (LinearLayout.LayoutParams) childView.getLayoutParams();
			int leftMargin = (int) (layoutParams.leftMargin * scaleX);
			int rightMargin = (int) (layoutParams.rightMargin * scaleX);
			int topMargin = (int) (layoutParams.topMargin * scaleY);
			int bottomMargin = (int) (layoutParams.bottomMargin * scaleY);
			int childWidth = (int) (childView.getMeasuredWidth() * linearLayout.getScaleX());
			int childHeight = (int) (childView.getMeasuredHeight() * linearLayout.getScaleY());

			if (linearLayout.getOrientation() == LinearLayout.VERTICAL) {
				int childTop = i == 0
						? childRect.top
								- (int) (topMargin * scaleX)
								+ (int) ((topMargin + childHeight + bottomMargin) * scaleY)
						: (int) ((topMargin + childHeight + bottomMargin) * scaleY)
								+ paddingTop;
				childRect.bottom = childTop;
				childRect.top = paddingTop;
				childRect.right = paddingLeft + parentWidth + parentRect.left;
				childRect.left = parentRect.left;
				paddingTop = childRect.bottom;
			} else {
				int childLeft = i == 0
						? childRect.left
								- (int) (leftMargin * scaleY)
								+ (int) ((leftMargin + childWidth + rightMargin) * scaleX)
						: (int) ((leftMargin + childWidth + rightMargin) * scaleX)
								+ paddingLeft;
				childRect.right = childLeft;
				childRect.top = parentRect.top;
				childRect.bottom = paddingTop + parentHeight + parentRect.top;
				childRect.left = paddingLeft;
				paddingLeft = childRect.right;
			}
			addDroppable(new Droppable(childRect, linearLayout, i, getDepth(linearLayout) + 1));

			addDroppableFor(childView, draggedView);
		}
	}

	private void addDroppableFor(UnknownView unknownView, View draggedView) {
		addDroppable(
				new Droppable(getRectFor(unknownView), unknownView, -1, getDepth(unknownView)));

		Stream.of(unknownView)
				.flatMap(
						view -> IntStream.range(0, view.getChildCount())
								.mapToObj(view::getChildAt)
								.filter(Objects::nonNull)
								.filter(
										childView -> childView.getVisibility() != View.GONE))
				.forEach(childView -> addDroppableFor(childView, draggedView));
	}

	private void addScrollDroppableFor(ViewGroup viewGroup, View draggedView) {
		long visibleChildCount = IntStream.range(0, viewGroup.getChildCount())
				.mapToObj(viewGroup::getChildAt)
				.filter(Objects::nonNull)
				.filter(childView -> childView.getVisibility() != View.GONE)
				.peek(childView -> addDroppableFor(childView, draggedView))
				.count();

		if (visibleChildCount < 1) {
			addDroppable(new Droppable(getRectFor(viewGroup), viewGroup, -1, getDepth(viewGroup)));
		}
	}

	private View findScaledParent(View view) {
		var parent = view.getParent();
		if (parent instanceof View parentView) {
			if (parentView.getScaleX() == 1.0f && parentView.getScaleY() == 1.0f) {
				return findScaledParent(parentView);
			}
			return parentView;
		}
		return null;
	}

	private Droppable getDroppableFor(int x, int y) {
		return droppables.stream()
				.filter(
						droppable -> {
							Rect rect = droppable.getRect();
							return x >= rect.left
									&& x < rect.right
									&& y >= rect.top
									&& y < rect.bottom;
						})
				.max(Comparator.comparingInt(Droppable::getDepth))
				.orElse(null);
	}

	private Rect getRectFor(View view) {
		var rect = new Rect();
		view.getGlobalVisibleRect(rect);
		int scaledWidth = (int) (view.getWidth() * scaleX);
		int scaledHeight = (int) (view.getHeight() * scaleY);
		rect.right = rect.left + scaledWidth;
		rect.bottom = rect.top + scaledHeight;
		return rect;
	}

	public void findDroppable(View view) {
		View scaledParent = findScaledParent(this);
		if (scaledParent != null) {
			scaleX = scaledParent.getScaleX();
			scaleY = scaledParent.getScaleY();
		} else {
			scaleX = getScaleX();
			scaleY = getScaleY();
		}
		addDroppableFor(this, view);
	}

	public int getDepth(View view) {
		if (view != null)
			return getDepth(view, 0);
		return 0;
	}

	private int getDepth(View view, int depth) {
		int maxDepth = depth;
		var parent = view.getParent();
		if (parent != null && parent instanceof View parentView) {
			int parentDepth = getDepth(parentView, depth + 2);
			if (parentDepth > maxDepth) {
				maxDepth = parentDepth;
			}
		}
		return maxDepth;
	}

	public void resetDragging() {
		resetDroppable(true);
		droppables.clear();
	}

	public void resetDroppable(boolean clearDroppable) {
		removeItem(shadow);
		if (clearDroppable) {
			droppable = null;
		}
	}

	public void updateDroppableFor(int x, int y) {
		Droppable newDroppable = getDroppableFor(x, y);

		if (newDroppable != droppable && newDroppable != null) {
			resetDroppable(true);
			if (newDroppable.getView() instanceof ViewGroup parentView) {
				parentView.addView(shadow, newDroppable.getIndex());
				droppable = newDroppable;
			}
		} else if (newDroppable == null) {
			try {
				resetDroppable(true);
			} catch (IllegalStateException ignored) {
				// Ignoring exception
			}
		}
	}

	public class Droppable {
		private Rect rect;
		private View view;
		private int index;
		private int depth;

		public Droppable(Rect rect, View view, int index, int depth) {
			this.rect = rect;
			this.view = view;
			this.index = index;
			this.depth = depth;
		}

		public Rect getRect() {
			return rect;
		}

		public View getView() {
			return view;
		}

		public int getIndex() {
			return index;
		}

		public int getDepth() {
			return depth;
		}
	}
}
