package com.icst.logic.view;

import java.util.ArrayList;

import com.icst.android.appstudio.beans.ActionBlockBean;
import com.icst.android.appstudio.beans.BlockBean;
import com.icst.logic.block.view.ActionBlockBeanView;
import com.icst.logic.config.BlockMarginConstants;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.utils.ActionBlockUtils;
import com.icst.logic.utils.BlockImageUtils;
import com.icst.logic.utils.CanvaMathUtils;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.ImageViewUtils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class ActionBlockLayerView extends ActionBlockDropZoneView
		implements LayerBeanView<ActionBlockLayerView> {

	private int layerPosition;
	private boolean isFirstLayer;
	private boolean isLastLayer;
	private String color;
	private BlockBean block;

	private LinearLayout actionBlockLayerTop;
	private LinearLayout blockLayout;
	private LinearLayout actionBlockLayerBottom;

	public ActionBlockLayerView(
			Context context,
			LogicEditorConfiguration logicEditorConfiguration,
			LogicEditorView logicEditor) {
		super(context, logicEditorConfiguration, logicEditor);
		actionBlockLayerTop = new LinearLayout(context);
		addView(actionBlockLayerTop);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		lp.setMargins(0, BlockMarginConstants.CHAINED_ACTION_BLOCK_IN_LAYER_TOP_MARGIN, 0, 0);
		blockLayout = new LinearLayout(context);
		blockLayout.setOrientation(VERTICAL);
		addView(blockLayout);
		blockLayout.setLayoutParams(lp);

		actionBlockLayerBottom = new LinearLayout(context);
		addView(actionBlockLayerBottom);
	}

	@Override
	public void setColor(String color) {
		this.color = color;

		actionBlockLayerTop.setBackgroundDrawable(
				ImageViewUtils.getImageView(
						getContext(),
						ColorUtils.harmonizeHexColor(getContext(), getColor()),
						BlockImageUtils.getImage(BlockImageUtils.Image.ACTION_BLOCK_LAYER_TOP)));

		blockLayout.setBackgroundDrawable(
				ImageViewUtils.getImageView(
						getContext(),
						ColorUtils.harmonizeHexColor(getContext(), getColor()),
						BlockImageUtils.getImage(
								BlockImageUtils.Image.ACTION_BLOCK_LAYER_BACKDROP)));

		actionBlockLayerBottom.setBackgroundDrawable(
				ImageViewUtils.getImageView(
						getContext(),
						ColorUtils.harmonizeHexColor(getContext(), getColor()),
						BlockImageUtils.getImage(BlockImageUtils.Image.ACTION_BLOCK_LAYER_BOTTOM)));
		invalidate();
	}

	// Configured for ActionBlockLayerView
	@Override
	public int getIndex(float x, float y) {
		int[] relativeCoordinates = CanvaMathUtils.getRelativeCoordinates(this, getLogicEditor());

		int index = 0;
		for (int i = 0; i < blockLayout.getChildCount(); i++) {
			View child = blockLayout.getChildAt(i);
			if (y - ((int) relativeCoordinates[1]) > child.getY() + (child.getHeight() / 2)) {
				index = i + 1;
			} else {
				break;
			}
		}

		return index;
	}

	// Configured for ActionBlockLayerView
	@Override
	protected void addBlockBeans(ArrayList<ActionBlockBean> actionBlocks, int index) {
		getBlockBeans().addAll(index, actionBlocks);

		for (int i = 0; i < actionBlocks.size(); ++i) {
			ActionBlockBean actionBlock = actionBlocks.get(i);
			ActionBlockBeanView actionBlockBeanView = ActionBlockUtils.getBlockView(
					getContext(), actionBlock, getConfiguration(), getLogicEditor());

			if (actionBlockBeanView == null)
				continue;

			actionBlockBeanView.setInsideCanva(true);
			blockLayout.addView(actionBlockBeanView, i + index);

			if (i == 0 && index == 0)
				continue;

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			lp.setMargins(0, BlockMarginConstants.CHAINED_ACTION_BLOCK_IN_LAYER_TOP_MARGIN, 0, 0);
			actionBlockBeanView.setLayoutParams(lp);
		}
	}

	// Configured for ActionBlockLayerView
	@Override
	public boolean canDrop(ArrayList<ActionBlockBean> blocks, float x, float y) {
		int index = 0;
		for (int i = 0; i < blockLayout.getChildCount(); i++) {
			View child = blockLayout.getChildAt(i);
			if (CanvaMathUtils.isCoordinatesInsideTargetView(
					child, getLogicEditor().getEditorSectionView(), x, y)) {
				index = i;
				break;
			}
		}
		if (blockLayout.getChildAt(index) instanceof ActionBlockBeanView blockBeanView) {
			if (blockBeanView.canDrop(blocks, x, y)) {
				return true;
			}
		}
		return canDrop(blocks, getIndex(x, y));
	}

	// Configured for ActionBlockLayerView
	@Override
	public boolean canDrop(ActionBlockBean block, float x, float y) {
		ArrayList<ActionBlockBean> actionBlocks = new ArrayList<ActionBlockBean>();
		actionBlocks.add(block);
		int index = 0;
		for (int i = 0; i < blockLayout.getChildCount(); i++) {
			View child = blockLayout.getChildAt(i);
			if (CanvaMathUtils.isCoordinatesInsideTargetView(
					child, getLogicEditor().getEditorSectionView(), x, y)) {
				index = i;
				break;
			}
		}
		if (blockLayout.getChildAt(index) instanceof ActionBlockBeanView blockBeanView) {
			if (blockBeanView.canDrop(actionBlocks, x, y)) {
				return true;
			}
		}
		return canDrop(block, getIndex(x, y));
	}

	// Configured for ActionBlockLayerView
	@Override
	public void highlightNearestTarget(ArrayList<ActionBlockBean> blocks, float x, float y) {
		int index = 0;
		for (int i = 0; i < blockLayout.getChildCount(); i++) {
			View child = blockLayout.getChildAt(i);
			if (CanvaMathUtils.isCoordinatesInsideTargetView(
					child, getLogicEditor().getEditorSectionView(), x, y)) {
				index = i;
				break;
			}
		}
		if (blockLayout.getChildAt(index) instanceof ActionBlockBeanView blockBeanView) {
			if (blockBeanView.canDrop(blocks, x, y)) {
				blockBeanView.highlightNearestTarget(blocks, x, y);
				return;
			}
		}

		index = getIndex(x, y);
		if (canDrop(blocks, index)) {
			getLogicEditor().removeDummyHighlighter();
			LayoutParams highlighterLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			highlighterLp.setMargins(0, BlockMarginConstants.CHAINED_ACTION_BLOCK_TOP_MARGIN, 0, 0);
			NearestTargetHighlighterView highlighter = new NearestTargetHighlighterView(getContext(), blocks.get(0));
			getLogicEditor().setDummyHighlighter(highlighter);
			blockLayout.addView(highlighter, index);
			highlighter.setLayoutParams(highlighterLp);
		}
	}

	// Configured for ActionBlockLayerView
	@Override
	public void highlightNearestTarget(ActionBlockBean block, float x, float y) {
		int index = 0;
		for (int i = 0; i < blockLayout.getChildCount(); i++) {
			View child = blockLayout.getChildAt(i);
			if (CanvaMathUtils.isCoordinatesInsideTargetView(
					child, getLogicEditor().getEditorSectionView(), x, y)) {
				index = i;
				break;
			}
		}
		if (blockLayout.getChildAt(index) instanceof ActionBlockBeanView blockBeanView) {
			if (blockBeanView.canDrop(block, x, y)) {
				blockBeanView.highlightNearestTarget(block, x, y);
				return;
			}
		}

		index = getIndex(x, y);
		if (canDrop(block, x, y)) {
			getLogicEditor().removeDummyHighlighter();
			LayoutParams highlighterLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			highlighterLp.setMargins(0, BlockMarginConstants.CHAINED_ACTION_BLOCK_TOP_MARGIN, 0, 0);
			NearestTargetHighlighterView highlighter = new NearestTargetHighlighterView(getContext(), block);
			getLogicEditor().setDummyHighlighter(highlighter);
			blockLayout.addView(highlighter, index);
			highlighter.setLayoutParams(highlighterLp);
		}
	}

	@Override
	public void dropToNearestTarget(ArrayList<ActionBlockBean> blocks, float x, float y) {
		int index = 0;
		for (int i = 0; i < blockLayout.getChildCount(); i++) {
			View child = blockLayout.getChildAt(i);
			if (CanvaMathUtils.isCoordinatesInsideTargetView(
					child, getLogicEditor().getEditorSectionView(), x, y)) {
				index = i;
				break;
			}
		}
		if (blockLayout.getChildAt(index) instanceof ActionBlockBeanView blockBeanView) {
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

	@Override
	public void dropToNearestTarget(ActionBlockBean block, float x, float y) {
		ArrayList<ActionBlockBean> blocks = new ArrayList<ActionBlockBean>();
		blocks.add(block);
		dropToNearestTarget(blocks, x, y);
	}

	@Override
	public BlockBean getBlock() {
		return this.block;
	}

	@Override
	public String getColor() {
		return this.color;
	}

	@Override
	public int getLayerPosition() {
		return this.layerPosition;
	}

	@Override
	public boolean isFirstLayer() {
		return this.isFirstLayer;
	}

	@Override
	public boolean isLastLayer() {
		return this.isLastLayer;
	}

	@Override
	public void setBlock(BlockBean block) {
		this.block = block;
	}

	@Override
	public void setFirstLayer(boolean isFirstLayer) {
		this.isFirstLayer = isFirstLayer;
	}

	@Override
	public void setLastLayer(boolean isLastLayer) {
		this.isLastLayer = isLastLayer;
	}

	@Override
	public void setLayerPosition(int layerPosition) {
		this.layerPosition = layerPosition;
	}

	@Override
	public ActionBlockLayerView getView() {
		return this;
	}

	public LinearLayout getBlockLayout() {
		return this.blockLayout;
	}
}
