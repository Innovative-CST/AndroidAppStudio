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
  private static final long serialVersionUID = 021173504L;

  private String name;
  private String title;
  private String description;
  private String rawCode;
  private String eventReplacer;
  private int icon;

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

  @Override
  public Event clone() {
    Event event = new Event();
    event.setName(getName() != null ? new String(getName()) : null);
    event.setTitle(getTitle() != null ? new String(getTitle()) : null);
    event.setDescription(getDescription() != null ? new String(getDescription()) : null);
    event.setRawCode(getRawCode() != null ? new String(getRawCode()) : null);
    event.setEventReplacer(getEventReplacer() != null ? new String(getEventReplacer()) : null);
    event.setIcon(new Integer(getIcon()));
    return event;
  }
}
