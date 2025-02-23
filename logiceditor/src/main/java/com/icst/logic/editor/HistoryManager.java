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

package com.icst.logic.editor;

import java.util.Stack;

import com.icst.logic.editor.view.LogicEditorView;

public class HistoryManager {

	private LogicEditorView logicEditor;

	private Stack<CanvaAction> undoStack;
	private Stack<CanvaAction> redoStack;

	public HistoryManager(LogicEditorView logicEditor) {
		this.logicEditor = logicEditor;
		undoStack = new Stack<>();
		redoStack = new Stack<>();
	}

	public void onPerformCanvaAction(CanvaAction canvaAction) {
		undoStack.push(canvaAction);
		redoStack.clear();
	}

	public void undo() {
		if (!undoStack.isEmpty()) {
			CanvaAction action = undoStack.pop();
			logicEditor.getLogicEditorCanva().performAction(action);
			redoStack.push(action);
		}
	}

	public void redo() {
		if (!redoStack.isEmpty()) {
			CanvaAction action = redoStack.pop();
			logicEditor.getLogicEditorCanva().performAction(action);
			undoStack.push(action);
		}
	}

	public boolean canUndo() {
		return !undoStack.isEmpty();
	}

	public boolean canRedo() {
		return !redoStack.isEmpty();
	}
}
