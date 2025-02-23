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

import com.icst.android.appstudio.R.id;
import com.icst.android.appstudio.databinding.FragmentJavaVariableManagerBinding;
import com.icst.android.appstudio.models.ModuleModel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class JavaVariableManagerFragment extends Fragment {
	private FragmentJavaVariableManagerBinding binding;
	private ModuleModel module;
	private String packageName;
	private String className;

	public JavaVariableManagerFragment() {
	}

	public JavaVariableManagerFragment(ModuleModel module, String packageName, String className) {
		this.module = module;
		this.packageName = packageName;
		this.className = className;
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
		}

		binding = FragmentJavaVariableManagerBinding.inflate(inflator);
		binding.bottomNavigationView.setOnItemSelectedListener(
				menu -> {
					if (menu.getItemId() == id.static_variables) {
						getActivity()
								.getSupportFragmentManager()
								.beginTransaction()
								.replace(
										id.fragment_container,
										new StaticVariableManagerFragment(module, packageName, className))
								.commit();
					} else if (menu.getItemId() == id.non_static_variables) {
						getActivity()
								.getSupportFragmentManager()
								.beginTransaction()
								.replace(
										id.fragment_container,
										new NonStaticVariableManagerFragment(module, packageName, className))
								.commit();
					} else if (menu.getItemId() == id.layout_variables) {
						LayoutVariableManagerFragment layoutManager = new LayoutVariableManagerFragment();
						Bundle args = new Bundle();
						args.putString("module", module.module);
						args.putString("projectRootDirectory", module.projectRootDirectory.getAbsolutePath());
						args.putString("className", className);
						args.putString("packageName", packageName);
						layoutManager.setArguments(args);
						getActivity()
								.getSupportFragmentManager()
								.beginTransaction()
								.replace(id.fragment_container, layoutManager)
								.commit();
					}
					return true;
				});
		binding.bottomNavigationView.setSelectedItemId(id.non_static_variables);
		return binding.getRoot();
	}
}
