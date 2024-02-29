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

import android.code.editor.common.utils.FileUtils;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.tscodeeditor.android.appstudio.R;
import com.tscodeeditor.android.appstudio.adapters.LicenseListAdapter;
import com.tscodeeditor.android.appstudio.databinding.ActivityLicenseBinding;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;

public class LicenseActivity extends BaseActivity {
  private ActivityLicenseBinding binding;
  private ArrayList<License> LicenseList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityLicenseBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    binding.toolbar.setTitle(R.string.app_name);
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    binding.toolbar.setNavigationOnClickListener(
        view -> {
          onBackPressed();
        });

    String LicenseListFileText = FileUtils.readFileFromAssets(getAssets(), "LicenseList.json");
    LicenseList = new ArrayList<License>();

    try {
      JSONArray arr = new JSONArray(LicenseListFileText);
      for (int pos = 0; pos < arr.length(); ++pos) {
        if (arr.getJSONObject(pos).has("Name") && arr.getJSONObject(pos).has("Path")) {
          License License = new License();
          License.setLicenseName(arr.getJSONObject(pos).getString("Name"));
          License.setLicensePath(arr.getJSONObject(pos).getString("Path"));
          LicenseList.add(License);
        }
      }
    } catch (JSONException e) {
    }
    binding.list.setAdapter(new LicenseListAdapter(LicenseList, this));
    binding.list.setLayoutManager(new LinearLayoutManager(this));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }

  public class License {
    private String LicenseName;
    private String LicensePath;

    public String getLicenseName() {
      return this.LicenseName;
    }

    public void setLicenseName(String LicenseName) {
      this.LicenseName = LicenseName;
    }

    public String getLicensePath() {
      return this.LicensePath;
    }

    public void setLicensePath(String LicensePath) {
      this.LicensePath = LicensePath;
    }
  }
}
