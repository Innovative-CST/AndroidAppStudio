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
