package com.elfilibustero.uidesigner.adapters;

import java.util.List;

public interface ChoiceAdapter<T> {
    T getSelectedItem();

    void setSelectedItem(T item);

    void setItemList(List<?> list);
}
