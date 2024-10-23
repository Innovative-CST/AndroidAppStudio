/*
 * This file is part of Android AppStudio [https://github.com/Innovative-CST/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.icst.android.appstudio.adapters.resourcemanager;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.resourcemanager.LayoutManagerActivity;
import com.icst.android.appstudio.activities.resourcemanager.ResourceManagerActivity;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.databinding.AdapterResourceManagerBinding;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.IconUtils;
import java.util.ArrayList;

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
