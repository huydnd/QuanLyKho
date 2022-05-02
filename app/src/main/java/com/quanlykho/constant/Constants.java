package com.quanlykho.constant;

public class Constants {
	// import warehouse table
	public static final String WAREHOUSE_TABLE= "Warehouse";
	public static final String WAREHOUSE_ID= "WarehouseId";
	public static final String WAREHOUSE_NAME= "WarehouseName";
	public static final String WAREHOUSE_ADDRESS= "WarehouseAddress";

	//import supplies table
	public static final String SUPPLIES_TABLE= "Supplies";
	public static final String SUPPLIES_ID="SuppliesId";
	public static final String SUPPLIES_NAME="SuppliesName";
	public static final String SUPPLIES_UNIT="SuppliesUnit";
	public static final String SUPPLIES_IMAGE="SuppliesImage";

	//import receipt table
	public static final String RECEIPT_TABLE="Receipt";
	public static final String RECEIPT_ID="ReceiptId";
	public static final String RECEIPT_WAREHOUSE_ID="Receipt_WarehouseId";
	public static final String RECEIPT_DATE="ReceiptDate";

	//import receipt table
	public static final String EXPORT_TABLE="Export";
	public static final String EXPORT_ID="ExportId";
	public static final String EXPORT_WAREHOUSE_ID="Export_WarehouseId";
	public static final String EXPORT_DATE="ExportDate";

	//import detail table
	public static final String DETAIL_TABLE="Detail";
	public static final String DETAIL_SUPPLIES_ID="Detail_SuppliesId";
	public static final String DETAIL_RECEIPT_ID="Detail_ReceiptId";
	public static final String DETAIL_AMOUNT="DetailAmount";

	//import detail table
	public static final String EXDETAIL_TABLE="ExDetail";
	public static final String EXDETAIL_SUPPLIES_ID="ExDetail_SuppliesId";
	public static final String EXDETAIL_EXPORT_ID="ExDetail_ExportId";
	public static final String EXDETAIL_AMOUNT="ExDetailAmount";
}
