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
