/*
 *  This file is part of AndroidAppStudio.
 *
 *  AndroidAppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidAppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidAppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.logic.view;

import com.icst.android.appstudio.beans.ValueInputBlockElementBean;
import com.icst.logic.block.view.BlockBeanView;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.UnitUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.view.View;

public class LogicEditorSpinner extends View {

	private ValueInputBlockElementBean valueInputBlockElementBean;
	private BlockBeanView blockView;
	private Paint text;
	private int color;

	public LogicEditorSpinner(Context context, ValueInputBlockElementBean valueInputBlockElementBean,
			BlockBeanView blockView,
			int color) {
		super(context);
		this.valueInputBlockElementBean = valueInputBlockElementBean;
		this.blockView = blockView;
		this.color = color;

		text = new Paint();
		text.setColor(ColorUtils.getTextColorForColor(color));
		text.setTextSize(20);
		text.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
	}

	private int dp(float px) {
		return UnitUtils.dpToPx(getContext(), px);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		String className = valueInputBlockElementBean.getAcceptedReturnType().getClassName();
		String value = valueInputBlockElementBean.getValue();

		float width = dp(16) + text.measureText(className) + text.measureText(value);
		setMeasuredDimension(
				resolveSize((int) width, widthMeasureSpec),
				resolveSize(dp(20), heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		String className = valueInputBlockElementBean.getAcceptedReturnType().getClassName();
		canvas.drawText(className, dp(2), dp(14), text);

		String value = valueInputBlockElementBean.getValue();
		canvas.drawText(value, dp(4) + text.measureText(className), dp(14), text);

		Paint trianglePaint = new Paint();
		trianglePaint.setColor(ColorUtils.getTextColorForColor(color));
		trianglePaint.setStyle(Paint.Style.FILL);
		// trianglePaint.setPathEffect(new CornerPathEffect(5));

		Path trianglePath = new Path();
		trianglePath.moveTo(dp(6) + text.measureText(className) + text.measureText(value), dp(8));
		trianglePath.lineTo(dp(16) + text.measureText(className) + text.measureText(value), dp(8));
		trianglePath.lineTo(dp(11) + text.measureText(className) + text.measureText(value), dp(14));
		trianglePath.close();
		canvas.drawPath(trianglePath, trianglePaint);
	}
}
