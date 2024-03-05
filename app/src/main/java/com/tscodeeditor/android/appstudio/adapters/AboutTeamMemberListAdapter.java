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
