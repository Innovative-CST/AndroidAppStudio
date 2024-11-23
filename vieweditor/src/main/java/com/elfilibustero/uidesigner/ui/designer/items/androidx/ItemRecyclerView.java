package com.elfilibustero.uidesigner.ui.designer.items.androidx;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.elfilibustero.uidesigner.lib.utils.Utils;
import com.elfilibustero.uidesigner.ui.designer.DesignerItem;
import com.elfilibustero.uidesigner.ui.designer.DesignerListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ItemRecyclerView extends RecyclerView implements DesignerItem, DesignerListItem {

	private SimpleAdapter adapter;

	public ItemRecyclerView(Context context) {
		super(context);
		initialize(context);
	}

	private void initialize(Context context) {
		setMinimumWidth((int) Utils.getDip(context, 32.0f));
		setMinimumHeight((int) Utils.getDip(context, 32.0f));
		setLayoutManager(new LinearLayoutManager(context));
		setListItem(android.R.layout.simple_list_item_1);
	}

	@Override
	public void setListItem(int layout) {
		adapter = new SimpleAdapter(layout);
		setAdapter(adapter);
		setItemCount(3);
	}

	@Override
	public void setItemCount(int count) {
		adapter.setItemList(Utils.generateItems("Recycler item", count));
	}

	private String className;

	@Override
	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public Class<?> getClassType() {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			return RecyclerView.class;
		}
	}

	private static class SimpleAdapter
			extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {
		private List<String> dataList;

		private int layout;

		public SimpleAdapter(@LayoutRes int layout) {
			dataList = new ArrayList<>();
			this.layout = layout;
		}

		public void setItemList(List<String> newList) {
			DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
					new DiffUtil.Callback() {
						@Override
						public int getOldListSize() {
							return dataList.size();
						}

						@Override
						public int getNewListSize() {
							return newList.size();
						}

						@Override
						public boolean areItemsTheSame(int old, int newPos) {
							return Objects.equals(dataList.get(old), newList.get(newPos));
						}

						@Override
						public boolean areContentsTheSame(int old, int newPos) {
							return Objects.equals(dataList.get(old), newList.get(newPos));
						}
					});
			dataList.clear();
			dataList.addAll(newList);
			diffResult.dispatchUpdatesTo(this);
		}

		@NonNull @Override
		public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
			return new SimpleViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
			holder.bind(dataList.get(position));
		}

		@Override
		public int getItemCount() {
			return dataList.size();
		}

		static class SimpleViewHolder extends RecyclerView.ViewHolder {
			private TextView textView;

			SimpleViewHolder(View itemView) {
				super(itemView);
				textView = itemView.findViewById(android.R.id.text1);
			}

			void bind(String itemText) {
				if (textView != null)
					textView.setText(itemText);
			}
		}
	}
}
