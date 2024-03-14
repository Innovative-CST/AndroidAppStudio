package com.tscodeeditor.android.appstudio.block.model;

import java.io.Serializable;

public class Event implements Serializable, Cloneable {
  private String name;

  @Override
  public Event clone() throws CloneNotSupportedException {
    Event event = new Event();
    event.setName(new String(getName()));
    return event;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
