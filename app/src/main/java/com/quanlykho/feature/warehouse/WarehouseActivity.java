package com.quanlykho.feature.warehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.quanlykho.R;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.feature.receipt.ReceiptActivity;
import com.quanlykho.model.Receipt;

public class WarehouseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warehouse);
		Toast.makeText(WarehouseActivity.this,"Show kho",Toast.LENGTH_SHORT).show();
	}


}