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

package com.icst.android.appstudio.utils;

import java.util.ArrayList;

import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.model.VariableModel;

public class VariablesUtils {

	public static ArrayList<VariableModel> getAllVariables(FileModel file) {
		ArrayList<VariableModel> variables = ExtensionUtils.extractVariablesFromExtensions();
		ArrayList<VariableModel> result = new ArrayList<VariableModel>();

		for (int i = 0; i < variables.size(); ++i) {
			VariableModel variable = variables.get(i);

			if (variable.getVariableTitle() == null) {
				continue;
			}

			if (variable.getFileExtensions() != null) {
				String[] extensions = variable.getFileExtensions();
			}

			result.add(variable);
		}

		return result;
	}

	public static ArrayList<VariableModel> getInstanceVariables(FileModel file) {
		ArrayList<VariableModel> variables = getAllVariables(file);
		ArrayList<VariableModel> result = new ArrayList<VariableModel>();

		for (int i = 0; i < variables.size(); ++i) {
			if (!variables.get(i).getIsStaticVariable()) {
				result.add(variables.get(i));
			}
		}

		return result;
	}

	public static ArrayList<VariableModel> getStaticVariables(FileModel file) {
		ArrayList<VariableModel> variables = getAllVariables(file);
		ArrayList<VariableModel> result = new ArrayList<VariableModel>();

		for (int i = 0; i < variables.size(); ++i) {
			if (variables.get(i).getIsStaticVariable()) {
				result.add(variables.get(i));
			}
		}

		return result;
	}

	private static boolean containsString(String[] array, String str) {
		for (int i = 0; i < array.length; ++i) {
			if (str.equals(array[i])) {
				return true;
			}
		}
		return false;
	}
}
