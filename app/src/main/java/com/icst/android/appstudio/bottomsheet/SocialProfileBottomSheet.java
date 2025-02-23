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

import java.util.ArrayList;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.icst.android.appstudio.adapters.SocialProfileAdapter;
import com.icst.android.appstudio.databinding.BottomsheetSocialProfileBottomSheetBinding;
import com.icst.android.appstudio.models.SocialProfile;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;

public class SocialProfileBottomSheet extends BottomSheetDialog {
	public SocialProfileBottomSheet(ArrayList<SocialProfile> socialProfiles, Activity activity) {
		super(activity);

		BottomsheetSocialProfileBottomSheetBinding binding = BottomsheetSocialProfileBottomSheetBinding
				.inflate(activity.getLayoutInflater());
		setContentView(binding.getRoot());

		binding.profilesList.setAdapter(new SocialProfileAdapter(socialProfiles, activity));
		binding.profilesList.setLayoutManager(new LinearLayoutManager(activity));
	}
}
