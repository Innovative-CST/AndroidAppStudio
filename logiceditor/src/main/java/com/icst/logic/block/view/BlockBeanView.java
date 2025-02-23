/*
 *  This file is part of AndroidAppStudio.
 *
 *  AndroidAppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidAppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidAppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.logic.block.view;

import com.icst.android.appstudio.beans.BlockBean;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;

import android.content.Context;
import android.view.ViewGroup;

public abstract class BlockBeanView extends ViewGroup {
	private LogicEditorConfiguration logicEditorConfiguration;
	private LogicEditorView logicEditor;
	private boolean isInsideCanva;

	public BlockBeanView(
			Context context,
			LogicEditorConfiguration logicEditorConfiguration,
			LogicEditorView logicEditor) {
		super(context);
		this.logicEditorConfiguration = logicEditorConfiguration;
		this.logicEditor = logicEditor;
	}

	public LogicEditorConfiguration getLogicEditorConfiguration() {
		return this.logicEditorConfiguration;
	}

	private boolean canDragged() {
		return logicEditor != null;
	}

	public LogicEditorView getLogicEditor() {
		return this.logicEditor;
	}

	public boolean isInsideCanva() {
		return this.isInsideCanva;
	}

	public void setInsideCanva(boolean isInsideCanva) {
		this.isInsideCanva = isInsideCanva;
	}

	public abstract boolean canDrop(BlockBean block, float x, float y);

	public abstract void highlightNearestTarget(BlockBean block, float x, float y);

	public abstract void drop(BlockBean block, float x, float y);
}
