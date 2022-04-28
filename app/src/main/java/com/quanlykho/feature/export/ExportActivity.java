package com.quanlykho.feature.export;

import static com.quanlykho.util.Import.mergeDetail;
import static com.quanlykho.util.Import.suppliesDetailListToExDetailList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.DetailQuery;
import com.quanlykho.database.dao.ExDetailQuery;
import com.quanlykho.database.dao.ExportQuery;
import com.quanlykho.database.dao.ReceiptQuery;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.feature.exporthistory.ExportHistoryActivity;
import com.quanlykho.feature.pdfexport.CreatePdfActivity;
import com.quanlykho.feature.receipt.ReceiptActivity;
import com.quanlykho.feature.receipthistory.ReceiptHistoryActivity;
import com.quanlykho.feature.statistic.SuppliesChartActivity;
import com.quanlykho.feature.supplies.SuppliesActivity;
import com.quanlykho.feature.warehouse.WarehouseActivity;
import com.quanlykho.model.ExDetail;
import com.quanlykho.model.Export;
import com.quanlykho.model.SuppliesDetail;
import com.quanlykho.model.Warehouse;
import com.zeugmasolutions.localehelper.LocaleHelper;
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegate;
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ExportActivity extends AppCompatActivity {

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
	private DAO.ExportQuery exportQuery = new ExportQuery();
	private DAO.ExDetailQuery exDetailQuery =new ExDetailQuery();
	private TextView khoInfoTextView;
	private ImageButton addDetailButton,delDetailButton;
	private Button saveExportButton;
	private int warehouseIndex=-1;

	private ExportViewModel viewModel;

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
		warehouseNameListAdapter = new ArrayAdapter(ExportActivity.this, R.layout.dropdown_item, warehouseNameList);
		warehouseNameDropdown.setAdapter(warehouseNameListAdapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_export);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_menu_48));
		getSupportActionBar().setTitle("XUẤT KHO");
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
		viewModel = new ViewModelProvider(this).get(ExportViewModel.class);
		addDetailButton = findViewById(R.id.addSuppliesButton);
		delDetailButton = findViewById(R.id.delSuppliesButton);
		saveExportButton = findViewById(R.id.saveExportButton);
		warehouseNameDropdown = findViewById(R.id.warehouseExportNameDropdown);
		khoInfoTextView = findViewById(R.id.exkhoInfoTextView);
		drawerLayout = findViewById(R.id.activity_export_drawer);
		navigationView = findViewById(R.id.navigationView);
	}
	private void setEvent(){
		warehouseNameDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Warehouse warehouse = warehouseArrayList.get(i);
				khoInfoTextView.setText("Mã kho : "+warehouse.getWarehouseId()+"\nĐịa chỉ kho : "+warehouse.getWarehouseAddress());
				viewModel.setWarehouseName(warehouse.getWarehouseName());
				warehouseIndex=i;
			}
		});
		saveExportButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				suppliesDetailArrayList=viewModel.getData().getValue();
				if(warehouseIndex==-1){
					//Tao popup chua chon kho vs chi tiet
					Toast.makeText(ExportActivity.this,"Vui lòng chọn kho để nhập!",Toast.LENGTH_SHORT).show();
					return;
				}else if(suppliesDetailArrayList==null){
					Toast.makeText(ExportActivity.this,"Danh sách chi tiết vật tư trống!",Toast.LENGTH_SHORT).show();
					return;
				}

				Export export = new Export(warehouseArrayList.get(warehouseIndex).getWarehouseId(),String.valueOf(LocalDateTime.now().toLocalDate()));
				exportQuery.createExport(export, new QueryResponse<Export>() {
					@Override
					public void onSuccess(Export data) {
						ArrayList<SuppliesDetail>suppliesDetails=mergeDetail(suppliesDetailArrayList);
						ArrayList<ExDetail> exDetails = suppliesDetailListToExDetailList(suppliesDetails,data.getExportId());
						for (ExDetail e: exDetails){
							exDetailQuery.createExDetail(e, new QueryResponse<Boolean>() {
								@Override
								public void onSuccess(Boolean data) {
									Log.d("TESTDETAIL","4: "+e.getExDetailAmount());
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
					}
				});


			}
		});
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				int id = menuItem.getItemId();

				switch (id) {
					case R.id.nav_dashboard:
						startActivity( new Intent(ExportActivity.this, DashboardActivity.class));
						break;
					case R.id.nav_receipt:
						startActivity( new Intent(ExportActivity.this, ReceiptActivity.class));
						break;
					case R.id.nav_export:
						startActivity( new Intent(ExportActivity.this, ExportActivity.class));
						break;
					case R.id.nav_warehouse:
						startActivity( new Intent(ExportActivity.this, WarehouseActivity.class));
						break;
					case R.id.nav_supplies:
						startActivity( new Intent(ExportActivity.this, SuppliesActivity.class));
						break;
					case R.id.nav_history:
						startActivity( new Intent(ExportActivity.this, ReceiptHistoryActivity.class));
						break;
					case R.id.nav_ex_history:
						startActivity( new Intent(ExportActivity.this, ExportHistoryActivity.class));
						break;
					case R.id.nav_statistics:
						startActivity( new Intent(ExportActivity.this, SuppliesChartActivity.class));
						break;
					case R.id.nav_pdf:
						startActivity( new Intent(ExportActivity.this, CreatePdfActivity.class));
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
}