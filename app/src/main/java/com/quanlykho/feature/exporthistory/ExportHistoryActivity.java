package com.quanlykho.feature.exporthistory;

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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.ExDetailQuery;
import com.quanlykho.database.dao.ExportQuery;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.feature.export.ExportActivity;
import com.quanlykho.feature.pdfexport.CreatePdfActivity;
import com.quanlykho.feature.receipt.ReceiptActivity;
import com.quanlykho.feature.receipt.ReceiptDetailListAdapter;
import com.quanlykho.feature.receipthistory.ReceiptHistoryActivity;
import com.quanlykho.feature.statistic.SuppliesChartActivity;
import com.quanlykho.feature.supplies.SuppliesActivity;
import com.quanlykho.feature.warehouse.WarehouseActivity;
import com.quanlykho.model.ExDetail;
import com.quanlykho.model.Export;
import com.quanlykho.model.SuppliesDetail;
import com.quanlykho.util.Import;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExportHistoryActivity extends AppCompatActivity {
	private DrawerLayout drawerLayout;
	private NavigationView navigationView;

	private ListView exportHistoryListView;
	private ImageButton datePickerButton,directionButton;
	private ToggleButton filterButton;
	private TextInputEditText dateTextView;
	private MaterialDatePicker datePicker;


	private ArrayList<Export> exportArrayList=new ArrayList<Export>();
	private ArrayList<Export> tempExportArrayList=new ArrayList<Export>();
	private DAO.ExportQuery exportQuery=new ExportQuery();
	private DAO.ExDetailQuery exDetailQuery=new ExDetailQuery();

	private AlertDialog.Builder dialogBuilder;
	private AlertDialog dialog;

	ExportListAdapter exportListAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_export_history);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_menu_48));
		getSupportActionBar().setTitle("LỊCH SỬ XUẤT KHO");
		MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
		datePicker = materialDateBuilder.build();
		setControl();
		setEvent();
		exportQuery.readAllExport(new QueryResponse<List<Export>>() {
			@Override
			public void onSuccess(List<Export> data) {
				exportArrayList.addAll(data);
			}
			@Override
			public void onFailure(String message) {

			}
		});

		exportListAdapter= new ExportListAdapter(ExportHistoryActivity.this,R.layout.item_receipt,exportArrayList);
		exportHistoryListView.setAdapter(exportListAdapter);
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

	void setControl(){
		exportHistoryListView = findViewById(R.id.ex_export_history_listview);
		datePickerButton = findViewById(R.id.ex_datePickerButton);
		directionButton = findViewById(R.id.ex_wayButton);
		filterButton = findViewById(R.id.ex_filterButton);
		dateTextView = findViewById(R.id.ex_dateTextView);
		drawerLayout = findViewById(R.id.activity_ex_history_drawer);
		navigationView = findViewById(R.id.navigationView);
	}

	void setEvent(){
		datePickerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
			}
		});
		datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
			@Override
			public void onPositiveButtonClick(Object selection) {
				SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
				dateTextView.setText(simpleFormat.format(datePicker.getSelection()));
			}
		});
		filterButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b)
				{
					ArrayList<Export> swapExportArrayList=new ArrayList<Export>();
					String date=dateTextView.getText().toString();
					for(Export e: exportArrayList){
						if(e.getExportDate().equals(date)){
							Log.d("List","Add mr"+date);
							tempExportArrayList.add(e);
						}
					}
					swapExportArrayList.addAll(exportArrayList);
					Log.d("List","Sw="+swapExportArrayList.size());
					exportArrayList.removeAll(exportArrayList);
					exportArrayList.addAll(tempExportArrayList);
					Log.d("List","Re="+exportArrayList.size());
					tempExportArrayList.removeAll(tempExportArrayList);
					tempExportArrayList.addAll(swapExportArrayList);
					Log.d("List","Te="+tempExportArrayList.size());
					exportListAdapter.notifyDataSetChanged();
				}
				else
				{
					exportArrayList.removeAll(exportArrayList);
					exportArrayList.addAll(tempExportArrayList);
					tempExportArrayList.removeAll(tempExportArrayList);
					Log.d("List","Rc="+exportArrayList.size());
					exportListAdapter.notifyDataSetChanged();
				}
			}
		});
		directionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Collections.reverse(exportArrayList);
				exportListAdapter.notifyDataSetChanged();
			}
		});

		exportHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				createExportDetailPopup(ExportHistoryActivity.this,exportArrayList.get(i));
			}
		});
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				int id = menuItem.getItemId();

				switch (id) {
					case R.id.nav_dashboard:
						startActivity( new Intent(ExportHistoryActivity.this, DashboardActivity.class));
						break;
					case R.id.nav_receipt:
						startActivity( new Intent(ExportHistoryActivity.this, ReceiptActivity.class));
						break;
					case R.id.nav_export:
						startActivity( new Intent(ExportHistoryActivity.this, ExportActivity.class));
						break;
					case R.id.nav_warehouse:
						startActivity( new Intent(ExportHistoryActivity.this, WarehouseActivity.class));
						break;
					case R.id.nav_supplies:
						startActivity( new Intent(ExportHistoryActivity.this, SuppliesActivity.class));
						break;
					case R.id.nav_history:
						startActivity( new Intent(ExportHistoryActivity.this, ReceiptHistoryActivity.class));
						break;
					case R.id.nav_ex_history:
						startActivity( new Intent(ExportHistoryActivity.this, ExportHistoryActivity.class));
						break;
					case R.id.nav_statistics:
						startActivity( new Intent(ExportHistoryActivity.this, SuppliesChartActivity.class));
						break;
					case R.id.nav_pdf:
						startActivity( new Intent(ExportHistoryActivity.this, CreatePdfActivity.class));
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
	public void createExportDetailPopup(Context context, Export export) {
		ArrayList<ExDetail> exDetailsArrayList = new ArrayList<ExDetail>();
		dialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View addExportDetailPopup = inflater.inflate(R.layout.popup_show_receipt_detail, null);
		ListView listView;
		listView = addExportDetailPopup.findViewById(R.id.popup_show_receipt_detail_listview);

		exDetailQuery.readAllExDetailFromExport(export.getExportId(), new QueryResponse<List<ExDetail>>() {
			@Override
			public void onSuccess(List<ExDetail> data) {
				exDetailsArrayList.addAll(data);
			}
			@Override
			public void onFailure(String message) {

			}
		});
		ArrayList<SuppliesDetail> suppliesDetailArrayList = new ArrayList<SuppliesDetail>();
		suppliesDetailArrayList.addAll(Import.exDetailListToSuppliesDetailList(exDetailsArrayList));
		ReceiptDetailListAdapter receiptDetailListAdapter = new ReceiptDetailListAdapter(addExportDetailPopup.getContext(),R.layout.item_receipt_detail,suppliesDetailArrayList);
		listView.setAdapter(receiptDetailListAdapter);
		dialogBuilder.setView(addExportDetailPopup);
		dialog = dialogBuilder.create();
		dialog.show();
	}
}