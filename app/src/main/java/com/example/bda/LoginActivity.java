package com.example.bda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextView backbtn;

    private TextInputEditText LoginEmail, loginPassword;
    private TextView forgotPassword;
    private Button Loginbutton;


    private ProgressDialog loader;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user !=null){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        };

        backbtn = findViewById(R.id.backbtn);
        LoginEmail = findViewById(R.id.LoginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        forgotPassword = findViewById(R.id.forgotPassword);
        Loginbutton = findViewById(R.id.Loginbutton);

        loader = new ProgressDialog(this);



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegistrationPart.class);
                startActivity(intent);
            }
        });

        Loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //extract the entered email and password
                final String email = LoginEmail.getText().toString().trim();
                final String password = loginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    LoginEmail.setError("Email is required");
                }
                if (TextUtils.isEmpty(password)){
                    loginPassword.setError("Password is required");
                }
                //when account requirements are met

                else {
                    loader.setMessage("Login in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()){
                             Toast.makeText(LoginActivity.this, "Log in successful", Toast.LENGTH_SHORT);
                             Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                             startActivity(intent);
                             finish();
                         }
                         else {
                             Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT);

                         }

                         loader.dismiss();
                        }
                    });

                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}