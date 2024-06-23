package com.elfilibustero.uidesigner.ui.designer.properties;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elfilibustero.uidesigner.adapters.SingleChoiceAdapter;
import com.elfilibustero.uidesigner.lib.utils.Constants;
import com.elfilibustero.uidesigner.lib.utils.PropertiesUtil;
import com.icst.android.appstudio.vieweditor.databinding.PropertySelectorItemBinding;

import java.util.ArrayList;
import java.util.List;

public class IdsSelectorDialog extends PropertyDialog {

    private PropertySelectorItemBinding binding;
    private int type;
    private RecyclerView selector;

    private List<String> ids = new ArrayList<>();

    private SingleChoiceAdapter<String> choiceAdapter;

    public IdsSelectorDialog(Context context, int type, List<String> ids) {
        super(context);
        this.type = type;
        this.ids = ids;
    }

    @Override
    public View getView() {
        binding = PropertySelectorItemBinding.inflate(LayoutInflater.from(getContext()));
        selector = binding.selector;
        selector.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onDialogShow() {
        if (type == Constants.SELECTOR_TYPE_CONSTRAINT) {
            ids.add("parent");
        }
        choiceAdapter = new SingleChoiceAdapter<>();
        choiceAdapter.setItemList(ids);
        var value = getValue();
        var reference = PropertiesUtil.getUnitOrPrefix(value);
        if (reference != null) {
            value = reference.second;
        }
        choiceAdapter.setOnItemSelectedListener(
                selectedItem -> {
                    checkErrors();
                });
        if (!value.isEmpty()) choiceAdapter.setSelectedItem(value);
        selector.setAdapter(choiceAdapter);
        checkErrors();
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String save() {
        var selectedItem = choiceAdapter.getSelectedItem();
        if (selectedItem.equals("parent")) {
            return selectedItem;
        }
        return "@id/" + selectedItem;
    }

    private void checkErrors() {
        var selectedItem = choiceAdapter.getSelectedItem();
        if (selectedItem == null || ids.isEmpty()) {
            setEnabled(AlertDialog.BUTTON_POSITIVE, false);
            return;
        }
        setEnabled(AlertDialog.BUTTON_POSITIVE, true);
    }
}
