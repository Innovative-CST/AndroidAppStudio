package com.elfilibustero.uidesigner.lib.base;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.icst.android.appstudio.vieweditor.R;

public abstract class BaseDialog extends MaterialAlertDialogBuilder {

    protected AlertDialog dialog;
    private String title;

    public BaseDialog(Context context) {
        super(context);
        dialog = create();
        dialog.setView(getView());
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (d, w) -> {});
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (d, w) -> {});
    }

    public abstract View getView();

    public abstract void onDialogShow();

    public void setEnabled(int button, boolean enabled) {
        if (dialog.getButton(button) != null) {
            dialog.getButton(button).setEnabled(enabled);
        }
    }

    public void setTitle(String title) {
        this.title = title;
        dialog.setTitle(title);
    }

    public String getTitle() {
        return title;
    }

    public void setMessage(String message) {
        dialog.setMessage(message);
    }

    @Override
    public AlertDialog show() {
        dialog.show();
        onDialogShow();
        return dialog;
    }

    public AlertDialog get() {
        return dialog;
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
