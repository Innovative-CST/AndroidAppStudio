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

import com.icst.android.appstudio.block.tag.AdditionalCodeHelperTag;
import com.icst.android.appstudio.block.tag.ImportTag;
import com.icst.android.appstudio.block.utils.ArrayUtils;
import com.icst.android.appstudio.block.utils.RawCodeReplacer;
import com.icst.android.appstudio.vieweditor.models.LayoutModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class JavaFileModel extends FileModel implements Serializable {
  public static final long serialVersionUID = 20L;

  private int classType;
  private String extendingClass;
  private String extendingClassImport;
  private String[] implementingInterface;
  private String[] implementingInterfaceImports;
  private ArrayList<VariableModel> layouts;
  private ArrayList<String> layoutsName;

  public static final int SIMPLE_JAVA_CLASS = 0;
  public static final int JAVA_ACTIVITY = 1;

  public String getCode(
      String packageName,
      File layoutDirectory,
      ArrayList<Object> builtInEvents,
      ArrayList<Object> events,
      ArrayList<VariableModel> instanceVariables,
      ArrayList<VariableModel> staticVariables,
      HashMap<String, Object> projectVariables) {
    String resultCode = getRawCode() != null ? new String(getRawCode()) : null;
    if (resultCode == null) {
      return "";
    }

    resultCode =
        resultCode.replace(
            RawCodeReplacer.getReplacer(getReplacerKey(), "imports"),
            getImportsCode(builtInEvents, events));
    resultCode =
        resultCode.replace(
            RawCodeReplacer.getReplacer(getReplacerKey(), "inheritence"), getInheritenceCode());

    if (instanceVariables != null) {
      StringBuilder instanceVariablesCode = new StringBuilder();
      for (int i = 0; i < instanceVariables.size(); ++i) {
        if (i != 0) {
          instanceVariablesCode.append("\n");
        }
        instanceVariablesCode.append(instanceVariables.get(i).getDefCode());
      }

      String formatter = null;
      String[] lines = getRawCode().split("\n");
      for (String line : lines) {
        if (line.contains(RawCodeReplacer.getReplacer(getReplacerKey(), "variables"))) {
          formatter =
              line.substring(
                  0, line.indexOf(RawCodeReplacer.getReplacer(getReplacerKey(), "variables")));
        }
      }

      StringBuilder formattedGeneratedCode = new StringBuilder();

      String[] generatedCodeLines = instanceVariablesCode.toString().split("\n");

      for (int generatedCodeLinePosition = 0;
          generatedCodeLinePosition < generatedCodeLines.length;
          ++generatedCodeLinePosition) {

        if (formatter != null) {
          if (generatedCodeLinePosition != 0) formattedGeneratedCode.append(formatter);
        }

        formattedGeneratedCode.append(generatedCodeLines[generatedCodeLinePosition]);
        if (generatedCodeLinePosition != (generatedCodeLines.length - 1)) {
          formattedGeneratedCode.append("\n");
        }
      }

      resultCode =
          resultCode.replace(
              RawCodeReplacer.getReplacer(getReplacerKey(), "variables"),
              formattedGeneratedCode.toString());
    }

    if (layouts != null && layoutsName != null) {
      StringBuilder layoutVariablesCode = new StringBuilder();
      for (int i = 0; i < layouts.size(); ++i) {

        /*
         * Find the layout with layout name at index i.
         * And pass it to generate variables
         */

        if (!new File(layoutDirectory, layoutsName.get(i)).exists()) continue;

        if (new File(layoutDirectory, layoutsName.get(i)).isDirectory()) continue;

        LayoutModel layout = null;

        try {
          FileInputStream mFileInputStream =
              new FileInputStream(new File(layoutDirectory, layoutsName.get(i)));
          ObjectInputStream mObjectInputStream = new ObjectInputStream(mFileInputStream);
          Object mObject = mObjectInputStream.readObject();
          mFileInputStream.close();
          mObjectInputStream.close();
          if (LayoutModel.class.isInstance(mObject)) {
            layout = LayoutModel.class.cast(mObject);
          }

        } catch (Exception e) {
          continue;
        }

        if (layout == null) {
          continue;
        }

        if (i != 0) {
          layoutVariablesCode.append("\n");
        }
        layoutVariablesCode.append(layouts.get(i).getLayoutDefCode(layout));
      }

      String formatter = null;
      String[] lines = getRawCode().split("\n");
      for (String line : lines) {
        if (line.contains(RawCodeReplacer.getReplacer(getReplacerKey(), "layoutVariables"))) {
          formatter =
              line.substring(
                  0, line.indexOf(RawCodeReplacer.getReplacer(getReplacerKey(), "layoutVariables")));
        }
      }

      StringBuilder formattedGeneratedCode = new StringBuilder();

      String[] generatedCodeLines = layoutVariablesCode.toString().split("\n");

      for (int generatedCodeLinePosition = 0;
          generatedCodeLinePosition < generatedCodeLines.length;
          ++generatedCodeLinePosition) {

        if (formatter != null) {
          if (generatedCodeLinePosition != 0) formattedGeneratedCode.append(formatter);
        }

        formattedGeneratedCode.append(generatedCodeLines[generatedCodeLinePosition]);
        if (generatedCodeLinePosition != (generatedCodeLines.length - 1)) {
          formattedGeneratedCode.append("\n");
        }
      }

      resultCode =
          resultCode.replace(
              RawCodeReplacer.getReplacer(getReplacerKey(), "variables"),
              formattedGeneratedCode.toString());
    }

    if (staticVariables != null) {
      StringBuilder staticVariablesCode = new StringBuilder();
      for (int i = 0; i < staticVariables.size(); ++i) {
        if (i != 0) {
          staticVariablesCode.append("\n");
        }
        staticVariablesCode.append(staticVariables.get(i).getDefCode());
      }

      String formatter = null;
      String[] lines = getRawCode().split("\n");
      for (String line : lines) {
        if (line.contains(RawCodeReplacer.getReplacer(getReplacerKey(), "static-variables"))) {
          formatter =
              line.substring(
                  0,
                  line.indexOf(RawCodeReplacer.getReplacer(getReplacerKey(), "static-variables")));
        }
      }

      StringBuilder formattedGeneratedCode = new StringBuilder();

      String[] generatedCodeLines = staticVariablesCode.toString().split("\n");

      for (int generatedCodeLinePosition = 0;
          generatedCodeLinePosition < generatedCodeLines.length;
          ++generatedCodeLinePosition) {

        if (formatter != null) {
          if (generatedCodeLinePosition != 0) formattedGeneratedCode.append(formatter);
        }

        formattedGeneratedCode.append(generatedCodeLines[generatedCodeLinePosition]);
        if (generatedCodeLinePosition != (generatedCodeLines.length - 1)) {
          formattedGeneratedCode.append("\n");
        }
      }

      resultCode =
          resultCode.replace(
              RawCodeReplacer.getReplacer(getReplacerKey(), "static-variables"),
              formattedGeneratedCode.toString());
    }

    boolean[] ignoreEvents = null;
    if (events != null) {
      ignoreEvents = new boolean[events.size()];
      for (int i = 0; i < events.size(); ++i) {
        if (events.get(i) instanceof Event) {
          Event event = (Event) events.get(i);

          if (event.isDirectFileEvent()) {
            String formatter = null;
            String[] lines = resultCode.split("\n");
            for (String line : lines) {
              if (line.contains(RawCodeReplacer.getReplacer(getReplacerKey(), "directEvents"))) {
                formatter =
                    line.substring(
                        0,
                        line.indexOf(
                            RawCodeReplacer.getReplacer(getReplacerKey(), "directEvents")));
              }
            }

            StringBuilder formattedGeneratedCode = new StringBuilder();

            String[] generatedCodeLines = event.getCode(projectVariables).toString().split("\n");

            for (int generatedCodeLinePosition = 0;
                generatedCodeLinePosition < generatedCodeLines.length;
                ++generatedCodeLinePosition) {

              if (formatter != null) {
                if (generatedCodeLinePosition != 0) formattedGeneratedCode.append(formatter);
              }

              formattedGeneratedCode.append(generatedCodeLines[generatedCodeLinePosition]);
              if (generatedCodeLinePosition != (generatedCodeLines.length - 1)) {
                formattedGeneratedCode.append("\n");
              }
            }

            resultCode =
                resultCode.replace(
                    RawCodeReplacer.getReplacer(getReplacerKey(), "directEvents"),
                    formattedGeneratedCode
                        .toString()
                        .concat("\n\n")
                        .concat(formatter)
                        .concat(RawCodeReplacer.getReplacer(getReplacerKey(), "directEvents")));
            ignoreEvents[i] = true;
          }
        }
      }
    }

    if (builtInEvents != null) {

      for (int eventCount = 0; eventCount < builtInEvents.size(); ++eventCount) {
        if (builtInEvents.get(eventCount) instanceof Event) {
          Event event = (Event) builtInEvents.get(eventCount);
          resultCode =
              resultCode.replace(
                  RawCodeReplacer.getReplacer(getReplacerKey(), event.getName()),
                  event.getCode(projectVariables));
        }
      }
    }

    if (events != null) {
      for (int eventCount = 0; eventCount < events.size(); ++eventCount) {
        if (!ignoreEvents[eventCount]) {
          if (events.get(eventCount) instanceof Event) {
            Event event = (Event) events.get(eventCount);
            resultCode =
                resultCode.replace(
                    RawCodeReplacer.getReplacer(event.getEventReplacerKey(), event.getName()),
                    event.getCode(projectVariables));
          }
        }
      }
    }

    resultCode = resultCode.replace(RawCodeReplacer.getReplacer("$FileName"), getFileName());
    resultCode =
        resultCode.replace(
            RawCodeReplacer.getReplacer(getReplacerKey(), "filePackage name"), packageName);
    resultCode = RawCodeReplacer.removeAndroidAppStudioString(getReplacerKey(), resultCode);
    return resultCode;
  }

  public String getInheritenceCode() {
    StringBuilder inheritenceCode = new StringBuilder();
    if (getExtendingClass() != null) {
      inheritenceCode.append(" ");
      inheritenceCode.append("extends ");
      inheritenceCode.append(getExtendingClass());
    }
    if (getImplementingInterface() != null) {
      for (int i = 0; i < getImplementingInterface().length; ++i) {
        if (i == 0) {
          inheritenceCode.append(" ");
          inheritenceCode.append("implements");
        }

        if (i != 0 && (i != (getImplementingInterface().length - 1))) {
          inheritenceCode.append(", ");
        }

        inheritenceCode.append(getImplementingInterface()[i]);
      }
    }
    inheritenceCode.append(" ");
    return inheritenceCode.toString();
  }

  public String getImportsCode(ArrayList<Object> builtInEvents, ArrayList<Object> events) {
    ArrayList<ImportTag> usedImports = getUsedImports(builtInEvents, events);
    StringBuilder importsCode = new StringBuilder();

    ArrayList<String> imported = new ArrayList<String>();

    if (getExtendingClassImport() != null) {
      imported.add(getExtendingClassImport());
      importsCode.append("import ");
      importsCode.append(getExtendingClassImport());
      importsCode.append(";");
    }

    if (getImplementingInterfaceImports() != null) {
      for (int i = 0; i < getImplementingInterfaceImports().length; ++i) {
        imported.add(getImplementingInterfaceImports()[i]);
        importsCode.append("\n");
        importsCode.append("import ");
        importsCode.append(getImplementingInterfaceImports()[i]);
        importsCode.append(";");
      }
    }

    for (int i = 0; i < usedImports.size(); ++i) {
      if (usedImports.get(i).getImportClass() != null) {
        if (!imported.contains(usedImports.get(i).getImportClass())) {
          importsCode.append("\n");
          importsCode.append("import ");
          importsCode.append(usedImports.get(i).getImportClass());
          importsCode.append(";");
          imported.add(usedImports.get(i).getImportClass());
        }
      }
    }

    return importsCode.toString();
  }

  public ArrayList<ImportTag> getUsedImports(
      ArrayList<Object> builtInEvents, ArrayList<Object> events) {
    ArrayList<ImportTag> usedImports = new ArrayList<ImportTag>();

    if (builtInEvents != null) {

      for (int eventCount = 0; eventCount < builtInEvents.size(); ++eventCount) {
        if (builtInEvents.get(eventCount) instanceof Event) {
          Event event = (Event) builtInEvents.get(eventCount);

          for (int tags = 0; tags < event.getAdditionalTagsUsed().size(); ++tags) {
            AdditionalCodeHelperTag tag = event.getAdditionalTagsUsed().get(tags);

            if (tag instanceof ImportTag) {
              usedImports.add((ImportTag) tag);
            }
          }
        }
      }
    }

    if (events != null) {
      for (int eventCount = 0; eventCount < events.size(); ++eventCount) {
        if (events.get(eventCount) instanceof Event) {
          Event event = (Event) events.get(eventCount);

          for (int tags = 0; tags < event.getAdditionalTagsUsed().size(); ++tags) {
            AdditionalCodeHelperTag tag = event.getAdditionalTagsUsed().get(tags);

            if (tag instanceof ImportTag) {
              usedImports.add((ImportTag) tag);
            }
          }
        }
      }
    }

    return usedImports;
  }

  @Override
  protected FileModel clone() {
    JavaFileModel fileModel = new JavaFileModel();
    fileModel.setFileName(getFileName() != null ? new String(getFileName()) : null);
    fileModel.setFileExtension(getFileExtension() != null ? new String(getFileExtension()) : null);
    fileModel.setBuiltInEventsName(
        getBuiltInEventsName() != null ? new String(getBuiltInEventsName()) : null);

    ArrayList<Object> clonedBuildInEvents = new ArrayList<Object>();
    for (int position = 0; position < getDefaultBuiltInEvents().size(); ++position) {
      if (getDefaultBuiltInEvents().get(position) instanceof Event) {
        clonedBuildInEvents.add(((Event) getDefaultBuiltInEvents().get(position)).clone());
      } else if (getDefaultBuiltInEvents().get(position) instanceof Event) {
        clonedBuildInEvents.add(
            ((EventGroupModel) getDefaultBuiltInEvents().get(position)).clone());
      }
    }
    fileModel.setReplacerKey(getReplacerKey() != null ? new String(getReplacerKey()) : null);
    fileModel.setDefaultBuiltInEvents(clonedBuildInEvents);
    fileModel.setRawCode(getRawCode() != null ? new String(getRawCode()) : null);
    fileModel.setFolder(new Boolean(isFolder()));
    fileModel.setAndroidLibrary(new Boolean(isAndroidLibrary()));
    fileModel.setAndroidAppModule(new Boolean(isAndroidAppModule()));
    fileModel.setClassType(new Integer(getClassType()));
    fileModel.setExtendingClass(
        getExtendingClass() != null ? new String(getExtendingClass()) : null);
    fileModel.setImplementingInterface(ArrayUtils.clone(getImplementingInterface()));
    fileModel.setExtendingClassImport(
        getExtendingClassImport() != null ? new String(getExtendingClassImport()) : null);
    fileModel.setImplementingInterfaceImports(ArrayUtils.clone(getImplementingInterfaceImports()));
    return fileModel;
  }

  public int getClassType() {
    return this.classType;
  }

  public void setClassType(int classType) {
    this.classType = classType;
  }

  public String getExtendingClass() {
    return this.extendingClass;
  }

  public void setExtendingClass(String extendingClass) {
    this.extendingClass = extendingClass;
  }

  public String[] getImplementingInterface() {
    return this.implementingInterface;
  }

  public void setImplementingInterface(String[] implementingInterface) {
    this.implementingInterface = implementingInterface;
  }

  public String getExtendingClassImport() {
    return this.extendingClassImport;
  }

  public void setExtendingClassImport(String extendingClassImport) {
    this.extendingClassImport = extendingClassImport;
  }

  public String[] getImplementingInterfaceImports() {
    return this.implementingInterfaceImports;
  }

  public void setImplementingInterfaceImports(String[] implementingInterfaceImports) {
    this.implementingInterfaceImports = implementingInterfaceImports;
  }

  public ArrayList<VariableModel> getLayouts() {
    return this.layouts;
  }

  public void setLayouts(ArrayList<VariableModel> layouts) {
    this.layouts = layouts;
  }
}
