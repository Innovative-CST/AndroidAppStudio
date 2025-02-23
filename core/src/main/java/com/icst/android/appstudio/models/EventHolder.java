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

package com.icst.android.appstudio.models;

import java.io.File;
import java.io.Serializable;

public class EventHolder implements Serializable, Cloneable {
	public static final long serialVersionUID = 8L;

	private String holderName;
	private byte[] icon;
	private File filePath;
	private boolean isBuiltInEvents;
	private boolean disableNewEvents;

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

	public boolean isBuiltInEvents() {
		return this.isBuiltInEvents;
	}

	public void setBuiltInEvents(boolean isBuiltInEvents) {
		this.isBuiltInEvents = isBuiltInEvents;
	}

	public File getFilePath() {
		return this.filePath;
	}

	public void setFilePath(File filePath) {
		this.filePath = filePath;
	}

	public boolean getDisableNewEvents() {
		return this.disableNewEvents;
	}

	public void setDisableNewEvents(boolean disableNewEvents) {
		this.disableNewEvents = disableNewEvents;
	}

	@Override
	protected EventHolder clone() {
		EventHolder eventHolder = new EventHolder();
		eventHolder.setHolderName(new String(getHolderName() != null ? getHolderName() : ""));
		eventHolder.setBuiltInEvents(new Boolean(isBuiltInEvents()));
		eventHolder.setDisableNewEvents(new Boolean(getDisableNewEvents()));
		if (getIcon() != null) {
			eventHolder.setIcon(new byte[this.icon.length]);
			System.arraycopy(getIcon(), 0, eventHolder.getIcon(), 0, this.icon.length);
		}
		return eventHolder;
	}
}
