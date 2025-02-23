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

package com.icst.android.appstudio.beans.utils;

import java.util.ArrayList;

import com.icst.android.appstudio.beans.CloneableBean;
import com.icst.android.appstudio.beans.DatatypeBean;

public final class BeanArrayCloneUtils {
	public static <T extends CloneableBean> ArrayList<T> clone(ArrayList<T> beans) {
		if (beans == null)
			return null;
		ArrayList<T> clone = new ArrayList<>();
		for (int i = 0; i < beans.size(); ++i) {
			clone.add((T) beans.get(i).cloneBean());
		}
		return clone;
	}

	public static DatatypeBean[] cloneDatatypeBeanArray(DatatypeBean[] datatypeBean) {
		if (datatypeBean == null)
			return null;
		DatatypeBean[] clonedBeans = new DatatypeBean[datatypeBean.length];
		for (int i = 0; i < datatypeBean.length; ++i) {
			clonedBeans[i] = datatypeBean[i].cloneBean();
		}
		return clonedBeans;
	}
}
