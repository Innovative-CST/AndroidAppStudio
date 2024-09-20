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

package com.icst.android.appstudio;

import com.icst.android.appstudio.extensions.activityextension.ActivityExtension;
import com.icst.android.appstudio.extensions.basicvariables.BasicVariablesExtensions;
import com.icst.android.appstudio.extensions.controlextension.ControlExtension;
import com.icst.android.appstudio.extensions.controlextension.OperatorExtension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ExtensionGenerator {
  public static void generateFiles(File output) throws Exception {
    /*
     * MAKE YOUR EXTENSION FILE IN ANOTHER CLASS AND SERIALIZE IT
     * For Example:
     * serialize(YOUR_EXTENSION_OBJECT, new File(output, "FileName.fileExtension"), "TaskName");
     */
    serialize(
        ControlExtension.getExtensionBundle(),
        new File(output, "ControlBlocks.extaas"),
        "generateControlBlocks");

    serialize(
        OperatorExtension.getExtensionBundle(),
        new File(output, "OperatorBlocks.extaas"),
        "generateOperatorBlocks");

    serialize(
        ActivityExtension.getExtensionBundle(),
        new File(output, "ActivityEvents.extaas"),
        "generateActivityEvents");

    serialize(
        BasicVariablesExtensions.getExtensionBundle(),
        new File(output, "BasicVariable.extaas"),
        "generateBasicVariable");
  }

  // DO NOT MODIFY THIS METHOD
  public static void main(String[] args) throws Exception {
    generateFiles(new File(args[0]));

    boolean installExtensions = Boolean.parseBoolean(args[1]);
    boolean isDeveloperMode = Boolean.parseBoolean(args[2]);
    File storage = new File(args[3]);

    // Do not perform installation of extension until specified.
    if (!installExtensions) return;
    if (!isDeveloperMode) return;
    if (storage.getAbsolutePath().equals("NOT_PROVIDED")) return;

    File IDEDIRECTORY = new File(storage, ".AndroidAppBuilder");
    File EXTENSION_DIR = new File(IDEDIRECTORY, "Extension");

    File outputDir = new File(args[0]);
    if (!outputDir.exists()) outputDir.mkdirs();
    if (!EXTENSION_DIR.exists()) EXTENSION_DIR.mkdirs();

    System.out.println();
    for (File file : outputDir.listFiles()) {
      Path in = Path.of(file.toURI());
      Path out = Path.of(new File(EXTENSION_DIR, file.getName()).toURI());
      try {
        System.out.println("Installing ".concat(file.getName()).concat(" in your file system."));
        Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    System.out.println(
        "\nAndroid AppStudio will search the installed extensions if it is built using the current local.properties configuration.");
  }

  // DO NOT MODIFY THIS METHOD
  public static void serialize(Object object, File path, String taskName) throws Exception {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(path);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
      objectOutputStream.writeObject(object);
      fileOutputStream.close();
      objectOutputStream.close();
      System.out.println("> Task :extension:".concat(taskName));
    } catch (Exception e) {
      System.out.println("Failed to serialized ".concat(path.getAbsolutePath()));
      throw e;
    }
  }
}
