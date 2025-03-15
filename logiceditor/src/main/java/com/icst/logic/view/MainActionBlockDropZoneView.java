/*
 *  This file is part of Block IDLE.
 *
 *  Block IDLE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Block IDLE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Block IDLE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.logic.view;

import java.util.ArrayList;

import com.icst.blockidle.beans.ActionBlockBean;
import com.icst.blockidle.beans.BlockBean;
import com.icst.blockidle.beans.EventBlockBean;
import com.icst.blockidle.beans.TerminatorBlockBean;
import com.icst.logic.bean.ActionBlockDropZone;
import com.icst.logic.block.view.ActionBlockBeanView;
import com.icst.logic.block.view.EventBlockBeanView;
import com.icst.logic.config.BlockMarginConstants;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.exception.EventDefinationBlockNotFound;
import com.icst.logic.exception.TerminatedDropZoneException;
import com.icst.logic.exception.UnexpectedTerminatedException;
import com.icst.logic.exception.UnexpectedViewAddedException;
import com.icst.logic.utils.ActionBlockUtils;
import com.icst.logic.utils.CanvaMathUtils;
import com.icst.logic.utils.UnitUtils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class MainActionBlockDropZoneView extends BlockDropZoneView {
	private EventBlockBean eventDefination;
	private EventBlockBeanView eventDefinationBlockView;
	private Context context;
	private ArrayList<ActionBlockBean> blockBeans;
	private ActionBlockDropZone actionBlockDropZone;

	public MainActionBlockDropZoneView(
			Context context,
			EventBlockBean eventDefination,
			LogicEditorConfiguration logicEditorConfiguration,
			LogicEditorView logicEditor) {
		super(context, logicEditorConfiguration, logicEditor);
		this.context = context;
		this.eventDefination = eventDefination;

		setOrientation(VERTICAL);

		if (eventDefination == null) {
			throw new EventDefinationBlockNotFound();
		}

		eventDefinationBlockView = new EventBlockBeanView(
				context, eventDefination, getConfiguration(), getLogicEditor());

		addView(eventDefinationBlockView);
		LinearLayout.LayoutParams eventDefBlockLp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		eventDefinationBlockView.setLayoutParams(eventDefBlockLp);

		blockBeans = new ArrayList<ActionBlockBean>();
		actionBlockDropZone = new ActionBlockDropZone() {

			@Override
			public boolean isTerminated() {
				if (blockBeans == null)
					return false;
				if (blockBeans.size() == 0)
					return false;
				return blockBeans.get(blockBeans.size() - 1) instanceof TerminatorBlockBean;
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

		if (view instanceof ActionBlockBeanView) {
			super.addView(view);
		} else if (getChildCount() == 0) {
			super.addView(view);
		} else if (view instanceof NearestTargetHighlighterView) {
			super.addView(view);
		} else
			throw new UnexpectedViewAddedException(this, view);
	}

	// Always throw this error to make sure no unexpected view is added.
	@Override
	public void addView(View view, int index) {
		if (view instanceof EventBlockBeanView eventDefBlockView) {
			super.addView(view, index);
		} else if (view instanceof ActionBlockBeanView actionBlockView) {
			super.addView(actionBlockView, index);
		} else if (view instanceof NearestTargetHighlighterView) {
			super.addView(view, index + (eventDefinationBlockView.getParent() == null ? 0 : 1));
		} else
			throw new UnexpectedViewAddedException(this, view);
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

	@Override
	public void highlightNearestTargetIfAllowed(BlockBean block, float x, float y) {
		if (block instanceof ActionBlockBean actionBlockBean) {
			ArrayList<ActionBlockBean> blocks = new ArrayList<ActionBlockBean>();
			blocks.add(actionBlockBean);
			highlightNearestTargetIfAllowed(blocks, x, y);
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
		if (block instanceof ActionBlockBean actionBlockBean) {
			ArrayList<ActionBlockBean> blocks = new ArrayList<ActionBlockBean>();
			blocks.add(actionBlockBean);
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
		if (index != 0) {
			index -= 1;
		}
		if (canDrop(blocks, index)) {
			getLogicEditor().removeDummyHighlighter();
			LayoutParams highlighterLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			highlighterLp.setMargins(0, BlockMarginConstants.ACTION_BLOCK_TOP_MARGIN, 0, 0);
			NearestTargetHighlighterView highlighter = new NearestTargetHighlighterView(getContext(), blocks.get(0));
			getLogicEditor().setDummyHighlighter(highlighter);
			addView(highlighter, index);
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
		if (index != 0) {
			index -= 1;
		}
		if (canDrop(blocks, index)) {
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, BlockMarginConstants.ACTION_BLOCK_TOP_MARGIN, 0, 0);
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
		index = getIndex(x, y);
		if (index != 0) {
			index -= 1;
		}
		return canDrop(blocks, index);
	}

	@Override
	public boolean canDrop(BlockBean block, float x, float y) {
		if (block instanceof ActionBlockBean actionBlockBean) {
			ArrayList<ActionBlockBean> blockArray = new ArrayList<>();
			blockArray.add(actionBlockBean);
			return canDrop(blockArray, x, y);
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

	public boolean canDrop(ArrayList<ActionBlockBean> actionBlocks, int index) {
		if (index >= blockBeans.size()) {
			if (blockBeans.size() == index) {
				if (isTerminated())
					return false;
				else
					return true;

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

	public void addActionBlocksBeans(ArrayList<ActionBlockBean> actionBlocks, int index) {
		if (blockBeans == null) {
			blockBeans = new ArrayList<ActionBlockBean>();
		}
		if (index >= blockBeans.size()) {
			if (blockBeans.size() == index) {
				if (isTerminated())
					throw new TerminatedDropZoneException();
				else
					addBlockBeans(actionBlocks, index);
			} else {
				if (isTerminated())
					throw new TerminatedDropZoneException();
				else
					addBlockBeans(actionBlocks, blockBeans.size());
			}
		} else {
			if (actionBlocks.size() == 0)
				return;
			if (!(actionBlocks.get(actionBlocks.size() - 1) instanceof TerminatorBlockBean))
				addBlockBeans(actionBlocks, index);
			else
				throw new UnexpectedTerminatedException();
		}
	}

	private void addBlockBeans(ArrayList<ActionBlockBean> actionBlocks, int index) {
		this.blockBeans.addAll(index, actionBlocks);

		for (int i = 0; i < actionBlocks.size(); ++i) {
			ActionBlockBean actionBlock = actionBlocks.get(i);
			ActionBlockBeanView actionBlockBeanView = ActionBlockUtils.getBlockView(
					context, actionBlock, getConfiguration(), getLogicEditor());
			actionBlockBeanView.setInsideCanva(true);

			if (actionBlockBeanView == null)
				continue;

			addView(
					actionBlockBeanView,
					index + i + (eventDefinationBlockView.getParent() == null ? 0 : 1));

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			lp.setMargins(
					0,
					UnitUtils.dpToPx(getContext(), BlockMarginConstants.ACTION_BLOCK_TOP_MARGIN),
					0,
					0);
			actionBlockBeanView.setLayoutParams(lp);
		}
	}

	// public ArrayList<ActionBlockBean> breakFromIndex(int index) {}

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
