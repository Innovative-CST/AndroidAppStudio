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

package com.icst.logic.editor.view;

import java.util.ArrayList;

import com.icst.android.appstudio.beans.ActionBlockBean;
import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.BlockPaletteBean;
import com.icst.android.appstudio.beans.EventBean;
import com.icst.android.appstudio.beans.ExpressionBlockBean;
import com.icst.logic.adapter.BlockPaletteAdapter;
import com.icst.logic.block.view.ActionBlockBeanView;
import com.icst.logic.block.view.BlockBeanView;
import com.icst.logic.block.view.BooleanBlockView;
import com.icst.logic.block.view.ExpressionBlockBeanView;
import com.icst.logic.block.view.NumericBlockBeanView;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.HistoryManager;
import com.icst.logic.editor.databinding.LayoutLogicEditorBinding;
import com.icst.logic.editor.event.LogicEditorEventDispatcher;
import com.icst.logic.editor.event.LogicEditorEventListener;
import com.icst.logic.listener.DraggableTouchListener;
import com.icst.logic.utils.CanvaMathUtils;
import com.icst.logic.view.ActionBlockDropZoneView;
import com.icst.logic.view.BlockDropZoneView;
import com.icst.logic.view.BooleanBlockElementBeanView;
import com.icst.logic.view.DraggingBlockDummy;
import com.icst.logic.view.ExpressionBlockDropZoneView;
import com.icst.logic.view.GeneralBlockElementView;
import com.icst.logic.view.MainActionBlockDropZoneView;
import com.icst.logic.view.NearestTargetHighlighterView;
import com.icst.logic.view.NumericBlockElementBeanView;
import com.icst.logic.view.StringBlockBeanView;
import com.icst.logic.view.StringBlockElementBeanView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

/* Main LogicEditor View */
public class LogicEditorView extends RelativeLayout {

	private EventBean event;
	private LayoutLogicEditorBinding binding;
	private ArrayList<BlockDropZoneView> blockDropZones;
	private HistoryManager historyManager;
	private LogicEditorEventDispatcher eventDispatcher;
	private DraggableTouchListener mDraggableTouchListener;
	private DraggingBlockDummy draggingView;
	private NearestTargetHighlighterView highlighter;
	private Object draggingBean;
	private boolean isBlockPallateVisible = false;

	public LogicEditorView(final Context context, final AttributeSet set) {
		super(context, set);
		binding = LayoutLogicEditorBinding.inflate(LayoutInflater.from(context));
		blockDropZones = new ArrayList<BlockDropZoneView>();
		eventDispatcher = new LogicEditorEventDispatcher();
		mDraggableTouchListener = new DraggableTouchListener(this);
		LogicEditorView.LayoutParams lp = new LogicEditorView.LayoutParams(
				LogicEditorView.LayoutParams.MATCH_PARENT,
				LogicEditorView.LayoutParams.MATCH_PARENT);
		binding.getRoot().setLayoutParams(lp);

		binding.fab.setOnClickListener(
				v -> {
					isBlockPallateVisible = !isBlockPallateVisible;
					showBlocksPallete(isBlockPallateVisible);
				});

		addView(binding.getRoot());
	}

	public void setDraggingView(DraggingBlockDummy draggingView, float x, float y) {
		if (draggingView == null)
			return;
		this.draggingView = draggingView;
		if (draggingView.getParent() != null) {
			if (draggingView.getParent() instanceof ViewGroup parent) {
				parent.removeView(draggingView);
			}
		}

		addView(draggingView);
		draggingView.setX(x);
		draggingView.setY(y);
	}

	public void preparePallete(ArrayList<BlockPaletteBean> mBlockPaletteBean) {
		binding.blocksHolderList.setLayoutManager(new LinearLayoutManager(getContext()));
		binding.blocksHolderList.setAdapter(
				new BlockPaletteAdapter(
						mBlockPaletteBean,
						binding.blockList,
						getLogicEditorCanva().getLogicEditorConfiguration(),
						this));
	}

	public void startDrag(Object draggingBean, DraggingBlockDummy draggingView, float x, float y) {
		this.draggingBean = draggingBean;
		setDraggingView(draggingView, x, y);
		lockBlockListScroll(true);
	}

	public void moveDraggingView(float x, float y) {
		draggingView.setX(x);
		draggingView.setY(y);
		draggingView.setAllowedDropIcon(canDropDraggingView(x, y));
		draggingView.requestLayout();
		removeDummyHighlighter();
		if (canDropDraggingView(x, y)) {
			highlightNearestTargetIfAllowed(x, y);
		}
	}

