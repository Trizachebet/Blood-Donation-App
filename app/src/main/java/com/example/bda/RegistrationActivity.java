package com.example.bda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    private TextView backbtn;

    private CircleImageView profile_pic;

    private TextInputEditText enterIDnumber, phonenumber, addEmail, addpassword, enterfullname;

    private Spinner bloodgroups;

    private Button regbutton;

    private Uri resultUri;

    private ProgressDialog loader;

    private FirebaseAuth mAuth;//used for adding authentication for our users

    //for database reference to be able to save the data that user types to the database
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        profile_pic = findViewById(R.id.profile_pic);
        enterIDnumber = findViewById(R.id.enterIDnumber);
        phonenumber = findViewById(R.id.phonenumber);
        addEmail = findViewById(R.id.addEmail);
        addpassword = findViewById(R.id.addpassword);
        enterfullname = findViewById(R.id.enterfullname);
        bloodgroups = findViewById(R.id.bloodGroupsSpinner);
        regbutton = findViewById(R.id.regbutton);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,  1);
            }
        });

        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = addEmail.getText().toString().trim();
               // trim used to remove any white spaces between the text that the user types
               final String password = addpassword.getText().toString().trim();
               final String fullname = enterfullname.getText().toString().trim();
                final String IDnumber = enterIDnumber.getText().toString().trim();
                final String phonenumb = phonenumber.getText().toString().trim();
                final String bloodGroupsSpinner = bloodgroups.getSelectedItem().toString();

                if (TextUtils.isEmpty(email)){
                    addEmail.setError("Enter Email to proceed");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    addpassword.setError("Enter password to proceed");
                    return;
                }

                if (TextUtils.isEmpty(fullname)){
                    enterfullname.setError("Enter fullname to proceed");
                    return;
                }

                if (TextUtils.isEmpty(IDnumber)){
                    enterIDnumber.setError("Enter IDnumber to proceed");
                    return;
                }

                if (TextUtils.isEmpty(phonenumb)){
                    phonenumber.setError("Enter your phonenumber to proceed");
                    return;
                }

                if (bloodGroupsSpinner.equals("Select your blood group")){
                    Toast.makeText(RegistrationActivity.this, "Select your blood group",Toast.LENGTH_SHORT).show();
                    return;
                }
                //now register users and link their data to the database
                else {

                    loader.setMessage("Registering you...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    //create user with Email and password(creating registration)

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(RegistrationActivity.this,"Error" + error, Toast.LENGTH_SHORT).show();

                            }
                            else {
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currentUserId);

                                //Data structure that uses key value pairs to store data
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserId);
                                userInfo.put("name", fullname);
                                userInfo.put("email", email);
                                userInfo.put("idnumber", IDnumber);
                                userInfo.put("phonenumber", phonenumb);
                                userInfo.put("bloodgroups", bloodGroupsSpinner);
                                userInfo.put("type", "donor");
                                userInfo.put("search", "donor"+bloodGroupsSpinner);//you cant add two separate queries on a single statement so you concat

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RegistrationActivity.this, "Data set Successfully",Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(RegistrationActivity.this, task.getException().toString(),Toast.LENGTH_SHORT).show();

                                        }

                                        finish();
                                       // loader.dismiss();

                                    }
                                });
                                //add profile pic to the database
                                if(resultUri !=null){
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile pics").child(currentUserId);
                                    Bitmap bitmap = null;

                                    try{
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);

                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data = byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask = filePath.putBytes(data);

                                    //incase photo fails
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegistrationActivity.this, "Failed to upload image",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            if (taskSnapshot.getMetadata() !=null && taskSnapshot.getMetadata().getReference() !=null){
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profilepictureurl", imageUrl);

                                                        userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(RegistrationActivity.this, "Image url added to database successfully",Toast.LENGTH_SHORT).show();

                                                                }
                                                                else {
                                                                    Toast.makeText(RegistrationActivity.this, task.getException().toString(),Toast.LENGTH_SHORT).show();

                                                                }

                                                            }
                                                        });

                                                        finish();

                                                    }
                                                });
                                            }

                                        }
                                    });

                                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();
                                }

                            }

                        }
                    });

                    

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == RESULT_OK && data != null ){
            resultUri  = data.getData();
            profile_pic.setImageURI(resultUri);

        }


    }
}