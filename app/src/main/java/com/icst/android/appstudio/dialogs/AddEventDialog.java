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

package com.icst.android.appstudio.dialogs;

import java.io.File;
import java.util.ArrayList;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.adapters.AddEventsAdapter;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.model.JavaFileModel;
import com.icst.android.appstudio.databinding.DialogAddEventBinding;
import com.icst.android.appstudio.fragments.events.JavaEventManagerFragment;
import com.icst.android.appstudio.utils.EventUtils;

import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

public class AddEventDialog extends MaterialAlertDialogBuilder {
	private static final int INFO_SECTION = 0;
	private static final int ADD_EVENT_SECTION = 1;
	private AddEventsAdapter adapter;
	private DialogAddEventBinding binding;
	private AlertDialog dialog;

	public AddEventDialog(JavaEventManagerFragment fragment, JavaFileModel file, File holdersDir) {
		super(fragment.getActivity());
		binding = DialogAddEventBinding.inflate(fragment.getActivity().getLayoutInflater());
		ArrayList<String> superClasses = new ArrayList<String>();
		ArrayList<String> superClassesImports = new ArrayList<String>();
		if (file.getExtendingClass() != null) {
			superClasses.add(file.getExtendingClass());
		}
		if (file.getImplementingInterface() != null) {
			for (String interfaceClass : file.getImplementingInterface()) {
				superClasses.add(interfaceClass);
			}
		}

		if (file.getExtendingClassImport() != null) {
			superClassesImports.add(file.getExtendingClassImport());
		}
		if (file.getImplementingInterfaceImports() != null) {
			for (String interfaceClassImport : file.getImplementingInterfaceImports()) {
				superClassesImports.add(interfaceClassImport);
			}
		}
		ArrayList<Event> events = EventUtils.filterEvents(
				new ArrayList<Event>(),
				EventUtils.getAllEventsFromHolders(holdersDir),
				superClasses,
				superClassesImports,
				file);

		setView(binding.getRoot());

		if (events.size() == 0) {
			switchSection(INFO_SECTION);
			binding.add.setVisibility(View.GONE);
			binding.cancel.setOnClickListener(
					v -> {
						dialog.dismiss();
					});
		} else {
			switchSection(ADD_EVENT_SECTION);
			adapter = new AddEventsAdapter(events);
			binding.list.setAdapter(adapter);
			binding.list.setLayoutManager(new LinearLayoutManager(fragment.getContext()));
			binding.add.setOnClickListener(
					v -> {
						EventUtils.installEvents(adapter.getSelectedEvents(), holdersDir, true);
						fragment.loadEvents();
						dialog.dismiss();
					});
			binding.cancel.setOnClickListener(
					v -> {
						dialog.dismiss();
					});
		}
		dialog = show();
	}

	public void switchSection(int section) {
		binding.addEventSection.setVisibility(section == ADD_EVENT_SECTION ? View.VISIBLE : View.GONE);
		binding.info.setVisibility(section == INFO_SECTION ? View.VISIBLE : View.GONE);
	}
}
