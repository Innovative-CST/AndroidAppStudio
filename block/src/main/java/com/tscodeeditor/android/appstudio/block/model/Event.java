package com.tscodeeditor.android.appstudio.block.model;

import java.io.Serializable;

public class Event implements Serializable, Cloneable {

  @Override
  public Event clone() throws CloneNotSupportedException {
    Event event = new Event();
    return event;
  }
}
