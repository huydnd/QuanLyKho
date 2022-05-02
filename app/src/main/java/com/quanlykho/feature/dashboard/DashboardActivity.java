package com.quanlykho.feature.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.quanlykho.R;
import com.quanlykho.feature.export.ExportActivity;
import com.quanlykho.feature.exporthistory.ExportHistoryActivity;
import com.quanlykho.feature.receipt.ReceiptActivity;
import com.quanlykho.feature.receipthistory.ReceiptHistoryActivity;
import com.quanlykho.feature.supplies.SuppliesActivity;
import com.quanlykho.feature.warehouse.WarehouseActivity;


public class DashboardActivity extends AppCompatActivity {

	private DrawerLayout drawerLayout;
//	private NavigationView navigationView;
	CardView receiptButton,exportButton, warehouseButton,suppliesButton, receiptHistoryButton,exportHistoryButton;
	ImageButton quitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_menu_48));
//		getSupportActionBar().setTitle(R.string.dashboard);
		setControl();
		setEvent();
	}


//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId())
//		{
//			case android.R.id.home:
//				drawerLayout.openDrawer(Gravity.LEFT);
//				break;
//
//			default:break;
//		}
//		return false;
//	}

	private void setControl(){
		receiptButton = findViewById(R.id.receiptActivityButton);
		warehouseButton = findViewById(R.id.warehouseActivityButton);
		suppliesButton = findViewById(R.id.suppliesActivityButton);
		receiptHistoryButton = findViewById(R.id.receiptHistoryActivityButton);
		exportButton =findViewById(R.id.exportActivityButton);
		exportHistoryButton = findViewById(R.id.ExportHistoryActivityButton);
		quitButton = findViewById(R.id.logOutB);
//		drawerLayout = findViewById(R.id.activity_dashboad_drawer);
//		navigationView = findViewById(R.id.navigationView);

	}

	public void setEvent(){
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
		quitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finishAffinity();

			}
		});
//		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//			@Override
//			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//				int id = menuItem.getItemId();
//
//				switch (id) {
//					case R.id.nav_dashboard:
//						startActivity( new Intent(DashboardActivity.this, DashboardActivity.class));
//						break;
//					case R.id.nav_receipt:
//						startActivity( new Intent(DashboardActivity.this, ReceiptActivity.class));
//						break;
//					case R.id.nav_export:
//						startActivity( new Intent(DashboardActivity.this, ExportActivity.class));
//						break;
//					case R.id.nav_warehouse:
//						startActivity( new Intent(DashboardActivity.this, WarehouseActivity.class));
//						break;
//					case R.id.nav_supplies:
//						startActivity( new Intent(DashboardActivity.this, SuppliesActivity.class));
//						break;
//					case R.id.nav_history:
//						startActivity( new Intent(DashboardActivity.this, ReceiptHistoryActivity.class));
//						break;
//					case R.id.nav_ex_history:
//						startActivity( new Intent(DashboardActivity.this, ExportHistoryActivity.class));
//						break;
//					case R.id.nav_quit:
//						finishAndRemoveTask();
//						break;
//					default:
//						break;
//				}
//				return true;
//			}
//		});
	}


}