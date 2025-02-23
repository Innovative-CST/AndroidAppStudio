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

package com.icst.android.appstudio.block.adapter;

import java.util.ArrayList;

import com.icst.android.appstudio.block.editor.EventEditor;
import com.icst.android.appstudio.block.model.BlockModel;
import com.icst.android.appstudio.block.utils.UnitUtils;
import com.icst.android.appstudio.block.view.BlockView;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

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

		RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		linearLayout.setLayoutParams(lp);
		return new ViewHolder(linearLayout);
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, final int pos) {
		LinearLayout parent = (LinearLayout) arg0.itemView;
		if (list.get(pos) instanceof BlockModel) {
			HorizontalScrollView hslayout = new HorizontalScrollView(parent.getContext()) {
				@Override
				public boolean onInterceptTouchEvent(MotionEvent arg0) {
					return !editor.isDragging && super.onInterceptTouchEvent(arg0);
				}
			};
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			hslayout.setLayoutParams(lp);
			hslayout.setPadding(
					UnitUtils.dpToPx(editor.getContext(), 8),
					UnitUtils.dpToPx(editor.getContext(), 8),
					UnitUtils.dpToPx(editor.getContext(), 8),
					UnitUtils.dpToPx(editor.getContext(), 8));
			BlockView block = new BlockView(
					editor,
					editor.getContext(),
					((BlockModel) list.get(pos)).clone(),
					editor.isDarkMode());
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
