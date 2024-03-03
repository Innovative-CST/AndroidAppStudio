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

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.tscodeeditor.android.appstudio.databinding.AdapterProjectBinding;
import com.tscodeeditor.android.appstudio.models.ProjectModel;
import java.util.ArrayList;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {
  private ArrayList<ProjectModel> projectList;
  private Activity activity;

  public ProjectListAdapter(ArrayList<ProjectModel> projectList, Activity activity) {
    this.projectList = projectList;
    this.activity = activity;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View v) {
      super(v);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
    AdapterProjectBinding binding = AdapterProjectBinding.inflate(activity.getLayoutInflater());
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
  }

  @Override
  public int getItemCount() {
    return projectList.size();
  }
}
