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

package com.icst.android.appstudio.adapters;

import android.app.Activity;
import android.code.editor.common.utils.ColorUtils;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.icst.android.appstudio.activities.EventEditorActivity;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.databinding.AdapterEventBinding;
import com.icst.android.appstudio.models.ModuleModel;
import java.io.File;
import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
  public class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View view) {
      super(view);
    }
  }

  private ArrayList<Object> events;
  private Activity activity;
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

  public EventAdapter(
      ArrayList<Object> events,
      Activity activity,
      ModuleModel module,
      File fileModelDirectory,
      File eventListPath) {
    this.events = events;
    this.activity = activity;
    this.module = module;
    this.fileModelDirectory = fileModelDirectory;
    this.eventListPath = eventListPath;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
    View view = AdapterEventBinding.inflate(LayoutInflater.from(arg0.getContext())).getRoot();
    RecyclerView.LayoutParams layoutParams =
        new RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    view.setLayoutParams(layoutParams);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    if (getEvents().get(position) instanceof Event) {
      Event event = (Event) getEvents().get(position);
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
                  binding.getRoot().getContext(), com.google.android.material.R.attr.colorPrimary));
          icon.setTintMode(PorterDuff.Mode.MULTIPLY);
        }
        binding.icon.setImageDrawable(icon);
      }

      binding.cardView.setOnClickListener(
          v -> {
            File eventFile = new File(eventListPath, event.getName());
            Intent editor = new Intent(holder.itemView.getContext(), EventEditorActivity.class);
            editor.putExtra("module", module);
            editor.putExtra("fileModelDirectory", fileModelDirectory.getAbsolutePath());
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

  public ArrayList<Object> getEvents() {
    return this.events;
  }

  public void setEvents(ArrayList<Object> events) {
    this.events = events;
  }
}
