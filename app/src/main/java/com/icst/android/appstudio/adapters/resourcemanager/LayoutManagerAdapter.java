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

import java.io.File;
import java.util.ArrayList;

import com.icst.android.appstudio.activities.resourcemanager.LayoutEditorActivity;
import com.icst.android.appstudio.activities.resourcemanager.LayoutManagerActivity;
import com.icst.android.appstudio.databinding.AdapterManagerLayoutBinding;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.vieweditor.models.LayoutModel;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class LayoutManagerAdapter extends RecyclerView.Adapter<LayoutManagerAdapter.ViewHolder> {

	private LayoutManagerActivity activity;
	private ArrayList<LayoutModel> layoutList;
	private ArrayList<File> fileList;
	private ModuleModel module;
	private String layoutDirectoryName;

	public LayoutManagerAdapter(
			LayoutManagerActivity activity,
			ArrayList<LayoutModel> layoutList,
			ArrayList<File> fileList,
			ModuleModel module,
			String layoutDirectoryName) {
		this.activity = activity;
		this.layoutList = layoutList;
		this.fileList = fileList;
		this.module = module;
		this.layoutDirectoryName = layoutDirectoryName;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		AdapterManagerLayoutBinding binding = AdapterManagerLayoutBinding.inflate(activity.getLayoutInflater());
		RecyclerView.LayoutParams layoutParam = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		binding.getRoot().setLayoutParams(layoutParam);
		return new ViewHolder(binding.getRoot());
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		AdapterManagerLayoutBinding binding = AdapterManagerLayoutBinding.bind(holder.itemView);
		binding.title.setText(layoutList.get(position).getLayoutName());
		binding
				.getRoot()
				.setOnClickListener(
						v -> {
							Intent layoutEditor = new Intent(activity, LayoutEditorActivity.class);
							layoutEditor.putExtra("module", module);
							layoutEditor.putExtra("layoutDirectoryName", layoutDirectoryName);
							layoutEditor.putExtra("layoutFileName", fileList.get(position).getAbsolutePath());
							activity.startActivity(layoutEditor);
						});
	}

	@Override
	public int getItemCount() {
		return layoutList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
		}
	}
}
