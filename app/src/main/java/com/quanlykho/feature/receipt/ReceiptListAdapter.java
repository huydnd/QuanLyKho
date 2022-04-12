package com.quanlykho.feature.receipt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanlykho.R;
import com.quanlykho.model.Receipt;

import java.util.List;

public class ReceiptListAdapter extends BaseAdapter {

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
		viewHolder.warehouseNameTextView.setText(receipt.getReceiptWarehouseId());
		viewHolder.countSuppliesTextView.setText(receipt.getReceiptWarehouseId());
		viewHolder.receiptDateTextView.setText(receipt.getReceiptDate());
		return view;
	}
}
