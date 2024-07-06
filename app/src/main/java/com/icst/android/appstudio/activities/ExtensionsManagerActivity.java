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

package com.icst.android.appstudio.activities;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.adapters.ExtensionAdapter;
import com.icst.android.appstudio.databinding.ActivityExtensionsManagerBinding;
import com.icst.android.appstudio.models.ExtensionAdapterModel;
import com.icst.android.appstudio.models.ExtensionBundle;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import java.io.File;
import java.util.ArrayList;

public class ExtensionsManagerActivity extends BaseActivity {

  private ActivityExtensionsManagerBinding binding;
  private DatabaseReference extensionDatabase;
  private ArrayList<ExtensionAdapterModel> availableExtensions;
  private boolean isInitialized;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    extensionDatabase = FirebaseDatabase.getInstance().getReference("extensions");
    super.onCreate(savedInstanceState);

    binding = ActivityExtensionsManagerBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    binding.toolbar.setTitle(R.string.app_name);
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    fetchChildValues();
    isInitialized = true;
  }

  private void fetchChildValues() {
    binding.loading.setVisibility(View.VISIBLE);
    binding.nestedScrollView.setVisibility(View.GONE);
    extensionDatabase.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            availableExtensions = new ArrayList<>();
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
              ExtensionAdapterModel extension = new ExtensionAdapterModel();
              if (childSnapshot.child("title").exists()) {
                extension.setTitle(childSnapshot.child("title").getValue(String.class));
              }
              if (childSnapshot.child("latest_version").exists()) {
                extension.setLatestVersion(
                    childSnapshot.child("latest_version").getValue(Integer.class));
              }

              if (childSnapshot.child("file_name").exists()) {
                if (new File(
                        EnvironmentUtils.EXTENSION_DIR,
                        childSnapshot.child("file_name").getValue(String.class))
                    .exists()) {
                  extension.setIsInstalled(true);

                  ExtensionBundle bundle =
                      DeserializerUtils.deserialize(
                          new File(
                              EnvironmentUtils.EXTENSION_DIR,
                              childSnapshot.child("file_name").getValue(String.class)),
                          ExtensionBundle.class);
                  extension.setInstalledVersion(bundle.getVersion());
                }
              }

              if (childSnapshot.child("authors").exists()) {
                extension.setAuthors(childSnapshot.child("authors").getValue(String.class));
              }

              extension.setChildKey(childSnapshot.getKey());

              availableExtensions.add(extension);
              binding.nestedScrollView.setVisibility(View.VISIBLE);
              binding.loading.setVisibility(View.GONE);
              binding.extensions.setAdapter(
                  new ExtensionAdapter(availableExtensions, ExtensionsManagerActivity.this));
              binding.extensions.setLayoutManager(
                  new LinearLayoutManager(ExtensionsManagerActivity.this));
            }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {}
        });
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (isInitialized) {
      fetchChildValues();
    }
  }
}
