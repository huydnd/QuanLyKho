package com.quanlykho.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quanlykho.constant.Constants;
import com.quanlykho.database.QueryResponse;
import com.quanlykho.database.SQLiteDatabaseHelper;
import com.quanlykho.model.Supplies;
import com.quanlykho.util.App;
import com.quanlykho.util.User;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuppliesQuery implements DAO.SuppliesQuery{
	private final SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getInstance();

//	private DatabaseReference mDatabase= FirebaseDatabase.getInstance("https://warehouse-management-pro-c5dd5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

	private ContentValues getContentValuesForsupplies(Supplies supplies){
		ContentValues contentValues = new ContentValues();

		contentValues.put(Constants.SUPPLIES_NAME, supplies.getSuppliesName());
		contentValues.put(Constants.SUPPLIES_UNIT, supplies.getSuppliesUnit());
		contentValues.put(Constants.SUPPLIES_IMAGE, supplies.getSuppliesImage());

		return contentValues;
	}
	@Override
	public void createSupplies(Context context,Supplies supplies, QueryResponse<Boolean> response){
		try (android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
			ContentValues contentValues = getContentValuesForsupplies(supplies);
			long id = sqLiteDatabase.insertOrThrow(Constants.SUPPLIES_TABLE, null, contentValues);
			if (id > 0) {
				response.onSuccess(true);
				supplies.setSuppliesId((int) id);
//				DatabaseReference slRef= mDatabase.child(User.getUId()+"/Supplies/"+String.valueOf(supplies.getSuppliesId()));
//				Map<String,String> slData= new HashMap<String,String>();
//				slData.put("SuppliesName",supplies.getSuppliesName());
//				slData.put("SuppliesUnit",supplies.getSuppliesUnit());
//				slData.put("SuppliesImage", Base64.getEncoder().encodeToString(supplies.getSuppliesImage()));
//				slRef.setValue(slData);
			} else
				response.onFailure("Không tạo được Vật tư");
		} catch (SQLiteException e) {
			response.onFailure(e.getMessage());
		}
	}

	@Override
	public void readSupplies(int suppliesId, QueryResponse<Supplies> response){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		Cursor cursor = null;

		try {
			cursor = sqLiteDatabase.query(Constants.SUPPLIES_TABLE, null,
					Constants.SUPPLIES_ID + " =? ", new String[]{String.valueOf(suppliesId)},
					null, null, null);

			if(cursor!=null && cursor.moveToFirst()) {
				Supplies supplies = getSuppliesFromCursor(cursor);
				response.onSuccess(supplies);
			}
			else
				response.onFailure("Không tìm thấy");

		} catch (Exception e){
			response.onFailure(e.getMessage());
		} finally {
			sqLiteDatabase.close();
			if(cursor!=null)
				cursor.close();
		}
	}

	@Override
	public void readAllSupplies(QueryResponse<List<Supplies>> response){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
		List<Supplies> suppliesList = new ArrayList<>();

		Cursor cursor = null;
		try{
			cursor = sqLiteDatabase.query(Constants.SUPPLIES_TABLE, null,null,null,null,null,null);
			if(cursor!=null && cursor.moveToFirst()){
				do {
					Supplies supplies = getSuppliesFromCursor(cursor);
					suppliesList.add(supplies);
				} while (cursor.moveToNext());

				response.onSuccess(suppliesList);
			} else
				response.onFailure("Chưa có vật tư nào");
		}catch (Exception e){
			response.onFailure(e.getMessage());
		} finally {
			sqLiteDatabase.close();
			if(cursor!=null)
				cursor.close();
		}
	}

	@Override
	public void updateSupplies(Supplies supplies, QueryResponse<Boolean> response){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

		String id = String.valueOf(supplies.getSuppliesId());
		ContentValues contentValues = getContentValuesForsupplies(supplies);
		int row = sqLiteDatabase.update(Constants.SUPPLIES_TABLE,
				contentValues,
				Constants.SUPPLIES_ID + " = ?",
				new String[]{id});
		if(row>0){
			response.onSuccess(true);
			Toast.makeText(App.context, "Xác nhận thay đổi thông tin vật tư", Toast.LENGTH_LONG).show();
//			DatabaseReference slRef= mDatabase.child(User.getUId()+"/Supplies/"+String.valueOf(supplies.getSuppliesId()));
//			Map<String,String> slData= new HashMap<String,String>();
//			slData.put("SuppliesName",supplies.getSuppliesName());
//			slData.put("SuppliesUnit",supplies.getSuppliesUnit());
//			slData.put("SuppliesImage",Base64.getEncoder().encodeToString(supplies.getSuppliesImage()));
//			slRef.setValue(slData);
		}
		else{
			response.onFailure("Không thể thay đổi thông tin vật tư");
		}
	}

	@Override
	public void deleteSupplies(int suppliesID, QueryResponse<Boolean> response){
		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
		int row = sqLiteDatabase.delete(Constants.SUPPLIES_TABLE, Constants.SUPPLIES_ID+" = ?",new String[]{String.valueOf(suppliesID)});

		if(row>0){
			response.onSuccess(true);
			Toast.makeText(App.context, "Đã xóa vật tư", Toast.LENGTH_LONG).show();
//			mDatabase.child(User.getUId()+"/Supplies/"+String.valueOf(suppliesID)).removeValue();
		}
		else{
			response.onFailure("Không thể xóa vật tư này");
		}
	}
//	@Override
//	public void deleteSuppliesByName(String suppliesName, QueryResponse<Boolean> response){
//		android.database.sqlite.SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
//		int row = sqLiteDatabase.delete(Constants.SUPPLIES_TABLE, Constants.SUPPLIES_NAME+" = ?",new String[]{String.valueOf(suppliesName)});
//
//		if(row>0){
//			response.onSuccess(true);
//			Toast.makeText(App.context, "Đã xóa vật tư", Toast.LENGTH_LONG).show();
//		}
//		else{
//			response.onFailure("Không thể xóa vật tư này");
//		}
//	}



	private Supplies getSuppliesFromCursor(Cursor cursor){
		int id = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.SUPPLIES_ID));
		String name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.SUPPLIES_NAME));
		String unit = cursor.getString(cursor.getColumnIndexOrThrow(Constants.SUPPLIES_UNIT));
		byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(Constants.SUPPLIES_IMAGE));
		return new Supplies(id, name, unit, image);
	}
}
