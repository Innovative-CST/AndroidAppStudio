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

package com.icst.android.appstudio.extensions.controlextension;

import java.util.ArrayList;

import com.icst.android.appstudio.block.model.BlockHolderModel;

public final class ControlHolder {
	public static ArrayList<BlockHolderModel> getHolders() {
		ArrayList<BlockHolderModel> holders = new ArrayList<BlockHolderModel>();

		holders.add(getControlBlockHolder());

		return holders;
	}

	private static BlockHolderModel getControlBlockHolder() {
		BlockHolderModel controlHolder = new BlockHolderModel();
		controlHolder.setName("Control");
		controlHolder.setColor("#17A2AF");
		return controlHolder;
	}
}
