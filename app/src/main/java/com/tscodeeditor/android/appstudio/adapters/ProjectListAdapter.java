/*
 *  This file is part of Android AppStudio.
 *
 *  Android AppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Android AppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Android AppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.tscodeeditor.android.appstudio.adapters;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.tscodeeditor.android.appstudio.activities.ProjectManagerActivity;
import com.tscodeeditor.android.appstudio.activities.ProjectModelConfigrationActivity;
import com.tscodeeditor.android.appstudio.activities.ProjectNavigationActivity;
import com.tscodeeditor.android.appstudio.databinding.AdapterProjectBinding;
import com.tscodeeditor.android.appstudio.models.ProjectModel;
import java.io.File;
import java.util.ArrayList;

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
    AdapterProjectBinding binding =
        AdapterProjectBinding.inflate(mProjectManagerActivity.getLayoutInflater());
    RecyclerView.LayoutParams mLayoutParams =
        new RecyclerView.LayoutParams(
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
              Intent projectNavigation =
                  new Intent(mProjectManagerActivity, ProjectNavigationActivity.class);
              projectNavigation.putExtra(
                  "projectRootDirectory", projectFileList.get(position).getAbsolutePath());
              mProjectManagerActivity.startActivity(projectNavigation);
            });
    binding
        .getRoot()
        .setOnLongClickListener(
            (view) -> {
              Intent modifyProject = new Intent();
              modifyProject.setClass(
                  mProjectManagerActivity, ProjectModelConfigrationActivity.class);
              modifyProject.putExtra("isNewProject", false);
              modifyProject.putExtra(
                  "projectRootDirectory", projectFileList.get(position).getAbsolutePath());
              mProjectManagerActivity.projectListUpdateActivityResultLauncher.launch(modifyProject);
              return false;
            });
  }

  @Override
  public int getItemCount() {
    return projectList.size();
  }
}
