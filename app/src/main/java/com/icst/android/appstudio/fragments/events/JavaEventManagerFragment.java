/*
 * This file is part of Android AppStudio [https://github.com/TS-Code-Editor/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.icst.android.appstudio.fragments.events;

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
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;

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

  public JavaEventManagerFragment(
      ModuleModel module, String packageName, String className, boolean disableNewEvents) {
    this.module = module;
    this.packageName = packageName;
    this.className = className;
    this.disableNewEvents = disableNewEvents;
    eventsHolderDir =
        new File(
            new File(
                EnvironmentUtils.getJavaDirectory(module, packageName), className.concat(".java")),
            EnvironmentUtils.EVENTS_DIR);
    file =
        DeserializerUtils.deserialize(
            new File(
                new File(
                    EnvironmentUtils.getJavaDirectory(module, packageName),
                    className.concat(".java")),
                EnvironmentUtils.JAVA_FILE_MODEL),
            JavaFileModel.class);
  }

  @Override
  @MainThread
  @Nullable
  public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle bundle) {
    binding = FragmentJavaEventManagerBinding.inflate(inflator);

    if (file != null) {
      switchSection(LOADING_SECTION);
      loadEvents();
      binding.fab.setOnClickListener(
          v -> {
            AddEventDialog dialog =
                new AddEventDialog(JavaEventManagerFragment.this, file, eventsHolderDir);
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
              ArrayList<EventHolder> eventHolderList =
                  EventsHolderUtils.getEventHolder(eventsHolderDir);
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
     * Set the current fragment if EventList of a Fragment is attached to a FileModel.
     */
    for (int position = 0; position < eventHolderList.size(); ++position) {
      Menu menu = binding.navigationRail.getMenu();
      MenuItem item =
          menu.add(Menu.NONE, position, Menu.NONE, eventHolderList.get(position).getHolderName());
      item.setIcon(getEventHolderIcon(getActivity(), eventHolderList.get(position)));
      if (eventHolderList.get(position).isBuiltInEvents()) {
        ArrayList<Object> events =
            EventUtils.getEvents(
                new File(
                    new File(eventsHolderDir, eventHolderList.get(position).getHolderName()),
                    EnvironmentUtils.EVENTS_DIR));
        if (events.size() == 0) {
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
          ArrayList<Object> events =
              EventUtils.getEvents(
                  new File(
                      new File(eventsHolderDir, eventHolderList.get(position).getHolderName()),
                      EnvironmentUtils.EVENTS_DIR));
          if (events.size() == 0) {
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
      RecyclerView.LayoutParams layoutParams =
          new RecyclerView.LayoutParams(
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
          Drawable icon =
              new BitmapDrawable(
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
