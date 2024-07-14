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

package com.icst.android.appstudio.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import java.io.File;

public class ModuleModel implements Parcelable, Cloneable {
  /*
   * Example: :module:submodule
   */
  public String module;
  public File moduleDirectory;
  public File moduleOutputDirectory;
  public File projectRootDirectory;
  public File javaSourceDirectory;
  public File javaSourceOutputDirectory;
  public File resourceDirectory;
  public File resourceOutputDirectory;
  public File gradleFileDirectory;
  public File gradleOutputFile;
  public File manifestFile;
  public File manifestOutputFile;

  public ModuleModel() {}

  protected ModuleModel(Parcel in) {
    module = in.readString();
    moduleDirectory = new File(in.readString());
    moduleOutputDirectory = new File(in.readString());
    projectRootDirectory = new File(in.readString());
    javaSourceDirectory = new File(in.readString());
    javaSourceOutputDirectory = new File(in.readString());
    resourceDirectory = new File(in.readString());
    resourceOutputDirectory = new File(in.readString());
    gradleFileDirectory = new File(in.readString());
    gradleOutputFile = new File(in.readString());
    manifestFile = new File(in.readString());
    manifestOutputFile = new File(in.readString());
  }

  public void init(String module, File projectRootDirectory) {
    this.module = module;
    this.projectRootDirectory = projectRootDirectory;
    this.moduleDirectory = EnvironmentUtils.getModuleDirectory(projectRootDirectory, module);
    moduleOutputDirectory = EnvironmentUtils.getModuleOutputDirectory(projectRootDirectory, module);

    javaSourceDirectory = getJavaDirectory();
    javaSourceOutputDirectory = getJavaOutputDirectory();

    resourceDirectory = getResourceDirectory();
    resourceOutputDirectory = getResourceOutputDirectory();

    gradleFileDirectory = getGradleFileDirectory();
    gradleOutputFile = getGradleOutputDirectory();

    manifestFile = getManifestFile();
	manifestOutputFile = getManifestOutputFile();
  }

  private File getManifestFile() {
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
        EnvironmentUtils.MANIFEST);
  }

  private File getManifestOutputFile() {
    return new File(
        new File(
            new File(
                EnvironmentUtils.getModuleOutputDirectory(projectRootDirectory, module),
                EnvironmentUtils.SOURCE_DIR),
            EnvironmentUtils.MAIN_DIR),
        EnvironmentUtils.MANIFEST);
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
                EnvironmentUtils.getModuleOutputDirectory(projectRootDirectory, module),
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
                EnvironmentUtils.getModuleOutputDirectory(projectRootDirectory, module),
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
        EnvironmentUtils.getModuleOutputDirectory(projectRootDirectory, module),
        EnvironmentUtils.GRADLE_FILE);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flag) {
    dest.writeString(module);
    dest.writeString(moduleDirectory.getAbsolutePath());
    dest.writeString(moduleOutputDirectory.getAbsolutePath());
    dest.writeString(projectRootDirectory.getAbsolutePath());
    dest.writeString(javaSourceDirectory.getAbsolutePath());
    dest.writeString(javaSourceOutputDirectory.getAbsolutePath());
    dest.writeString(resourceDirectory.getAbsolutePath());
    dest.writeString(resourceOutputDirectory.getAbsolutePath());
    dest.writeString(gradleFileDirectory.getAbsolutePath());
    dest.writeString(gradleOutputFile.getAbsolutePath());
    dest.writeString(manifestFile.getAbsolutePath());
    dest.writeString(manifestOutputFile.getAbsolutePath());
  }

  public static final Creator<ModuleModel> CREATOR =
      new Creator<ModuleModel>() {
        @Override
        public ModuleModel createFromParcel(Parcel in) {
          return new ModuleModel(in);
        }

        @Override
        public ModuleModel[] newArray(int size) {
          return new ModuleModel[size];
        }
      };
}
