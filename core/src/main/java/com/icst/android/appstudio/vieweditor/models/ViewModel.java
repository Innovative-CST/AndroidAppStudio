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

package com.icst.android.appstudio.vieweditor.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewModel implements Serializable {
  public static final long serialVersionUID = 15L;

  private String viewClass;
  private boolean isRootElement;
  private ArrayList<AttributesModel> attributes;
  private ArrayList<ViewModel> childs;

  public String getCode(String whitespace, LayoutModel layout) {
    StringBuilder code = new StringBuilder();
    if (isRootElement) {
      code.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    }
    code.append(whitespace);
    code.append("<");
    code.append(viewClass);
    if (isRootElement) {
      if (layout.isAndroidNameSpaceUsed()) {
		  code.append("\n\t");
		  code.append("xmlns:android");
		  code.append(String.valueOf("=\""));
		  code.append("http://schemas.android.com/apk/res/android");
		  code.append(String.valueOf("\""));
	  }
	  if (layout.isAppNameSpaceUsed()) {
		  code.append("\n\t");
		  code.append("xmlns:app");
		  code.append(String.valueOf("=\""));
		  code.append("http://schemas.android.com/apk/res-auto");
		  code.append(String.valueOf("\""));
	  }
	  if (layout.isAndroidNameSpaceUsed()) {
		  code.append("\n\t");
		  code.append("xmlns:tools");
		  code.append(String.valueOf("=\""));
		  code.append("http://schemas.android.com/tools");
		  code.append(String.valueOf("\""));
	  }
    }
    if (attributes == null && childs == null) {
      code.append("/>");
    } else if (attributes == null) {
      code.append(">\n");
      for (int i = 0; i < childs.size(); ++i) {
        code.append(childs.get(i).getCode(whitespace.concat("\t"), layout));
        code.append("\n");
      }
      code.append(whitespace);
      code.append("</");
      code.append(viewClass);
      code.append(">");
    } else if (attributes != null && childs != null) {
      for (int i = 0; i < attributes.size(); ++i) {
        if (i == 0) {
          code.append("\n");
          code.append(whitespace.concat("\t"));
        }

        code.append(attributes.get(i).getAttribute());
        code.append(String.valueOf("=\""));
        code.append(String.valueOf(attributes.get(i).getAttributeValue()));
        code.append(String.valueOf("\""));

        if (i != (attributes.size() - 1)) {
          code.append("\n");
          code.append(whitespace.concat("\t"));
        }
      }

      code.append(">\n");
      for (int i = 0; i < childs.size(); ++i) {
        code.append(childs.get(i).getCode(whitespace.concat("\t"), layout));
        code.append("\n");
      }
      code.append(whitespace);
      code.append("</");
      code.append(viewClass);
      code.append(">");
    } else if (childs == null) {
      for (int i = 0; i < attributes.size(); ++i) {
        if (i == 0) {
          code.append("\n");
          code.append(whitespace.concat("\t"));
        }

        code.append(attributes.get(i).getAttribute());
        code.append(String.valueOf("=\""));
        code.append(String.valueOf(attributes.get(i).getAttributeValue()));
        code.append(String.valueOf("\""));

        if (i != (attributes.size() - 1)) {
          code.append("\n");
          code.append(whitespace.concat("\t"));
        }
      }

      code.append("/>");
    }

    return code.toString();
  }

  public void setClass(String viewClass) {
    this.viewClass = viewClass;
  }

  public ViewModel cloneViewModel() {
    return null;
  }

  public boolean getIsRootElement() {
    return this.isRootElement;
  }

  public void setRootElement(boolean isRootElement) {
    this.isRootElement = isRootElement;
  }

  public ArrayList<AttributesModel> getAttributes() {
    return this.attributes;
  }

  public void setAttributes(ArrayList<AttributesModel> attributes) {
    this.attributes = attributes;
  }

  public ArrayList<ViewModel> getChilds() {
    return this.childs;
  }

  public void setChilds(ArrayList<ViewModel> childs) {
    this.childs = childs;
  }
}
