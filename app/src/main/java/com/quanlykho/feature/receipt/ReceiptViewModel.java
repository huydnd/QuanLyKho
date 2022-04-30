package com.quanlykho.feature.receipt;

import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quanlykho.model.Supplies;
import com.quanlykho.model.SuppliesDetail;

import java.util.ArrayList;

public class ReceiptViewModel extends ViewModel {
	private final MutableLiveData<ArrayList<SuppliesDetail>> listSuppliesDetail = new MutableLiveData<ArrayList<SuppliesDetail>>();

	public void setData(ArrayList<SuppliesDetail> suppliesDetailArrayList){
		listSuppliesDetail.setValue(suppliesDetailArrayList);
	}

	public LiveData<ArrayList<SuppliesDetail>> getData(){
		return  listSuppliesDetail;
	}

}
