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

package com.icst.android.appstudio.view;

import android.code.editor.common.utils.FileUtils;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.drawerlayout.widget.DrawerLayout;
import com.blankj.utilcode.util.FileIOUtils;
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
import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.provider.AssetsFileResolver;
import java.io.File;
import java.util.ArrayList;

/*
 * A WorkSpacePane for Code Editor.
 */

public class CodeEditorPaneView extends LinearLayout implements WorkSpacePane {

  private BaseActivity activity;
  private CodeEditorLayout codeEditor;
  private File file;
  private ArrayList<ActionButton> actionButtons;
  private DrawerLayout drawer;
  private NavigationView mNavigationView;

  public CodeEditorPaneView(BaseActivity activity, File file) {
    super(activity);
    this.activity = activity;
    this.file = file;

    drawer = new DrawerLayout(getContext());
    mNavigationView = new NavigationView(getContext());
    codeEditor = new CodeEditorLayout(getContext());

    // Set up LayoutParams for NavigationView
    DrawerLayout.LayoutParams mNavigationViewLayoutParams =
        new DrawerLayout.LayoutParams(96, DrawerLayout.LayoutParams.MATCH_PARENT);
    mNavigationViewLayoutParams.gravity = Gravity.END;

    // Set up LayoutParams for the CodeEditorLayout
    DrawerLayout.LayoutParams editorLayoutParams =
        new DrawerLayout.LayoutParams(
            DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT);

    initEditor();
    setupDrawerLayout();
    addActionButtonsToList();
    addActionButtonsToNavigationView();

    // Configure the drawer layout
    drawer.addView(mNavigationView);
    drawer.addView(codeEditor);
    codeEditor.setLayoutParams(editorLayoutParams);
    mNavigationView.setLayoutParams(mNavigationViewLayoutParams);
    mNavigationView.bringToFront();
  }

  private void setupDrawerLayout() {
    LinearLayout.LayoutParams drawerLayoutParams =
        new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    drawer.setLayoutParams(drawerLayoutParams);
    addView(drawer);
  }

  private void addActionButtonsToList() {
    actionButtons = new ArrayList<ActionButton>();
    ActionButton save =
        new ActionButton() {
          @Override
          public void onClick() {
            FileIOUtils.writeFileFromString(file, codeEditor.getText().toString());
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
          }
        };
    save.setIcon(R.drawable.content_save_outline);
    save.setText("Save");
    actionButtons.add(save);
  }

  private void addActionButtonsToNavigationView() {
    actionButtons.forEach(
        actionButton -> {
          ViewGroup.LayoutParams lp =
              new ViewGroup.LayoutParams(
                  ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
          ActionButtonView actionButtonView = new ActionButtonView(getContext(), actionButton);
          actionButtonView.setLayoutParams(lp);
          mNavigationView.addView(actionButtonView);
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

  public File getEditorFile() {
    return this.file;
  }
}
