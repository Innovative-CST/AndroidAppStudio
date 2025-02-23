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

import java.io.File;
import java.util.HashMap;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.builtin.blocks.GradleDepedencyBlocks;
import com.icst.android.appstudio.databinding.ActivityEventEditorBinding;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.models.ProjectModel;
import com.icst.android.appstudio.models.SettingModel;
import com.icst.android.appstudio.utils.BlockUtils;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.SettingUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.utils.serialization.SerializerUtil;

import android.os.Build;
import android.os.Bundle;

public class EventEditorActivity extends BaseActivity {
	private ActivityEventEditorBinding binding;

	private ProjectModel projectModel;
	private ModuleModel module;
	/*
	 * Contains the location of currently selected file model.
	 * For example: /../../Projects/100/../abc/FileModel
	 */
	private File fileModelDirectory;
	/*
	 * Contains the location of event list path.
	 * For example: /../../Projects/100/../../Events/Config
	 */
	private File eventListPath;
	/*
	 * Contains the location of event file path.
	 * For example: /../../Projects/100/../../Events/Config/ActivityBasics
	 */
	private File eventFile;
	/*
	 * Main Event Object
	 */
	private Event event;
	private FileModel file;

	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityEventEditorBinding.inflate(getLayoutInflater());

		// Initialize the files paths

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			module = getIntent().getParcelableExtra("module", ModuleModel.class);
		} else {
			module = (ModuleModel) getIntent().getParcelableExtra("module");
		}

		fileModelDirectory = new File(getIntent().getStringExtra("fileModelDirectory"));
		eventListPath = new File(getIntent().getStringExtra("eventListPath"));
		eventFile = new File(getIntent().getStringExtra("eventFile"));

		setContentView(binding.getRoot());
		// SetUp the toolbar
		binding.toolbar.setTitle(R.string.app_name);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

		DeserializerUtils.deserialize(
				eventFile,
				new DeserializerUtils.DeserializerListener() {

					@Override
					public void onSuccessfullyDeserialized(Object deserializedObject) {
						if (deserializedObject instanceof Event) {
							event = (Event) deserializedObject;
						}
					}

					@Override
					public void onFailed(int errorCode, Exception e) {
					}
				});
		DeserializerUtils.deserialize(
				new File(module.projectRootDirectory, EnvironmentUtils.PROJECT_CONFIGRATION),
				new DeserializerUtils.DeserializerListener() {

					@Override
					public void onSuccessfullyDeserialized(Object deserializedObject) {
						if (deserializedObject instanceof ProjectModel) {
							projectModel = (ProjectModel) deserializedObject;
						}
					}

					@Override
					public void onFailed(int errorCode, Exception e) {
					}
				});

		file = DeserializerUtils.deserialize(fileModelDirectory, FileModel.class);

		if (event == null) {
			finish();
			return;
		}
		if (file == null) {
			finish();
			return;
		}
		if (event.getEventTopBlock() != null) {
			HashMap<String, Object> variables = new HashMap<String, Object>();
			if (projectModel != null) {
				variables.put("ProjectModel", projectModel);
			}
			if (event != null) {
				variables.put("Event", event);
			}
			SettingModel settings = SettingUtils.readSettings(EnvironmentUtils.SETTING_FILE);
			if (settings == null) {
				settings = new SettingModel();
			}
			binding.eventEditor.initEditor(event, settings.isEnabledDarkMode(), variables);
		}

		if (event.getName() != null) {
			if (event.getName().equals("dependenciesBlock")) {
				binding.eventEditor.setHolder(GradleDepedencyBlocks.getGradleDepedencyBlocks());
			} else if (event.getName().equals("androidBlock")) {
				binding.eventEditor.setHolder(GradleDepedencyBlocks.getGradleAndroidBlocks());
			} else {
				binding.eventEditor.setHolder(BlockUtils.loadBlockHolders(file, event));
			}
		}
	}

	@Override
	protected void onPause() {
		binding.eventEditor.loadBlocksInEvent();
		SerializerUtil.serialize(
				event,
				eventFile,
				new SerializerUtil.SerializerCompletionListener() {

					@Override
					public void onSerializeComplete() {
					}

					@Override
					public void onFailedToSerialize(Exception exception) {
					}
				});
		super.onPause();
	}
}
