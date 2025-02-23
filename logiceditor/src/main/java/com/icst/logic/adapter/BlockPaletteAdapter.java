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

package com.icst.logic.adapter;

import java.util.ArrayList;

import com.icst.android.appstudio.beans.BlockPaletteBean;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;
import com.icst.logic.view.BlockPaletteView;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class BlockPaletteAdapter extends RecyclerView.Adapter<BlockPaletteAdapter.ViewHolder> {
	private ArrayList<BlockPaletteBean> palette;
	private RecyclerView blocksRecyclerView;
	private LogicEditorConfiguration config;
	private LogicEditorView logicEditor;

	public BlockPaletteAdapter(
			ArrayList<BlockPaletteBean> palette,
			RecyclerView blocksRecyclerView,
			LogicEditorConfiguration config,
			LogicEditorView logicEditor) {
		this.palette = palette;
		this.blocksRecyclerView = blocksRecyclerView;
		this.config = config;
		this.logicEditor = logicEditor;
	}

	private int selectedPosition = -1; // No item selected initially

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View view) {
			super(view);
		}
	}

	@Override
	public int getItemCount() {
		return palette.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, int position) {
		BlockPaletteView paletteView = (BlockPaletteView) arg0.itemView;
		paletteView.setPalette(palette.get(position));
		paletteView.setSelected(selectedPosition == position);
		paletteView.setOnClickListener(
				v -> {
					paletteView.setSelected(!paletteView.isSelected());
					notifyItemChanged(selectedPosition);
					selectedPosition = position;
					blocksRecyclerView.setAdapter(
							new BlockListAdapter(
									palette.get(position).getBlocks(), config, logicEditor));
					blocksRecyclerView.setLayoutManager(
							new LinearLayoutManager(arg0.itemView.getContext()));
				});
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = new BlockPaletteView(arg0.getContext());
		RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
				RecyclerView.LayoutParams.MATCH_PARENT,
				RecyclerView.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(layoutParams);
		return new ViewHolder(view);
	}
}
