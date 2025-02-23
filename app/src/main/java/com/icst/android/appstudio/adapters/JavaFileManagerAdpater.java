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

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.JavaFileManagerActivity;
import com.icst.android.appstudio.activities.JavaFileModelEditorActivity;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.model.JavaFileModel;
import com.icst.android.appstudio.databinding.AdapterJavaFileManagerBinding;
import com.icst.android.appstudio.models.ModuleModel;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class JavaFileManagerAdpater
		extends RecyclerView.Adapter<JavaFileManagerAdpater.ViewHolder> {
	private ModuleModel module;
	private String packageName;
	private ArrayList<FileModel> folderList;
	private ArrayList<JavaFileModel> javaFilesList;
	private ArrayList<File> pathList;
	private JavaFileManagerActivity activity;

	public JavaFileManagerAdpater(
			JavaFileManagerActivity activity,
			ArrayList<FileModel> folderList,
			ArrayList<JavaFileModel> javaFilesList,
			ArrayList<File> pathList,
			ModuleModel module,
			String packageName) {
		this.activity = activity;
		this.folderList = folderList;
		this.javaFilesList = javaFilesList;
		this.pathList = pathList;
		this.module = module;
		this.packageName = packageName;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		AdapterJavaFileManagerBinding binding = AdapterJavaFileManagerBinding.inflate(activity.getLayoutInflater());
		RecyclerView.LayoutParams layoutParam = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		binding.getRoot().setLayoutParams(layoutParam);
		return new ViewHolder(binding.getRoot());
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		if (folderList.size() != 0 && position < folderList.size()) {
			AdapterJavaFileManagerBinding binding = AdapterJavaFileManagerBinding.bind(holder.itemView);
			binding.title.setText(folderList.get(position).getFileName());
			binding.icon.setImageResource(R.drawable.ic_folder);
			binding
					.getRoot()
					.setOnClickListener(
							v -> {
								Intent javaManager = new Intent(activity, JavaFileManagerActivity.class);
								javaManager.putExtra("module", module);
								if (packageName.isEmpty()) {
									javaManager.putExtra("packageName", folderList.get(position).getFileName());
								} else {
									javaManager.putExtra(
											"packageName",
											packageName.concat(".").concat(folderList.get(position).getFileName()));
								}
								activity.startActivity(javaManager);
							});
		} else {
			AdapterJavaFileManagerBinding binding = AdapterJavaFileManagerBinding.bind(holder.itemView);
			binding.title.setText(javaFilesList.get(position - folderList.size()).getFileName());
			binding.icon.setImageResource(R.drawable.ic_java);
			binding
					.getRoot()
					.setOnClickListener(
							v -> {
								Intent editor = new Intent(activity, JavaFileModelEditorActivity.class);
								editor.putExtra("module", module);
								editor.putExtra(
										"fileName", javaFilesList.get(position - folderList.size()).getFileName());
								editor.putExtra("packageName", packageName);
								activity.startActivity(editor);
							});
		}
	}

	@Override
	public int getItemCount() {
		return folderList.size() + javaFilesList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
		}
	}
}
