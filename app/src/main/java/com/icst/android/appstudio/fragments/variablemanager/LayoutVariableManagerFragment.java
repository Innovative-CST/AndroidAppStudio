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

import com.icst.android.appstudio.adapters.LayoutVariableAdapter;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.model.VariableModel;
import com.icst.android.appstudio.bottomsheet.EditLayoutVariableModelBottomSheet;
import com.icst.android.appstudio.databinding.FragmentLayoutVariableManagerBinding;
import com.icst.android.appstudio.listener.LayoutVariableModelChangeListener;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;
import com.icst.android.appstudio.vieweditor.models.LayoutVariableModel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class LayoutVariableManagerFragment extends Fragment {

	private FragmentLayoutVariableManagerBinding binding;
	private static final int LOADING_SECTION = 0;
	private static final int LIST_SECTION = 1;
	private static final int INFO_SECTION = 2;
	private ModuleModel module;
	private String packageName;
	private String className;
	private FileModel file;
	private ArrayList<VariableModel> layoutNames;
	private ArrayList<LayoutVariableModel> variables;

	@Override
	@MainThread
	@Nullable public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle savedInstanceState) {
		if (savedInstanceState != null || getArguments() != null) {
			Bundle args = null;
			if (savedInstanceState != null) {
				args = savedInstanceState;
			} else {
				args = getArguments();
			}
			module = new ModuleModel();
			module.init(args.getString("module"), new File(args.getString("projectRootDirectory")));
			className = args.getString("className");
			packageName = args.getString("packageName");

			file = DeserializerUtils.deserialize(
					new File(
							new File(
									EnvironmentUtils.getJavaDirectory(module, packageName),
									className.concat(".java")),
							EnvironmentUtils.JAVA_FILE_MODEL),
					FileModel.class);

			variables = DeserializerUtils.deserialize(
					new File(
							new File(
									EnvironmentUtils.getJavaDirectory(module, packageName),
									className.concat(".java")),
							EnvironmentUtils.LAYOUT_VARIABLE),
					ArrayList.class);

			if (variables == null) {
				variables = new ArrayList<LayoutVariableModel>();
			}
		}

		binding = FragmentLayoutVariableManagerBinding.inflate(inflator);

		if (variables.size() == 0) {
			switchSection(INFO_SECTION);
			binding.info.setText("No layout variables yet.");
		} else {
			switchSection(LIST_SECTION);
			binding.list.setAdapter(
					new LayoutVariableAdapter(
							variables, LayoutVariableManagerFragment.this, module, packageName, className));
			binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
		}

		binding.fab.setOnClickListener(
				(v) -> {
					EditLayoutVariableModelBottomSheet editLayoutVariableModelBottomSheet = new EditLayoutVariableModelBottomSheet(
							getContext(),
							module,
							null,
							new LayoutVariableModelChangeListener() {
								@Override
								public void onLayoutVariableModelDelete() {
									// Cancel is clicked
								}

								@Override
								public void onLayoutVariableModelUpdate(LayoutVariableModel model) {
									variables.add(model);
									saveVariables();
									loadVariables();
								}
							});

					editLayoutVariableModelBottomSheet.show();
				});

		return binding.getRoot();
	}

	public void saveVariables() {
		if (variables.size() != 0) {
			SerializerUtil.serialize(
					variables,
					new File(
							new File(
									EnvironmentUtils.getJavaDirectory(module, packageName),
									className.concat(".java")),
							EnvironmentUtils.LAYOUT_VARIABLE),
					new SerializerUtil.SerializerCompletionListener() {

						@Override
						public void onFailedToSerialize(Exception exception) {
						}

						@Override
						public void onSerializeComplete() {
						}
					});
		} else {
			new File(
					new File(
							EnvironmentUtils.getJavaDirectory(module, packageName),
							className.concat(".java")),
					EnvironmentUtils.LAYOUT_VARIABLE)
					.delete();
		}
	}

	public void loadVariables() {
		switchSection(LOADING_SECTION);
		Thread thread = new Thread(
				() -> {
					variables = DeserializerUtils.deserialize(
							new File(
									new File(
											EnvironmentUtils.getJavaDirectory(module, packageName),
											className.concat(".java")),
									EnvironmentUtils.LAYOUT_VARIABLE),
							ArrayList.class);

					if (variables == null) {
						variables = new ArrayList<LayoutVariableModel>();
						getActivity()
								.runOnUiThread(
										() -> {
											switchSection(INFO_SECTION);
											binding.info.setText("No layout variables yet.");
										});
					} else {
						getActivity()
								.runOnUiThread(
										() -> {
											switchSection(LIST_SECTION);
											binding.list.setAdapter(
													new LayoutVariableAdapter(
															variables,
															LayoutVariableManagerFragment.this,
															module,
															packageName,
															className));
											binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
										});
					}
				});
		thread.start();
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
