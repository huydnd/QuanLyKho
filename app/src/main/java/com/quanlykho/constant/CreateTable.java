package com.quanlykho.constant;

public class CreateTable {
	public static String CREATE_WAREHOUSE_TABLE=
			"CREATE TABLE " + Constants.WAREHOUSE_TABLE+  " ("+
					Constants.WAREHOUSE_ID+	" INTEGER NOT NULL UNIQUE,"+
					Constants.WAREHOUSE_NAME+ " TEXT NOT NULL,"+
					Constants.WAREHOUSE_ADDRESS+  " TEXT NOT NULL,"+
					"PRIMARY KEY("+Constants.WAREHOUSE_ID+" AUTOINCREMENT)"+
					")";

	public static String CREATE_SUPPLIES_TABLE=
			"CREATE TABLE "+Constants.SUPPLIES_TABLE+ " ("+
					Constants.SUPPLIES_ID+ " INTEGER NOT NULL UNIQUE,"+
					Constants.SUPPLIES_NAME+ " TEXT NOT NULL,"+
					Constants.SUPPLIES_UNIT+ " TEXT,"+
					Constants.SUPPLIES_IMAGE+ " BLOB,"+
					"PRIMARY KEY("+Constants.SUPPLIES_ID+ " AUTOINCREMENT)"+
					")";

	public static String CREATE_RECEIPT_TABLE=
			"CREATE TABLE "+Constants.RECEIPT_TABLE+ " ("+
					Constants.RECEIPT_ID+ " INTEGER NOT NULL UNIQUE,"+
					Constants.RECEIPT_WAREHOUSE_ID+	" INTEGER NOT NULL,"+
					Constants.RECEIPT_DATE+	" INTEGER NOT NULL,"+
					"FOREIGN KEY (" + Constants.RECEIPT_WAREHOUSE_ID + ") REFERENCES "+ Constants.WAREHOUSE_TABLE + "(" +Constants.WAREHOUSE_ID+ ") ON UPDATE CASCADE,"+
					"PRIMARY KEY("+Constants.RECEIPT_ID+" AUTOINCREMENT)"+
					")";

	public static String CREATE_EXPORT_TABLE=
			"CREATE TABLE "+Constants.EXPORT_TABLE+ " ("+
					Constants.EXPORT_ID+ " INTEGER NOT NULL UNIQUE,"+
					Constants.EXPORT_WAREHOUSE_ID+	" INTEGER NOT NULL,"+
					Constants.EXPORT_DATE+	" INTEGER NOT NULL,"+
					Constants.EXPORT_ADDRESS+ " TEXT,"+
					"FOREIGN KEY (" + Constants.EXPORT_WAREHOUSE_ID + ") REFERENCES "+ Constants.WAREHOUSE_TABLE + "(" +Constants.WAREHOUSE_ID+ ") ON UPDATE CASCADE,"+
					"PRIMARY KEY("+Constants.EXPORT_ID+" AUTOINCREMENT)"+
					")";

	public static String CREATE_DETAIL_TABLE=
			"CREATE TABLE "+Constants.DETAIL_TABLE+" ("+
					Constants.DETAIL_SUPPLIES_ID+"	INTEGER NOT NULL,"+
					Constants.DETAIL_RECEIPT_ID+"  INTEGER NOT NULL,"+
					Constants.DETAIL_AMOUNT+"  INTEGER NOT NULL,"+
					"FOREIGN KEY(" + Constants.DETAIL_RECEIPT_ID + ") REFERENCES " + Constants.RECEIPT_TABLE + "(" + Constants.RECEIPT_ID+ ") ON UPDATE CASCADE,"+
					"FOREIGN KEY(" + Constants.DETAIL_SUPPLIES_ID + ") REFERENCES " + Constants.SUPPLIES_TABLE + "(" +Constants.SUPPLIES_ID + ") ON UPDATE CASCADE,"+
					"PRIMARY KEY("+Constants.DETAIL_SUPPLIES_ID+","+Constants.DETAIL_RECEIPT_ID+")"+
					")";
	public static String CREATE_EXDETAIL_TABLE=
			"CREATE TABLE "+Constants.EXDETAIL_TABLE+" ("+
					Constants.EXDETAIL_SUPPLIES_ID+"	INTEGER NOT NULL,"+
					Constants.EXDETAIL_EXPORT_ID+"  INTEGER NOT NULL,"+
					Constants.EXDETAIL_AMOUNT+"  INTEGER NOT NULL,"+
					"FOREIGN KEY(" + Constants.EXDETAIL_EXPORT_ID + ") REFERENCES " + Constants.EXPORT_TABLE + "(" + Constants.EXPORT_ID+ ") ON UPDATE CASCADE,"+
					"FOREIGN KEY(" + Constants.EXDETAIL_SUPPLIES_ID + ") REFERENCES " + Constants.SUPPLIES_TABLE + "(" +Constants.SUPPLIES_ID + ") ON UPDATE CASCADE,"+
					"PRIMARY KEY("+Constants.EXDETAIL_SUPPLIES_ID+","+Constants.EXDETAIL_EXPORT_ID+")"+
					")";

}
