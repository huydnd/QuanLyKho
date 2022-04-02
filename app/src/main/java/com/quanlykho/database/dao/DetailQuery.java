package com.quanlykho.database.dao;

import com.quanlykho.database.QueryResponse;
import com.quanlykho.model.Detail;

import java.util.List;

public class DetailQuery implements DAO.DetailQuery{


	@Override
	public void createDetail(Detail detail, QueryResponse<Boolean> response) {

	}

	@Override
	public void readDetail(int detail_suppliesId, int detail_receiptId, QueryResponse<Detail> response) {

	}

	@Override
	public void readAllDetailFromReceipt(int receiptId, QueryResponse<List<Detail>> response) {

	}
}
