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

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.icst.android.appstudio.beans.BlockBean;
import com.icst.android.appstudio.beans.EventBean;
import com.icst.logic.bean.ActionBlockDropZone;
import com.icst.logic.editor.HistoryManager;
import com.icst.logic.editor.databinding.LayoutLogicEditorBinding;
import com.icst.logic.editor.event.LogicEditorEventDispatcher;
import com.icst.logic.editor.event.LogicEditorEventListener;
import com.icst.logic.lib.config.LogicEditorConfiguration;
import com.icst.logic.lib.view.BlockDropZoneView;
import com.icst.logic.listener.DraggableTouchListener;
import java.util.ArrayList;

/* Main LogicEditor View */
public class LogicEditorView extends RelativeLayout {

	private EventBean event;
	private LayoutLogicEditorBinding binding;
	private ArrayList<BlockDropZoneView> blockDropZones;
	private HistoryManager historyManager;
	private LogicEditorEventDispatcher eventDispatcher;
	private DraggableTouchListener mDraggableTouchListener;
	private View draggingView;
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

	public void setDraggingView(View draggingView, float x, float y) {
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

	public void moveDraggingView(float x, float y) {
		draggingView.setX(x);
		draggingView.setY(y);
		draggingView.requestLayout();
	}

	public void dropDraggingView(float x, float y) {
		if (draggingView.getParent() != null) {
			if (draggingView.getParent() instanceof ViewGroup parent) {
				parent.removeView(draggingView);
			}
		}
	}

	public void openEventInCanva(EventBean event, LogicEditorConfiguration configuration) {
		this.event = event;
		this.historyManager = new HistoryManager(this);
		binding.logicEditorCanvaView.openEventInCanva(event, configuration, this);
	}

	public void dragBlockBean(BlockBean blockBean, float x, float y) {
		// Deliver drag events to LogicEditorEventDispatcher and update HistoryManager
		eventDispatcher.onBlockDragged(blockBean);
		// TODO: Drag block logic...
	}

	public void dragBlockBeans(ArrayList<BlockBean> blockBeans, float x, float y) {
	}

	public void dragActionBlockDropZone(
			ActionBlockDropZone actionBlockDropZone,
			ActionBlockDropZone draggedFrom,
			int indexOfDrag) {
		// Deliver drag events to LogicEditorEventDispatcher and update HistoryManager
		eventDispatcher.onActionBlockDropZoneDragged(actionBlockDropZone, draggedFrom, indexOfDrag);
		// TODO: Action block drop zone block logic...
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
}
