package com.quanlykho.feature.userinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.quanlykho.R;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {

	private static final int GALLERY_INTENT_CODE = 1023 ;
	TextView fullName,email,phone,verifyMsg;
	FirebaseAuth fAuth;
	FirebaseFirestore fStore;
	String userId;
	TextView resendCode;
	Button resetPassLocal,changeProfile;
	FirebaseUser user;
	ImageView image;
	CircleImageView profileImage;
	StorageReference storageReference;

	@Override
	protected void onResume() {
		super.onResume();
		if(!user.isEmailVerified()){
			verifyMsg.setVisibility(View.VISIBLE);
			resendCode.setVisibility(View.VISIBLE);
			fullName.setVisibility(View.GONE);
			email.setVisibility(View.GONE);
			phone.setVisibility(View.GONE);
			changeProfile.setVisibility(View.GONE);
			resetPassLocal.setVisibility(View.GONE);

			resendCode.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {

					user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
						@Override
						public void onSuccess(Void aVoid) {
							Toast.makeText(v.getContext(), R.string.Verification_Email_Has_been_Sent, Toast.LENGTH_SHORT).show();
						}
					}).addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							Log.d("tag", "onFailure: Email not sent " + e.getMessage());
						}
					});
				}
			});
		}else{
			verifyMsg.setVisibility(View.GONE);
			resendCode.setVisibility(View.GONE);
			fullName.setVisibility(View.VISIBLE);
			email.setVisibility(View.VISIBLE);
			phone.setVisibility(View.VISIBLE);
			changeProfile.setVisibility(View.VISIBLE);
			resetPassLocal.setVisibility(View.VISIBLE);
		}
		DocumentReference documentReference = fStore.collection("users").document(userId);
		documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
			@Override
			public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
				if(documentSnapshot.exists()){
					phone.setText(documentSnapshot.getString("phone"));
					fullName.setText(documentSnapshot.getString("name"));
					email.setText(documentSnapshot.getString("email"));
				}else {
					Log.d("tag", "onEvent: Document do not exists");
				}
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		setControl();
		StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/avatar.png");
		profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
			@Override
			public void onSuccess(Uri uri) {
				Picasso.get().load(uri).into(profileImage);
				Picasso.get().load(uri).into(image);
			}
		});

		resendCode = findViewById(R.id.resendCode);
		verifyMsg = findViewById(R.id.verifyMsg);

		userId = fAuth.getCurrentUser().getUid();
		user = fAuth.getCurrentUser();





		resetPassLocal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				final EditText resetPassword = new EditText(v.getContext());

				final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
				passwordResetDialog.setTitle(R.string.Reset_Password);
				passwordResetDialog.setMessage(R.string.Enter_New_Password_more_6_Characters_long);
				passwordResetDialog.setView(resetPassword);

				passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// extract the email and send reset link
						String newPassword = resetPassword.getText().toString();
						user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
							@Override
							public void onSuccess(Void aVoid) {
								Toast.makeText(UserInfoActivity.this, R.string.Password_Reset_Successfully, Toast.LENGTH_SHORT).show();
							}
						}).addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								Toast.makeText(UserInfoActivity.this, R.string.Password_Reset_Failed, Toast.LENGTH_SHORT).show();
							}
						});
					}
				});

				passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// close
					}
				});
				passwordResetDialog.create().show();
			}
		});

		changeProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// open gallery
				Intent intent = new Intent(v.getContext(),EditProfileActivity.class);
				intent.putExtra("fullName",fullName.getText().toString());
				intent.putExtra("email",email.getText().toString());
				intent.putExtra("phone",phone.getText().toString());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				UserInfoActivity.this.finish();
			}
		});
	}

	private void setControl(){
		phone = findViewById(R.id.profilePhone);
		fullName = findViewById(R.id.profileName);
		email    = findViewById(R.id.profileEmail);
		resetPassLocal = findViewById(R.id.resetPasswordButton);

		image= findViewById(R.id.image);
		profileImage = findViewById(R.id.avatarImage);
		changeProfile = findViewById(R.id.changeProfileButton);


		fAuth = FirebaseAuth.getInstance();
		fStore = FirebaseFirestore.getInstance();
		storageReference = FirebaseStorage.getInstance().getReference();
	}

}