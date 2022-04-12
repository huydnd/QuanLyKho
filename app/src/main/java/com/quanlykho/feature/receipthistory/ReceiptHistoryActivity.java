package com.quanlykho.feature.receipt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.quanlykho.R;
import com.quanlykho.feature.dashboard.DashboardActivity;

public class ReceiptHistoryActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_receipt);
		Toast.makeText(this.getBaseContext(),"Show lịch sử",Toast.LENGTH_SHORT).show();



	}
}