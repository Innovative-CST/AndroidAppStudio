/*
 * This file is part of Android AppStudio [https://github.com/Innovative-CST/AndroidAppStudio].
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

package com.icst.logic.lib.view;

import java.util.ArrayList;

import com.icst.android.appstudio.beans.ActionBlockBean;
import com.icst.android.appstudio.beans.EventBlockBean;
import com.icst.android.appstudio.beans.TerminatorBlockBean;
import com.icst.logic.bean.ActionBlockDropZone;
import com.icst.logic.block.view.ActionBlockBeanView;
import com.icst.logic.block.view.EventBlockBeanView;
import com.icst.logic.core.BlockMarginConstants;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.exception.EventDefinationBlockNotFound;
import com.icst.logic.exception.TerminatedDropZoneException;
import com.icst.logic.exception.UnexpectedTerminatedException;
import com.icst.logic.exception.UnexpectedViewAddedException;
import com.icst.logic.lib.config.LogicEditorConfiguration;
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
			super.addView(view, index);
		} else
			throw new UnexpectedViewAddedException(this, view);
	}

	public int getIndex(float x, float y) {
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

	public void highlightNearestTarget(ArrayList<ActionBlockBean> blocks, float x, float y) {
		if (canDrop(blocks, x, y)) {
			getLogicEditor().removeDummyHighlighter();
			LayoutParams highlighterLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			highlighterLp.setMargins(0, BlockMarginConstants.CHAINED_ACTION_BLOCK_TOP_MARGIN, 0, 0);
			NearestTargetHighlighterView highlighter = new NearestTargetHighlighterView(getContext(), blocks.get(0));
			getLogicEditor().setDummyHighlighter(highlighter);
			int index = getIndex(x, y);
			if (index == 0) {
				index = 1;
			}
			addView(highlighter, index);

			highlighter.setLayoutParams(highlighterLp);
		}
	}

	public void highlightNearestTarget(ActionBlockBean block, float x, float y) {
		if (canDrop(block, x, y)) {
			getLogicEditor().removeDummyHighlighter();
			LayoutParams highlighterLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			highlighterLp.setMargins(0, BlockMarginConstants.CHAINED_ACTION_BLOCK_TOP_MARGIN, 0, 0);
			NearestTargetHighlighterView highlighter = new NearestTargetHighlighterView(getContext(), block);
			getLogicEditor().setDummyHighlighter(highlighter);
			int index = getIndex(x, y);
			if (index == 0) {
				index = 1;
			}
			addView(highlighter, index);
			highlighter.setLayoutParams(highlighterLp);
		}
	}

	public boolean canDrop(ArrayList<ActionBlockBean> blocks, float x, float y) {
		return canDrop(blocks, getIndex(x, y));
	}

	public boolean canDrop(ActionBlockBean block, float x, float y) {
		ArrayList<ActionBlockBean> blockArray = new ArrayList<>();
		blockArray.add(block);
		return canDrop(blockArray, getIndex(x, y));
	}

	public boolean canDrop(ArrayList<ActionBlockBean> actionBlocks, int index) {
		if (index > blockBeans.size()) {
			if (blockBeans.size() == index) {
				if (isTerminated())
					return false;
				else
					return true;

			} else {
				if (actionBlocks.size() == 0)
					return true;
				return !(actionBlocks.get(actionBlocks.size() - 1) instanceof TerminatorBlockBean);
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
					i + (eventDefinationBlockView.getParent() == null ? 0 : 1));

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			lp.setMargins(
					0,
					UnitUtils.dpToPx(
							getContext(), BlockMarginConstants.CHAINED_ACTION_BLOCK_TOP_MARGIN),
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
