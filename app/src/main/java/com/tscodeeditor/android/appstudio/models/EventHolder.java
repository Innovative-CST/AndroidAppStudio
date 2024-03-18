package com.tscodeeditor.android.appstudio.models;

import java.io.File;
import java.io.Serializable;

public class EventHolder implements Serializable, Cloneable {
  private static final long serialVersionUID = 021173505L;

  private String holderName;
  private String icon;
  private File filePath;
  private boolean isBuiltInEvents;

  public String getHolderName() {
    return this.holderName;
  }

  public void setHolderName(String holderName) {
    this.holderName = holderName;
  }

  public String getIcon() {
    return this.icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  @Override
  protected Object clone() {
    EventHolder eventHolder = new EventHolder();
    eventHolder.setHolderName(new String(getHolderName() != null ? getHolderName() : ""));
    eventHolder.setHolderName(new String(getIcon()));
    return eventHolder;
  }

  public boolean isBuiltInEvents() {
    return this.isBuiltInEvents;
  }

  public void setBuiltInEvents(boolean isBuiltInEvents) {
    this.isBuiltInEvents = isBuiltInEvents;
  }

  public File getFilePath() {
    return this.filePath;
  }

  public void setFilePath(File filePath) {
    this.filePath = filePath;
  }
}
