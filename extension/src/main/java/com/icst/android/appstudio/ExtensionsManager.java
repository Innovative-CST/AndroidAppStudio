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

import java.util.ArrayList;
import java.util.HashMap;

import com.icst.android.appstudio.extensions.activityextension.ActivityExtension;
import com.icst.android.appstudio.extensions.basicvariables.BasicVariablesExtensions;
import com.icst.android.appstudio.extensions.commentextension.CommentExtension;
import com.icst.android.appstudio.extensions.controlextension.ControlExtension;
import com.icst.android.appstudio.extensions.controlextension.OperatorExtension;

public class ExtensionsManager {
	public static final String EXTENSION_FILE_NAME = "extensionFileName";
	public static final String EXTENSION_BUNDLE = "extensionBundle";

	public static ArrayList<HashMap<String, Object>> getExtensions() throws Exception {
		/*
		 * MAKE YOUR EXTENSION LIST HERE
		 */

		ArrayList<HashMap<String, Object>> extensions = new ArrayList<HashMap<String, Object>>();

		{
			HashMap<String, Object> extension = new HashMap<String, Object>();
			extension.put(EXTENSION_FILE_NAME, "ControlBlocks.extaas");
			extension.put(EXTENSION_BUNDLE, ControlExtension.getExtensionBundle());
			extensions.add(extension);
		}

		{
			HashMap<String, Object> extension = new HashMap<String, Object>();
			extension.put(EXTENSION_FILE_NAME, "OperatorBlocks.extaas");
			extension.put(EXTENSION_BUNDLE, OperatorExtension.getExtensionBundle());
			extensions.add(extension);
		}

		{
			HashMap<String, Object> extension = new HashMap<String, Object>();
			extension.put(EXTENSION_FILE_NAME, "ActivityEvents.extaas");
			extension.put(EXTENSION_BUNDLE, ActivityExtension.getExtensionBundle());
			extensions.add(extension);
		}

		{
			HashMap<String, Object> extension = new HashMap<String, Object>();
			extension.put(EXTENSION_FILE_NAME, "BasicVariable.extaas");
			extension.put(EXTENSION_BUNDLE, BasicVariablesExtensions.getExtensionBundle());
			extensions.add(extension);
		}

		{
			HashMap<String, Object> extension = new HashMap<String, Object>();
			extension.put(EXTENSION_FILE_NAME, "CommentExtension.extaas");
			extension.put(EXTENSION_BUNDLE, CommentExtension.getExtensionBundle());
			extensions.add(extension);
		}

		return extensions;
	}
}
