/*
 * This file is part of Android AppStudio [https://github.com/Innovative-CST/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.icst.android.appstudio.extensions.basicvariables;

import com.icst.android.appstudio.ImageUtils;
import com.icst.android.appstudio.block.enums.InputTypes;
import com.icst.android.appstudio.block.model.VariableModel;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
