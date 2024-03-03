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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import com.tscodeeditor.android.appstudio.R;
import com.tscodeeditor.android.appstudio.databinding.ActivityProjectManagerBinding;
import com.tscodeeditor.android.appstudio.utils.EnvironmentUtils;
import com.tscodeeditor.android.appstudio.utils.PermissionUtils;
import java.io.File;
import java.util.concurrent.Executors;

public class ProjectManagerActivity extends BaseActivity {
  private ActivityProjectManagerBinding binding;

  // Contants for showing the section easily
  public static final int LOADING_SECTION = 0;
  public static final int NO_PROJECTS_YET_SECTION = 1;
  public static final int PROJECT_LIST_SECTION = 2;
  public static final int ERROR_SECTION = 3;

  // Social links
  public static final String DISCORD = "https://discord.com/invite/RM5qaZs4kd";

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    // Initialize binding
    binding = ActivityProjectManagerBinding.inflate(getLayoutInflater());

    // Set layout of activity
    setContentView(binding.getRoot());

    // SetUp the drawer
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

    // SetUp the items click events in drawer
    binding.navigationView.setNavigationItemSelectedListener(
        menuItem -> {
          if (menuItem.getItemId() == R.id.source_License) {
            Intent licenseActivity = new Intent();
            licenseActivity.setClass(this, LicenseActivity.class);
            startActivity(licenseActivity);
          }
          if (menuItem.getItemId() == R.id.discord) {
            Intent discord = new Intent();
            discord.setAction(Intent.ACTION_VIEW);
            discord.setData(Uri.parse(DISCORD));
            startActivity(discord);
          }
          return true;
        });

    /*
     * Ask for storage permission if not granted.
     * Load projects if storage permission is granted.
     * Show storage permission denied error when storage permission is denied.
     */
    if (PermissionUtils.isStoagePermissionGranted(this)) {
      tryToLoadProjects();
    } else {
      showError(getString(R.string.storage_permission_denied));
      PermissionUtils.showStoragePermissionDialog(this);
    }
  }

  /*
   * Method for switching the section quickly.
   * All other section will be GONE except the section of which the section code is provided
   */
  public void switchSection(int section) {
    binding.loading.setVisibility(section == LOADING_SECTION ? View.VISIBLE : View.GONE);
    binding.noProjectsYet.setVisibility(
        section == NO_PROJECTS_YET_SECTION ? View.VISIBLE : View.GONE);
    binding.projectList.setVisibility(section == PROJECT_LIST_SECTION ? View.VISIBLE : View.GONE);
    binding.errorSection.setVisibility(section == ERROR_SECTION ? View.VISIBLE : View.GONE);
  }

  public void showError(String errorText) {
    switchSection(ERROR_SECTION);
    binding.errorText.setText(errorText);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }

  public void tryToLoadProjects() {
    /*
     * Show LOADING_SECTION.
     * Then load project in a saparate thread
     */
    switchSection(LOADING_SECTION);
    Executors.newSingleThreadExecutor()
        .execute(
            () -> {
              /*
               * Check if the PROJECTS directory exists or not.
               * If not exists then:
               * - Create PROJECTS directory now.
               * - Show NO_PROJECTS_YET_SECTION.
               * - Stop the thread on further execution.
               */
              if (!EnvironmentUtils.PROJECTS.exists()) {
                EnvironmentUtils.PROJECTS.mkdirs();
                runOnUiThread(
                    () -> {
                      switchSection(NO_PROJECTS_YET_SECTION);
                    });
                return;
              }
              /*
               * List all the file list available in PROJECTS.
               * And check all the paths and only list when confirmed that it is a project directory.
               */
              for (File file : EnvironmentUtils.PROJECTS.listFiles()) {
                /*
                 * Only check further if the file is a directory.
                 * If file is a file then skip it.
                 */
                if (file.isFile()) continue;
                /*
                 * Check if the ProjectModel file exists in the directory(file)
                 * If it does not exists then skip it.
                 */
                if (!new File(file, EnvironmentUtils.PROJECT_CONFIGRATION).exists()) continue;
              }
            });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (PermissionUtils.isStoagePermissionGranted(this)) {
      tryToLoadProjects();
    } else {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        PermissionUtils.showRationaleOfStoragePermissionDialog(this);
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int arg0, String[] arg1, int[] arg2) {
    super.onRequestPermissionsResult(arg0, arg1, arg2);
    switch (arg0) {
      case 1:
      case -1:
      case 10:
        boolean isDenied = false;
        for (int position = 0; position < arg2.length; position++) {
          if (arg2[position] == PackageManager.PERMISSION_DENIED) {
            isDenied = true;
            if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(arg1[position])) {
              if (Build.VERSION.SDK_INT >= 23) {
                if (shouldShowRequestPermissionRationale(arg1[position])) {
                  PermissionUtils.showRationaleOfStoragePermissionDialog(this);
                } else {
                  PermissionUtils.showStoragePermissionDialogForGoToSettings(this);
                }
              }
            }
          }
        }
        if (!isDenied) {
          tryToLoadProjects();
        }
        break;
    }
  }
}
