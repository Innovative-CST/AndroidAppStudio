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

package com.icst.android.appstudio.activities;

import java.io.File;
import java.util.ArrayList;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.adapters.PaneAdapter;
import com.icst.android.appstudio.databinding.ActivityCodeEditorBinding;
import com.icst.android.appstudio.interfaces.WorkSpacePane;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.view.CodeEditorPaneView;
import com.icst.android.appstudio.view.TerminalPaneView;
import com.icst.android.appstudio.viewholder.FileTreeViewHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.unnamed.b.atv.view.TreeNodeWrapperView;

import android.code.editor.common.utils.ColorUtils;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

public class CodeEditorActivity extends BaseActivity {
	public ActivityCodeEditorBinding binding;
	private File directory;
	private TreeNode root;
	private ArrayList<WorkSpacePane> panes;
	private PaneAdapter paneAdapter;

	public static final int NO_WORKSPACE = 0;
	public static final int WORKSPACE = 1;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		binding = ActivityCodeEditorBinding.inflate(getLayoutInflater());
		panes = new ArrayList<WorkSpacePane>();
		paneAdapter = new PaneAdapter(panes, this);
		setContentView(binding.getRoot());

		ImageView shellIcon = new ImageView(this);
		Drawable shellIconDrawable = ContextCompat.getDrawable(this, R.drawable.language_shell);
		shellIconDrawable.setTint(
				ColorUtils.getColor(this, com.google.android.material.R.attr.colorOnSurface));
		shellIcon.setImageDrawable(shellIconDrawable);
		shellIcon.setBackgroundDrawable(
				ContextCompat.getDrawable(this, R.drawable.ripple_on_color_surface));
		shellIcon.setOnClickListener(
				v -> {
					switchSection(WORKSPACE);
					TerminalPaneView terminalPane = new TerminalPaneView(
							CodeEditorActivity.this, directory, EnvironmentUtils.LOGIN_SHELL) {
						@Override
						public void onRelease() {
							panes.remove(this);
							paneAdapter.notifyDataSetChanged();
							switchSection(NO_WORKSPACE);
						}
					};
					LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
					panes.add(terminalPane);
					terminalPane.setLayoutParams(layoutParam);
					paneAdapter.notifyDataSetChanged();
					binding.workspaceContainer.removeAllViews();
					binding.workspaceContainer.addView(terminalPane);
				});
		binding.navigationRail.addHeaderView(shellIcon);

		binding.toolbar.setTitle("Android AppStudio");
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
		binding.list.setAdapter(paneAdapter);
		binding.list.setLayoutManager(new LinearLayoutManager(this));

		/*
		 * Info:
		 * CodeEditorActivity may be requested to open a files or a folder.
		 *
		 * How it handles folder?
		 *
		 * If folder is opened then the folder will path fill be assigned to `directory`
		 * variable.
		 * File tree of folder will loaded in drawer.
		 *
		 * How it handles file?
		 *
		 * If a file is opened then it must be in folder. So open the file in workspace
		 * (pane).
		 * And assign directory variable to parent folder(e.g. folder in which file is
		 * stored) of opened file.
		 */
		String path = getIntent().getStringExtra("path");
		if (new File(path).exists()) {
			if (new File(path).isFile()) {
				directory = new File(path).getParentFile();
				openInEditorPane(new File(path));
			} else {
				directory = new File(path);
				switchSection(NO_WORKSPACE);
			}
		} else {
			Toast.makeText(this, "Could not open directory.", Toast.LENGTH_SHORT);
			finish();
		}

		/*
		 * Add Menu to navigationRail as following:
		 * 1. File Tree
		 * Displays the file tree of directory opened in editor.
		 *
		 * 2. Curretly opened panes
		 * Displays the unfinished editor pane or terminal pane.
		 */
		Menu menu = binding.navigationRail.getMenu();
		MenuItem fileTree = menu.add(Menu.NONE, 0, Menu.NONE, "");
		fileTree.setIcon(R.drawable.ic_folder);

		MenuItem workspace = menu.add(Menu.NONE, 1, Menu.NONE, "");
		workspace.setIcon(R.drawable.ic_edit);

		binding.navigationRail.setOnItemSelectedListener(
				(menuItem) -> {
					int id = menuItem.getItemId();
					if (id == 0) {
						binding.fileTreeSection.setVisibility(View.VISIBLE);
						binding.workSpacesListSection.setVisibility(View.GONE);
					} else if (id == 1) {
						binding.fileTreeSection.setVisibility(View.GONE);
						binding.workSpacesListSection.setVisibility(View.VISIBLE);
					}
					return true;
				});

		// Load file tree of directory
		loadFileTree();
	}

	public void loadFileTree() {
		// Assign root for file tree
		root = TreeNode.root();

		// Add root folder to start tree structure
		TreeNode child = new TreeNode(directory);
		child.setViewHolder(new FileTreeViewHolder(this));
		root.addChild(child);
		/*
		 * Add Tree view
		 * Attach it with root(TreeNode)
		 */
		AndroidTreeView tView = new AndroidTreeView(this, root);
		tView.setDefaultAnimation(true);
		TreeNodeWrapperView treeView = new TreeNodeWrapperView(this, com.unnamed.b.atv.R.style.TreeNodeStyle);
		treeView.getNodeContainer().addView(tView.getView());

		// Add File Tree Widget to drawer
		binding.fileTreeContainer.addView(treeView);
	}

	/*
	 * Method for switching the section quickly.
	 * All other section will be GONE except the section of which the section code
	 * is provided
	 */
	public void switchSection(int section) {
		binding.emptyWorkspace.setVisibility(section == NO_WORKSPACE ? View.VISIBLE : View.GONE);
		binding.workspace.setVisibility(section == WORKSPACE ? View.VISIBLE : View.GONE);
	}

	public void openInEditorPane(File file) {
		CodeEditorPaneView pane = null;

		switchSection(WORKSPACE);
		binding.progressbar.setVisibility(View.VISIBLE);

		for (int i = 0; i < panes.size(); ++i) {
			if (panes.get(i) instanceof CodeEditorPaneView editorPane) {
				if (editorPane.getEditorFile().getAbsolutePath().equals(file.getAbsolutePath())) {
					pane = editorPane;
				}
			}
		}

		if (pane == null) {
			pane = new CodeEditorPaneView(this, file) {

				@Override
				public void onRelease() {
					panes.remove(this);
					paneAdapter.notifyDataSetChanged();
					switchSection(NO_WORKSPACE);
				}
			};
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			panes.add(pane);
			pane.setLayoutParams(layoutParam);
			paneAdapter.notifyDataSetChanged();
		}

		binding.workspaceContainer.removeAllViews();
		binding.workspaceContainer.addView(pane);
		binding.progressbar.setVisibility(View.GONE);
	}
}
