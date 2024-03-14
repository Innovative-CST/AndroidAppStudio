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

package com.tscodeeditor.android.appstudio.utils.builtin;

import com.tscodeeditor.android.appstudio.block.model.Event;
import com.tscodeeditor.android.appstudio.block.model.FileModel;
import com.tscodeeditor.android.appstudio.block.utils.RawCodeReplacer;
import com.tscodeeditor.android.appstudio.models.ProjectModel;
import java.util.ArrayList;

public class GradleFilesInitializer {
  public static FileModel getAppModuleGradleFileModule(ProjectModel project) {
    FileModel appModuleGradleFile = new FileModel();
    appModuleGradleFile.setFileName("build");
    appModuleGradleFile.setFileExtension("gradle");

    StringBuilder appModuleGradleFileRawCode = new StringBuilder();
    appModuleGradleFileRawCode.append("plugins {\n\tid 'com.android.application'\n}\n");
    appModuleGradleFileRawCode.append(RawCodeReplacer.getReplacer("androidBlock"));
    appModuleGradleFileRawCode.append(RawCodeReplacer.getReplacer("dependenciesBlock"));

    appModuleGradleFile.setRawCode(appModuleGradleFileRawCode.toString());

    ArrayList<Event> builtinEvents = new ArrayList<Event>();

    Event androidBlockEvent = new Event();
    androidBlockEvent.setTitle("App Configration");
    androidBlockEvent.setName("androidBlock");
    androidBlockEvent.setDescription("Contains basic defination of your app");
    androidBlockEvent.setEventReplacer("blockCode");
    androidBlockEvent.setRawCode("android {\n" + RawCodeReplacer.getReplacer("blockCode") + "\n}");

    Event dependenciesBlockEvent = new Event();
    dependenciesBlockEvent.setTitle("App Libraries");
    dependenciesBlockEvent.setName("dependenciesBlock");
    dependenciesBlockEvent.setDescription("Contains library used by your app");
    dependenciesBlockEvent.setEventReplacer("blockCode");
    dependenciesBlockEvent.setRawCode(
        "dependencies {\n" + RawCodeReplacer.getReplacer("blockCode") + "\n}");

    builtinEvents.add(androidBlockEvent);
    builtinEvents.add(dependenciesBlockEvent);

    appModuleGradleFile.setDefaultBuiltInEvents(builtinEvents);

    return appModuleGradleFile;
  }
}
