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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.icst.android.appstudio.databinding.AdapterSocialProfileBinding;
import com.icst.android.appstudio.models.SocialProfile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class SocialProfileAdapter extends RecyclerView.Adapter<SocialProfileAdapter.ViewHolder> {

	public ArrayList<SocialProfile> profiles;
	public Activity activity;

	public SocialProfileAdapter(ArrayList<SocialProfile> profiles, Activity activity) {
		this.profiles = profiles;
		this.activity = activity;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		AdapterSocialProfileBinding binding = AdapterSocialProfileBinding
				.inflate(LayoutInflater.from(parent.getContext()));
		RecyclerView.LayoutParams layoutParam = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		binding.getRoot().setLayoutParams(layoutParam);
		return new ViewHolder(binding.getRoot());
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		SocialProfile profile = profiles.get(position);
		AdapterSocialProfileBinding binding = AdapterSocialProfileBinding.bind(holder.itemView);
		binding.platformName.setText(
				profile.getPlatformName() != null ? profile.getPlatformName() : "");
		if (profile.getPlatformIconUrl() != null) {
			MultiTransformation multi = new MultiTransformation<Bitmap>(new CircleCrop());
			Glide.with(activity)
					.load(Uri.parse(profile.getPlatformIconUrl()))
					.thumbnail(0.10F)
					.into(binding.platformIcon);
		}
		if (profile.getUrl() != null) {
			binding
					.getRoot()
					.setOnClickListener(
							v -> {
								Intent profileOpener = new Intent();
								profileOpener.setAction(Intent.ACTION_VIEW);
								profileOpener.setData(Uri.parse(profile.getUrl()));
								activity.startActivity(profileOpener);
							});
		}
	}

	@Override
	public int getItemCount() {
		return profiles.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
		}
	}
}
