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

package com.tscodeeditor.android.appstudio.models;

import com.tscodeeditor.android.appstudio.utils.EnvironmentUtils;
import com.tscodeeditor.android.appstudio.utils.serialization.DeserializerUtils;
import java.io.File;
import java.io.Serializable;

public class ModuleModel implements Serializable, Cloneable {
  public static final long serialVersionUID = 21L;

  /*
   * Example: :module:submodule
   */
  public String module;
  public File moduleDirectory;
  public File moduleOutputDirectory;

  public File projectRootDirectory;

  public ProjectModel projectModel;

  public File javaSourceDirectory;
  public File javaSourceOutputDirectory;
  public String currentSelectedJavaSourceDirectory;
  public String currentSelectedJavaSourceFile;

  public File resourceDirectory;
  public File resourceOutputDirectory;
  public String currentSelectedResourceDirectory;
  public String currentSelectedResourceFile;

  public File gradleFileDirectory;
  public File gradleOutputFile;

  public void init(String module, File projectRootDirectory) {
    this.module = module;
    this.projectRootDirectory = projectRootDirectory;
    this.moduleDirectory = EnvironmentUtils.getModuleDirectory(projectRootDirectory, module);
    moduleOutputDirectory = EnvironmentUtils.getModuleOutputDirectory(module);
    projectModel =
        DeserializerUtils.deserialize(
            new File(projectRootDirectory, EnvironmentUtils.PROJECT_CONFIGRATION),
            ProjectModel.class);
    javaSourceDirectory = getJavaDirectory();
    javaSourceOutputDirectory = getJavaOutputDirectory();

    resourceDirectory = getResourceDirectory();
    resourceOutputDirectory = getResourceOutputDirectory();

    gradleFileDirectory = getGradleFileDirectory();
    gradleOutputFile = getGradleOutputDirectory();
  }

  private File getJavaDirectory() {
    return new File(
        new File(
            new File(
                new File(
                    new File(
                        new File(moduleDirectory, EnvironmentUtils.FILES),
                        EnvironmentUtils.SOURCE_DIR),
                    EnvironmentUtils.FILES),
                EnvironmentUtils.MAIN_DIR),
            EnvironmentUtils.FILES),
        EnvironmentUtils.JAVA_DIR);
  }

  private File getJavaOutputDirectory() {
    return new File(
        new File(
            new File(
                new File(
                    EnvironmentUtils.getBuildDir(projectRootDirectory),
                    EnvironmentUtils.getModuleOutputDirectory(module).getAbsolutePath()),
                EnvironmentUtils.SOURCE_DIR),
            EnvironmentUtils.MAIN_DIR),
        EnvironmentUtils.JAVA_DIR);
  }

  private File getResourceDirectory() {
    return new File(
        new File(
            new File(
                new File(
                    new File(
                        new File(moduleDirectory, EnvironmentUtils.FILES),
                        EnvironmentUtils.SOURCE_DIR),
                    EnvironmentUtils.FILES),
                EnvironmentUtils.MAIN_DIR),
            EnvironmentUtils.FILES),
        EnvironmentUtils.RES_DIR);
  }

  private File getResourceOutputDirectory() {
    return new File(
        new File(
            new File(
                new File(
                    EnvironmentUtils.getBuildDir(projectRootDirectory),
                    EnvironmentUtils.getModuleOutputDirectory(module).getAbsolutePath()),
                EnvironmentUtils.SOURCE_DIR),
            EnvironmentUtils.MAIN_DIR),
        EnvironmentUtils.RES_DIR);
  }

  private File getGradleFileDirectory() {
    return new File(
        new File(
            EnvironmentUtils.getModuleDirectory(projectRootDirectory, module),
            EnvironmentUtils.FILES),
        EnvironmentUtils.GRADLE_FILE);
  }

  private File getGradleOutputDirectory() {
    return new File(
        EnvironmentUtils.getModuleOutputDirectory(module), EnvironmentUtils.GRADLE_FILE);
  }
}
