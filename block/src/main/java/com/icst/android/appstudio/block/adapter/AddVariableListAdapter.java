/*
 * This file is part of Android AppStudio [https://github.com/TS-Code-Editor/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.icst.android.appstudio.block.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.icst.android.appstudio.block.databinding.AdapterAddVariableBinding;
import com.icst.android.appstudio.block.model.VariableModel;
import com.icst.android.appstudio.block.utils.ColorUtils;
import java.util.ArrayList;

public class AddVariableListAdapter extends ArrayAdapter<VariableModel> {
  private VariableModel selectedVariable;

  public AddVariableListAdapter(Context context, ArrayList<VariableModel> variables) {
    super(context, 0, variables);
  }

  @NonNull
  @Override
  public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

    AdapterAddVariableBinding listitemView =
        AdapterAddVariableBinding.inflate(LayoutInflater.from(getContext()));

    VariableModel variable = getItem(position);
    Drawable icon = null;
    if (variable.getIcon() != null) {
      icon =
          new BitmapDrawable(
              getContext().getResources(),
              BitmapFactory.decodeByteArray(variable.getIcon(), 0, variable.getIcon().length));
    } else {
      icon =
          new BitmapDrawable(
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
              com.google.android.material.R.attr.colorOnSurface));
      icon.setTintMode(PorterDuff.Mode.MULTIPLY);
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
