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

package com.icst.android.appstudio.activities;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.icst.android.appstudio.bottomsheet.SocialProfileBottomSheet;
import com.icst.android.appstudio.databinding.AdapterAboutTeamMemberBinding;
import com.icst.android.appstudio.models.TeamMember;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class AboutTeamMemberListAdapter
		extends RecyclerView.Adapter<AboutTeamMemberListAdapter.ViewHolder> {

	public ArrayList<TeamMember> members;
	public AboutTeamActivity mAboutTeamActivity;

	public AboutTeamMemberListAdapter(
			ArrayList<TeamMember> members, AboutTeamActivity mAboutTeamActivity) {
		this.members = members;
		this.mAboutTeamActivity = mAboutTeamActivity;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		AdapterAboutTeamMemberBinding binding = AdapterAboutTeamMemberBinding
				.inflate(LayoutInflater.from(parent.getContext()));
		RecyclerView.LayoutParams layoutParam = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		binding.getRoot().setLayoutParams(layoutParam);
		return new ViewHolder(binding.getRoot());
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		TeamMember member = members.get(position);
		AdapterAboutTeamMemberBinding binding = AdapterAboutTeamMemberBinding.bind(holder.itemView);
		binding.name.setText(member.getName() != null ? member.getName() : "");
		binding.description.setText(member.getDescription() != null ? member.getDescription() : "");
		if (member.getProfilePhotoUrl() != null) {
			MultiTransformation multi = new MultiTransformation<Bitmap>(new CircleCrop());
			Glide.with(mAboutTeamActivity)
					.load(Uri.parse(member.getProfilePhotoUrl()))
					.thumbnail(0.10F)
					.into(binding.profile);
		}
		if (member.getTag() == null) {
			binding.tagContainer.setVisibility(View.GONE);
		} else {
			binding.tagContainer.setVisibility(View.VISIBLE);
			binding.tag.setText(member.getTag());
		}
		if (member.getSocialProfiles() != null) {
			binding
					.getRoot()
					.setOnClickListener(
							v -> {
								SocialProfileBottomSheet profilesBottomSheet = new SocialProfileBottomSheet(
										member.getSocialProfiles(), mAboutTeamActivity);
								profilesBottomSheet.show();
							});
		}
	}

	@Override
	public int getItemCount() {
		return members.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
		}
	}
}
