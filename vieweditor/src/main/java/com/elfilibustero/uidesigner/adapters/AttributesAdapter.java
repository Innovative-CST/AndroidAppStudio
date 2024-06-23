package com.elfilibustero.uidesigner.adapters;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.TooltipCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.elfilibustero.uidesigner.lib.utils.PropertiesUtil;
import com.icst.android.appstudio.vieweditor.R;

import java.util.List;

public class AttributesAdapter extends RecyclerView.Adapter<AttributesAdapter.VH> {

    private List<Pair<String, String>> attrs;
    private ItemClickListener clickListener;

    public AttributesAdapter(List<Pair<String, String>> attrs) {
        this.attrs = attrs;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        clickListener = listener;
    }

    public class VH extends RecyclerView.ViewHolder {
        public TextView attributeName;
        public TextView attributeValue;
        private ImageView attributeIcon;

        public VH(View view) {
            super(view);
            attributeName = view.findViewById(R.id.attribute_name);
            attributeValue = view.findViewById(R.id.attribute_value);
            attributeIcon = view.findViewById(R.id.icon);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.show_attribute_item, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        var attribute = attrs.get(position);
        var attributeName = attribute.first;
        var names = attributeName.split(":");
        if (names.length > 1) {
            holder.attributeIcon.setImageResource(PropertiesUtil.getIconFor(names[1]));
            holder.attributeName.setText(names[1]);
            holder.attributeValue.setText(names[0]);
        } else {
            holder.attributeIcon.setImageResource(PropertiesUtil.getIconFor(names[0]));
            holder.attributeName.setText(names[0]);
            holder.attributeValue.setText("");
        }

        TooltipCompat.setTooltipText(holder.itemView, attributeName);
        holder.itemView.setOnClickListener(
                v -> {
                    if (clickListener != null) clickListener.onClick(attribute);
                });
        holder.itemView.setOnLongClickListener(
                v -> {
                    if (clickListener != null) clickListener.onLongClick(attribute, position);
                    return true;
                });
    }

    @Override
    public int getItemCount() {
        return attrs.size();
    }

    public void removeItem(int position) {
        attrs.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Pair<String, String> item, int position) {
        attrs.add(position, item);
        notifyItemInserted(position);
    }

    public List<Pair<String, String>> getAttributes() {
        return attrs;
    }

    public static interface ItemClickListener {

        void onClick(Pair<String, String> position);

        void onLongClick(Pair<String, String> attribute, int position);
    }
}
