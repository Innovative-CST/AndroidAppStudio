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

package com.icst.logic.block.view;

import java.util.ArrayList;

import com.icst.blockidle.beans.ActionBlockBean;
import com.icst.blockidle.beans.BlockBean;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.utils.CanvaMathUtils;
import com.icst.logic.view.ActionBlockLayerView;
import com.icst.logic.view.BlockElementLayerBeanView;
import com.icst.logic.view.LayerBeanView;

import android.content.Context;

public abstract class ActionBlockBeanView extends BlockBeanView {

	public ActionBlockBeanView(
			Context context,
			LogicEditorConfiguration logicEditorConfiguration,
			LogicEditorView logicEditor) {
		super(context, logicEditorConfiguration, logicEditor);
	}

	public abstract ActionBlockBean getActionBlockBean();

	public abstract ArrayList<LayerBeanView> getLayers();

	@Override
	public boolean canDrop(BlockBean block, float x, float y) {
		if (block instanceof ActionBlockBean mActionBlockBean) {
			ArrayList<ActionBlockBean> blocks = new ArrayList<>();
			blocks.add(mActionBlockBean);
			return canDrop(blocks, x, y);
		} else {
			for (int i = 0; i < getLayers().size(); ++i) {
				if (CanvaMathUtils.isCoordinatesInsideTargetView(
						getLayers().get(i).getView(), getLogicEditor().getEditorSectionView(), x, y)) {
					if (getLayers().get(i) instanceof BlockElementLayerBeanView layerView) {
						return layerView.canDrop(
								block, getLogicEditor().getEditorSectionView(), x, y);
					} else if (getLayers().get(i) instanceof ActionBlockLayerView layerView) {
						return layerView.canDrop(block, x, y);
					}
				}
			}
		}
		return false;
	}

	public boolean canDrop(ArrayList<ActionBlockBean> blocks, float x, float y) {
		for (int i = 0; i < getLayers().size(); ++i) {
			if (!CanvaMathUtils.isCoordinatesInsideTargetView(
					getLayers().get(i).getView(), getLogicEditor().getEditorSectionView(), x, y)) {
				continue;
			}
			if (getLayers().get(i) instanceof ActionBlockLayerView layerView) {
				return layerView.canDrop(blocks, x, y);
			}
		}
		return false;
	}

	@Override
	public void highlightNearestTarget(BlockBean block, float x, float y) {
		if (block instanceof ActionBlockBean mActionBlockBean) {
			ArrayList<ActionBlockBean> blocks = new ArrayList<>();
			blocks.add(mActionBlockBean);
			highlightNearestTarget(blocks, x, y);
		} else {
			for (int i = 0; i < getLayers().size(); ++i) {
				if (!CanvaMathUtils.isCoordinatesInsideTargetView(
						getLayers().get(i).getView(), getLogicEditor().getEditorSectionView(), x, y)) {
					continue;
				}
				if (getLayers().get(i) instanceof BlockElementLayerBeanView layerView) {
					layerView.highlightNearestTargetIfAllowed(
							block, getLogicEditor().getEditorSectionView(), x, y);
				} else if (getLayers().get(i) instanceof ActionBlockLayerView layerView) {
					layerView.highlightNearestTargetIfAllowed(block, x, y);
				}
			}
		}
	}

	public void highlightNearestTarget(ArrayList<ActionBlockBean> blocks, float x, float y) {
		for (int i = 0; i < getLayers().size(); ++i) {
			if (!CanvaMathUtils.isCoordinatesInsideTargetView(
					getLayers().get(i).getView(), getLogicEditor().getEditorSectionView(), x, y)) {
				continue;
			}
			if (getLayers().get(i) instanceof ActionBlockLayerView layerView) {
				layerView.highlightNearestTargetIfAllowed(blocks, x, y);
			}
		}
	}

	@Override
	public void drop(BlockBean block, float x, float y) {
		if (block instanceof ActionBlockBean mActionBlockBean) {
			ArrayList<ActionBlockBean> blocks = new ArrayList<>();
			blocks.add(mActionBlockBean);
			drop(blocks, x, y);
		} else {
			for (int i = 0; i < getLayers().size(); ++i) {
				if (!CanvaMathUtils.isCoordinatesInsideTargetView(
						getLayers().get(i).getView(), getLogicEditor().getEditorSectionView(), x, y)) {
					continue;
				}
				if (getLayers().get(i) instanceof BlockElementLayerBeanView layerView) {
					layerView.dropToNearestTargetIfAllowed(
							block, getLogicEditor().getEditorSectionView(), x, y);
				} else if (getLayers().get(i) instanceof ActionBlockLayerView layerView) {
					layerView.dropBlockIfAllowed(block, x, y);
				}
			}
		}
	}

	public void drop(ArrayList<ActionBlockBean> blocks, float x, float y) {
		for (int i = 0; i < getLayers().size(); ++i) {
			if (!CanvaMathUtils.isCoordinatesInsideTargetView(
					getLayers().get(i).getView(), getLogicEditor().getEditorSectionView(), x, y)) {
				continue;
			}
			if (getLayers().get(i) instanceof ActionBlockLayerView layerView) {
				layerView.dropBlockIfAllowed(blocks, x, y);
			}
		}
	}

}
