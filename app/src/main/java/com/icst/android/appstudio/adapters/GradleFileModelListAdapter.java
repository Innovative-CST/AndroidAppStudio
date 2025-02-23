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
import com.icst.android.appstudio.activities.EventsActivity;
import com.icst.android.appstudio.activities.JavaFileManagerActivity;
import com.icst.android.appstudio.activities.ModulesActivity;
import com.icst.android.appstudio.activities.manifest.AndroidManifestManager;
import com.icst.android.appstudio.activities.resourcemanager.ResourceManagerActivity;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.databinding.AdapterFileModelListItemBinding;
import com.icst.android.appstudio.databinding.LayoutProjectEditorNavigationBinding;
import com.icst.android.appstudio.utils.EnvironmentUtils;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class GradleFileModelListAdapter
		extends RecyclerView.Adapter<GradleFileModelListAdapter.ViewHolder> {
	private ArrayList<FileModel> fileList;
	private ModulesActivity modulesActivity;

	public GradleFileModelListAdapter(
			ArrayList<FileModel> fileList, ModulesActivity modulesActivity) {
		this.fileList = fileList;
		this.modulesActivity = modulesActivity;
		for (int i = 0; i < fileList.size(); ++i) {
			FileModel file = fileList.get(i);
			if (file.getName().equals(EnvironmentUtils.SOURCE_DIR) && file.isFolder()) {
				fileList.remove(i);
			}
		}
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int viewType) {
		if (viewType == 0) {
			AdapterFileModelListItemBinding item = AdapterFileModelListItemBinding
					.inflate(LayoutInflater.from(arg0.getContext()));
			View _v = item.getRoot();
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		} else {
			LayoutProjectEditorNavigationBinding item = LayoutProjectEditorNavigationBinding
					.inflate(LayoutInflater.from(arg0.getContext()));
			View _v = item.getRoot();
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int pos) {
		if (getItemViewType(pos) == 0) {
			final int position = pos - getExtraItemCount();
			AdapterFileModelListItemBinding binding = AdapterFileModelListItemBinding.bind(holder.itemView);
			binding.title.setText(fileList.get(position).getName());
			if (fileList.get(position).isFolder()) {
				if (fileList.get(position).isAndroidLibrary()
						|| fileList.get(position).isAndroidAppModule()) {
					if (fileList.get(position).isAndroidLibrary())
						binding.icon.setImageResource(R.drawable.ic_alpha_l);
					else
						binding.icon.setImageResource(R.drawable.ic_alpha_a);

				} else
					binding.icon.setImageResource(R.drawable.ic_folder);

				binding.cardView.setOnClickListener(
						v -> {
							Intent modules = new Intent(modulesActivity, ModulesActivity.class);
							modules.putExtra(
									"projectRootDirectory", modulesActivity.projectRootDirectory.getAbsolutePath());
							modules.putExtra(
									"currentDir",
									new File(
											modulesActivity.currentDir,
											new File(
													new File(fileList.get(position).getName()),
													EnvironmentUtils.FILES)
													.getAbsolutePath())
											.getAbsolutePath());
							modules.putExtra(
									"isInsideModule",
									fileList.get(position).isAndroidAppModule()
											|| fileList.get(position).isAndroidLibrary());
							modules.putExtra(
									"outputPath",
									new File(modulesActivity.outputDir, fileList.get(position).getName())
											.getAbsolutePath());
							modules.putExtra(
									"module",
									modulesActivity.module.concat(":").concat(fileList.get(position).getName()));
							modulesActivity.startActivity(modules);
						});
			} else {
				if (fileList.get(position).getFileExtension() != null) {
					if (fileList.get(position).getFileExtension().equals("gradle")) {
						binding.icon.setImageResource(R.drawable.ic_gradle);
					}
				}

				binding.cardView.setOnClickListener(
						v -> {
							Intent eventsActivity = new Intent(modulesActivity, EventsActivity.class);
							eventsActivity.putExtra(
									"projectRootDirectory", modulesActivity.projectRootDirectory.getAbsolutePath());
							eventsActivity.putExtra(
									"fileModelDirectory",
									new File(
											new File(modulesActivity.currentDir, fileList.get(position).getName()),
											EnvironmentUtils.FILE_MODEL)
											.getAbsolutePath());
							eventsActivity.putExtra("module", modulesActivity.module);
							modulesActivity.startActivity(eventsActivity);
						});
			}
		} else {
			LayoutProjectEditorNavigationBinding binding = LayoutProjectEditorNavigationBinding.bind(holder.itemView);
			binding.programEditor.setOnClickListener(
					v -> {
						Intent javaManager = new Intent(modulesActivity, JavaFileManagerActivity.class);
						javaManager.putExtra("module", modulesActivity.module);
						javaManager.putExtra(
								"projectRootDirectory", modulesActivity.projectRootDirectory.getAbsolutePath());
						modulesActivity.startActivity(javaManager);
					});
			binding.resourceEditor.setOnClickListener(
					v -> {
						Intent resourceManager = new Intent(modulesActivity, ResourceManagerActivity.class);
						resourceManager.putExtra("module", modulesActivity.module);
						resourceManager.putExtra(
								"projectRootDirectory", modulesActivity.projectRootDirectory.getAbsolutePath());
						modulesActivity.startActivity(resourceManager);
					});
			binding.manifestEditor.setOnClickListener(
					v -> {
						Intent manifestEditor = new Intent(modulesActivity, AndroidManifestManager.class);
						manifestEditor.putExtra("module", modulesActivity.module);
						manifestEditor.putExtra(
								"projectRootDirectory", modulesActivity.projectRootDirectory.getAbsolutePath());
						modulesActivity.startActivity(manifestEditor);
					});
		}
	}

	@Override
	public int getItemCount() {
		return fileList.size() + getExtraItemCount();
	}

	@Override
	public int getItemViewType(int position) {
		if (getExtraItemCount() == 1) {
			return position == 0 ? 1 : 0;
		} else
			return 0;
	}

	public int getExtraItemCount() {
		if (modulesActivity.isInsideModule) {
			return 1;
		} else
			return 0;
	}
}
