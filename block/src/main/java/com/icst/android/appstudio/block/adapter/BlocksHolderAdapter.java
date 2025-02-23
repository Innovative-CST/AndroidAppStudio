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

import com.icst.android.appstudio.block.databinding.AdapterEventEditorBlockHolderBinding;
import com.icst.android.appstudio.block.editor.EventEditor;
import com.icst.android.appstudio.block.model.BlockHolderModel;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BlocksHolderAdapter extends RecyclerView.Adapter<BlocksHolderAdapter.ViewHolder> {

	public ArrayList<BlockHolderModel> list;
	public EventEditor editor;

	public BlocksHolderAdapter(ArrayList<BlockHolderModel> _arr, EventEditor editor) {
		list = _arr;
		this.editor = editor;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		AdapterEventEditorBlockHolderBinding item = AdapterEventEditorBlockHolderBinding
				.inflate(LayoutInflater.from(parent.getContext()));
		View _v = item.getRoot();
		RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		_v.setLayoutParams(_lp);
		return new ViewHolder(_v);
	}

	@Override
	public void onBindViewHolder(ViewHolder _holder, final int position) {
		AdapterEventEditorBlockHolderBinding binding = AdapterEventEditorBlockHolderBinding.bind(_holder.itemView);
		binding.holderName.setText(list.get(position).getName());
		binding.color.setBackgroundColor(Color.parseColor(list.get(position).getColor()));
		binding
				.getRoot()
				.setOnClickListener(
						(view) -> {
							editor.binding.blockList.setAdapter(
									new BlockAdapter(list.get(position).getList(), editor));
							editor.binding.blockList.setLayoutManager(
									new LinearLayoutManager(editor.getContext()));
						});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
		}
	}
}
