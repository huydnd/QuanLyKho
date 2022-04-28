package com.quanlykho.feature.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quanlykho.R;
import com.quanlykho.database.SQLiteDatabaseHelper;
import com.quanlykho.feature.dashboard.DashboardActivity;
import com.quanlykho.util.SyncFirebase;
import com.quanlykho.util.User;
import com.sun.mail.imap.protocol.UID;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

	CircularProgressButton mLogin;
	private EditText mEmail, mPassword;

	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//for changing status bar icon colors
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference myRef = database.getReference("message");

		mAuth = FirebaseAuth.getInstance();
		firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
				if (user !=null){
					User.setUId(user.getUid());
					Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
					startActivity(intent);
					finish();
					return;
				}
			}
		};

		myRef.setValue("Hello, World!");
		if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		}
		setContentView(R.layout.activity_login);

		mLogin = (CircularProgressButton) findViewById(R.id.cirLoginButton);

		mEmail = (EditText) findViewById(R.id.editTextEmail);
		mPassword = (EditText) findViewById(R.id.editTextPassword);

		mLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final String email = mEmail.getText().toString();
				final String password = mPassword.getText().toString();
				mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if(!task.isSuccessful()){
							Toast.makeText(LoginActivity.this, "sign in error", Toast.LENGTH_SHORT).show();
						}else{
							SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getInstance();
							databaseHelper.onUpgrade(databaseHelper.getWritableDatabase(),3,3);
							SyncFirebase.getData();
						}
					}
				});
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		mAuth.addAuthStateListener(firebaseAuthStateListener);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mAuth.removeAuthStateListener(firebaseAuthStateListener);
	}

	public void onLoginClick(View View){
		startActivity(new Intent(this,RegisterActivity.class));
		overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

	}

}