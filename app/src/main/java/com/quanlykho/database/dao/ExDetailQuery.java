package com.quanlykho.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.quanlykho.constant.Constants;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.SQLiteDatabaseHelper;
import com.quanlykho.model.Detail;
import com.quanlykho.model.ExDetail;

import java.util.ArrayList;
import java.util.List;

public class ExDetailQuery implements DAO.ExDetailQuery {

	private final SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getInstance();

	@Override
	public void createExDetail(ExDetail exDetail, QueryResponse<Boolean> response) {
		try (android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
			String query = "INSERT INTO ExDetail (ExDetail_SuppliesId,ExDetail_ExportId,ExDetailAmount)\n" +
					"VALUES(" + exDetail.getExDetailSuppliesId() + "," + exDetail.getExDetailExportId() + "," + exDetail.getExDetailAmount() + ");";
			sqLiteDatabase.execSQL(query);
			response.onSuccess(true);

		} catch (SQLiteException e) {
			Log.d("TESTDETAIL","INSERT: FAIL CMNR "+e);
			response.onFailure(e.getMessage());
		}
	}

	@Override
	public void readExDetail(int exDetail_suppliesId, int exDetail_exportId, QueryResponse<ExDetail> response) {
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

		String QUERY = "SELECT * FROM "
				+ Constants.EXDETAIL_TABLE + " WHERE "
				+Constants.EXDETAIL_EXPORT_ID + " = "
				+exDetail_exportId+" AND "
				+Constants.EXDETAIL_SUPPLIES_ID+ " = "
				+exDetail_suppliesId;
		Cursor cursor = null;
		try{
			cursor = sqLiteDatabase.rawQuery(QUERY,null);
			ExDetail exDetail = new ExDetail();

			if(cursor.moveToFirst()) {
				do {
					exDetail.setExDetailExportId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.EXDETAIL_EXPORT_ID)));;
					exDetail.setExDetailSuppliesId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.EXDETAIL_SUPPLIES_ID)));
					exDetail.setExDetailAmount(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.EXDETAIL_AMOUNT)));
				} while (cursor.moveToNext());

				response.onSuccess(exDetail);
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
	public void readAllExDetailFromExport(int exportId, QueryResponse<List<ExDetail>> response) {
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

		String QUERY = "SELECT * FROM "
				+ Constants.EXDETAIL_TABLE + " WHERE "
				+Constants.EXDETAIL_EXPORT_ID + " = "
				+exportId;
		Cursor cursor = null;
		try{
			List<ExDetail> exDetails= new ArrayList<>();
			cursor = sqLiteDatabase.rawQuery(QUERY,null);


			if(cursor.moveToFirst()) {
				do {
					ExDetail exDetail = new ExDetail();
					exDetail.setExDetailExportId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.EXDETAIL_EXPORT_ID)));;
					exDetail.setExDetailSuppliesId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.EXDETAIL_SUPPLIES_ID)));
					exDetail.setExDetailAmount(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.EXDETAIL_AMOUNT)));
					exDetails.add(exDetail);
				} while (cursor.moveToNext());

				response.onSuccess(exDetails);
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
