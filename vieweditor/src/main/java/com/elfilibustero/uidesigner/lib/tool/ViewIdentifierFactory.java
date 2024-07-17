package com.elfilibustero.uidesigner.lib.tool;

import android.content.Context;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class ViewIdentifierFactory {

    private static ViewIdentifierFactory instance;
    private Context context;
    private Map<String, Integer> idsMap;

    private ViewIdentifierFactory(Context context) {
        this.context = context;
        this.idsMap = new HashMap<>();
    }

    public static void init(Context context) {
        instance = new ViewIdentifierFactory(context);
    }

    public static ViewIdentifierFactory getInstance() {
        return instance;
    }

    public void clear() {
        idsMap.clear();
    }

    public Map<String, Integer> getIds() {
        return idsMap;
    }

    public void update(View view, String id, int viewId) {
        if (id == null) return;
        String name = parseReferName(id);
        idsMap.put(name, viewId);
        view.setId(viewId);
    }

    public void register(View view, String id) {
        if (id == null) return;
        String name = parseReferName(id);
        if (!idsMap.containsKey(name)) {
            view.setId(View.generateViewId());
            idsMap.put(name, view.getId());
        }
    }

    public void remove(String id) {
        if (id == null) return;
        String name = parseReferName(id);
        idsMap.remove(name);
    }

    public int getId(String id) {
        String name = parseReferName(id);
        return idsMap.getOrDefault(name, View.NO_ID);
    }

    private String parseReferName(String id) {
        return ResourceFactory.parseReferName(id);
    }

    public View findViewById(View root, String id) {
        return root.findViewById(getId(id));
    }

    public String getIdFromView(View view) {
        for (Map.Entry<String, Integer> entry : idsMap.entrySet()) {
            String id = entry.getKey();
            int viewId = entry.getValue().intValue();

            if (viewId == view.getId()) {
                return id;
            }
        }
        return null;
    }
}
