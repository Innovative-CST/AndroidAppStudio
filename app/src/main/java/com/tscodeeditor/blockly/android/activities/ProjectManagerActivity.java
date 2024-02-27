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
