/*
 *  This file is part of Android AppStudio.
 *
 *  Android AppStudio is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Android AppStudio is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with Android AppStudio.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.tscodeeditor.android.appstudio.utils;

import java.util.regex.Pattern;

/*
 * Class for validating a TextField using pattern.
 */
public class Validator {
  private String pattern;
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
    Pattern compiledPattern = Pattern.compile(pattern);
    String trimmedText = text.trim();
    if (getIsRequired()) {
      if (trimmedText.length() == 0) {
        return "Field is required";
      }
    } else {
      if (trimmedText.length() == 0) {
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
    if (compiledPattern.matcher(trimmedText).matches()) {
      return null;
    }
    return "Invalid data entered!";
  }

  public boolean isValid(String text) {
    Pattern compiledPattern = Pattern.compile(pattern);
    String trimmedText = text.trim();
    if (getIsRequired()) {
      if (trimmedText.length() == 0) {
        return false;
      }
    } else {
      if (trimmedText.length() == 0) {
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
    if (compiledPattern.matcher(trimmedText).matches()) {
      return true;
    }
    return false;
  }
}
