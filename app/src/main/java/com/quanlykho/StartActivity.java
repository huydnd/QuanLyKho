package com.quanlykho;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.quanlykho.feature.dashboard.DashboardActivity;

public class StartActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			// close splash activity
			finish();
			// Start main activity
			startActivity(new Intent(StartActivity.this, DashboardActivity.class));


		}
	}
}