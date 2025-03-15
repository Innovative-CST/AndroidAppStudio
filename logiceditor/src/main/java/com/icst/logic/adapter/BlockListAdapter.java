/*
 *  This file is part of Block IDLE.
 *
 *  Block IDLE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Block IDLE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Block IDLE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.logic.adapter;

import java.util.ArrayList;

import com.icst.blockidle.beans.BlockBean;
import com.icst.blockidle.beans.ExpressionBlockBean;
import com.icst.blockidle.beans.RegularBlockBean;
import com.icst.blockidle.beans.TerminatorBlockBean;
import com.icst.logic.block.view.BlockBeanView;
import com.icst.logic.block.view.RegularBlockBeanView;
import com.icst.logic.block.view.TerminatorBlockBeanView;
import com.icst.logic.builder.ExpressionBlockViewFactory;
import com.icst.logic.config.LogicEditorConfiguration;
import com.icst.logic.editor.view.LogicEditorView;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.ViewHolder> {
	private ArrayList<BlockBean> blocks;
	private LogicEditorConfiguration config;
	private LogicEditorView logicEditor;

	public BlockListAdapter(
			ArrayList<BlockBean> blocks,
			LogicEditorConfiguration config,
			LogicEditorView logicEditor) {
		this.blocks = blocks;
		this.config = config;
		this.logicEditor = logicEditor;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View view) {
			super(view);
		}
	}

	@Override
	public int getItemCount() {
		return blocks.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, int position) {
		HorizontalScrollView hScrollView = (HorizontalScrollView) arg0.itemView;
		BlockBeanView blockBeanView = null;

		if (blocks.get(position) instanceof RegularBlockBean regularBlock) {
			blockBeanView = new RegularBlockBeanView(
					arg0.itemView.getContext(), regularBlock, config, logicEditor);
		} else if (blocks.get(position) instanceof TerminatorBlockBean terminatorBlock) {
			blockBeanView = new TerminatorBlockBeanView(
					arg0.itemView.getContext(), terminatorBlock, config, logicEditor);
		} else if (blocks.get(position) instanceof ExpressionBlockBean expressionBlockBean) {
			blockBeanView = ExpressionBlockViewFactory.generateView(expressionBlockBean, arg0.itemView.getContext(),
					config, logicEditor);
		}
		hScrollView.setPadding(8, 8, 0, 0);
		hScrollView.addView(blockBeanView);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = new HorizontalScrollView(arg0.getContext()) {
			@Override
			public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
				return !logicEditor.getDraggableTouchListener().isDragging()
						&& super.onInterceptTouchEvent(motionEvent);
			}
		};
		RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
				RecyclerView.LayoutParams.MATCH_PARENT,
				RecyclerView.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(layoutParams);
		return new ViewHolder(view);
	}
}
