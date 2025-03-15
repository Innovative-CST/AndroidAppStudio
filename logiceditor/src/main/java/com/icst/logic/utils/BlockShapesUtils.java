/*
 *  This file is part of Block IDLE.
 *
 *  Block IDLE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Block IDLE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Block IDLE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.logic.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.DisplayMetrics;

public final class BlockShapesUtils {

	public static void drawActionBlockHeader(
			Canvas canvas, Context context, float xOffset, float yOffset, float width, int color) {
		Path mPath = new Path();
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(color);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));

		mPath.moveTo(dpToPx(context, 5F) + xOffset, dpToPx(context, 0F) + yOffset);
		mPath.lineTo(dpToPx(context, 11F) + xOffset, dpToPx(context, 0F) + yOffset);
		mPath.lineTo(dpToPx(context, 19F) + xOffset, dpToPx(context, 6F) + yOffset);
		mPath.lineTo(dpToPx(context, 35F) + xOffset, dpToPx(context, 6F) + yOffset);
		mPath.lineTo(dpToPx(context, 43F) + xOffset, dpToPx(context, 0F) + yOffset);
		mPath.lineTo(
				Float.valueOf(width) - dpToPx(context, 5F) + xOffset,
				dpToPx(context, 0F) + yOffset);
		mPath.lineTo(Float.valueOf(width) + xOffset, dpToPx(context, 5F) + yOffset);
		mPath.lineTo(Float.valueOf(width) + xOffset, dpToPx(context, 7F) + yOffset);
		mPath.lineTo(dpToPx(context, 0F) + xOffset, dpToPx(context, 7F) + yOffset);
		mPath.lineTo(dpToPx(context, 0F) + xOffset, dpToPx(context, 5F) + yOffset);
		mPath.lineTo(dpToPx(context, 5F) + xOffset, dpToPx(context, 0F) + yOffset);

		canvas.drawPath(mPath, mPaint);
	}

	public static void drawRegularBlockFooter(
			Canvas canvas, Context context, float xOffset, float yOffset, float width, int color) {
		Path mPath = new Path();
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(color);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));

		mPath.moveTo(xOffset, yOffset);
		mPath.lineTo(dpToPx(context, 5F) + xOffset, dpToPx(context, 5F) + yOffset);
		mPath.lineTo(dpToPx(context, 11F) + xOffset, dpToPx(context, 5F) + yOffset);
		mPath.lineTo(dpToPx(context, 20F) + xOffset, dpToPx(context, 12F) + yOffset);
		mPath.lineTo(dpToPx(context, 34F) + xOffset, dpToPx(context, 12F) + yOffset);
		mPath.lineTo(dpToPx(context, 43F) + xOffset, dpToPx(context, 5F) + yOffset);
		mPath.lineTo(
				Float.valueOf(width) + xOffset - dpToPx(context, 5F),
				dpToPx(context, 5F) + yOffset);
		mPath.lineTo(Float.valueOf(width) + xOffset, yOffset);
		mPath.lineTo(xOffset, yOffset);
		canvas.drawPath(mPath, mPaint);
	}

	public static void drawTerminatorBlockFooter(
			Canvas canvas, Context context, float xOffset, float yOffset, float width, int color) {
		Path mPath = new Path();
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(color);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));

		mPath.moveTo(xOffset, yOffset);
		mPath.lineTo(dpToPx(context, 5F) + xOffset, dpToPx(context, 5F) + yOffset);
		mPath.lineTo(
				Float.valueOf(width) + xOffset - dpToPx(context, 5F),
				dpToPx(context, 5F) + yOffset);
		mPath.lineTo(Float.valueOf(width) + xOffset, yOffset);
		mPath.lineTo(xOffset, yOffset);
		canvas.drawPath(mPath, mPaint);
	}

	public static void drawEventBlockHeader(
			Canvas canvas, Context context, float xOffset, float yOffset, float width, int color) {
		Path mPath = new Path();
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(color);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));

		mPath.moveTo(xOffset, yOffset + dpToPx(context, 10F));
		mPath.arcTo(
				xOffset,
				yOffset,
				xOffset + dpToPx(context, 60),
				xOffset + dpToPx(context, 10),
				180,
				180,
				false);
		mPath.lineTo(xOffset + width - dpToPx(context, 5F), yOffset + dpToPx(context, 5F));
		mPath.lineTo(xOffset + width, yOffset + dpToPx(context, 10F));
		mPath.lineTo(xOffset + width, yOffset + dpToPx(context, 12F));
		mPath.lineTo(xOffset, yOffset + dpToPx(context, 12F));
		canvas.drawPath(mPath, mPaint);
	}

	public static void drawActionBlockLayer(
			Canvas canvas, Context context, float width, float height, int color) {
		Path mPath = new Path();
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(color);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));

		mPath.lineTo(width, 0);
		mPath.lineTo(width - dpToPx(context, 5), dpToPx(context, 4));
		mPath.lineTo(dpToPx(context, 54), dpToPx(context, 4));
		mPath.lineTo(dpToPx(context, 46), dpToPx(context, 10));
		mPath.lineTo(dpToPx(context, 31), dpToPx(context, 10));
		mPath.lineTo(dpToPx(context, 23), dpToPx(context, 4));
		mPath.lineTo(dpToPx(context, 15), dpToPx(context, 4));
		mPath.lineTo(dpToPx(context, 10), dpToPx(context, 10));
		mPath.lineTo(dpToPx(context, 10), height + dpToPx(context, 0));
		mPath.lineTo(dpToPx(context, 15), height + dpToPx(context, 5));
		mPath.lineTo(width - dpToPx(context, 5), height + dpToPx(context, 5));
		mPath.lineTo(width, height + dpToPx(context, 10));
		mPath.lineTo(0, height + dpToPx(context, 10));
		mPath.lineTo(0, 0);

		canvas.drawPath(mPath, mPaint);
	}

	public static void drawBooleanBlock(
			Canvas canvas, Context context, int width, int height, int color) {
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(color);
		mPaint.setStyle(Paint.Style.FILL);

		Path triangle1 = new Path();
		triangle1.moveTo(height / 2, 0);
		triangle1.lineTo(height / 2, height);
		triangle1.lineTo(0, height / 2);
		triangle1.close();
		canvas.drawPath(triangle1, mPaint);

		Rect rectagle = new Rect();
		rectagle.set(height / 2, 0, width + (height / 2), height);
		canvas.drawRect(rectagle, mPaint);

		Path triangle2 = new Path();
		triangle2.moveTo(width + (height / 2), 0);
		triangle2.lineTo(width + height, height / 2);
		triangle2.lineTo(width + (height / 2), height);
		triangle2.close();
		canvas.drawPath(triangle2, mPaint);
	}

	public static void drawNumericBlock(
			Canvas canvas, Context context, int width, int height, int color) {
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(color);
		mPaint.setStyle(Paint.Style.FILL);

		Path path = new Path();
		path.arcTo(0, 0, height, height, 90, 180, false);
		path.lineTo(width + (height / 2), 0);
		path.arcTo(width, 0, width + height, height, 270, 180, false);
		path.lineTo(height / 2, height);
		canvas.drawPath(path, mPaint);
	}

	public static void drawNumericBlockElement(
			Canvas canvas, Context context, int width, int height, int color) {
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(color);
		mPaint.setStyle(Paint.Style.FILL);

		Path path = new Path();
		path.arcTo(0, 0, height, height, 90, 180, false);
		path.lineTo(width - height, 0);
		path.arcTo(width - height, 0, width, height, 270, 180, false);
		path.lineTo(height / 2, height);
		canvas.drawPath(path, mPaint);
	}

	public static void drawNumericBlockHighlighter(
			Canvas canvas, Context context, int width, int height, int color) {
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(color);
		mPaint.setStyle(Paint.Style.FILL);

		Path path = new Path();
		path.arcTo(0, 0, height, height, 90, 180, false);
		path.lineTo(width - height, 0);
		path.arcTo(width - height, 0, width, height, 270, 180, false);
		path.lineTo(height / 2, height);
		canvas.drawPath(path, mPaint);
	}

	public static void drawBooleanBlockElement(
			Canvas canvas, Context context, int width, int height, int color) {
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(10);
		mPaint.setColor(color);
		mPaint.setAlpha(50);
		mPaint.setStyle(Paint.Style.FILL);

		Path triangle1 = new Path();
		triangle1.moveTo(height / 2, 0);
		triangle1.lineTo(height / 2, height);
		triangle1.lineTo(0, height / 2);
		triangle1.close();
		canvas.drawPath(triangle1, mPaint);

		Rect rectagle = new Rect();
		rectagle.set(height / 2, 0, width - (height / 2), height);
		canvas.drawRect(rectagle, mPaint);

		Path triangle2 = new Path();
		triangle2.moveTo(width - (height / 2), 0);
		triangle2.lineTo(width, height / 2);
		triangle2.lineTo(width - (height / 2), height);
		triangle2.close();
		canvas.drawPath(triangle2, mPaint);
	}

	public static void drawBooleanBlockHighlighter(
			Canvas canvas, Context context, int width, int height, int color) {
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(10);
		mPaint.setColor(color);
		mPaint.setStyle(Paint.Style.FILL);

		Path triangle1 = new Path();
		triangle1.moveTo(height / 2, 0);
		triangle1.lineTo(height / 2, height);
		triangle1.lineTo(0, height / 2);
		triangle1.close();
		canvas.drawPath(triangle1, mPaint);

		Rect rectagle = new Rect();
		rectagle.set(height / 2, 0, width - (height / 2), height);
		canvas.drawRect(rectagle, mPaint);

		Path triangle2 = new Path();
		triangle2.moveTo(width - (height / 2), 0);
		triangle2.lineTo(width, height / 2);
		triangle2.lineTo(width - (height / 2), height);
		triangle2.close();
		canvas.drawPath(triangle2, mPaint);
	}

	public static void drawGeneralBlockElement(
			Canvas canvas, Context context, int width, int height, int color) {
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(10);
		mPaint.setColor(color);
		mPaint.setAlpha(50);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(5));

		Path mPath = new Path();
		mPath.lineTo(width, 0);
		mPath.lineTo(width, height);
		mPath.lineTo(0, height);
		mPath.close();
		canvas.drawPath(mPath, mPaint);
	}

	public static void drawGeneralExpressionBlockHighlighter(
			Canvas canvas, Context context, int width, int height, int color) {
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(10);
		mPaint.setColor(color);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(5));

		Path mPath = new Path();
		mPath.lineTo(width, 0);
		mPath.lineTo(width, height);
		mPath.lineTo(0, height);
		mPath.close();
		canvas.drawPath(mPath, mPaint);
	}

	public static float dpToPx(Context context, float dp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
	}
}
