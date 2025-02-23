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

package com.icst.android.appstudio.block.view;

import java.util.HashMap;

import com.icst.android.appstudio.block.R;
import com.icst.android.appstudio.block.editor.EventEditor;
import com.icst.android.appstudio.block.model.BlockValueFieldModel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class BlockFieldExtensionViewOnly extends LinearLayout {
	public BlockFieldExtensionViewOnly(
			Context context,
			BlockView blockView,
			BlockValueFieldModel blockFieldModel,
			EventEditor editor) {
		super(context);

		Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.block_field_extension_view_only);
		drawable.setTint(Color.parseColor("#ffffff"));
		drawable.setTintMode(PorterDuff.Mode.MULTIPLY);
		setBackground(drawable);

		TextView text = new TextView(context);

		HashMap<String, Object> variables = editor.getVariables();

		text.setText(blockFieldModel.getCode(variables));
		text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		text.setSingleLine(true);
		addView(text);
	}
}
