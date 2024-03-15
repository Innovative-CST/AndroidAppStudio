/*
 *  This file is part of Android AppStudio.
 *
 *  Android AppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Android AppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Android AppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.tscodeeditor.android.appstudio.block.model;

import java.io.Serializable;
import java.util.ArrayList;

public class FileModel implements Serializable {
  private static final long serialVersionUID = 021173503L;

  private String fileName;
  private String fileExtension;
  private String rawCode;
  private ArrayList<Event> defaultBuiltInEvents;
  private boolean isFolder;

  public String getFileName() {
    return this.fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileExtension() {
    return this.fileExtension;
  }

  public void setFileExtension(String fileExtension) {
    this.fileExtension = fileExtension;
  }

  public String getRawCode() {
    return this.rawCode;
  }

  public void setRawCode(String rawCode) {
    this.rawCode = rawCode;
  }

  public ArrayList<Event> getDefaultBuiltInEvents() {
    return this.defaultBuiltInEvents;
  }

  public void setDefaultBuiltInEvents(ArrayList<Event> defaultBuiltInEvents) {
    this.defaultBuiltInEvents = defaultBuiltInEvents;
  }

  public boolean getIsFolder() {
    return this.isFolder;
  }

  public void setIsFolder(boolean isFolder) {
    this.isFolder = isFolder;
  }
}
