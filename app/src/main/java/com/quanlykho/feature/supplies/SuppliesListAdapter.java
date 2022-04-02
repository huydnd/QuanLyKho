package com.quanlykho.feature.supplies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quanlykho.R;
import com.quanlykho.model.Supplies;
import java.util.List;

public class SuppliesListAdapter extends BaseAdapter {

	private final Context context;
	private final int layout;
	private final List<Supplies> suppliesList;

	public SuppliesListAdapter(Context context, int layout, List<Supplies> suppliesList) {
		this.context = context;
		this.layout = layout;
		this.suppliesList = suppliesList;
	}

	@Override
	public int getCount() {
		return suppliesList.size();
	}

	@Override
	public Object getItem(int i) {
		return suppliesList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return suppliesList.get(i).getSuppliesId();
	}

	static class ViewHolder{
		public TextView suppliesName;
		public TextView suppliesUnit;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		if(view == null){
			LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(layout,null);
			viewHolder =new ViewHolder();
			viewHolder.suppliesName= view.findViewById(R.id.suppliesNameText);
			viewHolder.suppliesUnit= view.findViewById(R.id.suppliesUnitText);
			view.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) view.getTag();
		}
		Supplies supplies = suppliesList.get(i);
		viewHolder.suppliesName.setText(supplies.getSuppliesName());
		viewHolder.suppliesUnit.setText(supplies.getSuppliesUnit());
		return view;
	}
}
