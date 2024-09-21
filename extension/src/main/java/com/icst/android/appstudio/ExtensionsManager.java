package com.icst.android.appstudio;

import com.icst.android.appstudio.extensions.activityextension.ActivityExtension;
import com.icst.android.appstudio.extensions.basicvariables.BasicVariablesExtensions;
import com.icst.android.appstudio.extensions.controlextension.ControlExtension;
import com.icst.android.appstudio.extensions.controlextension.OperatorExtension;
import java.util.ArrayList;
import java.util.HashMap;

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

    return extensions;
  }
}
