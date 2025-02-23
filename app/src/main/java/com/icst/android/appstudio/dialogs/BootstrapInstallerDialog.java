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

import java.util.concurrent.CompletableFuture;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.activities.BaseActivity;
import com.icst.android.appstudio.databinding.DialogBootstrapInstallerBinding;
import com.icst.android.appstudio.utils.BootstrapInstallerUtils;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.editor.editors.sora.lang.textmate.provider.TextMateProvider;
import com.icst.editor.tools.Themes;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.provider.AssetsFileResolver;

public class BootstrapInstallerDialog extends MaterialAlertDialogBuilder {
	private Activity activity;
	private AlertDialog dialog;
	private DialogBootstrapInstallerBinding binding;

	public BootstrapInstallerDialog(
			BaseActivity activity, BootstrapInstallCompletionListener listener) {
		super(activity);
		this.activity = activity;
		binding = DialogBootstrapInstallerBinding.inflate(LayoutInflater.from(activity));

		FileProviderRegistry.getInstance()
				.addFileProvider(new AssetsFileResolver(activity.getAssets()));
		try {
			TextMateProvider.loadGrammars();
		} catch (Exception e) {
		}

		binding.editor.setEditable(false);
		if (activity.getSetting().isEnabledDarkMode()) {
			binding.editor.setTheme(Themes.SoraEditorTheme.Dark.Monokai);
		} else {
			binding.editor.setTheme(Themes.SoraEditorTheme.Light.Default);
		}

		setView(binding.getRoot());
		setTitle("Bootstrap Installation");
		setCancelable(false);
		StringBuilder log = new StringBuilder();

		final CompletableFuture<Void> future = BootstrapInstallerUtils.install(
				activity,
				new BootstrapInstallerUtils.ProgressListener() {
					@Override
					public void onWarning(String warning) {
						log.append(warning);
						log.append("\n");
						activity.runOnUiThread(
								() -> {
									binding.editor.setText(log.toString());
								});
					}

					@Override
					public void onProgress(String message) {
						log.append(message);
						log.append("\n");
						activity.runOnUiThread(
								() -> {
									binding.editor.setText(log.toString());
								});
					}
				},
				EnvironmentUtils.PREFIX);

		future.whenComplete(
				(voidResult, throwable) -> {
					activity.runOnUiThread(
							() -> {
								dialog.dismiss();
								listener.onComplete();
							});
				});
		dialog = create();
		dialog.show();
	}

	public interface BootstrapInstallCompletionListener {
		void onComplete();
	}
}
