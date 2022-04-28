package com.quanlykho.feature.export;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quanlykho.model.SuppliesDetail;

import java.util.ArrayList;

public class ExportViewModel extends ViewModel {
	private final MutableLiveData<ArrayList<SuppliesDetail>> listSuppliesDetail = new MutableLiveData<ArrayList<SuppliesDetail>>();

	public void setData(ArrayList<SuppliesDetail> suppliesDetailArrayList){
		listSuppliesDetail.setValue(suppliesDetailArrayList);
	}

	public LiveData<ArrayList<SuppliesDetail>> getData(){
		return  listSuppliesDetail;
	}

	private final MutableLiveData<String> WarehouseName = new MutableLiveData<String>();


	public void setWarehouseName(String warehouseName){
		WarehouseName.setValue(warehouseName);
	}

	public LiveData<String> getWarehouseName(){
		return  WarehouseName;
	}


}
