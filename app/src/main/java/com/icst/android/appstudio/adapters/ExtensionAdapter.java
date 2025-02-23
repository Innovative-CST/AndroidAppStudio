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
