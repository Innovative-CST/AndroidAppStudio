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

package com.icst.android.appstudio.adapters;

import java.util.ArrayList;

import com.icst.android.appstudio.activities.BaseActivity;
import com.icst.android.appstudio.activities.ExtensionActivity;
import com.icst.android.appstudio.databinding.AdapterExtensionBinding;
import com.icst.android.appstudio.models.ExtensionAdapterModel;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class ExtensionAdapter extends RecyclerView.Adapter<ExtensionAdapter.ViewHolder> {
	private ArrayList<ExtensionAdapterModel> extensions;
	private BaseActivity activity;

	public ExtensionAdapter(ArrayList<ExtensionAdapterModel> extensions, BaseActivity activity) {
		this.extensions = extensions;
		this.activity = activity;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View view) {
			super(view);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		AdapterExtensionBinding binding = AdapterExtensionBinding.inflate(LayoutInflater.from(arg0.getContext()));
		RecyclerView.LayoutParams layoutParam = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		binding.getRoot().setLayoutParams(layoutParam);
		return new ViewHolder(binding.getRoot());
	}

	@Override
	public int getItemCount() {
		return extensions.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, final int arg1) {
		ExtensionAdapterModel extension = extensions.get(arg1);
		AdapterExtensionBinding binding = AdapterExtensionBinding.bind(arg0.itemView);

		binding.extensionName.setText(extension.getTitle());

		StringBuilder details = new StringBuilder();
		if (extension.getIsInstalled()) {
			if (extension.getInstalledVersion() < extension.getLatestVersion()) {
				details.append("Update");
				details.append(String.valueOf(extension.getInstalledVersion()));
				details.append(" to ");
				details.append(String.valueOf(extension.getLatestVersion()));
			} else {
				details.append("Installed ");
				details.append(String.valueOf(extension.getInstalledVersion()));
			}
		} else {
			details.append("Not installed");
		}

		binding.details.setText(details.toString());

		binding
				.getRoot()
				.setOnClickListener(
						v -> {
							Intent extensionActivity = new Intent(activity, ExtensionActivity.class);
							extensionActivity.putExtra("childKey", extension.getChildKey());
							activity.startActivity(extensionActivity);
						});
	}
}
