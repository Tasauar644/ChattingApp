package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {

    EditText rg_Name,rg_Email,rg_Password,rg_Password_confirm;
    Button rg_signUp,rg_logIn;
    CircleImageView rg_profile_pic;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FirebaseDatabase database;
    FirebaseStorage storage;
    android.app.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        rg_signUp=findViewById(R.id.signUpButton);
        rg_logIn=findViewById(R.id.logInButton);

        rg_Name=findViewById(R.id.rgName);
        rg_Email=findViewById(R.id.rgEmail);
        rg_Password=findViewById(R.id.rgPassword);
        rg_Password_confirm=findViewById(R.id.rgPassword2);

        rg_profile_pic=findViewById(R.id.profileImage);

        database= FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        auth= FirebaseAuth.getInstance();

        rg_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(registration.this,login.class);
                startActivity(intent);
                finish();
            }
        });


        rg_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namee= rg_Name.getText().toString();
                String emaill=rg_Email.getText().toString();
                String passwordd=rg_Password.getText().toString();
                String cpassword=rg_Password_confirm.getText().toString();
                String status="Hey i am using this App";

                if (TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) || TextUtils.isEmpty(passwordd) || TextUtils.isEmpty(cpassword)){
                    Toast.makeText(registration.this, "Please Enter Valid information", Toast.LENGTH_SHORT).show();
                }
                else if (!emaill.matches(emailPattern)){
                    rg_Email.setError("Type a valid email.");
                }
                else if (!passwordd.equals(cpassword)) {
                    rg_Password.setError("Password Does Not Match");

                }
                else {
                    progressDialog.show();
                    auth.createUserWithEmailAndPassword(emaill,passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                String id= task.getResult().getUser().getUid();
                                DatabaseReference databaseReference=database.getReference().child("users").child(id);
                                StorageReference storageReference=storage.getReference().child("Upload").child(id);

                                if (imageURI!=null){
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri=uri.toString();
                                                        Users users=new Users(imageuri,namee,emaill,passwordd,id,status);
                                                        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()){
                                                                    Intent intent= new Intent(registration.this,login.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                                else {
                                                                    Toast.makeText(registration.this, "Error in creating user: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                                                }
                                                            }
                                                        });

                                                    }
                                                });
                                            }


                                        }
                                    });
                                }
                                else {
                                    String status="Hey i am using this App";
                                    imageuri="https://firebasestorage.googleapis.com/v0/b/chattingapp-8e9a5.appspot.com/o/man.png?alt=media&token=b8fb636f-6c3b-466d-9a91-576eb0171805";
                                    Users users=new Users(imageuri,namee,emaill,passwordd,id,status);
                                    databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                Intent intent= new Intent(registration.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else {
                                                Toast.makeText(registration.this, "Error in creating user: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });


                                }
                            }
                            else {
                                Toast.makeText(registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });



        rg_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==10){
            if (data!=null){
                imageURI=data.getData();
                rg_profile_pic.setImageURI(imageURI);

            }

        }
    }
}