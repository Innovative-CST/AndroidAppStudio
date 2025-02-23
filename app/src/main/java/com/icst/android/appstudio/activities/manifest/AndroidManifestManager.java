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

package com.icst.android.appstudio.activities.manifest;

import java.io.File;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.BaseActivity;
import com.icst.android.appstudio.databinding.ActivityAndroidManifestManagerBinding;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;
import com.icst.android.appstudio.xml.XmlModel;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class AndroidManifestManager extends BaseActivity {

	private ActivityAndroidManifestManagerBinding binding;
	private ModuleModel module;
	private XmlModel manifest;
	private ActivityResultLauncher<Intent> applicationChangesCallback;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		binding = ActivityAndroidManifestManagerBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		binding.toolbar.setTitle(R.string.app_name);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

		module = new ModuleModel();
		module.init(
				getIntent().getStringExtra("module"),
				new File(getIntent().getStringExtra("projectRootDirectory")));

		manifest = DeserializerUtils.deserialize(module.manifestFile, XmlModel.class);

		applicationChangesCallback = registerForActivityResult(
				new ActivityResultContracts.StartActivityForResult(),
				new ActivityResultCallback<ActivityResult>() {
					@Override
					public void onActivityResult(ActivityResult result) {
						if (result.getResultCode() == RESULT_OK) {
							Intent intent = result.getData();

							if (manifest != null) {
								if (manifest.getChildren() != null) {
									for (int i = 0; i < manifest.getChildren().size(); ++i) {
										if (manifest.getChildren().get(i).getName().equals("application")) {
											manifest
													.getChildren()
													.set(i, (XmlModel) intent.getSerializableExtra("xmlModel"));
											SerializerUtil.serialize(
													manifest,
													module.manifestFile,
													new SerializerUtil.SerializerCompletionListener() {
														@Override
														public void onFailedToSerialize(Exception exception) {
														}

														@Override
														public void onSerializeComplete() {
														}
													});
										}
									}
								}
							}
						}
					}
				});

		binding.application.setOnClickListener(
				(v) -> {
					Intent application = new Intent(AndroidManifestManager.this, AttributesManagerActivity.class);
					application.putExtra("module", module);

					if (manifest != null) {
						if (manifest.getChildren() != null) {
							for (int i = 0; i < manifest.getChildren().size(); ++i) {
								if (manifest.getChildren().get(i).getName().equals("application")) {
									application.putExtra("xmlModel", manifest.getChildren().get(i));
									application.putExtra("tag", "android:name");
								}
							}
						}
					}

					applicationChangesCallback.launch(application);
				});
	}
}
