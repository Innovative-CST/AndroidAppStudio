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

import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tscodeeditor.android.appstudio.R;
import com.tscodeeditor.android.appstudio.adapters.ProjectModelConfigAdapter;
import com.tscodeeditor.android.appstudio.databinding.ActivityProjectModelConfigrationBinding;

public class ProjectModelConfigrationActivity extends BaseActivity {
  private ActivityProjectModelConfigrationBinding binding;
  private ProjectModelConfigAdapter mProjectModelConfigAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initialize binding
    binding = ActivityProjectModelConfigrationBinding.inflate(getLayoutInflater());

    // Set layout of activity
    setContentView(binding.getRoot());

    // SetUp the toolbar
    binding.toolbar.setTitle(R.string.create_new_project);
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    // Initialize the viewpager to show the fragments
    mProjectModelConfigAdapter = new ProjectModelConfigAdapter(this);

    binding.viewpager.setAdapter(mProjectModelConfigAdapter);
    binding.viewpager.registerOnPageChangeCallback(
        new ViewPager2.OnPageChangeCallback() {
          @Override
          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            /*
             * Update buttons and apge status textview
             */
            updateButtonConfigration();
            binding.pageStatus.setText(
                "Step "
                    + String.valueOf(binding.viewpager.getCurrentItem() + 1)
                    + " out of "
                    + mProjectModelConfigAdapter.fragments.size());
          }
        });
  }

  public void updateButtonConfigration() {
    /*
     * Disable the previos button when view pager is at the first page.
     * Switch next button as done button at the last page.
     */
    binding.previous.setEnabled(!(binding.viewpager.getCurrentItem() == 0));
    binding.next.setText(
        (mProjectModelConfigAdapter.fragments.size() - 1) == binding.viewpager.getCurrentItem()
            ? R.string.done
            : R.string.next);
    binding.previous.setOnClickListener(
        v -> {
          binding.viewpager.setCurrentItem(binding.viewpager.getCurrentItem() - 1);
        });
    binding.next.setOnClickListener(
        v -> {
          if ((mProjectModelConfigAdapter.fragments.size() - 1)
              != binding.viewpager.getCurrentItem()) {
            binding.viewpager.setCurrentItem(binding.viewpager.getCurrentItem() + 1);
          } else {
            saveProject();
          }
        });
  }

  public void saveProject() {
    boolean isRequiredFieldsProperlyFilled = true;
    for (int position = 0; position < mProjectModelConfigAdapter.fragments.size(); ++position) {
      if (!mProjectModelConfigAdapter.fragments.get(position).getIsRequiredFieldsProperlyFilled()) {
        isRequiredFieldsProperlyFilled = false;
      }
    }
    if (isRequiredFieldsProperlyFilled) {
      MaterialAlertDialogBuilder fieldsNotProperlyField = new MaterialAlertDialogBuilder(this);
      fieldsNotProperlyField.setTitle(R.string.an_error_occured);
      fieldsNotProperlyField.setMessage(R.string.fields_not_filled_properly);
      fieldsNotProperlyField.setPositiveButton(R.string.done, (arg0, arg1) -> {});
      fieldsNotProperlyField.create().show();
    }
  }
}
