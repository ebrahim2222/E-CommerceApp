package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LogInActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersRef;
    EditText loginEmail,loginPassword;
    Button loginButton;
    ProgressDialog progressDialog;
    CheckBox loginCbx;
    FirebaseUser currentUser;
    TextView loginAdmin,loginUser;
    String adminUserCheck;
    private static final String TAG = "LogInActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        adminUserCheck = "Users";
        mAuth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_btn);
        progressDialog = new ProgressDialog(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference();
        loginAdmin = findViewById(R.id.loginAdmin);
        loginUser = findViewById(R.id.login_user);
        Paper.init(LogInActivity.this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                if(email.isEmpty())
                {
                    Toast.makeText(LogInActivity.this, "enter email", Toast.LENGTH_SHORT).show();

                }else if(password.isEmpty())
                {
                    Toast.makeText(LogInActivity.this, "enter password", Toast.LENGTH_SHORT).show();

                }else
                {
                    signIn(email,password);
                }
            }
        });
        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminUserCheck = "Users";
                loginAdmin.setVisibility(View.VISIBLE);
                loginUser.setVisibility(View.INVISIBLE);
                loginButton.setText("Login User");
            }
        });

        loginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminUserCheck = "Admin";
                loginUser.setVisibility(View.VISIBLE);
                loginAdmin.setVisibility(View.INVISIBLE);
                loginButton.setText("Login Admin");
            }
        });
    }

    private void signIn(String email, String password) {
        usersRef.child(adminUserCheck).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for(DataSnapshot ds : children)
                {
                    Users users = ds.getValue(Users.class);
                    if(users.getEmail().equals(email) && users.getPassword().equals(password))
                    {
                        if(adminUserCheck.equals("Admin"))
                        {
                            startActivity(new Intent(LogInActivity.this,AdminsPanelActivity.class));
                            Paper.book().write("admin","admin");
                            finish();
                        }else if (adminUserCheck.equals("Users"))
                        {
                            Paper.book().write("key",users.getKey());
                            Paper.book().write("phone",users.getPhone());
                            Paper.book().write("email",users.getEmail());
                            Paper.book().write("password",users.getPassword());
                            startActivity(new Intent(LogInActivity.this,HomeActivity.class));
                            finish();
                        }
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(LogInActivity.this, "invalid email", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LogInActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}