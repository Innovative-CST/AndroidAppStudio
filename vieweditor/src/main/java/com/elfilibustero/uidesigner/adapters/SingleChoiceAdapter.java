package com.elfilibustero.uidesigner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.elfilibustero.uidesigner.adapters.interfaces.ItemSelectedListener;
import com.icst.android.appstudio.vieweditor.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SingleChoiceAdapter<T> extends RecyclerView.Adapter<SingleChoiceAdapter<T>.ViewHolder>
        implements ChoiceAdapter<T> {

    private List<T> mData;
    private T mSelectedItem = null;
    private boolean mIsSelecting = false;

    private ItemSelectedListener<T> listener;

    public SingleChoiceAdapter() {
        this.mData = new ArrayList<>();
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

    public void setOnItemSelectedListener(ItemSelectedListener<T> listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single_choice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T item = mData.get(position);
        holder.radioButton.setText(String.valueOf(item));
        holder.radioButton.setChecked(item.equals(mSelectedItem));
        holder.radioButton.setOnClickListener(
                v -> {
                    if (!mIsSelecting) {
                        mIsSelecting = true;
                        mSelectedItem = item;
                        v.postDelayed(
                                () -> {
                                    notifyDataSetChanged();
                                    mIsSelecting = false;
                                    if (listener != null) {
                                        listener.onItemSelectedListener(mSelectedItem);
                                    }
                                },
                                200);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mData.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.single_choice_button);
        }
    }
}
