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
import com.icst.android.appstudio.activities.EventEditorActivity;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.model.JavaFileModel;
import com.icst.android.appstudio.databinding.AdapterEventBinding;
import com.icst.android.appstudio.databinding.FragmentJavaEventManagerBinding;
import com.icst.android.appstudio.dialogs.AddEventDialog;
import com.icst.android.appstudio.models.EventHolder;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.EventUtils;
import com.icst.android.appstudio.utils.EventsHolderUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;

import android.app.Activity;
import android.code.editor.common.utils.ColorUtils;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class JavaEventManagerFragment extends Fragment {
	private FragmentJavaEventManagerBinding binding;
	private ModuleModel module;
	private String packageName;
	private String className;
	private Activity activity;
	private JavaFileModel file;
	private File eventsHolderDir;
	private boolean disableNewEvents;

	private static final int LOADING_SECTION = 0;
	private static final int LIST_SECTION = 1;
	private static final int INFO_SECTION = 2;

	public JavaEventManagerFragment() {
	}

	public JavaEventManagerFragment(
			ModuleModel module, String packageName, String className, boolean disableNewEvents) {
		this.module = module;
		this.packageName = packageName;
		this.className = className;
		this.disableNewEvents = disableNewEvents;
		eventsHolderDir = new File(
				new File(
						EnvironmentUtils.getJavaDirectory(module, packageName), className.concat(".java")),
				EnvironmentUtils.EVENTS_DIR);
		file = DeserializerUtils.deserialize(
				new File(
						new File(
								EnvironmentUtils.getJavaDirectory(module, packageName),
								className.concat(".java")),
						EnvironmentUtils.JAVA_FILE_MODEL),
				JavaFileModel.class);
	}

	@Override
	@MainThread
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putString("module", module.module);
		bundle.putString("projectRootDirectory", module.projectRootDirectory.getAbsolutePath());
		bundle.putBoolean("disableNewEvents", disableNewEvents);
		bundle.putString("className", className);
		bundle.putString("packageName", packageName);
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
			className = savedInstanceState.getString("className");
			packageName = savedInstanceState.getString("packageName");
			activity = getActivity();
			eventsHolderDir = new File(
					new File(
							EnvironmentUtils.getJavaDirectory(module, packageName),
							className.concat(".java")),
					EnvironmentUtils.EVENTS_DIR);
			file = DeserializerUtils.deserialize(
					new File(
							new File(
									EnvironmentUtils.getJavaDirectory(module, packageName),
									className.concat(".java")),
							EnvironmentUtils.JAVA_FILE_MODEL),
					JavaFileModel.class);
		}

		binding = FragmentJavaEventManagerBinding.inflate(inflator);

		if (file != null) {
			switchSection(LOADING_SECTION);
			loadEvents();
			binding.fab.setOnClickListener(
					v -> {
						AddEventDialog dialog = new AddEventDialog(JavaEventManagerFragment.this, file,
								eventsHolderDir);
					});
		} else {
			switchSection(INFO_SECTION);
			binding.info.setText("Failed to load java file");
		}

		return binding.getRoot();
	}

	public void loadEvents() {
		Executors.newSingleThreadExecutor()
				.execute(
						() -> {
							ArrayList<EventHolder> eventHolderList = EventsHolderUtils.getEventHolder(eventsHolderDir);
							getActivity()
									.runOnUiThread(
											() -> {
												// Loading the events
												loadEventData(eventHolderList);
											});
						});
	}

	/*
	 * Loads the NavigationRail View and Events.
	 */
	private void loadEventData(ArrayList<EventHolder> eventHolderList) {
		binding.navigationRail.getMenu().clear();
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
			item.setIcon(getEventHolderIcon(getActivity(), eventHolderList.get(position)));
			if (eventHolderList.get(position).isBuiltInEvents()) {
				ArrayList<Object> events = EventUtils.getEvents(
						new File(
								new File(eventsHolderDir, eventHolderList.get(position).getHolderName()),
								EnvironmentUtils.EVENTS_DIR));
				if (events.isEmpty()) {
					showInfo(R.string.no_events_yet);
				} else {
					binding.list.setAdapter(
							new EventListAdapter(
									events,
									getActivity(),
									module,
									packageName,
									className,
									new File(
											new File(eventsHolderDir, eventHolderList.get(position).getHolderName()),
											EnvironmentUtils.EVENTS_DIR)));
					binding.list.setLayoutManager(new LinearLayoutManager(getActivity()));
					switchSection(LIST_SECTION);
				}
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
					ArrayList<Object> events = EventUtils.getEvents(
							new File(
									new File(eventsHolderDir, eventHolderList.get(position).getHolderName()),
									EnvironmentUtils.EVENTS_DIR));
					if (events.isEmpty()) {
						showInfo(R.string.no_events_yet);
					} else {
						binding.list.setAdapter(
								new EventListAdapter(
										events,
										getActivity(),
										module,
										packageName,
										className,
										new File(
												new File(eventsHolderDir,
														eventHolderList.get(position).getHolderName()),
												EnvironmentUtils.EVENTS_DIR)));
						binding.list.setLayoutManager(new LinearLayoutManager(getActivity()));
						switchSection(LIST_SECTION);
					}
					return true;
				});

		if (eventHolderList.size() == 0) {
			showInfo(R.string.no_events_yet);
		}
	}

	public static Drawable getEventHolderIcon(Context context, EventHolder holder) {
		if (holder.getIcon() != null) {
			return new BitmapDrawable(
					context.getResources(),
					BitmapFactory.decodeByteArray(holder.getIcon(), 0, holder.getIcon().length));
		}
		return null;
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

	public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

		private ArrayList<Object> events;
		private Activity activity;
		private ModuleModel module;
		private String packageName;
		private String className;
		private File eventListPath;

		public EventListAdapter(
				ArrayList<Object> events,
				Activity activity,
				ModuleModel module,
				String packageName,
				String className,
				File eventListPath) {
			this.events = events;
			this.activity = activity;
			this.module = module;
			this.packageName = packageName;
			this.className = className;
			this.eventListPath = eventListPath;
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View view) {
				super(view);
			}
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup arg0, int viewtype) {
			View view = AdapterEventBinding.inflate(LayoutInflater.from(arg0.getContext())).getRoot();
			RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(layoutParams);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			if (events.get(position) instanceof Event) {
				Event event = (Event) events.get(position);
				AdapterEventBinding binding = AdapterEventBinding.bind(holder.itemView);
				binding.title.setText(event.getTitle());
				binding.description.setText(event.getDescription());
				if (event.getIcon() != null) {
					Drawable icon = new BitmapDrawable(
							binding.getRoot().getContext().getResources(),
							BitmapFactory.decodeByteArray(event.getIcon(), 0, event.getIcon().length));

					if (event.getApplyColorFilter()) {
						icon.setTint(
								ColorUtils.getColor(
										getActivity(), com.google.android.material.R.attr.colorOnSurfaceVariant));
					}
					binding.icon.setImageDrawable(icon);
				}
				binding.cardView.setOnClickListener(
						v -> {
							File eventFile = new File(eventListPath, event.getName());
							Intent editor = new Intent(holder.itemView.getContext(), EventEditorActivity.class);
							editor.putExtra("module", module);
							editor.putExtra(
									"fileModelDirectory",
									new File(
											new File(
													new File(
															EnvironmentUtils.getJavaDirectory(module, packageName),
															className)
															.getAbsolutePath()
															.concat(".java")),
											EnvironmentUtils.JAVA_FILE_MODEL)
											.getAbsolutePath());
							editor.putExtra("eventListPath", eventListPath.getAbsolutePath());
							editor.putExtra("eventFile", eventFile.getAbsolutePath());
							activity.startActivity(editor);
						});
			}
		}

		@Override
		public int getItemCount() {
			return events.size();
		}
	}
}
