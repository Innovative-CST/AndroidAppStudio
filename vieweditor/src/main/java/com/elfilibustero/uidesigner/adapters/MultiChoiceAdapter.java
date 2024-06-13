package com.elfilibustero.uidesigner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.elfilibustero.uidesigner.adapters.interfaces.ItemSelectedListener;
import com.tscodeeditor.android.appstudio.vieweditor.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MultiChoiceAdapter<T> extends RecyclerView.Adapter<MultiChoiceAdapter<T>.ViewHolder>
        implements ChoiceAdapter<List<T>> {

    private List<T> mData;
    private Set<T> mCheckedItems = new HashSet<>();

    private ItemSelectedListener<Set<T>> listener;

    public MultiChoiceAdapter() {
        mData = new ArrayList<>();
    }

    @Override
    public void setItemList(List<?> list) {
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(
                        new DiffUtil.Callback() {
                            @Override
                            public int getOldListSize() {
                                return mData.size();
                            }

                            @Override
                            public int getNewListSize() {
                                return list.size();
                            }

                            @Override
                            public boolean areItemsTheSame(int old, int newPos) {
                                return Objects.equals(mData.get(old), list.get(newPos));
                            }

                            @Override
                            public boolean areContentsTheSame(int old, int newPos) {
                                return Objects.equals(mData.get(old), list.get(newPos));
                            }
                        });
        mData.clear();
        mData.addAll((List<T>) list);
        diffResult.dispatchUpdatesTo(this);
    }

    public void setOnItemSelectedListener(ItemSelectedListener<Set<T>> listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_multi_choice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T item = mData.get(position);
        holder.checkBox.setText(String.valueOf(item));
        holder.checkBox.setChecked(mCheckedItems.contains(item));
        holder.checkBox.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        mCheckedItems.add(item);
                    } else {
                        mCheckedItems.remove(item);
                    }
                    if (listener != null) {
                        listener.onItemSelectedListener(mCheckedItems);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public List<T> getSelectedItem() {
        return new ArrayList<>(mCheckedItems);
    }

    @Override
    public void setSelectedItem(List<T> items) {
        if (items != null && !items.isEmpty()) {
            mCheckedItems.clear();
            mCheckedItems.addAll(items);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.multi_choice_checkbox);
        }
    }
}
