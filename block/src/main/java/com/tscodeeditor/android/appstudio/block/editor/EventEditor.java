/*
 *  This file is part of Android AppStudio.
 *
 *  Android AppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Android AppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Android AppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.tscodeeditor.android.appstudio.block.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.tscodeeditor.android.appstudio.block.databinding.EventEditorLayoutBinding;

public class EventEditor extends LinearLayout {

  private EventEditorLayoutBinding binding;

  // Contants for showing the section easily
  public static final int LOADING_SECTION = 0;
  public static final int INFO_SECTION = 1;
  public static final int EDITOR_SECTION = 2;
  public static final int VALUE_EDITOR_SECTION = 3;

  public EventEditor(final Context context, final AttributeSet set) {
    super(context, set);

    binding =
        EventEditorLayoutBinding.inflate(
            LayoutInflater.from(context));

    addView(binding.getRoot());

    invalidate();
  }

  /*
   * Method for switching the section quickly.
   * All other section will be GONE except the section of which the section code is provided
   */
  private void switchSection(int section) {
    binding.loading.setVisibility(section == LOADING_SECTION ? View.VISIBLE : View.GONE);
    binding.info.setVisibility(section == INFO_SECTION ? View.VISIBLE : View.GONE);
    binding.editorSection.setVisibility(section == EDITOR_SECTION ? View.VISIBLE : View.GONE);
    binding.valueEditorSection.setVisibility(
        section == VALUE_EDITOR_SECTION ? View.VISIBLE : View.GONE);
  }

  private void showBlocksPallete(boolean show) {
    binding.blockArea.setVisibility(show ? View.VISIBLE : View.GONE);
  }
}
