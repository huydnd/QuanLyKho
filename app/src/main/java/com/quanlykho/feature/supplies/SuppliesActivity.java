package com.quanlykho.feature.supplies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.quanlykho.R;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.model.Supplies;

import java.util.ArrayList;

public class SuppliesActivity extends AppCompatActivity {

	ListView suppliesListView;
	ArrayList<Supplies> suppliesArrayList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplies);
		Toast.makeText(SuppliesActivity.this,"Show vật tư",Toast.LENGTH_SHORT).show();
		mapping();
		SuppliesListAdapter suppliesListAdapter = new SuppliesListAdapter(this,R.layout.item_supplies,suppliesArrayList);

		suppliesListView.setAdapter(suppliesListAdapter);
	}
	private void listening(){

	}

	private void mapping(){
		suppliesListView = findViewById(R.id.suppliesListView);
	}
}