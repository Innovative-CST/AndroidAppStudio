/*
 *  This file is part of Blockly Android.
 *
 *  Blockly Android is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Blockly Android is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Blockly Android.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.tscodeeditor.blockly.android.activities;

import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import com.tscodeeditor.blockly.android.R;
import com.tscodeeditor.blockly.android.databinding.ActivityProjectManagerBinding;

public class ProjectManagerActivity extends BaseActivity {
  private ActivityProjectManagerBinding binding;

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    // Initialize binding
    binding = ActivityProjectManagerBinding.inflate(getLayoutInflater());

    // Set layout of activity
    setContentView(binding.getRoot());

    binding.toolbar.setTitle(R.string.app_name);
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar, R.string.app_name, R.string.app_name);
    binding.toolbar.setNavigationOnClickListener(
        v -> {
          if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
          } else {
            binding.drawerLayout.openDrawer(GravityCompat.START);
          }
        });
    binding.drawerLayout.addDrawerListener(toggle);
    toggle.syncState();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }
}
