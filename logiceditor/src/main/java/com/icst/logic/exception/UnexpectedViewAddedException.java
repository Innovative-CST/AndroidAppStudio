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

package com.icst.logic.exception;

import android.view.View;
import android.view.ViewGroup;

/** UnexpectedViewAddedException ia thrown, when a Unexpected view is tried to
 * add to a View like
 * ActionBlockDropZoneView, ExpressionBlockDropZoneView. */
public class UnexpectedViewAddedException extends RuntimeException {
	private ViewGroup parent;
	private View unexpectedView;

	public UnexpectedViewAddedException(ViewGroup parent, View unexpectedView) {
		super(
				new String("A unexpected view(")
						.concat(unexpectedView.getClass().getName())
						.concat(") is tried to add to the ")
						.concat(parent.getClass().getName()));
	}

	public ViewGroup getParent() {
		return this.parent;
	}

	public void setParent(ViewGroup parent) {
		this.parent = parent;
	}

	public View getUnexpectedView() {
		return this.unexpectedView;
	}

	public void setUnexpectedView(View unexpectedView) {
		this.unexpectedView = unexpectedView;
	}
}
