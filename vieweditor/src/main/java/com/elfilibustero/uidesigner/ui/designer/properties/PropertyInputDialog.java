package com.elfilibustero.uidesigner.ui.designer.properties;

import java.util.regex.Pattern;

import com.elfilibustero.uidesigner.lib.tool.ResourceFactory;
import com.elfilibustero.uidesigner.lib.tool.ViewIdentifierFactory;
import com.elfilibustero.uidesigner.lib.utils.Constants;
import com.elfilibustero.uidesigner.lib.utils.PropertiesUtil;
import com.elfilibustero.uidesigner.lib.utils.SimpleTextWatcher;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.icst.android.appstudio.vieweditor.databinding.PropertyInputItemBinding;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AlertDialog;

public class PropertyInputDialog extends PropertyDialog {

	private PropertyInputItemBinding binding;
	private TextInputLayout inputLayout;
	private TextInputEditText inputText;

	private Pattern patternId = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*");
	private String exception;
	private int min;
	private int max;
	private int type;

	public PropertyInputDialog(Context context, int type) {
		super(context);
		this.type = type;
	}

	@Override
	public View getView() {
		binding = PropertyInputItemBinding.inflate(LayoutInflater.from(getContext()));
		inputLayout = binding.textInputLayout;
		inputText = binding.inputText;
		inputLayout.setHint("Enter value");
		return binding.getRoot();
	}

	@Override
	public void onDialogShow() {
		setup();
		checkErrors();
		inputText.addTextChangedListener(
				new SimpleTextWatcher() {
					@Override
					public void afterTextChanged(Editable s) {
						checkErrors();
					}
				});
	}

	private void setup() {
		var value = getValue();
		switch (type) {
			case Constants.INPUT_TYPE_ID -> {
				inputText.setInputType(
						InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
				inputText.setImeOptions(EditorInfo.IME_ACTION_DONE);
				var reference = PropertiesUtil.getUnitOrPrefix(value);
				if (reference != null) {
					// first attempt to parse @+id/
					inputLayout.setPrefixText(reference.first);
					value = reference.second;
				} else {
					// second attempt to parse @+id/
					inputLayout.setPrefixText("@+id/");
					value = ResourceFactory.parseReferName(value);
				}
				exception = value;
				inputLayout.setHint("Enter new ID");
			}
			case Constants.INPUT_TYPE_NUMBER -> {
				switch (getName()) {
					case "rotation" -> setMinAndMax(0, 360);
					default -> setMinAndMax(0, 999);
				}

				inputText.setInputType(
						InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
				if (value.isEmpty())
					value = "0";
			}
			case Constants.INPUT_TYPE_FLOAT -> {
				switch (getName()) {
					case "alpha" -> setMinAndMax(0, 1);
					case "scaleX", "scaleY" -> setMinAndMax(0, 2);
					case "rotationX", "rotationY" -> setMinAndMax(0, 360);
				}

				inputText.setInputType(
						min < 0
								? InputType.TYPE_CLASS_NUMBER
										| InputType.TYPE_NUMBER_FLAG_SIGNED
										| InputType.TYPE_NUMBER_FLAG_DECIMAL
								: InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

				if (value.isEmpty())
					value = "1.0";
			}
			default -> inputText.setInputType(InputType.TYPE_CLASS_TEXT);
		}
		inputText.setText(value);
		inputText.setSelection(inputText.getText().length());
		inputText.requestFocus();
	}

	private void setMinAndMax(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String save() {
		var text = inputText.getText().toString();
		Object result = switch (type) {
			case Constants.INPUT_TYPE_ID -> "@+id/" + text;
			case Constants.INPUT_TYPE_NUMBER -> Integer.valueOf(text);
			case Constants.INPUT_TYPE_FLOAT -> Float.valueOf(text);
			default -> text;
		};
		return String.valueOf(result);
	}

	private void checkErrors() {
		String text = inputText.getText().toString();

		// Check if the TextInputEditText is empty
		if (text.isEmpty()) {
			inputLayout.setErrorEnabled(true);
			inputLayout.setError("Field cannot be empty!");
			setEnabled(AlertDialog.BUTTON_POSITIVE, false);
			return;
		}

		switch (type) {
			case Constants.INPUT_TYPE_ID -> {
				var ids = ViewIdentifierFactory.getInstance().getIds();
				if (exception != null && exception.length() > 0 && text.equals(exception)) {
					return;
				}

				if (ids.containsKey(text)) {
					inputLayout.setErrorEnabled(true);
					inputLayout.setError("Current ID is already used");
					setEnabled(AlertDialog.BUTTON_POSITIVE, false);
					return;
				}
				if (!Character.isLetter(text.charAt(0))) {
					inputLayout.setErrorEnabled(true);
					inputLayout.setError("The ID must start with a letter");
					setEnabled(AlertDialog.BUTTON_POSITIVE, false);
					return;
				}
				if (!patternId.matcher(text).matches()) {
					inputLayout.setErrorEnabled(true);
					inputLayout.setError("Only use letters(a-z), numbers and special character(_)");
					setEnabled(AlertDialog.BUTTON_POSITIVE, false);
					return;
				}
			}
			case Constants.INPUT_TYPE_NUMBER -> {
				try {
					int f = Integer.parseInt(text);
					if (f >= min && f <= max) {
						inputLayout.setErrorEnabled(false);
						setEnabled(AlertDialog.BUTTON_POSITIVE, true);
						return;
					}
					inputLayout.setErrorEnabled(true);
					inputLayout.setError(String.format("%d ~ %d", min, max));
					setEnabled(AlertDialog.BUTTON_POSITIVE, false);
					return;
				} catch (NumberFormatException e) {
					inputLayout.setErrorEnabled(true);
					inputLayout.setError(String.format("%d ~ %d", min, max));
					setEnabled(AlertDialog.BUTTON_POSITIVE, false);
					return;
				}
			}

			case Constants.INPUT_TYPE_FLOAT -> {
				try {
					float f = Float.parseFloat(text);
					if (f >= (float) min && f <= (float) max) {
						inputLayout.setErrorEnabled(false);
						setEnabled(AlertDialog.BUTTON_POSITIVE, true);
						return;
					}
					inputLayout.setErrorEnabled(true);
					inputLayout.setError(String.format("%d ~ %d", min, max));
					setEnabled(AlertDialog.BUTTON_POSITIVE, false);
					return;
				} catch (NumberFormatException e) {
					inputLayout.setErrorEnabled(true);
					inputLayout.setError(String.format("%d ~ %d", min, max));
					setEnabled(AlertDialog.BUTTON_POSITIVE, false);
					return;
				}
			}
		}

		// No errors detected
		inputLayout.setErrorEnabled(false);
		inputLayout.setError("");
		setEnabled(AlertDialog.BUTTON_POSITIVE, true);
	}
}
