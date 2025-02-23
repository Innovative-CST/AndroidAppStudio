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

package com.icst.android.appstudio.adapters;

import java.io.File;
import java.util.ArrayList;

import com.icst.android.appstudio.activities.EventEditorActivity;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.databinding.AdapterEventBinding;
import com.icst.android.appstudio.models.ModuleModel;

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
		RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
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
				Drawable icon = new BitmapDrawable(
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
