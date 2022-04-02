package com.quanlykho.database.dao;

import com.quanlykho.database.QueryResponse;
import com.quanlykho.model.Detail;
import com.quanlykho.model.Receipt;
import com.quanlykho.model.Supplies;
import com.quanlykho.model.Warehouse;

import java.util.List;

public interface DAO {
	public interface WarehouseQuery{
		void createWarehouse(Warehouse warehouse, QueryResponse<Boolean> response);
		void readWarehouse(int WarehouseID, QueryResponse<Warehouse> response);
		void readAllWarehouse(QueryResponse<List<Warehouse>> response);
		void updateWarehouse(Warehouse warehouse, QueryResponse<Boolean> response);
		void deleteWarehouse(int WarehouseID, QueryResponse<Boolean> response);
		void anyWarehouseCreated(QueryResponse<Boolean> response);
	}

	public interface SuppliesQuery{
		void createSupplies(Supplies supplies, QueryResponse<Boolean> response);
		void readSupplies(int suppliesId, QueryResponse<Supplies> response);
		void readAllSupplies(QueryResponse<List<Supplies>> response);
		void updateSupplies(Supplies supplies, QueryResponse<Boolean> response);
		void deleteSupplies(int suppliesId, QueryResponse<Boolean> response);
	}

	public interface ReceiptQuery{
		void createReceipt(Receipt receipt, QueryResponse<Boolean> response);
		void readReceipt(int receiptId, QueryResponse<Receipt> response);
//		void readAllReceipt(QueryResponse<List<Receipt>> response);
		void readAllReceiptFromWarehouse(int warehouseId, QueryResponse<List<Receipt>> response);
//		void updateReceipt(Receipt receipt, QueryResponse<Boolean> response);
	}

	public interface DetailQuery{
		void createDetail(Detail detail, QueryResponse<Boolean> response);
		void readDetail(int detail_suppliesId,int detail_receiptId, QueryResponse<Detail> response);
		void readAllDetailFromReceipt(int receiptId, QueryResponse<List <Detail>> response);
	}
}
