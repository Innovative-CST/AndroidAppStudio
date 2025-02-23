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

package com.icst.android.appstudio.builtin.blockfield;

import java.util.HashMap;

import com.icst.android.appstudio.block.model.BlockValueFieldModel;
import com.icst.android.appstudio.models.ProjectModel;

public class PackageNameBlockField extends BlockValueFieldModel {
	public PackageNameBlockField() {
		setFieldType(BlockValueFieldModel.FieldType.FIELD_EXTENSION_VIEW_ONLY);
		setEnableEdit(false);
	}

	@Override
	public String getCode(HashMap<String, Object> variables) {
		if (variables.containsKey("ProjectModel")) {
			return ((ProjectModel) variables.get("ProjectModel")).getPackageName();
		}

		return "null";
	}

	@Override
	public PackageNameBlockField clone() {
		PackageNameBlockField clone = new PackageNameBlockField();
		clone.setReplacer(getReplacer() != null ? getReplacer() : null);
		return clone;
	}
}
