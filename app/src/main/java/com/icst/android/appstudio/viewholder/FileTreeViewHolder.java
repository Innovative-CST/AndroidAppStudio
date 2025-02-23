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

package com.icst.android.appstudio.viewholder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Executors;

import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.CodeEditorActivity;
import com.icst.android.appstudio.databinding.ViewHolderFileTreeBinding;
import com.icst.android.appstudio.utils.FileIconUtils;
import com.unnamed.b.atv.model.TreeNode;

import android.code.editor.common.utils.FileUtils;
import android.transition.ChangeImageTransform;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;

public class FileTreeViewHolder extends TreeNode.BaseNodeViewHolder<File> {
	private CodeEditorActivity activity;
	private ViewHolderFileTreeBinding binding;
	private TreeNode node;

	public FileTreeViewHolder(CodeEditorActivity activity) {
		super(activity);
		this.activity = activity;
	}

	@Override
	public View createNodeView(TreeNode node, File file) {
		this.node = node;
		binding = ViewHolderFileTreeBinding.inflate(activity.getLayoutInflater());
		binding.expandCollapse.setVisibility(file.isFile() ? View.GONE : View.VISIBLE);
		binding.path.setText(FileUtils.getLatSegmentOfFilePath(file.getAbsolutePath()));

		applyPadding(node, 16);

		if (file.isDirectory()) {
			binding.icon.setImageResource(R.drawable.ic_folder);
			updateExpandCollapseIcon(node.isExpanded());
		} else {
			binding.icon.setImageDrawable(FileIconUtils.getFileIcon(file, activity));
		}

		binding
				.getRoot()
				.setOnClickListener(
						v -> {
							if (file.isDirectory()) {
								if (node.isExpanded()) {
									getTreeView().collapseNode(node);
									updateExpandCollapseIcon(node.isExpanded());
								} else {
									Executors.newSingleThreadExecutor()
											.execute(
													() -> {
														activity.runOnUiThread(
																() -> {
																	binding.viewFlipper.setDisplayedChild(1);
																});

														listDirInNode(node, file);

														activity.runOnUiThread(
																() -> {
																	binding.viewFlipper.setDisplayedChild(0);
																	getTreeView().expandNode(node);
																	updateExpandCollapseIcon(node.isExpanded());
																});
													});
								}
							} else {
								activity.openInEditorPane(file);
							}
						});

		return binding.getRoot();
	}

	/*
	 * Loads the child nodes in paramter @node.
	 * Child will be the list of files in folder @file.
	 */
	public void listDirInNode(TreeNode node, File file) {
		node.children.clear();
		ArrayList<File> list = new ArrayList<File>();
		for (File dir : file.listFiles()) {
			list.add(dir);
		}
		Collections.sort(list, new FileComparator());
		for (int pos = 0; pos < list.size(); pos++) {
			TreeNode child = new TreeNode(list.get(pos));
			child.setViewHolder(new FileTreeViewHolder(activity));
			node.addChild(child);
		}
	}

	/*
	 * Changes the image of node corresponding icon according to state of node.
	 */
	public void updateExpandCollapseIcon(boolean isExpanded) {
		TransitionManager.beginDelayedTransition(binding.getRoot(), new ChangeImageTransform());
		binding.expandCollapse.setImageResource(
				isExpanded ? R.drawable.chevron_down : R.drawable.chevron_right);
	}

	/*
	 * Applies the padding according to node level.
	 * To give it tree like view.
	 */
	public LinearLayout applyPadding(final TreeNode node, final int padding) {
		binding
				.getRoot()
				.setPaddingRelative(
						binding.getRoot().getPaddingLeft() + (padding * (node.getLevel() - 1)),
						binding.getRoot().getPaddingTop(),
						binding.getRoot().getPaddingRight(),
						binding.getRoot().getPaddingBottom());
		return binding.getRoot();
	}

	/*
	 * A FileComparator class which just put the files in order of abcd....
	 * like other file manager with folders first and files at end.
	 */
	final class FileComparator implements Comparator<File> {
		public int compare(File f1, File f2) {
			if (f1 == f2)
				return 0;
			if (f1.isDirectory() && f2.isFile())
				return -1;
			if (f1.isFile() && f2.isDirectory())
				return 1;
			return f1.getAbsolutePath().compareToIgnoreCase(f2.getAbsolutePath());
		}
	}
}
