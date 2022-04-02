package com.quanlykho.util;

import android.app.Application;
import android.content.Context;

public class App extends Application {
	public static Context context = null;
	//public static CrudAllEventListener crudAllEventListener;
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}
}
