package com.quanlykho.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quanlykho.constant.Constants;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.SQLiteDatabaseHelper;
import com.quanlykho.model.Supplies;
import com.quanlykho.model.SuppliesByDate;
import com.quanlykho.model.SuppliesDetail;
import com.quanlykho.model.Warehouse;
import com.quanlykho.util.App;
import com.quanlykho.util.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseQuery implements DAO.WarehouseQuery {
	private final SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getInstance();

	private DatabaseReference mDatabase= FirebaseDatabase.getInstance("https://warehouse-management-pro-c5dd5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

	private ContentValues getContentValuesForWarehouse(Warehouse warehouse) {

		ContentValues contentValues = new ContentValues();

		contentValues.put(Constants.WAREHOUSE_NAME, warehouse.getWarehouseName());
		contentValues.put(Constants.WAREHOUSE_ADDRESS, warehouse.getWarehouseAddress());

		return contentValues;
	}
	@Override
	public void createWarehouse(Context context,Warehouse warehouse, QueryResponse<Boolean> response) {
		try(SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
			ContentValues contentValues = getContentValuesForWarehouse(warehouse);
			long id = sqLiteDatabase.insertOrThrow(Constants.WAREHOUSE_TABLE, null, contentValues);
			if (id > 0) {
				response.onSuccess(true);
				warehouse.setWarehouseId((int) id);
				Toast.makeText(context, "Tạo kho thành công. Mã kho: "+warehouse.getWarehouseId(), Toast.LENGTH_LONG).show();
				DatabaseReference whRef= mDatabase.child(User.getUId()+"/Warehouse/"+String.valueOf(warehouse.getWarehouseId()));
				Map<String,String> whData= new HashMap<String,String>();
				whData.put("WarehouseName",warehouse.getWarehouseName());
				whData.put("WarehouseAddress",warehouse.getWarehouseAddress());
				whRef.setValue(whData);
			} else
				response.onFailure("Không thể tạo được nhà kho mới. Vui lòng kiểm tra lại thông tin");
		} catch (SQLiteException e) {
			response.onFailure(e.getMessage());
		}
	}

	@Override
	public void readWarehouse(int WarehouseID, QueryResponse<Warehouse> response) {
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = sqLiteDatabase.query(Constants.WAREHOUSE_TABLE, null,
					Constants.WAREHOUSE_ID + " =? ", new String[]{String.valueOf(WarehouseID)},
					null, null, null,null);
			if(cursor!=null && cursor.moveToFirst()) {
				Warehouse warehouse = getWarehouseFromCursor(cursor);
				response.onSuccess(warehouse);
			}
			else
				response.onFailure("Không tìm thấy nhà kho này trong database");

		} catch (Exception e){
			response.onFailure(e.getMessage());
		} finally {
			sqLiteDatabase.close();
			if(cursor!=null)
				cursor.close();
		}
	}

	@Override
	public void readWarehouseByName(String WarehouseName, QueryResponse<Warehouse> response) {
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = sqLiteDatabase.query(Constants.WAREHOUSE_TABLE, null,
					Constants.WAREHOUSE_NAME + " =? ", new String[]{String.valueOf(WarehouseName)},
					null, null, null,null);
			if(cursor!=null && cursor.moveToFirst()) {
				Warehouse warehouse = getWarehouseFromCursor(cursor);
				response.onSuccess(warehouse);
			}
			else
				response.onFailure("Không tìm thấy nhà kho này trong database");

		} catch (Exception e){
			response.onFailure(e.getMessage());
		} finally {
			sqLiteDatabase.close();
			if(cursor!=null)
				cursor.close();
		}
	}

	@Override
	public void readAllWarehouse(QueryResponse<List<Warehouse>> response) {
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		List<Warehouse> warehousesList = new ArrayList<>();

		Cursor cursor = null;
		try{
			cursor = sqLiteDatabase.query(Constants.WAREHOUSE_TABLE, null,null,null,null,null,null);
			if(cursor!=null && cursor.moveToFirst()){
				do {
					Warehouse warehouse = getWarehouseFromCursor(cursor);
					warehousesList.add(warehouse);
				} while (cursor.moveToNext());

				response.onSuccess(warehousesList);
			} else
				response.onFailure("Không tồn tại kho trong cơ sở dữ liệu");
		}catch (Exception e){
			response.onFailure(e.getMessage());
		} finally {
			sqLiteDatabase.close();
			if(cursor!=null)
				cursor.close();
		}
	}

	@Override
	public void readAllDetailByWarehouseName(String warehouseName, QueryResponse<List<SuppliesDetail>> response ){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		List<SuppliesDetail> suppliesDetailList = new ArrayList<>();

		Cursor cursor = null;
		try{
			cursor = sqLiteDatabase.rawQuery("SELECT Supplies.SuppliesName , sum(Detail.DetailAmount) as DetailAmount, Supplies.SuppliesUnit\n" +
					"From Detail JOIN Supplies ON Detail.Detail_SuppliesId = Supplies.SuppliesId\n" +
					"JOIN Receipt ON Detail.Detail_ReceiptId = Receipt.ReceiptId\n" +
					"JOIN Warehouse ON Receipt.Receipt_WarehouseId = Warehouse.WarehouseId \n" +
					"AND Warehouse.WarehouseName= ? \n" +
					"GROUP BY Supplies.SuppliesName",new String[]{warehouseName});
			if(cursor!=null && cursor.moveToFirst()){
				do {
					SuppliesDetail suppliesDetail = getSuppliesDetailInWarehouseFromCursor(cursor);
					suppliesDetailList.add(suppliesDetail);
				} while (cursor.moveToNext());
				Log.d("HELLO", "So luong deetail="+suppliesDetailList.size());
				response.onSuccess(suppliesDetailList);
			} else
				Log.d("HELLO", "So luong deetail="+suppliesDetailList.size());
				response.onFailure("Không tồn tại vật tư trong kho");
		}catch (Exception e){
			response.onFailure(e.getMessage());
		} finally {
			sqLiteDatabase.close();
			if(cursor!=null)
				cursor.close();
		}
	}

	@Override
	public void readAllExDetailByWarehouseName(String warehouseName, QueryResponse<List<SuppliesDetail>> response ){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		List<SuppliesDetail> suppliesDetailList = new ArrayList<>();

		Cursor cursor = null;
		try{
			cursor = sqLiteDatabase.rawQuery("SELECT Supplies.SuppliesName , sum(ExDetail.ExDetailAmount) as DetailAmount, Supplies.SuppliesUnit\n" +
					"From ExDetail JOIN Supplies ON ExDetail.ExDetail_SuppliesId = Supplies.SuppliesId\n" +
					"JOIN Export ON ExDetail.ExDetail_ExportId = Export.ExportId\n" +
					"JOIN Warehouse ON Export.Export_WarehouseId = Warehouse.WarehouseId \n" +
					"AND Warehouse.WarehouseName= ? \n" +
					"GROUP BY Supplies.SuppliesName",new String[]{warehouseName});
			if(cursor!=null && cursor.moveToFirst()){
				do {
					SuppliesDetail suppliesDetail = getSuppliesDetailInWarehouseFromCursor(cursor);
					suppliesDetailList.add(suppliesDetail);
				} while (cursor.moveToNext());
				Log.d("HELLO", "So luong deetail="+suppliesDetailList.size());
				response.onSuccess(suppliesDetailList);
			} else
				Log.d("HELLO", "So luong deetail="+suppliesDetailList.size());
			response.onFailure("Không tồn tại vật tư trong kho");
		}catch (Exception e){
			response.onFailure(e.getMessage());
		} finally {
			sqLiteDatabase.close();
			if(cursor!=null)
				cursor.close();
		}
	}

	@Override
	public void readAllSuppliesWithReceiptDateByWarehouseName(String warehouseName, String suppliesName, QueryResponse<List<SuppliesByDate>> response ){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		List<SuppliesByDate> suppliesByDateArrayList = new ArrayList<SuppliesByDate>();

		Cursor cursor = null;
		try{
			cursor = sqLiteDatabase.rawQuery("SELECT Receipt.ReceiptDate, sum(Detail.DetailAmount) as DetailAmount, Supplies.SuppliesName\n" +
					"From Detail JOIN Supplies ON Detail.Detail_SuppliesId = Supplies.SuppliesId\n" +
					"AND Supplies.SuppliesName= ? \n" +
					"JOIN Receipt ON Detail.Detail_ReceiptId = Receipt.ReceiptId\n" +
					"JOIN Warehouse ON Receipt.Receipt_WarehouseId = Warehouse.WarehouseId \n" +
					"AND Warehouse.WarehouseName= ? \n" +
					"GROUP BY Receipt.ReceiptDate",new String[]{suppliesName,warehouseName});
			if(cursor!=null && cursor.moveToFirst()){
				do {
					SuppliesByDate suppliesByDate = getSuppliesByDateInWarehouseFromCursor(cursor);
					suppliesByDateArrayList.add(suppliesByDate);
				} while (cursor.moveToNext());
				Log.d("HELLO", "So luong detail="+suppliesByDateArrayList.size());
				response.onSuccess(suppliesByDateArrayList);
			} else
				Log.d("HELLO", "So luong detail="+suppliesByDateArrayList.size());
			response.onFailure("Không tồn tại vật tư trong kho");
		}catch (Exception e){
			response.onFailure(e.getMessage());
		} finally {
			sqLiteDatabase.close();
			if(cursor!=null)
				cursor.close();
		}
	}

	@Override
	public void anyWarehouseCreated(QueryResponse<Boolean> response){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		boolean empty = true;
		Cursor cursor = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM "+Constants.WAREHOUSE_TABLE, null);
		if (cursor != null && cursor.moveToFirst()) {
			empty = (cursor.getInt (0) == 0);
		}
		cursor.close();

		if(empty){
			response.onSuccess(empty);
		}else{
			response.onFailure("Chưa có nhà kho nào được tạo");
		}
	}

	@Override
	public void updateWarehouse(Warehouse warehouse, QueryResponse<Boolean> response) {
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
		String id = String.valueOf(warehouse.getWarehouseId());
		ContentValues contentValues = getContentValuesForWarehouse(warehouse);
		int row = sqLiteDatabase.update(Constants.WAREHOUSE_TABLE,
				contentValues,
				Constants.WAREHOUSE_ID + " = ?",
				new String[]{id});
		if(row>0){
			response.onSuccess(true);
			Toast.makeText(App.context, "Xác nhận thay đổi thông tin nhà kho", Toast.LENGTH_LONG).show();
			DatabaseReference whRef= mDatabase.child(User.getUId()+"/Warehouse/"+String.valueOf(warehouse.getWarehouseId()));

			Map<String, String> whData = new HashMap<String, String>();
			whData.put("WarehouseName",warehouse.getWarehouseName());
			whData.put("WarehouseAddress",warehouse.getWarehouseAddress());
			whRef.setValue(whData);

		}
		else{
			response.onFailure("Không thể thay đổi thông tin nhà kho");
		}
	}

	@Override
	public void deleteWarehouse(int WarehouseID, QueryResponse<Boolean> response) {
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
		int row = sqLiteDatabase.delete(Constants.WAREHOUSE_TABLE, Constants.WAREHOUSE_ID+" = ?",new String[]{String.valueOf(WarehouseID)});
		DAO.ReceiptQuery receiptQuery=new ReceiptQuery();
		if(row>0){
			response.onSuccess(true);
			Toast.makeText(App.context, "Đã xóa kho", Toast.LENGTH_LONG).show();

			mDatabase.child(User.getUId()+"/Warehouse/"+String.valueOf(WarehouseID)).removeValue();
		}
		else{
			response.onFailure("Không thể xóa kho này");
		}
	}

