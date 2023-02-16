package com.example.bda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegistrationPart extends AppCompatActivity {

    private Button donorbtn, recipientButton;
    private  TextView backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_part);

        donorbtn = findViewById(R.id.donorbtn);
        recipientButton = findViewById(R.id.recipientButton);
        backbtn = findViewById(R.id.backbtn);

        donorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationPart.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });



        recipientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationPart.this,RecipientRegistrationActivity

                        .class);
                startActivity(intent);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationPart.this,LoginActivity.class);
            }
        });


    }
}