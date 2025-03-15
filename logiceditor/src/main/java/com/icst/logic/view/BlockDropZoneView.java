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
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;

import android.content.Context;
import android.widget.LinearLayout;

/** A view representing a block drop zone */
public abstract class BlockDropZoneView extends LinearLayout {
	private LogicEditorConfiguration configuration;
	private LogicEditorView logicEditor;

	public BlockDropZoneView(
			Context context, LogicEditorConfiguration configuration, LogicEditorView logicEditor) {
		super(context);
		this.configuration = configuration;
		this.logicEditor = logicEditor;
	}

	public LogicEditorConfiguration getConfiguration() {
		return this.configuration;
	}

	public LogicEditorView getLogicEditor() {
		return this.logicEditor;
	}

	public abstract boolean canDrop(BlockBean block, float x, float y);

	public abstract void dropBlockIfAllowed(BlockBean block, float x, float y);

	public abstract void highlightNearestTargetIfAllowed(BlockBean block, float x, float y);
}
