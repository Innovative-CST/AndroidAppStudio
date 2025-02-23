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

package com.icst.android.appstudio.javablocks;

import java.util.ArrayList;

import com.icst.android.appstudio.beans.ActionBlockBean;
import com.icst.android.appstudio.beans.BlockElementBean;
import com.icst.android.appstudio.beans.BlockElementLayerBean;
import com.icst.android.appstudio.beans.EventBean;
import com.icst.android.appstudio.beans.EventBlockBean;
import com.icst.android.appstudio.beans.LabelBlockElementBean;
import com.icst.android.appstudio.beans.utils.CodeFormatterUtils;

public final class MainJavaEventBean {

	public static EventBean getJavaMainEvent() {
		EventBean event = new EventBean();
		event.setActionBlockBeans(new ArrayList<ActionBlockBean>());
		event.setEventDefinationBlockBean(getJavaMainEventBlock());

		StringBuilder code = new StringBuilder();
		code.append("public static void main(String[] args) {\n\t");
		code.append(CodeFormatterUtils.getKeySyntaxString("EventCode"));
		code.append("\n}");

		event.setCodeSyntax(code.toString());
		return event;
	}

	public static EventBlockBean getJavaMainEventBlock() {
		EventBlockBean block = new EventBlockBean();
		block.setColor("#FCC303");

		ArrayList<BlockElementLayerBean> layers = new ArrayList<>();

		BlockElementLayerBean layer1 = new BlockElementLayerBean();
		ArrayList<BlockElementBean> layer1elements = new ArrayList<>();
		LabelBlockElementBean onMainMethodCalled = new LabelBlockElementBean();
		onMainMethodCalled.setLabel("onMainMethodCalled");
		layer1elements.add(onMainMethodCalled);
		layer1.setBlockElementBeans(layer1elements);

		layers.add(layer1);
		block.setElementsLayers(layers);
		return block;
	}
}