//	@Override
//	public void deleteWarehouseByName(String WarehouseName, QueryResponse<Boolean> response) {
//		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
//		int row = sqLiteDatabase.delete(Constants.WAREHOUSE_TABLE, Constants.WAREHOUSE_NAME+" = ?",new String[]{String.valueOf(WarehouseName)});
//		if(row>0){
//			Log.d("TestId",WarehouseName+" row="+ row);
//
//
//			response.onSuccess(true);
//			Toast.makeText(App.context, "Đã xóa kho", Toast.LENGTH_LONG).show();
//		}
//		else{
//			response.onFailure("Không thể xóa kho này");
//		}
//	}

	private Warehouse getWarehouseFromCursor(Cursor cursor){
		int id = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.WAREHOUSE_ID));
		String name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.WAREHOUSE_NAME));
		String address = cursor.getString(cursor.getColumnIndexOrThrow(Constants.WAREHOUSE_ADDRESS));
		return new Warehouse(id, name, address);
	}

	private SuppliesDetail getSuppliesDetailInWarehouseFromCursor(Cursor cursor){
		String suppliesName = cursor.getString(cursor.getColumnIndexOrThrow(Constants.SUPPLIES_NAME));
		int detailAmount = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.DETAIL_AMOUNT));
		String suppliesUnit = cursor.getString(cursor.getColumnIndexOrThrow(Constants.SUPPLIES_UNIT));
		return new SuppliesDetail(suppliesName, detailAmount, suppliesUnit);
	}
	private SuppliesByDate getSuppliesByDateInWarehouseFromCursor(Cursor cursor){
		String suppliesName = cursor.getString(cursor.getColumnIndexOrThrow(Constants.SUPPLIES_NAME));
		int detailAmount = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.DETAIL_AMOUNT));
		String date = cursor.getString(cursor.getColumnIndexOrThrow(Constants.RECEIPT_DATE));
		return new SuppliesByDate(suppliesName, detailAmount, date);
	}

}
