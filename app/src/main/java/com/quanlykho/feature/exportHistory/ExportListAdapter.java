package com.quanlykho.feature.exportHistory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanlykho.R;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.ExDetailQuery;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.feature.receipthistory.ReceiptListAdapter;
import com.quanlykho.model.Detail;
import com.quanlykho.model.ExDetail;
import com.quanlykho.model.Export;
import com.quanlykho.model.Receipt;
import com.quanlykho.model.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class ExportListAdapter extends BaseAdapter {
	private ArrayList<Warehouse> warehouses=new ArrayList<Warehouse>();
	private DAO.WarehouseQuery warehouseQuery=new WarehouseQuery();
	private ArrayList<ExDetail> exDetails=new ArrayList<ExDetail>();
	private DAO.ExDetailQuery exDetailQuery=new ExDetailQuery();

	private final Context context;
	private final int layout;
	private final List<Export> exportList;

	public ExportListAdapter(Context context, int layout, List<Export> exportList) {
		this.context = context;
		this.layout = layout;
		this.exportList = exportList;
	}

	@Override
	public int getCount() {
		return exportList.size();
	}

	@Override
	public Object getItem(int i) {
		return exportList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return exportList.get(i).getExportId();
	}

	static class ViewHolder{
		public TextView warehouseNameTextView;
		public TextView countSuppliesTextView;
		public TextView exportDateTextView;
	}
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ExportListAdapter.ViewHolder viewHolder;
		if(view==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view= inflater.inflate(layout,null);
			viewHolder = new ExportListAdapter.ViewHolder();
			viewHolder.warehouseNameTextView = view.findViewById(R.id.warehouseNameTextView);
			viewHolder.countSuppliesTextView = view.findViewById(R.id.countSuppliesTextView);
			viewHolder.exportDateTextView = view.findViewById(R.id.receiptDateTextView);
			view.setTag(viewHolder);
		}else{
			viewHolder= (ExportListAdapter.ViewHolder) view.getTag();
		}
		Export export = exportList.get(i);
		warehouseQuery.readWarehouse(export.getExportWarehouseId(), new QueryResponse<Warehouse>() {
			@Override
			public void onSuccess(Warehouse data) {
				viewHolder.warehouseNameTextView.setText(data.getWarehouseName());
			}
			@Override
			public void onFailure(String message) {

			}
		});
		exDetailQuery.readAllExDetailFromExport(export.getExportId(), new QueryResponse<List<ExDetail>>() {
			@Override
			public void onSuccess(List<ExDetail> data) {
				Log.d("TESTDETAIL","3: "+ data.size());
				viewHolder.countSuppliesTextView.setText(String.valueOf(data.size()));
			}
			@Override
			public void onFailure(String message) {
			}
		});
		viewHolder.exportDateTextView.setText(export.getExportDate());
		return view;
	}

}
