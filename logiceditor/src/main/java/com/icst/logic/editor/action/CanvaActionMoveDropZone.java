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

package com.icst.logic.editor.action;

import com.icst.logic.editor.CanvaAction;
import com.icst.logic.view.BlockDropZoneView;

public class CanvaActionMoveDropZone implements CanvaAction {
	private int oldPositionX;
	private int oldPositionY;
	private BlockDropZoneView dropZoneView;

	public CanvaActionMoveDropZone(
			int oldPositionX, int oldPositionY, BlockDropZoneView dropZoneView) {
		this.oldPositionX = oldPositionX;
		this.oldPositionY = oldPositionY;
		this.dropZoneView = dropZoneView;
	}

	public int getOldPositionX() {
		return this.oldPositionX;
	}

	public void setOldPositionX(int oldPositionX) {
		this.oldPositionX = oldPositionX;
	}

	public int getOldPositionY() {
		return this.oldPositionY;
	}

	public void setOldPositionY(int oldPositionY) {
		this.oldPositionY = oldPositionY;
	}

	public BlockDropZoneView getDropZoneView() {
		return this.dropZoneView;
	}

	public void setDropZoneView(BlockDropZoneView dropZoneView) {
		this.dropZoneView = dropZoneView;
	}
}
