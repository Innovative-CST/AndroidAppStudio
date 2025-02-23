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

package com.icst.android.appstudio.dialogs;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.BaseActivity;
import com.icst.editor.editors.sora.lang.textmate.provider.TextMateProvider;
import com.icst.editor.tools.Themes;
import com.icst.editor.widget.CodeEditorLayout;

import android.app.Activity;
import android.widget.Toast;

import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.provider.AssetsFileResolver;

public class LayoutSourceViewerDialog extends MaterialAlertDialogBuilder {
	private Activity activity;
	private CodeEditorLayout editor;

	public LayoutSourceViewerDialog(BaseActivity activity, String code) {
		super(activity);
		this.activity = activity;
		FileProviderRegistry.getInstance()
				.addFileProvider(new AssetsFileResolver(activity.getAssets()));
		try {
			TextMateProvider.loadGrammars();
		} catch (Exception e) {
			Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		editor = new CodeEditorLayout(activity);
		editor.setEditable(false);
		if (activity.getSetting().isEnabledDarkMode()) {
			editor.setTheme(Themes.SoraEditorTheme.Dark.Monokai);
		} else {
			editor.setTheme(Themes.SoraEditorTheme.Light.Default);
		}

		editor.setLanguageMode("xml");
		editor.setText(code);
		setView(editor);
		setTitle(R.string.source_code);
		setPositiveButton(R.string.dismiss, (arg0, arg1) -> {
		});
	}
}
