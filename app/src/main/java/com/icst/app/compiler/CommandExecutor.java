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

package com.icst.app.compiler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.icst.app.compiler.progress.BuildEventProgressListener;

public class CommandExecutor {
	private final ProcessBuilder mProcess = new ProcessBuilder();
	private BuildEventProgressListener listener;
	private final StringWriter mWriter = new StringWriter();

	public CommandExecutor(BuildEventProgressListener listener) {
		this.listener = listener;
	}

	public void setCommands(ArrayList<String> arrayList) {
		mProcess.command(arrayList);
	}

	public String execute() {
		try {
			Process process = mProcess.start();
			Scanner scanner = new Scanner(process.getInputStream());
			while (scanner.hasNextLine()) {
				mWriter.append(scanner.nextLine());
				mWriter.append(System.lineSeparator());
			}

			Scanner scanner2 = new Scanner(process.getErrorStream());
			while (scanner2.hasNextLine()) {
				mWriter.append(scanner2.nextLine());
				mWriter.append(System.lineSeparator());
			}

			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace(new PrintWriter(mWriter));
		}
		return mWriter.toString();
	}
}
