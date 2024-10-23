package com.icst.android.appstudio.utils.terminal;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.termux.view.TerminalView;

public final class KeyListener implements VirtualKeysView.IVirtualKeysView {

	private final TerminalView terminal;

	public KeyListener(TerminalView terminal) {
		this.terminal = terminal;
	}

	@Override
	public void onVirtualKeyButtonClick(View view, VirtualKeyButton buttonInfo, Button button) {
		if (terminal == null) {
			return;
		}
		if (buttonInfo.isMacro()) {
			String[] keys = buttonInfo.getKey().split(" ");
			boolean ctrlDown = false;
			boolean altDown = false;
			boolean shiftDown = false;
			boolean fnDown = false;
			for (String key : keys) {
				if (SpecialButton.CTRL.getKey().equals(key)) {
					ctrlDown = true;
				} else if (SpecialButton.ALT.getKey().equals(key)) {
					altDown = true;
				} else if (SpecialButton.SHIFT.getKey().equals(key)) {
					shiftDown = true;
				} else if (SpecialButton.FN.getKey().equals(key)) {
					fnDown = true;
				} else {
					onTerminalExtraKeyButtonClick(key, ctrlDown, altDown, shiftDown, fnDown);
					ctrlDown = false;
					altDown = false;
					shiftDown = false;
					fnDown = false;
				}
			}
		} else {
			onTerminalExtraKeyButtonClick(buttonInfo.getKey(), false, false, false, false);
		}
	}

	private void onTerminalExtraKeyButtonClick(
			String key, boolean ctrlDown, boolean altDown, boolean shiftDown, boolean fnDown) {
		if (VirtualKeysConstants.PRIMARY_KEY_CODES_FOR_STRINGS.containsKey(key)) {
			Integer keyCode = VirtualKeysConstants.PRIMARY_KEY_CODES_FOR_STRINGS.get(key);
			if (keyCode == null) {
				return;
			}
			int metaState = 0;
			if (ctrlDown) {
				metaState |= KeyEvent.META_CTRL_ON | KeyEvent.META_CTRL_LEFT_ON;
			}
			if (altDown) {
				metaState |= KeyEvent.META_ALT_ON | KeyEvent.META_ALT_LEFT_ON;
			}
			if (shiftDown) {
				metaState |= KeyEvent.META_SHIFT_ON | KeyEvent.META_SHIFT_LEFT_ON;
			}
			if (fnDown) {
				metaState |= KeyEvent.META_FUNCTION_ON;
			}

			KeyEvent keyEvent = new KeyEvent(0, 0, KeyEvent.ACTION_UP, keyCode, 0, metaState);
			terminal.onKeyDown(keyCode, keyEvent);
		} else {
			for (int off = 0; off < key.length();) {
				int codePoint = key.codePointAt(off);
				terminal.inputCodePoint(codePoint, ctrlDown, altDown);
				off += Character.charCount(codePoint);
			}
		}
	}

	@Override
	public boolean performVirtualKeyButtonHapticFeedback(
			View view, VirtualKeyButton buttonInfo, Button button) {
		return false;
	}
}
