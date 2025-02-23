/*
 *  This file is part of AndroidAppStudio.
 *
 *  AndroidAppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidAppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidAppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.icst.android.appstudio.view;

import java.io.File;
import java.util.Map;

import org.json.JSONException;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.icst.android.appstudio.R;
import com.icst.android.appstudio.activities.TerminalActivity;
import com.icst.android.appstudio.databinding.TerminalPaneViewBinding;
import com.icst.android.appstudio.interfaces.WorkSpacePane;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.terminal.KeyListener;
import com.icst.android.appstudio.utils.terminal.SpecialButton;
import com.icst.android.appstudio.utils.terminal.VirtualKeysConstants;
import com.icst.android.appstudio.utils.terminal.VirtualKeysInfo;
import com.termux.terminal.TerminalEmulator;
import com.termux.terminal.TerminalSession;
import com.termux.terminal.TerminalSessionClient;
import com.termux.view.TerminalViewClient;

import android.graphics.drawable.Drawable;
import android.system.ErrnoException;
import android.system.Os;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/*
 * A WorkSpacePane for Terminal.
 */

public abstract class TerminalPaneView extends LinearLayout
		implements TerminalViewClient, TerminalSessionClient, WorkSpacePane {
	private float terminalTextSize = 24f;
	private final float minTextSize = 10.0f;
	private final float maxTextSize = 30.0f;

	private KeyListener keyListener;
	private TerminalPaneViewBinding binding;
	private TerminalSession terminalSession;
	private AppCompatActivity activity;
	private File workingDir;
	private File executeFile;

	public TerminalPaneView(AppCompatActivity activity, File workingDir, File executeFile) {
		super(activity);
		this.activity = activity;
		this.workingDir = workingDir;
		this.executeFile = executeFile;
		init();
	}

	public void init() {
		binding = TerminalPaneViewBinding.inflate(activity.getLayoutInflater());
		binding.termux.setTextSize((int) terminalTextSize);
		keyListener = new KeyListener(binding.termux);
		try {
			binding.extraKeys.setVirtualKeysViewClient(keyListener);
			binding.extraKeys.reload(
					new VirtualKeysInfo(
							TerminalActivity.VIRTUAL_KEYS, "", VirtualKeysConstants.CONTROL_CHARS_ALIASES));
		} catch (JSONException e) {
		}

		activity
				.getWindow()
				.setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
								| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		setupTerminalView();

		addView(binding.getRoot());
	}

	public void grantFileExecutionPermissiom(File path) {
		try {
			Os.chmod(path.getAbsolutePath(), 0700);
		} catch (ErrnoException e) {
		}
	}

	public void setupTerminalView() {
		grantFileExecutionPermissiom(
				new File(EnvironmentUtils.PREFIX, "etc/termux/bootstrap/termux-bootstrap-second-stage.sh"));

		String[] env = null;
		String[] argsList = {};

		final Map<String, String> environment = EnvironmentUtils.getEnvironment();
		env = new String[environment.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : environment.entrySet()) {
			env[i] = entry.getKey() + "=" + entry.getValue();
			i++;
		}

		terminalSession = new TerminalSession(
				executeFile.getAbsolutePath(),
				workingDir.getAbsolutePath(),
				argsList,
				env,
				TerminalEmulator.DEFAULT_TERMINAL_TRANSCRIPT_ROWS,
				this);
		binding.termux.setTerminalViewClient(this);
		binding.termux.attachSession(terminalSession);
	}

	@Override
	public boolean onLongPress(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean readControlKey() {
		Boolean state = binding.extraKeys.readSpecialButton(SpecialButton.CTRL, true);
		return state != null && state;
	}

	@Override
	public boolean readAltKey() {
		Boolean state = binding.extraKeys.readSpecialButton(SpecialButton.ALT, true);
		return state != null && state;
	}

	@Override
	public boolean onCodePoint(int arg0, boolean arg1, TerminalSession arg2) {
		return false;
	}

	@Override
	public void onEmulatorSet() {
	}

	@Override
	public float onScale(float scale) {
		float currentTextSize = terminalTextSize;
		float newTextSize = currentTextSize * scale;

		newTextSize = Math.max(minTextSize, Math.min(newTextSize, maxTextSize));
		binding.termux.setTextSize((int) newTextSize);
		terminalTextSize = newTextSize;

		if (scale < 0.9f || scale > 1.1f) {
			return 1.0f;
		}
		return scale;
	}

	@Override
	public void onSingleTapUp(MotionEvent arg0) {
		showSoftInput();
	}

	public void showSoftInput() {
		KeyboardUtils.showSoftInput(binding.termux);
	}

	@Override
	public boolean shouldBackButtonBeMappedToEscape() {
		return false;
	}

	@Override
	public boolean shouldEnforceCharBasedInput() {
		return true;
	}

	@Override
	public boolean shouldUseCtrlSpaceWorkaround() {
		return false;
	}

	@Override
	public void copyModeChanged(boolean arg0) {
	}

	@Override
	public boolean onKeyDown(int arg0, KeyEvent arg1, TerminalSession arg2) {
		if (arg0 == KeyEvent.KEYCODE_ENTER && !arg2.isRunning()) {
			onRelease();
			return true;
		}
		return false;
	}

	@Override
	public boolean onKeyUp(int arg0, KeyEvent arg1) {
		return false;
	}

	@Override
	public void onTextChanged(TerminalSession arg0) {
		binding.termux.onScreenUpdated();
	}

	@Override
	public void onTitleChanged(TerminalSession arg0) {
	}

	@Override
	public void onSessionFinished(TerminalSession arg0) {
	}

	@Override
	public void onBell(TerminalSession arg0) {
	}

	@Override
	public void onColorsChanged(TerminalSession arg0) {
	}

	@Override
	public void onTerminalCursorStateChange(boolean arg0) {
	}

	@Override
	public Integer getTerminalCursorStyle() {
		return TerminalEmulator.TERMINAL_CURSOR_STYLE_UNDERLINE;
	}

	@Override
	public void logError(String arg0, String arg1) {
	}

	@Override
	public void logWarn(String arg0, String arg1) {
	}

	@Override
	public void logInfo(String arg0, String arg1) {
	}

	@Override
	public void logDebug(String arg0, String arg1) {
	}

	@Override
	public void logVerbose(String arg0, String arg1) {
	}

	@Override
	public void logStackTraceWithMessage(String arg0, String arg1, Exception arg2) {
	}

	@Override
	public void logStackTrace(String arg0, Exception arg1) {
	}

	@Override
	public void onClipboardText(TerminalSession arg0, String arg1) {
		ClipboardUtils.copyText(arg1);
	}

	@Override
	public Drawable getWorkSpacePaneIcon() {
		return ContextCompat.getDrawable(activity, R.drawable.language_shell);
	}

	@Override
	public String getWorkSpacePaneName() {
		return "Terminal";
	}

	@Override
	public Drawable getWorkSpaceStatus() {
		return null;
	}

	@Override
	public abstract void onRelease();

	@Override
	public void onReleaseRequest() {
		terminalSession.finishIfRunning();
		onRelease();
	}
}
