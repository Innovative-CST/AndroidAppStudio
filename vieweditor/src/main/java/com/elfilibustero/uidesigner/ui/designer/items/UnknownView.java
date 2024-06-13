package com.elfilibustero.uidesigner.ui.designer.items;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.elfilibustero.uidesigner.ui.designer.DesignerItem;

// Inherit from ViewGroup to prevent views within unknown tags from being parsed to the same level
public class UnknownView extends FrameLayout implements DesignerItem {
    private String className = "null";
    private int minSize;
    private Paint paint;

    public UnknownView(Context ctx) {
        super(ctx);
        setWillNotDraw(false);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, ctx.getResources().getDisplayMetrics());
        setPadding(padding, padding, padding, padding);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setColor(Color.GRAY);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, ctx.getResources().getDisplayMetrics()));
        minSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, ctx.getResources().getDisplayMetrics());
    }
    
    @Override
    public void setClassName(String cs) {
        if (cs == null) className = "null";
        else className = cs.toString();
        invalidate();
    }
    
    public String getClassName() {
        return className;
    }
    
    @Override
    public Class<?> getClassType() {
        return isViewGroup() ? ViewGroup.class : View.class;
    }
    
    public boolean isViewGroup() {
        return getChildCount() > 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLineY = getHeight() / 2 + dy;
        int baseLineX = (getWidth() - Math.min(getWidth(), (int) paint.measureText(className))) / 2;
        canvas.drawText(className, baseLineX, baseLineY, paint);
        //super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        Rect bounds = new Rect();

        paint.getTextBounds(className, 0, className.length(), bounds);

        if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.max(minSize, bounds.width() + getPaddingLeft() + getPaddingRight());

        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.max(minSize, bounds.height() + getPaddingTop() + getPaddingBottom());
        }
        setMeasuredDimension(width, height);

    }


}
