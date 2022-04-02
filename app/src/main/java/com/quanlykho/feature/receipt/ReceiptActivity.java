package com.quanlykho.feature.receipt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.quanlykho.R;

public class ReceiptActivity extends AppCompatActivity {

	ImageButton addDetailButton,delDetailButton;
	Button saveReceiptButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt);
		Toast.makeText(ReceiptActivity.this,"Nhập kho",Toast.LENGTH_SHORT).show();
		mapping();
	}
	private void mapping(){
		addDetailButton = findViewById(R.id.addSuppliesButton);
		delDetailButton = findViewById(R.id.delSuppliesButton);
		saveReceiptButton = findViewById(R.id.saveReceiptButton);

	}


}