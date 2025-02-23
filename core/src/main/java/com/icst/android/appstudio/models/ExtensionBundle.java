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

package com.icst.android.appstudio.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.icst.android.appstudio.block.model.BlockHolderModel;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.model.Event;
import com.icst.android.appstudio.block.model.VariableModel;

public class ExtensionBundle implements Serializable {

	public static final long serialVersionUID = 23L;

	private String name;
	private int version;
	private ArrayList<Event> events;
	private ArrayList<BlockModel> blocks;
	private ArrayList<BlockHolderModel> blockHolders;
	private ArrayList<EventHolder> eventHolders;
	private ArrayList<VariableModel> variables;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public ArrayList<Event> getEvents() {
		return this.events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	public ArrayList<BlockModel> getBlocks() {
		return this.blocks;
	}

	public void setBlocks(ArrayList<BlockModel> blocks) {
		this.blocks = blocks;
	}

	public ArrayList<BlockHolderModel> getHolders() {
		return this.blockHolders;
	}

	public void setHolders(ArrayList<BlockHolderModel> blockHolders) {
		this.blockHolders = blockHolders;
	}

	public ArrayList<VariableModel> getVariables() {
		return this.variables;
	}

	public void setVariables(ArrayList<VariableModel> variables) {
		this.variables = variables;
	}

	public ArrayList<EventHolder> getEventHolders() {
		return this.eventHolders;
	}

	public void setEventHolders(ArrayList<EventHolder> eventHolders) {
		this.eventHolders = eventHolders;
	}
}