	public void highlightNearestTargetIfAllowed(float x, float y) {
		boolean hasNearbyTarget = false;
		for (int i = blockDropZones.size() - 1; i >= 0; --i) {
			if (!CanvaMathUtils.isCoordinatesInsideTargetView(
					blockDropZones.get(i), binding.editorSection, x, y)) {
				continue;
			}

			if (blockDropZones.get(i) instanceof MainActionBlockDropZoneView dropZone) {

				if (draggingBean instanceof ArrayList blockArr) {
					ArrayList<ActionBlockBean> blocks = (ArrayList<ActionBlockBean>) blockArr;
					if (dropZone.canDrop(blocks, x, y)) {
						hasNearbyTarget = true;
						dropZone.highlightNearestTargetIfAllowed(blocks, x, y);
					}
				} else if (draggingBean instanceof BlockBean block) {
					if (dropZone.canDrop(block, x, y)) {
						hasNearbyTarget = true;
						dropZone.highlightNearestTargetIfAllowed(block, x, y);
					}
				}

			} else if (blockDropZones.get(i) instanceof ActionBlockDropZoneView actionBlockDropZoneView) {

				if (draggingBean instanceof ArrayList blockArr) {
					ArrayList<ActionBlockBean> blocks = (ArrayList<ActionBlockBean>) blockArr;
					if (actionBlockDropZoneView.canDrop(blocks, x, y)) {
						hasNearbyTarget = true;
						actionBlockDropZoneView.highlightNearestTargetIfAllowed(blocks, x, y);
					}
				} else if (draggingBean instanceof BlockBean block) {
					if (actionBlockDropZoneView.canDrop(block, x, y)) {
						hasNearbyTarget = true;
						actionBlockDropZoneView.highlightNearestTargetIfAllowed(block, x, y);
					}
				}

			} else if (blockDropZones.get(i) instanceof ExpressionBlockDropZoneView expressionBlockDropZoneView) {
				if (draggingBean instanceof ExpressionBlockBean expressionBlockBean) {
					if (expressionBlockDropZoneView.canDrop(expressionBlockBean, x, y)) {
						hasNearbyTarget = true;
						expressionBlockDropZoneView.highlightNearestTargetIfAllowed(
								expressionBlockBean, x, y);
					}
				}
			} else
				continue;
		}

		if (!hasNearbyTarget) {
			removeDummyHighlighter();
		}
	}

	public void dropBlock(float x, float y) {
		boolean hasNearbyTarget = false;
		for (int i = blockDropZones.size() - 1; i >= 0; --i) {
			if (!CanvaMathUtils.isCoordinatesInsideTargetView(
					blockDropZones.get(i), binding.editorSection, x, y)) {
				continue;
			}

			if (blockDropZones.get(i) instanceof MainActionBlockDropZoneView dropZone) {

				if (draggingBean instanceof ArrayList blockArr) {
					ArrayList<ActionBlockBean> blocks = (ArrayList<ActionBlockBean>) blockArr;
					if (dropZone.canDrop(blocks, x, y)) {
						hasNearbyTarget = true;
						dropZone.dropBlockIfAllowed(blocks, x, y);
					}
				} else if (draggingBean instanceof BlockBean block) {
					if (dropZone.canDrop(block, x, y)) {
						hasNearbyTarget = true;
						dropZone.dropBlockIfAllowed(block, x, y);
					}
				}

			} else if (blockDropZones.get(i) instanceof ActionBlockDropZoneView actionBlockDropZoneView) {

				if (draggingBean instanceof ArrayList blockArr) {
					ArrayList<ActionBlockBean> blocks = (ArrayList<ActionBlockBean>) blockArr;
					if (actionBlockDropZoneView.canDrop(blocks, x, y)) {
						hasNearbyTarget = true;
						actionBlockDropZoneView.dropBlockIfAllowed(blocks, x, y);
					}
				} else if (draggingBean instanceof BlockBean block) {
					if (actionBlockDropZoneView.canDrop(block, x, y)) {
						hasNearbyTarget = true;
						actionBlockDropZoneView.dropBlockIfAllowed(block, x, y);
					}
				}

			} else if (blockDropZones.get(i) instanceof ExpressionBlockDropZoneView expressionBlockDropZoneView) {
				if (draggingBean instanceof ExpressionBlockBean expressionBlockBean) {
					if (expressionBlockDropZoneView.canDrop(expressionBlockBean, x, y)) {
						hasNearbyTarget = true;
						expressionBlockDropZoneView.dropBlockIfAllowed(expressionBlockBean, x, y);
					}
				}
			} else
				continue;
		}

		if (!hasNearbyTarget) {

			if (draggingBean instanceof ArrayList blockArr) {
				ArrayList<ActionBlockBean> blocks = (ArrayList<ActionBlockBean>) blockArr;

				ActionBlockDropZoneView newZone = new ActionBlockDropZoneView(
						getContext(), blocks, new LogicEditorConfiguration(), this);

				LogicEditorCanvaView.LayoutParams lp = new LogicEditorCanvaView.LayoutParams(
						LogicEditorCanvaView.LayoutParams.WRAP_CONTENT,
						LogicEditorCanvaView.LayoutParams.WRAP_CONTENT);

				newZone.setX(x + binding.logicEditorCanvaView.getScrollX());
				newZone.setY(y + binding.logicEditorCanvaView.getScrollY());
				getLogicEditorCanva().addView(newZone);
				blockDropZones.add(newZone);
				newZone.setLayoutParams(lp);

			} else if (draggingBean instanceof ActionBlockBean block) {

				ArrayList<ActionBlockBean> blocks = new ArrayList<ActionBlockBean>();
				blocks.add(block);

				ActionBlockDropZoneView newZone = new ActionBlockDropZoneView(
						getContext(), blocks, new LogicEditorConfiguration(), this);

				LogicEditorCanvaView.LayoutParams lp = new LogicEditorCanvaView.LayoutParams(
						LogicEditorCanvaView.LayoutParams.WRAP_CONTENT,
						LogicEditorCanvaView.LayoutParams.WRAP_CONTENT);

				newZone.setX(x + binding.logicEditorCanvaView.getScrollX());
				newZone.setY(y + binding.logicEditorCanvaView.getScrollY());
				getLogicEditorCanva().addView(newZone);
				blockDropZones.add(newZone);
				newZone.setLayoutParams(lp);
			} else if (draggingBean instanceof ExpressionBlockBean expressionBlockBean) {
				ExpressionBlockDropZoneView newZone = new ExpressionBlockDropZoneView(
						getContext(), new LogicEditorConfiguration(), this);

				LogicEditorCanvaView.LayoutParams lp = new LogicEditorCanvaView.LayoutParams(
						LogicEditorCanvaView.LayoutParams.WRAP_CONTENT,
						LogicEditorCanvaView.LayoutParams.WRAP_CONTENT);

				newZone.setExpressionBlockBean(expressionBlockBean);
				newZone.setX(x + binding.logicEditorCanvaView.getScrollX());
				newZone.setY(y + binding.logicEditorCanvaView.getScrollY());
				getLogicEditorCanva().addView(newZone);
				blockDropZones.add(newZone);
				newZone.setLayoutParams(lp);
			}
		}

		if (mDraggableTouchListener.getTouchingView() instanceof BlockBeanView draggingBlockView) {
			if (draggingBlockView.isInsideCanva()) {
				removeOldReferences();
			}
		}
	}

