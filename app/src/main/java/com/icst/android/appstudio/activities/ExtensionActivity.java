/*
 * This file is part of Android AppStudio [https://github.com/Innovative-CST/AndroidAppStudio].
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
import android.widget.Toast;
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
import java.io.File;

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
