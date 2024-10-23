package com.elfilibustero.uidesigner;

import android.content.Context;

public class AppLoader {

	private static Context context;

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		AppLoader.context = context;
	}
}
