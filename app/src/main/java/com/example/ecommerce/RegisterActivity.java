package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.Model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;
    EditText registerEmail,regiserPhone,registerPassword;
    Button registerButton;
    FirebaseUser currentUser;
    ProgressDialog progressDialog;
    private Users users;
    private DatabaseReference push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerEmail = findViewById(R.id.register_email);
        regiserPhone = findViewById(R.id.register_phone);
        registerPassword = findViewById(R.id.register_password);
        registerButton = findViewById(R.id.register_btn);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference().child("Users");
        progressDialog = new ProgressDialog(RegisterActivity.this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

    }

    private void createUser() {
        String email = registerEmail.getText().toString();
        String phone = regiserPhone.getText().toString();
        String password = registerPassword.getText().toString();

        if(email.isEmpty())
        {
            Toast.makeText(this, "please enter your email", Toast.LENGTH_SHORT).show();
        }else if(phone.isEmpty())
        {
            Toast.makeText(this, "please enter your phone", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty())
        {
            Toast.makeText(this, "please enter your password", Toast.LENGTH_SHORT).show();
        }else
            signUp(email,password,phone);
    }

    private void signUp(String email, String password,String phone) {
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        push = userRef.push();
        users = new Users(email,password,phone,push.getKey().toString());
        push.setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(RegisterActivity.this,LogInActivity.class));
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Created Successfuly", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}