	public void removeOldReferences() {
		View touchingView = mDraggableTouchListener.getTouchingView();

		if (touchingView instanceof ActionBlockBeanView actionBlockBeanView) {
			ViewParent parent = actionBlockBeanView.getParent();
			if (parent == null)
				return;

			if (parent instanceof MainActionBlockDropZoneView mainChain) {
				int index = mainChain.indexOfChild(actionBlockBeanView);
				mainChain.getChildAt(index).setOnTouchListener(null);
				mainChain.removeViews(index, mainChain.getChildCount() - index);
				mainChain.dereferenceActionBlocks(index - 1);
			} else if (parent instanceof ActionBlockDropZoneView regularChain) {
				int index = regularChain.indexOfChild(actionBlockBeanView);
				regularChain.getChildAt(index).setOnTouchListener(null);
				regularChain.removeViews(index, regularChain.getChildCount() - index);
				regularChain.dereferenceActionBlocks(index);
			}
		} else if (touchingView instanceof ExpressionBlockBeanView expressionBlockBean) {
			ViewParent parent = expressionBlockBean.getParent();
			if (parent == null)
				return;

			if (parent instanceof ExpressionBlockDropZoneView expressionBlockDropZone) {
				expressionBlockDropZone.getExpressionBlockBeanView().setOnTouchListener(null);
				blockDropZones.remove(expressionBlockDropZone);
				getLogicEditorCanva().removeView(expressionBlockDropZone);
			} else if (parent instanceof StringBlockElementBeanView stringBlockElementBeanView) {
				expressionBlockBean.setOnTouchListener(null);
				stringBlockElementBeanView.setValue("");
			} else if (parent.getParent() != null) {

				if (parent.getParent() instanceof BooleanBlockElementBeanView booleanBlockElementBeanView) {
					expressionBlockBean.setOnTouchListener(null);
					booleanBlockElementBeanView.setValue(false);
				} else if (parent.getParent() instanceof NumericBlockElementBeanView numericBlockElementBeanView) {
					expressionBlockBean.setOnTouchListener(null);
					numericBlockElementBeanView.setValue("");
				} else if (parent.getParent() instanceof GeneralBlockElementView generalBlockElementView) {
					expressionBlockBean.setOnTouchListener(null);
					generalBlockElementView.setValue("");
				}

			}
		}
	}

