package com.example.bda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView type, name, Email, IDnumber, phonenumber, bloodGroupsSpinner;
    private CircleImageView profilepic;
    private Button backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        //button to take user back to main activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        type = findViewById(R.id.type);
        name = findViewById(R.id.enterfullname);
        Email = findViewById(R.id.addEmail);
        IDnumber = findViewById(R.id.IDnumber);
        phonenumber = findViewById(R.id.phonenumber);
        bloodGroupsSpinner = findViewById(R.id.bloodGroupsSpinner);
        profilepic = findViewById(R.id.profile_pic);
        backbtn = findViewById(R.id.backbtn);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    type.setText(snapshot.child("type").getValue().toString());
                    name.setText(snapshot.child("name").getValue().toString());
                    IDnumber.setText(snapshot.child("idnumber").getValue().toString());
                    phonenumber.setText(snapshot.child("phonenumber").getValue().toString());
                    bloodGroupsSpinner.setText(snapshot.child("bloodgroups").getValue().toString());
                    Email.setText(snapshot.child("email").getValue().toString());

                   // Glide.with(getApplicationContext()).load(snapshot.child("profilepicurl").
                    //        getValue().toString()).into(profilepic);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //directs the user back to the main activity

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
        return super.onOptionsItemSelected(item);
    }
}
}