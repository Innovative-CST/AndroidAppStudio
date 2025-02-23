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

package com.icst.android.appstudio.bottomsheet;

import java.io.File;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.ProjectManagerActivity;
import com.icst.android.appstudio.activities.ProjectModelConfigrationActivity;
import com.icst.android.appstudio.databinding.BottomsheetProjectOptionBinding;

import android.code.editor.common.interfaces.FileDeleteListener;
import android.code.editor.common.utils.FileDeleteUtils;
import android.content.Intent;

public class ProjectOptionsSheet extends BottomSheetDialog {
	private BottomsheetProjectOptionBinding binding;

	public ProjectOptionsSheet(ProjectManagerActivity activity, File projectRootDirectory) {
		super(activity);

		binding = BottomsheetProjectOptionBinding.inflate(activity.getLayoutInflater());

		setContentView(binding.getRoot());
		binding.settings.setOnClickListener(
				v -> {
					Intent modifyProject = new Intent();
					modifyProject.setClass(activity, ProjectModelConfigrationActivity.class);
					modifyProject.putExtra("isNewProject", false);
					modifyProject.putExtra("projectRootDirectory", projectRootDirectory.getAbsolutePath());
					activity.projectListUpdateActivityResultLauncher.launch(modifyProject);
				});
		binding.delete.setOnClickListener(
				v -> {
					MaterialAlertDialogBuilder deleteConfirm = new MaterialAlertDialogBuilder(activity);
					deleteConfirm.setTitle(R.string.warning);
					deleteConfirm.setIcon(R.drawable.ic_alert);
					deleteConfirm.setMessage(R.string.project_delete_warning_text);
					deleteConfirm.setPositiveButton(
							R.string.delete,
							(param1, param2) -> {
								FileDeleteUtils.delete(
										projectRootDirectory,
										new FileDeleteListener() {

											@Override
											public void onProgressUpdate(int deleteDone) {
											}

											@Override
											public void onTotalCount(int total) {
											}

											@Override
											public void onDeleting(File path) {
											}

											@Override
											public void onDeleteComplete(File path) {
											}

											@Override
											public void onTaskComplete() {
												ProjectOptionsSheet.this.dismiss();
												activity.tryToLoadProjects();
											}
										},
										true,
										activity);
							});

					deleteConfirm.setNegativeButton(R.string.cancel, (param1, param2) -> {
					});
					deleteConfirm.create().show();
				});
	}
}
