package com.quickersilver.themeengine;

import androidx.annotation.ColorRes;
import androidx.annotation.StyleRes;

public enum Theme {
  AMBER(R.style.Theme_ThemeEngine_Amber, R.color.amber_theme_primary);
  
  @StyleRes private final int themeId;

  @ColorRes private final int primaryColor;

  Theme(@StyleRes int themeId, @ColorRes int primaryColor) {
    this.themeId = themeId;
    this.primaryColor = primaryColor;
  }

  @StyleRes
  public int getThemeId() {
    return themeId;
  }

  @ColorRes
  public int getPrimaryColor() {
    return primaryColor;
  }
}
