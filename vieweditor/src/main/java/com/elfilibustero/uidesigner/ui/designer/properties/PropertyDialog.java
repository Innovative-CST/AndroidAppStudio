package com.elfilibustero.uidesigner.ui.designer.properties;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.elfilibustero.uidesigner.lib.base.BaseDialog;
import com.elfilibustero.uidesigner.lib.tool.ResourceFactory;

public abstract class PropertyDialog extends BaseDialog {

    private SaveCallback callback;
    private String name;
    private String value;

    public PropertyDialog(Context context) {
        super(context);
        dialog.setButton(
                AlertDialog.BUTTON_POSITIVE,
                "Save",
                (d, w) -> {
                    if (isValid()) {
                        if (callback != null) callback.onSave(save());
                    }
                });
    }

    public void setName(String name) {
        this.name = ResourceFactory.parseReferName(name, ":");
        setTitle(this.name);
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public abstract String save();

    public abstract boolean isValid();

    public PropertyDialog addSaveCallback(SaveCallback callback) {
        this.callback = callback;
        return this;
    }

    public interface SaveCallback {
        void onSave(String value);
    }
}
