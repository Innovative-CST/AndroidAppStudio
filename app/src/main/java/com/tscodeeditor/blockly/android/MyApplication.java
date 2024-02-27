package com.tscodeeditor.blockly.android;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
  private static Context mApplicationContext;

  public static Context getContext() {
    return mApplicationContext;
  }

  @Override
  public void onCreate() {
    mApplicationContext = getApplicationContext();
    super.onCreate();
  }
}
