/*
 *  This file is part of Android Code Editor.
 *
 *  Android Code Editor is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Android Code Editor is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Android Code Editor.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.editor.editors.sora.lang.textmate.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.res.AssetManager;

import io.github.rosemoe.sora.langs.textmate.registry.GrammarRegistry;

public class TextMateProvider {
	public static void loadGrammars() {
		GrammarRegistry.getInstance().loadGrammars("Editor/SoraEditor/languages.json");
	}

	public static String getLanguageScope(Context context, String fileExt) {
		Map<String, String> scopes;
		var type = new TypeToken<Map<String, String>>() {
		}.getType();
		scopes = new Gson()
				.fromJson(
						readFileFromAssets(context.getAssets(), "Editor/SoraEditor/language_scopes.json"),
						type);
		return scopes.get(fileExt);
	}

	public static String readFileFromAssets(AssetManager assetManager, String fileName) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			InputStream inputStream = assetManager.open(fileName);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append("\n");
			}

			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}
}
