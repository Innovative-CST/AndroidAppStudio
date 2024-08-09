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

package com.icst.android.appstudio.builder;

import android.code.editor.common.utils.FileUtils;
import com.icst.android.appstudio.block.model.FileModel;
import com.icst.android.appstudio.block.model.JavaFileModel;
import com.icst.android.appstudio.block.tag.DependencyTag;
import com.icst.android.appstudio.helper.FileModelCodeHelper;
import com.icst.android.appstudio.helper.ProjectCodeBuilderCancelToken;
import com.icst.android.appstudio.listener.ProjectCodeBuildListener;
import com.icst.android.appstudio.models.ModuleModel;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.serialization.DeserializerUtils;
import java.io.File;
import java.util.ArrayList;

public class JavaSourceBuilder {
  private ModuleModel module;
  private boolean rebuild;
  private String packageName;
  private File inputDir;
  private File outputDir;
  private ProjectCodeBuildListener listener;
  private ProjectCodeBuilderCancelToken cancelToken;
  private ArrayList<DependencyTag> dependencies;

  public void build() {
    dependencies = new ArrayList<DependencyTag>();

    if (module == null || packageName == null || inputDir == null || outputDir == null) {
      if (listener != null) {
        listener.onBuildProgressLog("Null values are passed to JavaSourceBuilder");
      }
      return;
    }

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

            JavaSourceBuilder mainJavaSrcBuilder = new JavaSourceBuilder();
            mainJavaSrcBuilder.setModule(module);
            mainJavaSrcBuilder.setRebuild(rebuild);
            mainJavaSrcBuilder.setPackageName(fileModel.getFileName());
            mainJavaSrcBuilder.setInputDir(inputDir);
            mainJavaSrcBuilder.setOutputDir(outputDir);
            mainJavaSrcBuilder.setListener(listener);
            mainJavaSrcBuilder.setCancelToken(cancelToken);
            mainJavaSrcBuilder.build();
            dependencies.addAll(mainJavaSrcBuilder.getDependencies());
          } else {

            JavaSourceBuilder mainJavaSrcBuilder = new JavaSourceBuilder();
            mainJavaSrcBuilder.setModule(module);
            mainJavaSrcBuilder.setRebuild(rebuild);
            mainJavaSrcBuilder.setPackageName(
                packageName.concat(".").concat(fileModel.getFileName()));
            mainJavaSrcBuilder.setInputDir(inputDir);
            mainJavaSrcBuilder.setOutputDir(outputDir);
            mainJavaSrcBuilder.setListener(listener);
            mainJavaSrcBuilder.setCancelToken(cancelToken);
            mainJavaSrcBuilder.build();
            dependencies.addAll(mainJavaSrcBuilder.getDependencies());
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
        fileGenerator.setModule(module);
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
        dependencies.addAll(fileGenerator.getUsedDependency());
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

  public ModuleModel getModule() {
    return this.module;
  }

  public void setModule(ModuleModel module) {
    this.module = module;
  }

  public boolean getRebuild() {
    return this.rebuild;
  }

  public void setRebuild(boolean rebuild) {
    this.rebuild = rebuild;
  }

  public String getPackageName() {
    return this.packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public File getInputDir() {
    return this.inputDir;
  }

  public void setInputDir(File inputDir) {
    this.inputDir = inputDir;
  }

  public File getOutputDir() {
    return this.outputDir;
  }

  public void setOutputDir(File outputDir) {
    this.outputDir = outputDir;
  }

  public ProjectCodeBuildListener getListener() {
    return this.listener;
  }

  public void setListener(ProjectCodeBuildListener listener) {
    this.listener = listener;
  }

  public ProjectCodeBuilderCancelToken getCancelToken() {
    return this.cancelToken;
  }

  public void setCancelToken(ProjectCodeBuilderCancelToken cancelToken) {
    this.cancelToken = cancelToken;
  }

  public ArrayList<DependencyTag> getDependencies() {
    return this.dependencies;
  }
}
