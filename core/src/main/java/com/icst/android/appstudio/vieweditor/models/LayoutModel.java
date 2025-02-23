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
import java.util.HashMap;

public class LayoutModel implements Serializable {
	public static final long serialVersionUID = 14L;

	private ViewModel view;
	private String layoutName;
	private HashMap<String, String> identifiableView;
	private boolean isAndroidNameSpaceUsed;
	private boolean isAppNameSpaceUsed;
	private boolean isToolsNameSpaceUsed;

	public ViewModel getView() {
		return this.view;
	}

	public void setView(ViewModel view) {
		this.view = view;
	}

	public String getLayoutName() {
		return this.layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public boolean isAndroidNameSpaceUsed() {
		return this.isAndroidNameSpaceUsed;
	}

	public void setAndroidNameSpaceUsed(boolean isAndroidNameSpaceUsed) {
		this.isAndroidNameSpaceUsed = isAndroidNameSpaceUsed;
	}

	public boolean isAppNameSpaceUsed() {
		return this.isAppNameSpaceUsed;
	}

	public void setAppNameSpaceUsed(boolean isAppNameSpaceUsed) {
		this.isAppNameSpaceUsed = isAppNameSpaceUsed;
	}

	public boolean isToolsNameSpaceUsed() {
		return this.isToolsNameSpaceUsed;
	}

	public void setToolsNameSpaceUsed(boolean isToolsNameSpaceUsed) {
		this.isToolsNameSpaceUsed = isToolsNameSpaceUsed;
	}

	public String getCode() {
		ViewModel view = getView();
		if (view == null) {
			return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		}
		return view.getCode("", this);
	}

	public HashMap<String, String> getIdentifiableView() {
		return this.identifiableView;
	}

	public void setIdentifiableView(HashMap<String, String> identifiableView) {
		this.identifiableView = identifiableView;
	}
}
