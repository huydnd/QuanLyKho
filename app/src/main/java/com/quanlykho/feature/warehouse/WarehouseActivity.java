package com.quanlykho.feature.warehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.feature.export.ExportActivity;
import com.quanlykho.feature.exporthistory.ExportHistoryActivity;
import com.quanlykho.feature.receipt.ReceiptActivity;
import com.quanlykho.feature.receipthistory.ReceiptHistoryActivity;
import com.quanlykho.feature.supplies.SuppliesActivity;
import com.quanlykho.model.SuppliesDetail;
import com.quanlykho.model.Warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WarehouseActivity extends AppCompatActivity {

	private DrawerLayout drawerLayout;
	private NavigationView navigationView;


	//For Activity
	private Button addWarehouseButton, editWarehouseButton, delWarehouseButton;
	private AutoCompleteTextView warehouseNameDropdown;
	private final ArrayList<Warehouse> warehouseArrayList=new ArrayList<Warehouse>();
	private ArrayList<String> warehouseNameList = new ArrayList<String>();
	private ArrayAdapter warehouseNameListAdapter;
	//Fragment
	private Fragment fragment=null;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	//For Popup Add
	private Button addButton_addWarehousePopup, cancelButton_addWarehousePopup;
	private EditText nameWarehouseEditText_addWarehousePopup, addressWarehouseEditText_addWarehousePopup;
	private AlertDialog.Builder dialogBuilder;
	private AlertDialog dialog;
	//For Popup Edit
	private Button editButton_editWarehousePopup, cancelButton_editWarehousePopup;
	private EditText nameWarehouseEditText_editWarehousePopup, addressWarehouseEditText_editWarehousePopup;
	private TextView khoInfoTextView;

	private DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();

	private int warehouseIndex=-1;

	@Override
	protected void onResume() {
		super.onResume();
		if(warehouseArrayList.size()>0){
		warehouseNameList= (ArrayList<String>) warehouseArrayList.stream()
				.map(Warehouse::getWarehouseName).collect(Collectors.toList());
		}
		if(warehouseNameList.size()>=5){
			warehouseNameDropdown.setDropDownHeight(720);
		}
		warehouseNameListAdapter = new ArrayAdapter(WarehouseActivity.this, R.layout.dropdown_item, warehouseNameList);
		warehouseNameDropdown.setAdapter(warehouseNameListAdapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warehouse);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_menu_48));
		getSupportActionBar().setTitle("DANH SÁCH KHO");
		setControl();
		setEvent();
		//đọc dữ liệu
		warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
			@Override
			public void onSuccess(List<Warehouse> data) {
				Log.d("TEST","so luong wh="+ data.size());
				warehouseArrayList.addAll(data);
			}
			@Override
			public void onFailure(String message) { }
		});


		warehouseQuery.readAllDetailByWarehouseName(new String("ThuDuc"), new QueryResponse<List<SuppliesDetail>>() {
			@Override
			public void onSuccess(List<SuppliesDetail> data) {
				Log.d("TEST","so luong dt="+ data.size());
			}
			@Override
			public void onFailure(String message) {
				Log.d("TEST",message);
			}
		});
		fragment = new WarehouseInfoFragment();
		fragmentTransaction= getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.warehouseInfoFragment,fragment).commitNow();
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
		addWarehouseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				createWarehousePopup(WarehouseActivity.this);
			}
		});
		editWarehouseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(warehouseIndex == -1){
					Toast.makeText(WarehouseActivity.this,"Chưa kho nào được chọn",Toast.LENGTH_SHORT).show();
				}else{
					editWarehousePopup(WarehouseActivity.this,warehouseIndex);
				}

			}
		});
		delWarehouseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(warehouseIndex==-1){
					Toast.makeText(WarehouseActivity.this,"Chưa kho nào được chọn",Toast.LENGTH_SHORT).show();
				}else{
					new AlertDialog.Builder(WarehouseActivity.this)
							.setTitle("Xác nhận xóa kho")
							.setMessage("Xóa kho "+warehouseNameList.get(warehouseIndex)+" khỏi cơ sở dữ liệu?")

							// Specifying a listener allows you to take an action before dismissing the dialog.
							// The dialog is automatically dismissed when a dialog button is clicked.
							.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									delWarehouse(warehouseNameList.get(warehouseIndex));
								}
							})

							// A null listener allows the button to dismiss the dialog and take no further action.
							.setNegativeButton("Hủy", null)
							.setIcon(android.R.drawable.ic_dialog_alert)
							.show();
				}

			}
		});
		warehouseNameDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				warehouseIndex=i;
				Warehouse warehouse = warehouseArrayList.get(i);
				khoInfoTextView.setText("Mã kho : "+warehouse.getWarehouseId()+"\nĐịa chỉ kho : "+warehouse.getWarehouseAddress());
				fragment = new WarehouseInfoFragment();
				fragment = WarehouseInfoFragment.newInstance(warehouse.getWarehouseName());
				fragmentTransaction= getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.warehouseInfoFragment,fragment).commitNow();
			}
		});
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				int id = menuItem.getItemId();

				switch (id) {
					case R.id.nav_dashboard:
						startActivity( new Intent(WarehouseActivity.this, DashboardActivity.class));
						break;
					case R.id.nav_receipt:
						startActivity( new Intent(WarehouseActivity.this, ReceiptActivity.class));
						break;
					case R.id.nav_export:
						startActivity( new Intent(WarehouseActivity.this, ExportActivity.class));
						break;
					case R.id.nav_warehouse:
						startActivity( new Intent(WarehouseActivity.this, WarehouseActivity.class));
						break;
					case R.id.nav_supplies:
						startActivity( new Intent(WarehouseActivity.this, SuppliesActivity.class));
						break;
					case R.id.nav_history:
						startActivity( new Intent(WarehouseActivity.this, ReceiptHistoryActivity.class));
						break;
					case R.id.nav_ex_history:
						startActivity( new Intent(WarehouseActivity.this, ExportHistoryActivity.class));
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
		addWarehouseButton = findViewById(R.id.addWareHouseButton);
		editWarehouseButton = findViewById(R.id.editWarehouseButton);
		delWarehouseButton = findViewById(R.id.delWarehouseButton);
		warehouseNameDropdown = findViewById(R.id.warehouseNameDropdown);
		khoInfoTextView =findViewById(R.id.khoInfoTextView);
		drawerLayout = findViewById(R.id.activity_warehouse_drawer);
		navigationView = findViewById(R.id.navigationView);
	}


	public void createWarehousePopup(Context context){
		dialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		final View addWarehousePopup = inflater.inflate(R.layout.popup_warehouse_add,null);
		//setControl
		nameWarehouseEditText_addWarehousePopup = addWarehousePopup.findViewById(R.id.popup_warehouse_add_warehouseNameEditText);
		addressWarehouseEditText_addWarehousePopup = addWarehousePopup.findViewById(R.id.popup_warehouse_add_warehouseAddressEditText);
		addButton_addWarehousePopup = addWarehousePopup.findViewById(R.id.popup_warehouse_add_warehouseAddButton);
		cancelButton_addWarehousePopup = addWarehousePopup.findViewById(R.id.popup_warehouse_add_warehouseCancelButton);
		//createAndShow
		dialogBuilder.setView(addWarehousePopup);
		dialog = dialogBuilder.create();
		dialog.show();

		//setListener
		addButton_addWarehousePopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String warehouseName = nameWarehouseEditText_addWarehousePopup.getText().toString();
				String warehouseAddress = addressWarehouseEditText_addWarehousePopup.getText().toString();
				createWarehouse(context,warehouseName,warehouseAddress);
				dialog.dismiss();


			}
		});

		cancelButton_addWarehousePopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});

	}

	void createWarehouse(Context context,String name, String address){
		if(name.length()==0){
			Toast.makeText(context, "Tên kho không được trống", Toast.LENGTH_LONG).show();
			return;
		}
		if(address.length()==0){
			Toast.makeText(context, "Địa chỉ kho không được trống", Toast.LENGTH_LONG).show();
			return;
		}

		DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
		ArrayList<Warehouse> e = new ArrayList<>();
		warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
			@Override
			public void onSuccess(List<Warehouse> data) {
				e.addAll(data);
			}
			@Override
			public void onFailure(String message) {

			}
		});
		for(Warehouse w : e) {
			Log.d("TEST","Show list ten kho - create: "+w.getWarehouseName());
			if (w.getWarehouseName().equals(name) || w.getWarehouseAddress().equals(address)) {
				Toast.makeText(context, "Thông tin kho đã tồn tại", Toast.LENGTH_LONG).show();
				return;
			}
		}
		Warehouse warehouse = new Warehouse(name,address);
		warehouseQuery.createWarehouse(context,warehouse, new QueryResponse<Boolean>() {
			@Override
			public void onSuccess(Boolean data) {
				warehouseArrayList.removeAll(warehouseArrayList);
				warehouseNameList.removeAll(warehouseNameList);
				warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
					@Override
					public void onSuccess(List<Warehouse> data) {
						warehouseArrayList.addAll(data);
					}
					@Override
					public void onFailure(String message) {
					}
				});
//				ArrayList<String> warehouseNameList= (ArrayList<String>) warehouseArrayList.stream()
//						.map(Warehouse::getWarehouseName).collect(Collectors.toList());
				onResume();
			}
			@Override
			public void onFailure(String message) {
			}
		});

	}

	public void editWarehousePopup(Context context, int index){
		String oldName = warehouseNameList.get(index);
		Warehouse warehouse = new Warehouse();
		warehouseQuery.readWarehouseByName(oldName, new QueryResponse<Warehouse>() {
			@Override
			public void onSuccess(Warehouse data) {
				warehouse.setWarehouseId(data.getWarehouseId());
				warehouse.setWarehouseName(data.getWarehouseName());
				warehouse.setWarehouseAddress(data.getWarehouseAddress());
			}
			@Override
			public void onFailure(String message) {

			}
		});
		dialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		final View editWarehousePopup = inflater.inflate(R.layout.popup_warehouse_edit,null);
		//setControl
		nameWarehouseEditText_editWarehousePopup = editWarehousePopup.findViewById(R.id.popup_warehouse_edit_warehouseNameEditText);
		addressWarehouseEditText_editWarehousePopup = editWarehousePopup.findViewById(R.id.popup_warehouse_edit_warehouseAddressEditText);
		editButton_editWarehousePopup = editWarehousePopup.findViewById(R.id.popup_warehouse_edit_warehouseAddButton);
		cancelButton_editWarehousePopup = editWarehousePopup.findViewById(R.id.popup_warehouse_edit_warehouseCancelButton);
		//setText
		nameWarehouseEditText_editWarehousePopup.setText(warehouse.getWarehouseName());
		addressWarehouseEditText_editWarehousePopup.setText(warehouse.getWarehouseAddress());
		//createAndShow
		dialogBuilder.setView(editWarehousePopup);
		dialog = dialogBuilder.create();
		dialog.show();

		//setListener
		editButton_editWarehousePopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String warehouseName = nameWarehouseEditText_editWarehousePopup.getText().toString();
				String warehouseAddress = addressWarehouseEditText_editWarehousePopup.getText().toString();
				if(warehouseName.length()==0){
					Toast.makeText(context, "Tên kho không được trống", Toast.LENGTH_LONG).show();
					return;
				}
				if(warehouseAddress.length()==0){
					Toast.makeText(context, "Địa chỉ kho không được trống", Toast.LENGTH_LONG).show();
					return;
				}
				warehouse.setWarehouseName(warehouseName);
				warehouse.setWarehouseAddress(warehouseAddress);
				editWarehouse(context,warehouse);
				dialog.dismiss();
			}
		});

		cancelButton_editWarehousePopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});

	}

	void editWarehouse(Context context,Warehouse warehouse){
		ArrayList<Warehouse> e = new ArrayList<>();
		warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
			@Override
			public void onSuccess(List<Warehouse> data) {
				e.addAll(data);
			}
			@Override
			public void onFailure(String message) {

			}
		});
		for(Warehouse w : e) {
			if(w.getWarehouseId()!=warehouse.getWarehouseId()){
				if (w.getWarehouseName().equals(warehouse.getWarehouseName()) || w.getWarehouseAddress().equals(warehouse.getWarehouseAddress())) {
					Toast.makeText(context, "Thông tin kho đã tồn tại", Toast.LENGTH_LONG).show();
					return;
				}
			}
		}
		warehouseQuery.updateWarehouse(warehouse, new QueryResponse<Boolean>() {
			@Override
			public void onSuccess(Boolean data) {
				warehouseArrayList.removeAll(warehouseArrayList);
				warehouseNameList.removeAll(warehouseNameList);
				warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
					@Override
					public void onSuccess(List<Warehouse> data) {
						warehouseArrayList.addAll(data);
					}
					@Override
					public void onFailure(String message) {
					}
				});
				onResume();
			}
			@Override
			public void onFailure(String message) {
			}
		});
	}

	void delWarehouse(String name){
		warehouseQuery.deleteWarehouseByName(name, new QueryResponse<Boolean>() {
			@Override
			public void onSuccess(Boolean data) {
				warehouseArrayList.removeAll(warehouseArrayList);
				warehouseNameList.removeAll(warehouseNameList);
				warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
					@Override
					public void onSuccess(List<Warehouse> data) {
						warehouseArrayList.addAll(data);
						warehouseNameDropdown.setText("Chọn kho",false);
						warehouseIndex = -1;
					}
					@Override
					public void onFailure(String message) {
						warehouseNameDropdown.setText("Chọn kho",false);
						warehouseIndex = -1;
						warehouseNameList.add(message);
					}
				});
				onResume();
			}
			@Override
			public void onFailure(String message) {
			}
		});
	}
}