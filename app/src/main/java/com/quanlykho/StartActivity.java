package com.quanlykho;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.SQLiteDatabaseHelper;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.feature.login.LoginActivity;
import com.quanlykho.model.Warehouse;
import com.quanlykho.util.App;

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
			setDatabase();
			startActivity(new Intent(StartActivity.this, LoginActivity.class));
		}
	}
	private void setDatabase() {
		SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getInstance();
	}
}