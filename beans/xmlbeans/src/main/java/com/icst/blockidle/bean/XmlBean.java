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

package com.icst.blockidle.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class XmlBean implements Serializable {
	public static final long serialVersionUID = 1L;

	private boolean addAndroidNameSpace;
	private boolean addAppNameSpace;
	private boolean addToolsNameSpace;
	private boolean isRootElement;
	private ArrayList<XmlAttributeBean> attributes;
	private ArrayList<XmlBean> children;
	private String name;
	private String id;

	public String getCode(String whitespace) {
		StringBuilder code = new StringBuilder();
		if (isRootElement) {
			code.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		}
		code.append(whitespace);
		code.append("<");
		code.append(name);
		if (isRootElement) {
			if (getAddAndroidNameSpace()) {
				code.append("\n\t");
				code.append("xmlns:android");
				code.append(String.valueOf("=\""));
				code.append("http://schemas.android.com/apk/res/android");
				code.append(String.valueOf("\""));
			}
			if (getAddAppNameSpace()) {
				code.append("\n\t");
				code.append("xmlns:app");
				code.append(String.valueOf("=\""));
				code.append("http://schemas.android.com/apk/res-auto");
				code.append(String.valueOf("\""));
			}
			if (getAddToolsNameSpace()) {
				code.append("\n\t");
				code.append("xmlns:tools");
				code.append(String.valueOf("=\""));
				code.append("http://schemas.android.com/tools");
				code.append(String.valueOf("\""));
			}
		}
		if (attributes == null && children == null) {
			code.append("/>");
		} else if (attributes == null) {
			code.append(">\n");
			for (int i = 0; i < children.size(); ++i) {
				code.append(children.get(i).getCode(whitespace.concat("\t")));
				code.append("\n");
			}
			code.append(whitespace);
			code.append("</");
			code.append(name);
			code.append(">");
		} else if (attributes != null && children != null) {
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
			for (int i = 0; i < children.size(); ++i) {
				code.append(children.get(i).getCode(whitespace.concat("\t")));
				code.append("\n");
			}
			code.append(whitespace);
			code.append("</");
			code.append(name);
			code.append(">");
		} else if (children == null) {
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

	public boolean getAddAndroidNameSpace() {
		return this.addAndroidNameSpace;
	}

	public void setAddAndroidNameSpace(boolean addAndroidNameSpace) {
		this.addAndroidNameSpace = addAndroidNameSpace;
	}

	public boolean getAddAppNameSpace() {
		return this.addAppNameSpace;
	}

	public void setAddAppNameSpace(boolean addAppNameSpace) {
		this.addAppNameSpace = addAppNameSpace;
	}

	public boolean getAddToolsNameSpace() {
		return this.addToolsNameSpace;
	}

	public void setAddToolsNameSpace(boolean addToolsNameSpace) {
		this.addToolsNameSpace = addToolsNameSpace;
	}

	public boolean isRootElement() {
		return this.isRootElement;
	}

	public void setRootElement(boolean isRootElement) {
		this.isRootElement = isRootElement;
	}

	public ArrayList<XmlAttributeBean> getAttributes() {
		return this.attributes;
	}

	public void setAttributes(ArrayList<XmlAttributeBean> attributes) {
		this.attributes = attributes;
	}

	public ArrayList<XmlBean> getChildren() {
		return this.children;
	}

	public void setChildren(ArrayList<XmlBean> children) {
		this.children = children;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
