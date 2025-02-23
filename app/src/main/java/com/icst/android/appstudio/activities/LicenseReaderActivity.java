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

package com.icst.android.appstudio.activities;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.databinding.ActivityLicenseReaderBinding;

import android.code.editor.common.utils.FileUtils;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;

public class LicenseReaderActivity extends BaseActivity {
	private ActivityLicenseReaderBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityLicenseReaderBinding.inflate(getLayoutInflater());
		// set content view to binding's root.
		setContentView(binding.getRoot());

		binding.toolbar.setTitle(R.string.app_name);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

		binding.LicenseText.setAutoLinkMask(Linkify.WEB_URLS);
		binding.LicenseText.setMovementMethod(LinkMovementMethod.getInstance());
		binding.LicenseText.setText(
				FileUtils.readFileFromAssets(getAssets(), getIntent().getStringExtra("Path")));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		binding = null;
	}
}
