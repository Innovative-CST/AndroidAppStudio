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

import com.icst.android.appstudio.models.ExtensionBundle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.FileWriter;
import java.io.IOException;

public class ExtensionsMetaDataGenerator {

  // DO NOT MODIFY THIS METHOD
  public static void main(String[] args) throws Exception {
    File outputDir = new File(args[0]);

    ArrayList<HashMap<String, Object>> extensions = ExtensionsManager.getExtensions();

    for (int i = 0; i < extensions.size(); ++i) {
      if (extensions.get(i).containsKey(ExtensionsManager.EXTENSION_BUNDLE)) {

        String fileName = ((String) extensions.get(i).get(ExtensionsManager.EXTENSION_FILE_NAME));
        ExtensionBundle extension =
            (ExtensionBundle) extensions.get(i).get(ExtensionsManager.EXTENSION_BUNDLE);

        String name = extractName(fileName);
        String metadata = getMetaDataJSON(extension);
        File metadataFile = new File(outputDir, name.concat(".json"));

        System.out.println("Generating metadata : ".concat(name).concat(".json"));
        writeFile(metadataFile, metadata);
      } else {
        throw new Exception(ExtensionsManager.EXTENSION_BUNDLE.concat(" key is not set."));
      }
    }
  }

  // DO NOT MODIFY THIS METHOD
  private static void serialize(Object object, File path, String taskName) throws Exception {
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

  private static String extractName(String fileName) {
    String baseName = fileName.replace(".extaas", "");
    return baseName.toString();
  }

  private static String getMetaDataJSON(ExtensionBundle extension) {
    StringBuilder json = new StringBuilder();

    json.append("{");
    json.append("\n\t");
    json.append("\"name\": \"");
    json.append(extension.getName());
    json.append("\",");
    json.append("\n\t");
    json.append("\"version\": ");
    json.append(extension.getVersion());
    json.append(",");
    json.append("\n\t");
    json.append("\"blocks\": ");
    json.append(getArrayListSize(extension.getBlocks()));
    json.append(",");
    json.append("\n\t");
    json.append("\"events\": ");
    json.append(getArrayListSize(extension.getEvents()));
    json.append(",");
    json.append("\n\t");
    json.append("\"variables\": ");
    json.append(getArrayListSize(extension.getVariables()));
    json.append(",");
    json.append("\n\t");
    json.append("\"holders\": ");
    json.append(getArrayListSize(extension.getEventHolders()));
    json.append("\n");
    json.append("}");
    return json.toString();
  }

  public static int getArrayListSize(ArrayList arrayList) {
    return arrayList == null ? 0 : arrayList.size();
  }

  private static void writeFile(File path, String str) {
    createNewFile(path);
    FileWriter fileWriter = null;

    try {
      fileWriter = new FileWriter(path, false);
      fileWriter.write(str);
      fileWriter.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (fileWriter != null) fileWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void createNewFile(File file) {
    file.getParentFile().mkdirs();

    try {
      if (!file.exists()) file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
