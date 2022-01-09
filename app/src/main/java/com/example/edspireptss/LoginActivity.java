package com.example.edspireptss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button newAcc;
    EditText etEmail, etPass;
    Button btnLogin;
    TextView tvCreateAcc, tvRecover;
    private ProgressDialog loadingBar;

    FirebaseAuth mAuth;
    FirebaseUser user;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvCreateAcc=findViewById(R.id.tv_CreateAcc);
        tvCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mAuth= FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        btnLogin=findViewById(R.id.btnLogin);
        etEmail=findViewById(R.id.etLoginEmail);
        etPass=findViewById(R.id.etLoginPassword);
        tvRecover=findViewById(R.id.tvRecover);
        loadingBar=new ProgressDialog(this);


        tvRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Fusnm;
                Fusnm=etEmail.getText().toString();
                sendForgotPassLink(Fusnm);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Lusnm,Lpass;
                Lusnm=etEmail.getText().toString();
                Lpass=etPass.getText().toString();
                Login(Lusnm,Lpass);
            }
        });
    }

    public void Login(String Lusnm, final String Lpass)
    {
        loadingBar.setMessage("Please wait, while we load your application.");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        if(TextUtils.isEmpty(Lusnm))
        {
            etEmail.setError("Please enter Email.");
            etEmail.requestFocus();
            loadingBar.dismiss();
            return;
        }
        else if(TextUtils.isEmpty(Lpass)){
            etPass.setError("Please enter Password.");
            etPass.requestFocus();
            loadingBar.dismiss();
            return;
        }
        mAuth.signInWithEmailAndPassword(Lusnm,Lpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        loadingBar.dismiss();
                        Intent intent = new Intent(LoginActivity.this, com.example.edspireptss.Home.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Verify email....." + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    return;
                }
            }
        });
    }
    public void sendForgotPassLink(String emailId){
        if(!TextUtils.isEmpty(emailId)){
            mAuth.sendPasswordResetEmail(emailId).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Reset link sent to"+etEmail.getText().toString(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            etEmail.setError("Please enter Email.");
            etEmail.requestFocus();
        }
    }

}