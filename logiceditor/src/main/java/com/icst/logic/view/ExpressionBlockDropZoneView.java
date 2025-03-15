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

import com.icst.blockidle.beans.BlockBean;
import com.icst.blockidle.beans.ExpressionBlockBean;
import com.icst.logic.block.view.ExpressionBlockBeanView;
import com.icst.logic.builder.ExpressionBlockViewFactory;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.utils.CanvaMathUtils;

import android.content.Context;

public class ExpressionBlockDropZoneView extends BlockDropZoneView {

	public ExpressionBlockDropZoneView(
			Context context, LogicEditorConfiguration configuration, LogicEditorView logicEditor) {
		super(context, configuration, logicEditor);
	}

	private ExpressionBlockBeanView mExpressionBlockBeanView;

	public void setExpressionBlockBean(ExpressionBlockBean mExpressionBlockBean) {
		removeAllViews();
		mExpressionBlockBeanView = ExpressionBlockViewFactory.generateView(mExpressionBlockBean, getContext(),
				getConfiguration(), getLogicEditor());
		mExpressionBlockBeanView.setInsideCanva(true);
		addView(mExpressionBlockBeanView);
	}

	public ExpressionBlockBeanView getExpressionBlockBeanView() {
		return mExpressionBlockBeanView;
	}

	@Override
	public boolean canDrop(BlockBean block, float x, float y) {
		boolean insideTarget = CanvaMathUtils.isCoordinatesInsideTargetView(
				this, getLogicEditor().getEditorSectionView(), x, y);
		if (insideTarget) {
			return mExpressionBlockBeanView.canDrop(block, x, y);
		} else
			return false;
	}

	@Override
	public void dropBlockIfAllowed(BlockBean block, float x, float y) {
		if (canDrop(block, x, y)) {
			mExpressionBlockBeanView.drop(block, x, y);
		}
	}

	@Override
	public void highlightNearestTargetIfAllowed(BlockBean block, float x, float y) {
		if (canDrop(block, x, y)) {
			mExpressionBlockBeanView.highlightNearestTarget(block, x, y);
		}
	}
}
