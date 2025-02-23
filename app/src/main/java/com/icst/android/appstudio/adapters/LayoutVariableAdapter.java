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

import java.util.ArrayList;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.bottomsheet.EditLayoutVariableModelBottomSheet;
import com.icst.android.appstudio.databinding.AdapterLayoutVariableBinding;
import com.icst.android.appstudio.fragments.variablemanager.LayoutVariableManagerFragment;
import com.icst.android.appstudio.listener.LayoutVariableModelChangeListener;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.vieweditor.models.LayoutVariableModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class LayoutVariableAdapter extends RecyclerView.Adapter<LayoutVariableAdapter.ViewHolder> {
	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
		}
	}

	private ArrayList<LayoutVariableModel> variables;
	private LayoutVariableManagerFragment fragment;
	private ModuleModel module;
	private String packageName;
	private String className;

	public LayoutVariableAdapter(
			ArrayList<LayoutVariableModel> variables,
			LayoutVariableManagerFragment fragment,
			ModuleModel module,
			String packageName,
			String className) {
		this.variables = variables;
		this.fragment = fragment;
		this.module = module;
		this.packageName = packageName;
		this.className = className;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		AdapterLayoutVariableBinding binding = AdapterLayoutVariableBinding
				.inflate(LayoutInflater.from(fragment.getContext()));
		RecyclerView.LayoutParams mLayoutParams = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		binding.getRoot().setLayoutParams(mLayoutParams);
		return new ViewHolder(binding.getRoot());
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		AdapterLayoutVariableBinding binding = AdapterLayoutVariableBinding.bind(holder.itemView);
		binding.variableName.setText(variables.get(position).getVariableName());
		binding.variableType.setText(variables.get(position).getLayoutName());
		binding.representation.setImageResource(R.drawable.ic_layout);
		binding
				.getRoot()
				.setOnClickListener(
						v -> {
							EditLayoutVariableModelBottomSheet editLayoutVariableModelBottomSheet = new EditLayoutVariableModelBottomSheet(
									fragment.getContext(),
									module,
									variables.get(position),
									new LayoutVariableModelChangeListener() {
										@Override
										public void onLayoutVariableModelDelete() {
											variables.remove(position);
											fragment.saveVariables();
											fragment.loadVariables();
										}

										@Override
										public void onLayoutVariableModelUpdate(LayoutVariableModel model) {
											variables.add(model);
											fragment.saveVariables();
											fragment.loadVariables();
										}
									});

							editLayoutVariableModelBottomSheet.show();
						});
	}

	@Override
	public int getItemCount() {
		return variables.size();
	}
}
