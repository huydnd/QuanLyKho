package com.quanlykho.feature.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.quanlykho.R;
import com.quanlykho.StartActivity;
import com.quanlykho.feature.receipt.ReceiptActivity;
import com.quanlykho.feature.receipt.ReceiptHistoryActivity;
import com.quanlykho.feature.supplies.SuppliesActivity;
import com.quanlykho.feature.warehouse.WarehouseActivity;


public class DashboardActivity extends AppCompatActivity {

	Button receiptButton, warehouseButton,suppliesButton, receiptHistoryButton,quitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		Toast.makeText(DashboardActivity.this,"Dashboard",Toast.LENGTH_SHORT).show();
		mapping();
		listening();
	}

	public void listening(){
		receiptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(DashboardActivity.this,ReceiptActivity.class));
			}
		});
		warehouseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(DashboardActivity.this,WarehouseActivity.class);
				startActivity(intent);
			}
		});
		suppliesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(DashboardActivity.this, SuppliesActivity.class);
				startActivity(intent);
			}
		});
		receiptHistoryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(DashboardActivity.this, ReceiptHistoryActivity.class);
				startActivity(intent);
			}
		});
		quitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finishAndRemoveTask();
			}
		});
	}

	private void mapping(){
		receiptButton = (Button) findViewById(R.id.receiptActivityButton);
		warehouseButton = findViewById(R.id.warehouseActivityButton);
		suppliesButton = findViewById(R.id.suppliesActivityButton);
		receiptHistoryButton = findViewById(R.id.receiptHistoryActivityButton);
		quitButton = findViewById(R.id.quitButton);
	}
}