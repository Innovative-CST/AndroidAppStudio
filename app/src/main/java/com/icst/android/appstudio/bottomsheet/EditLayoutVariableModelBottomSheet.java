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
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.databinding.BottomsheetLayoutVariableModelEditorBinding;
import com.icst.android.appstudio.listener.LayoutVariableModelChangeListener;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.FileModelUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.vieweditor.models.LayoutModel;
import com.icst.android.appstudio.vieweditor.models.LayoutVariableModel;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

public class EditLayoutVariableModelBottomSheet extends BottomSheetDialog {

	private LayoutVariableModel model;
	private ModuleModel module;

	public EditLayoutVariableModelBottomSheet(
			Context context,
			ModuleModel module,
			LayoutVariableModel model,
			LayoutVariableModelChangeListener listener) {
		super(context);
		this.model = model;
		this.module = module;

		BottomsheetLayoutVariableModelEditorBinding binding = BottomsheetLayoutVariableModelEditorBinding
				.inflate(LayoutInflater.from(context));

		setContentView(binding.getRoot());

		ArrayAdapter adapter = new ArrayAdapter<String>(
				getContext(), R.layout.autocomplete_adapter_layout_chooser, R.id.variableName);
		ArrayList<String> layoutNames = new ArrayList<String>();
		addLayoutNamesToList(layoutNames, adapter);

		binding.layoutName.setAdapter(adapter);

		binding.layoutVariableName.addTextChangedListener(
				new TextWatcher() {

					@Override
					public void afterTextChanged(Editable arg0) {
					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					}

					@Override
					public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

						binding.done.setEnabled(
								shouldEnableActionButton(
										binding.layoutName.getText().toString(),
										binding.layoutVariableName.getText().toString()));
					}
				});

		binding.layoutName.addTextChangedListener(
				new TextWatcher() {

					@Override
					public void afterTextChanged(Editable arg0) {
					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					}

					@Override
					public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

						binding.done.setEnabled(
								shouldEnableActionButton(
										binding.layoutName.getText().toString(),
										binding.layoutVariableName.getText().toString()));
					}
				});

		binding.done.setEnabled(
				shouldEnableActionButton(
						binding.layoutName.getText().toString(),
						binding.layoutVariableName.getText().toString()));

		if (this.model == null) {
			binding.delete.setText("Cancel");
		} else {
			binding.layoutName.setText(model.getLayoutName());
			binding.layoutVariableName.setText(model.getVariableName());
		}

		binding.delete.setOnClickListener(
				v -> {
					if (this.model != null) {
						listener.onLayoutVariableModelDelete();
					}
					dismiss();
				});
		binding.done.setOnClickListener(
				v -> {
					if (this.model == null) {
						this.model = new LayoutVariableModel();
					}
					this.model.setLayoutName(binding.layoutName.getText().toString());
					this.model.setVariableName(binding.layoutVariableName.getText().toString());
					listener.onLayoutVariableModelUpdate(this.model);
					dismiss();
				});
	}

	public boolean shouldEnableActionButton(String str1, String str2) {
		if (str1.isEmpty() || str2.isEmpty()) {
			return false;
		}
		return true;
	}

	public void addLayoutNamesToList(ArrayList<String> layoutNames, ArrayAdapter<String> adapter) {
		File resListDirectory = new File(module.resourceDirectory, EnvironmentUtils.FILES);
		ArrayList<FileModel> resFolders = FileModelUtils.getFileModelList(resListDirectory);

		if (resFolders == null)
			return;

		for (int position = 0; position < resFolders.size(); ++position) {

			if (Pattern.compile("^layout(?:-[a-zA-Z0-9]+)?$")
					.matcher(resFolders.get(position).getName())
					.matches()) {

				File layoutsDir = new File(
						resListDirectory,
						resFolders
								.get(position)
								.getName()
								.concat(File.separator)
								.concat(EnvironmentUtils.FILES));

				if (!layoutsDir.exists())
					return;

				for (File layoutFile : layoutsDir.listFiles()) {
					LayoutModel layoutFileModel = DeserializerUtils.deserialize(layoutFile, LayoutModel.class);

					if (layoutFileModel != null) {
						boolean alreadyPresentLayout = false;
						for (int i = 0; i < layoutNames.size(); ++i) {
							if (layoutNames.get(i).equals(layoutFileModel.getLayoutName())) {
								alreadyPresentLayout = true;
							}
						}

						if (!alreadyPresentLayout) {
							layoutNames.add(layoutFileModel.getLayoutName());
							adapter.add(layoutFileModel.getLayoutName());
						}
					}
				}
			}
		}
	}
}
