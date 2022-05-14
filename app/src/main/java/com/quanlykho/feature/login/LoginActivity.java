package com.quanlykho.feature.login;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


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
import com.zeugmasolutions.localehelper.LocaleHelper;
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegate;
import com.zeugmasolutions.localehelper.LocaleHelperActivityDelegateImpl;
import com.zeugmasolutions.localehelper.Locales;

import java.util.Locale;
import java.util.Objects;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

	CircularProgressButton mLogin;
	private EditText mEmail, mPassword;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

	private ToggleButton lgToggleButton;
	LocaleHelperActivityDelegate localeHelperActivityDelegate= new LocaleHelperActivityDelegateImpl();

	@NonNull
	@Override
	public AppCompatDelegate getDelegate() {
		return localeHelperActivityDelegate.getAppCompatDelegate(super.getDelegate());
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(localeHelperActivityDelegate.attachBaseContext(newBase));
	}

	@Override
	protected void onResume() {
		super.onResume();
		localeHelperActivityDelegate.onResumed(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		localeHelperActivityDelegate.onPaused();
	}

	@Override
	public Context createConfigurationContext(Configuration overrideConfiguration) {
		Context context= super.createConfigurationContext(overrideConfiguration);
		return LocaleHelper.INSTANCE.onAttach(context);
	}

	@Override
	public Context getApplicationContext() {
		return localeHelperActivityDelegate.getApplicationContext(super.getApplicationContext());
	}

	public void updateLocale(Locale locale){
		localeHelperActivityDelegate.setLocale(this,locale);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//for changing status bar icon colors
		localeHelperActivityDelegate.onCreate(this);



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
		lgToggleButton = findViewById(R.id.lgToggleButton);
		lgToggleButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(lgToggleButton.isChecked()){
					updateLocale(Locales.INSTANCE.getVietnamese());
					onResume();
				}else{
					updateLocale(Locales.INSTANCE.getEnglish());
					onResume();
				}
			}
		});

		mLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mLogin.startAnimation();
				final String email = mEmail.getText().toString();
				final String password = mPassword.getText().toString();
				if(TextUtils.isEmpty(email)){
					mEmail.setError("Email is required!");
					mLogin.revertAnimation();
					return;
				}
				if(TextUtils.isEmpty(password)){
					mPassword.setError("Password is required!");
					mLogin.revertAnimation();
					return;
				}
				if(password.length()<6){
					mPassword.setError("Password must be >= 6 characters");
					mLogin.revertAnimation();
					return;
				}
				mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if(!task.isSuccessful()){
							mLogin.revertAnimation();
							Toast.makeText(LoginActivity.this, "Sign in error!\n"+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
						}else{
							mLogin.revertAnimation();

//							SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getInstance();
//							databaseHelper.onUpgrade(databaseHelper.getWritableDatabase(),3,3);
//							SyncFirebase.getData();
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

	public void onResetPasswordClick(View View){
		startActivity(new Intent(this,ForgetPasswordActivity.class));
		overridePendingTransition(R.anim.slide_in_left,R.anim.stay);
	}

}