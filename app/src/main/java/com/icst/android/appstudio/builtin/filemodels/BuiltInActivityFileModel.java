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

package com.icst.android.appstudio.builtin.filemodels;

import com.icst.android.appstudio.block.model.JavaFileModel;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;

public final class BuiltInActivityFileModel {
	public static JavaFileModel getActivityFileModel(String className) {
		JavaFileModel javaClass = new JavaFileModel();
		javaClass.setFileName(className);
		javaClass.setFileExtension("java");
		javaClass.setExtendingClass("AppCompatActivity");
		javaClass.setExtendingClassImport("androidx.appcompat.app.AppCompatActivity");
		javaClass.setClassType(JavaFileModel.SIMPLE_JAVA_CLASS);
		javaClass.setReplacerKey(className.concat(".file"));
		StringBuilder rawCode = new StringBuilder();
		rawCode.append("package ");
		rawCode.append(RawCodeReplacer.getReplacer(javaClass.getReplacerKey(), "filePackage name"));
		rawCode.append(";");
		rawCode.append("\n");
		rawCode.append("\n");
		rawCode.append(RawCodeReplacer.getReplacer(javaClass.getReplacerKey(), "imports"));
		rawCode.append("\n");
		rawCode.append("\n");
		rawCode.append("public class ");
		rawCode.append(RawCodeReplacer.getReplacer("$FileName"));
		rawCode.append(RawCodeReplacer.getReplacer(javaClass.getReplacerKey(), "inheritence"));
		rawCode.append("{");
		rawCode.append("\n\n");
		rawCode.append("\t");
		rawCode.append(RawCodeReplacer.getReplacer(javaClass.getReplacerKey(), "static-variables"));
		rawCode.append("\n\n");
		rawCode.append("\t");
		rawCode.append(RawCodeReplacer.getReplacer(javaClass.getReplacerKey(), "variables"));
		rawCode.append("\n\n");
		rawCode.append("\t");
		rawCode.append(RawCodeReplacer.getReplacer(javaClass.getReplacerKey(), "layoutVariables"));
		rawCode.append("\n");
		rawCode.append("\n");
		rawCode.append("\t");
		rawCode.append(RawCodeReplacer.getReplacer(javaClass.getReplacerKey(), "directEvents"));
		rawCode.append("\n");
		rawCode.append("\n");
		rawCode.append("}");
		javaClass.setRawCode(rawCode.toString());
		return javaClass;
	}
}
