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

public class Event implements Serializable, Cloneable {
  private String name;
  private String title;
  private String description;
  private String rawCode;
  private String eventReplacer;
  private int icon;

  @Override
  public Event clone() throws CloneNotSupportedException {
    Event event = new Event();
    event.setName(new String(getName()));
    event.setTitle(new String(getTitle()));
    event.setDescription(new astring(getDescription()));
    return event;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getRawCode() {
    return this.rawCode;
  }

  public void setRawCode(String rawCode) {
    this.rawCode = rawCode;
  }

  public String getEventReplacer() {
    return this.eventReplacer;
  }

  public void setEventReplacer(String eventReplacer) {
    this.eventReplacer = eventReplacer;
  }

  public int getIcon() {
    return this.icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }
}
