package com.elfilibustero.uidesigner.ui.designer.tree;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tscodeeditor.android.appstudio.vieweditor.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeAdapter extends RecyclerView.Adapter<TreeAdapter.ViewHolder> {
    private final List<TreeNode> nodes;
    private TreeItemClickListener listener;

    public TreeAdapter() {
        nodes = new ArrayList<>();
    }

    public void setOnTreeItemClickListener(TreeItemClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<TreeNode> treeNodes) {
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(
                        new DiffUtil.Callback() {
                            @Override
                            public int getOldListSize() {
                                return nodes.size();
                            }

                            @Override
                            public int getNewListSize() {
                                return treeNodes.size();
                            }

                            @Override
                            public boolean areItemsTheSame(int old, int newPos) {
                                return Objects.equals(nodes.get(old), treeNodes.get(newPos));
                            }

                            @Override
                            public boolean areContentsTheSame(int old, int newPos) {
                                return Objects.equals(nodes.get(old), treeNodes.get(newPos));
                            }
                        });
        nodes.clear();
        nodes.addAll(treeNodes);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tree_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TreeNode node = nodes.get(position);
        holder.bind(node);
    }

    @Override
    public int getItemCount() {
        return nodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView className;
        private RecyclerView childList;
        private ImageView arrow;
        private ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.class_name);
            childList = itemView.findViewById(R.id.child_list);
            arrow = itemView.findViewById(R.id.arrow);
            icon = itemView.findViewById(R.id.icon);
            
            setupGestureDetectors(itemView);
            arrow.setOnClickListener(
                    view -> {
                        expand();
                    });
        }

        public void bind(TreeNode node) {
            className.setText(node.getName());
            icon.setImageResource(node.getIcon());
            arrow.setRotation(0);
            childList.setVisibility(View.GONE);
            if (node.isExpanded()) {
                arrow.animate().rotationBy(90).start();
                if (!node.getChildren().isEmpty()) childList.setVisibility(View.VISIBLE);
            }

            if (node.isViewGroup()) {
                arrow.setVisibility(View.VISIBLE);
                if (!node.getChildren().isEmpty()) {
                    TreeAdapter adapter = new TreeAdapter();
                    adapter.setOnTreeItemClickListener(listener);
                    adapter.submitList(node.getChildren());
                    childList.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
                    childList.setAdapter(adapter);
                }
            } else {
                arrow.setVisibility(View.INVISIBLE);
            }
        }

        private void setupGestureDetectors(View itemView) {
            var itemViewGesture =
                    new GestureDetector(
                            itemView.getContext(),
                            new GestureDetector.SimpleOnGestureListener() {
                                @Override
                                public boolean onSingleTapUp(MotionEvent e) {
                                    expand();
                                    if (listener != null)
                                        listener.onItemExpandClick(nodes.get(getAdapterPosition()));
                                    return true;
                                }

                                @Override
                                public void onLongPress(MotionEvent e) {
                                    if (listener != null)
                                        listener.onItemLongClick(
                                                nodes.get(getAdapterPosition()), e);
                                }
                            });

            var classNameGesture =
                    new GestureDetector(
                            itemView.getContext(),
                            new GestureDetector.SimpleOnGestureListener() {
                                @Override
                                public boolean onSingleTapUp(MotionEvent e) {
                                    if (listener != null)
                                        listener.onItemClick(nodes.get(getAdapterPosition()));
                                    return true;
                                }

                                @Override
                                public void onLongPress(MotionEvent e) {
                                    if (listener != null)
                                        listener.onItemLongClick(
                                                nodes.get(getAdapterPosition()), e);
                                }
                            });

            itemView.setOnTouchListener(
                    (view, event) -> {
                        itemViewGesture.onTouchEvent(event);
                        return true;
                    });
            
            className.setOnTouchListener(
                    (view, event) -> {
                        classNameGesture.onTouchEvent(event);
                        return true;
                    });
        }

        private void expand() {
            int position = getAdapterPosition();
            TreeNode node = nodes.get(position);
            node.setExpanded(!node.isExpanded());
            if (!node.isExpanded()) {
                collapseAll(node);
            }
            notifyItemChanged(position);
        }

        private void collapseAll(TreeNode node) {
            if (node == null) {
                return;
            }

            node.setExpanded(false);
            for (TreeNode child : node.getChildren()) {
                collapseAll(child);
            }
        }
    }

    public interface TreeItemClickListener {
        void onItemClick(TreeNode node);

        void onItemLongClick(TreeNode node, MotionEvent event);

        void onItemExpandClick(TreeNode node);
    }
}
