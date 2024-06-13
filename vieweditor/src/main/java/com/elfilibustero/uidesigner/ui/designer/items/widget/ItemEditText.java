package com.elfilibustero.uidesigner.ui.designer.items.widget;

import android.content.Context;
import android.widget.EditText;

import com.elfilibustero.uidesigner.ui.designer.DesignerItem;

public class ItemEditText extends EditText implements DesignerItem {
	
	public ItemEditText(Context context) {
		super(context);
		initialize();
	}
	
	private void initialize() {
		setFocusable(false);
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
            return EditText.class;
        }
    }
}
