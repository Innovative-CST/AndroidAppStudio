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

package com.icst.android.appstudio.vieweditor.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewModel implements Serializable {
	public static final long serialVersionUID = 15L;

	private String viewClass;
	private boolean isRootElement;
	private ArrayList<AttributesModel> attributes;
	private ArrayList<ViewModel> childs;

	public String getCode(String whitespace, LayoutModel layout) {
		StringBuilder code = new StringBuilder();
		if (isRootElement) {
			code.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		}
		code.append(whitespace);
		code.append("<");
		code.append(viewClass);
		if (isRootElement) {
			if (layout.isAndroidNameSpaceUsed()) {
				code.append("\n\t");
				code.append("xmlns:android");
				code.append(String.valueOf("=\""));
				code.append("http://schemas.android.com/apk/res/android");
				code.append(String.valueOf("\""));
			}
			if (layout.isAppNameSpaceUsed()) {
				code.append("\n\t");
				code.append("xmlns:app");
				code.append(String.valueOf("=\""));
				code.append("http://schemas.android.com/apk/res-auto");
				code.append(String.valueOf("\""));
			}
			if (layout.isAndroidNameSpaceUsed()) {
				code.append("\n\t");
				code.append("xmlns:tools");
				code.append(String.valueOf("=\""));
				code.append("http://schemas.android.com/tools");
				code.append(String.valueOf("\""));
			}
		}
		if (attributes == null && childs == null) {
			code.append("/>");
		} else if (attributes == null) {
			code.append(">\n");
			for (int i = 0; i < childs.size(); ++i) {
				code.append(childs.get(i).getCode(whitespace.concat("\t"), layout));
				code.append("\n");
			}
			code.append(whitespace);
			code.append("</");
			code.append(viewClass);
			code.append(">");
		} else if (attributes != null && childs != null) {
			for (int i = 0; i < attributes.size(); ++i) {
				if (i == 0) {
					code.append("\n");
					code.append(whitespace.concat("\t"));
				}

				code.append(attributes.get(i).getAttribute());
				code.append(String.valueOf("=\""));
				code.append(String.valueOf(attributes.get(i).getAttributeValue()));
				code.append(String.valueOf("\""));

				if (i != (attributes.size() - 1)) {
					code.append("\n");
					code.append(whitespace.concat("\t"));
				}
			}

			code.append(">\n");
			for (int i = 0; i < childs.size(); ++i) {
				code.append(childs.get(i).getCode(whitespace.concat("\t"), layout));
				code.append("\n");
			}
			code.append(whitespace);
			code.append("</");
			code.append(viewClass);
			code.append(">");
		} else if (childs == null) {
			for (int i = 0; i < attributes.size(); ++i) {
				if (i == 0) {
					code.append("\n");
					code.append(whitespace.concat("\t"));
				}

				code.append(attributes.get(i).getAttribute());
				code.append(String.valueOf("=\""));
				code.append(String.valueOf(attributes.get(i).getAttributeValue()));
				code.append(String.valueOf("\""));

				if (i != (attributes.size() - 1)) {
					code.append("\n");
					code.append(whitespace.concat("\t"));
				}
			}

			code.append("/>");
		}

		return code.toString();
	}

	public void setClass(String viewClass) {
		this.viewClass = viewClass;
	}

	public ViewModel cloneViewModel() {
		return null;
	}

	public boolean getIsRootElement() {
		return this.isRootElement;
	}

	public void setRootElement(boolean isRootElement) {
		this.isRootElement = isRootElement;
	}

	public ArrayList<AttributesModel> getAttributes() {
		return this.attributes;
	}

	public void setAttributes(ArrayList<AttributesModel> attributes) {
		this.attributes = attributes;
	}

	public ArrayList<ViewModel> getChilds() {
		return this.childs;
	}

	public void setChilds(ArrayList<ViewModel> childs) {
		this.childs = childs;
	}
}
