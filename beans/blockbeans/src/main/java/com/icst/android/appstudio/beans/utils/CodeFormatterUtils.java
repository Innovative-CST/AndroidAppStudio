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

package com.icst.android.appstudio.beans.utils;

import com.icst.android.appstudio.beans.ValueInputBlockElementBean;

public final class CodeFormatterUtils {

	public static String formatCode(
			String codeSyntax, ValueInputBlockElementBean mValueInputBlockElementBean) {
		return codeSyntax.replace(
				getKeySyntaxString(mValueInputBlockElementBean.getKey()),
				mValueInputBlockElementBean.getValue());
	}

	public static String getKeySyntaxString(String key) {
		return new String("<CodeKey : ").concat(key).concat(">");
	}

	public static String addIntendation(String code, int intendation) {
		StringBuilder intendedCode = new StringBuilder();
		String[] lines = code.split("\n");
		for (int i = 0; i < lines.length; ++i) {
			String line = lines[i];
			if (i != 0) {
				intendedCode.append("\t".repeat(intendation));
			}
			intendedCode.append(line);
			intendedCode.append("\n");
		}
		return intendedCode.toString();
	}

	public static int getIntendation(String codeSyntax, String item) {

		String[] codeLines = codeSyntax.split("\n");
		for (String line : codeLines) {
			if (line.contains(item)) {
				int spaceCount = line.indexOf(item);
				return spaceCount;
			}
		}

		return 0;
	}
}
