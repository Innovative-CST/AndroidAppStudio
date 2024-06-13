package com.elfilibustero.uidesigner.ui.designer.properties;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elfilibustero.uidesigner.adapters.SingleChoiceAdapter;
import com.elfilibustero.uidesigner.lib.utils.ConstantsProperties;
import com.elfilibustero.uidesigner.lib.utils.PropertiesUtil;
import com.elfilibustero.uidesigner.lib.utils.SimpleTextWatcher;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tscodeeditor.android.appstudio.vieweditor.databinding.PropertySizeItemBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertySizeDialog extends PropertyDialog {

    private PropertySizeItemBinding binding;
    private RecyclerView selector;
    private View dimenContainer;
    private ChipGroup chips;
    private TextInputLayout inputLayout;
    private TextInputEditText inputText;
    private List<String> list;

    private SingleChoiceAdapter<String> choiceAdapter;

    private Map<String, Integer> chipsId = new HashMap<>();

    private static final String DIMEN_VALUE = "dimension value";

    public PropertySizeDialog(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        binding = PropertySizeItemBinding.inflate(LayoutInflater.from(getContext()));
        selector = binding.selector;
        inputLayout = binding.textInputLayout;
        inputText = binding.inputText;
        dimenContainer = binding.dimenContainer;
        chips = binding.chips;
        inputLayout.setHint("Enter dimension value");
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
        if (isWidthOrHeight()) {
            list = new ArrayList<>(ConstantsProperties.MAP_ENUM.get(getName()).keySet());
            list.add(DIMEN_VALUE);
            selector.setVisibility(View.VISIBLE);
            dimenContainer.setVisibility(View.GONE);
            choiceAdapter = new SingleChoiceAdapter<>();
            choiceAdapter.setItemList(list);
            selector.setAdapter(choiceAdapter);
        } else {
            checkErrors();
            selector.setVisibility(View.GONE);
            dimenContainer.setVisibility(View.VISIBLE);
        }
        populateChips();
        setupListeners();
        setSelectedItem(getValue());
    }

    private boolean isWidthOrHeight() {
        return getName().equals("layout_width") || getName().equals("layout_height");
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
                if (chipText.equals("dp") || chipText.equals("sp")) {
                    return text + chipText;
                } else if (!chipText.equals("input")) {
                    return chipText + text;
                } else {
                    return text;
                }
            }
        }
        return choiceAdapter.getSelectedItem();
    }

    private void setSelectedItem(String value) {
        var reference = PropertiesUtil.getUnitOrPrefix(value);
        if (reference != null) {
            dimenContainer.setVisibility(View.VISIBLE);
            if (isWidthOrHeight()) choiceAdapter.setSelectedItem(DIMEN_VALUE);
            inputText.requestFocus();
            if (chipsId.containsKey(reference.first)) {
                Chip chip = chips.findViewById(chipsId.get(reference.first));
                chip.setChecked(true);
                inputText.setText(reference.second);
            } else {
                ((Chip) chips.getChildAt(0)).setChecked(true);
                inputText.setText(value);
            }
            inputText.setSelection(inputText.getText().length());
        } else {
            if (isWidthOrHeight()) {
                dimenContainer.setVisibility(View.GONE);
                choiceAdapter.setSelectedItem(value);
            } else {
                inputText.requestFocus();
                ((Chip) chips.getChildAt(0)).setChecked(true);
            }
        }
    }

    private void clearPrefixAndSuffix() {
        inputLayout.setPrefixText("");
        inputLayout.setSuffixText("");
        inputText.setText("");
    }

    private void setupListeners() {
        if (isWidthOrHeight()) {
            choiceAdapter.setOnItemSelectedListener(
                    selectedItem -> {
                        if (selectedItem.equalsIgnoreCase(DIMEN_VALUE)) {
                            dimenContainer.setVisibility(View.VISIBLE);
                            inputText.requestFocus();
                            ((Chip) chips.getChildAt(0)).setChecked(true);
                            checkErrors();
                        } else {
                            inputText.setText("");
                            inputLayout.setErrorEnabled(false);
                            inputLayout.setError("");
                            setEnabled(AlertDialog.BUTTON_POSITIVE, true);
                            dimenContainer.setVisibility(View.GONE);
                        }
                    });
        }

        inputText.addTextChangedListener(
                new SimpleTextWatcher() {
                    @Override
                    public void afterTextChanged(Editable p1) {
                        checkErrors();
                    }
                });

        chips.setOnCheckedStateChangeListener(
                (group, checkedIds) -> {
                    clearPrefixAndSuffix();
                    Chip checkedChip = group.findViewById(checkedIds.get(0));
                    // Check if the chip is checked
                    if (checkedChip != null && checkedChip.isChecked()) {
                        String chipText = checkedChip.getText().toString();
                        if (chipText.equals("dp") || chipText.equals("sp")) {
                            inputLayout.setSuffixText(chipText);
                            inputText.setInputType(
                                    InputType.TYPE_CLASS_NUMBER
                                            | InputType.TYPE_NUMBER_FLAG_SIGNED
                                            | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            return;
                        } else if (!chipText.equals("input")) {
                            inputLayout.setPrefixText(chipText);
                        }
                        inputText.setInputType(
                                InputType.TYPE_CLASS_TEXT
                                        | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    }
                });
    }

    @Override
    public boolean isValid() {
        return true;
    }

    private void checkErrors() {
        String text = inputText.getText().toString();

        if (text.equals("")) {
            inputLayout.setErrorEnabled(true);
            inputLayout.setError("Field cannot be empty!");
            setEnabled(AlertDialog.BUTTON_POSITIVE, false);
            return;
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
            case "layout_height",
                    "layout_width",
                    "minHeight",
                    "minWidth",
                    "layout_margin",
                    "padding",
                    "translationX",
                    "translationY",
                    "translationZ",
                    "paddingLeft",
                    "paddingStart",
                    "paddingRight",
                    "paddingEnd",
                    "paddingTop",
                    "paddingBottom",
                    "layout_marginBottom",
                    "layout_marginLeft",
                    "layout_marginStart",
                    "layout_marginEnd",
                    "layout_marginTop",
                    "layout_marginRight",
                    "elevation",
                    "strokeWidth",
                    "cardElevation",
                    "cardCornerRadius" -> {
                arguments.add("dp");
                arguments.add("@dimen/");
                arguments.add("?attr/");
                break;
            }
            case "textSize" -> {
                arguments.add("sp");
                arguments.add("@dimen/");
                break;
            }

            default -> {}
        }

        return arguments;
    }
}
