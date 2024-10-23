package com.elfilibustero.uidesigner.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

public class ShadowView extends View {

	private Paint highlightPaint;
	private Paint shadowPaint;
	private RectF rectF;

	public ShadowView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		highlightPaint = new Paint();
		highlightPaint.setColor(Color.parseColor("#bdbdbd"));

		shadowPaint = new Paint();
		shadowPaint.setColor(Color.parseColor("#e0e0e0"));
		shadowPaint.setShadowLayer(8, 0, 0, Color.parseColor("#40000000")); // Semi-transparent black shadow
		setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);

		rectF = new RectF();
		setLayoutParams(new LinearLayout.LayoutParams((int) getDip(context, 54.0f), (int) getDip(context, 32.0f)));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int width = getWidth();
		int height = getHeight();

		canvas.drawRect(0, 0, width, height, highlightPaint);
		rectF.set(10, 10, width - 10, height - 10);
		canvas.drawRoundRect(rectF, 10, 10, shadowPaint);
	}

	public float getDip(Context context, float f) {
		return TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
	}
}
