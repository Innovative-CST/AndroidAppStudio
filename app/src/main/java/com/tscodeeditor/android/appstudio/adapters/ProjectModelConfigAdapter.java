/*
 *  This file is part of Android AppStudio.
 *
 *  Android AppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Android AppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Android AppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.tscodeeditor.android.appstudio.adapters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.tscodeeditor.android.appstudio.fragments.projectmodelconfig.ProjectModelAppConfigrationFragment;
import com.tscodeeditor.android.appstudio.fragments.projectmodelconfig.ProjectModelAppSetupFragment;
import com.tscodeeditor.android.appstudio.fragments.projectmodelconfig.ProjectModelConfigBaseFragment;
import java.util.ArrayList;

public class ProjectModelConfigAdapter extends FragmentStateAdapter {
  public ArrayList<ProjectModelConfigBaseFragment> fragments;

  public ProjectModelConfigAdapter(AppCompatActivity activity) {
    super(activity);
    fragments = new ArrayList<ProjectModelConfigBaseFragment>();
    fragments.add(new ProjectModelAppSetupFragment());
    fragments.add(new ProjectModelAppConfigrationFragment());
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
