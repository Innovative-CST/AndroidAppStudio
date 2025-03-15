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

package com.icst.logic.listener;

import java.util.ArrayList;

import com.icst.blockidle.beans.ActionBlockBean;
import com.icst.blockidle.beans.BlockBean;
import com.icst.logic.block.view.ActionBlockBeanView;
import com.icst.logic.block.view.ExpressionBlockBeanView;
import com.icst.logic.block.view.RegularBlockBeanView;
import com.icst.logic.block.view.TerminatorBlockBeanView;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.utils.CanvaMathUtils;
import com.icst.logic.utils.UnitUtils;
import com.icst.logic.view.ActionBlockDropZoneView;
import com.icst.logic.view.DraggingBlockDummy;
import com.icst.logic.view.MainActionBlockDropZoneView;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

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

							if (actionBlockBeanView.getParent() instanceof MainActionBlockDropZoneView mainChain) {
								blocksList = mainChain.getBlockBeans();
								int index = mainChain.indexOfChild(actionBlockBeanView) - 1;
								for (int i = index; i < mainChain.getBlocksSize(); ++i) {
									draggingBlocks.add(blocksList.get(i));
									actionBlockDropZone
											.getChildAt(i + 1)
											.setVisibility(View.GONE);
								}
							} else if (actionBlockBeanView
									.getParent() instanceof ActionBlockDropZoneView regularChain) {
								int index = regularChain.indexOfChild(actionBlockBeanView);
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
									draggingBlocks.get(0),
									getLogicEditor().canDropDraggingView(x, y));
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
											.cloneBean(),
									getLogicEditor().canDropDraggingView(x, y));
							draggingView.setDraggedFromCanva(
									actionBlockBeanView.isInsideCanva());

							getLogicEditor().startDrag(draggingBean, draggingView, x, y);
						} else if (actionBlockBeanView instanceof TerminatorBlockBeanView terminatorBlockBeanView) {
							draggingBean = terminatorBlockBeanView.getTerminatorBlockBean().cloneBean();
							DraggingBlockDummy draggingView = new DraggingBlockDummy(
									getLogicEditor().getContext(),
									terminatorBlockBeanView
											.getTerminatorBlockBean()
											.cloneBean(),
									getLogicEditor().canDropDraggingView(x, y));
							draggingView.setDraggedFromCanva(
									actionBlockBeanView.isInsideCanva());

							getLogicEditor().startDrag(draggingBean, draggingView, x, y);
						}
					}
				}
				if (touchingView instanceof ExpressionBlockBeanView expressionBlockBeanView) {

					isDragging = true;
					Object draggingBean = null;

					if (expressionBlockBeanView.isInsideCanva()) {
						draggingBean = expressionBlockBeanView.getExpressionBlockBean();
					} else {
						draggingBean = expressionBlockBeanView.getExpressionBlockBean().cloneBean();
					}

					getLogicEditor().getLogicEditorCanva().setAllowScroll(false);
					DraggingBlockDummy draggingView = new DraggingBlockDummy(
							getLogicEditor().getContext(),
							BlockBean.class.cast(expressionBlockBeanView.getExpressionBlockBean().cloneBean()),
							getLogicEditor().canDropDraggingView(x, y));
					draggingView.setDraggedFromCanva(expressionBlockBeanView.isInsideCanva());
					getLogicEditor().startDrag(draggingBean, draggingView, x, y);

					if (expressionBlockBeanView.isInsideCanva()) {
						expressionBlockBeanView.setVisibility(View.GONE);
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
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if (isDragging) {
					getLogicEditor().dropDraggingView(this.x, this.y);
				}
				getLogicEditor().getLogicEditorCanva().setAllowScroll(true);
				dragHandler.removeCallbacks(dragStartRunnable);
				isDragging = false;
				touchingView = null;
				break;
		}

		return true;
	}

	public View getTouchingView() {
		return this.touchingView;
	}

	public boolean isDragging() {
		return isDragging;
	}
}