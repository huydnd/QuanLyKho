package com.quanlykho.feature.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.quanlykho.R;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class ForgetPasswordActivity extends AppCompatActivity {


    private CircularProgressButton resetbutton;
    private EditText email;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        resetbutton=findViewById(R.id.cirResetButton);
        email=findViewById(R.id.editTextEmail);
        progressBar=findViewById(R.id.progressBar);

        auth=FirebaseAuth.getInstance();

        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getemail=email.getText().toString().trim();

                if(TextUtils.isEmpty(getemail)){
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(getemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(ForgetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);

                    }
                });
            }
        });
    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

}