package net.suteren.medicomp.domain;

import android.app.Application;
import android.content.Context;

public class ApplicationContextHolder extends Application {
	private static Context instance = null;

	public static void setContext(Context context) {
		instance = context;
	}

	public static Context getContext() {

		return instance;
	}
}
