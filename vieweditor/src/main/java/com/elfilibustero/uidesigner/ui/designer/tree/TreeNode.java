package com.elfilibustero.uidesigner.ui.designer.tree;

import android.view.View;
import androidx.annotation.DrawableRes;
import com.elfilibustero.uidesigner.lib.handler.ViewPaletteHandler;
import com.elfilibustero.uidesigner.lib.tool.DynamicViewFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeNode {
	private View view;
	private String name;
	private boolean isViewGroup;
	private List<TreeNode> children;
	private boolean expanded;
	private int icon;

	public TreeNode(View view) {
		this.view = view;
		this.name = DynamicViewFactory.getSimpleName(view);
		this.children = new ArrayList<>();
		this.expanded = false;
		this.isViewGroup = false;
		icon = ViewPaletteHandler.getIcon(name);
	}

	public View getView() {
		return view;
	}

	public String getName() {
		return name;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void addChild(TreeNode child) {
		children.add(child);
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isViewGroup() {
		return isViewGroup;
	}

	public void setViewGroup(boolean isViewGroup) {
		this.isViewGroup = isViewGroup;
	}

	@DrawableRes
	public int getIcon() {
		return icon;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TreeNode treeNode = (TreeNode) o;
		return Objects.equals(view, treeNode.view) && Objects.equals(children, treeNode.children);
	}

	@Override
	public int hashCode() {
		return Objects.hash(view, children);
	}
}
