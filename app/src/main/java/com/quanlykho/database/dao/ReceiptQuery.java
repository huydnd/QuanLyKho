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
import com.quanlykho.model.Receipt;
import com.quanlykho.util.App;
import com.quanlykho.util.User;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptQuery implements DAO.ReceiptQuery{

	private final SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getInstance();

	private DatabaseReference mDatabase= FirebaseDatabase.getInstance("https://warehouse-management-pro-c5dd5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

	public void createReceipt(Receipt receipt, QueryResponse<Receipt> response){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(Constants.RECEIPT_WAREHOUSE_ID, receipt.getReceiptWarehouseId());
		contentValues.put(Constants.RECEIPT_DATE, receipt.getReceiptDate());

		try{
			int rowCount = (int) sqLiteDatabase.insertOrThrow(Constants.RECEIPT_TABLE, null,contentValues);

			if (rowCount > 0){
				receipt.setReceiptId(rowCount);
				response.onSuccess(receipt);
				String info =  "Xác nhận phiếu nhập kho lúc: " + receipt.getReceiptDate();
				Toast.makeText(App.context, info, Toast.LENGTH_LONG).show();

				DatabaseReference rcRef= mDatabase.child(User.getUId()+"/Receipt/"+String.valueOf(receipt.getReceiptId()));
				Map<String,String> rcData= new HashMap<String,String>();
				rcData.put("Receipt_WarehouseId",String.valueOf(receipt.getReceiptWarehouseId()));
				rcData.put("ReceiptDate",receipt.getReceiptDate());
				rcRef.setValue(rcData);
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
	public void readReceipt(int receiptId, QueryResponse<Receipt> response) {

	}

	@Override
	public void readAllReceipt(QueryResponse<List<Receipt>> response){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		String QUERY = "SELECT * FROM "
				+Constants.RECEIPT_TABLE;
		Cursor cursor = null;
		try{
			List<Receipt> receipts= new ArrayList<>();
			cursor = sqLiteDatabase.rawQuery(QUERY,null);

			if(cursor.moveToFirst()) {
				do {
					Receipt receipt = new Receipt();

					receipt.setReceiptId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.RECEIPT_ID)));;
					receipt.setReceiptWarehouseId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.RECEIPT_WAREHOUSE_ID)));
					receipt.setReceiptDate(cursor.getString(cursor.getColumnIndexOrThrow(Constants.RECEIPT_DATE)));

					receipts.add(receipt);
				} while (cursor.moveToNext());

				response.onSuccess(receipts);
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
	public void readAllReceiptFromWarehouse(int WarehouseId, QueryResponse<List<Receipt>> response){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		//  select * from import_ticket_table where warehouse_id = 1
		String QUERY = "SELECT * FROM "
				+Constants.RECEIPT_TABLE + " WHERE "
				+Constants.RECEIPT_WAREHOUSE_ID + " = "
				+WarehouseId;
		Cursor cursor = null;
		try{
			List<Receipt> receipts= new ArrayList<>();
			cursor = sqLiteDatabase.rawQuery(QUERY,null);

			if(cursor.moveToFirst()) {
				do {
					Receipt receipt = new Receipt();

					receipt.setReceiptId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.RECEIPT_ID)));;
					receipt.setReceiptWarehouseId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.RECEIPT_WAREHOUSE_ID)));
					receipt.setReceiptDate(cursor.getString(cursor.getColumnIndexOrThrow(Constants.RECEIPT_DATE)));

					receipts.add(receipt);
				} while (cursor.moveToNext());

				response.onSuccess(receipts);
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
	public void deleteReceiptByWarehouseId(int warehouseID, QueryResponse<Boolean> response) {
		List<Receipt> datax=new ArrayList<Receipt>();
		readAllReceiptFromWarehouse(warehouseID, new QueryResponse<List<Receipt>>() {
			@Override
			public void onSuccess(List<Receipt> data) {
				datax.addAll(data);
			}
			@Override
			public void onFailure(String message) {
			}
		});
		try (android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
			String query = "DELETE FROM Receipt WHERE Receipt.Receipt_WarehouseId=" + warehouseID + ";";
			sqLiteDatabase.execSQL(query);
			response.onSuccess(true);
			for(Receipt receipt : datax){
				mDatabase.child(User.getUId()+"/Receipt/"+String.valueOf(receipt.getReceiptId())).removeValue();
			}
		}catch (SQLiteException e){
			response.onFailure("Không thể xóa");
		}
	}

	public void getRowCount(QueryResponse<Integer> response){
		String countQuery = "SELECT  * FROM " + Constants.RECEIPT_TABLE;
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
