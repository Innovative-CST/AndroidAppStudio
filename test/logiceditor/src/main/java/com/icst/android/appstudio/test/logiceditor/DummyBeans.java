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

package com.icst.android.appstudio.test.logiceditor;

import java.util.ArrayList;

import com.icst.android.appstudio.beans.ActionBlockBean;
import com.icst.android.appstudio.beans.ActionBlockLayerBean;
import com.icst.android.appstudio.beans.BlockElementBean;
import com.icst.android.appstudio.beans.BlockElementLayerBean;
import com.icst.android.appstudio.beans.EventBean;
import com.icst.android.appstudio.beans.EventBlockBean;
import com.icst.android.appstudio.beans.LabelBlockElementBean;
import com.icst.android.appstudio.beans.LayerBean;
import com.icst.android.appstudio.beans.RegularBlockBean;
import com.icst.android.appstudio.beans.StringBlockBean;
import com.icst.android.appstudio.beans.StringBlockElementBean;
import com.icst.android.appstudio.beans.TerminatorBlockBean;

public class DummyBeans {
	public static EventBean getDummyEvent() {
		EventBean event = new EventBean();
		ArrayList<ActionBlockBean> blocks = new ArrayList<ActionBlockBean>();
		blocks.add(getDummyBlock());
		event.setActionBlockBeans(blocks);
		event.setEventDefinationBlockBean(getDummyEventDefBlockBean());
		return event;
	}

	public static RegularBlockBean getDummyBlock() {
		RegularBlockBean testRBlock = new RegularBlockBean();
		testRBlock.setColor("#3345ff");
		testRBlock.setDragAllowed(true);

		ArrayList<LayerBean> layers = new ArrayList<LayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean onTestLabel = new LabelBlockElementBean();
		onTestLabel.setLabel("showToast");

		layer1Elements.add(onTestLabel);
		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		BlockElementLayerBean layer2 = new BlockElementLayerBean();
		ArrayList<BlockElementBean> layer2Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean onTestLabel2 = new LabelBlockElementBean();
		onTestLabel2.setLabel("showToast");

		layer2Elements.add(onTestLabel2);
		layer2.setBlockElementBeans(layer2Elements);

		layers.add(layer2);

		testRBlock.setLayers(layers);
		return testRBlock;
	}

	private static EventBlockBean getDummyEventDefBlockBean() {
		EventBlockBean blockBean = new EventBlockBean();
		blockBean.setColor("#998803");
		blockBean.setDragAllowed(true);

		ArrayList<BlockElementLayerBean> layers = new ArrayList<BlockElementLayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean onTestLabel = new LabelBlockElementBean();
		onTestLabel.setLabel("onTestEvent");

		layer1Elements.add(onTestLabel);
		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);
		blockBean.setElementsLayers(layers);

		return blockBean;
	}

	public static RegularBlockBean getDummyBlock2() {
		RegularBlockBean testRBlock = new RegularBlockBean();
		testRBlock.setColor("#552367");
		testRBlock.setDragAllowed(true);

		ArrayList<LayerBean> layers = new ArrayList<LayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean onTestLabel = new LabelBlockElementBean();
		onTestLabel.setLabel("showToast");

		layer1Elements.add(onTestLabel);
		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		ActionBlockLayerBean layer2 = new ActionBlockLayerBean();
		layers.add(layer2);

		testRBlock.setLayers(layers);
		return testRBlock;
	}

	public static TerminatorBlockBean getStopBlock() {
		TerminatorBlockBean stopBlock = new TerminatorBlockBean();
		stopBlock.setColor("#E44180");
		stopBlock.setDragAllowed(true);

		ArrayList<LayerBean> layers = new ArrayList<LayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean onTestLabel = new LabelBlockElementBean();
		onTestLabel.setLabel("stop");

		layer1Elements.add(onTestLabel);
		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		stopBlock.setLayers(layers);
		return stopBlock;
	}

	public static RegularBlockBean getToastBlock() {
		RegularBlockBean toast = new RegularBlockBean();
		toast.setColor("#3345ff");
		toast.setDragAllowed(true);

		ArrayList<LayerBean> layers = new ArrayList<LayerBean>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean onTestLabel = new LabelBlockElementBean();
		onTestLabel.setLabel("showToast");

		layer1Elements.add(onTestLabel);
		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		toast.setLayers(layers);
		return toast;
	}

	public static StringBlockBean getNewStringBlock() {
		StringBlockBean neStr = new StringBlockBean();
		neStr.setColor("#008824");
		neStr.setDragAllowed(true);

		ArrayList<BlockElementLayerBean> layers = new ArrayList<>();
		BlockElementLayerBean layer1 = new BlockElementLayerBean();

		ArrayList<BlockElementBean> layer1Elements = new ArrayList<BlockElementBean>();

		LabelBlockElementBean onTestLabel = new LabelBlockElementBean();
		onTestLabel.setLabel("new string");

		layer1Elements.add(onTestLabel);

		StringBlockElementBean field = new StringBlockElementBean();
		field.setKey("mkey");
		field.setString("Test");

		layer1Elements.add(field);

		layer1.setBlockElementBeans(layer1Elements);

		layers.add(layer1);

		neStr.setElementsLayers(layers);
		return neStr;
	}
}
