package com.quanlykho.feature.export;

import static com.quanlykho.util.Import.mergeDetail;
import static com.quanlykho.util.Import.suppliesDetailListToExDetailList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.ExDetailQuery;
import com.quanlykho.database.dao.ExportQuery;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.feature.exporthistory.ExportHistoryActivity;
import com.quanlykho.feature.supplies.SuppliesListAdapter;
import com.quanlykho.model.ExDetail;
import com.quanlykho.model.Export;
import com.quanlykho.model.Supplies;
import com.quanlykho.model.SuppliesDetail;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SaveExportActivity extends AppCompatActivity {

	private ArrayList<SuppliesDetail> suppliesDetailArrayList =new ArrayList<SuppliesDetail>();
	private MaterialButton saveButton,cancelButton;
	private ListView listView;
	private ExportDetailListAdapter exportDetailListAdapter;
	private AutoCompleteTextView autoCompleteTextView;
	private DAO.ExportQuery exportQuery=new ExportQuery();
	private DAO.ExDetailQuery exDetailQuery=new ExDetailQuery();

	String address="";
	int warehouseId=-1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_export);
		setControl();
		setEvent();
		address = getIntent().getExtras().getString("address", "");
		warehouseId= getIntent().getExtras().getInt("warehouseId",-1);
		suppliesDetailArrayList =getIntent().getExtras().getParcelableArrayList("list");
		autoCompleteTextView.setText(address);
		exportDetailListAdapter = new ExportDetailListAdapter(SaveExportActivity.this, R.layout.item_receipt_detail,suppliesDetailArrayList);
		listView.setAdapter(exportDetailListAdapter);
	}

	void setControl(){
		saveButton = findViewById(R.id.saveButton);
		cancelButton = findViewById(R.id.cancelButton);
		listView = findViewById(R.id.listViewSaveExport);
		autoCompleteTextView = findViewById(R.id.addressTextView);
	}
	void setEvent(){
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(warehouseId==-1){
					Toast.makeText(SaveExportActivity.this,"WarehouseID = -1",Toast.LENGTH_LONG).show();
					return;
				}
				Export export = new Export(warehouseId,String.valueOf(LocalDateTime.now().toLocalDate()),autoCompleteTextView.getText().toString());
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
						Toast.makeText(SaveExportActivity.this,"Lưu phiếu xuất kho thành công",Toast.LENGTH_LONG).show();
						Intent intent = new Intent(SaveExportActivity.this, ExportHistoryActivity.class);
						startActivity(intent);
					}
					@Override
					public void onFailure(String message) {
						Toast.makeText(SaveExportActivity.this,"Lưu phiếu xuất kho thất bại"+message,Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SaveExportActivity.this, DashboardActivity.class);
				startActivity(intent);
			}
		});
	}

}