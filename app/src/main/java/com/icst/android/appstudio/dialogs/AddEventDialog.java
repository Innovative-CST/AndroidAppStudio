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

package com.icst.android.appstudio.dialogs;

import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.adapters.AddEventsAdapter;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.model.JavaFileModel;
import com.icst.android.appstudio.databinding.DialogAddEventBinding;
import com.icst.android.appstudio.fragments.events.JavaEventManagerFragment;
import com.icst.android.appstudio.utils.EventUtils;
import java.io.File;
import java.util.ArrayList;

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
    ArrayList<Event> events =
        EventUtils.filterEvents(
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
