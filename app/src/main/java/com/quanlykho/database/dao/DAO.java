package com.quanlykho.database.dao;

import android.content.Context;

import com.quanlykho.database.QueryResponse;
import com.quanlykho.model.Detail;
import com.quanlykho.model.ExDetail;
import com.quanlykho.model.Export;
import com.quanlykho.model.Receipt;
import com.quanlykho.model.Supplies;
import com.quanlykho.model.SuppliesDetail;
import com.quanlykho.model.Warehouse;

import java.util.ArrayList;
import java.util.List;

public interface DAO {
	public interface WarehouseQuery{
		void createWarehouse(Context context,Warehouse warehouse, QueryResponse<Boolean> response);
		void readWarehouse(int WarehouseID, QueryResponse<Warehouse> response);
		void readWarehouseByName(String WarehouseName, QueryResponse<Warehouse> response);
		void readAllWarehouse(QueryResponse<List<Warehouse>> response);
		void readAllDetailByWarehouseName(String WarehouseName, QueryResponse<List<SuppliesDetail>> response);
		void readAllExDetailByWarehouseName(String WarehouseName, QueryResponse<List<SuppliesDetail>> response);
		void updateWarehouse(Warehouse warehouse, QueryResponse<Boolean> response);
		void deleteWarehouse(int WarehouseID, QueryResponse<Boolean> response);
		void deleteWarehouseByName(String WarehouseName, QueryResponse<Boolean> response);
		void anyWarehouseCreated(QueryResponse<Boolean> response);
	}

	public interface SuppliesQuery{
		void createSupplies(Context context,Supplies supplies, QueryResponse<Boolean> response);
		void readSupplies(int suppliesId, QueryResponse<Supplies> response);
		void readAllSupplies(QueryResponse<List<Supplies>> response);
		void updateSupplies(Supplies supplies, QueryResponse<Boolean> response);
		void deleteSupplies(int suppliesId, QueryResponse<Boolean> response);
		void deleteSuppliesByName(String suppliesName, QueryResponse<Boolean> response);
	}

	public interface ReceiptQuery{
		void createReceipt(Receipt receipt, QueryResponse<Receipt> response);
		void readReceipt(int receiptId, QueryResponse<Receipt> response);
		void readAllReceipt(QueryResponse<List<Receipt>> response);
		void readAllReceiptFromWarehouse(int warehouseId, QueryResponse<List<Receipt>> response);
//		void updateReceipt(Receipt receipt, QueryResponse<Boolean> response);
	}

	public interface ExportQuery{
		void createExport(Export export, QueryResponse<Export> response);
		void readExport(int exportId, QueryResponse<Export> response);
		void readAllExport(QueryResponse<List<Export>> response);
		void readAllExportFromWarehouse(int warehouseId, QueryResponse<List<Export>> response);
//		void updateExport(Export export, QueryResponse<Boolean> response);
	}

	public interface DetailQuery{
		void createDetail(Detail detail, QueryResponse<Boolean> response);
		void createListDetail(ArrayList<Detail> details, QueryResponse<Boolean> response);
		void readDetail(int detail_suppliesId,int detail_receiptId, QueryResponse<Detail> response);
		void readAllDetailFromReceipt(int receiptId, QueryResponse<List <Detail>> response);
	}
	public interface ExDetailQuery{
		void createExDetail(ExDetail exDetail, QueryResponse<Boolean> response);
		void readExDetail(int exDetail_suppliesId,int exDetail_exportId, QueryResponse<ExDetail> response);
		void readAllExDetailFromExport(int exportId, QueryResponse<List <ExDetail>> response);
	}

}
