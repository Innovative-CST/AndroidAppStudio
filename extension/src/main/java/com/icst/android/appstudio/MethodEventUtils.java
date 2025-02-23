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

package com.icst.android.appstudio;

import java.io.IOException;

import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.tag.AdditionalCodeHelperTag;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;

public class MethodEventUtils {
	public static Event buildMethodEvent(
			String name,
			String title,
			String description,
			String eventReplacer,
			String eventReplacerKey,
			String createInHolder,
			String iconName,
			String accessModifier,
			String returnType,
			String methodName,
			String[] parameters,
			String[] classes,
			String[] classesImports,
			String[] annotations,
			BlockModel block,
			AdditionalCodeHelperTag[] additionalCodeHelperTag,
			boolean directFileEvent,
			boolean enableEdit,
			boolean enableRootBlockDrag,
			boolean enableRootBlockEditing,
			boolean applyColorFilter,
			boolean isStatic,
			boolean isFinal) {
		Event event = new Event();
		event.setTitle(title);
		event.setName(name);
		event.setDescription(description);
		event.setEventReplacer(eventReplacer);
		event.setDirectFileEvent(directFileEvent);
		event.setEventReplacerKey(eventReplacerKey);
		event.setEnableEdit(enableEdit);
		event.setEnableRootBlocksDrag(enableRootBlockDrag);
		event.setEnableRootBlocksValueEditing(enableRootBlockEditing);
		event.setCreateInHolderName(createInHolder);
		event.setClasses(classes);
		event.setClassesImports(classesImports);
		try {
			event.setIcon(ImageUtils.convertImageToByteArray(iconName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		event.setEventTopBlock(block);
		event.setAdditionalTags(additionalCodeHelperTag);
		event.setApplyColorFilter(applyColorFilter);
		StringBuilder rawCode = new StringBuilder();

		if (annotations != null) {
			for (String annotation : annotations) {
				rawCode.append(annotation);
				rawCode.append("\n");
			}
		}

		if (accessModifier != null) {
			rawCode.append(accessModifier);
		}

		if (isStatic) {
			rawCode.append(" static");
		}
		if (isFinal) {
			rawCode.append(" final");
		}

		rawCode.append(" ");
		rawCode.append(returnType);
		rawCode.append(" ");
		rawCode.append(methodName);
		rawCode.append("(");
		if (parameters != null) {
			for (int i = 0; i < parameters.length; ++i) {
				if (i != 0) {
					rawCode.append(", ");
				}
				rawCode.append(parameters[i]);
			}
		}
		rawCode.append(") {\n\t");
		rawCode.append(
				RawCodeReplacer.getReplacer(event.getEventReplacerKey(), event.getEventReplacer()));
		rawCode.append("\n}");
		event.setRawCode(rawCode.toString());

		return event;
	}
}
