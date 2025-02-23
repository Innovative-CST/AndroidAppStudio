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

package com.icst.android.appstudio.adapters.resourcemanager;

import java.util.ArrayList;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.resourcemanager.LayoutManagerActivity;
import com.icst.android.appstudio.activities.resourcemanager.ResourceManagerActivity;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.databinding.AdapterResourceManagerBinding;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.IconUtils;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class ResourceManagerAdapter
		extends RecyclerView.Adapter<ResourceManagerAdapter.ViewHolder> {
	private ModuleModel module;

	private ArrayList<FileModel> files;
	private ResourceManagerActivity activity;

	public ResourceManagerAdapter(
			ArrayList<FileModel> files, ResourceManagerActivity activity, ModuleModel module) {
		this.files = files;
		this.activity = activity;
		this.module = module;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		AdapterResourceManagerBinding binding = AdapterResourceManagerBinding.inflate(activity.getLayoutInflater());
		RecyclerView.LayoutParams layoutParam = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		binding.getRoot().setLayoutParams(layoutParam);
		return new ViewHolder(binding.getRoot());
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		AdapterResourceManagerBinding binding = AdapterResourceManagerBinding.bind(holder.itemView);
		binding.title.setText(files.get(position).getName());
		int icon = IconUtils.getResourceManagerFileModelIcon(files.get(position));
		binding.icon.setImageResource(icon != 0 ? icon : R.drawable.ic_folder);

		binding
				.getRoot()
				.setOnClickListener(
						v -> {
							switch (files.get(position).getName()) {
								case "layout", "layout-land":
									Intent layoutManager = new Intent(activity, LayoutManagerActivity.class);
									layoutManager.putExtra("module", module);
									layoutManager.putExtra("layoutDirectoryName", files.get(position).getName());
									activity.startActivity(layoutManager);
									break;
							}
						});
	}

	@Override
	public int getItemCount() {
		return files.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
		}
	}
}
