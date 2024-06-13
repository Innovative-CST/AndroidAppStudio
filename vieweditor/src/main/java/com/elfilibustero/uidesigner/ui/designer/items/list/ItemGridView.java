package com.elfilibustero.uidesigner.ui.designer.items.list;

import android.content.Context;
import android.widget.GridView;

import com.elfilibustero.uidesigner.adapters.SimpleListAdapter;
import com.elfilibustero.uidesigner.lib.utils.Utils;
import com.elfilibustero.uidesigner.ui.designer.DesignerItem;
import com.elfilibustero.uidesigner.ui.designer.DesignerListItem;

public class ItemGridView extends GridView implements DesignerItem, DesignerListItem {

    private SimpleListAdapter adapter;

    public ItemGridView(Context context) {
        super(context);
        setNumColumns(2);
        setColumnWidth(-1);
        setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        setListItem(android.R.layout.simple_list_item_1);
    }

    @Override
    public void setListItem(int layout) {
        adapter = new SimpleListAdapter(layout);
        setAdapter(adapter);
        setItemCount(6);
    }

    @Override
    public void setItemCount(int count) {
        adapter.setData(Utils.generateItems("Grid item", count));
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
            return GridView.class;
        }
    }
}
