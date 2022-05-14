package com.quanlykho.feature.mapservice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.quanlykho.R;

public class MapActivity extends AppCompatActivity {

//	private Button btn_get;
	private FrameLayout frameLayout;
	private boolean flag = false;
	private String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_map);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_arrow_back_24));
		getSupportActionBar().setTitle("GOOGLE MAP");
		frameLayout = findViewById(R.id.frameLayout);
		Initial();

		if (flag) {
			//Used to Loading the MapFragment
			MapFragment mapFragment = new MapFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mapFragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				break;
			default:break;
		}
		return false;
	}

	private void Initial() {
		if (ContextCompat.checkSelfPermission(MapActivity.this, permission[0]) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[]{permission[0]}, 100);
		} else {
			flag = true;
		}

		if (ContextCompat.checkSelfPermission(MapActivity.this, permission[1]) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[]{permission[1]}, 101);
		} else {
			flag = true;
		}
	}

	private View.OnClickListener PERMISSION = new View.OnClickListener() {
		@Override
		public void onClick(View view) {

			//Used to Check permission of ACCESS_FINE_LOCATION，the requestCode doesn't must be 101 or 100.
			if (ContextCompat.checkSelfPermission(MapActivity.this, permission[0]) == PackageManager.PERMISSION_DENIED) {
				ActivityCompat.requestPermissions(MapActivity.this, new String[]{permission[0]}, 100);
			} else {
				flag = true;
				Toast.makeText(MapActivity.this, "The ACCESS_FINE_LOCATION Permission already have it.", Toast.LENGTH_SHORT).show();
			}

			//Used to Check permission of ACCESS_COARSE_LOCATION，the requestCode doesn't must be 101 or 100.
			if (ContextCompat.checkSelfPermission(MapActivity.this, permission[1]) == PackageManager.PERMISSION_DENIED) {
				ActivityCompat.requestPermissions(MapActivity.this, new String[]{permission[1]}, 101);
			} else {
				flag = true;
				Toast.makeText(MapActivity.this, "The ACCESS_COARSE_LOCATION Permission already have it.", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		//The request result of ACCESS_FINE_LOCATION
		if (requestCode == 100) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				flag = true;
				com.quanlykho.feature.mapservice.MapFragment mapFragment = new com.quanlykho.feature.mapservice.MapFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mapFragment).commit();
			}
		}

		//The request result of ACCESS_COARSE_LOCATION
		if (requestCode == 101) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				flag = true;
				com.quanlykho.feature.mapservice.MapFragment mapFragment = new com.quanlykho.feature.mapservice.MapFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mapFragment).commit();
			}
		}
	}
}