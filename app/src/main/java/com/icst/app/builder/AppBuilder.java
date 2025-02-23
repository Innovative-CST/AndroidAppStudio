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

package com.icst.app.builder;

import com.icst.android.appstudio.models.ModuleModel;
import com.icst.app.compiler.AAPT2Compiler;
import com.icst.app.compiler.progress.BuildEventProgressListener;

public class AppBuilder {
	public void build(BuildListener listener, ModuleModel module) {
		if (listener != null) {
			listener.onBuildProgress("AAPT2 > Executing AAPT2 Compiler....");
		}

		AAPT2Compiler aapt2 = new AAPT2Compiler(
				module,
				new BuildEventProgressListener() {
					@Override
					public void onProgress(String message) {
						if (listener != null) {
							listener.onBuildProgress(message);
						}
					}
				});
		aapt2.compile();

		if (listener != null) {
			listener.onBuildFinish();
		}
	}
}
