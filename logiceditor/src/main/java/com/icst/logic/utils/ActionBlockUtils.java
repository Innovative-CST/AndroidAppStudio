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

package com.icst.logic.utils;

import com.icst.android.appstudio.beans.ActionBlockBean;
import com.icst.android.appstudio.beans.RegularBlockBean;
import com.icst.android.appstudio.beans.TerminatorBlockBean;
import com.icst.logic.block.view.ActionBlockBeanView;
import com.icst.logic.block.view.RegularBlockBeanView;
import com.icst.logic.block.view.TerminatorBlockBeanView;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;

import android.content.Context;

public final class ActionBlockUtils {
	public static ActionBlockBeanView getBlockView(Context context, ActionBlockBean actionBlock,
			LogicEditorConfiguration logicEditorConfiguration, LogicEditorView logicEditor) {
		if (actionBlock instanceof RegularBlockBean regularBlockBean) {
			return new RegularBlockBeanView(context, regularBlockBean, logicEditorConfiguration, logicEditor);
		} else if (actionBlock instanceof TerminatorBlockBean terminatorBlock) {
			return new TerminatorBlockBeanView(context, terminatorBlock, logicEditorConfiguration, logicEditor);
		} else
			return null;
	}
}
