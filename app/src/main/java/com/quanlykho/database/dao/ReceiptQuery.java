package com.quanlykho.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.quanlykho.constant.Constants;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.SQLiteDatabaseHelper;
import com.quanlykho.model.Receipt;
import com.quanlykho.util.App;

import java.util.ArrayList;
import java.util.List;

public class ReceiptQuery implements DAO.ReceiptQuery{

	private final SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getInstance();

	public void createReceipt(Receipt receipt, QueryResponse<Boolean> response){
		SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(Constants.RECEIPT_WAREHOUSE_ID, receipt.getReceiptWarehouseId());
		contentValues.put(Constants.RECEIPT_DATE, receipt.getReceiptDate());

		try{
			long rowCount = sqLiteDatabase.insertOrThrow(Constants.RECEIPT_TABLE, null,contentValues);

			if (rowCount > 0){
				response.onSuccess(true);
				String info =  "Xác nhận phiếu nhập kho lúc: " + receipt.getReceiptDate();
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
	public void readReceipt(int receiptId, QueryResponse<Receipt> response) {

	}

	@Override
	public void readAllReceiptFromWarehouse(int WarehouseId, QueryResponse<List<Receipt>> response){
		SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
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

	public void getRowCount(QueryResponse<Integer> response){
		String countQuery = "SELECT  * FROM " + Constants.RECEIPT_TABLE;
		SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		if (count > 0)
			response.onSuccess(count);
		else
			response.onFailure("rowcount = -1");
	}

	
}
