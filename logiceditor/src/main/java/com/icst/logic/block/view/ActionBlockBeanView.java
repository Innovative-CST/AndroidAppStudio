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

package com.icst.logic.block.view;

import java.util.ArrayList;

import com.icst.android.appstudio.beans.ActionBlockBean;
import com.icst.android.appstudio.beans.BlockBean;
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
