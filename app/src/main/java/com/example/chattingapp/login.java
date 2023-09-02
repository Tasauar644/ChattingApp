package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    Button button;
    EditText email,password;
    String emailPattern="^[A-Za-z0-9+_. -]+@(. +)$";
    FirebaseAuth auth;

   // @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       auth= FirebaseAuth.getInstance();
       button=findViewById(R.id.logButton);
       email=findViewById(R.id.editTextLogEmailAddress);
       password=findViewById(R.id.editTextLogPassword);


       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String Email=email.getText().toString();
               String Password=password.getText().toString();

               if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){

                   Toast.makeText(login.this,"Enter your email and password",Toast.LENGTH_SHORT).show();

               }
               else  if (!Email.matches(emailPattern)){
                   Toast.makeText(login.this,"Give proper email",Toast.LENGTH_SHORT).show();
               }

               else {
                   auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               try {
                                   Intent intent=new Intent(login.this,MainActivity.class);
                                   startActivity(intent);
                                   finish();;

                               }
                               catch (Exception e){
                                   Toast.makeText(login.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                               }
                           }
                           else {
                               Toast.makeText(login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                           }

                       }
                   });
               } 
           }
       });

    }
}