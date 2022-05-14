package com.quanlykho.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quanlykho.constant.Constants;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.SQLiteDatabaseHelper;
import com.quanlykho.model.Detail;
import com.quanlykho.model.Receipt;
import com.quanlykho.util.App;
import com.quanlykho.util.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailQuery implements DAO.DetailQuery{

	private final SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getInstance();

	@Override
	public void createDetail(Detail detail, QueryResponse<Boolean> response) {
		try (android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
			String query = "INSERT INTO Detail (Detail_SuppliesId,Detail_ReceiptId,DetailAmount)\n" +
					"VALUES(" + detail.getDetailSuppliesId() + "," + detail.getDetailReceiptId() + "," + detail.getDetailAmount() + ");";
			sqLiteDatabase.execSQL(query);
			response.onSuccess(true);

		} catch (SQLiteException e) {
			Log.d("TESTDETAIL","INSERT: FAIL CMNR "+e);
			response.onFailure(e.getMessage());
		}
	}
//	@Override
//	public void createListDetail(ArrayList<Detail> details, QueryResponse<Boolean> response) {
//		for(Detail detail: details) {
//			String query = "INSERT INTO Detail (Detail_SuppliesId,Detail_ReceiptId,DetailAmount)\n" +
//					"VALUES(" + detail.getDetailSuppliesId() + "," + detail.getDetailReceiptId() + "," + detail.getDetailAmount() + ");";
//			try (android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
//				sqLiteDatabase.execSQL(query);
//				response.onSuccess(true);
//			} catch (SQLiteException e) {
//				response.onFailure(e.getMessage());
//			}
//		}
//	}
	@Override
	public void readDetail(int detail_suppliesId, int detail_receiptId, QueryResponse<Detail> response) {
	}

	@Override
	public void deleteDetailByReceiptId(int receiptId, QueryResponse<Boolean> response) {
		try (android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
			String query = "DELETE FROM Detail WHERE Detail.Detail_ReceiptId= " + receiptId + ";";
			sqLiteDatabase.execSQL(query);
			response.onSuccess(true);
		}catch (SQLiteException e){
			response.onFailure("Không thể xóa");
		}
	}


	@Override
	public void readAllDetailFromReceipt(int receiptId, QueryResponse<List<Detail>> response) {
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

		String QUERY = "SELECT * FROM "
				+Constants.DETAIL_TABLE + " WHERE "
				+Constants.DETAIL_RECEIPT_ID + " = "
				+receiptId;
		Cursor cursor = null;
		try{
			List<Detail> details= new ArrayList<>();
			cursor = sqLiteDatabase.rawQuery(QUERY,null);


			if(cursor.moveToFirst()) {
				do {
					Detail detail = new Detail();
					detail.setDetailReceiptId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.DETAIL_RECEIPT_ID)));;
					detail.setDetailSuppliesId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.DETAIL_SUPPLIES_ID)));
					detail.setDetailAmount(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.DETAIL_AMOUNT)));
					Log.d("TESTH",detail.toString());
					details.add(detail);
				} while (cursor.moveToNext());

				response.onSuccess(details);
			}
		}catch (Exception e){
			response.onFailure(e.getMessage());
		}finally {
			sqLiteDatabase.close();
			if(cursor != null)
				cursor.close();
		}
	}
}
