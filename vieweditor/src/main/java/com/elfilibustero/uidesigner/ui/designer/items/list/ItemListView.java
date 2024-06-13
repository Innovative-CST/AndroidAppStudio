package com.elfilibustero.uidesigner.ui.designer.items.list;

import android.content.Context;
import android.widget.ListView;

import com.elfilibustero.uidesigner.adapters.SimpleListAdapter;
import com.elfilibustero.uidesigner.lib.utils.Utils;
import com.elfilibustero.uidesigner.ui.designer.DesignerItem;
import com.elfilibustero.uidesigner.ui.designer.DesignerListItem;

public class ItemListView extends ListView implements DesignerItem, DesignerListItem {
    
    private SimpleListAdapter adapter;
    
    public ItemListView(Context context) {
        super(context);
        initialize(context);
    }

    private void initialize(Context context) {
        setListItem(android.R.layout.simple_list_item_1);
    }

    @Override
    public void setListItem(int layout) {
        adapter = new SimpleListAdapter(layout);
        setAdapter(adapter);
        setItemCount(3);
    }

    @Override
    public void setItemCount(int count) {
        adapter.setData(Utils.generateItems("List item", count));
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
            return ListView.class;
        }
    }
}
