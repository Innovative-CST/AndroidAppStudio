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

package com.icst.android.appstudio.block.tag;

import java.io.Serializable;

import com.icst.android.appstudio.block.utils.ArrayUtils;

public class BlockModelTag implements Serializable {
	public static final long serialVersionUID = 13L;

	public String[] supportedFileExtensions;
	public String[] notSupportedFileExtensions;
	public String[] supportedEvents;
	public String[] notSupportedEvents;
	public AdditionalCodeHelperTag[] additionalTags;
	public boolean showInAllBlocksDeveloperMode;
	public boolean isForDeveloperOnly;

	public String[] getSupportedFileExtensions() {
		return this.supportedFileExtensions;
	}

	public void setSupportedFileExtensions(String[] supportedFileExtensions) {
		this.supportedFileExtensions = supportedFileExtensions;
	}

	public String[] getNotSupportedFileExtensions() {
		return this.notSupportedFileExtensions;
	}

	public void setNotSupportedFileExtensions(String[] notSupportedFileExtensions) {
		this.notSupportedFileExtensions = notSupportedFileExtensions;
	}

	public String[] getSupportedEvents() {
		return this.supportedEvents;
	}

	public void setSupportedEvents(String[] supportedEvents) {
		this.supportedEvents = supportedEvents;
	}

	public String[] getNotSupportedEvents() {
		return this.notSupportedEvents;
	}

	public void setNotSupportedEvents(String[] notSupportedEvents) {
		this.notSupportedEvents = notSupportedEvents;
	}

	public AdditionalCodeHelperTag[] getAdditionalTags() {
		return this.additionalTags;
	}

	public void setAdditionalTags(AdditionalCodeHelperTag[] additionalTags) {
		this.additionalTags = additionalTags;
	}

	public boolean getShowInAllBlocksDeveloperMode() {
		return this.showInAllBlocksDeveloperMode;
	}

	public void setShowInAllBlocksDeveloperMode(boolean showInAllBlocksDeveloperMode) {
		this.showInAllBlocksDeveloperMode = showInAllBlocksDeveloperMode;
	}

	public boolean isForDeveloperOnly() {
		return this.isForDeveloperOnly;
	}

	public void setForDeveloperOnly(boolean isForDeveloperOnly) {
		this.isForDeveloperOnly = isForDeveloperOnly;
	}

	public BlockModelTag clone() {
		BlockModelTag clone = new BlockModelTag();

		clone.setSupportedFileExtensions(
				getSupportedFileExtensions() == null
						? null
						: ArrayUtils.clone(getSupportedFileExtensions()));

		clone.setNotSupportedFileExtensions(
				getNotSupportedFileExtensions() == null
						? null
						: ArrayUtils.clone(getNotSupportedFileExtensions()));

		clone.setSupportedEvents(
				getSupportedEvents() == null ? null : ArrayUtils.clone(getSupportedEvents()));

		clone.setNotSupportedEvents(
				getNotSupportedEvents() == null ? null : ArrayUtils.clone(getNotSupportedEvents()));

		clone.setShowInAllBlocksDeveloperMode(new Boolean(getShowInAllBlocksDeveloperMode()));

		clone.setForDeveloperOnly(new Boolean(isForDeveloperOnly()));

		clone.setAdditionalTags(ArrayUtils.clone(getAdditionalTags()));
		return clone;
	}
}
