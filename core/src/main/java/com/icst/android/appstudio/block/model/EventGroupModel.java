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

package com.icst.android.appstudio.block.model;

import java.io.Serializable;
import java.util.ArrayList;

public class EventGroupModel implements Serializable, Cloneable {
	public static final long serialVersionUID = 12L;

	private String name;
	private String title;
	private String description;
	private String rawCode;
	private String replacer;
	private ArrayList<Event> events;

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

	public String getReplacer() {
		return this.replacer;
	}

	public void setReplacer(String replacer) {
		this.replacer = replacer;
	}

	public ArrayList<Event> getEvents() {
		return this.events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	@Override
	public EventGroupModel clone() {
		EventGroupModel eventGroupModel = new EventGroupModel();
		eventGroupModel.setName(this.name != null ? new String(this.name) : null);
		eventGroupModel.setTitle(this.title != null ? new String(this.title) : null);
		eventGroupModel.setDescription(this.description != null ? new String(this.description) : null);
		eventGroupModel.setRawCode(this.rawCode != null ? new String(this.rawCode) : null);
		eventGroupModel.setReplacer(this.replacer != null ? new String(this.replacer) : null);
		ArrayList<Event> clonedEvents = new ArrayList<Event>();
		for (int eventCount = 0; eventCount < events.size(); ++eventCount) {
			clonedEvents.add(events.get(eventCount));
		}
		eventGroupModel.setEvents(clonedEvents);
		return eventGroupModel;
	}
}
