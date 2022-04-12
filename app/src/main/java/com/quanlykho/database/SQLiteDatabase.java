package com.quanlykho.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.quanlykho.constant.CreateTable;
import com.quanlykho.constant.DropTable;
import com.quanlykho.util.App;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "WarehouseDB";
	private static final int DATABASE_VERSION = 1;
	private static SQLiteDatabaseHelper databaseHelper;

	public SQLiteDatabaseHelper(@Nullable Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
