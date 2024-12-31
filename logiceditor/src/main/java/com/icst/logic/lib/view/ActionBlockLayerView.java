package com.icst.logic.lib.view;

import com.icst.android.appstudio.beans.BlockBean;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.lib.config.LogicEditorConfiguration;
import com.icst.logic.utils.BlockImageUtils;
import com.icst.logic.utils.ColorUtils;
import com.icst.logic.utils.ImageViewUtils;

import android.content.Context;

public class ActionBlockLayerView extends ActionBlockDropZoneView
		implements LayerBeanView<ActionBlockLayerView> {

	private int layerPosition;
	private boolean isFirstLayer;
	private boolean isLastLayer;
	private String color;
	private BlockBean block;

	public ActionBlockLayerView(Context context,
			LogicEditorConfiguration logicEditorConfiguration,
			LogicEditorView logicEditor) {
		super(context, logicEditorConfiguration, logicEditor);
	}

	@Override
	public void setColor(String color) {
		this.color = color;
		BlockImageUtils.Image image = null;

		image = BlockImageUtils.Image.BLOCK_ELEMENT_LAYER_BACKDROP;

		setBackgroundDrawable(
				ImageViewUtils.getImageView(
						getContext(),
						ColorUtils.harmonizeHexColor(getContext(), getColor()),
						BlockImageUtils.getImage(image)));
		invalidate();
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
}
