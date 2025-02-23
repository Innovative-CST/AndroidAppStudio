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

package com.icst.logic.view;

import java.util.ArrayList;
import java.util.Collections;

import com.icst.android.appstudio.beans.ActionBlockBean;
import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.TerminatorBlockBean;
import com.icst.logic.bean.ActionBlockDropZone;
import com.icst.logic.block.view.ActionBlockBeanView;
import com.icst.logic.config.BlockMarginConstants;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.exception.TerminatedDropZoneException;
import com.icst.logic.exception.UnexpectedTerminatedException;
import com.icst.logic.exception.UnexpectedViewAddedException;
import com.icst.logic.utils.ActionBlockUtils;
import com.icst.logic.utils.CanvaMathUtils;
import com.icst.logic.utils.UnitUtils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class ActionBlockDropZoneView extends BlockDropZoneView {
	private Context context;
	private ArrayList<ActionBlockBean> blockBeans;
	private ActionBlockDropZone actionBlockDropZone;

	public ActionBlockDropZoneView(
			Context context,
			ArrayList<ActionBlockBean> blockBeans,
			LogicEditorConfiguration logicEditorConfiguration,
			LogicEditorView logicEditor) {
		super(context, logicEditorConfiguration, logicEditor);
		this.context = context;
		this.blockBeans = blockBeans == null ? new ArrayList<ActionBlockBean>() : blockBeans;
		setOrientation(VERTICAL);
		addBlockView();
		actionBlockDropZone = new ActionBlockDropZone() {

			@Override
			public boolean isTerminated() {
				if (ActionBlockDropZoneView.this.blockBeans == null)
					return false;
				if (ActionBlockDropZoneView.this.blockBeans.size() == 0)
					return false;
				return ActionBlockDropZoneView.this.blockBeans
						.get(ActionBlockDropZoneView.this.blockBeans.size() - 1) instanceof TerminatorBlockBean;
			}
		};
	}

	public void dereferenceActionBlocks(int index) {
		int numberOfBlocks = blockBeans.size();
		for (int i = index; i < numberOfBlocks; ++i) {
			blockBeans.remove(index);
		}
	}

	// Always throw this error to make sure no unexpected view is added.
	@Override
	public void addView(View view) {
		if (view instanceof LinearLayout) {
			super.addView(view);
		} else if (view instanceof ActionBlockBeanView) {
			super.addView(view);
		} else {
			throw new UnexpectedViewAddedException(this, view);
		}
	}

	// Always throw this error to make sure no unexpected view is added.
	@Override
	public void addView(View view, int index) {
		if (view instanceof LinearLayout) {
			super.addView(view, index);
		} else if (view instanceof ActionBlockBeanView) {
			super.addView(view, index);
		} else {
			throw new UnexpectedViewAddedException(this, view);
		}
	}

	@Override
	public void highlightNearestTargetIfAllowed(BlockBean block, float x, float y) {
		if (block instanceof ActionBlockBean mActionBlockBean) {
			ArrayList<ActionBlockBean> actionBlocks = new ArrayList<ActionBlockBean>();
			actionBlocks.add(mActionBlockBean);
			highlightNearestTargetIfAllowed(actionBlocks, x, y);
		} else {
			int index = 0;
			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				if (CanvaMathUtils.isCoordinatesInsideTargetView(
						child, getLogicEditor().getEditorSectionView(), x, y)) {
					index = i;
					break;
				}
			}
			if (getChildAt(index) instanceof ActionBlockBeanView blockBeanView) {
				blockBeanView.highlightNearestTarget(block, x, y);
			}
		}
	}

	@Override
	public void dropBlockIfAllowed(BlockBean block, float x, float y) {
		if (block instanceof ActionBlockBean mActionBlockBean) {
			ArrayList<ActionBlockBean> blocks = new ArrayList<ActionBlockBean>();
			blocks.add(mActionBlockBean);
			dropBlockIfAllowed(blocks, x, y);
		} else {
			int index = 0;
			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				if (CanvaMathUtils.isCoordinatesInsideTargetView(
						child, getLogicEditor().getEditorSectionView(), x, y)) {
					index = i;
					break;
				}
			}
			if (getChildAt(index) instanceof ActionBlockBeanView blockBeanView) {
				blockBeanView.drop(block, x, y);
			}
		}
	}

	@Override
	public boolean canDrop(BlockBean block, float x, float y) {
		if (block instanceof ActionBlockBean mActionBlockBean) {
			ArrayList<ActionBlockBean> actionBlocks = new ArrayList<ActionBlockBean>();
			actionBlocks.add(mActionBlockBean);
			return canDrop(actionBlocks, x, y);
		} else {
			int index = 0;
			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				if (CanvaMathUtils.isCoordinatesInsideTargetView(
						child, getLogicEditor().getEditorSectionView(), x, y)) {
					index = i;
					break;
				}
			}
			if (getChildAt(index) instanceof ActionBlockBeanView blockBeanView) {
				return blockBeanView.canDrop(block, x, y);
			}
		}
		return false;
	}

	public void highlightNearestTargetIfAllowed(
			ArrayList<ActionBlockBean> blocks, float x, float y) {
		int index = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (CanvaMathUtils.isCoordinatesInsideTargetView(
					child, getLogicEditor().getEditorSectionView(), x, y)) {
				index = i;
				break;
			}
		}
		if (getChildAt(index) instanceof ActionBlockBeanView blockBeanView) {
			if (blockBeanView.canDrop(blocks, x, y)) {
				blockBeanView.highlightNearestTarget(blocks, x, y);
				return;
			}
		}

		index = getIndex(x, y);
		if (canDrop(blocks, index)) {
			getLogicEditor().removeDummyHighlighter();
			LayoutParams highlighterLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			highlighterLp.setMargins(0, BlockMarginConstants.ACTION_BLOCK_TOP_MARGIN, 0, 0);
			NearestTargetHighlighterView highlighter = new NearestTargetHighlighterView(getContext(), blocks.get(0));
			getLogicEditor().setDummyHighlighter(highlighter);
			super.addView(highlighter, index);

			highlighter.setLayoutParams(highlighterLp);
		}
	}

	public void dropBlockIfAllowed(ArrayList<ActionBlockBean> blocks, float x, float y) {
		int index = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (CanvaMathUtils.isCoordinatesInsideTargetView(
					child, getLogicEditor().getEditorSectionView(), x, y)) {
				index = i;
				break;
			}
		}
		if (getChildAt(index) instanceof ActionBlockBeanView blockBeanView) {
			if (blockBeanView.canDrop(blocks, x, y)) {
				blockBeanView.drop(blocks, x, y);
				return;
			}
		}
		index = getIndex(x, y);
		if (canDrop(blocks, index)) {
			addBlockBeans(blocks, index);
		}
	}

	public boolean canDrop(ArrayList<ActionBlockBean> blocks, float x, float y) {
		int index = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (CanvaMathUtils.isCoordinatesInsideTargetView(
					child, getLogicEditor().getEditorSectionView(), x, y)) {
				index = i;
				break;
			}
		}
		if (getChildAt(index) instanceof ActionBlockBeanView blockBeanView) {
			if (blockBeanView.canDrop(blocks, x, y)) {
				return true;
			}
		}
		return canDrop(blocks, getIndex(x, y));
	}

	public void addActionBlocksBeans(ArrayList<ActionBlockBean> actionBlocks, int index) {
		if (blockBeans == null) {
			blockBeans = new ArrayList<ActionBlockBean>();
		}
		if (actionBlocks == null) {
			return;
		}
		if (index <= blockBeans.size()) {
			if (blockBeans.size() == index) {
				if (isTerminated())
					throw new TerminatedDropZoneException();
				else
					addBlockBeans(actionBlocks, index);
			} else {
				if (actionBlocks.size() == 0)
					return;
				if (!(actionBlocks.get(actionBlocks.size() - 1) instanceof TerminatorBlockBean))
					addBlockBeans(actionBlocks, index);
				else
					throw new UnexpectedTerminatedException();
			}
		} else
			throw new IndexOutOfBoundsException(index);
	}

	private int getIndex(float x, float y) {
		int[] relativeCoordinates = CanvaMathUtils.getRelativeCoordinates(this, getLogicEditor());

		int index = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (y - ((int) relativeCoordinates[1]) > child.getY() + (child.getHeight() / 2)) {
				index = i + 1;
			} else {
				break;
			}
		}

		return index;
	}

	private boolean canDrop(ActionBlockBean actionBlock, int index) {
		ArrayList<ActionBlockBean> singleBlockList = new ArrayList<>(Collections.singletonList(actionBlock));

		return canDrop(singleBlockList, index);
	}

	private boolean canDrop(ArrayList<ActionBlockBean> actionBlocks, int index) {
		if (index >= blockBeans.size()) {
			if (blockBeans.size() == index) {
				if (isTerminated())
					return false;
				else {
					return true;
				}
			} else {
				if (actionBlocks.size() == 0)
					return true;
				return !isTerminated();
			}
		} else {
			if (actionBlocks.size() == 0) {
				return true;
			}

			return (!(actionBlocks.get(actionBlocks.size() - 1) instanceof TerminatorBlockBean));
		}
	}

	private void addBlockView() {
		for (int i = 0; i < blockBeans.size(); ++i) {
			ActionBlockBean actionBlock = blockBeans.get(i);
			ActionBlockBeanView actionBlockBeanView = ActionBlockUtils.getBlockView(
					context, actionBlock, getConfiguration(), getLogicEditor());

			if (actionBlockBeanView == null)
				continue;

			actionBlockBeanView.setInsideCanva(true);
			super.addView(actionBlockBeanView, i);

			if (i == 0) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);

				lp.setMargins(0, 0, 0, 0);
				actionBlockBeanView.setLayoutParams(lp);
			} else {

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);

				lp.setMargins(
						0,
						UnitUtils.dpToPx(
								getContext(), BlockMarginConstants.ACTION_BLOCK_TOP_MARGIN),
						0,
						0);
				actionBlockBeanView.setLayoutParams(lp);
			}
		}
	}

	private void addBlockBeans(ArrayList<ActionBlockBean> actionBlocks, int index) {
		this.blockBeans.addAll(index, actionBlocks);

		for (int i = 0; i < actionBlocks.size(); ++i) {
			ActionBlockBean actionBlock = actionBlocks.get(i);
			ActionBlockBeanView actionBlockBeanView = ActionBlockUtils.getBlockView(
					context, actionBlock, getConfiguration(), getLogicEditor());

			if (actionBlockBeanView == null)
				continue;

			actionBlockBeanView.setInsideCanva(true);
			super.addView(actionBlockBeanView, i + index);

			if (i == 0 && index == 0) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);

				lp.setMargins(0, 0, 0, 0);
				actionBlockBeanView.setLayoutParams(lp);
			} else {

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);

				lp.setMargins(
						0,
						UnitUtils.dpToPx(
								getContext(), BlockMarginConstants.ACTION_BLOCK_TOP_MARGIN),
						0,
						0);
				actionBlockBeanView.setLayoutParams(lp);
			}
		}
	}

	public int getBlocksSize() {
		return blockBeans.size();
	}

	public boolean isTerminated() {
		return actionBlockDropZone.isTerminated();
	}

	public ActionBlockDropZone getActionBlockDropZone() {
		return this.actionBlockDropZone;
	}

	public ArrayList<ActionBlockBean> getBlockBeans() {
		return this.blockBeans;
	}
}
