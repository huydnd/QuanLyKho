package com.quanlykho.feature.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanlykho.R;
import com.quanlykho.model.Detail;

import java.util.List;

public class DetailListAdapter extends BaseAdapter {

	private final Context context;
	private final int layout;
	private final List<Detail> detailList;

	public DetailListAdapter(Context context, int layout, List<Detail> detailList) {
		this.context = context;
		this.layout = layout;
		this.detailList = detailList;
	}

	@Override
	public int getCount() {
		return detailList.size();
	}

	@Override
	public Object getItem(int i) {
		return detailList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return detailList.get(i).getDetailSuppliesId();
	}

	static class ViewHoder{
		TextView detailName;
		TextView detailAmount;
		TextView detailUnit;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHoder viewHoder;
		if(view==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(layout,null);
			viewHoder =new ViewHoder();
			viewHoder.detailName = view.findViewById(R.id.detailNameText);
			viewHoder.detailAmount = view.findViewById(R.id.detailAmountText);
			viewHoder.detailUnit = view.findViewById(R.id.detailUnitText);
			view.setTag(viewHoder);
		}else{
			viewHoder= (ViewHoder) view.getTag();
		}
		Detail detail = detailList.get(i);
		viewHoder.detailName.setText(detail.getDetailSuppliesId());
		viewHoder.detailAmount.setText(detail.getDetailAmount());
		viewHoder.detailUnit.setText(detail.getDetailSuppliesId());
		return view;
	}
}
