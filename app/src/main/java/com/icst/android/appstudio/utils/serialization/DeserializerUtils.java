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

package com.icst.android.appstudio.utils.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class DeserializerUtils {
	/*
	 * Constants for the error code.
	 */
	public static final int FILE_NOT_FOUND = 0;
	public static final int PATH_IS_DIRECTORY = 1;
	public static final int ERROR_OCCURED_WHILE_DESERIALIZING = 2;

	/*
	 * This method tries to deserialize file from a file path provided as argument.
	 * This method will call the DeserializerListener methods when necessary and the
	 * method will be terminated.
	 * The method can also fail but does not throw error directly but pass the error
	 * in the listener(DeserializerListener).
	 */
	public static void deserialize(File path, DeserializerListener listener) {
		/*
		 * Check whether the path provided exists or not.
		 * If path does not exists then:
		 * - Call DeserializerListener#onFailed.
		 * - Terminate tge method from executing further.
		 */
		if (!path.exists()) {
			listener.onFailed(FILE_NOT_FOUND, new Exception("Path not found."));
			return;
		}
		/*
		 * Check whether the path provided is a directory or not.
		 * If path is directory then:
		 * - Call DeserializerListener#onFailed.
		 * - Terminate then method from executing further.
		 */
		if (path.isDirectory()) {
			listener.onFailed(
					PATH_IS_DIRECTORY, new Exception("Path is expected to be file but got directory."));
			return;
		}
		/*
		 * Try to Deserialize the path provided as argumemt.
		 */
		try {
			FileInputStream mFileInputStream = new FileInputStream(path);
			ObjectInputStream mObjectInputStream = new ObjectInputStream(mFileInputStream);
			Object mObject = mObjectInputStream.readObject();
			mFileInputStream.close();
			mObjectInputStream.close();
			/*
			 * Method successfully executed with any errors.
			 * Parse the deserialized object to the listener.
			 */
			listener.onSuccessfullyDeserialized(mObject);
		} catch (Exception e) {
			/*
			 * This method is failed to Deserialize the file at path.
			 * Parsing the error to listener.
			 */
			listener.onFailed(ERROR_OCCURED_WHILE_DESERIALIZING, e);
		}
	}

	public static Object deserialize(File path) {
		if (!path.exists())
			return null;

		if (path.isDirectory())
			return null;

		try {
			FileInputStream mFileInputStream = new FileInputStream(path);
			ObjectInputStream mObjectInputStream = new ObjectInputStream(mFileInputStream);
			Object mObject = mObjectInputStream.readObject();
			mFileInputStream.close();
			mObjectInputStream.close();
			return mObject;
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> T deserialize(File path, Class<T> objectClass) {
		if (!path.exists())
			return null;

		if (path.isDirectory())
			return null;

		try {
			FileInputStream mFileInputStream = new FileInputStream(path);
			ObjectInputStream mObjectInputStream = new ObjectInputStream(mFileInputStream);
			Object mObject = mObjectInputStream.readObject();
			mFileInputStream.close();
			mObjectInputStream.close();
			if (objectClass.isInstance(mObject)) {
				return (T) mObject;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public interface DeserializerListener {
		void onSuccessfullyDeserialized(Object object);

		void onFailed(int errorCode, Exception e);
	}
}
