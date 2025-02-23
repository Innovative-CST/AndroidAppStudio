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

package com.icst.android.appstudio.extensions.basicvariables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.icst.android.appstudio.ImageUtils;
import com.icst.android.appstudio.block.enums.InputTypes;
import com.icst.android.appstudio.block.model.VariableModel;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;

public class BasicVariables {

	public static ArrayList<VariableModel> getVariables() {
		ArrayList<VariableModel> var = new ArrayList<VariableModel>();
		var.add(getIntVariable());
		var.add(getStaticIntVariable());
		var.add(getStringVariable());
		var.add(getStaticStringVariable());
		return var;
	}

	public static VariableModel getIntVariable() {
		VariableModel variable = new VariableModel();
		variable.setAccessModifier(VariableModel.ACCESS_MODIFIER_PRIVATE);
		variable.setVariableTitle("Integer");
		variable.setVariableType("int");
		variable.setCanInitializedGlobally(true);
		variable.setVariableInitializerCode(RawCodeReplacer.getReplacer("variable", "intVal"));
		variable.setNonFixedVariableName(RawCodeReplacer.getReplacer("variable", "variableName"));
		variable.setApplyColorFilter(true);

		try {
			variable.setIcon(ImageUtils.convertImageToByteArray("images/numeric.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		HashMap<String, String> titles = new HashMap<String, String>();
		titles.put("intVal", "Integer Value");
		variable.setVariableTitles(titles);

		HashMap<String, Integer> valuesType = new HashMap<String, Integer>();
		valuesType.put("intVal", InputTypes.INPUT_TYPE_INT.getInputType());
		variable.setInputType(valuesType);
		return variable;
	}

	public static VariableModel getStaticIntVariable() {
		VariableModel variable = new VariableModel();
		variable.setAccessModifier(VariableModel.ACCESS_MODIFIER_PRIVATE);
		variable.setVariableTitle("Integer");
		variable.setVariableType("int");
		variable.setMustBeGloballyIntialized(true);
		variable.setCanInitializedGlobally(true);
		variable.setVariableInitializerCode(RawCodeReplacer.getReplacer("variable", "intVal"));
		variable.setNonFixedVariableName(RawCodeReplacer.getReplacer("variable", "variableName"));
		variable.setIsStaticVariable(true);
		variable.setApplyColorFilter(true);

		try {
			variable.setIcon(ImageUtils.convertImageToByteArray("images/numeric.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		HashMap<String, String> titles = new HashMap<String, String>();
		titles.put("intVal", "Integer Value");
		variable.setVariableTitles(titles);

		HashMap<String, Integer> valuesType = new HashMap<String, Integer>();
		valuesType.put("intVal", InputTypes.INPUT_TYPE_INT.getInputType());
		variable.setInputType(valuesType);
		return variable;
	}

	public static VariableModel getStringVariable() {
		VariableModel variable = new VariableModel();
		variable.setAccessModifier(VariableModel.ACCESS_MODIFIER_PRIVATE);
		variable.setVariableTitle("String");
		variable.setVariableType("String");
		variable.setCanInitializedGlobally(true);
		variable.setVariableInitializerCode(
				"\"".concat(RawCodeReplacer.getReplacer("variable", "stringValue")).concat("\""));
		variable.setNonFixedVariableName(RawCodeReplacer.getReplacer("variable", "variableName"));
		variable.setApplyColorFilter(true);

		try {
			variable.setIcon(ImageUtils.convertImageToByteArray("images/alphabetical.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		HashMap<String, String> titles = new HashMap<String, String>();
		titles.put("stringValue", "Enter String Value");
		variable.setVariableTitles(titles);

		HashMap<String, Integer> valuesType = new HashMap<String, Integer>();
		valuesType.put("stringValue", InputTypes.INPUT_TYPE_STRING.getInputType());
		variable.setInputType(valuesType);
		return variable;
	}

	public static VariableModel getStaticStringVariable() {
		VariableModel variable = new VariableModel();
		variable.setAccessModifier(VariableModel.ACCESS_MODIFIER_PRIVATE);
		variable.setVariableTitle("String");
		variable.setVariableType("String");
		variable.setCanInitializedGlobally(true);
		variable.setVariableInitializerCode(
				"\"".concat(RawCodeReplacer.getReplacer("variable", "stringValue")).concat("\""));
		variable.setNonFixedVariableName(RawCodeReplacer.getReplacer("variable", "variableName"));
		variable.setIsStaticVariable(true);
		variable.setApplyColorFilter(true);

		try {
			variable.setIcon(ImageUtils.convertImageToByteArray("images/alphabetical.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		HashMap<String, String> titles = new HashMap<String, String>();
		titles.put("stringValue", "Enter String Value");
		variable.setVariableTitles(titles);

		HashMap<String, Integer> valuesType = new HashMap<String, Integer>();
		valuesType.put("stringValue", InputTypes.INPUT_TYPE_STRING.getInputType());
		variable.setInputType(valuesType);
		return variable;
	}
}
