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
