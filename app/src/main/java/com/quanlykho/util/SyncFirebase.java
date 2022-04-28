package com.quanlykho.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quanlykho.model.Warehouse;

public class SyncFirebase {
	private static FirebaseDatabase firebaseDatabase;
	private static DatabaseReference mDatabase= FirebaseDatabase.getInstance("https://warehouse-management-pro-c5dd5-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
	private static String UId=User.getUId();
	public SyncFirebase() {
	}

	public static void getData(){
		DatabaseReference warehouseRef = mDatabase.child(UId+"/Warehouse");
		warehouseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DataSnapshot> task) {
				if (task.isSuccessful()) {
					Log.d("firebase", String.valueOf(task.getResult().getValue()));
				}
			}
		});
	}


}
