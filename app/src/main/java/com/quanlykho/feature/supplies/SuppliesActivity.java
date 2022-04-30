package com.quanlykho.feature.supplies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.SuppliesQuery;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.feature.export.ExportActivity;
import com.quanlykho.feature.exportHistory.ExportHistoryActivity;
import com.quanlykho.feature.receipt.ReceiptActivity;
import com.quanlykho.feature.receipthistory.ReceiptHistoryActivity;
import com.quanlykho.feature.warehouse.WarehouseActivity;
import com.quanlykho.model.Supplies;
import com.quanlykho.model.SuppliesDetail;
import com.quanlykho.model.Warehouse;
import com.quanlykho.util.MovableFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SuppliesActivity extends AppCompatActivity implements SuppliesListAdapter.CustomButtonListener {

	private DrawerLayout drawerLayout;
	private NavigationView navigationView;



	ListView suppliesListView;
	ArrayList<Supplies> suppliesArrayList= new ArrayList<Supplies>();
	SuppliesListAdapter suppliesListAdapter;
	MovableFloatingActionButton addButton;
	//For Popup add
	private Button addButton_addSuppliesPopup, cancelButton_addSuppliesPopup;
	private EditText nameSuppliesEditText_addSuppliesPopup, unitSuppliesEditText_addSuppliesPopup;
	//For popup edit
	private Button editButton_editSuppliesPopup, cancelButton_editSuppliesPopup;
	private EditText nameSuppliesEditText_editSuppliesPopup, unitSuppliesEditText_editSuppliesPopup;


	private AlertDialog.Builder dialogBuilder;
	private AlertDialog dialog;
	private DAO.SuppliesQuery suppliesQuery = new SuppliesQuery();

	private int suppliesIndex = -1;

	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplies);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_menu_48));
		getSupportActionBar().setTitle("DANH SÁCH VẬT TƯ");
		setControl();
		setEvent();

		suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
			@Override
			public void onSuccess(List<Supplies> data) {
				suppliesArrayList.addAll(data);
				Toast.makeText(SuppliesActivity.this,"So luong vat tu = "+suppliesArrayList.size(),Toast.LENGTH_SHORT).show();

			}
			@Override
			public void onFailure(String message) {

			}
		});
		suppliesListAdapter = new SuppliesListAdapter(this,R.layout.item_supplies,suppliesArrayList);
		suppliesListAdapter.setCustomButtonListener(this);
		suppliesListView.setAdapter(suppliesListAdapter);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
			case android.R.id.home:
				drawerLayout.openDrawer(Gravity.LEFT);
				break;

			default:break;
		}
		return false;
	}

	private void setEvent(){
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				createSuppliesPopup(SuppliesActivity.this);
			}
		});
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				int id = menuItem.getItemId();

				switch (id) {
					case R.id.nav_dashboard:
						startActivity( new Intent(SuppliesActivity.this, DashboardActivity.class));
						break;
					case R.id.nav_receipt:
						startActivity( new Intent(SuppliesActivity.this, ReceiptActivity.class));
						break;
					case R.id.nav_export:
						startActivity( new Intent(SuppliesActivity.this, ExportActivity.class));
						break;
					case R.id.nav_warehouse:
						startActivity( new Intent(SuppliesActivity.this, WarehouseActivity.class));
						break;
					case R.id.nav_supplies:
						startActivity( new Intent(SuppliesActivity.this, SuppliesActivity.class));
						break;
					case R.id.nav_history:
						startActivity( new Intent(SuppliesActivity.this, ReceiptHistoryActivity.class));
						break;
					case R.id.nav_ex_history:
						startActivity( new Intent(SuppliesActivity.this, ExportHistoryActivity.class));
						break;
					case R.id.nav_quit:
						finishAffinity();
						break;
					default:
						break;
				}
				return true;
			}
		});

	}

	private void setControl(){
		addButton = findViewById(R.id.floatingActionButton);
		suppliesListView = findViewById(R.id.suppliesListView);
		drawerLayout = findViewById(R.id.activity_supplies_drawer);
		navigationView = findViewById(R.id.navigationView);
	}

	void createSuppliesPopup(Context context){
		dialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		final View addSuppliesPopup = inflater.inflate(R.layout.popup_supplies_add,null);
		//setControl
		nameSuppliesEditText_addSuppliesPopup = addSuppliesPopup.findViewById(R.id.popup_supplies_add_suppliesNameEditText);
		unitSuppliesEditText_addSuppliesPopup = addSuppliesPopup.findViewById(R.id.popup_supplies_add_suppliesUnitEditText);
		addButton_addSuppliesPopup = addSuppliesPopup.findViewById(R.id.popup_supplies_add_suppliesAddButton);
		cancelButton_addSuppliesPopup = addSuppliesPopup.findViewById(R.id.popup_supplies_add_suppliesCancelButton);
		//createAndShow
		dialogBuilder.setView(addSuppliesPopup);
		dialog = dialogBuilder.create();
		dialog.show();

		//setListener
		addButton_addSuppliesPopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String suppliesName = nameSuppliesEditText_addSuppliesPopup.getText().toString();
				String suppliesUnit = unitSuppliesEditText_addSuppliesPopup.getText().toString();
				createSupplies(context,suppliesName,suppliesUnit);
				dialog.dismiss();


			}
		});

		cancelButton_addSuppliesPopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
	}

	void createSupplies(Context context,String name, String unit){
		if(name.length()==0){
			Toast.makeText(context, "Tên vật tư không được trống", Toast.LENGTH_SHORT).show();
			return;
		}
		if(unit.length()==0){
			Toast.makeText(context, "Đơn vị không được trống", Toast.LENGTH_SHORT).show();
			return;
		}

		ArrayList<Supplies> e = new ArrayList<>();
		suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
			@Override
			public void onSuccess(List<Supplies> data) {
				e.addAll(data);
			}
			@Override
			public void onFailure(String message) {

			}
		});
		for(Supplies w : e) {
			if (w.getSuppliesName().equals(name)) {
				Toast.makeText(context, "Thông tin vật tư đã tồn tại", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		Supplies supplies= new Supplies(name,unit);
		suppliesQuery.createSupplies(context,supplies, new QueryResponse<Boolean>() {
			@Override
			public void onSuccess(Boolean data) {
				suppliesArrayList.removeAll(suppliesArrayList);
				suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
					@Override
					public void onSuccess(List<Supplies> data) {
						suppliesArrayList.addAll(data);
					}
					@Override
					public void onFailure(String message) {
					}
				});
				suppliesListAdapter.notifyDataSetChanged();
//				ArrayList<String> warehouseNameList= (ArrayList<String>) warehouseArrayList.stream()
//						.map(Warehouse::getWarehouseName).collect(Collectors.toList());
				onResume();
			}
			@Override
			public void onFailure(String message) {
			}
		});

	}


	public void editSuppliesPopup(Context context,int index){
		Supplies supplies = suppliesArrayList.get(index);

		dialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		final View editSuppliesPopup = inflater.inflate(R.layout.popup_supplies_edit,null);
		//setControl
		nameSuppliesEditText_editSuppliesPopup = editSuppliesPopup.findViewById(R.id.popup_supplies_edit_suppliesNameEditText);
		unitSuppliesEditText_editSuppliesPopup = editSuppliesPopup.findViewById(R.id.popup_supplies_edit_suppliesUnitEditText);
		editButton_editSuppliesPopup = editSuppliesPopup.findViewById(R.id.popup_supplies_edit_suppliesEditButton);
		cancelButton_editSuppliesPopup = editSuppliesPopup.findViewById(R.id.popup_supplies_edit_suppliesCancelButton);
		//createAndShow
		nameSuppliesEditText_editSuppliesPopup.setText(supplies.getSuppliesName());
		unitSuppliesEditText_editSuppliesPopup.setText(supplies.getSuppliesUnit());
		dialogBuilder.setView(editSuppliesPopup);
		dialog = dialogBuilder.create();
		dialog.show();

		//setListener
		editButton_editSuppliesPopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String name= nameSuppliesEditText_editSuppliesPopup.getText().toString();
				String unit= unitSuppliesEditText_editSuppliesPopup.getText().toString();
				if(name.length()==0){
					Toast.makeText(context, "Tên vật tư không được trống", Toast.LENGTH_SHORT).show();
					return;
				}
				if(unit.length()==0){
					Toast.makeText(context, "Đơn vị không được trống", Toast.LENGTH_SHORT).show();
					return;
				}
				supplies.setSuppliesName(name);
				supplies.setSuppliesUnit(unit);
				editSupplies(context,supplies);
				dialog.dismiss();
			}
		});

		cancelButton_editSuppliesPopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				dialog.dismiss();
			}
		});
	}

	void editSupplies(Context context,Supplies supplies){
		ArrayList<Supplies> e = new ArrayList<>();
		suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
			@Override
			public void onSuccess(List<Supplies> data) {
				e.addAll(data);
			}
			@Override
			public void onFailure(String message) {

			}
		});
		for(Supplies w : e) {
			if ((w.getSuppliesId()!=supplies.getSuppliesId())&&(w.getSuppliesName().equals(supplies.getSuppliesName()))) {
				Toast.makeText(context, "Thông tin vật tư đã tồn tại", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		suppliesQuery.updateSupplies(supplies, new QueryResponse<Boolean>() {
			@Override
			public void onSuccess(Boolean data) {
				suppliesArrayList.removeAll(suppliesArrayList);
				suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
					@Override
					public void onSuccess(List<Supplies> data) {
						suppliesArrayList.addAll(data);
					}
					@Override
					public void onFailure(String message) {
					}
				});
				suppliesListAdapter.notifyDataSetChanged();
				onResume();
			}
			@Override
			public void onFailure(String message) {
			}
		});

	}
	void delSupplies(String name) {
		suppliesQuery.deleteSuppliesByName(name, new QueryResponse<Boolean>() {
			@Override
			public void onSuccess(Boolean data) {
				suppliesArrayList.removeAll(suppliesArrayList);
				suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
					@Override
					public void onSuccess(List<Supplies> data) {
						suppliesArrayList.addAll(data);
					}

					@Override
					public void onFailure(String message) {
					}
				});
				suppliesListAdapter.notifyDataSetChanged();
				onResume();
			}

			@Override
			public void onFailure(String message) {
			}
		});
	}
	@Override
	public void onEditButtonClickListener(int position) {
		editSuppliesPopup(SuppliesActivity.this,position);
	}

	@Override
	public void onDelButtonClickListenner(int position) {
		delSupplies(suppliesArrayList.get(position).getSuppliesName());
	}


}