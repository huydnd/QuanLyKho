package com.quanlykho.util;

import android.util.Log;

import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.SuppliesQuery;
import com.quanlykho.model.Detail;
import com.quanlykho.model.ExDetail;
import com.quanlykho.model.Supplies;
import com.quanlykho.model.SuppliesByDate;
import com.quanlykho.model.SuppliesDetail;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Import {
	//ham gop chi tiet
	public static ArrayList<SuppliesDetail> mergeDetail(ArrayList<SuppliesDetail> suppliesDetailArrayList) {
		ArrayList<SuppliesDetail> newList = new ArrayList<SuppliesDetail>();
		newList.add(suppliesDetailArrayList.get(0));
		SuppliesDetail suppliesDetail;
		outerloop:
		for (int i = 1; i < suppliesDetailArrayList.size(); i++) {
			suppliesDetail = suppliesDetailArrayList.get(i);
			interloop:
			for (SuppliesDetail sln : newList) {
				if (suppliesDetailArrayList.get(i).getName().equals(sln.getName())) {
					sln.setAmount(sln.getAmount() + suppliesDetail.getAmount());
					continue outerloop;
				}
			}
			newList.add(suppliesDetail);
		}
		return newList;
	}

	//ham danh sach nhap -> chi tiet nhap
	public static ArrayList<Detail> suppliesDetailListToDetailList(ArrayList<SuppliesDetail> suppliesDetailArrayList,int receiptId){
		DAO.SuppliesQuery suppliesQuery=new SuppliesQuery();
		ArrayList<Supplies> suppliesArrayList=new ArrayList<Supplies>();
		suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
			@Override
			public void onSuccess(List<Supplies> data) {
				suppliesArrayList.addAll(data);
			}
			@Override
			public void onFailure(String message) {
			}
		});
		ArrayList<Detail> detailArrayList = new ArrayList<Detail>();
		HashMap<String,Integer> hashMap = new HashMap<String,Integer>();
		for(Supplies e: suppliesArrayList){
			hashMap.put(e.getSuppliesName(),e.getSuppliesId());
		}
		for(SuppliesDetail e: suppliesDetailArrayList){
			Detail detail=new Detail();
			detail.setDetailReceiptId(receiptId);
			detail.setDetailSuppliesId(hashMap.get(e.getName()));
			detail.setDetailAmount(e.getAmount());
			detailArrayList.add(detail);
		}
		return detailArrayList;
	}

	//ham danh sach xuat -> chi tiet xuat
	public static ArrayList<ExDetail> suppliesDetailListToExDetailList(ArrayList<SuppliesDetail> suppliesDetailArrayList,int exportId){
		DAO.SuppliesQuery suppliesQuery=new SuppliesQuery();
		ArrayList<Supplies> suppliesArrayList=new ArrayList<Supplies>();
		suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
			@Override
			public void onSuccess(List<Supplies> data) {
				suppliesArrayList.addAll(data);
			}
			@Override
			public void onFailure(String message) {
			}
		});
		ArrayList<ExDetail> exDetailArrayList = new ArrayList<ExDetail>();
		HashMap<String,Integer> hashMap = new HashMap<String,Integer>();
		for(Supplies e: suppliesArrayList){
			hashMap.put(e.getSuppliesName(),e.getSuppliesId());
		}
		for(SuppliesDetail e: suppliesDetailArrayList){
			ExDetail exDetail=new ExDetail();
			exDetail.setExDetailExportId(exportId);
			exDetail.setExDetailSuppliesId(hashMap.get(e.getName()));
			exDetail.setExDetailAmount(e.getAmount());
			exDetailArrayList.add(exDetail);
		}
		return exDetailArrayList;
	}

	//chi tiet nhap kho => danh sach show
	public static ArrayList<SuppliesDetail> detailListToSuppliesDetailList(ArrayList<Detail> detailArrayList){
		DAO.SuppliesQuery suppliesQuery=new SuppliesQuery();
		ArrayList<Supplies> suppliesArrayList=new ArrayList<Supplies>();
		suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
			@Override
			public void onSuccess(List<Supplies> data) {
				suppliesArrayList.addAll(data);
			}
			@Override
			public void onFailure(String message) {
			}
		});
		ArrayList<SuppliesDetail> suppliesDetailArrayList = new ArrayList<SuppliesDetail>();
		HashMap<Integer,String> hashMapName = new HashMap<Integer,String>();
		HashMap<Integer,String> hashMapUnit = new HashMap<Integer,String>();
		for(Supplies e: suppliesArrayList){
			hashMapName.put(e.getSuppliesId(),e.getSuppliesName());
			hashMapUnit.put(e.getSuppliesId(),e.getSuppliesUnit());
		}
		for(Detail e: detailArrayList){
			SuppliesDetail suppliesDetail=new SuppliesDetail();
			suppliesDetail.setName(hashMapName.get(e.getDetailSuppliesId()));
			suppliesDetail.setUnit(hashMapUnit.get(e.getDetailSuppliesId()));
			suppliesDetail.setAmount(e.getDetailAmount());

			suppliesDetailArrayList.add(suppliesDetail);
		}
		return suppliesDetailArrayList;
	}

	//chi tiet xuat kho => danh sach show
	public static ArrayList<SuppliesDetail> exDetailListToSuppliesDetailList(ArrayList<ExDetail> exDetailArrayList){
		DAO.SuppliesQuery suppliesQuery=new SuppliesQuery();
		ArrayList<Supplies> suppliesArrayList=new ArrayList<Supplies>();
		suppliesQuery.readAllSupplies(new QueryResponse<List<Supplies>>() {
			@Override
			public void onSuccess(List<Supplies> data) {
				suppliesArrayList.addAll(data);
			}
			@Override
			public void onFailure(String message) {
			}
		});
		ArrayList<SuppliesDetail> suppliesDetailArrayList = new ArrayList<SuppliesDetail>();
		HashMap<Integer,String> hashMapName = new HashMap<Integer,String>();
		HashMap<Integer,String> hashMapUnit = new HashMap<Integer,String>();
		for(Supplies e: suppliesArrayList){
			hashMapName.put(e.getSuppliesId(),e.getSuppliesName());
			hashMapUnit.put(e.getSuppliesId(),e.getSuppliesUnit());
		}
		for(ExDetail e: exDetailArrayList){
			SuppliesDetail suppliesDetail=new SuppliesDetail();
			suppliesDetail.setName(hashMapName.get(e.getExDetailSuppliesId()));
			suppliesDetail.setUnit(hashMapUnit.get(e.getExDetailSuppliesId()));
			suppliesDetail.setAmount(e.getExDetailAmount());

			suppliesDetailArrayList.add(suppliesDetail);
		}
		return suppliesDetailArrayList;
	}

	//ham list nhap - list xuat = list con lai
	public static ArrayList<SuppliesDetail> subtractListSuppliesDetail(ArrayList<SuppliesDetail> receipt,ArrayList<SuppliesDetail> export){
		Log.d("ex","rc="+receipt.toString());
		Log.d("ex","ex="+export.toString());
		for(SuppliesDetail e: export){
			int x = e.getAmount();
			for(SuppliesDetail f : receipt){
				if(e.getName().equals(f.getName())){
					int y=f.getAmount();
					f.setAmount(y-x);
				}
			}
		}
		Log.d("ex","rc="+receipt.toString());
		return receipt;
	}


	//suppliesDetail  list to supplies By Date



}
