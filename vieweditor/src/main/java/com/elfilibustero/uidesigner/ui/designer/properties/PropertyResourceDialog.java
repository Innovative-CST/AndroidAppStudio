package com.elfilibustero.uidesigner.ui.designer.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elfilibustero.uidesigner.adapters.ResourceAdapter;
import com.elfilibustero.uidesigner.enums.ResourceType;
import com.elfilibustero.uidesigner.lib.tool.ResourceFactory;
import com.elfilibustero.uidesigner.lib.utils.PropertiesUtil;
import com.elfilibustero.uidesigner.lib.utils.SimpleTextWatcher;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.icst.android.appstudio.vieweditor.databinding.PropertyResourceItemBinding;

import android.content.Context;
import android.text.Editable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PropertyResourceDialog extends PropertyDialog {

	private PropertyResourceItemBinding binding;
	private RecyclerView selector;
	private View container;
	private ChipGroup chips;
	private TextInputLayout inputLayout;
	private TextInputEditText inputText;
	private List<String> list = new ArrayList<>();

	private ResourceAdapter<Object> adapter;

	private Map<String, Integer> chipsId = new HashMap<>();

	public PropertyResourceDialog(Context context) {
		super(context);
	}

	@Override
	public View getView() {
		binding = PropertyResourceItemBinding.inflate(LayoutInflater.from(getContext()));
		selector = binding.selector;
		inputLayout = binding.textInputLayout;
		inputText = binding.inputText;
		container = binding.container;
		chips = binding.chips;
		inputLayout.setHint("Enter value");
		selector.setLayoutManager(new LinearLayoutManager(getContext()));
		return binding.getRoot();
	}

	private Chip createChip(String name) {
		var chip = new Chip(getContext());
		chip.setText(name);
		int id = View.generateViewId();
		chip.setId(id);
		chipsId.put(name, id);
		chip.setCheckable(true);
		return chip;
	}

	@Override
	public void onDialogShow() {
		adapter = new ResourceAdapter<>();
		selector.setAdapter(adapter);
		selector.setVisibility(View.VISIBLE);
		populateChips();
		setupListeners();
		setSelectedItem(getValue());
	}

	private void populateChips() {
		chips.setSingleSelection(true);
		chips.setSelectionRequired(true);
		for (String arg : getArguments()) {
			chips.addView(createChip(arg));
		}
	}

	@Override
	public String save() {
		String text = inputText.getText().toString();
		if (!text.isEmpty()) {
			Chip chip = chips.findViewById(chips.getCheckedChipId());
			if (chip != null) {
				String chipText = chip.getText().toString();
				if (!chipText.equals("input")) {
					return chipText + text;
				} else {
					return text;
				}
			}
		}
		return null;
	}

	private void setSelectedItem(String value) {
		var reference = PropertiesUtil.getUnitOrPrefix(value);
		if (reference != null) {
			inputText.requestFocus();
			if (chipsId.containsKey(reference.first)) {
				Chip chip = chips.findViewById(chipsId.get(reference.first));
				chip.setChecked(true);
				inputText.setText(reference.second);
				adapter.setSelectedItem(reference.second);
				int position = adapter.getData().indexOf(adapter.getLastSelectedItemPosition());
				if (position != -1) {
					selector.smoothScrollToPosition(position);
				}
			} else {
				((Chip) chips.getChildAt(0)).setChecked(true);
				inputText.setText(value);
			}
			inputText.setSelection(inputText.getText().length());
		} else {
			inputText.requestFocus();
			inputText.setText(value);
			((Chip) chips.getChildAt(0)).setChecked(true);
		}
	}

	private void clearPrefix() {
		inputLayout.setPrefixText("");
		inputText.setText("");
	}

	private void setupListeners() {
		adapter.setOnItemSelectedListener(
				result -> {
					Pair<String, String> selectedItem = (Pair<String, String>) result;
					if (chipsId.containsKey(selectedItem.second)) {
						Chip chip = chips.findViewById(chipsId.get(selectedItem.second));
						chip.setChecked(true);
						inputText.setText(selectedItem.first);
					} else {
						((Chip) chips.getChildAt(0)).setChecked(true);
						inputText.setText(selectedItem.first);
					}
					inputText.setSelection(inputText.getText().length());
				});

		inputText.addTextChangedListener(
				new SimpleTextWatcher() {
					@Override
					public void afterTextChanged(Editable p1) {
						checkErrors();
					}
				});

		chips.setOnCheckedStateChangeListener(
				(group, checkedIds) -> {
					clearPrefix();
					Chip checkedChip = group.findViewById(checkedIds.get(0));
					// Check if the chip is checked
					if (checkedChip != null && checkedChip.isChecked()) {
						String chipText = checkedChip.getText().toString();
						if (!chipText.equals("input") && !chipText.equals("#")) {
							switch (ResourceType.fromPrefix(chipText)) {
								case ANDROID_DRAWABLE, DRAWABLE -> adapter.setItemList(
										populateAndroidDrawableList(),
										ResourceType.fromPrefix(chipText));

								case ANDROID_COLOR, COLOR -> adapter.setItemList(
										new ArrayList<>(), null);
							}
							inputLayout.setPrefixText(chipText);
						} else if (!chipText.equals("input")) {
							inputLayout.setPrefixText(chipText);
							adapter.setItemList(new ArrayList<>(), null);
						} else {
							adapter.setItemList(new ArrayList<>(), null);
						}
					}
				});
	}

	@Override
	public boolean isValid() {
		return true;
	}

	private List<Pair<String, Object>> populateAndroidDrawableList() {
		List<Pair<String, Object>> list = new ArrayList<>();
		for (Map.Entry<String, Object> entry : ResourceFactory.getInstance().getDrawables().entrySet()) {
			list.add(Pair.create(entry.getKey(), entry.getValue()));
		}
		return list;
	}

	private void checkErrors() {
		String text = inputText.getText().toString();

		if (text.isEmpty()) {
			inputLayout.setErrorEnabled(true);
			inputLayout.setError("Field cannot be empty!");
			setEnabled(AlertDialog.BUTTON_POSITIVE, false);
			return;
		}

		var ch = chips.getCheckedChipId();
		Chip chip = chips.findViewById(ch);

		if (chip != null && chip.isChecked()) {
			var chipText = chip.getText().toString();
			if (chipText.equals("#")) {
				if (!ResourceFactory.isHexColor(chipText + text)) {
					inputLayout.setErrorEnabled(true);
					inputLayout.setError("Invalid hex color format");
					setEnabled(AlertDialog.BUTTON_POSITIVE, false);
					return;
				}
			} else {
				switch (ResourceType.fromPrefix(chipText)) {
					case ANDROID_DRAWABLE, DRAWABLE -> {
						if (!Character.isLetter(text.charAt(0))) {
							inputLayout.setErrorEnabled(true);
							inputLayout.setError(
									"The name must begin with a letter or special character(_)");
							setEnabled(AlertDialog.BUTTON_POSITIVE, false);
							return;
						}
						if (!text.matches("^[a-z_][a-z0-9_]*$")) {
							inputLayout.setErrorEnabled(true);
							inputLayout.setError(
									"Only use letters(a-z), numbers and special character(_)");
							setEnabled(AlertDialog.BUTTON_POSITIVE, false);
							return;
						}
					}

					case ANDROID_COLOR, COLOR -> {
						if (!text.matches("^[a-z0-9_]+$")) {
							inputLayout.setErrorEnabled(true);
							inputLayout.setError(
									"Only use lowercase letters(a-z), numbers and special character(_)");
							setEnabled(AlertDialog.BUTTON_POSITIVE, false);
							return;
						}
					}
				}
			}
		}

		// No errors detected
		inputLayout.setErrorEnabled(false);
		inputLayout.setError("");
		setEnabled(AlertDialog.BUTTON_POSITIVE, true);
	}

	private List<String> getArguments() {
		List<String> arguments = new ArrayList<>();
		arguments.add("input");
		switch (getName()) {
			case "src", "checkedIcon", "srcCompat" -> {
				arguments.add("@android:drawable/");
				arguments.add("@drawable/");
			}
			case "background", "foreground" -> {
				arguments.add("@android:drawable/");
				arguments.add("@drawable/");
				arguments.add("@android:color/");
				arguments.add("@color/");
				arguments.add("#");
				break;
			}

			case "backgroundTint",
					"tint",
					"textColor",
					"textHintColor",
					"cardBackgroundColor",
					"cardForegroundColor",
					"strokeColor",
					"checkedIconTint" -> {
				arguments.add("@android:color/");
				arguments.add("@color/");
				arguments.add("#");
				break;
			}

			default -> {
			}
		}

		return arguments;
	}
}
