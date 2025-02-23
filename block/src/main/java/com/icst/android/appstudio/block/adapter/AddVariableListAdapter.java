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

package com.icst.android.appstudio.block.adapter;

import java.util.ArrayList;

import com.icst.android.appstudio.block.databinding.AdapterAddVariableBinding;
import com.icst.android.appstudio.block.model.VariableModel;
import com.icst.android.appstudio.block.utils.ColorUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddVariableListAdapter extends ArrayAdapter<VariableModel> {
	private VariableModel selectedVariable;

	public AddVariableListAdapter(Context context, ArrayList<VariableModel> variables) {
		super(context, 0, variables);
	}

	@NonNull @Override
	public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

		AdapterAddVariableBinding listitemView = AdapterAddVariableBinding.inflate(LayoutInflater.from(getContext()));

		VariableModel variable = getItem(position);
		Drawable icon = null;
		if (variable.getIcon() != null) {
			icon = new BitmapDrawable(
					getContext().getResources(),
					BitmapFactory.decodeByteArray(variable.getIcon(), 0, variable.getIcon().length));
		} else {
			icon = new BitmapDrawable(
					getContext().getResources(),
					textToBitmap(
							variable.getVariableTitle(),
							16,
							ColorUtils.getColor(
									listitemView.getRoot().getContext(),
									com.google.android.material.R.attr.colorOnSurface),
							listitemView.getRoot().getContext()));
		}

		if (variable.getApplyColorFilter()) {
			icon.setTint(
					ColorUtils.getColor(
							listitemView.getRoot().getContext(),
							com.google.android.material.R.attr.colorOnSurfaceVariant));
		}

		listitemView.icon.setImageDrawable(icon);
		listitemView.title.setText(variable.getVariableTitle());
		listitemView
				.getRoot()
				.setOnClickListener(
						v -> {
							setSelectedVariable(variable);
						});

		return listitemView.getRoot();
	}

	private Bitmap textToBitmap(String text, int textSize, int textColor, Context context) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(
				TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, textSize, context.getResources().getDisplayMetrics()));
		paint.setColor(textColor);
		paint.setTextAlign(Paint.Align.LEFT);

		float baseline = -paint.ascent(); // ascent() is negative
		int width = (int) (paint.measureText(text) + 0.5f); // round
		int height = (int) (baseline + paint.descent() + 0.5f);

		Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(image);
		canvas.drawText(text, 0, baseline, paint);
		return image;
	}

	public VariableModel getSelectedVariable() {
		return this.selectedVariable;
	}

	public void setSelectedVariable(VariableModel selectedVariable) {
		this.selectedVariable = selectedVariable;
	}
}
