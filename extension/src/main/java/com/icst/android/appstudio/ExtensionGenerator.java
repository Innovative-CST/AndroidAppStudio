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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages the generation and installation of extensions in Android AppStudio.
 * The class processes
 * extension bundles, serializes them, and handles installation if required.
 */
public class ExtensionGenerator {

	/**
	 * Main method to process extensions and optionally install them.
	 * 
	 * @param args
	 *            Command-line arguments:
	 *            args[0] - Output directory for the extensions.
	 *            args[1] - Boolean flag to determine if extensions should be
	 *            installed.
	 *            args[2] - Boolean flag for developer mode.
	 *            args[3] - File storage location for extension installation.
	 * @throws Exception
	 *             if any error occurs during extension processing or installation.
	 */
	public static void main(String[] args) throws Exception {
		File outputDir = new File(args[0]);
		boolean installExtensions = Boolean.parseBoolean(args[1]);
		boolean isDeveloperMode = Boolean.parseBoolean(args[2]);
		File storage = new File(args[3]);

		// Handle extensions processing and installation
		processExtensions(outputDir);
		installExtensions(installExtensions, isDeveloperMode, storage, outputDir);
	}

	/**
	 * Processes extensions by serializing the extension bundle into the specified
	 * output directory.
	 *
	 * @param outputDir
	 *            The directory where the serialized extension files will be stored.
	 * @throws Exception
	 *             if the extension bundle key is not set or serialization fails.
	 */
	public static void processExtensions(File outputDir) throws Exception {
		ArrayList<HashMap<String, Object>> extensions = ExtensionsManager.getExtensions();

		for (HashMap<String, Object> extension : extensions) {
			if (extension.containsKey(ExtensionsManager.EXTENSION_BUNDLE)) {
				File extensionFile = new File(outputDir, (String) extension.get(ExtensionsManager.EXTENSION_FILE_NAME));
				String taskName = extractTaskName((String) extension.get(ExtensionsManager.EXTENSION_FILE_NAME));
				serialize(extension.get(ExtensionsManager.EXTENSION_BUNDLE), extensionFile, taskName);
			} else {
				throw new Exception(ExtensionsManager.EXTENSION_BUNDLE.concat(" key is not set."));
			}
		}
	}

	/**
	 * Installs the extensions into the specified storage directory if the
	 * installation conditions are
	 * met.
	 *
	 * @param installExtensions
	 *            Flag to determine if installation is required.
	 * @param isDeveloperMode
	 *            Flag to check if the developer mode is enabled.
	 * @param storage
	 *            The storage directory where the extensions will be installed.
	 * @param outputDir
	 *            The output directory containing the serialized extensions.
	 */
	public static void installExtensions(
			boolean installExtensions, boolean isDeveloperMode, File storage, File outputDir) {
		if (!installExtensions || !isDeveloperMode || storage.getAbsolutePath().equals("NOT_PROVIDED"))
			return;

		File ideDirectory = new File(storage, ".AndroidAppBuilder");
		File extensionDir = new File(ideDirectory, "Extension");

		createDirectories(outputDir, extensionDir);
		installFiles(outputDir, extensionDir);

		System.out.println(
				"\nAndroid AppStudio will search the installed extensions if it is built using the current local.properties configuration.");
	}

	/**
	 * Creates the necessary directories if they do not exist.
	 *
	 * @param outputDir
	 *            The directory where the serialized extension files are stored.
	 * @param extensionDir
	 *            The directory where the extensions will be installed.
	 */
	public static void createDirectories(File outputDir, File extensionDir) {
		if (!outputDir.exists())
			outputDir.mkdirs();
		if (!extensionDir.exists())
			extensionDir.mkdirs();
	}

	/**
	 * Installs the serialized extension files from the output directory into the
	 * extension directory.
	 *
	 * @param outputDir
	 *            The directory containing the serialized extension files.
	 * @param extensionDir
	 *            The directory where the extension files will be installed.
	 */
	public static void installFiles(File outputDir, File extensionDir) {
		System.out.println("> Task :extension:installExtension\n");

		for (File file : outputDir.listFiles()) {
			Path source = Path.of(file.toURI());
			Path destination = Path.of(new File(extensionDir, file.getName()).toURI());

			if (file.isDirectory())
				continue;

			try {
				System.out.println("Installing ".concat(file.getName()).concat(" in your file system."));
				Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Serializes the given extension bundle object into the specified file path.
	 *
	 * @param object
	 *            The extension bundle object to be serialized.
	 * @param path
	 *            The file path where the serialized object will be saved.
	 * @param taskName
	 *            The name of the task associated with the extension.
	 * @throws Exception
	 *             if serialization fails.
	 */
	public static void serialize(Object object, File path, String taskName) throws Exception {
		try (FileOutputStream fileOutputStream = new FileOutputStream(path);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

			objectOutputStream.writeObject(object);
			System.out.println("> Task :extension:".concat(taskName));
		} catch (Exception e) {
			System.out.println("Failed to serialize ".concat(path.getAbsolutePath()));
			throw e;
		}
	}

	/**
	 * Extracts the task name from the given file name by removing the file
	 * extension and converting
	 * it into camel case.
	 *
	 * @param fileName
	 *            The file name to extract the task name from.
	 * @return The extracted task name in camel case format.
	 */
	public static String extractTaskName(String fileName) {
		String baseName = fileName.replace(".extaas", "");
		String[] words = baseName.split("(?=[A-Z])|_");

		StringBuilder taskName = new StringBuilder("generate");
		for (String word : words) {
			if (!word.isEmpty()) {
				taskName.append(Character.toUpperCase(word.charAt(0)));
				if (word.length() > 1) {
					taskName.append(word.substring(1).toLowerCase());
				}
			}
		}
		return taskName.toString();
	}
}
