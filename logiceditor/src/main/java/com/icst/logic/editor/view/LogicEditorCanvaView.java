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
import android.view.MotionEvent;
import android.view.View;
import com.icst.android.appstudio.beans.EventBean;
import com.icst.logic.lib.config.LogicEditorConfiguration;
import com.icst.logic.lib.view.BlockDropZoneView;
import com.icst.logic.lib.view.MainActionBlockDropZoneView;
import java.util.ArrayList;

/**
 * Logic Editor Canva, It is tha canva that can be scrolled according to the
 * contents. It adjusts
 * its height and width according to its content when scrolled it is increased
 * and renders.
 */
public class LogicEditorCanvaView extends LogicEditorScrollView {

	private EventBean eventBean;

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
		if (eventBean == null) {
			removeAllViews();
			return;
		}

		MainActionBlockDropZoneView mainChainDropZone = new MainActionBlockDropZoneView(
				getContext(),
				eventBean.getEventDefinationBlockBean(),
				logicEditorConfiguration);
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
	 * @param currentMax
	 *            Current maximum value (width or height).
	 * @param dimension
	 *            Whether calculating for HEIGHT or WIDTH.
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
}
