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

package com.icst.android.appstudio.fragments.variablemanager;

import java.io.File;
import java.util.ArrayList;

import com.icst.android.appstudio.adapters.VariableListAdapter;
import com.icst.android.appstudio.block.dialog.variables.ChooseVariablesDialog;
import com.icst.android.appstudio.block.dialog.variables.EditVariableDialog;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.model.VariableModel;
import com.icst.android.appstudio.databinding.FragmentNonStaticVariableBinding;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.VariablesUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NonStaticVariableManagerFragment extends Fragment {
	private FragmentNonStaticVariableBinding binding;
	private ModuleModel module;
	private String packageName;
	private String className;
	private FileModel file;
	private ArrayList<VariableModel> variables;

	private static final int LOADING_SECTION = 0;
	private static final int LIST_SECTION = 1;
	private static final int INFO_SECTION = 2;

	public NonStaticVariableManagerFragment() {
	}

	public NonStaticVariableManagerFragment(
			ModuleModel module, String packageName, String className) {
		this.module = module;
		this.packageName = packageName;
		this.className = className;
		file = DeserializerUtils.deserialize(
				new File(
						new File(
								EnvironmentUtils.getJavaDirectory(module, packageName),
								className.concat(".java")),
						EnvironmentUtils.JAVA_FILE_MODEL),
				FileModel.class);
	}

	@Override
	@MainThread
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putString("module", module.module);
		bundle.putString("projectRootDirectory", module.projectRootDirectory.getAbsolutePath());
		bundle.putString("className", className);
		bundle.putString("packageName", packageName);
	}

	@Override
	@MainThread
	@Nullable public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			module = new ModuleModel();
			module.init(
					savedInstanceState.getString("module"),
					new File(savedInstanceState.getString("projectRootDirectory")));
			className = savedInstanceState.getString("className");
			packageName = savedInstanceState.getString("packageName");

			file = DeserializerUtils.deserialize(
					new File(
							new File(
									EnvironmentUtils.getJavaDirectory(module, packageName),
									className.concat(".java")),
							EnvironmentUtils.JAVA_FILE_MODEL),
					FileModel.class);
		}

		binding = FragmentNonStaticVariableBinding.inflate(inflator);
		loadList();
		binding.fab.setOnClickListener(
				v -> {
					ChooseVariablesDialog dialog = new ChooseVariablesDialog(getContext(),
							VariablesUtils.getInstanceVariables(file)) {
						@Override
						public void onSelectedVariable(VariableModel selectedVariable) {
							EditVariableDialog addVar = new EditVariableDialog(getContext(), selectedVariable.clone()) {
								@Override
								public void onVariableModified(VariableModel variable) {
									variables.add(variable);
									SerializerUtil.serialize(
											variables,
											new File(
													new File(
															EnvironmentUtils.getJavaDirectory(module, packageName),
															className.concat(".java")),
													EnvironmentUtils.VARIABLES),
											new SerializerUtil.SerializerCompletionListener() {

												@Override
												public void onSerializeComplete() {
												}

												@Override
												public void onFailedToSerialize(Exception exception) {
												}
											});
									loadList();
								}
							};
						}
					};
				});

		return binding.getRoot();
	}

	public void loadList() {
		variables = DeserializerUtils.deserialize(
				new File(
						new File(
								EnvironmentUtils.getJavaDirectory(module, packageName),
								className.concat(".java")),
						EnvironmentUtils.VARIABLES),
				ArrayList.class);

		if (variables == null) {
			variables = new ArrayList<VariableModel>();
		}
		if (variables.size() > 0) {
			binding.list.setAdapter(
					new VariableListAdapter(variables, this, module, packageName, className));
			binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
			switchSection(LIST_SECTION);
		} else {
			switchSection(INFO_SECTION);
			binding.info.setText("No variables yet");
		}
	}

	private void switchSection(int section) {
		binding.loadingSection.setVisibility(LOADING_SECTION == section ? View.VISIBLE : View.GONE);
		binding.listSection.setVisibility(LIST_SECTION == section ? View.VISIBLE : View.GONE);
		binding.infoSection.setVisibility(INFO_SECTION == section ? View.VISIBLE : View.GONE);
	}

	private void showInfo(int info) {
		switchSection(INFO_SECTION);
		binding.info.setText(info);
	}
}
