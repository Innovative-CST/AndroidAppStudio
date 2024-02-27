package com.tscodeeditor.blockly.android.activities;

import android.os.Bundle;
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
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }
}
