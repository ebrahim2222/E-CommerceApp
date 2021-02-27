package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.Model.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import io.paperdb.Paper;

public class SettingsActivity extends AppCompatActivity {
    ImageView profilePic;
    EditText userName,userPhone,userAddress;
    Button saveUser;
    int permissionRequestCode = 1;
    Uri selectedImage,myUri;
    String checker = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersRef;
    FirebaseStorage firebaseStorage;
    StorageReference profileRef;
    private String savedPhone,savedEmail,savedPassword,savedKey;
    private String name,phone,address;
    String downloadedImage;
    Users users;
    private static final String TAG = "SettingsActivity";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profilePic = findViewById(R.id.setting_profile_image);
        userName = findViewById(R.id.setting_user_name);
        userPhone = findViewById(R.id.setting_user_phone);
        userAddress = findViewById(R.id.setting_user_address);
        saveUser = findViewById(R.id.setting_save_user);
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference().child("Users");
        firebaseStorage = FirebaseStorage.getInstance();
        profileRef = firebaseStorage.getReference().child("User_Profile");
        Paper.init(SettingsActivity.this);
        progressDialog = new ProgressDialog(this);

        savedPhone =(String) Paper.book().read("phone");
        savedEmail = (String) Paper.book().read("email");
        savedPassword = (String) Paper.book().read("password") ;
        savedKey = (String) Paper.book().read("key");
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(SettingsActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
                    {

                    }else {
                        ActivityCompat.requestPermissions(SettingsActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},permissionRequestCode);
                    }
                }else {
                    openGallery();
                }
            }
        });

        displayUserInfo();

        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 name = userName.getText().toString();
                 phone = userPhone.getText().toString();
                 address = userAddress.getText().toString();
                if(checker.equals("checked"))
                {
                    addUserInfo(selectedImage,name,phone,address);

                }else
                {
                    updateInfo(name,phone,address);
                }

            }
        });

        Log.d(TAG, "hema onCreate: "+savedPhone);
    }

    private void displayUserInfo() {

        usersRef.child(savedKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("image").exists())
                {
                    String phone1 = snapshot.child("phone").getValue().toString();
                    downloadedImage = snapshot.child("image").getValue().toString();
                    String address1 = snapshot.child("address").getValue().toString();
                    String name1 = snapshot.child("name").getValue().toString();

                    Picasso.get().load(downloadedImage).into(profilePic);
                    userName.setText(name1);
                    userAddress.setText(address1);
                    userPhone.setText(phone1);
                }else {
                    userPhone.setText(snapshot.child("phone").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SettingsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateInfo(String name, String phone, String address) {
        if(name.isEmpty())
        {
            Toast.makeText(this, "enter your name", Toast.LENGTH_SHORT).show();
        }else if(phone.isEmpty())
        {
            Toast.makeText(this, "enter your phone", Toast.LENGTH_SHORT).show();
        }else if(address.isEmpty())
        {
            Toast.makeText(this, "enter your address", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setTitle("please wait");
            progressDialog.setMessage("Loading");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            users = new Users(savedEmail, savedPassword, phone, downloadedImage, address, name,savedKey);
            usersRef.child(savedKey).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                    progressDialog.dismiss();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }

    }

    private void addUserInfo(Uri uri, String name, String phone, String address) {

       if(name.isEmpty())
       {
           Toast.makeText(this, "enter your name", Toast.LENGTH_SHORT).show();
       }else if(phone.isEmpty())
       {
           Toast.makeText(this, "enter your phone", Toast.LENGTH_SHORT).show();
       }else if(address.isEmpty())
       {
           Toast.makeText(this, "enter your address", Toast.LENGTH_SHORT).show();
       }else
       {
           progressDialog.setTitle("please wait");
           progressDialog.setMessage("Loading");
           progressDialog.setCanceledOnTouchOutside(false);
           progressDialog.show();
           profileRef.child(savedKey).child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           myUri = uri;
                           users = new Users(savedEmail,savedPassword,phone,uri.toString(),address,name,savedKey);
                           usersRef.child(savedKey).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SettingsActivity.this, "success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                                    progressDialog.dismiss();
                                }
                            });
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                           progressDialog.dismiss();
                       }
                   });

               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   progressDialog.dismiss();
               }
           });
       }
    }

    private void openGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                selectedImage = result.getUri();
                checker = "checked";
                profilePic.setImageURI(selectedImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                checker="";
            }
        }

    }
}