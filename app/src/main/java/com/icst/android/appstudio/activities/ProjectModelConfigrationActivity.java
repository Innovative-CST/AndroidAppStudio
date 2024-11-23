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

import java.io.File;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.adapters.ProjectModelConfigAdapter;
import com.icst.android.appstudio.databinding.ActivityProjectModelConfigrationBinding;
import com.icst.android.appstudio.models.ProjectModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.ProjectModelSerializationUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.viewpager2.widget.ViewPager2;

public class ProjectModelConfigrationActivity extends BaseActivity {
	private ActivityProjectModelConfigrationBinding binding;
	private ProjectModelConfigAdapter mProjectModelConfigAdapter;
	private boolean isNewProject;
	private ProjectModel mProjectModel;
	private File projectRootDirectory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize variables
		binding = ActivityProjectModelConfigrationBinding.inflate(getLayoutInflater());
		isNewProject = getIntent().getBooleanExtra("isNewProject", true);
		if (isNewProject) {
			mProjectModel = new ProjectModel();
		} else {
			projectRootDirectory = new File(getIntent().getStringExtra("projectRootDirectory"));
			ProjectModelSerializationUtils.deserialize(
					new File(projectRootDirectory, EnvironmentUtils.PROJECT_CONFIGRATION),
					new ProjectModelSerializationUtils.DeserializerListener() {

						@Override
						public void onSuccessfullyDeserialized(ProjectModel object) {
							mProjectModel = object;
						}

						@Override
						public void onFailed(int errorCode, Exception e) {
							finish();
						}
					});
		}

		// Set layout of activity
		setContentView(binding.getRoot());

		// SetUp the toolbar
		binding.toolbar.setTitle(R.string.create_new_project);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// Initialize the viewpager to show the fragments
		mProjectModelConfigAdapter = new ProjectModelConfigAdapter(this, isNewProject, mProjectModel);

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
		binding.pageStatus.setText(
				"Step "
						+ String.valueOf(binding.viewpager.getCurrentItem() + 1)
						+ " out of "
						+ mProjectModelConfigAdapter.fragments.size());
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
					if ((mProjectModelConfigAdapter.fragments.size() - 1) != binding.viewpager.getCurrentItem()) {
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
		if (!isRequiredFieldsProperlyFilled) {
			MaterialAlertDialogBuilder fieldsNotProperlyField = new MaterialAlertDialogBuilder(this);
			fieldsNotProperlyField.setTitle(R.string.an_error_occured);
			fieldsNotProperlyField.setMessage(R.string.fields_not_filled_properly);
			fieldsNotProperlyField.setPositiveButton(R.string.done, (arg0, arg1) -> {
			});
			fieldsNotProperlyField.create().show();
		} else {
			if (mProjectModel != null) {
				for (int position = 0; position < mProjectModelConfigAdapter.fragments.size(); ++position) {
					mProjectModelConfigAdapter.fragments.get(position).addValueInProjectModelOfFragment();
				}
				File projectRootDir = null;
				if (isNewProject) {
					projectRootDir = new File(
							EnvironmentUtils.PROJECTS,
							new File(newProjectDirecotoryName()).getAbsolutePath());
					if (!projectRootDir.exists()) {
						projectRootDir.mkdirs();
					}
				} else {
					projectRootDir = projectRootDirectory;
					if (!projectRootDir.exists()) {
						projectRootDir.mkdirs();
					}
				}
				SerializerUtil.serialize(
						mProjectModel,
						new File(projectRootDir, EnvironmentUtils.PROJECT_CONFIGRATION),
						new SerializerUtil.SerializerCompletionListener() {

							@Override
							public void onSerializeComplete() {
								Intent data = new Intent();
								setResult(RESULT_OK, data);
								finish();
							}

							@Override
							public void onFailedToSerialize(Exception exception) {
								runOnUiThread(
										() -> {
											Toast.makeText(
													ProjectModelConfigrationActivity.this,
													exception.getMessage(),
													Toast.LENGTH_SHORT)
													.show();
										});
							}
						});
			}
		}
	}

	private String newProjectDirecotoryName() {
		boolean isNameFoundName = false;
		int projectNumber = 100;
		while (!isNameFoundName) {
			if (!new File(EnvironmentUtils.PROJECTS, String.valueOf(projectNumber)).exists()) {
				isNameFoundName = true;
			} else {
				projectNumber++;
			}
		}
		return String.valueOf(projectNumber);
	}
}
