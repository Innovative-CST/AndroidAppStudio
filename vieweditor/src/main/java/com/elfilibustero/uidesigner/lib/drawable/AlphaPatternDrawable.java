package com.elfilibustero.uidesigner.lib.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AlphaPatternDrawable extends Drawable {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int size;
    private int colorOdd;
    private int colorEven;
    
    public static Builder builder() {
        return new Builder();
    }

    public static AlphaPatternDrawable create() {
        return new AlphaPatternDrawable(builder());
    }

    private AlphaPatternDrawable(Builder builder) {
        this.size = builder.size;
        this.colorOdd = builder.colorOdd;
        this.colorEven = builder.colorEven;
        configurePaint();
    }

    private void configurePaint() {
        Bitmap bitmap = Bitmap.createBitmap(size * 2, size * 2, Bitmap.Config.ARGB_8888);

        Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint.setStyle(Paint.Style.FILL);

        Canvas canvas = new Canvas(bitmap);

        Rect rect = new Rect(0, 0, size, size);
        bitmapPaint.setColor(colorOdd);
        canvas.drawRect(rect, bitmapPaint);

        rect.offset(size, size);
        canvas.drawRect(rect, bitmapPaint);

        bitmapPaint.setColor(colorEven);
        rect.offset(-size, 0);
        canvas.drawRect(rect, bitmapPaint);

        rect.offset(size, -size);
        canvas.drawRect(rect, bitmapPaint);

        paint.setShader(new BitmapShader(bitmap, BitmapShader.TileMode.REPEAT, BitmapShader.TileMode.REPEAT));
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPaint(paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
    
    public static final class Builder {

        private int size = 40;
        private int colorOdd = 0xFFC2C2C2;
        private int colorEven = 0xFFF3F3F3;

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder colorOdd(int color) {
            colorOdd = color;
            return this;
        }

        public Builder colorEven(int color) {
            colorEven = color;
            return this;
        }

        public AlphaPatternDrawable build() {
            return new AlphaPatternDrawable(this);
        }
    }
}
