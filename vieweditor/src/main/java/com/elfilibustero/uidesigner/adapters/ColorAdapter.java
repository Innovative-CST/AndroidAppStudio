package com.elfilibustero.uidesigner.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.elfilibustero.uidesigner.adapters.interfaces.ItemSelectedListener;
import com.elfilibustero.uidesigner.lib.tool.ResourceFactory;
import com.icst.layout.editor.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ColorAdapter<T> extends RecyclerView.Adapter<ColorAdapter<T>.ViewHolder>
		implements ChoiceAdapter<T> {

	private List<T> mData;
	private T mSelectedItem = null;
	private boolean mIsSelecting = false;
	private int lastSelectedItemPosition = -1;

	private ItemSelectedListener<T> listener;

	public ColorAdapter() {
		mData = new ArrayList<>();
	}

	@Override
	public void setItemList(List<?> newList) {
		DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
				new DiffUtil.Callback() {
					@Override
					public int getOldListSize() {
						return mData.size();
					}

					@Override
					public int getNewListSize() {
						return newList.size();
					}

					@Override
					public boolean areItemsTheSame(int old, int newPos) {
						return Objects.equals(mData.get(old), newList.get(newPos));
					}

					@Override
					public boolean areContentsTheSame(int old, int newPos) {
						return Objects.equals(mData.get(old), newList.get(newPos));
					}
				});
		mData.clear();
		mData.addAll((List<T>) newList);
		diffResult.dispatchUpdatesTo(this);
	}

	public void setOnItemSelectedListener(ItemSelectedListener<T> listener) {
		this.listener = listener;
	}

	@NonNull @Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.resource_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.bind((String) mData.get(position));
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	public List<T> getData() {
		return mData;
	}

	@Override
	public T getSelectedItem() {
		return mSelectedItem;
	}

	@Override
	public void setSelectedItem(T item) {
		mSelectedItem = item;
		notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	public class ViewHolder extends RecyclerView.ViewHolder {
		ImageView icon;
		RadioButton name;
		TextView value;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			icon = itemView.findViewById(R.id.icon);
			name = itemView.findViewById(R.id.name);
			value = itemView.findViewById(R.id.value);
		}

		public void bind(String item) {
			name.setText(item);
			name.setChecked(item.equals(mSelectedItem));
			name.setClickable(false);
			var drawable = ResourceFactory.getInstance().getDrawable(item);
			if (drawable != null)
				icon.setBackground(drawable);
			itemView.setOnClickListener(
					v -> {
						if (!mIsSelecting) {
							mIsSelecting = true;
							mSelectedItem = (T) item;
							v.postDelayed(
									() -> {
										notifyDataSetChanged();
										mIsSelecting = false;
										if (listener != null)
											listener.onItemSelectedListener((T) item);
									},
									200);
						}
					});
		}
	}
}
