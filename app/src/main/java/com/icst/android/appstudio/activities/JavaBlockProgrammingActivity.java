/*
 * This file is part of Android AppStudio [https://github.com/Innovative-CST/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.icst.android.appstudio.activities;

import java.util.ArrayList;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.beans.BlockPaletteBean;
import com.icst.android.appstudio.databinding.ActivityJavaBlockProgrammingBinding;
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
