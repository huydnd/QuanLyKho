package com.quanlykho.util;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.net.Uri;

public class ThirdPartyApp {

	public static void googlemapSearchForAddress(String address){
		String[] arr = address.split(" ");
		String query = "";
		for(String s: arr){
			query += s + " ";
		}
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("https")
				.authority("www.google.com")
				.appendPath("maps")
				.appendPath("search")
				.appendPath("")
				.appendQueryParameter("api", "1")
				.appendQueryParameter("query", query);
		String url = builder.build().toString();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(App.context, intent, null);
	}
}
