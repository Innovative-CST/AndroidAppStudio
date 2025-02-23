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

package com.icst.android.appstudio.block.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.icst.android.appstudio.block.tag.AdditionalCodeHelperTag;
import com.icst.android.appstudio.block.utils.ArrayUtils;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;

public class Event implements Serializable, Cloneable {
	public static final long serialVersionUID = 1L;

	private String name;
	private String title;
	private String description;
	private String rawCode;
	private String eventReplacer;
	private String eventReplacerKey;
	private BlockModel eventTopBlock;
	private ArrayList<BlockModel> blockModels;
	private AdditionalCodeHelperTag[] additionalTags;
	private String[] classes;
	private String[] classesImports;
	private String[] holderName;
	private String[] extension;
	private String createInHolderName;
	private byte[] icon;
	private boolean applyColorFilter;
	private boolean isDirectFileEvent;
	private boolean enableEdit;
	private boolean enableRootBlocksDrag;
	private boolean enableRootBlocksValueEditing;
	private boolean enableRootBlocksSubBlockEditing;

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

	public String getRawCode() {
		return this.rawCode;
	}

	public void setRawCode(String rawCode) {
		this.rawCode = rawCode;
	}

	public String getEventReplacer() {
		return this.eventReplacer;
	}

	public void setEventReplacer(String eventReplacer) {
		this.eventReplacer = eventReplacer;
	}

	public String getEventReplacerKey() {
		return this.eventReplacerKey;
	}

	public void setEventReplacerKey(String eventReplacerKey) {
		this.eventReplacerKey = eventReplacerKey;
	}

	public boolean getEnableEdit() {
		return this.enableEdit;
	}

	public void setEnableEdit(boolean enableEdit) {
		this.enableEdit = enableEdit;
	}

	public boolean getEnableRootBlocksDrag() {
		return this.enableRootBlocksDrag;
	}

	public void setEnableRootBlocksDrag(boolean enableRootBlocksDrag) {
		this.enableRootBlocksDrag = enableRootBlocksDrag;
	}

	public boolean getEnableRootBlocksValueEditing() {
		return this.enableRootBlocksValueEditing;
	}

	public void setEnableRootBlocksValueEditing(boolean enableRootBlocksValueEditing) {
		this.enableRootBlocksValueEditing = enableRootBlocksValueEditing;
	}

	public BlockModel getEventTopBlock() {
		return this.eventTopBlock;
	}

	public void setEventTopBlock(BlockModel eventTopBlock) {
		this.eventTopBlock = eventTopBlock;
	}

	public ArrayList<BlockModel> getBlockModels() {
		return this.blockModels;
	}

	public void setBlockModels(ArrayList<BlockModel> blockModels) {
		this.blockModels = blockModels;
	}

	public boolean getEnableRootBlocksSubBlockEditing() {
		return this.enableRootBlocksSubBlockEditing;
	}

	public void setEnableRootBlocksSubBlockEditing(boolean enableRootBlocksSubBlockEditing) {
		this.enableRootBlocksSubBlockEditing = enableRootBlocksSubBlockEditing;
	}

	public ArrayList<AdditionalCodeHelperTag> getAdditionalTagsUsed() {
		ArrayList<AdditionalCodeHelperTag> tags = new ArrayList<AdditionalCodeHelperTag>();

		// Add this event code helper tag to tags
		if (getAdditionalTags() != null) {
			for (int i = 0; i < getAdditionalTags().length; ++i) {
				tags.add(getAdditionalTags()[i]);
			}
		}

		if (getBlockModels() == null) {
			return tags;
		}

		for (int blocksCount = 0; blocksCount < getBlockModels().size(); ++blocksCount) {
			BlockModel block = getBlockModels().get(blocksCount);
			tags.addAll(block.getAdditionalTagsOfBlock());
		}

		return tags;
	}

