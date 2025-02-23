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

package com.icst.logic.builder;

import com.icst.android.appstudio.beans.BooleanBlockBean;
import com.icst.android.appstudio.beans.ExpressionBlockBean;
import com.icst.android.appstudio.beans.GeneralExpressionBlockBean;
import com.icst.android.appstudio.beans.NumericBlockBean;
import com.icst.android.appstudio.beans.StringBlockBean;
import com.icst.logic.block.view.BooleanBlockView;
import com.icst.logic.block.view.ExpressionBlockBeanView;
import com.icst.logic.block.view.GeneralExpressionBlockView;
import com.icst.logic.block.view.NumericBlockBeanView;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.view.StringBlockBeanView;

import android.content.Context;

public final class ExpressionBlockViewFactory {

	public static final ExpressionBlockBeanView generateView(
			ExpressionBlockBean expressionBlockBean,
			Context context,
			LogicEditorConfiguration configuration,
			LogicEditorView logicEditor) {
		if (expressionBlockBean instanceof StringBlockBean stringBlockBean) {
			StringBlockBeanView view = new StringBlockBeanView(context, stringBlockBean, configuration, logicEditor);
			return view;
		} else if (expressionBlockBean instanceof BooleanBlockBean booleanBlockBean) {
			BooleanBlockView view = new BooleanBlockView(context, booleanBlockBean, configuration, logicEditor);
			return view;
		} else if (expressionBlockBean instanceof NumericBlockBean numericBlockBean) {
			NumericBlockBeanView view = new NumericBlockBeanView(context, numericBlockBean, configuration, logicEditor);
			return view;
		} else if (expressionBlockBean instanceof GeneralExpressionBlockBean generalExpressionBlockBean) {
			GeneralExpressionBlockView view = new GeneralExpressionBlockView(context, generalExpressionBlockBean,
					configuration, logicEditor);
			return view;
		}
		return null;
	}
}
