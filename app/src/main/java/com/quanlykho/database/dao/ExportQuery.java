package com.quanlykho.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.quanlykho.constant.Constants;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.SQLiteDatabaseHelper;
import com.quanlykho.model.Export;
import com.quanlykho.model.Receipt;
import com.quanlykho.util.App;

import java.util.ArrayList;
import java.util.List;

public class ExportQuery implements DAO.ExportQuery{

	private final SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getInstance();

	public void createExport(Export export, QueryResponse<Export> response){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(Constants.EXPORT_WAREHOUSE_ID, export.getExportWarehouseId());
		contentValues.put(Constants.EXPORT_DATE, export.getExportDate());

		try{
			int rowCount = (int) sqLiteDatabase.insertOrThrow(Constants.EXPORT_TABLE, null,contentValues);

			if (rowCount > 0){
				export.setExportId(rowCount);
				response.onSuccess(export);
				String info =  "Xác nhận phiếu nhập kho lúc: " + export.getExportDate();
				Toast.makeText(App.context, info, Toast.LENGTH_LONG).show();
			}
			else
				response.onFailure("Không tạo được phiếu xuất kho!");

		}catch (SQLiteException e){
			response.onFailure(e.getMessage());
		}finally {
			sqLiteDatabase.close();
		}
	}

	@Override
	public void readExport(int exportId, QueryResponse<Export> response) {

	}

	@Override
	public void readAllExport(QueryResponse<List<Export>> response){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		String QUERY = "SELECT * FROM "
				+Constants.EXPORT_TABLE;
		Cursor cursor = null;
		try{
			List<Export> exports= new ArrayList<>();
			cursor = sqLiteDatabase.rawQuery(QUERY,null);

			if(cursor.moveToFirst()) {
				do {
					Export export= new Export();

					export.setExportId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.EXPORT_ID)));;
					export.setExportWarehouseId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.EXPORT_WAREHOUSE_ID)));
					export.setExportDate(cursor.getString(cursor.getColumnIndexOrThrow(Constants.EXPORT_DATE)));

					exports.add(export);
				} while (cursor.moveToNext());

				response.onSuccess(exports);
			}
		}catch (Exception e){
			response.onFailure(e.getMessage());
		}finally {
			sqLiteDatabase.close();
			if(cursor != null)
				cursor.close();
		}
	}

	@Override
	public void readAllExportFromWarehouse(int WarehouseId, QueryResponse<List<Export>> response){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		//  select * from import_ticket_table where warehouse_id = 1
		String QUERY = "SELECT * FROM "
				+Constants.EXPORT_TABLE + " WHERE "
				+Constants.EXPORT_WAREHOUSE_ID + " = "
				+WarehouseId;
		Cursor cursor = null;
		try{
			List<Export> exports= new ArrayList<>();
			cursor = sqLiteDatabase.rawQuery(QUERY,null);

			if(cursor.moveToFirst()) {
				do {
					Export export = new Export();

					export.setExportId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.EXPORT_ID)));;
					export.setExportWarehouseId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.EXPORT_WAREHOUSE_ID)));
					export.setExportDate(cursor.getString(cursor.getColumnIndexOrThrow(Constants.EXPORT_DATE)));

					exports.add(export);
				} while (cursor.moveToNext());

				response.onSuccess(exports);
			}
		}catch (Exception e){
			response.onFailure(e.getMessage());
		}finally {
			sqLiteDatabase.close();
			if(cursor != null)
				cursor.close();
		}
	}

	public void getRowCount(QueryResponse<Integer> response){
		String countQuery = "SELECT  * FROM " + Constants.EXPORT_TABLE;
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		if (count > 0)
			response.onSuccess(count);
		else
			response.onFailure("rowcount = -1");
	}


}
