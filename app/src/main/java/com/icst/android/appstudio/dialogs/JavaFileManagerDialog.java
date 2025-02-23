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

package com.icst.android.appstudio.dialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.JavaFileManagerActivity;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.model.JavaFileModel;
import com.icst.android.appstudio.builtin.filemodels.BuiltInActivityFileModel;
import com.icst.android.appstudio.databinding.DialogJavaManagerBinding;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.FileModelUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

public class JavaFileManagerDialog extends MaterialAlertDialogBuilder {
	private JavaFileManagerActivity activity;
	private ArrayList<FileModel> folderList;
	private ArrayList<JavaFileModel> javaFilesList;
	private ArrayList<File> pathList;
	private ModuleModel module;
	private String packageName;

	private DialogJavaManagerBinding binding;

	public JavaFileManagerDialog(
			JavaFileManagerActivity activity,
			ArrayList<FileModel> folderList,
			ArrayList<JavaFileModel> javaFilesList,
			ArrayList<File> pathList,
			ModuleModel module,
			String packageName) {
		super(activity);

		this.activity = activity;
		this.folderList = folderList;
		this.javaFilesList = javaFilesList;
		this.pathList = pathList;
		this.module = module;
		this.packageName = packageName;

		binding = DialogJavaManagerBinding.inflate(activity.getLayoutInflater());
		if (packageName.isEmpty()) {
			binding.activity.setVisibility(View.GONE);
		}
		setView(binding.getRoot());
		binding.fileName.setSingleLine(true);

		binding.fileName.addTextChangedListener(
				new TextWatcher() {

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					}

					@Override
					public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
						syncError();
					}

					@Override
					public void afterTextChanged(Editable arg0) {
					}
				});

		binding.fileTypeChooser.addOnButtonCheckedListener(
				(group, id, isChecked) -> {
					syncError();
				});

		setPositiveButton(
				R.string.done,
				(param1, param2) -> {
					if (binding.fileTypeChooser.getCheckedButtonId() == R.id.activity) {
						if (validateClassNameAndExistance(binding.fileName.getText().toString())) {
							binding.fileNameInputLayout.setErrorEnabled(false);
							File classPath = new File(
									EnvironmentUtils.getJavaDirectory(module, packageName),
									binding.fileName.getText().toString().concat(".java"));
							classPath.mkdirs();
							new File(classPath, EnvironmentUtils.EVENTS_DIR).mkdirs();
							SerializerUtil.serialize(
									BuiltInActivityFileModel.getActivityFileModel(
											binding.fileName.getText().toString()),
									new File(classPath, EnvironmentUtils.JAVA_FILE_MODEL),
									new SerializerUtil.SerializerCompletionListener() {

										@Override
										public void onSerializeComplete() {
											activity.loadFiles();
										}

										@Override
										public void onFailedToSerialize(Exception exception) {
										}
									});

						} else {
							Toast.makeText(activity, "Please enter another name...", Toast.LENGTH_SHORT).show();
						}
					} else if (binding.fileTypeChooser.getCheckedButtonId() == R.id.folder) {
						String finalPackageName = packageName.isEmpty()
								? binding.fileName.getText().toString()
								: packageName.concat(".").concat(binding.fileName.getText().toString());
						if (validatePackageNameAndExistance(
								binding.fileName.getText().toString(), finalPackageName)) {
							binding.fileNameInputLayout.setErrorEnabled(false);
							FileModel folder = FileModelUtils.getFolderModel(binding.fileName.getText().toString());
							File packagePath = new File(
									EnvironmentUtils.getJavaDirectory(module, packageName),
									binding.fileName.getText().toString());
							packagePath.mkdirs();
							new File(packagePath, EnvironmentUtils.FILES).mkdirs();
							SerializerUtil.serialize(
									folder,
									new File(packagePath, EnvironmentUtils.FILE_MODEL),
									new SerializerUtil.SerializerCompletionListener() {

										@Override
										public void onSerializeComplete() {
											activity.loadFiles();
										}

										@Override
										public void onFailedToSerialize(Exception exception) {
										}
									});

						} else {
							Toast.makeText(activity, "Please enter another package name...", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
		setNegativeButton(R.string.cancel, (param1, param2) -> {
		});
	}

	public void syncError() {
		if (binding.fileTypeChooser.getCheckedButtonId() == R.id.activity) {
			if (validateClassNameAndExistance(binding.fileName.getText().toString())) {
				binding.fileNameInputLayout.setErrorEnabled(false);
			} else {
				binding.fileNameInputLayout.setErrorEnabled(true);
				binding.fileNameInputLayout.setError("Please enter another name...");
			}
		} else if (binding.fileTypeChooser.getCheckedButtonId() == R.id.folder) {
			String finalPackageName = packageName.isEmpty()
					? binding.fileName.getText().toString()
					: packageName.concat(".").concat(binding.fileName.getText().toString());
			if (validatePackageNameAndExistance(
					binding.fileName.getText().toString(), finalPackageName)) {
				binding.fileNameInputLayout.setErrorEnabled(false);
			} else {
				binding.fileNameInputLayout.setErrorEnabled(true);
				binding.fileNameInputLayout.setError("Please enter another package...");
			}
		}
	}

	public boolean validateClassNameAndExistance(String fileName) {
		for (int files = 0; files < javaFilesList.size(); ++files) {
			if (javaFilesList.get(files).getFileName().equals(fileName)) {
				return false;
			}
		}

		for (int files = 0; files < folderList.size(); ++files) {
			if (folderList.get(files).getName().equals(fileName.concat(".java"))) {
				return false;
			}
		}

		return validateClassName(fileName);
	}

	public boolean validatePackageNameAndExistance(String packageName, String finalPackageName) {
		for (int files = 0; files < folderList.size(); ++files) {
			if (folderList.get(files).getName().equals(packageName)) {
				return false;
			}
		}

		return validatePackageName(finalPackageName);
	}

	public boolean validateClassName(String fileName) {
		String regex = "^[a-zA-Z_][a-zA-Z0-9_]*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(fileName);
		return matcher.matches();
	}

	public boolean validatePackageName(String packageName) {
		String regex = "^(?=\\.{0,2}[a-zA-Z_$][\\w$]*(\\.[a-zA-Z_$][\\w$]*)*$)[^\\d][\\w.]*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(packageName);
		return matcher.matches();
	}
}
