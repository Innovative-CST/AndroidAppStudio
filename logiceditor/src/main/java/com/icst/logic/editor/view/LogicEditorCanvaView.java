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

package com.icst.logic.editor.view;

import java.util.ArrayList;

import com.icst.blockidle.beans.EventBean;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.CanvaAction;
import com.icst.logic.editor.action.CanvaActionMoveDropZone;
import com.icst.logic.view.BlockDropZoneView;
import com.icst.logic.view.MainActionBlockDropZoneView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Logic Editor Canva, It is tha canva that can be scrolled according to the contents. It adjusts
 * its height and width according to its content when scrolled it is increased and renders.
 */
public class LogicEditorCanvaView extends LogicEditorScrollView {

	private EventBean eventBean;
	private LogicEditorConfiguration logicEditorConfiguration;
	protected MainActionBlockDropZoneView mainChainDropZone;

	public LogicEditorCanvaView(final Context context, final AttributeSet set) {
		super(context, set);
		initializeCanva();
	}

	public void initializeCanva() {
		setAllowScroll(true);
		setClipChildren(true);
	}

	public void openEventInCanva(
			EventBean eventBean,
			LogicEditorConfiguration logicEditorConfiguration,
			LogicEditorView logicEditor) {
		this.eventBean = eventBean;
		this.logicEditorConfiguration = logicEditorConfiguration;
		if (eventBean == null) {
			removeAllViews();
			return;
		}

		mainChainDropZone = new MainActionBlockDropZoneView(
				getContext(),
				eventBean.getEventDefinationBlockBean(),
				logicEditorConfiguration,
				logicEditor);
		LogicEditorScrollView.LayoutParams lp = new LogicEditorScrollView.LayoutParams(
				LogicEditorScrollView.LayoutParams.WRAP_CONTENT,
				LogicEditorScrollView.LayoutParams.WRAP_CONTENT);

		mainChainDropZone.addActionBlocksBeans(eventBean.getActionBlockBeans(), 0);

		addView(mainChainDropZone);
		mainChainDropZone.setLayoutParams(lp);
		// For ArrayList only, not for adding view in logic editor as it is done in
		// Canva.
		ArrayList<BlockDropZoneView> blockDropZones = new ArrayList<BlockDropZoneView>();
		blockDropZones.add(mainChainDropZone);
		logicEditor.setBlockDropZones(blockDropZones);
	}

	public void performAction(CanvaAction action) {
		if (action instanceof CanvaActionMoveDropZone canvaActionMoveDropZone) {
			BlockDropZoneView dropZone = canvaActionMoveDropZone.getDropZoneView();
			LogicEditorCanvaView.LayoutParams lp = new LogicEditorCanvaView.LayoutParams(
					LogicEditorCanvaView.LayoutParams.WRAP_CONTENT,
					LogicEditorCanvaView.LayoutParams.WRAP_CONTENT);
			lp.setMargins(
					canvaActionMoveDropZone.getOldPositionX(),
					canvaActionMoveDropZone.getOldPositionY(),
					0,
					0);

			dropZone.setLayoutParams(lp);
		}
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		super.onLayout(arg0, arg1, arg2, arg3, arg4);
		updateCanvasDimensions();
	}

	@Override
	protected void onScrollChanged(int arg0, int arg1, int arg2, int arg3) {
		super.onScrollChanged(arg0, arg1, arg2, arg3);
		updateCanvasDimensions();
	}

	@Override
	public boolean onTouchEvent(MotionEvent motion) {
		updateCanvasDimensions();
		return super.onTouchEvent(motion);
	}

	/** Adjusts the canvas dimensions based on child views. */
	private void updateCanvasDimensions() {
		// Set initial width and height
		getLayoutParams().width = getRight() - getLeft();
		getLayoutParams().height = getBottom() - getTop();

		int maxWidth = getLayoutParams().width;
		int maxHeight = getLayoutParams().height;

		// Calculate maximum width and height required for the canvas
		maxWidth = calculateMaxDimension(maxWidth, Dimension.WIDTH);
		maxHeight = calculateMaxDimension(maxHeight, Dimension.HEIGHT);

		// Update layout dimensions
		getLayoutParams().width = maxWidth;
		getLayoutParams().height = maxHeight;
		requestLayout();
	}

	private enum Dimension {
		WIDTH, HEIGHT
	}

	/**
	 * Calculates the maximum width or height for the canvas.
	 *
	 * @param currentMax Current maximum value (width or height).
	 * @param dimension Whether calculating for HEIGHT or WIDTH.
	 * @return Updated(+150px) maximum dimension.
	 */
	private int calculateMaxDimension(int currentMax, Dimension dimension) {
		int max = currentMax;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			int dimensionValue = 0;

			if (dimension == Dimension.WIDTH) {
				dimensionValue = (int) (child.getX()
						+ child.getPaddingLeft()
						+ child.getWidth()
						+ child.getPaddingRight()
						+ 150);
			} else {
				dimensionValue = (int) (child.getY()
						+ child.getPaddingTop()
						+ child.getHeight()
						+ child.getPaddingBottom()
						+ 150);
			}
			max = Math.max(dimensionValue, max);
		}
		return max;
	}

	public LogicEditorConfiguration getLogicEditorConfiguration() {
		return this.logicEditorConfiguration;
	}
}
