package com.quanlykho.feature.warehouse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.model.Supplies;
import com.quanlykho.model.SuppliesDetail;
import com.quanlykho.util.Import;

import java.util.ArrayList;
import java.util.List;

public class WarehouseInfoFragment extends Fragment {

	private static String ARG_WAREHOUSE_NAME = "warehouseName";
	private String warehouseName;
	private ArrayList<SuppliesDetail> suppliesDetailArrayList=new ArrayList<SuppliesDetail>();
	private WarehouseInfoListAdapter warehouseInfoListAdapter;

	private ListView warehouseInfoListView;

	DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();


	public WarehouseInfoFragment(){

	}

	public static WarehouseInfoFragment newInstance(String warehouseName){
		WarehouseInfoFragment warehouseInfoFragment= new WarehouseInfoFragment();
		Bundle args = new Bundle();
		args.putString(ARG_WAREHOUSE_NAME,warehouseName);
		warehouseInfoFragment.setArguments(args);
		return warehouseInfoFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			warehouseName = getArguments().getString(ARG_WAREHOUSE_NAME);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_warehouse_info, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		warehouseQuery.readAllDetailByWarehouseName(warehouseName, new QueryResponse<List<SuppliesDetail>>() {
			@Override
			public void onSuccess(List<SuppliesDetail> data) {
				suppliesDetailArrayList.addAll(data);
				Log.d("WTF","data="+data.size());
			}
			@Override
			public void onFailure(String message) {
			}
		});
		ArrayList<SuppliesDetail> tempListSuppliesDetail= new ArrayList<SuppliesDetail>();
		warehouseQuery.readAllExDetailByWarehouseName(warehouseName, new QueryResponse<List<SuppliesDetail>>() {
			@Override
			public void onSuccess(List<SuppliesDetail> data) {
				tempListSuppliesDetail.addAll(data);
			}
			@Override
			public void onFailure(String message) {
				Log.d("TEST",message);
			}
		});
		ArrayList<SuppliesDetail> listSuppliesDetail= new ArrayList<SuppliesDetail>();
		listSuppliesDetail.addAll(Import.subtractListSuppliesDetail(suppliesDetailArrayList,tempListSuppliesDetail));
		suppliesDetailArrayList.removeAll(suppliesDetailArrayList);
		suppliesDetailArrayList.addAll(listSuppliesDetail);

		warehouseInfoListView = view.findViewById(R.id.warehouse_info_listview);

		warehouseInfoListAdapter = new WarehouseInfoListAdapter(this.getContext(),R.layout.item_receipt_detail,suppliesDetailArrayList);
		warehouseInfoListView.setAdapter(warehouseInfoListAdapter);
	}


}
