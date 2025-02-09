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

package com.icst.android.appstudio.dialogs.resourcemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.resourcemanager.LayoutManagerActivity;
import com.icst.android.appstudio.databinding.DialogCreateLayoutBinding;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;
import com.icst.android.appstudio.vieweditor.models.LayoutModel;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

public class ManageLayoutDialog extends MaterialAlertDialogBuilder {

	private ArrayList<LayoutModel> layoutsList;
	private ArrayList<File> filesList;
	private DialogCreateLayoutBinding binding;

	public ManageLayoutDialog(
			LayoutManagerActivity activity,
			ArrayList<LayoutModel> layoutsList,
			ArrayList<File> filesList,
			File layoutDirectory) {
		super(activity);

		this.layoutsList = layoutsList;
		this.filesList = filesList;

		binding = DialogCreateLayoutBinding.inflate(activity.getLayoutInflater());
		setView(binding.getRoot());
		binding.layoutName.setSingleLine(true);

		binding.layoutName.addTextChangedListener(
				new TextWatcher() {

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					}

					@Override
					public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
						if (validateLayoutNameAndExistance(binding.layoutName.getText().toString())) {
							binding.layoutNameInputLayout.setErrorEnabled(true);
							binding.layoutNameInputLayout.setError("Please enter another name...");
						} else {
							binding.layoutNameInputLayout.setErrorEnabled(false);
						}
					}

					@Override
					public void afterTextChanged(Editable arg0) {
					}
				});

		setPositiveButton(
				R.string.done,
				(param1, param2) -> {
					if (validateLayoutNameAndExistance(binding.layoutName.getText().toString())) {
						Toast.makeText(activity, "Please enter another name...", Toast.LENGTH_SHORT).show();
					} else {
						binding.layoutNameInputLayout.setErrorEnabled(false);
						LayoutModel layout = new LayoutModel();
						layout.setLayoutName(binding.layoutName.getText().toString());
						SerializerUtil.serialize(
								layout,
								new File(layoutDirectory, binding.layoutName.getText().toString()),
								new SerializerUtil.SerializerCompletionListener() {

									@Override
									public void onSerializeComplete() {
										activity.loadLayouts();
									}

									@Override
									public void onFailedToSerialize(Exception exception) {
									}
								});
					}
				});
		setNegativeButton(R.string.cancel, (param1, param2) -> {
		});
	}

	public boolean validateLayoutNameAndExistance(String fileName) {
		for (int files = 0; files < filesList.size(); ++files) {
			if (filesList.get(files).getName().equals(fileName)) {
				return true;
			}
		}

		for (int files = 0; files < layoutsList.size(); ++files) {
			if (layoutsList.get(files).getLayoutName().equals(fileName)) {
				return true;
			}
		}

		return !validateLayoutName(fileName.concat(".xml"));
	}

	public boolean validateLayoutName(String fileName) {
		String regex = "^[a-z][a-z0-9_]*\\.xml$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(fileName);
		return matcher.matches();
	}
}
