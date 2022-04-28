package com.quanlykho.util;


import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.zeugmasolutions.localehelper.LocaleHelper;
import com.zeugmasolutions.localehelper.LocaleHelperApplicationDelegate;

public class App extends Application {
	public static Context context = null;

	private LocaleHelperApplicationDelegate localeHelperApplicationDelegate=new LocaleHelperApplicationDelegate();

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(localeHelperApplicationDelegate.attachBaseContext(base));
	}

	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		localeHelperApplicationDelegate.onConfigurationChanged(this);
	}

	@Override
	public Context getApplicationContext() {
		return LocaleHelper.INSTANCE.onAttach(super.getApplicationContext());
	}
	//public static CrudAllEventListener crudAllEventListener;
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}

}
