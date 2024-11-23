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

package com.icst.editor.widget;

import org.eclipse.tm4e.core.registry.IThemeSource;

import com.icst.editor.editors.sora.lang.textmate.AndroidCodeEditorTMLanguage;
import com.icst.editor.editors.sora.lang.textmate.provider.TextMateProvider;
import com.icst.editor.tools.Language;
import com.icst.editor.tools.Themes;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import io.github.rosemoe.sora.langs.textmate.TextMateColorScheme;
import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.ThemeRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.model.ThemeModel;
import io.github.rosemoe.sora.widget.CodeEditor;

public class CodeEditorLayout extends CodeEditor {
	private Context context;

	public CodeEditorLayout(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public CodeEditorLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		Typeface font = Typeface.createFromAsset(context.getAssets(), "Editor/SoraEditor/jetbrains.ttf");
		setTypefaceText(font);
	}

	public void setLanguageMode(String LanguageMode) {
		if (!LanguageMode.equals(Language.UNKNOWN)) {
			setEditorLanguage(
					AndroidCodeEditorTMLanguage.create(
							TextMateProvider.getLanguageScope(context, LanguageMode)));
		}
	}

	public void setTheme(String theme) {
		String ThemeFile;
		String ThemeFileName;
		switch (theme) {
			case Themes.SoraEditorTheme.Dark.Darcula:
				ThemeFile = "Editor/SoraEditor/darcula.json";
				ThemeFileName = "darcula.json";
				initTheme(ThemeFile, ThemeFileName);
				break;
			case Themes.SoraEditorTheme.Dark.Monokai:
				ThemeFile = "Editor/SoraEditor/monokai-color-theme.json";
				ThemeFileName = "monokai-color-theme.json";
				initTheme(ThemeFile, ThemeFileName);
				break;
			case Themes.SoraEditorTheme.Dark.Solarized_Drak:
				ThemeFile = "Editor/SoraEditor/solarized_drak.json";
				ThemeFileName = "solarized_drak.json";
				initTheme(ThemeFile, ThemeFileName);
				break;
			case Themes.SoraEditorTheme.Light.Quietlight:
				ThemeFile = "Editor/SoraEditor/quietlight.json";
				ThemeFileName = "quietlight.json";
				initTheme(ThemeFile, ThemeFileName);
				break;
			case Themes.SoraEditorTheme.Light.Solarized_Light:
				ThemeFile = "Editor/SoraEditor/solarized-light-color-theme.json";
				ThemeFileName = "solarized-light-color-theme.json";
				initTheme(ThemeFile, ThemeFileName);
				break;
		}
	}

	public void initTheme(String p1, String p2) {
		try {
			setColorScheme(
					new TextMateColorScheme(
							ThemeRegistry.getInstance(),
							new ThemeModel(
									IThemeSource.fromInputStream(
											FileProviderRegistry.getInstance().tryGetInputStream(p1), p1, null),
									p1)));
		} catch (Exception e) {
		}
	}
}