	public String getCode(HashMap<String, Object> variables) {
		StringBuilder generatedCode = new StringBuilder();
		if (getBlockModels() == null) {
			String returnEmptyEventCode = new String(getRawCode());
			returnEmptyEventCode = RawCodeReplacer.removeAndroidAppStudioString(getEventReplacerKey(),
					returnEmptyEventCode);
			return returnEmptyEventCode;
		}

		for (int blocksCount = 0; blocksCount < getBlockModels().size(); ++blocksCount) {
			if (blocksCount != 0)
				generatedCode.append("\n");
			generatedCode.append(getBlockModels().get(blocksCount).getCode(variables));
		}

		String eventCode = new String(getRawCode());

		// Get space for formatting....
		String formatter = null;
		String[] lines = eventCode.split("\n");
		for (String line : lines) {
			if (line.contains(RawCodeReplacer.getReplacer(getEventReplacerKey(), getEventReplacer()))) {
				formatter = line.substring(
						0,
						line.indexOf(
								RawCodeReplacer.getReplacer(getEventReplacerKey(), getEventReplacer())));
			}
		}

		StringBuilder formattedGeneratedCode = new StringBuilder();

		String[] generatedCodeLines = generatedCode.toString().split("\n");

		for (int generatedCodeLinePosition = 0; generatedCodeLinePosition < generatedCodeLines.length; ++generatedCodeLinePosition) {

			if (formatter != null) {
				if (generatedCodeLinePosition != 0)
					formattedGeneratedCode.append(formatter);
			}

			formattedGeneratedCode.append(generatedCodeLines[generatedCodeLinePosition]);
			if (generatedCodeLinePosition != (generatedCodeLines.length - 1)) {
				formattedGeneratedCode.append("\n");
			}
		}

		eventCode = eventCode.replace(
				RawCodeReplacer.getReplacer(getEventReplacerKey(), getEventReplacer()),
				formattedGeneratedCode.toString());

		return eventCode;
	}

	@Override
	public Event clone() {
		Event event = new Event();
		event.setName(getName() != null ? new String(getName()) : null);
		event.setTitle(getTitle() != null ? new String(getTitle()) : null);
		event.setDescription(getDescription() != null ? new String(getDescription()) : null);
		event.setRawCode(getRawCode() != null ? new String(getRawCode()) : null);
		event.setEventReplacer(getEventReplacer() != null ? new String(getEventReplacer()) : null);
		event.setCreateInHolderName(
				getCreateInHolderName() != null ? new String(getCreateInHolderName()) : null);
		event.setEventReplacerKey(
				getEventReplacerKey() != null ? new String(getEventReplacerKey()) : null);
		event.setEventTopBlock(getEventTopBlock() != null ? getEventTopBlock().clone() : null);
		event.setAdditionalTags(ArrayUtils.clone(getAdditionalTags()));
		event.setClasses(ArrayUtils.clone(getClasses()));
		event.setClassesImports(ArrayUtils.clone(getClassesImports()));
		event.setHolderName(ArrayUtils.clone(getHolderName()));
		event.setExtension(ArrayUtils.clone(getExtension()));
		event.setDirectFileEvent(new Boolean(isDirectFileEvent()));
		event.setEnableEdit(new Boolean(getEnableEdit()));
		event.setEnableRootBlocksDrag(new Boolean(getEnableRootBlocksDrag()));
		event.setEnableRootBlocksValueEditing(new Boolean(getEnableRootBlocksValueEditing()));
		event.setEnableRootBlocksSubBlockEditing(new Boolean(getEnableRootBlocksSubBlockEditing()));
		event.setApplyColorFilter(new Boolean(getApplyColorFilter()));

		if (getIcon() != null) {
			event.setIcon(new byte[this.icon.length]);
			System.arraycopy(getIcon(), 0, event.getIcon(), 0, getIcon().length);
		}

		if (getBlockModels() != null) {
			ArrayList<BlockModel> clonedBlockModels = new ArrayList<BlockModel>();
			for (int i = 0; i < getBlockModels().size(); ++i) {
				clonedBlockModels.add(getBlockModels().get(i).clone());
			}

			event.setBlockModels(clonedBlockModels);
		} else
			event.setBlockModels(null);

		return event;
	}

	public AdditionalCodeHelperTag[] getAdditionalTags() {
		return this.additionalTags;
	}

	public void setAdditionalTags(AdditionalCodeHelperTag[] additionalTags) {
		this.additionalTags = additionalTags;
	}

	public String[] getClasses() {
		return this.classes;
	}

	public void setClasses(String[] classes) {
		this.classes = classes;
	}

	public String[] getHolderName() {
		return this.holderName;
	}

	public void setHolderName(String[] holderName) {
		this.holderName = holderName;
	}

	public String[] getExtension() {
		return this.extension;
	}

	public void setExtension(String[] extension) {
		this.extension = extension;
	}

	public String getCreateInHolderName() {
		return this.createInHolderName;
	}

	public void setCreateInHolderName(String createInHolderName) {
		this.createInHolderName = createInHolderName;
	}

	public String[] getClassesImports() {
		return this.classesImports;
	}

	public void setClassesImports(String[] classesImports) {
		this.classesImports = classesImports;
	}

	public boolean isDirectFileEvent() {
		return this.isDirectFileEvent;
	}

	public void setDirectFileEvent(boolean isDirectFileEvent) {
		this.isDirectFileEvent = isDirectFileEvent;
	}

	public byte[] getIcon() {
		return this.icon;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	public boolean getApplyColorFilter() {
		return this.applyColorFilter;
	}

	public void setApplyColorFilter(boolean applyColorFilter) {
		this.applyColorFilter = applyColorFilter;
	}
}
