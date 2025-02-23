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
import java.util.HashMap;

import com.icst.android.appstudio.block.tag.AdditionalCodeHelperTag;
import com.icst.android.appstudio.block.utils.ArrayUtils;

public class BlockValueFieldModel extends BlockFieldModel implements Serializable, Cloneable {
	public static final long serialVersionUID = 11L;

	private boolean enableEdit;
	private String acceptors[];
	private String replacer;
	private BlockModel blockModel;
	private AdditionalCodeHelperTag[] additionalTags;
	private int fieldType;
	private String pattern; // Only works if fieldType is pattern validator.

	public final class FieldType {
		public static final int FIELD_TYPE_NOT_SET = 0;
		public static final int FIELD_INPUT_ONLY = 1;
		public static final int FIELD_EXTENSION_VIEW_ONLY = 2;
		public static final int FIELD_BOOLEAN = 3;
		public static final int FIELD_NUMBER = 4;
	}

	public String[] getAcceptors() {
		return this.acceptors;
	}

	public void setAcceptors(String[] acceptors) {
		this.acceptors = acceptors;
	}

	public boolean isEnabledEdit() {
		return this.enableEdit;
	}

	public void setEnableEdit(boolean enableEdit) {
		this.enableEdit = enableEdit;
	}

	public BlockModel getBlockModel() {
		return this.blockModel;
	}

	public void setBlockModel(BlockModel blockModel) {
		if (blockModel != null) {
			setValue(null);
		}
		this.blockModel = blockModel;
	}

	public String getPattern() {
		return this.pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getReplacer() {
		return this.replacer;
	}

	public void setReplacer(String replacer) {
		this.replacer = replacer;
	}

	public int getFieldType() {
		return this.fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}

	@Override
	public void setValue(String value) {
		super.setValue(value);
		if (value != null) {
			setBlockModel(null);
		}
	}

	public String getCode(HashMap<String, Object> variables) {
		return getBlockModel() != null ? getBlockModel().getCode(variables) : getValue();
	}

	public AdditionalCodeHelperTag[] getAdditionalTags() {
		return this.additionalTags;
	}

	public void setAdditionalTags(AdditionalCodeHelperTag[] additionalTags) {
		this.additionalTags = additionalTags;
	}

	@Override
	public BlockValueFieldModel clone() {
		BlockValueFieldModel blockValueFieldModel = new BlockValueFieldModel();
		blockValueFieldModel.setValue(getValue() != null ? new String(getValue()) : null);
		blockValueFieldModel.setReplacer(getReplacer() != null ? getReplacer() : null);
		blockValueFieldModel.setEnableEdit(new Boolean(isEnabledEdit()));
		blockValueFieldModel.setBlockModel(getBlockModel() != null ? getBlockModel() : null);
		blockValueFieldModel.setFieldType(new Integer(getFieldType()));
		blockValueFieldModel.setPattern(getPattern() != null ? new String(getPattern()) : null);
		blockValueFieldModel.setAdditionalTags(ArrayUtils.clone(getAdditionalTags()));
		if (getAcceptors() != null) {
			String[] acceptors = new String[getAcceptors().length];

			for (int position = 0; position < getAcceptors().length; ++position) {
				acceptors[position] = new String(getAcceptors()[position]);
			}
			blockValueFieldModel.setAcceptors(acceptors);
		}
		return blockValueFieldModel;
	}
}
