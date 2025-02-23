/*
 *  This file is part of AndroidAppStudio.
 *
 *  AndroidAppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidAppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidAppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.android.appstudio.activities;

import java.io.File;
import java.util.ArrayList;

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

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

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

									ExtensionBundle bundle = DeserializerUtils.deserialize(
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
					public void onCancelled(DatabaseError databaseError) {
					}
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
