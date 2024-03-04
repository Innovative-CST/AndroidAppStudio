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

package com.tscodeeditor.android.appstudio.fragments.projectmodelconfig;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import com.tscodeeditor.android.appstudio.databinding.FragmentProjectModelAppsetupLayoutBinding;

public class ProjectModelAppSetupFragment extends ProjectModelConfigBaseFragment {
  private FragmentProjectModelAppsetupLayoutBinding binding;

  @Override
  @MainThread
  @Nullable
  public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
    binding = FragmentProjectModelAppsetupLayoutBinding.inflate(layoutInflater);
    return binding.getRoot();
  }
}
