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

package com.tscodeeditor.android.appstudio.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tscodeeditor.android.appstudio.R;

public class PermissionUtils {
  /**
   * The `showStoragePermissionDialog` method displays an alert dialog asking the user to grant
   * storage permission.
   */
  public static void showStoragePermissionDialog(Activity activity) {
    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(activity);
    dialog.setTitle(R.string.storage_permission_required);
    dialog.setMessage(
        R.string.storage_permission_is_required_please_allow_app_to_use_storage_in_next_page);
    dialog.setPositiveButton(
        R.string.str_continue,
        (_dialog, _which) -> {
          requestStoragePermission(activity, 1);
        });
    dialog.setNegativeButton(
        R.string.no_thanks,
        (_dialog, _which) -> {
          activity.finishAffinity();
        });
    dialog.create().show();
  }

  /*
   * The `showRationaleOfStoragePermissionDialog` method creates an alert dialog using
   * `MaterialAlertDialogBuilder` to explain the rationale behind requesting storage permission in an
   * app. The dialog includes a title, a message explaining the importance of storage
   * permission for storing and reading files on the device, and buttons for the user to either
   * continue or decline the permission request.
   */
  public static void showRationaleOfStoragePermissionDialog(Activity activity) {
    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(activity);
    dialog.setTitle(R.string.storage_permission_required);
    dialog.setMessage(
        R.string
            .storage_permission_is_highly_recommend_for_storing_and_reading_files_in_device_without_this_permission_you_cannot_be_able_to_use_this_app);
    dialog.setPositiveButton(
        R.string.str_continue,
        (_dialog, _which) -> {
          requestStoragePermission(activity, 1);
        });
    dialog.setNegativeButton(
        R.string.no_thanks,
        (_dialog, _which) -> {
          activity.finishAffinity();
        });
    dialog.create().show();
  }

  public static void showStoragePermissionDialogForGoToSettings(Activity context) {
    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
    dialog.setTitle(R.string.storage_permission_required);
    dialog.setMessage(
        R.string
            .storage_permission_is_highly_recommend_for_storing_and_reading_files_in_device_without_this_permission_you_cannot_be_able_to_use_this_app);
    dialog.setPositiveButton(
        R.string.setting,
        (_dialog, _which) -> {
          Intent intent = new Intent();
          intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
          Uri uri = Uri.fromParts("package", context.getPackageName(), null);
          intent.setData(uri);
          context.startActivity(intent);
        });
    dialog.setNegativeButton(
        R.string.no_thanks,
        (_dialog, _which) -> {
          context.finishAffinity();
        });
    dialog.create().show();
  }

  /*
   * Requests to grant storage permission.
   * If android version is 10 and below then it shows storage permission dialog.
   * If android version is 11 and above then it redirects to settings.
   */
  public static void requestStoragePermission(Activity activity, int reqCode) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      try {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, reqCode);
      } catch (Exception e) {

      }
    } else {
      ActivityCompat.requestPermissions(
          activity,
          new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
          },
          reqCode);
    }
  }

  // Checks if storage permission is granted or not.
  public static boolean isStoagePermissionGranted(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      return Environment.isExternalStorageManager();
    } else {
      if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
              == PackageManager.PERMISSION_DENIED
          || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
              == PackageManager.PERMISSION_DENIED) {
        return false;
      } else {
        return true;
      }
    }
  }
}
