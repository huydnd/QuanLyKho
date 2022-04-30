package com.quanlykho.feature.receipthistory;

import static com.quanlykho.util.Import.detailListToSuppliesDetailList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.DetailQuery;
import com.quanlykho.database.dao.ReceiptQuery;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.feature.export.ExportActivity;
import com.quanlykho.feature.exportHistory.ExportHistoryActivity;
import com.quanlykho.feature.receipt.ReceiptActivity;
import com.quanlykho.feature.receipt.ReceiptDetailListAdapter;
import com.quanlykho.feature.supplies.SuppliesActivity;
import com.quanlykho.feature.supplies.SuppliesListAdapter;
import com.quanlykho.feature.warehouse.WarehouseActivity;
import com.quanlykho.model.Detail;
import com.quanlykho.model.Receipt;
import com.quanlykho.model.Supplies;
import com.quanlykho.model.SuppliesDetail;
import com.quanlykho.model.Warehouse;
import com.quanlykho.util.Import;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ReceiptHistoryActivity extends AppCompatActivity {


	private DrawerLayout drawerLayout;
	private NavigationView navigationView;

	private ListView receiptHistoryListView;
	private ImageButton datePickerButton,directionButton;
	private ToggleButton filterButton;
	private TextInputEditText dateTextView;
	private MaterialDatePicker datePicker;


	private ArrayList<Receipt> receiptArrayList=new ArrayList<Receipt>();
	private ArrayList<Receipt> tempReceiptArrayList=new ArrayList<Receipt>();
	private DAO.ReceiptQuery receiptQuery=new ReceiptQuery();
	private DAO.DetailQuery detailQuery=new DetailQuery();

	private AlertDialog.Builder dialogBuilder;
	private AlertDialog dialog;

	ReceiptListAdapter receiptListAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_receipt);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_menu_48));
		getSupportActionBar().setTitle("LỊCH SỬ NHẬP KHO");
		MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
		datePicker = materialDateBuilder.build();
		setControl();
		setEvent();
		receiptQuery.readAllReceipt(new QueryResponse<List<Receipt>>() {
			@Override
			public void onSuccess(List<Receipt> data) {
				receiptArrayList.addAll(data);
			}
			@Override
			public void onFailure(String message) {

			}
		});

		receiptListAdapter= new ReceiptListAdapter(ReceiptHistoryActivity.this,R.layout.item_receipt,receiptArrayList);
		receiptHistoryListView.setAdapter(receiptListAdapter);
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
		receiptHistoryListView = findViewById(R.id.receipt_history_listview);
		datePickerButton = findViewById(R.id.datePickerButton);
		directionButton = findViewById(R.id.wayButton);
		filterButton = findViewById(R.id.filterButton);
		dateTextView = findViewById(R.id.dateTextView);
		drawerLayout = findViewById(R.id.activity_history_drawer);
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
					ArrayList<Receipt> swapreceiptArrayList=new ArrayList<Receipt>();
					String date=dateTextView.getText().toString();
					for(Receipt e: receiptArrayList){
						if(e.getReceiptDate().equals(date)){
							Log.d("List","Add mr"+date);
							tempReceiptArrayList.add(e);
						}
					}
					swapreceiptArrayList.addAll(receiptArrayList);
					Log.d("List","Sw="+swapreceiptArrayList.size());
					receiptArrayList.removeAll(receiptArrayList);
					receiptArrayList.addAll(tempReceiptArrayList);
					Log.d("List","Re="+receiptArrayList.size());
					tempReceiptArrayList.removeAll(tempReceiptArrayList);
					tempReceiptArrayList.addAll(swapreceiptArrayList);
					Log.d("List","Te="+tempReceiptArrayList.size());
					receiptListAdapter.notifyDataSetChanged();
				}
				else
				{
					receiptArrayList.removeAll(receiptArrayList);
					receiptArrayList.addAll(tempReceiptArrayList);
					tempReceiptArrayList.removeAll(tempReceiptArrayList);
					Log.d("List","Rc="+receiptArrayList.size());
					receiptListAdapter.notifyDataSetChanged();
				}
			}
		});
		directionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Collections.reverse(receiptArrayList);
				receiptListAdapter.notifyDataSetChanged();
			}
		});

		receiptHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				createReceiptDetailPopup(ReceiptHistoryActivity.this,receiptArrayList.get(i));
			}
		});
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
				int id = menuItem.getItemId();

				switch (id) {
					case R.id.nav_dashboard:
						startActivity( new Intent(ReceiptHistoryActivity.this, DashboardActivity.class));
						break;
					case R.id.nav_receipt:
						startActivity( new Intent(ReceiptHistoryActivity.this, ReceiptActivity.class));
						break;
					case R.id.nav_export:
						startActivity( new Intent(ReceiptHistoryActivity.this, ExportActivity.class));
						break;
					case R.id.nav_warehouse:
						startActivity( new Intent(ReceiptHistoryActivity.this, WarehouseActivity.class));
						break;
					case R.id.nav_supplies:
						startActivity( new Intent(ReceiptHistoryActivity.this, SuppliesActivity.class));
						break;
					case R.id.nav_history:
						startActivity( new Intent(ReceiptHistoryActivity.this, ReceiptHistoryActivity.class));
						break;
					case R.id.nav_ex_history:
						startActivity( new Intent(ReceiptHistoryActivity.this, ExportHistoryActivity.class));
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
	public void createReceiptDetailPopup(Context context,Receipt receipt) {
		ArrayList<Detail> detailArrayList = new ArrayList<Detail>();
		dialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View addReceiptDetailPopup = inflater.inflate(R.layout.popup_show_receipt_detail, null);
		ListView listView;
		listView = addReceiptDetailPopup.findViewById(R.id.popup_show_receipt_detail_listview);

		detailQuery.readAllDetailFromReceipt(receipt.getReceiptId(), new QueryResponse<List<Detail>>() {
			@Override
			public void onSuccess(List<Detail> data) {
				detailArrayList.addAll(data);
			}
			@Override
			public void onFailure(String message) {

			}
		});

		ArrayList<SuppliesDetail> suppliesDetailArrayList = new ArrayList<SuppliesDetail>();

		suppliesDetailArrayList.addAll(detailListToSuppliesDetailList(detailArrayList));

		ReceiptDetailListAdapter receiptDetailListAdapter = new ReceiptDetailListAdapter(addReceiptDetailPopup.getContext(),R.layout.item_receipt_detail,suppliesDetailArrayList);

		listView.setAdapter(receiptDetailListAdapter);
		dialogBuilder.setView(addReceiptDetailPopup);
		dialog = dialogBuilder.create();
		dialog.show();
	}
}