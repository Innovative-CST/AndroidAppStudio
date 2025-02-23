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
import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.databinding.ActivityEventsBinding;
import com.icst.android.appstudio.dialogs.SourceCodeViewerDialog;
import com.icst.android.appstudio.fragments.events.EventListFragment;
import com.icst.android.appstudio.helper.FileModelCodeHelper;
import com.icst.android.appstudio.models.EventHolder;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.EventsHolderUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class EventsActivity extends BaseActivity {

	private ActivityEventsBinding binding;
	/*
	 * Contains the location of currently selected file model.
	 * For example: /../../Project/100/../abc/FileModel
	 */
	private File fileModelDirectory;
	/*
	 * Contains the location of currently loaded event list.
	 * For example: /../../Project/100/../abc/Events
	 */
	private File eventsDir;

	private ModuleModel module;

	private MenuItem showSourceCode;
	private FileModel fileModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize the variables
		module = new ModuleModel();
		module.init(
				getIntent().getStringExtra("module"),
				new File(getIntent().getStringExtra("projectRootDirectory")));
		fileModelDirectory = new File(getIntent().getStringExtra("fileModelDirectory"));
		eventsDir = new File(fileModelDirectory.getParentFile(), EnvironmentUtils.EVENTS_DIR);
		binding = ActivityEventsBinding.inflate(getLayoutInflater());

		// Sets the layout of Activity
		setContentView(binding.getRoot());

		// Handles Toolbar
		binding.toolbar.setTitle(R.string.app_name);
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

		DeserializerUtils.deserialize(
				fileModelDirectory,
				new DeserializerUtils.DeserializerListener() {

					@Override
					public void onSuccessfullyDeserialized(Object object) {
						if (object instanceof FileModel) {
							fileModel = (FileModel) object;
						}
					}

					@Override
					public void onFailed(int errorCode, Exception e) {
					}
				});

		if (fileModel == null) {
			Toast.makeText(this, R.string.failed_to_deserialize_file_model, Toast.LENGTH_SHORT).show();
			finish();
		}

		Executors.newSingleThreadExecutor()
				.execute(
						() -> {
							ArrayList<EventHolder> eventHolderList = EventsHolderUtils.getEventHolder(eventsDir);
							runOnUiThread(
									() -> {
										// Loading the events
										loadEventData(eventHolderList);
									});
						});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		binding = null;
	}

	/*
	 * Loads the NavigationRail View and Events.
	 */
	private void loadEventData(ArrayList<EventHolder> eventHolderList) {
		/*
		 * ** Loads the Navigation menu **
		 * Adds the MenuItem in NavigationRail.
		 * Retreviews the MenuItem Icon from EventHolder.
		 * Set the current fragment if EventList of a Fragment is attached to a
		 * FileModel.
		 */
		for (int position = 0; position < eventHolderList.size(); ++position) {
			Menu menu = binding.navigationRail.getMenu();
			MenuItem item = menu.add(Menu.NONE, position, Menu.NONE, eventHolderList.get(position).getHolderName());
			item.setIcon(getEventHolderIcon(this, eventHolderList.get(position)));
			if (eventHolderList.get(position).isBuiltInEvents()) {
				getSupportFragmentManager()
						.beginTransaction()
						.replace(
								R.id.fragment_container,
								new EventListFragment(
										module,
										fileModelDirectory,
										new File(
												eventHolderList.get(position).getFilePath(),
												EnvironmentUtils.EVENTS_DIR),
										eventHolderList.get(position).getDisableNewEvents()))
						.commit();
			}
		}
		/*
		 * Sets the click listener of NavigationRail.
		 * Treating the MenuItem Id as position of ArrayList
		 * Retreview the FilePath of EventList from EventHolder itself.
		 */
		binding.navigationRail.setOnItemSelectedListener(
				(menuItem) -> {
					int position = menuItem.getItemId();
					getSupportFragmentManager()
							.beginTransaction()
							.replace(
									R.id.fragment_container,
									new EventListFragment(
											module,
											fileModelDirectory,
											new File(
													eventHolderList.get(position).getFilePath(),
													EnvironmentUtils.EVENTS_DIR),
											eventHolderList.get(position).getDisableNewEvents()))
							.commit();
					return true;
				});
	}

	public static Drawable getEventHolderIcon(Context context, EventHolder holder) {
		if (holder.getIcon() != null) {
			return new BitmapDrawable(
					context.getResources(),
					BitmapFactory.decodeByteArray(holder.getIcon(), 0, holder.getIcon().length));
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_show_code, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		showSourceCode = menu.findItem(R.id.show_source_code);
		if (fileModel == null) {
			if (showSourceCode != null) {
				showSourceCode.setVisible(false);
			}
		} else {
			if (showSourceCode != null) {
				showSourceCode.setVisible(true);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		if (menuItem.getItemId() == R.id.show_source_code) {
			if (fileModel != null) {
				FileModelCodeHelper helper = new FileModelCodeHelper();
				helper.setFileModel(fileModel);
				helper.setEventsDirectory(eventsDir);
				helper.setProjectRootDirectory(module.projectRootDirectory);
				SourceCodeViewerDialog sourceCodeDialog = new SourceCodeViewerDialog(this, fileModel, helper.getCode());
				sourceCodeDialog.create().show();
			}
		}

		return super.onOptionsItemSelected(menuItem);
	}
}
