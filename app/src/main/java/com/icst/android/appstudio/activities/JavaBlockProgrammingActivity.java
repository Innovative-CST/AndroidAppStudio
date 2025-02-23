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

package com.icst.android.appstudio.activities;

import java.util.ArrayList;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.beans.BlockPaletteBean;
import com.icst.android.appstudio.databinding.ActivityJavaBlockProgrammingBinding;
import com.icst.android.appstudio.javablocks.ClassBlockBeans;
import com.icst.android.appstudio.javablocks.ControlBlockBeans;
import com.icst.android.appstudio.javablocks.IOBlockBeans;
import com.icst.android.appstudio.javablocks.MainJavaEventBean;
import com.icst.android.appstudio.javablocks.OperatorBlockBeans;
import com.icst.editor.editors.sora.lang.textmate.provider.TextMateProvider;
import com.icst.editor.tools.Themes;
import com.icst.editor.widget.CodeEditorLayout;
import com.icst.logic.config.LogicEditorConfiguration;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.provider.AssetsFileResolver;

public class JavaBlockProgrammingActivity extends BaseActivity {
	private ActivityJavaBlockProgrammingBinding binding;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		binding = ActivityJavaBlockProgrammingBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		binding.toolbar.setTitle("Block Program");
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
		binding.logicEditor.openEventInCanva(
				MainJavaEventBean.getJavaMainEvent(), new LogicEditorConfiguration());
		ArrayList<BlockPaletteBean> palette = new ArrayList<BlockPaletteBean>();
		palette.add(IOBlockBeans.getIOBlockPalette());
		palette.add(OperatorBlockBeans.getOperatorBlockPalette());
		palette.add(ControlBlockBeans.getControlBlockPalette());
		palette.add(ClassBlockBeans.getClassBlockPalette());
		binding.logicEditor.preparePallete(palette);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_show_code, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		if (menuItem.getItemId() == R.id.show_source_code) {
			String code = binding.logicEditor.getPreparedEventBean().getProcessedCode();
			SourceCodeViewerDialog sourceCodeDialog = new SourceCodeViewerDialog(this, code);
			sourceCodeDialog.create().show();
		}

		return super.onOptionsItemSelected(menuItem);
	}

	public class SourceCodeViewerDialog extends MaterialAlertDialogBuilder {
		private Activity activity;
		private CodeEditorLayout editor;

		public SourceCodeViewerDialog(BaseActivity activity, String code) {
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
			editor.setLanguageMode("java");
			editor.setText(code);
			setView(editor);
			setTitle(R.string.source_code);
			setPositiveButton(R.string.dismiss, (arg0, arg1) -> {
			});
		}
	}
}