	public void dropDraggingView(float x, float y) {
		if (draggingView.getParent() != null) {
			if (draggingView.getParent() instanceof ViewGroup parent) {
				parent.removeView(draggingView);
			}
		}
		removeDummyHighlighter();
		if (canDropDraggingView(x, y)) {
			dropBlock(x, y);
		} else {
			if (draggingView.isDraggedFromCanva()) {
				resetFromDragging();
			}
		}
		lockBlockListScroll(false);
	}

	public void resetFromDragging() {
		View touchingView = mDraggableTouchListener.getTouchingView();

		if (touchingView instanceof ActionBlockBeanView actionBlockBeanView) {

			if (actionBlockBeanView.getParent() == null)
				return;

			int index = 0;
			if (actionBlockBeanView.getParent() instanceof MainActionBlockDropZoneView mainChain) {
				index = mainChain.indexOfChild(actionBlockBeanView);
				for (int i = index; i < mainChain.getBlocksSize(); ++i) {
					mainChain.getChildAt(i).setVisibility(View.VISIBLE);
				}
			} else if (actionBlockBeanView.getParent() instanceof ActionBlockDropZoneView regularChain) {
				index = regularChain.indexOfChild(actionBlockBeanView);
				for (int i = index; i < regularChain.getChildCount(); ++i) {
					regularChain.getChildAt(i).setVisibility(View.VISIBLE);
				}
			}
		} else if (touchingView instanceof StringBlockBeanView stringBlockBeanView) {
			stringBlockBeanView.setVisibility(VISIBLE);
		} else if (touchingView instanceof BooleanBlockView booleanBlockView) {
			booleanBlockView.setVisibility(VISIBLE);
		} else if (touchingView instanceof NumericBlockBeanView numericBlockBeanView) {
			numericBlockBeanView.setVisibility(VISIBLE);
		}
	}

	public boolean canDropDraggingView(float x, float y) {
		return CanvaMathUtils.isCoordinatesInsideTargetView(binding.editorSection, this, x, y);
	}

	public void openEventInCanva(EventBean event, LogicEditorConfiguration configuration) {
		this.event = event;
		this.historyManager = new HistoryManager(this);
		binding.logicEditorCanvaView.openEventInCanva(event, configuration, this);
	}

	//	public void dragActionBlockDropZone(
	//			ActionBlockDropZone actionBlockDropZone,
	//			ActionBlockDropZone draggedFrom,
	//			int indexOfDrag) {
	//		// Deliver drag events to LogicEditorEventDispatcher and update HistoryManager
	//		eventDispatcher.onActionBlockDropZoneDragged(actionBlockDropZone, draggedFrom, indexOfDrag);
	//		// TODO: Action block drop zone block logic...
	//	}

	public void setDummyHighlighter(NearestTargetHighlighterView highlighter) {
		this.highlighter = highlighter;
	}

	public NearestTargetHighlighterView getDummyHighlighter() {
		return highlighter;
	}

	public void removeDummyHighlighter() {
		if (highlighter != null) {
			if (highlighter.getParent() instanceof ViewGroup parent) {
				parent.removeView(highlighter);
			}
			highlighter = null;
		}
	}

	public void lockBlockListScroll(boolean shouldLock) {
		binding.blockList.requestDisallowInterceptTouchEvent(shouldLock);
	}

	public void addLogicEditorEventListener(LogicEditorEventListener eventListener) {
		eventDispatcher.getEventListener().add(eventListener);
	}

	public void removeLogicEditorEventListener(LogicEditorEventListener listener) {
		eventDispatcher.getEventListener().remove(listener);
	}

	public void removeAllLogicEditorEventListener() {
		eventDispatcher.getEventListener().clear();
	}

	public void showBlocksPallete(boolean show) {
		binding.blockArea.setVisibility(show ? VISIBLE : GONE);
	}

	public LogicEditorCanvaView getLogicEditorCanva() {
		return binding.logicEditorCanvaView;
	}

	public ArrayList<BlockDropZoneView> getBlockDropZones() {
		return this.blockDropZones;
	}

	public void setBlockDropZones(ArrayList<BlockDropZoneView> blockDropZones) {
		this.blockDropZones = blockDropZones;
	}

	public DraggableTouchListener getDraggableTouchListener() {
		return this.mDraggableTouchListener;
	}

	public DraggingBlockDummy getDraggingView() {
		return draggingView;
	}

	public LinearLayout getEditorSectionView() {
		return binding.editorSection;
	}

	public EventBean getPreparedEventBean() {
		event.setActionBlockBeans(binding.logicEditorCanvaView.mainChainDropZone.getBlockBeans());
		return event;
	}

	public Object getDraggingBean() {
		return draggingBean;
	}
}
