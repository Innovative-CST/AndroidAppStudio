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

import java.io.File;
import java.util.concurrent.Executors;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.activities.BaseActivity;
import com.icst.android.appstudio.databinding.DialogProjectBuilderBinding;
import com.icst.android.appstudio.exception.ProjectCodeBuildException;
import com.icst.android.appstudio.helper.ProjectCodeBuildProgress;
import com.icst.android.appstudio.helper.ProjectCodeBuilder;
import com.icst.android.appstudio.helper.ProjectCodeBuilderCancelToken;
import com.icst.android.appstudio.listener.ProjectCodeBuildListener;
import com.icst.android.appstudio.utils.TimeUtils;
import com.icst.editor.editors.sora.lang.textmate.provider.TextMateProvider;
import com.icst.editor.tools.Themes;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.provider.AssetsFileResolver;

public class ProjectBuilderDialog extends MaterialAlertDialogBuilder {
	private BaseActivity activity;
	private File outpurDir;
	private File file;
	private ProjectCodeBuildListener listener;
	private ProjectCodeBuilderCancelToken cancelToken;
	private DialogProjectBuilderBinding binding;

	public ProjectBuilderDialog(BaseActivity activity, File projectRootDir, String module) {
		super(activity);

		this.activity = activity;
		this.outpurDir = outpurDir;
		this.file = file;

		binding = DialogProjectBuilderBinding.inflate(LayoutInflater.from(activity));

		setView(binding.getRoot());

		FileProviderRegistry.getInstance()
				.addFileProvider(new AssetsFileResolver(activity.getAssets()));
		try {
			TextMateProvider.loadGrammars();
		} catch (Exception e) {
			Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		var editor = binding.editor;
		editor.setEditable(false);

		if (activity.getSetting().isEnabledDarkMode()) {
			editor.setTheme(Themes.SoraEditorTheme.Dark.Monokai);
		} else {
			editor.setTheme(Themes.SoraEditorTheme.Light.Default);
		}
		binding.indicator.setVisibility(View.GONE);

		StringBuilder log = new StringBuilder();
		listener = new ProjectCodeBuildListener() {

			@Override
			public void onBuildStart() {
				activity.runOnUiThread(
						() -> {
							binding.indicator.setVisibility(View.VISIBLE);
							binding.currentLog.setVisibility(View.VISIBLE);
							binding.editor.setVisibility(View.GONE);
						});
			}

			@Override
			public void onBuildComplete(long buildTime) {
				activity.runOnUiThread(
						() -> {
							log.append("\n");
							log.append("Build successful in ".concat(TimeUtils.convertTime(buildTime)));
							log.append("\n");
							binding.indicator.setVisibility(View.GONE);
							binding.currentLog.setVisibility(View.GONE);
							binding.editor.setVisibility(View.VISIBLE);
							editor.setText(log.toString());
						});
			}

			@Override
			public void onBuildProgress(ProjectCodeBuildProgress progress) {
				log.append(progress.getMessage());
				log.append("\n");
				activity.runOnUiThread(
						() -> {
							binding.currentLog.setText(progress.getMessage());
						});
			}

			@Override
			public void onBuildProgressLog(String logMessage) {
				log.append(logMessage);
				log.append("\n");
				activity.runOnUiThread(
						() -> {
							binding.currentLog.setText(logMessage);
						});
			}

			@Override
			public void onBuildCancelled() {
				activity.runOnUiThread(
						() -> {
							binding.indicator.setVisibility(View.GONE);
							binding.currentLog.setVisibility(View.GONE);
							binding.editor.setVisibility(View.VISIBLE);
							editor.setText(log.toString());
						});
			}

			@Override
			public void onBuildFailed(ProjectCodeBuildException e, long buildTime) {
				log.append(e.getMessage());
				log.append("\n");
				activity.runOnUiThread(
						() -> {
							log.append("\n");
							log.append("Build failed in ".concat(TimeUtils.convertTime(buildTime)));
							log.append("\n");
							binding.indicator.setVisibility(View.GONE);
							binding.currentLog.setVisibility(View.GONE);
							binding.editor.setVisibility(View.VISIBLE);
							editor.setText(log.toString());
						});
			}
		};
		setCancelable(false);
		setPositiveButton("Cancel", (p1, p2) -> {
		});
		Executors.newSingleThreadExecutor()
				.execute(
						() -> {
							ProjectCodeBuilder.generateModulesCode(
									projectRootDir, module, true, listener, cancelToken);
						});
	}
}
