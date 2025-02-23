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

package com.icst.android.appstudio.builtin.xml;

import java.util.ArrayList;

import com.icst.android.appstudio.xml.XmlAttributeModel;
import com.icst.android.appstudio.xml.XmlModel;

public class BuiltInAndroidManifest {
	public static XmlModel get(String project) {
		XmlModel manifest = new XmlModel();

		manifest.setAddAndroidNameSpace(true);
		manifest.setAddToolsNameSpace(true);
		manifest.setId("manifest");
		manifest.setName("manifest");
		manifest.setRootElement(true);

		ArrayList<XmlModel> manifestChilds = new ArrayList<XmlModel>();

		XmlModel application = new XmlModel();
		application.setAddAndroidNameSpace(true);
		application.setAddToolsNameSpace(true);
		application.setId("application");
		application.setName("application");

		ArrayList<XmlAttributeModel> applicationAttr = new ArrayList<XmlAttributeModel>();

		XmlAttributeModel applicationName = new XmlAttributeModel();
		applicationName.setAttribute("android:name");
		applicationName.setAttributeValue(".MyApplication");
		applicationAttr.add(applicationName);

		XmlAttributeModel applicationLabel = new XmlAttributeModel();
		applicationLabel.setAttribute("android:label");
		applicationLabel.setAttributeValue(project);
		applicationAttr.add(applicationLabel);

		application.setAttributes(applicationAttr);

		manifestChilds.add(application);

		manifest.setChildren(manifestChilds);

		return manifest;
	}
}
