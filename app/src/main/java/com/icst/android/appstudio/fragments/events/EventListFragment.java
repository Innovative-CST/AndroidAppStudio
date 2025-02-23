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

package com.icst.android.appstudio.fragments.events;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.adapters.EventAdapter;
import com.icst.android.appstudio.databinding.FragmentEventListBinding;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EventUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class EventListFragment extends Fragment {
	private FragmentEventListBinding binding;
	private ModuleModel module;
	/*
	 * Contains the location of currently selected file model.
	 * For example: /../../Project/100/../abc/FileModel
	 */
	private File fileModelDirectory;
	/*
	 * Contains the location of event list path.
	 * For example: /../../Project/100/../../Events/Config
	 */
	private File eventListPath;
	private boolean disableNewEvents;

	private static final int LOADING_SECTION = 0;
	private static final int LIST_SECTION = 1;
	private static final int INFO_SECTION = 2;

	public EventListFragment(
			ModuleModel module, File fileModelDirectory, File eventListPath, boolean disableNewEvents) {
		this.module = module;
		this.eventListPath = eventListPath;
		this.fileModelDirectory = fileModelDirectory;
		this.disableNewEvents = disableNewEvents;
	}

	public EventListFragment() {
	}

	@Override
	@MainThread
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putString("module", module.module);
		bundle.putString("projectRootDirectory", module.projectRootDirectory.getAbsolutePath());
		bundle.putString("fileModelDirectory", fileModelDirectory.getAbsolutePath());
		bundle.putString("eventListPath", eventListPath.getAbsolutePath());
		bundle.putBoolean("disableNewEvents", disableNewEvents);
	}

	@Override
	@MainThread
	@Nullable public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			module = new ModuleModel();
			module.init(
					savedInstanceState.getString("module"),
					new File(savedInstanceState.getString("projectRootDirectory")));
			disableNewEvents = savedInstanceState.getBoolean("disableNewEvents");
			fileModelDirectory = new File(savedInstanceState.getString("fileModelDirectory"));
			eventListPath = new File(savedInstanceState.getString("eventListPath"));
		}

		binding = FragmentEventListBinding.inflate(inflator);
		switchSection(LOADING_SECTION);

		if (disableNewEvents)
			binding.fab.setVisibility(View.GONE);

		Executors.newSingleThreadExecutor()
				.execute(
						() -> {
							ArrayList<Object> events = EventUtils.getEvents(eventListPath);
							getActivity()
									.runOnUiThread(
											() -> {
												if (events.isEmpty()) {
													showInfo(R.string.no_events_yet);
													return;
												}
												binding.list.setAdapter(
														new EventAdapter(
																events, getActivity(), module, fileModelDirectory,
																eventListPath));
												binding.list.setLayoutManager(new LinearLayoutManager(getActivity()));
												switchSection(LIST_SECTION);
											});
						});

		return binding.getRoot();
	}

	private void switchSection(int section) {
		binding.loadingSection.setVisibility(LOADING_SECTION == section ? View.VISIBLE : View.GONE);
		binding.listSection.setVisibility(LIST_SECTION == section ? View.VISIBLE : View.GONE);
		binding.infoSection.setVisibility(INFO_SECTION == section ? View.VISIBLE : View.GONE);
	}

	private void showInfo(int info) {
		switchSection(INFO_SECTION);
		binding.info.setText(info);
	}
}
