package com.quanlykho.feature.receipthistory;

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
import com.quanlykho.database.dao.DetailQuery;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.model.Detail;
import com.quanlykho.model.Receipt;
import com.quanlykho.model.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class ReceiptListAdapter extends BaseAdapter {

	private ArrayList<Warehouse> warehouses=new ArrayList<Warehouse>();
	private DAO.WarehouseQuery warehouseQuery=new WarehouseQuery();
	private ArrayList<Detail> details=new ArrayList<Detail>();
	private DAO.DetailQuery detailQuery=new DetailQuery();

	private final Context context;
	private final int layout;
	private final List<Receipt> receiptList;

	public ReceiptListAdapter(Context context, int layout, List<Receipt> receiptList) {
		this.context = context;
		this.layout = layout;
		this.receiptList = receiptList;
	}

	@Override
	public int getCount() {
		return receiptList.size();
	}

	@Override
	public Object getItem(int i) {
		return receiptList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return receiptList.get(i).getReceiptId();
	}

	static class ViewHolder{
		public TextView warehouseNameTextView;
		public TextView countSuppliesTextView;
		public TextView receiptDateTextView;
	}
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		if(view==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view= inflater.inflate(layout,null);
			viewHolder = new ViewHolder();
			viewHolder.warehouseNameTextView = view.findViewById(R.id.warehouseNameTextView);
			viewHolder.countSuppliesTextView = view.findViewById(R.id.countSuppliesTextView);
			viewHolder.receiptDateTextView = view.findViewById(R.id.receiptDateTextView);
			view.setTag(viewHolder);
		}else{
			viewHolder= (ViewHolder) view.getTag();
		}
		Receipt receipt = receiptList.get(i);
		warehouseQuery.readWarehouse(receipt.getReceiptWarehouseId(), new QueryResponse<Warehouse>() {
			@Override
			public void onSuccess(Warehouse data) {
				viewHolder.warehouseNameTextView.setText(data.getWarehouseName());
			}
			@Override
			public void onFailure(String message) {

			}
		});
		detailQuery.readAllDetailFromReceipt(receipt.getReceiptId(), new QueryResponse<List<Detail>>() {
			@Override
			public void onSuccess(List<Detail> data) {
				Log.d("TESTDETAIL","3: "+ data.size());
				viewHolder.countSuppliesTextView.setText(String.valueOf(data.size()));
			}
			@Override
			public void onFailure(String message) {
			}
		});
		viewHolder.receiptDateTextView.setText(receipt.getReceiptDate());
		return view;
	}
}
