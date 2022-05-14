package com.quanlykho.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quanlykho.constant.Constants;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.SQLiteDatabaseHelper;
import com.quanlykho.model.Export;
import com.quanlykho.model.Receipt;
import com.quanlykho.util.App;
import com.quanlykho.util.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportQuery implements DAO.ExportQuery{

	private final SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getInstance();


	public void createExport(Export export, QueryResponse<Export> response){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(Constants.EXPORT_WAREHOUSE_ID, export.getExportWarehouseId());
		contentValues.put(Constants.EXPORT_DATE, export.getExportDate());
		contentValues.put(Constants.EXPORT_ADDRESS, export.getExportAddress());
		try{
			int rowCount = (int) sqLiteDatabase.insertOrThrow(Constants.EXPORT_TABLE, null,contentValues);

			if (rowCount > 0){
				export.setExportId(rowCount);
				response.onSuccess(export);
				String info =  "Xác nhận phiếu nhập kho lúc: " + export.getExportDate();
				Toast.makeText(App.context, info, Toast.LENGTH_LONG).show();

//				DatabaseReference exRef= mDatabase.child(User.getUId()+"/Export/"+String.valueOf(export.getExportId()));
//				Map<String,String> exData= new HashMap<String,String>();
//				exData.put("Export_WarehouseId",String.valueOf(export.getExportWarehouseId()));
//				exData.put("ExportDate",export.getExportDate());
//				exRef.setValue(exData);
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
					export.setExportAddress(cursor.getString(cursor.getColumnIndexOrThrow(Constants.EXPORT_ADDRESS)));
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
					export.setExportAddress(cursor.getString(cursor.getColumnIndexOrThrow(Constants.EXPORT_ADDRESS)));
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
	public void deleteExportByWarehouseId(int warehouseID, QueryResponse<Boolean> response) {
		List<Export> datax=new ArrayList<Export>();
		readAllExportFromWarehouse(warehouseID, new QueryResponse<List<Export>>() {
			@Override
			public void onSuccess(List<Export> data) {
				datax.addAll(data);
			}
			@Override
			public void onFailure(String message) {
			}
		});
		try (android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
			String query = "DELETE FROM Export WHERE Export.Export_WarehouseId=" + warehouseID + ";";
			sqLiteDatabase.execSQL(query);
			response.onSuccess(true);
//			for(Export export : datax){
//				mDatabase.child(User.getUId()+"/Export/"+String.valueOf(export.getExportId())).removeValue();
//			}
		}catch (SQLiteException e){
			response.onFailure("Không thể xóa");
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
