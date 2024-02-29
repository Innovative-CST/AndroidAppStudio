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
