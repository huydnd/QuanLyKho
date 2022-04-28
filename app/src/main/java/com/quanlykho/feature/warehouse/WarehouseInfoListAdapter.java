package com.quanlykho.feature.warehouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanlykho.R;
import com.quanlykho.feature.receipt.ReceiptDetailListAdapter;
import com.quanlykho.model.SuppliesDetail;

import java.util.List;

public class WarehouseInfoListAdapter extends BaseAdapter {
	private final Context context;
	private final int layout;
	private final List<SuppliesDetail> suppliesDetailList;

	public WarehouseInfoListAdapter(Context context, int layout, List<SuppliesDetail> suppliesDetailList) {
		this.context = context;
		this.layout = layout;
		this.suppliesDetailList = suppliesDetailList;
	}

	@Override
	public int getCount() {
		return suppliesDetailList.size();
	}
	@Override
	public Object getItem(int i) {
		return suppliesDetailList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return suppliesDetailList.get(i).getAmount();
	}

	static class ViewHolder{
		public TextView suppliesName;
		public TextView suppliesUnit;
		public TextView suppliesAmount;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		WarehouseInfoListAdapter.ViewHolder viewHolder;
		if(view==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view= inflater.inflate(layout,null);
			viewHolder = new WarehouseInfoListAdapter.ViewHolder();
			viewHolder.suppliesName = view.findViewById(R.id.receipt_detail_supplies_name);
			viewHolder.suppliesUnit = view.findViewById(R.id.receipt_detail_supplies_unit);
			viewHolder.suppliesAmount = view.findViewById(R.id.receipt_detail_supplies_amount);
			view.setTag(viewHolder);
		}else{
			viewHolder= (WarehouseInfoListAdapter.ViewHolder) view.getTag();
		}
		SuppliesDetail suppliesDetail = suppliesDetailList.get(i);
		viewHolder.suppliesName.setText(suppliesDetail.getName());
		viewHolder.suppliesUnit.setText(suppliesDetail.getUnit());
		viewHolder.suppliesAmount.setText(String.valueOf(suppliesDetail.getAmount()));
		return view;
	}
}
