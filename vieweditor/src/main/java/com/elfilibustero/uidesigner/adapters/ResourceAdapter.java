package com.elfilibustero.uidesigner.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.elfilibustero.uidesigner.adapters.interfaces.ItemSelectedListener;
import com.elfilibustero.uidesigner.enums.ResourceType;
import com.elfilibustero.uidesigner.lib.drawable.AlphaPatternDrawable;
import com.icst.android.appstudio.vieweditor.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourceAdapter<T> extends RecyclerView.Adapter<ResourceAdapter<T>.ViewHolder>
        implements ChoiceAdapter<T> {

    private List<T> mData;
    private ResourceType type = null;
    private T mSelectedItem = null;
    private boolean mIsSelecting = false;
    private int lastSelectedItemPosition = -1;

    private ItemSelectedListener<T> listener;

    public ResourceAdapter() {
        mData = new ArrayList<>();
    }

    public void setItemList(List<?> newList, ResourceType type) {
        this.type = type;
        setItemList(newList);
    }

    @Override
    public void setItemList(List<?> newList) {
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.resource_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public List<T> getData() {
        return mData;
    }

    public int getLastSelectedItemPosition() {
        for (int i = 0; i < mData.size(); i++) {
            var item = (Pair<String, Object>) mData.get(i);
            if (item.first.equals(mSelectedItem)) {
                return i;
            }
        }
        return -1;
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

        public void bind(Object item) {
            var resource = (Pair<String, Object>) item;
            name.setText(resource.first);
            name.setChecked(resource.first.equals(mSelectedItem));
            name.setClickable(false);
            switch (type) {
                case ANDROID_DRAWABLE, DRAWABLE -> {
                    icon.setBackground(AlphaPatternDrawable.create());
                    icon.setImageDrawable((Drawable) resource.second);
                }
                case ANDROID_COLOR, COLOR -> {
                    value.setText((String) resource.second);
                }
            }
            itemView.setOnClickListener(
                    v -> {
                        if (!mIsSelecting) {
                            mIsSelecting = true;
                            mSelectedItem = (T) resource.first;
                            v.postDelayed(
                                    () -> {
                                        notifyDataSetChanged();
                                        mIsSelecting = false;
                                        if (listener != null)
                                            listener.onItemSelectedListener(
                                                    (T)
                                                            Pair.create(
                                                                    resource.first,
                                                                    type.getPrefix()));
                                    },
                                    200);
                        }
                    });
            /*
            itemView.setOnClickListener(
                    v -> {
                        if (listener != null)
                            listener.onItemSelectedListener(
                                    (T) Pair.create(resource.first, type.getPrefix()));
                    });
            */
            // Pair<String, Drawable> pair = item;
        }
    }
}
