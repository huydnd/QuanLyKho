package com.quanlykho.feature.warehouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.model.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class WarehouseAddPopup {
	private WarehouseCrudListener warehouseCrudListener;
	private Button addButton_addWarehousePopup, cancelButton_addWarehousePopup;
	private EditText nameWarehouseEditText_addWarehousePopup, addressWarehouseEditText_addWarehousePopup;

	private AlertDialog.Builder dialogBuilder;
	private AlertDialog dialog;
	public void createAddWarehousePopup(Context context){
		dialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		final View addWarehousePopup = inflater.inflate(R.layout.popup_warehouse_add,null);

		nameWarehouseEditText_addWarehousePopup = addWarehousePopup.findViewById(R.id.popup_warehouse_add_warehouseNameEditText);
		addressWarehouseEditText_addWarehousePopup = addWarehousePopup.findViewById(R.id.popup_warehouse_add_warehouseAddressEditText);
		addButton_addWarehousePopup = addWarehousePopup.findViewById(R.id.popup_warehouse_add_warehouseAddButton);
		cancelButton_addWarehousePopup = addWarehousePopup.findViewById(R.id.popup_warehouse_add_warehouseCancelButton);

		dialogBuilder.setView(addWarehousePopup);
		dialog = dialogBuilder.create();
		dialog.show();


		addButton_addWarehousePopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String warehouseName = nameWarehouseEditText_addWarehousePopup.getText().toString();
				String warehouseAddress = addressWarehouseEditText_addWarehousePopup.getText().toString();
				createWarehouse(context,warehouseName,warehouseAddress);

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
			if (w.getWarehouseName().equals(name) && w.getWarehouseAddress().equals(address)) {
				Toast.makeText(context, "Thông tin kho đã tồn tại", Toast.LENGTH_LONG).show();
				return;
			}
		}
		Warehouse warehouse = new Warehouse(name,address);
		warehouseQuery.createWarehouse(warehouse, new QueryResponse<Boolean>() {
			@Override
			public void onSuccess(Boolean data) {
				dialog.dismiss();
				warehouseCrudListener.onWarehouseListUpdate(data);
			}

			@Override
			public void onFailure(String message) {
				warehouseCrudListener.onWarehouseListUpdate(false);
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			}
		});

	}
}
