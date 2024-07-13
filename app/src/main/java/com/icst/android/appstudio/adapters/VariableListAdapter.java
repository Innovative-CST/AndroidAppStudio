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

package com.icst.android.appstudio.adapters;

import android.code.editor.common.utils.ColorUtils;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.icst.android.appstudio.block.dialog.variables.EditVariableDialog;
import com.icst.android.appstudio.block.model.VariableModel;
import com.icst.android.appstudio.databinding.AdapterVariableBinding;
import com.icst.android.appstudio.fragments.variablemanager.NonStaticVariableManagerFragment;
import com.icst.android.appstudio.fragments.variablemanager.StaticVariableManagerFragment;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;
import java.io.File;
import java.util.ArrayList;

public class VariableListAdapter extends RecyclerView.Adapter<VariableListAdapter.ViewHolder> {
  private ArrayList<VariableModel> variables;
  private Fragment fragment;
  private ModuleModel module;
  private String packageName;
  private String className;

  public VariableListAdapter(
      ArrayList<VariableModel> variables,
      Fragment fragment,
      ModuleModel module,
      String packageName,
      String className) {
    this.variables = variables;
    this.fragment = fragment;
    this.module = module;
    this.packageName = packageName;
    this.className = className;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View v) {
      super(v);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
    AdapterVariableBinding binding =
        AdapterVariableBinding.inflate(LayoutInflater.from(parent.getContext()));
    RecyclerView.LayoutParams mLayoutParams =
        new RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    binding.getRoot().setLayoutParams(mLayoutParams);
    return new ViewHolder(binding.getRoot());
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    AdapterVariableBinding binding = AdapterVariableBinding.bind(holder.itemView);
    binding.variableName.setText(variables.get(position).getVariableName());
    binding.variableType.setText(variables.get(position).getVariableType());
    Drawable icon = null;
    if (variables.get(position).getIcon() != null) {
      icon =
          new BitmapDrawable(
              binding.getRoot().getContext().getResources(),
              BitmapFactory.decodeByteArray(
                  variables.get(position).getIcon(), 0, variables.get(position).getIcon().length));
    } else {
      icon =
          new BitmapDrawable(
              textToBitmap(
                  variables.get(position).getVariableTitle(),
                  16,
                  ColorUtils.getColor(
                      binding.getRoot().getContext(),
                      com.google.android.material.R.attr.colorPrimary),
                  binding.getRoot().getContext()));
    }

    if (variables.get(position).getApplyColorFilter()) {
      icon.setTint(
          ColorUtils.getColor(
              binding.getRoot().getContext(),
              com.google.android.material.R.attr.colorOnSurfaceVariant));
    }

    binding.representation.setImageDrawable(icon);
    binding
        .getRoot()
        .setOnClickListener(
            v -> {
              EditVariableDialog updateVar =
                  new EditVariableDialog(fragment.getActivity(), variables.get(position)) {
                    @Override
                    public void onVariableModified(VariableModel variable) {
                      variables.set(position, variable);
                      SerializerUtil.serialize(
                          variables,
                          new File(
                              new File(
                                  EnvironmentUtils.getJavaDirectory(module, packageName),
                                  className.concat(".java")),
                              EnvironmentUtils.VARIABLES),
                          new SerializerUtil.SerializerCompletionListener() {

                            @Override
                            public void onSerializeComplete() {}

                            @Override
                            public void onFailedToSerialize(Exception exception) {}
                          });
                      if (fragment
                          instanceof
                          NonStaticVariableManagerFragment
                          nonStaticVariableManagerFragment) {
                        nonStaticVariableManagerFragment.loadList();
                      } else if (fragment
                          instanceof StaticVariableManagerFragment staticVariableManagerFragment) {
                        staticVariableManagerFragment.loadList();
                      }
                    }
                  };
            });
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

  @Override
  public int getItemCount() {
    return variables.size();
  }
}
