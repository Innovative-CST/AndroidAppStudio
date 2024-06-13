package com.elfilibustero.uidesigner.adapters;

import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.elfilibustero.uidesigner.ui.designer.LayoutDesigner;
import com.elfilibustero.uidesigner.beans.ViewBean;
import com.elfilibustero.uidesigner.lib.view.PaletteButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaletteListAdapter extends RecyclerView.Adapter<PaletteListAdapter.ViewHolder> {

    private final List<ViewBean> beans;
    private final LayoutDesigner editor;

    public PaletteListAdapter(LayoutDesigner editor) {
        beans = new ArrayList<>();
        this.editor = editor;
    }

    public void submitList(List<ViewBean> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return beans.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int old, int newPos) {
                return Objects.equals(beans.get(old), newList.get(newPos));
            }

            @Override
            public boolean areContentsTheSame(int old, int newPos) {
                return Objects.equals(beans.get(old), newList.get(newPos));
            }
        });
        beans.clear();
        beans.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout root = new FrameLayout(parent.getContext());
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(beans.get(position));
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final PaletteButton button;

        public ViewHolder(FrameLayout view) {
            super(view);
            button = new PaletteButton(view.getContext());
            view.addView(button);
        }

        public void bind(ViewBean bean) {
            button.setIcon(bean.getIcon());
            button.setName(bean.getName());
            button.setBean(bean);
            button.setOnTouchListener(editor);
        }
    }
}
