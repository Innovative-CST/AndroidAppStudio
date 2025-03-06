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

package com.icst.android.appstudio.beans;

import java.io.Serializable;
import java.util.ArrayList;

import com.icst.android.appstudio.beans.utils.BlockBeansUIDConstants;
import com.icst.android.appstudio.beans.utils.CodeFormatterUtils;
import com.icst.android.appstudio.beans.utils.InputValueFormatter;

public class EventBean implements CodeProcessorBean, Serializable {

	public static final long serialVersionUID = BlockBeansUIDConstants.EVENT_BEAN;

	private String name;
	private String title;
	private String description;
	private String codeSyntax;
	private EventBlockBean eventDefinationBlockBean;
	private ArrayList<ActionBlockBean> actionBlockBeans;
	private String holderName;
	private byte[] icon;
	private BeanManifest beanManifest;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EventBlockBean getEventDefinationBlockBean() {
		return this.eventDefinationBlockBean;
	}

	public void setEventDefinationBlockBean(EventBlockBean eventDefinationBlockBean) {
		this.eventDefinationBlockBean = eventDefinationBlockBean;
	}

	public String getHolderName() {
		return this.holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public byte[] getIcon() {
		return this.icon;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	public ArrayList<ActionBlockBean> getActionBlockBeans() {
		return this.actionBlockBeans;
	}

	public void setActionBlockBeans(ArrayList<ActionBlockBean> actionBlockBeans) {
		this.actionBlockBeans = actionBlockBeans;
	}

	public void setCodeSyntax(String codeSyntax) {
		this.codeSyntax = codeSyntax;
	}

	private String processElementLayerCode(
			String code, BlockElementLayerBean blockElementLayerBean) {
		for (int i = 0; i < blockElementLayerBean.getBlockElementBeans().size(); ++i) {
			BlockElementBean blockElementBean = blockElementLayerBean.getBlockElementBeans().get(i);
			if (blockElementBean instanceof ValueInputBlockElementBean valueInputBlockElementBean) {
				code = processValueInputBlockElementCode(code, valueInputBlockElementBean);
			}
		}
		return code;
	}

	private String processValueInputBlockElementCode(
			String code, ValueInputBlockElementBean valueInputBlockElementBean) {
		return InputValueFormatter.formatCode(code, valueInputBlockElementBean);
	}

	@Override
	public String getCodeSyntax() {
		return this.codeSyntax;
	}

	@Override
	public String getProcessedCode() {
		String code = getCodeSyntax();

		StringBuilder blocksCode = new StringBuilder();
		actionBlockBeans.forEach(
				actionBlockBean -> {
					blocksCode.append(actionBlockBean.getProcessedCode());
					blocksCode.append("\n");
				});

		String key = "EventCode";
		String replacingCode = CodeFormatterUtils.getKeySyntaxString(key);
		int intendation = CodeFormatterUtils.getIntendation(code, replacingCode);
		String blocksIntendedCode = CodeFormatterUtils.addIntendation(blocksCode.toString(), intendation);
		code = code.replace(replacingCode, blocksIntendedCode);

		return code;
	}

	public BeanManifest getBeanManifest() {
		return this.beanManifest;
	}

	public void setBeanManifest(BeanManifest beanManifest) {
		this.beanManifest = beanManifest;
	}

	public <T extends BeanMetadata> ArrayList<T> getAllMetadata(Class<T> classType) {
		ArrayList<T> blocksMetadata = new ArrayList<T>();
		for (int i = 0; i < getActionBlockBeans().size(); ++i) {
			blocksMetadata.addAll(getActionBlockBeans().get(i).getAllMetadata(classType));
		}
		if (beanManifest != null) {
			if (beanManifest.getMetadata() != null) {
				blocksMetadata.addAll(beanManifest.get(classType));
			}
		}
		return blocksMetadata;
	}

}
