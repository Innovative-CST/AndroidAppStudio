package com.elfilibustero.uidesigner.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elfilibustero.uidesigner.lib.utils.Constants;
import com.elfilibustero.uidesigner.lib.utils.Utils;
import com.elfilibustero.uidesigner.ui.designer.LayoutContainer;
import com.google.android.material.color.MaterialColors;
import com.tscodeeditor.android.appstudio.vieweditor.databinding.DeviceViewBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class DeviceView extends RelativeLayout {
    private DeviceViewBinding binding;

    private ViewGroup statusBar;

    private Map<View, Rect> viewsPosition = new HashMap<>();

    private View selectView;
    private Rect selectedRect;

    private Paint paint;
    private Paint childPaint;

    public DeviceView(Context context) {
        super(context);
        init(context);
    }

    public DeviceView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }

    private void init(Context context) {
        binding = DeviceViewBinding.inflate(LayoutInflater.from(context), this, true);
        statusBar = binding.statusBar;
        statusBar.setLayoutParams(
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        Utils.getStatusBarHeight(context)));

        paint = Utils.getDefaultStrokePaint(context);

        childPaint = new Paint();
        childPaint.setStrokeWidth(Utils.getDip(context, 1.0f));
        updateTheme();
    }

    public void addContainer(View view) {
        binding.container.addView(view);
    }

    private void updateTheme() {
        int color = Utils.isDarkMode(getContext()) ? Color.WHITE : Color.BLACK;

        IntStream.range(0, statusBar.getChildCount())
                .mapToObj(statusBar::getChildAt)
                .forEach(
                        child -> {
                            if (child instanceof TextView textView) {
                                textView.setTextColor(color);
                            } else if (child instanceof ImageView imageView) {
                                imageView.setColorFilter(color);
                            }
                        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        updateStroke();
        try {
            super.onLayout(changed, left, top, right, bottom);
        } catch (ClassCastException ignored) {
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        super.dispatchDraw(canvas);
        drawDesign(canvas);
        toSelectPaint(canvas);
    }

    private void drawDesign(Canvas canvas) {
        childPaint.setStyle(Paint.Style.STROKE);
        childPaint.setColor(
                MaterialColors.getColor(
                        getContext(), com.google.android.material.R.attr.colorOutlineVariant, 0));
        viewsPosition.entrySet().stream()
                .filter(entry -> !isParentNotVisible(entry.getKey()))
                .forEach(entry -> canvas.drawRect(fixRect(entry.getValue()), childPaint));
    }

    private void toSelectPaint(Canvas canvas) {
        childPaint.setStyle(Paint.Style.FILL);
        childPaint.setColor(0xFF1886f7);

        viewsPosition.entrySet().stream()
                .filter(entry -> selectedRect != null && selectedRect.equals(entry.getValue()))
                .forEach(
                        entry -> {
                            int size = 20;
                            int offset = 8;

                            Rect rect = entry.getValue();

                            canvas.drawRect(
                                    rect.left - offset,
                                    rect.top - offset,
                                    rect.left + size - offset,
                                    rect.top + size - offset,
                                    childPaint);

                            canvas.drawRect(
                                    rect.right - size + offset,
                                    rect.top - offset,
                                    rect.right + offset,
                                    rect.top + size - offset,
                                    childPaint);

                            canvas.drawRect(
                                    rect.left - offset,
                                    rect.bottom - size + offset,
                                    rect.left + size - offset,
                                    rect.bottom + offset,
                                    childPaint);

                            canvas.drawRect(
                                    rect.right - size + offset,
                                    rect.bottom - size + offset,
                                    rect.right + offset,
                                    rect.bottom + offset,
                                    childPaint);

                            childPaint.setStyle(Paint.Style.STROKE);
                            canvas.drawRect(fixRect(rect), childPaint);
                        });
    }

    private boolean isParentNotVisible(View view) {
        View currentView = view;

        while (currentView != null && !(currentView instanceof LayoutContainer)) {
            if (currentView.getVisibility() == View.GONE) {
                return true;
            }
            currentView = (View) currentView.getParent();
        }
        return false;
    }

    private Rect fixRect(Rect rect) {
        Rect r = new Rect(rect);
        int half = (int) paint.getStrokeWidth() / 2;
        r.left += half;
        r.top += half;
        r.right -= half;
        r.bottom -= half;
        return r;
    }

    public void select(View v) {
        viewsPosition.entrySet().stream()
                .filter(entry -> v.equals(entry.getKey()))
                .findFirst()
                .ifPresent(
                        entry -> {
                            selectedRect = entry.getValue();
                            selectView = v;
                        });
    }

    public void removeSelection() {
        selectedRect = null;
        selectView = null;
        invalidate();
    }

    private void updateStroke() {
        viewsPosition.clear();
        updateStroke(binding.container);
    }

    private Rect getRectFor(View view) {
        Rect rect = new Rect();
        Rect topRect = new Rect();
        view.getGlobalVisibleRect(rect);
        getGlobalVisibleRect(topRect);

        float scaleX = getScaleX();
        float scaleY = getScaleY();

        rect.top = (int) ((rect.top - topRect.top) / scaleX);
        rect.left = (int) ((rect.left - topRect.left) / scaleX);
        rect.right = (int) ((rect.right - topRect.left) / scaleY);
        rect.bottom = (int) ((rect.bottom - topRect.top) / scaleY);
        return rect;
    }

    private void updateStroke(ViewGroup v) {
        IntStream.range(0, v.getChildCount())
                .mapToObj(v::getChildAt)
                .forEach(
                        child -> {
                            viewsPosition.put(child, getRectFor(child));
                            if (child instanceof ViewGroup viewGroup) {
                                if (!Constants.isExcludedViewGroup(viewGroup)) {
                                    updateStroke(viewGroup);
                                }
                            }
                        });
    }
}
