/*
 * This file is part of Android AppStudio [https://github.com/TS-Code-Editor/AndroidAppStudio].
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

package com.icst.android.appstudio.helper;

import android.code.editor.common.utils.FileUtils;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.model.JavaFileModel;
import com.icst.android.appstudio.listener.ProjectCodeBuildListener;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.FileModelUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import com.icst.android.appstudio.vieweditor.models.LayoutModel;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class ProjectCodeBuilder {

  public static void generateModulesCode(
      File projectRootDirectory,
      String moduleName,
      boolean rebuild,
      ProjectCodeBuildListener listener,
      ProjectCodeBuilderCancelToken cancelToken) {

    FileModel folder =
        DeserializerUtils.deserialize(
            new File(
                EnvironmentUtils.getModuleDirectory(projectRootDirectory, moduleName),
                EnvironmentUtils.FILE_MODEL),
            FileModel.class);
    if (folder != null) {
      if (folder.isAndroidAppModule() || folder.isAndroidLibrary()) {
        ModuleModel module = new ModuleModel();
        module.init(moduleName, projectRootDirectory);
        generateModuleCode(module, rebuild, listener, cancelToken);
        return;
      }
    }

    ArrayList<FileModel> files =
        FileModelUtils.getFileModelList(
            moduleName.equals("")
                ? EnvironmentUtils.getModuleDirectory(projectRootDirectory, moduleName)
                : new File(
                    EnvironmentUtils.getModuleDirectory(projectRootDirectory, moduleName),
                    EnvironmentUtils.FILES));
    if (files == null) {
      return;
    } else if (files.size() == 0) {
      return;
    }

    for (int i = 0; i < files.size(); ++i) {
      if (files.get(i).isAndroidAppModule() || files.get(i).isAndroidLibrary()) {
        ModuleModel module = new ModuleModel();
        module.init(moduleName + ":" + files.get(i).getName(), projectRootDirectory);
        generateModuleCode(module, rebuild, listener, cancelToken);
      } else if (files.get(i).isFolder()) {
        generateModulesCode(
            projectRootDirectory,
            moduleName + ":" + files.get(i).getName(),
            rebuild,
            listener,
            cancelToken);
      }
    }
  }

  public static void generateModuleCode(
      ModuleModel module,
      boolean rebuild,
      ProjectCodeBuildListener listener,
      ProjectCodeBuilderCancelToken cancelToken) {

    long executionStartTime = System.currentTimeMillis();
    if (listener != null) listener.onBuildStart();

    /************************************
     * Generate Module Output Directory *
     ************************************/

    if (listener != null) {
      listener.onBuildProgressLog("Run task : [" + module.module + ":generateCode]\n");
    }

    if (rebuild) {
      if (listener != null) {
        listener.onBuildProgressLog("Regenerating whole module code : [" + module.module + "]\n");
      }
    } else {
      if (listener != null) {
        listener.onBuildProgressLog(
            "Generating partially code of module : [" + module.module + "]\n");
      }
    }
    if (module.moduleOutputDirectory.exists()) {
      if (rebuild) {
        cleanFile(module.moduleOutputDirectory, listener, cancelToken);
      }
    } else {
      module.moduleOutputDirectory.mkdirs();
    }

    /*******************************
     * Generate Module Gradle File *
     *******************************/

    if (listener != null) {
      listener.onBuildProgressLog("> Task " + module.module + ":generateGradleFile");
    }

    FileModelCodeHelper gradleFileGenerator = new FileModelCodeHelper();
    gradleFileGenerator.setProjectRootDirectory(module.projectRootDirectory);
    gradleFileGenerator.setFileModel(
        DeserializerUtils.deserialize(
            new File(module.gradleFileDirectory, EnvironmentUtils.FILE_MODEL), FileModel.class));
    gradleFileGenerator.setEventsDirectory(
        new File(module.gradleFileDirectory, EnvironmentUtils.EVENTS_DIR));

    if (!module.gradleOutputFile.exists()) {
      FileUtils.writeFile(
          module.gradleOutputFile.getAbsolutePath(),
          gradleFileGenerator.getCode() == null ? "null" : gradleFileGenerator.getCode());
    } else {
      if (rebuild) {
        FileUtils.writeFile(
            module.gradleOutputFile.getAbsolutePath(),
            gradleFileGenerator.getCode() == null ? "null" : gradleFileGenerator.getCode());
      }
    }

    /*****************************
     * Generate Resource Folders *
     *****************************/

    if (listener != null) {
      listener.onBuildProgressLog("> Task " + module.module + ":generateResourceFolder");
    }

    if (module.resourceOutputDirectory.exists()) {
      if (rebuild) {
        cleanFile(module.resourceOutputDirectory, listener, cancelToken);
      }
    } else {
      module.resourceOutputDirectory.mkdirs();
    }

    ArrayList<FileModel> resFolders =
        FileModelUtils.getFileModelList(new File(module.resourceDirectory, EnvironmentUtils.FILES));

    if (resFolders != null) {

      for (int position = 0; position < resFolders.size(); ++position) {
        new File(module.resourceOutputDirectory, resFolders.get(position).getName()).mkdirs();

        if (Pattern.compile("^layout(?:-[a-zA-Z0-9]+)?$")
            .matcher(resFolders.get(position).getName())
            .matches()) {
          generateLayoutResources(
              module, rebuild, resFolders.get(position).getName(), listener, cancelToken);
        }
      }
    }

    /***********************
     * Generate Java Files *
     ***********************/

    if (listener != null) {
      listener.onBuildProgressLog("> Task " + module.module + ":generateJavaFiles");
      listener.onBuildProgressLog("  Generating");
    }

    generateJavaFile(
        module,
        rebuild,
        "",
        new File(module.javaSourceDirectory, EnvironmentUtils.FILES),
        module.javaSourceOutputDirectory,
        listener,
        cancelToken);

    // Yet be to done

    long endExectionTime = System.currentTimeMillis();
    long executionTime = endExectionTime - executionStartTime;
    if (listener != null) {
      listener.onBuildComplete(executionTime);
    }
  }

  public static void generateJavaFile(
      ModuleModel module,
      boolean rebuild,
      String packageName,
      File inputDir,
      File outputDir,
      ProjectCodeBuildListener listener,
      ProjectCodeBuilderCancelToken cancelToken) {
    if (!inputDir.exists()) {
      return;
    }

    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    for (File files : new File(inputDir, getJavaDirectoryForPackage(packageName)).listFiles()) {

      if (new File(files, EnvironmentUtils.FILE_MODEL).exists()) {
        FileModel fileModel =
            DeserializerUtils.deserialize(
                new File(files, EnvironmentUtils.FILE_MODEL), FileModel.class);

        if (fileModel == null) {
          return;
        }

        if (fileModel.isFolder()) {

          new File(outputDir, getJavaOutputDirectoryForPackage(packageName)).mkdirs();

          if (packageName.equals("")) {
            generateJavaFile(
                module,
                rebuild,
                fileModel.getFileName(),
                inputDir,
                outputDir,
                listener,
                cancelToken);
          } else {
            generateJavaFile(
                module,
                rebuild,
                packageName.concat(".").concat(fileModel.getFileName()),
                inputDir,
                outputDir,
                listener,
                cancelToken);
          }
        }
      } else if (new File(files, EnvironmentUtils.JAVA_FILE_MODEL).exists()) {

        JavaFileModel javaFileModel =
            DeserializerUtils.deserialize(
                new File(files, EnvironmentUtils.JAVA_FILE_MODEL), JavaFileModel.class);

        if (javaFileModel == null) {
          return;
        }

        if (packageName.equals("")) {
          if (listener != null) {
            listener.onBuildProgressLog("  " + javaFileModel.getFileName());
          }
        } else {
          if (listener != null) {
            listener.onBuildProgressLog(
                "  " + packageName.concat(".") + javaFileModel.getFileName());
          }
        }

        FileModelCodeHelper fileGenerator = new FileModelCodeHelper();

        fileGenerator.setEventsDirectory(new File(files, EnvironmentUtils.EVENTS_DIR));
        fileGenerator.setProjectRootDirectory(module.projectRootDirectory);
        fileGenerator.setFileModel(javaFileModel);
        String code =
            fileGenerator.getCode(
                packageName,
                new File(files, EnvironmentUtils.VARIABLES),
                new File(files, EnvironmentUtils.STATIC_VARIABLES));

        FileUtils.writeFile(
            new File(
                    new File(outputDir, getJavaOutputDirectoryForPackage(packageName)),
                    javaFileModel.getName())
                .getAbsolutePath(),
            code);
      }
    }
  }

  public static String getJavaDirectoryForPackage(String packageName) {

    StringBuilder packagePath = new StringBuilder();

    String[] packageBreakdown = packageName.split("\\.");
    for (int i = 0; i < packageBreakdown.length; ++i) {
      if (packageBreakdown[i].length() != 0) {
        packagePath.append(packageBreakdown[i]);
        packagePath.append(File.separator);
        packagePath.append(EnvironmentUtils.FILES);
        packagePath.append(File.separator);
      }
    }
    if (packageName.length() != 0) {
      if (packageBreakdown.length == 0) {
        packagePath.append(packageName);
        packagePath.append(File.separator);
        packagePath.append(EnvironmentUtils.FILES);
      }
    }

    return packagePath.toString();
  }

  public static String getJavaOutputDirectoryForPackage(String packageName) {

    StringBuilder packagePath = new StringBuilder();

    String[] packageBreakdown = packageName.split("\\.");
    for (int i = 0; i < packageBreakdown.length; ++i) {
      if (packageBreakdown[i].length() != 0) {
        packagePath.append(packageBreakdown[i]);
        packagePath.append(File.separator);
      }
    }
    if (packageName.length() != 0) {
      if (packageBreakdown.length == 0) {
        packagePath.append(packageName);
      }
    }

    return packagePath.toString();
  }

  public static void generateLayoutResources(
      ModuleModel module,
      boolean rebuild,
      String layoutDirName,
      ProjectCodeBuildListener listener,
      ProjectCodeBuilderCancelToken cancelToken) {
    if (listener != null) {
      listener.onBuildProgressLog(
          "> Task " + module.module + ":generateLayoutsFile[" + layoutDirName + "]");
    }

    File layoutsDir =
        new File(
            new File(new File(module.resourceDirectory, EnvironmentUtils.FILES), layoutDirName),
            EnvironmentUtils.FILES);
    if (!layoutsDir.exists()) return;

    for (File layoutFile : layoutsDir.listFiles()) {
      LayoutModel layout = DeserializerUtils.deserialize(layoutFile, LayoutModel.class);
      if (layout == null) {
        continue;
      }

      FileUtils.writeFile(
          new File(
                  new File(module.resourceOutputDirectory, layoutDirName),
                  layout.getLayoutName().concat(".xml"))
              .getAbsolutePath(),
          layout.getCode());
    }
  }

  private static boolean cleanFile(
      File file, ProjectCodeBuildListener listener, ProjectCodeBuilderCancelToken cancelToken) {
    if (!file.exists()) {
      return true;
    }
    if (file.isFile()) {
      return file.delete();
    } else {
      if (file.listFiles().length == 0) {
        if (listener != null) {
          file.delete();
        }
        return true;
      } else {
        for (File subFile : file.listFiles()) {
          if (!cleanFile(subFile, listener, cancelToken)) {
            return false;
          }
        }
        file.delete();
        return true;
      }
    }
  }
}
