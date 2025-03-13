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

import com.icst.android.appstudio.beans.XmlAttributeBean;
import com.icst.android.appstudio.beans.XmlBean;

public class BuiltInAndroidManifest {
	public static XmlBean get(String project) {
		XmlBean manifest = new XmlBean();

		manifest.setAddAndroidNameSpace(true);
		manifest.setAddToolsNameSpace(true);
		manifest.setId("manifest");
		manifest.setName("manifest");
		manifest.setRootElement(true);

		ArrayList<XmlBean> manifestChilds = new ArrayList<XmlBean>();

		XmlBean application = new XmlBean();
		application.setAddAndroidNameSpace(true);
		application.setAddToolsNameSpace(true);
		application.setId("application");
		application.setName("application");

		ArrayList<XmlAttributeBean> applicationAttr = new ArrayList<XmlAttributeBean>();

		XmlAttributeBean applicationName = new XmlAttributeBean();
		applicationName.setAttribute("android:name");
		applicationName.setAttributeValue(".MyApplication");
		applicationAttr.add(applicationName);

		XmlAttributeBean applicationLabel = new XmlAttributeBean();
		applicationLabel.setAttribute("android:label");
		applicationLabel.setAttributeValue(project);
		applicationAttr.add(applicationLabel);

		application.setAttributes(applicationAttr);

		manifestChilds.add(application);

		manifest.setChildren(manifestChilds);

		return manifest;
	}
}
