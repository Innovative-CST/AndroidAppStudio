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

package com.icst.android.appstudio.view;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.viewmodel.ActionButton;

import android.code.editor.common.utils.ColorUtils;
import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class ActionButtonView extends LinearLayout {
	public ActionButtonView(Context context, ActionButton actionButton) {
		super(context);
		setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ripple_on_color_surface));
		setOnClickListener(
				v -> {
					actionButton.onClick();
				});
		setOrientation(VERTICAL);
		setGravity(Gravity.CENTER);
		setPadding(8, 8, 8, 8);
		ImageView icon = new ImageView(context);
		icon.setImageDrawable(ContextCompat.getDrawable(context, actionButton.getIcon()));
		addView(icon);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		TextView text = new TextView(context);
		text.setText(actionButton.getText());
		text.setTextColor(
				ColorUtils.getColor(getContext(), com.google.android.material.R.attr.colorOnSurface));
		addView(text);
		text.setLayoutParams(lp);
	}
}
