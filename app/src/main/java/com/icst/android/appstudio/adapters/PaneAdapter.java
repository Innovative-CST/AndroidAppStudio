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

import com.icst.android.appstudio.activities.CodeEditorActivity;
import com.icst.android.appstudio.databinding.ViewHolderPaneBinding;
import com.icst.android.appstudio.interfaces.WorkSpacePane;
import com.icst.android.appstudio.view.CodeEditorPaneView;
import com.icst.android.appstudio.view.TerminalPaneView;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/*
 * Adapter for Recycler view in CodeEditorActivity to list TerminalPane and CodeEditorPane.
 */

public class PaneAdapter extends RecyclerView.Adapter<PaneAdapter.ViewHolder> {
	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
		}
	}

	private ArrayList<WorkSpacePane> panes;
	private CodeEditorActivity editorActivity;

	public PaneAdapter(ArrayList<WorkSpacePane> panes, CodeEditorActivity editorActivity) {
		this.panes = panes;
		this.editorActivity = editorActivity;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		ViewHolderPaneBinding binding = ViewHolderPaneBinding.inflate(editorActivity.getLayoutInflater());
		RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		binding.getRoot().setLayoutParams(layoutParams);
		return new ViewHolder(binding.getRoot());
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		ViewHolderPaneBinding binding = ViewHolderPaneBinding.bind(holder.itemView);

		binding.name.setText(panes.get(position).getWorkSpacePaneName());
		binding.icon.setImageDrawable(panes.get(position).getWorkSpacePaneIcon());

		binding
				.getRoot()
				.setOnClickListener(
						v -> {
							if (panes.get(position) instanceof CodeEditorPaneView editorPane) {
								editorActivity.switchSection(CodeEditorActivity.WORKSPACE);
								editorActivity.binding.workspaceContainer.removeAllViews();
								editorActivity.binding.workspaceContainer.addView(editorPane);
							} else if (panes.get(position) instanceof TerminalPaneView terminalPane) {
								editorActivity.switchSection(CodeEditorActivity.WORKSPACE);
								editorActivity.binding.workspaceContainer.removeAllViews();
								editorActivity.binding.workspaceContainer.addView(terminalPane);
							}
						});
	}

	@Override
	public int getItemCount() {
		return panes.size();
	}
}
