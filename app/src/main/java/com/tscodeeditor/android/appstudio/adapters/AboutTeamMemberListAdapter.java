/*
 * This file is part of Android AppStudio [https://github.com/TS-Code-Editor/AndroidAppStudio].
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

package com.tscodeeditor.android.appstudio.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.tscodeeditor.android.appstudio.R;
import java.util.ArrayList;
import java.util.HashMap;

public class AboutTeamMemberListAdapter
    extends RecyclerView.Adapter<AboutTeamMemberListAdapter.ViewHolder> {

  ArrayList<HashMap<String, Object>> _data;
  public ImageView profile;
  public TextView name;
  public TextView description;
  public AboutTeamActivity mAboutTeamActivity;

  public AboutTeamMemberListAdapter(
      ArrayList<HashMap<String, Object>> _arr, AboutTeamActivity mAboutTeamActivity) {
    _data = _arr;
    this.mAboutTeamActivity = mAboutTeamActivity;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater _inflater = mAboutTeamActivity.getLayoutInflater();
    View _v = _inflater.inflate(R.layout.adapter_about_team_member, null);
    RecyclerView.LayoutParams _lp =
        new RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    _v.setLayoutParams(_lp);
    return new ViewHolder(_v);
  }

  @Override
  public void onBindViewHolder(ViewHolder _holder, final int _position) {
    View _view = _holder.itemView;
    profile = _view.findViewById(R.id.profile);
    if (_data.get(_position).containsKey("Image")) {
      MultiTransformation multi = new MultiTransformation<Bitmap>(new CircleCrop());
      Glide.with(mAboutTeamActivity)
          .load(Uri.parse(_data.get(_position).get("Image").toString()))
          .thumbnail(0.10F)
          .into(profile);
    }
    name = _view.findViewById(R.id.name);
    description = _view.findViewById(R.id.description);
    if (_data.get(_position).containsKey("Name")) {
      name.setText(_data.get(_position).get("Name").toString());
    }
    if (_data.get(_position).containsKey("Description")) {
      description.setText(_data.get(_position).get("Description").toString());
    }
  }

  @Override
  public int getItemCount() {
    return _data.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View v) {
      super(v);
    }
  }
}
