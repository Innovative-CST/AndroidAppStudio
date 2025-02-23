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

package com.icst.android.appstudio.utils;

import java.util.regex.Pattern;

/*
 * Class for validating a TextField using pattern.
 */
public class Validator {
	private String pattern;
	private boolean enablePatternValidator;
	private int minimumCharacterRequired;
	private boolean enableMinimumCharacterRequired;
	private int maxmumCharacterLimit;
	private boolean enableMaximumCharacterLimit;
	private boolean isRequired;

	public String getPattern() {
		return this.pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int getMinimumCharacterRequired() {
		return this.minimumCharacterRequired;
	}

	public void setMinimumCharacterRequired(int minimumCharacterRequired) {
		this.minimumCharacterRequired = minimumCharacterRequired;
	}

	public boolean getEnableMinimumCharacterRequired() {
		return this.enableMinimumCharacterRequired;
	}

	public void setEnableMinimumCharacterRequired(boolean enableMinimumCharacterRequired) {
		this.enableMinimumCharacterRequired = enableMinimumCharacterRequired;
	}

	public int getMaxmumCharacterLimit() {
		return this.maxmumCharacterLimit;
	}

	public void setMaxmumCharacterLimit(int maxmumCharacterLimit) {
		this.maxmumCharacterLimit = maxmumCharacterLimit;
	}

	public boolean getEnableMaximumCharacterLimit() {
		return this.enableMaximumCharacterLimit;
	}

	public void setEnableMaximumCharacterLimit(boolean enableMaximumCharacterLimit) {
		this.enableMaximumCharacterLimit = enableMaximumCharacterLimit;
	}

	public boolean getIsRequired() {
		return this.isRequired;
	}

	public void setIsRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getInValidError(String text) {
		String trimmedText = text.trim();
		if (getIsRequired()) {
			if (trimmedText.isEmpty()) {
				return "Field is required";
			}
		} else {
			if (trimmedText.isEmpty()) {
				return null;
			}
		}
		if (getEnableMinimumCharacterRequired()) {
			if (getMinimumCharacterRequired() > trimmedText.length()) {
				return "Text is too short.Minimum length is " + getMinimumCharacterRequired();
			}
		}
		if (getEnableMaximumCharacterLimit()) {
			if (getMaxmumCharacterLimit() < trimmedText.length()) {
				return "Too many characters entered! Maximum length is " + getMaxmumCharacterLimit();
			}
		}
		if (getEnablePatternValidator()) {
			Pattern compiledPattern = Pattern.compile(pattern);
			if (compiledPattern.matcher(trimmedText).matches()) {
				return null;
			}
		} else {
			return null;
		}
		return "Invalid data entered!";
	}

	public boolean getEnablePatternValidator() {
		return this.enablePatternValidator;
	}

	public void setEnablePatternValidator(boolean enablePatternValidator) {
		this.enablePatternValidator = enablePatternValidator;
	}

	public boolean isValid(String text) {
		String trimmedText = text.trim();
		if (getIsRequired()) {
			if (trimmedText.isEmpty()) {
				return false;
			}
		} else {
			if (trimmedText.isEmpty()) {
				return true;
			}
		}
		if (getEnableMinimumCharacterRequired()) {
			if (getMinimumCharacterRequired() > trimmedText.length()) {
				return false;
			}
		}
		if (getEnableMaximumCharacterLimit()) {
			if (getMaxmumCharacterLimit() < trimmedText.length()) {
				return false;
			}
		}
		if (getEnablePatternValidator()) {
			Pattern compiledPattern = Pattern.compile(pattern);
			if (compiledPattern.matcher(trimmedText).matches()) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}
}
