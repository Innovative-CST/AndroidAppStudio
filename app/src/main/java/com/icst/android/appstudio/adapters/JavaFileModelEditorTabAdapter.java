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

package com.icst.android.appstudio.adapters;

import java.util.ArrayList;

import com.icst.android.appstudio.activities.BaseActivity;
import com.icst.android.appstudio.fragments.events.JavaEventManagerFragment;
import com.icst.android.appstudio.fragments.variablemanager.JavaVariableManagerFragment;
import com.icst.android.appstudio.models.ModuleModel;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class JavaFileModelEditorTabAdapter extends FragmentStateAdapter {

	private ArrayList<Fragment> fragments;
	private ModuleModel module;
	private String packageName;
	private String className;
	private boolean disableNewEvents;

	public JavaFileModelEditorTabAdapter(
			BaseActivity activity,
			ModuleModel module,
			String packageName,
			String className,
			boolean disableNewEvents) {
		super(activity);
		this.module = module;
		this.packageName = packageName;
		this.className = className;
		this.disableNewEvents = disableNewEvents;

		fragments = new ArrayList<Fragment>();
		fragments.add(new JavaVariableManagerFragment(module, packageName, className));
		fragments.add(new JavaEventManagerFragment(module, packageName, className, disableNewEvents));
	}

	@Override
	public int getItemCount() {
		return fragments.size();
	}

	@Override
	public Fragment createFragment(int position) {
		return fragments.get(position);
	}
}
