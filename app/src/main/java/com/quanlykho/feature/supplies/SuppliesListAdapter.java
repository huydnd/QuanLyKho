package com.quanlykho.feature.supplies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quanlykho.R;
import com.quanlykho.model.Supplies;
import java.util.List;

public class SuppliesListAdapter extends BaseAdapter {

	CustomButtonListener customListener=null;
	public interface CustomButtonListener{
		public void onEditButtonClickListener(int position);
		public void onDelButtonClickListenner(int position);
	}
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

	public static class ViewHolder{
		public TextView suppliesName;
		public TextView suppliesUnit;
		public ImageButton editButton;
		public ImageButton delButton;
	}

	public void setCustomButtonListener(CustomButtonListener customListener){
		this.customListener=customListener;
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
			viewHolder.editButton=view.findViewById(R.id.editSuppliesIconButton);
			viewHolder.delButton=view.findViewById(R.id.delSuppliesIconButton);
			view.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) view.getTag();
		}
		Supplies supplies = suppliesList.get(i);
		viewHolder.suppliesName.setText(supplies.getSuppliesName());
		viewHolder.suppliesUnit.setText(supplies.getSuppliesUnit());
		viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(customListener != null){
					customListener.onEditButtonClickListener(i);
				}
			}
		});
		viewHolder.delButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(customListener!=null){
					customListener.onDelButtonClickListenner(i);
				}
			}
		});
		return view;
	}
}
