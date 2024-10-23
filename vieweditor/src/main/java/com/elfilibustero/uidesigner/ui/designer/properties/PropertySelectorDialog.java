package com.elfilibustero.uidesigner.ui.designer.properties;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.elfilibustero.uidesigner.adapters.ChoiceAdapter;
import com.elfilibustero.uidesigner.adapters.MultiChoiceAdapter;
import com.elfilibustero.uidesigner.adapters.SingleChoiceAdapter;
import com.elfilibustero.uidesigner.lib.utils.Constants;
import com.elfilibustero.uidesigner.lib.utils.ConstantsProperties;
import com.icst.android.appstudio.vieweditor.databinding.PropertySelectorItemBinding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertySelectorDialog extends PropertyDialog {

	private PropertySelectorItemBinding binding;
	private int type;
	private RecyclerView selector;

	private ChoiceAdapter<?> choiceAdapter;

	public PropertySelectorDialog(Context context, int type) {
		super(context);
		this.type = type;
	}

	@Override
	public View getView() {
		binding = PropertySelectorItemBinding.inflate(LayoutInflater.from(getContext()));
		selector = binding.selector;
		selector.setLayoutManager(new LinearLayoutManager(getContext()));
		return binding.getRoot();
	}

	private List<String> getList() {
		var map = ((type == Constants.SELECTOR_TYPE_FLAG)
				? ConstantsProperties.MAP_FLAG
				: ConstantsProperties.MAP_ENUM)
				.get(getName());
		assert map != null;
		return new ArrayList<>(map.keySet());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onDialogShow() {
		var list = getList();
		choiceAdapter = (type == Constants.SELECTOR_TYPE_FLAG)
				? new MultiChoiceAdapter<>()
				: new SingleChoiceAdapter<>();
		choiceAdapter.setItemList(getList());
		var value = getValue();
		if (choiceAdapter instanceof SingleChoiceAdapter singleChoice) {
			if (!value.isEmpty())
				singleChoice.setSelectedItem(value);
			singleChoice.setOnItemSelectedListener(selectedItem -> checkErrors());
		} else if (choiceAdapter instanceof MultiChoiceAdapter multiChoice) {
			if (!value.isEmpty())
				multiChoice.setSelectedItem(getFlagValueToList(value));
			multiChoice.setOnItemSelectedListener(selectedItem -> checkErrors());
		}
		selector.setAdapter((RecyclerView.Adapter) choiceAdapter);
		checkErrors();
	}

	private List<String> getFlagValueToList(String value) {
		return Arrays.asList(value.split("\\|"));
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String save() {
		if (choiceAdapter instanceof SingleChoiceAdapter singleChoice) {
			return (String) singleChoice.getSelectedItem();
		} else if (choiceAdapter instanceof MultiChoiceAdapter multiChoice) {
			var selectedList = (List<String>) multiChoice.getSelectedItem();
			if (selectedList.size() == 1) {
				return selectedList.get(0);
			} else {
				StringBuilder result = new StringBuilder();
				for (String selected : selectedList) {
					if (result.length() > 0) {
						result.append("|");
					}
					result.append(selected);
				}
				return result.toString();
			}
		}
		return null;
	}

	private void checkErrors() {
		if (choiceAdapter instanceof SingleChoiceAdapter singleChoice) {
			var item = (String) singleChoice.getSelectedItem();
			if (item != null && !item.isEmpty()) {
				setEnabled(AlertDialog.BUTTON_POSITIVE, true);
				return;
			}
			setEnabled(AlertDialog.BUTTON_POSITIVE, false);
		} else if (choiceAdapter instanceof MultiChoiceAdapter multiChoice) {
			var item = (List<?>) multiChoice.getSelectedItem();
			if (item != null && !item.isEmpty()) {
				setEnabled(AlertDialog.BUTTON_POSITIVE, true);
				return;
			}
			setEnabled(AlertDialog.BUTTON_POSITIVE, false);
		}
	}
}
