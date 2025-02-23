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

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.databinding.ActivityExtensionBinding;
import com.icst.android.appstudio.models.ExtensionBundle;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;

import android.os.Bundle;
import android.widget.Toast;

public class ExtensionActivity extends BaseActivity {
	private ActivityExtensionBinding binding;
	private String childKey;
	private DatabaseReference extensionDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		extensionDatabase = FirebaseDatabase.getInstance().getReference("extensions");
		super.onCreate(savedInstanceState);

		binding = ActivityExtensionBinding.inflate(getLayoutInflater());

		setContentView(binding.getRoot());

		binding.toolbar.setTitle(R.string.app_name);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

		childKey = getIntent().getStringExtra("childKey");

		extensionDatabase
				.child(childKey)
				.addListenerForSingleValueEvent(
						new ValueEventListener() {

							@Override
							public void onDataChange(DataSnapshot arg0) {
								if (arg0.child("title").exists()) {
									binding.extensionName.setText(arg0.child("title").getValue(String.class));
								} else {
									binding.extensionName.setText("TITLE NOT PROVIDED");
								}

								updateActionButtom(arg0);
							}

							@Override
							public void onCancelled(DatabaseError arg0) {
							}
						});
	}

	public void updateActionButtom(DataSnapshot arg0) {
		if (arg0.child("file_name").exists()) {
			if (new File(EnvironmentUtils.EXTENSION_DIR, arg0.child("file_name").getValue(String.class))
					.exists()) {

				ExtensionBundle bundle = DeserializerUtils.deserialize(
						new File(
								EnvironmentUtils.EXTENSION_DIR, arg0.child("file_name").getValue(String.class)),
						ExtensionBundle.class);

				if (arg0.child("latest_version").exists()) {
					if (arg0.child("latest_version").getValue(Integer.class) > bundle.getVersion()) {
						binding.actionButton.setText("Update");

						if (arg0.child("download_url").exists()) {
							binding.actionButton.setOnClickListener(
									v -> {
										binding.actionButton.setText("Updating...");
										binding.actionButton.setOnClickListener(null);
										int downloadId = PRDownloader.download(
												arg0.child("download_url").getValue(String.class),
												EnvironmentUtils.EXTENSION_DIR.getAbsolutePath(),
												arg0.child("file_name").getValue(String.class))
												.build()
												.start(
														new OnDownloadListener() {
															@Override
															public void onDownloadComplete() {
																updateActionButtom(arg0);
															}

															@Override
															public void onError(Error error) {
																Toast.makeText(
																		ExtensionActivity.this,
																		error.getServerErrorMessage(),
																		Toast.LENGTH_SHORT)
																		.show();
																updateActionButtom(arg0);
															}
														});
									});
						} else {
							binding.actionButton.setOnClickListener(null);
						}

					} else {
						binding.actionButton.setText("Uninstall");
						binding.actionButton.setOnClickListener(
								v -> {
									new File(
											EnvironmentUtils.EXTENSION_DIR,
											arg0.child("file_name").getValue(String.class))
											.delete();
									updateActionButtom(arg0);
								});
					}
				}

			} else {
				binding.actionButton.setText("Install");

				if (arg0.child("download_url").exists()) {
					binding.actionButton.setOnClickListener(
							v -> {
								binding.actionButton.setText("Installing...");
								binding.actionButton.setOnClickListener(null);
								int downloadId = PRDownloader.download(
										arg0.child("download_url").getValue(String.class),
										EnvironmentUtils.EXTENSION_DIR.getAbsolutePath(),
										arg0.child("file_name").getValue(String.class))
										.build()
										.start(
												new OnDownloadListener() {
													@Override
													public void onDownloadComplete() {
														updateActionButtom(arg0);
													}

													@Override
													public void onError(Error error) {
														Toast.makeText(
																ExtensionActivity.this,
																error.getConnectionException().getMessage(),
																Toast.LENGTH_SHORT)
																.show();
														updateActionButtom(arg0);
													}
												});
							});
				} else {
					binding.actionButton.setOnClickListener(null);
				}
			}
		}
	}
}
