/*
 * This file is part of Android AppStudio [https://github.com/Innovative-CST/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.icst.android.appstudio.activities;

import java.io.File;
import java.util.Map;

import org.json.JSONException;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.icst.android.appstudio.databinding.ActivityTerminalBinding;
import com.icst.android.appstudio.utils.EnvironmentUtils;
import com.icst.android.appstudio.utils.terminal.KeyListener;
import com.icst.android.appstudio.utils.terminal.SpecialButton;
import com.icst.android.appstudio.utils.terminal.VirtualKeysConstants;
import com.icst.android.appstudio.utils.terminal.VirtualKeysInfo;
import com.termux.terminal.TerminalEmulator;
import com.termux.terminal.TerminalSession;
import com.termux.terminal.TerminalSessionClient;
import com.termux.view.TerminalView;
import com.termux.view.TerminalViewClient;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.system.ErrnoException;
import android.system.Os;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class TerminalActivity extends BaseActivity
		implements TerminalSessionClient, TerminalViewClient {

	private ActivityTerminalBinding binding;
	public TerminalView terminal;
	protected KeyListener keyListener;
	public static final String VIRTUAL_KEYS = "["
			+ "\n  ["
			+ "\n    \"ESC\","
			+ "\n    {"
			+ "\n      \"key\": \"/\","
			+ "\n      \"popup\": \"\\\\\""
			+ "\n    },"
			+ "\n    {"
			+ "\n      \"key\": \"-\","
			+ "\n      \"popup\": \"|\""
			+ "\n    },"
			+ "\n    \"HOME\","
			+ "\n    \"UP\","
			+ "\n    \"END\","
			+ "\n    \"PGUP\""
			+ "\n  ],"
			+ "\n  ["
			+ "\n    \"TAB\","
			+ "\n    \"CTRL\","
			+ "\n    \"ALT\","
			+ "\n    \"LEFT\","
			+ "\n    \"DOWN\","
			+ "\n    \"RIGHT\","
			+ "\n    \"PGDN\""
			+ "\n  ]"
			+ "\n]";

	@Override
	protected void onCreate(Bundle bundle) {
		getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		super.onCreate(bundle);

		binding = ActivityTerminalBinding.inflate(getLayoutInflater());
		keyListener = new KeyListener(binding.termux);
		try {
			binding.extraKeys.setVirtualKeysViewClient(keyListener);
			binding.extraKeys.reload(
					new VirtualKeysInfo(VIRTUAL_KEYS, "", VirtualKeysConstants.CONTROL_CHARS_ALIASES));
		} catch (JSONException e) {
		}

		getWindow()
				.setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
								| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		setContentView(binding.getRoot());
		terminal = binding.termux;

		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
				|| ContextCompat.checkSelfPermission(this,
						Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(
					this,
					new String[] {
							Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
					},
					1000);
		} else {
			initializeLogic();
		}
	}

	@Override
	public void onRequestPermissionsResult(
			int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}

	public void initializeLogic() {
		final File bash = new File(EnvironmentUtils.BIN_DIR, "bash");
		if ((EnvironmentUtils.PREFIX.exists()
				&& EnvironmentUtils.PREFIX.isDirectory()
				&& bash.exists()
				&& bash.isFile()
				&& bash.canExecute())) {
			setupTerminalView(false, "/data/data/com.icst.android.appstudio/files");
		} else {
			setupTerminalView(false, "/data/data/com.icst.android.appstudio/files");
		}
	}

	public void grantFileExecutionPermissiom(File path) {
		try {
			Os.chmod(path.getAbsolutePath(), 0700);
		} catch (ErrnoException e) {
		}
	}

	public void setupTerminalView(boolean systemShell, String cwd) {
		TerminalSession terminalSession;

		terminal.setTextSize((int) terminalTextSize);
		String executablePath;

		grantFileExecutionPermissiom(
				new File(EnvironmentUtils.PREFIX, "etc/termux/bootstrap/termux-bootstrap-second-stage.sh"));

		if (systemShell) {
			executablePath = "/system/bin/sh";
		} else {
			executablePath = EnvironmentUtils.LOGIN_SHELL.getAbsolutePath();
		}
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
				executablePath,
				cwd,
				argsList,
				env,
				TerminalEmulator.DEFAULT_TERMINAL_TRANSCRIPT_ROWS,
				this);
		terminal.setTerminalViewClient(this);
		terminal.attachSession(terminalSession);
	}

	public File mkdirIfNotExits(File in) {
		if (in != null && !in.exists()) {
			in.mkdir();
		}
		if (!in.exists()) {
			if (in.setWritable(true)) {
				in.mkdirs();
			}
		}

		return in;
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

	private float terminalTextSize = 24f;

	@Override
	public float onScale(float scale) {
		float currentTextSize = terminalTextSize;
		float newTextSize = currentTextSize * scale;
		float minTextSize = 10.0f;
		float maxTextSize = 30.0f;
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
			finish();
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
		terminal.onScreenUpdated();
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
}
