package com.quanlykho.database;

import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quanlykho.constant.CreateTable;
import com.quanlykho.constant.DropTable;
import com.quanlykho.database.dao.DAO;
import com.quanlykho.database.dao.WarehouseQuery;
import com.quanlykho.model.Warehouse;
import com.quanlykho.util.App;

import java.util.Map;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "WarehouseDB";
	private static final int DATABASE_VERSION = 3;
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
	public void onCreate(android.database.sqlite.SQLiteDatabase db) {
		db.execSQL(CreateTable.CREATE_WAREHOUSE_TABLE);
		db.execSQL(CreateTable.CREATE_SUPPLIES_TABLE);
		db.execSQL(CreateTable.CREATE_RECEIPT_TABLE);
		db.execSQL(CreateTable.CREATE_EXPORT_TABLE);
		db.execSQL(CreateTable.CREATE_DETAIL_TABLE);
		db.execSQL(CreateTable.CREATE_EXDETAIL_TABLE);
		Log.d("DEBUG-MSG", "CREATE DATABASE SUCCESSFULLY");
	}

	@Override
	public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DropTable.DROP_DETAIL_TABLE);
		db.execSQL(DropTable.DROP_EXDETAIL_TABLE);
		db.execSQL(DropTable.DROP_RECEIPT_TABLE);
		db.execSQL(DropTable.DROP_EXPORT_TABLE);
		db.execSQL(DropTable.DROP_SUPPLIES_TABLE);
		db.execSQL(DropTable.DROP_WAREHOUSE_TABLE);
		onCreate(db);
		Log.d("DEBUG-MSG", "DROP DATABASE SUCCESSFULLY");
	}

}
