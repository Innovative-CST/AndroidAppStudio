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

package com.icst.android.appstudio.adapters;

import java.io.File;
import java.util.ArrayList;

import com.icst.android.appstudio.block.dialog.variables.EditVariableDialog;
import com.icst.android.appstudio.block.model.VariableModel;
import com.icst.android.appstudio.databinding.AdapterVariableBinding;
import com.icst.android.appstudio.fragments.variablemanager.NonStaticVariableManagerFragment;
import com.icst.android.appstudio.fragments.variablemanager.StaticVariableManagerFragment;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;

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
		AdapterVariableBinding binding = AdapterVariableBinding.inflate(LayoutInflater.from(parent.getContext()));
		RecyclerView.LayoutParams mLayoutParams = new RecyclerView.LayoutParams(
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
			icon = new BitmapDrawable(
					binding.getRoot().getContext().getResources(),
					BitmapFactory.decodeByteArray(
							variables.get(position).getIcon(), 0, variables.get(position).getIcon().length));
		} else {
			icon = new BitmapDrawable(
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
							EditVariableDialog updateVar = new EditVariableDialog(fragment.getActivity(),
									variables.get(position)) {
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
												public void onSerializeComplete() {
												}

												@Override
												public void onFailedToSerialize(Exception exception) {
												}
											});
									if (fragment instanceof NonStaticVariableManagerFragment nonStaticVariableManagerFragment) {
										nonStaticVariableManagerFragment.loadList();
									} else if (fragment instanceof StaticVariableManagerFragment staticVariableManagerFragment) {
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
