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

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.CodeEditorActivity;
import com.icst.android.appstudio.activities.FileManagerActivity;
import com.icst.android.appstudio.databinding.AdapterFileBinding;
import com.icst.android.appstudio.utils.FileIconUtils;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class FilesListAdapter extends RecyclerView.Adapter<FilesListAdapter.ViewHolder> {
	private FileManagerActivity activity;

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View view) {
			super(view);
		}
	}

	public FilesListAdapter(FileManagerActivity activity) {
		this.activity = activity;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = AdapterFileBinding.inflate(LayoutInflater.from(arg0.getContext())).getRoot();
		RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(layoutParams);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		File file = new File(
				activity.getCurrentDir(),
				activity.getFilesMap().get(position).get("lastSegmentOfFilePath"));
		AdapterFileBinding binding = AdapterFileBinding.bind(holder.itemView);
		binding.title.setText(activity.getFilesMap().get(position).get("lastSegmentOfFilePath"));
		binding
				.getRoot()
				.setOnClickListener(
						(v) -> {
							if (new File(
									activity.getCurrentDir(),
									activity.getFilesMap().get(position).get("lastSegmentOfFilePath"))
									.isDirectory()) {
								activity.loadFileList(
										new File(
												activity.getCurrentDir(),
												activity.getFilesMap().get(position).get("lastSegmentOfFilePath")));
							} else {
								Intent editor = new Intent(activity, CodeEditorActivity.class);
								editor.putExtra(
										"path",
										new File(
												activity.getCurrentDir(),
												activity.getFilesMap().get(position).get("lastSegmentOfFilePath"))
												.getAbsolutePath());
								activity.startActivity(editor);
							}
						});
		if (file.isDirectory()) {
			binding.icon.setImageResource(R.drawable.ic_folder);
		} else {
			binding.icon.setImageDrawable(FileIconUtils.getFileIcon(file, activity));
		}
	}

	@Override
	public int getItemCount() {
		return activity.getFilesMap().size();
	}
}
