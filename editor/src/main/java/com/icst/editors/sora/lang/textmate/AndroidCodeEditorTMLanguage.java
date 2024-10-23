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

package com.icst.editor.editors.sora.lang.textmate;

import io.github.rosemoe.sora.langs.textmate.TextMateLanguage;
import io.github.rosemoe.sora.langs.textmate.registry.GrammarRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.ThemeRegistry;
import org.eclipse.tm4e.core.grammar.IGrammar;
import org.eclipse.tm4e.languageconfiguration.model.LanguageConfiguration;

public class AndroidCodeEditorTMLanguage extends TextMateLanguage {
	public final String languageScope;

	public AndroidCodeEditorTMLanguage(
			IGrammar iGrammar,
			LanguageConfiguration languageConfiguration,
			ThemeRegistry themeRegistry,
			String scope) {
		super(iGrammar, languageConfiguration, null, themeRegistry, false);
		this.languageScope = scope;
	}

	public static AndroidCodeEditorTMLanguage create(String scope) {
		final GrammarRegistry grammarRegistry = GrammarRegistry.getInstance();
		IGrammar grammar = grammarRegistry.findGrammar(scope);

		if (grammar == null) {
			throw new IllegalArgumentException(
					"Language with scope name not found.Scope : ".concat(scope));
		}

		var languageConfiguration = grammarRegistry.findLanguageConfiguration(grammar.getScopeName());

		return new AndroidCodeEditorTMLanguage(
				grammar, languageConfiguration, ThemeRegistry.getInstance(), scope);
	}
}
