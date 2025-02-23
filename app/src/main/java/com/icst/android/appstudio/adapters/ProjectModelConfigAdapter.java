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

import com.icst.android.appstudio.fragments.projectmodelconfig.ProjectModelAppConfigrationFragment;
import com.icst.android.appstudio.fragments.projectmodelconfig.ProjectModelAppSetupFragment;
import com.icst.android.appstudio.fragments.projectmodelconfig.ProjectModelConfigBaseFragment;
import com.icst.android.appstudio.models.ProjectModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ProjectModelConfigAdapter extends FragmentStateAdapter {
	public ArrayList<ProjectModelConfigBaseFragment> fragments;
	private boolean isNewProject;
	private ProjectModel mProjectModel;

	public ProjectModelConfigAdapter(
			AppCompatActivity activity, boolean isNewProject, ProjectModel mProjectModel) {
		super(activity);
		this.fragments = fragments;
		this.isNewProject = isNewProject;
		this.mProjectModel = mProjectModel;
		fragments = new ArrayList<ProjectModelConfigBaseFragment>();
		fragments.add(new ProjectModelAppSetupFragment(isNewProject, mProjectModel));
		fragments.add(new ProjectModelAppConfigrationFragment(isNewProject, mProjectModel));
	}

	@Override
	public int getItemCount() {
		return fragments.size();
	}

	@Override
	public Fragment createFragment(int position) {
		return fragments.get(position);
	}

	public ArrayList<ProjectModelConfigBaseFragment> getFragments() {
		return this.fragments;
	}

	public void setFragments(ArrayList<ProjectModelConfigBaseFragment> fragments) {
		this.fragments = fragments;
	}
}
