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

package com.icst.logic.listener;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import com.icst.android.appstudio.beans.ActionBlockBean;
import com.icst.logic.block.view.ActionBlockBeanView;
import com.icst.logic.block.view.RegularBlockBeanView;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.lib.view.ActionBlockDropZoneView;
import com.icst.logic.lib.view.DraggingBlockDummy;
import com.icst.logic.lib.view.MainActionBlockDropZoneView;
import com.icst.logic.utils.CanvaMathUtils;
import com.icst.logic.utils.UnitUtils;
import java.util.ArrayList;

public class DraggableTouchListener implements View.OnTouchListener {

	private LogicEditorView logicEditor;
	private View touchingView;
	private boolean isDragging;
	private Runnable dragStartRunnable;
	private Handler dragHandler;
	private float x, y;
	private float initialRelativeCoordinateX, initialRelativeCoordinateY;

	public DraggableTouchListener(LogicEditorView logicEditor) {
		this.logicEditor = logicEditor;
		dragStartRunnable = new Runnable() {
			@Override
			public void run() {
				if (touchingView instanceof ActionBlockBeanView actionBlockBeanView) {
					isDragging = true;
					Object draggingBean = null;
					getLogicEditor().getLogicEditorCanva().setAllowScroll(false);
					if (actionBlockBeanView.isInsideCanva()) {

						if (actionBlockBeanView.getParent() == null)
							return;
						if (actionBlockBeanView.getParent() instanceof ViewGroup actionBlockDropZone) {

							ArrayList<ActionBlockBean> draggingBlocks = new ArrayList<ActionBlockBean>();

							ArrayList<ActionBlockBean> blocksList = null;

							int index = 0;
							if (actionBlockDropZone instanceof MainActionBlockDropZoneView mainChain) {
								blocksList = mainChain.getBlockBeans();
								index = mainChain.indexOfChild(actionBlockBeanView) - 1;
								for (int i = index; i < mainChain.getBlocksSize(); ++i) {
									actionBlockDropZone
											.getChildAt(i + 1)
											.setVisibility(View.GONE);
									draggingBlocks.add(blocksList.get(i));
								}
							} else if (actionBlockDropZone instanceof ActionBlockDropZoneView regularChain) {
								index = regularChain.indexOfChild(actionBlockBeanView);
								blocksList = regularChain.getBlockBeans();

								for (int i = index; i < actionBlockDropZone.getChildCount(); ++i) {
									actionBlockDropZone
											.getChildAt(i)
											.setVisibility(View.GONE);
									draggingBlocks.add(blocksList.get(i));
								}
							}

							draggingBean = draggingBlocks;

							DraggingBlockDummy draggingView = new DraggingBlockDummy(
									getLogicEditor().getContext(),
									draggingBlocks.get(0));
							draggingView.setDraggedFromCanva(
									actionBlockBeanView.isInsideCanva());

							getLogicEditor().startDrag(draggingBean, draggingView, x, y);
						}

					} else {

						if (actionBlockBeanView instanceof RegularBlockBeanView regularBlockBeanView) {
							draggingBean = regularBlockBeanView.getRegularBlockBean().cloneBean();
							DraggingBlockDummy draggingView = new DraggingBlockDummy(
									getLogicEditor().getContext(),
									regularBlockBeanView
											.getRegularBlockBean()
											.cloneBean());
							draggingView.setDraggedFromCanva(
									actionBlockBeanView.isInsideCanva());

							getLogicEditor().startDrag(draggingBean, draggingView, x, y);
						}
					}
				}
			}
		};
		dragHandler = new Handler();
	}

	public LogicEditorView getLogicEditor() {
		return this.logicEditor;
	}

	public void setLogicEditor(LogicEditorView logicEditor) {
		this.logicEditor = logicEditor;
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		touchingView = view;
		initialRelativeCoordinateX = CanvaMathUtils.getRelativeCoordinates(touchingView, getLogicEditor())[0];
		initialRelativeCoordinateY = CanvaMathUtils.getRelativeCoordinates(touchingView, getLogicEditor())[1];
		x = motionEvent.getX()
				+ initialRelativeCoordinateX
				- UnitUtils.dpToPx(getLogicEditor().getContext(), 80);
		y = motionEvent.getY()
				+ initialRelativeCoordinateY
				- UnitUtils.dpToPx(getLogicEditor().getContext(), 80);

		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!isDragging) {
					dragHandler.postDelayed(
							dragStartRunnable, ViewConfiguration.getLongPressTimeout());
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (isDragging) {
					getLogicEditor().moveDraggingView(this.x, this.y);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (isDragging) {
					getLogicEditor().dropDraggingView(this.x, this.y);
				}
				isDragging = false;
				touchingView = null;
				getLogicEditor().getLogicEditorCanva().setAllowScroll(true);
				dragHandler.removeCallbacks(dragStartRunnable);
				break;
		}

		return true;
	}

	public View getTouchingView() {
		return this.touchingView;
	}
}
