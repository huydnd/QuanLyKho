package com.quanlykho.feature.mapservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quanlykho.R;
import com.quanlykho.feature.export.SaveExportActivity;
import com.quanlykho.model.SuppliesDetail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SelectAddressActivity extends AppCompatActivity implements OnMapReadyCallback {

	private GoogleMap mMap;
	FloatingActionButton getLocation;
	AutocompleteSupportFragment autocompleteFragment_ori;
	LocationManager manager;
	MyLocationListener listener;
	String chosenAddress = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_address);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_menu_48));
		getSupportActionBar().setTitle("CHỌN ĐỊA CHỈ");

		getLocation = (FloatingActionButton) findViewById(R.id.getLocation);
		FloatingActionButton next = (FloatingActionButton) findViewById(R.id.next);

		listener = new MyLocationListener(this);

		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		try {
			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,6000,0,listener);
		}
		catch (SecurityException ex){
			Toast.makeText(getApplicationContext(),"Access location denied!!!!",Toast.LENGTH_SHORT).show();
		}
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);

		//Initial the Place API
		if (!Places.isInitialized()) {
			Places.initialize(SelectAddressActivity.this, "AIzaSyC30522yFCSX2hRN8GZfOtxM0t_2mpOcvI");
		}
		// Initialize the AutocompleteSupportFragment.
		autocompleteFragment_ori = (AutocompleteSupportFragment)
				getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_ori);
		// Specify the types of place data to return.
		autocompleteFragment_ori.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
		// Set up a PlaceSelectionListener to handle the response.
		autocompleteFragment_ori.setOnPlaceSelectedListener(new PlaceSelectionListener() {
			@Override
			public void onPlaceSelected(@NonNull Place place) {

				if (place.getLatLng() != null) {
					LatLng placeLatLng_ori = place.getLatLng();
					chosenAddress = place.getName();
					mapFragment.getMapAsync(new OnMapReadyCallback() {
						@Override
						public void onMapReady(@NonNull GoogleMap googleMap) {
							googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLatLng_ori));
							googleMap.addMarker(new MarkerOptions().position(new LatLng(placeLatLng_ori.latitude, placeLatLng_ori.longitude)).title(place.getName()));
						}
					});
				}
			}
			@Override
			public void onError(@NonNull Status status) {
				Toast.makeText(SelectAddressActivity.this, "Try to Fetch the Original Place, but occures error.", Toast.LENGTH_SHORT).show();
			}
		});

		Bundle bundle = getIntent().getExtras();

		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SelectAddressActivity.this, SaveExportActivity.class);
				bundle.putString("address", chosenAddress);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

	@SuppressLint("MissingPermission")
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.848348, 106.786234),8));

		getLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMap.clear();
				Geocoder coder = new Geocoder(getApplicationContext());
				List<Address> addressList;
				Location location = null;

				try {
					location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				} catch (SecurityException ex) {
					Toast.makeText(getApplicationContext(), "Access location denied", Toast.LENGTH_SHORT).show();
				}
				if (location != null) {
					LatLng myPos = new LatLng(location.getLatitude(), location.getLongitude());
					try {
						addressList = coder.getFromLocation(myPos.latitude, myPos.longitude, 1);
						if (!addressList.isEmpty()) {
							String myAddress = "";
							for (int i = 0; i <= addressList.get(0).getMaxAddressLineIndex()-1; i++) {
								myAddress+=addressList.get(0).getAddressLine(i) + ", ";
							}
							myAddress+=addressList.get(0).getAddressLine(addressList.get(0).getMaxAddressLineIndex());
							chosenAddress = myAddress;
							mMap.addMarker(new MarkerOptions().position(myPos).title("My Location").snippet(myAddress)).setDraggable(true);
							autocompleteFragment_ori.setText(myAddress);
						}
					}
					catch (IOException e) {
						mMap.addMarker(new MarkerOptions().position(myPos).title("My Location"));
					}
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos,15));
				}
				else {
					Toast.makeText(getApplicationContext(), "Wait for location to be determined", Toast.LENGTH_SHORT).show();
				}
			}
		});
		mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
			@Override
			public void onMarkerDragStart(Marker marker) {

			}

			@Override
			public void onMarkerDrag(Marker marker) {

			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				Geocoder coder = new Geocoder(getApplicationContext());
				List<Address> addressList;

				try {
					addressList = coder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
					if (!addressList.isEmpty()) {
						String myAddress = "";
						for (int i = 0; i <= addressList.get(0).getMaxAddressLineIndex(); i++) {
							myAddress+=addressList.get(0).getAddressLine(i) + ", ";
						}
						chosenAddress = myAddress;
						autocompleteFragment_ori.setText(myAddress);
						marker.setSnippet(myAddress);
					}
					else {
						Toast.makeText(getApplicationContext(), "No address", Toast.LENGTH_SHORT).show();
					}
				}
				catch (IOException e) {
					Toast.makeText(getApplicationContext(), "Can't get location!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}