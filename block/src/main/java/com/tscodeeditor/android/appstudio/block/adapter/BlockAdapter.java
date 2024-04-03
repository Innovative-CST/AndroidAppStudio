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

package com.tscodeeditor.android.appstudio.block.adapter;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.tscodeeditor.android.appstudio.block.editor.EventEditor;
import com.tscodeeditor.android.appstudio.block.model.BlockModel;
import com.tscodeeditor.android.appstudio.block.utils.UnitUtils;
import com.tscodeeditor.android.appstudio.block.view.BlockView;
import java.util.ArrayList;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.ViewHolder> {

  private ArrayList<Object> list;
  private EventEditor editor;

  public BlockAdapter(ArrayList<Object> list, EventEditor editor) {
    this.list = list;
    this.editor = editor;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
    LinearLayout linearLayout = new LinearLayout(arg0.getContext());

    RecyclerView.LayoutParams lp =
        new RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    linearLayout.setLayoutParams(lp);
    return new ViewHolder(linearLayout);
  }

  @Override
  public void onBindViewHolder(ViewHolder arg0, final int pos) {
    LinearLayout parent = (LinearLayout) arg0.itemView;
    if (list.get(pos) instanceof BlockModel) {
      HorizontalScrollView hslayout =
          new HorizontalScrollView(parent.getContext()) {
            @Override
            public boolean onInterceptTouchEvent(MotionEvent arg0) {
              return !editor.isDragging && super.onInterceptTouchEvent(arg0);
            }
          };
      LinearLayout.LayoutParams lp =
          new LinearLayout.LayoutParams(
              ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      hslayout.setLayoutParams(lp);
      hslayout.setPadding(
          UnitUtils.dpToPx(editor.getContext(), 8),
          UnitUtils.dpToPx(editor.getContext(), 8),
          UnitUtils.dpToPx(editor.getContext(), 8),
          UnitUtils.dpToPx(editor.getContext(), 8));
      BlockView block = new BlockView(editor, editor.getContext(), ((BlockModel) list.get(pos)).clone();
      block.setEnableDragDrop(true);
      block.setEnableEditing(false);
      hslayout.addView(block);
      parent.addView(hslayout);
    }
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(View view) {
      super(view);
    }
  }
}
