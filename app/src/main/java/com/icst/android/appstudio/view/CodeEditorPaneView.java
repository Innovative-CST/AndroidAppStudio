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

package com.icst.android.appstudio.view;

import java.io.File;
import java.util.ArrayList;

import com.blankj.utilcode.util.FileIOUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.BaseActivity;
import com.icst.android.appstudio.interfaces.WorkSpacePane;
import com.icst.android.appstudio.utils.FileIconUtils;
import com.icst.android.appstudio.viewmodel.ActionButton;
import com.icst.editor.editors.sora.lang.textmate.provider.TextMateProvider;
import com.icst.editor.tools.Language;
import com.icst.editor.tools.Themes;
import com.icst.editor.widget.CodeEditorLayout;

import android.code.editor.common.utils.FileUtils;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;

import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.provider.AssetsFileResolver;

/*
 * A WorkSpacePane for Code Editor.
 */

public abstract class CodeEditorPaneView extends LinearLayout implements WorkSpacePane {

	private BaseActivity activity;
	private CodeEditorLayout codeEditor;
	private File file;
	private ArrayList<ActionButton> actionButtons;
	private DrawerLayout drawer;
	private NavigationView mNavigationView;
	private LinearLayout mNavigationViewItemListView;

	public CodeEditorPaneView(BaseActivity activity, File file) {
		super(activity);
		this.activity = activity;
		this.file = file;

		drawer = new DrawerLayout(getContext());
		mNavigationView = new NavigationView(getContext());
		codeEditor = new CodeEditorLayout(getContext());
		mNavigationViewItemListView = new LinearLayout(getContext());
		mNavigationViewItemListView.setOrientation(LinearLayout.VERTICAL);
		// Set up LayoutParams for NavigationView
		DrawerLayout.LayoutParams mNavigationViewLayoutParams = new DrawerLayout.LayoutParams(96,
				DrawerLayout.LayoutParams.MATCH_PARENT);
		mNavigationViewLayoutParams.gravity = Gravity.END;

		// Set up LayoutParams for the CodeEditorLayout
		DrawerLayout.LayoutParams editorLayoutParams = new DrawerLayout.LayoutParams(
				DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT);

		initEditor();
		setupDrawerLayout();
		addActionButtonsToList();
		addActionButtonsToNavigationView();

		// Configure the drawer layout
		drawer.addView(mNavigationView);
		drawer.addView(codeEditor);
		codeEditor.setLayoutParams(editorLayoutParams);
		mNavigationView.addView(mNavigationViewItemListView);
		mNavigationView.setLayoutParams(mNavigationViewLayoutParams);
		mNavigationView.bringToFront();
	}

	private void setupDrawerLayout() {
		LinearLayout.LayoutParams drawerLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		drawer.setLayoutParams(drawerLayoutParams);
		addView(drawer);
	}

	private void addActionButtonsToList() {
		actionButtons = new ArrayList<ActionButton>();
		ActionButton save = new ActionButton() {
			@Override
			public void onClick() {
				FileIOUtils.writeFileFromString(file, codeEditor.getText().toString());
				Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
			}
		};
		save.setIcon(R.drawable.content_save_outline);
		save.setText("Save");
		actionButtons.add(save);

		ActionButton closeWorkspacePane = new ActionButton() {
			@Override
			public void onClick() {
				onReleaseRequest();
			}
		};
		closeWorkspacePane.setIcon(R.drawable.ic_close_outline);
		closeWorkspacePane.setText("Close");
		actionButtons.add(closeWorkspacePane);
	}

	private void addActionButtonsToNavigationView() {
		actionButtons.forEach(
				actionButton -> {
					ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					ActionButtonView actionButtonView = new ActionButtonView(getContext(), actionButton);
					actionButtonView.setLayoutParams(lp);
					mNavigationViewItemListView.addView(actionButtonView);
				});
	}

	public void initEditor() {
		FileProviderRegistry.getInstance()
				.addFileProvider(new AssetsFileResolver(activity.getAssets()));
		try {
			TextMateProvider.loadGrammars();
		} catch (Exception e) {
		}

		codeEditor.setLanguageMode(getLanguageMode());
		if (activity.getSetting().isEnabledDarkMode()) {
			codeEditor.setTheme(Themes.SoraEditorTheme.Dark.Solarized_Drak);
		} else {
			codeEditor.setTheme(Themes.SoraEditorTheme.Light.Solarized_Light);
		}
		codeEditor.setText(FileUtils.readFile(file.getAbsolutePath()));
	}

	public boolean isFileSaved() {
		return FileIOUtils.readFile2String(file).equals(codeEditor.getText().toString());
	}

	public String getLanguageMode() {
		return switch (FileUtils.getPathFormat(file.getAbsolutePath())) {
			case "java" -> Language.Java;
			case "kt" -> Language.Kt;
			case "js" -> Language.JavaScript;
			case "html" -> Language.HTML;
			case "css" -> Language.CSS;
			case "xml" -> Language.XML;
			case "md" -> Language.Markdown;
			case "json" -> Language.JSON;
			case "gradle" -> Language.GRADLE;
			case "sh" -> Language.SHELLSCRIPT;
			default -> Language.UNKNOWN;
		};
	}

	@Override
	public Drawable getWorkSpacePaneIcon() {
		return FileIconUtils.getFileIcon(file, activity);
	}

	@Override
	public String getWorkSpacePaneName() {
		return FileUtils.getLatSegmentOfFilePath(file.getAbsolutePath());
	}

	@Override
	public Drawable getWorkSpaceStatus() {
		return null;
	}

	@Override
	public abstract void onRelease();

	@Override
	public void onReleaseRequest() {
		if (isFileSaved()) {
			onRelease();
		} else {
			MaterialAlertDialogBuilder confirmCloseWithSaveDialog = new MaterialAlertDialogBuilder(getContext());
			confirmCloseWithSaveDialog.setTitle("Unsaved Files");
			confirmCloseWithSaveDialog.setMessage(
					"This file is not saved, do you want to save it before closing this file workspace?");
			confirmCloseWithSaveDialog.setPositiveButton(
					"Save",
					(arg0, arg1) -> {
						FileIOUtils.writeFileFromString(file, codeEditor.getText().toString());
						Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
						onRelease();
					});
			confirmCloseWithSaveDialog.setNegativeButton(
					"Cancel Save",
					(arg0, arg1) -> {
						onRelease();
					});
			confirmCloseWithSaveDialog.show();
		}
	}

	public File getEditorFile() {
		return this.file;
	}
}
