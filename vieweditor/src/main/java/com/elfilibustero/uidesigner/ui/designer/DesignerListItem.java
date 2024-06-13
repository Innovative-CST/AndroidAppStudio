package com.elfilibustero.uidesigner.ui.designer;

import androidx.annotation.LayoutRes;

public interface DesignerListItem {

    void setListItem(@LayoutRes int layout);
    
    void setItemCount(int count);
}
