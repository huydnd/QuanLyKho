package com.quanlykho.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.quanlykho.constant.CreateTable;
import com.quanlykho.constant.DropTable;
import com.quanlykho.util.App;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "QuanLyKhoDB";
	private static final int DATABASE_VERSION = 1;
	private static SQLiteDatabaseHelper databaseHelper;

	private SQLiteDatabaseHelper() {
		super(App.context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static SQLiteDatabaseHelper getInstance() {

		if (databaseHelper == null) {
			synchronized (SQLiteDatabaseHelper.class){ //thread safe singleton
				if (databaseHelper == null)
					databaseHelper = new SQLiteDatabaseHelper();
			}
		}
		return databaseHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CreateTable.CREATE_WAREHOUSE_TABLE);
		db.execSQL(CreateTable.CREATE_SUPPLIES_TABLE);
		db.execSQL(CreateTable.CREATE_RECEIPT_TABLE);
		db.execSQL(CreateTable.CREATE_DETAIL_TABLE);
		Log.d("DEBUG-MSG", "CREATE DATABASE SUCCESSFULLY");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DropTable.DROP_DETAIL_TABLE);
		db.execSQL(DropTable.DROP_RECEIPT_TABLE);
		db.execSQL(DropTable.DROP_SUPPLIES_TABLE);
		db.execSQL(DropTable.DROP_WAREHOUSE_TABLE);
		onCreate(db);
		Log.d("DEBUG-MSG", "DROP DATABASE SUCCESSFULLY");
	}

}
