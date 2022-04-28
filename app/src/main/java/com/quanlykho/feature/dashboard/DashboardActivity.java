package com.quanlykho.feature.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import android.view.Gravity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.quanlykho.R;
import com.quanlykho.feature.export.ExportActivity;
import com.quanlykho.feature.exporthistory.ExportHistoryActivity;
import com.quanlykho.feature.login.LoginActivity;
import com.quanlykho.feature.mapservice.MapActivity;
import com.quanlykho.feature.pdfexport.CreatePdfActivity;
import com.quanlykho.feature.receipt.ReceiptActivity;
import com.quanlykho.feature.receipthistory.ReceiptHistoryActivity;
import com.quanlykho.feature.statistic.SuppliesChartActivity;
import com.quanlykho.feature.supplies.SuppliesActivity;
import com.quanlykho.feature.warehouse.WarehouseActivity;
import com.zeugmasolutions.localehelper.LocaleHelper;
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegate;
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl;
import com.zeugmasolutions.localehelper.Locales;

import java.util.Locale;


public class DashboardActivity extends AppCompatActivity  {
	private DrawerLayout drawerLayout;
	private NavigationView navigationView;
	private View header;
	CardView receiptButton,exportButton, warehouseButton,suppliesButton, receiptHistoryButton,exportHistoryButton,chartButton,pdfButton;
	ImageButton menuButton,quitButton;
	MenuItem menuItem1;
	ToggleButton toggleButton;

	LocaleHelperActivityDelegate localeHelperActivityDelegate= new LocaleHelperActivityDelegateImpl();

	@NonNull
	@Override
	public AppCompatDelegate getDelegate() {
		return localeHelperActivityDelegate.getAppCompatDelegate(super.getDelegate());
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(localeHelperActivityDelegate.attachBaseContext(newBase));
	}

	@Override
	protected void onResume() {
		super.onResume();
		localeHelperActivityDelegate.onResumed(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		localeHelperActivityDelegate.onPaused();
	}

	@Override
	public Context createConfigurationContext(Configuration overrideConfiguration) {
		Context context= super.createConfigurationContext(overrideConfiguration);
		return LocaleHelper.INSTANCE.onAttach(context);
	}

	@Override
	public Context getApplicationContext() {
		return localeHelperActivityDelegate.getApplicationContext(super.getApplicationContext());
	}

	public void updateLocale(Locale locale){
		localeHelperActivityDelegate.setLocale(this,locale);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		localeHelperActivityDelegate.onCreate(this);
		setContentView(R.layout.activity_dashboard);

		setControl();
		setEvent();
	}


	private void setControl(){
		receiptButton = findViewById(R.id.receiptActivityButton);
		warehouseButton = findViewById(R.id.warehouseActivityButton);
		suppliesButton = findViewById(R.id.suppliesActivityButton);
		receiptHistoryButton = findViewById(R.id.receiptHistoryActivityButton);
		exportButton =findViewById(R.id.exportActivityButton);
		exportHistoryButton = findViewById(R.id.ExportHistoryActivityButton);
		menuButton =findViewById(R.id.nav_menu);
		chartButton =findViewById(R.id.statisticActivityButton);
		pdfButton =findViewById(R.id.pdfExportActivityButton);
		quitButton = findViewById(R.id.logOutB);

		drawerLayout = findViewById(R.id.activity_dashboad_drawer);
		navigationView = findViewById(R.id.dbnavigationView);
		header=navigationView.getHeaderView(0);
		menuItem1 = navigationView.getMenu().findItem(R.id.nav_language);
		toggleButton = (ToggleButton) menuItem1.getActionView().findViewById(R.id.lgToggleButton);
	}

	private void setEvent(){
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				int id = menuItem.getItemId();

				switch (id) {
					case R.id.nav_user_info:
						startActivity( new Intent(DashboardActivity.this, DashboardActivity.class));
						break;
					case R.id.nav_map:
						startActivity( new Intent(DashboardActivity.this, MapActivity.class));
						break;
					case R.id.nav_language:
						if(toggleButton.isChecked()){
							toggleButton.setChecked(false);
							updateLocale(Locales.INSTANCE.getEnglish());
							onResume();
						}else {
							toggleButton.setChecked(true);
							updateLocale(Locales.INSTANCE.getVietnamese());
							onResume();
						}
						break;
					case R.id.nav_logut:
						FirebaseAuth.getInstance().signOut();
						startActivity( new Intent(DashboardActivity.this, LoginActivity.class));
						overridePendingTransition(R.anim.slide_in_left,R.anim.stay);
						break;
					default:
						break;
				}
				return true;
			}
		});
//		toggleButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (toggleButton.isChecked()){
//					updateLocale(Locales.INSTANCE.getVietnamese());
//					onResume();
//				}else{
//					updateLocale(Locales.INSTANCE.getEnglish());
//					onResume();
//				}
//			}
//		});
		navigationView.setItemIconTintList(null);
		receiptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(DashboardActivity.this,ReceiptActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
			}
		});
		exportButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(DashboardActivity.this, ExportActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
			}
		});
		warehouseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(DashboardActivity.this,WarehouseActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
			}
		});
		suppliesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(DashboardActivity.this, SuppliesActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
			}
		});
		receiptHistoryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(DashboardActivity.this, ReceiptHistoryActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
			}
		});
		exportHistoryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(DashboardActivity.this, ExportHistoryActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
			}
		});
		chartButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(DashboardActivity.this, SuppliesChartActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
			}
		});
		pdfButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(DashboardActivity.this, CreatePdfActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
			}
		});

		menuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawerLayout.openDrawer(Gravity.LEFT);
			}
		});
		quitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finishAndRemoveTask();
			}
		});
	}

//
}