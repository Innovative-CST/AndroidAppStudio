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

import com.icst.android.appstudio.activities.ModulesActivity;
import com.icst.android.appstudio.activities.ProjectManagerActivity;
import com.icst.android.appstudio.bottomsheet.ProjectOptionsSheet;
import com.icst.android.appstudio.databinding.AdapterProjectBinding;
import com.icst.android.appstudio.models.ProjectModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {
	private ArrayList<ProjectModel> projectList;
	private ArrayList<File> projectFileList;
	private ProjectManagerActivity mProjectManagerActivity;

	public ProjectListAdapter(
			ArrayList<ProjectModel> projectList,
			ArrayList<File> projectFileList,
			ProjectManagerActivity mProjectManagerActivity) {
		this.projectList = projectList;
		this.projectFileList = projectFileList;
		this.mProjectManagerActivity = mProjectManagerActivity;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		AdapterProjectBinding binding = AdapterProjectBinding.inflate(mProjectManagerActivity.getLayoutInflater());
		RecyclerView.LayoutParams mLayoutParams = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		binding.getRoot().setLayoutParams(mLayoutParams);
		return new ViewHolder(binding.getRoot());
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		AdapterProjectBinding binding = AdapterProjectBinding.bind(holder.itemView);
		binding.projectName.setText(projectList.get(position).getProjectName());
		binding.packageName.setText(projectList.get(position).getPackageName());
		binding
				.getRoot()
				.setOnClickListener(
						v -> {
							Intent modules = new Intent(mProjectManagerActivity, ModulesActivity.class);
							modules.putExtra(
									"projectRootDirectory", projectFileList.get(position).getAbsolutePath());
							modules.putExtra(
									"currentDir",
									EnvironmentUtils.getProjectDataDir(projectFileList.get(position))
											.getAbsolutePath());
							modules.putExtra("isInsideModule", false);
							mProjectManagerActivity.startActivity(modules);
						});
		binding
				.getRoot()
				.setOnLongClickListener(
						(view) -> {
							ProjectOptionsSheet sheet = new ProjectOptionsSheet(mProjectManagerActivity,
									projectFileList.get(position));
							sheet.show();
							return false;
						});
	}

	@Override
	public int getItemCount() {
		return projectList.size();
	}
}
