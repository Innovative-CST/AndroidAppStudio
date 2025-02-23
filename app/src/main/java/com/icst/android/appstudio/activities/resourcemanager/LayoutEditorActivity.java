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

package com.icst.android.appstudio.activities.resourcemanager;

import java.io.File;

import com.elfilibustero.uidesigner.ui.designer.LayoutDesigner;
import com.icst.android.appstudio.activities.BaseActivity;
import com.icst.android.appstudio.databinding.ActivityLayoutEditorBinding;
import com.icst.android.appstudio.dialogs.LayoutSourceViewerDialog;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;
import com.icst.android.appstudio.vieweditor.R;
import com.icst.android.appstudio.vieweditor.editor.ViewEditor;
import com.icst.android.appstudio.vieweditor.models.LayoutModel;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;

public class LayoutEditorActivity extends BaseActivity {
	// Contants for showing the section easily
	public static final int LOADING_SECTION = 0;
	public static final int EDITOR_SECTION = 1;
	public static final int ERROR_SECTION = 2;

	private ActivityLayoutEditorBinding binding;
	private ViewEditor editor;
	private ModuleModel module;
	private LayoutModel layout;
	private File layoutDirectory;
	private File layoutFileDirectory;
	private File layoutDirectoryOutput;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			module = getIntent().getParcelableExtra("module", ModuleModel.class);
		} else {
			module = (ModuleModel) getIntent().getParcelableExtra("module");
		}

		String layoutDirectoryName = getIntent().getStringExtra("layoutDirectoryName");
		String layoutFile = getIntent().getStringExtra("layoutFileName");
		layoutDirectory = new File(
				new File(
						new File(module.resourceDirectory, EnvironmentUtils.FILES), layoutDirectoryName),
				EnvironmentUtils.FILES);
		layoutFileDirectory = new File(layoutFile);
		layoutDirectoryOutput = new File(module.resourceOutputDirectory, layoutDirectoryName);

		binding = ActivityLayoutEditorBinding.inflate(getLayoutInflater());
		editor = binding.editor;
		layout = DeserializerUtils.deserialize(layoutFileDirectory, LayoutModel.class);

		if (layout == null) {
			layout = new LayoutModel();
			layout.setLayoutName(layoutDirectoryName);
		} else
			editor.setLayoutModel(layout);

		setContentView(binding.getRoot());
		// SetUp the toolbar
		binding.toolbar.setTitle(R.string.app_name);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		switchSection(EDITOR_SECTION);
		addMenuProvider(
				new MenuProvider() {
					@Override
					public void onCreateMenu(Menu menu, MenuInflater menuInflater) {
						menuInflater.inflate(R.menu.project, menu);
					}

					@Override
					public boolean onMenuItemSelected(MenuItem menuItem) {
						var id = menuItem.getItemId();
						if (id == R.id.device_size) {
							selectDeviceSize(findViewById(id));
						} else if (id == R.id.source_code) {
							LayoutModel layout = editor.getLayoutModel();
							LayoutSourceViewerDialog dialog = new LayoutSourceViewerDialog(LayoutEditorActivity.this,
									layout.getCode());
							dialog.show();
							return true;
						}
						return false;
					}
				},
				this,
				Lifecycle.State.RESUMED);
	}

	/*
	 * Method for switching the section quickly.
	 * All other section will be GONE except the section of which the section code
	 * is provided
	 */
	public void switchSection(int section) {
		binding.loading.setVisibility(section == LOADING_SECTION ? View.VISIBLE : View.GONE);
		binding.layoutEditorSection.setVisibility(section == EDITOR_SECTION ? View.VISIBLE : View.GONE);
		binding.errorSection.setVisibility(section == ERROR_SECTION ? View.VISIBLE : View.GONE);
	}

	public void showError(String errorText) {
		switchSection(ERROR_SECTION);
		binding.errorText.setText(errorText);
	}

	private void selectDeviceSize(View view) {
		final PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
		popupMenu.inflate(R.menu.menu_size);
		popupMenu.setOnMenuItemClickListener(
				item -> {
					var id = item.getItemId();
					if (id == R.id.device_size_small) {
						editor.setSize(LayoutDesigner.Size.SMALL);
					} else if (id == R.id.device_size_default) {
						editor.setSize(LayoutDesigner.Size.DEFAULT);
					} else if (id == R.id.device_size_large) {
						editor.setSize(LayoutDesigner.Size.LARGE);
					}
					return true;
				});

		popupMenu.show();
	}

	@Override
	@Deprecated
	@MainThread
	@CallSuper
	public void onBackPressed() {
		SerializerUtil.serialize(
				editor.getLayoutModel(),
				layoutFileDirectory,
				new SerializerUtil.SerializerCompletionListener() {
					@Override
					public void onFailedToSerialize(Exception exception) {
						Toast.makeText(LayoutEditorActivity.this, "Failed to save", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSerializeComplete() {
						LayoutEditorActivity.super.onBackPressed();
					}
				});
	}
}
