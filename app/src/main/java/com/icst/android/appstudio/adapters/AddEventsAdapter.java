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

import java.util.ArrayList;

import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.databinding.AdapterEventAddBinding;

import android.code.editor.common.utils.ColorUtils;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class AddEventsAdapter extends RecyclerView.Adapter<AddEventsAdapter.ViewHolder> {

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View view) {
			super(view);
		}
	}

	private ArrayList<Event> events;
	public boolean[] selectedCheckboxes;

	public AddEventsAdapter(ArrayList<Event> events) {
		this.events = events;
		selectedCheckboxes = new boolean[events.size()];
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = AdapterEventAddBinding.inflate(LayoutInflater.from(arg0.getContext())).getRoot();
		RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(layoutParams);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		if (getEvents().get(position) instanceof Event) {
			Event event = (Event) getEvents().get(position);
			AdapterEventAddBinding binding = AdapterEventAddBinding.bind(holder.itemView);
			binding.title.setText(event.getTitle());
			binding.description.setText(event.getDescription());
			if (event.getIcon() != null) {
				Drawable icon = new BitmapDrawable(
						binding.getRoot().getContext().getResources(),
						BitmapFactory.decodeByteArray(event.getIcon(), 0, event.getIcon().length));

				if (event.getApplyColorFilter()) {
					icon.setTint(
							ColorUtils.getColor(
									binding.getRoot().getContext(),
									com.google.android.material.R.attr.colorOnSurfaceVariant));
				}
				binding.icon.setImageDrawable(icon);
			}
			binding.cardView.setOnClickListener(
					v -> {
						binding.addCheckbox.setChecked(!binding.addCheckbox.isChecked());
					});
			binding.addCheckbox.setOnCheckedChangeListener(
					(button, isChecked) -> {
						selectedCheckboxes[position] = isChecked;
					});
		}
	}

	@Override
	public int getItemCount() {
		return events.size();
	}

	public ArrayList<Event> getEvents() {
		return this.events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	public ArrayList<Event> getSelectedEvents() {
		ArrayList<Event> selectedEvents = new ArrayList<Event>();

		for (int i = 0; i < events.size(); ++i) {
			if (selectedCheckboxes[i]) {
				selectedEvents.add(events.get(i));
			}
		}

		return selectedEvents;
	}
}
