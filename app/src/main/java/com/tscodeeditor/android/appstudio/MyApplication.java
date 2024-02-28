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

package com.tscodeeditor.android.appstudio;

import android.app.Application;
import android.content.Context;
import com.tscodeeditor.android.appstudio.utils.EnvironmentUtils;

public class MyApplication extends Application {
  private static Context mApplicationContext;

  public static Context getContext() {
    return mApplicationContext;
  }

  @Override
  public void onCreate() {
    mApplicationContext = getApplicationContext();
    EnvironmentUtils.init(this);
    super.onCreate();
  }
}
