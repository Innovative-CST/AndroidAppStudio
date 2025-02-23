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

package com.icst.android.appstudio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.icst.android.appstudio.models.ExtensionBundle;

public class ExtensionsMetaDataGenerator {

	// DO NOT MODIFY THIS METHOD
	public static void main(String[] args) throws Exception {
		File outputDir = new File(args[0]);

		ArrayList<HashMap<String, Object>> extensions = ExtensionsManager.getExtensions();

		for (int i = 0; i < extensions.size(); ++i) {
			if (extensions.get(i).containsKey(ExtensionsManager.EXTENSION_BUNDLE)) {

				String fileName = ((String) extensions.get(i).get(ExtensionsManager.EXTENSION_FILE_NAME));
				ExtensionBundle extension = (ExtensionBundle) extensions.get(i).get(ExtensionsManager.EXTENSION_BUNDLE);

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
		return baseName;
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
				if (fileWriter != null)
					fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createNewFile(File file) {
		file.getParentFile().mkdirs();

		try {
			if (!file.exists())
				file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
