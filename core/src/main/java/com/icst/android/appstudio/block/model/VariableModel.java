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

package com.icst.android.appstudio.block.model;

import com.icst.android.appstudio.block.utils.RawCodeReplacer;
import com.icst.android.appstudio.vieweditor.models.LayoutModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VariableModel implements Serializable {
  public static final long serialVersionUID = 24L;

  public static final int ACCESS_MODIFIER_PRIVATE = 1;
  public static final int ACCESS_MODIFIER_PROTECTED = 2;
  public static final int ACCESS_MODIFIER_PUBLIC = 3;

  public static final int VARIABLE = 0;
  public static final int LAYOUT = 1;

  private int accessModifier;
  private String variableTitle;
  private String variableType;
  private String variableName;
  private String nonFixedVariableName;
  private String variableInitializerCode;
  private String[] variableImports;
  private String[] fileExtensions;
  private boolean mustBeGloballyIntialized;
  private boolean isInitializedGlobally;
  private boolean canInitializedGlobally;
  private boolean isStaticVariable;
  private boolean isFinalVariable;
  private boolean applyColorFilter;
  private byte[] icon;
  private VariableModel[] requiredVariables;
  private HashMap<String, String> variableTitles;
  private HashMap<String, String> variableValues;
  private HashMap<String, Integer> inputType;

  public String getLayoutDefCode(LayoutModel layout) {
    StringBuilder code = new StringBuilder();
    int i = 0;
    for (Map.Entry<String, String> entry : layout.getIdentifiableView().entrySet()) {
      if (i != 0) {
        code.append("\n");
      }
      String id = entry.getKey();
      String viewName = entry.getValue();

      VariableModel variable = new VariableModel();
      variable.setAccessModifier(getAccessModifier());
      variable.setVariableType(viewName);
      variable.setNonFixedVariableName(
          new String(getVariableName())
              .concat("_")
              .concat(RawCodeReplacer.getReplacer("variable", "variableName")));
      variable.setVariableName(id);
      code.append(variable.getDefCode());
      i = i + 1;
    }

    return code.toString();
  }

  @SuppressWarnings("deprecation")
  public VariableModel clone() {
    VariableModel variable = new VariableModel();
    variable.accessModifier = new Integer(this.accessModifier);
    variable.variableTitle = this.variableTitle == null ? null : new String(this.variableTitle);
    variable.variableType = this.variableType == null ? null : new String(this.variableType);
    variable.variableName = this.variableName == null ? null : new String(this.variableName);
    variable.nonFixedVariableName =
        this.nonFixedVariableName == null ? null : new String(this.nonFixedVariableName);
    variable.variableInitializerCode =
        this.variableInitializerCode == null ? null : new String(this.variableInitializerCode);

    if (this.variableImports == null) {
      variable.variableImports = null;
    } else {
      variable.variableImports = new String[this.variableImports.length];
      for (int i = 0; i < this.variableImports.length; i++) {
        variable.variableImports[i] =
            this.variableImports[i] == null ? null : new String(this.variableImports[i]);
      }
    }

    if (this.fileExtensions == null) {
      variable.fileExtensions = null;
    } else {
      variable.fileExtensions = new String[this.fileExtensions.length];
      for (int i = 0; i < this.fileExtensions.length; i++) {
        variable.fileExtensions[i] =
            this.fileExtensions[i] == null ? null : new String(this.fileExtensions[i]);
      }
    }

    variable.mustBeGloballyIntialized = new Boolean(this.mustBeGloballyIntialized);
    variable.isInitializedGlobally = new Boolean(this.isInitializedGlobally);
    variable.canInitializedGlobally = new Boolean(this.canInitializedGlobally);
    variable.isStaticVariable = new Boolean(this.isStaticVariable);
    variable.isFinalVariable = new Boolean(this.isFinalVariable);
    variable.applyColorFilter = new Boolean(this.applyColorFilter);

    if (this.icon == null) {
      variable.icon = null;
    } else {
      variable.icon = new byte[this.icon.length];
      System.arraycopy(this.icon, 0, variable.icon, 0, this.icon.length);
    }

    if (this.requiredVariables == null) {
      variable.requiredVariables = null;
    } else {
      variable.requiredVariables = new VariableModel[this.requiredVariables.length];
      for (int i = 0; i < this.requiredVariables.length; i++) {
        variable.requiredVariables[i] =
            this.requiredVariables[i] == null ? null : this.requiredVariables[i].clone();
      }
    }

    if (this.variableTitles == null) {
      variable.variableTitles = null;
    } else {
      variable.variableTitles = new HashMap<>(this.variableTitles);
    }

    if (this.variableValues == null) {
      variable.variableValues = null;
    } else {
      variable.variableValues = new HashMap<>(this.variableValues);
    }

    if (this.inputType == null) {
      variable.inputType = null;
    } else {
      variable.inputType = new HashMap<>(this.inputType);
    }

    return variable;
  }

  public String getDefCode() {
    StringBuilder code = new StringBuilder();

    if (getRequiredVariables() != null) {
      for (int i = 0; i < getRequiredVariables().length; ++i) {
        if (i != 0) {
          code.append("\n");
        }

        VariableModel variable = getRequiredVariables()[i];
        code.append(getDefCode(getVariableValues(), this));
      }

      if (getRequiredVariables().length > 0) {
        code.append("\n");
      }

      if (variableType == null) {
        return code.toString();
      }
    }

    String accessModifier =
        switch (getAccessModifier()) {
          case ACCESS_MODIFIER_PRIVATE -> "private";
          case ACCESS_MODIFIER_PROTECTED -> "protected";
          case ACCESS_MODIFIER_PUBLIC -> "public";
          default -> "";
        };

    code.append(accessModifier);
    code.append(" ");

    if (isStaticVariable) {
      code.append("static");
      code.append(" ");
    }

    if (isFinalVariable) {
      code.append("final");
      code.append(" ");
    }

    code.append(variableType);
    code.append(" ");
    if (getNonFixedVariableName() == null) {
      code.append(variableName);
    } else {
      code.append(
          getNonFixedVariableName()
              .replace(RawCodeReplacer.getReplacer("variable", "variableName"), variableName));
    }

    if (isInitializedGlobally) {
      if (getVariableInitializerCode() == null) {
        code.append(";");
      } else {
        code.append(" = ");

        String init = new String(getVariableInitializerCode());

        for (Map.Entry<String, String> entry : variableValues.entrySet()) {
          String key = entry.getKey();
          String value = entry.getValue();
          init = init.replace(RawCodeReplacer.getReplacer("variable", key), value);
        }

        code.append(init);
      }
      code.append(";");
    } else {
      code.append(";");
    }

    return code.toString();
  }

  private String getDefCode(
      HashMap<String, String> variableValues, VariableModel parentVariableModel) {
    StringBuilder code = new StringBuilder();

    String accessModifier =
        switch (getAccessModifier()) {
          case ACCESS_MODIFIER_PRIVATE -> "private";
          case ACCESS_MODIFIER_PROTECTED -> "protected";
          case ACCESS_MODIFIER_PUBLIC -> "public";
          default -> "";
        };

    code.append(accessModifier);
    code.append(" ");

    if (isStaticVariable) {
      code.append("static");
      code.append(" ");
    }

    if (isFinalVariable) {
      code.append("final");
      code.append(" ");
    }

    code.append(variableType);
    code.append(" ");
    if (getNonFixedVariableName() == null) {
      code.append(variableName);
    } else {
      code.append(
          getNonFixedVariableName()
              .replace(RawCodeReplacer.getReplacer("variable", "variableName"), variableName));
    }

    if (isInitializedGlobally) {
      if (getVariableInitializerCode() == null) {
        code.append(";");
      } else {
        code.append(" = ");

        String init = new String(getVariableInitializerCode());

        for (Map.Entry<String, String> entry : variableValues.entrySet()) {
          String key = entry.getKey();
          String value = entry.getValue();
          init = init.replace(RawCodeReplacer.getReplacer("variable", key), value);
        }

        code.append(init);
      }
      code.append(";");
    } else {
      code.append(";");
    }

    return code.toString();
  }

  public int getAccessModifier() {
    return this.accessModifier;
  }

  public void setAccessModifier(int accessModifier) {
    this.accessModifier = accessModifier;
  }

  public String getVariableTitle() {
    return this.variableTitle;
  }

  public void setVariableTitle(String variableTitle) {
    this.variableTitle = variableTitle;
  }

  public String getVariableType() {
    return this.variableType;
  }

  public void setVariableType(String variableType) {
    this.variableType = variableType;
  }

  public String getVariableName() {
    return this.variableName;
  }

  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }

  public String getNonFixedVariableName() {
    return this.nonFixedVariableName;
  }

  public void setNonFixedVariableName(String nonFixedVariableName) {
    this.nonFixedVariableName = nonFixedVariableName;
  }

  public String getVariableInitializerCode() {
    return this.variableInitializerCode;
  }

  public void setVariableInitializerCode(String variableInitializerCode) {
    this.variableInitializerCode = variableInitializerCode;
  }

  public String[] getVariableImports() {
    return this.variableImports;
  }

  public void setVariableImports(String[] variableImports) {
    this.variableImports = variableImports;
  }

  public String[] getFileExtensions() {
    return this.fileExtensions;
  }

  public void setFileExtensions(String[] fileExtensions) {
    this.fileExtensions = fileExtensions;
  }

  public boolean getMustBeGloballyIntialized() {
    return this.mustBeGloballyIntialized;
  }

  public void setMustBeGloballyIntialized(boolean mustBeGloballyIntialized) {
    this.mustBeGloballyIntialized = mustBeGloballyIntialized;
  }

  public boolean getIsInitializedGlobally() {
    return this.isInitializedGlobally;
  }

  public void setIsInitializedGlobally(boolean isInitializedGlobally) {
    this.isInitializedGlobally = isInitializedGlobally;
  }

  public boolean getCanInitializedGlobally() {
    return this.canInitializedGlobally;
  }

  public void setCanInitializedGlobally(boolean canInitializedGlobally) {
    this.canInitializedGlobally = canInitializedGlobally;
  }

  public boolean getIsStaticVariable() {
    return this.isStaticVariable;
  }

  public void setIsStaticVariable(boolean isStaticVariable) {
    this.isStaticVariable = isStaticVariable;
  }

  public boolean getIsFinalVariable() {
    return this.isFinalVariable;
  }

  public void setIsFinalVariable(boolean isFinalVariable) {
    this.isFinalVariable = isFinalVariable;
  }

  public byte[] getIcon() {
    return this.icon;
  }

  public void setIcon(byte[] icon) {
    this.icon = icon;
  }

  public VariableModel[] getRequiredVariables() {
    return this.requiredVariables;
  }

  public void setRequiredVariables(VariableModel[] requiredVariables) {
    this.requiredVariables = requiredVariables;
  }

  public HashMap<String, String> getVariableTitles() {
    return this.variableTitles;
  }

  public void setVariableTitles(HashMap<String, String> variableTitles) {
    this.variableTitles = variableTitles;
  }

  public HashMap<String, String> getVariableValues() {
    return this.variableValues;
  }

  public void setVariableValues(HashMap<String, String> variableValues) {
    this.variableValues = variableValues;
  }

  public HashMap<String, Integer> getInputType() {
    return this.inputType;
  }

  public void setInputType(HashMap<String, Integer> inputType) {
    this.inputType = inputType;
  }

  public boolean getApplyColorFilter() {
    return this.applyColorFilter;
  }

  public void setApplyColorFilter(boolean applyColorFilter) {
    this.applyColorFilter = applyColorFilter;
  }
}
