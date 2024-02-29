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

package com.tscodeeditor.android.appstudio.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.tscodeeditor.android.appstudio.activities.LicenseActivity;
import com.tscodeeditor.android.appstudio.activities.LicenseReaderActivity;
import com.tscodeeditor.android.appstudio.databinding.LayoutLicenseListItemBinding;
import java.util.ArrayList;

public class LicenseListAdapter extends RecyclerView.Adapter<LicenseListAdapter.ViewHolder> {

  private ArrayList<LicenseActivity.License> LicenseList;
  private Activity activity;

  public LicenseListAdapter(ArrayList<LicenseActivity.License> LicenseList, Activity activity) {
    this.LicenseList = LicenseList;
    this.activity = activity;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutLicenseListItemBinding item =
        LayoutLicenseListItemBinding.inflate(activity.getLayoutInflater());
    View _v = item.getRoot();
    RecyclerView.LayoutParams _lp =
        new RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    _v.setLayoutParams(_lp);
    return new ViewHolder(_v);
  }

  @Override
  public void onBindViewHolder(ViewHolder _holder, final int _position) {
    LayoutLicenseListItemBinding binding = LayoutLicenseListItemBinding.bind(_holder.itemView);
    binding.name.setText(LicenseList.get(_position).getLicenseName());
    binding.getRoot().setOnClickListener(
        v -> {
          Intent LicenseReader = new Intent();
          LicenseReader.setClass(activity, LicenseReaderActivity.class);
          LicenseReader.putExtra("Path", LicenseList.get(_position).getLicensePath());
          activity.startActivity(LicenseReader);
        });
  }

  @Override
  public int getItemCount() {
    return LicenseList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View v) {
      super(v);
    }
  }
}
