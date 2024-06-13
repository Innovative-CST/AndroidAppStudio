/*
 * This file is part of Android AppStudio [https://github.com/TS-Code-Editor/AndroidAppStudio].
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

package com.tscodeeditor.android.appstudio.block.view;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.tscodeeditor.android.appstudio.block.R;
import com.tscodeeditor.android.appstudio.block.editor.EventEditor;
import com.tscodeeditor.android.appstudio.block.model.BlockModel;

public class BlockPreview extends LinearLayout {
  private BlockModel block;
  private EventEditor editor;

  public BlockPreview(EventEditor editor) {
    super(editor.getContext());
    this.editor = editor;
  }

  public void setBlock(BlockModel block) {
    this.block = block;
    drawPreview();
  }

  public void drawPreview() {
    String previewColor = editor.isDarkMode() ? "#FFFFFF" : "#000000";
    removeAllViews();
    setOrientation(LinearLayout.VERTICAL);
    if (block.getBlockType() == BlockModel.Type.defaultBlock) {
      LinearLayout.LayoutParams layoutParams =
          new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

      LinearLayout top = new LinearLayout(getContext());
      BlockView.setDrawable(top, R.drawable.block_default_top, Color.parseColor(previewColor));
      addView(top);
      LinearLayout body = new LinearLayout(getContext());
      BlockView.setDrawable(
          body, R.drawable.block_default_cut_bl_br, Color.parseColor(previewColor));
      addView(body);
      LinearLayout bottom = new LinearLayout(getContext());
      BlockView.setDrawable(
          bottom, R.drawable.block_default_bottom_joint, Color.parseColor(previewColor));
      addView(bottom);
      top.setLayoutParams(layoutParams);
      body.setLayoutParams(layoutParams);
      bottom.setLayoutParams(layoutParams);
    } else if (block.getBlockType() == BlockModel.Type.defaultBoolean) {
      LinearLayout.LayoutParams layoutParams =
          new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
      LinearLayout body = new LinearLayout(getContext());
      BlockView.setDrawable(
          body, R.drawable.block_boolean_backdrop, Color.parseColor(previewColor));
      addView(body);
      body.setLayoutParams(layoutParams);
    } else if (block.getBlockType() == BlockModel.Type.number) {
      LinearLayout.LayoutParams layoutParams =
          new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
      LinearLayout body = new LinearLayout(getContext());
      BlockView.setDrawable(body, R.drawable.block_number, Color.parseColor("#000000"));
      addView(body);
      body.setLayoutParams(layoutParams);
    }
  }

  public void removePreview() {
    if (getParent() == null) return;
    ((ViewGroup) getParent()).removeView(this);
  }
}
