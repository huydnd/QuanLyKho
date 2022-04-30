package com.quanlykho.feature.receipt;

import static com.quanlykho.util.Import.mergeDetail;
import static com.quanlykho.util.Import.suppliesDetailListToDetailList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.DetailQuery;
import com.quanlykho.database.dao.ReceiptQuery;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.feature.export.ExportActivity;
import com.quanlykho.feature.exportHistory.ExportHistoryActivity;
import com.quanlykho.feature.receipthistory.ReceiptHistoryActivity;
import com.quanlykho.feature.supplies.SuppliesActivity;
import com.quanlykho.feature.warehouse.WarehouseActivity;
import com.quanlykho.model.Detail;
import com.quanlykho.model.Receipt;
import com.quanlykho.model.SuppliesDetail;
import com.quanlykho.model.Warehouse;
import com.quanlykho.util.Import;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReceiptActivity extends AppCompatActivity {

	private DrawerLayout drawerLayout;
	private NavigationView navigationView;

	private AutoCompleteTextView warehouseNameDropdown;
	private ArrayList<Warehouse> warehouseArrayList=new ArrayList<Warehouse>();
	private ArrayList<String> warehouseNameList = new ArrayList<String>();
	private ArrayList<SuppliesDetail> suppliesDetailArrayList= new ArrayList<SuppliesDetail>();

	private ArrayAdapter warehouseNameListAdapter;
	private DAO.WarehouseQuery warehouseQuery=new WarehouseQuery();
	private DAO.ReceiptQuery receiptQuery = new ReceiptQuery();
	private DAO.DetailQuery detailQuery =new DetailQuery();
	private TextView khoInfoTextView;
	private ImageButton addDetailButton,delDetailButton;
	private Button saveReceiptButton;
	private int warehouseIndex=-1;

	private ReceiptViewModel viewModel;


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
		warehouseNameListAdapter = new ArrayAdapter(ReceiptActivity.this, R.layout.dropdown_item, warehouseNameList);
		warehouseNameDropdown.setAdapter(warehouseNameListAdapter);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_menu_48));
		getSupportActionBar().setTitle("NHẬP KHO");
		setControl();
		setEvent();
		warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
			@Override
			public void onSuccess(List<Warehouse> data) {
				warehouseArrayList.addAll(data);
			}
			@Override
			public void onFailure(String message) { }
		});
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
	private void setControl(){
		viewModel = new ViewModelProvider(this).get(ReceiptViewModel.class);
		addDetailButton = findViewById(R.id.addSuppliesButton);
		delDetailButton = findViewById(R.id.delSuppliesButton);
		saveReceiptButton = findViewById(R.id.saveReceiptButton);
		warehouseNameDropdown =findViewById(R.id.warehouseReceiptNameDropdown);
		khoInfoTextView =findViewById(R.id.khoInfoTextView);
		drawerLayout = findViewById(R.id.activity_receipt_drawer);
		navigationView = findViewById(R.id.navigationView);
	}
	private void setEvent(){
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				int id = menuItem.getItemId();

				switch (id) {
					case R.id.nav_dashboard:
						startActivity( new Intent(ReceiptActivity.this, DashboardActivity.class));
						break;
					case R.id.nav_receipt:
						startActivity( new Intent(ReceiptActivity.this, ReceiptActivity.class));
						break;
					case R.id.nav_export:
						startActivity( new Intent(ReceiptActivity.this, ExportActivity.class));
						break;
					case R.id.nav_warehouse:
						startActivity( new Intent(ReceiptActivity.this, WarehouseActivity.class));
						break;
					case R.id.nav_supplies:
						startActivity( new Intent(ReceiptActivity.this, SuppliesActivity.class));
						break;
					case R.id.nav_history:
						startActivity( new Intent(ReceiptActivity.this, ReceiptHistoryActivity.class));
						break;
					case R.id.nav_ex_history:
						startActivity( new Intent(ReceiptActivity.this, ExportHistoryActivity.class));
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
		warehouseNameDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Warehouse warehouse = warehouseArrayList.get(i);
				khoInfoTextView.setText("Mã kho : "+warehouse.getWarehouseId()+"\nĐịa chỉ kho : "+warehouse.getWarehouseAddress());
				warehouseIndex=i;
			}
		});
		saveReceiptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				suppliesDetailArrayList=viewModel.getData().getValue();
				if(warehouseIndex==-1){
					//Tao popup chua chon kho vs chi tiet
					Toast.makeText(ReceiptActivity.this,"Vui lòng chọn kho để nhập!",Toast.LENGTH_SHORT).show();
					return;
				}else if(suppliesDetailArrayList==null){
					Toast.makeText(ReceiptActivity.this,"Danh sách chi tiết vật tư trống!",Toast.LENGTH_SHORT).show();
					return;
				}

				Receipt receipt = new Receipt(warehouseArrayList.get(warehouseIndex).getWarehouseId(),String.valueOf(LocalDateTime.now().toLocalDate()));
				receiptQuery.createReceipt(receipt, new QueryResponse<Receipt>() {
					@Override
					public void onSuccess(Receipt data) {
						ArrayList<SuppliesDetail>suppliesDetails=mergeDetail(suppliesDetailArrayList);
						ArrayList<Detail> details = suppliesDetailListToDetailList(suppliesDetails,data.getReceiptId());
						Log.d("TESTDETAIL","3: "+details);
						for (Detail e: details){
							detailQuery.createDetail(e, new QueryResponse<Boolean>() {
								@Override
								public void onSuccess(Boolean data) {
									Log.d("TESTDETAIL","4: "+e.getDetailAmount());
								}
								@Override
								public void onFailure(String message) {
									Log.d("TESTDETAL","INSERT: FAIL CMNR "+e);
								}
							});
						}
						Intent intent = getIntent();
						finish();
						startActivity(intent);
					}
					@Override
					public void onFailure(String message) {
						Toast.makeText(ReceiptActivity.this,"Fail cmnr",Toast.LENGTH_SHORT).show();
					}
				});


			}
		});
	}

}