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
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.Model.Products;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewProductActivity extends AppCompatActivity {
    ImageView productImage;
    EditText productName,productDesc,productPrice;
    Button addProduct;
    int galleryrequestCode = 0 ;
    int permissionRequestCode = 1 ;
    private Uri uri;
    FirebaseStorage firebaseStorage;
    StorageReference productImageRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference productRef,allProductsRef;
    private Intent intent;
    private String categoryName;
    private String pName,pDesc,pPrice,date,time,productTimeAndDate;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        progressDialog = new ProgressDialog(AddNewProductActivity.this);
        productImage = findViewById(R.id.add_product_image);
        productName = findViewById(R.id.add_product_name);
        productDesc = findViewById(R.id.add_product_desc);
        productPrice = findViewById(R.id.add_product_price);
        addProduct = findViewById(R.id.add_product_btn);
        firebaseStorage = FirebaseStorage.getInstance();
        productImageRef = firebaseStorage.getReference().child("ProductsImages");
        firebaseDatabase = FirebaseDatabase.getInstance();
        productRef = firebaseDatabase.getReference().child("Products Category");
        allProductsRef = firebaseDatabase.getReference().child("All Products");

        intent = getIntent();
        getDataFromPreviousActivity();

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(AddNewProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(AddNewProductActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
                    {

                    }else {
                        ActivityCompat.requestPermissions(AddNewProductActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},permissionRequestCode);
                    }
                }else {
                    openGallery();
                }
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveProduct();

            }
        });
    }

    private void getDataFromPreviousActivity() {
        if(intent.hasExtra("category"))
        {
             categoryName = intent.getExtras().getString("category");
        }
    }




    private void saveProduct() {
        pName = productName.getText().toString();
        pDesc = productDesc.getText().toString();
        pPrice = productPrice.getText().toString();
        if(uri == null)
        {
            Toast.makeText(this, "please choose product image", Toast.LENGTH_SHORT).show();
        }else if(pName.isEmpty())
        {
            Toast.makeText(this, "please enter product name", Toast.LENGTH_SHORT).show();
        }else if(pDesc.isEmpty())
        {
            Toast.makeText(this, "please enter product desc", Toast.LENGTH_SHORT).show();
        }else if(pPrice.isEmpty())
        {
            Toast.makeText(this, "please enter product price", Toast.LENGTH_SHORT).show();
        }else
        {
            progressDialog.setTitle("Add Product");
            progressDialog.setMessage("Please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            storeImage();
        }
    }

    private void storeImage() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
         date = dateFormat.format(new Date());
         time = timeFormat.format(new Date());
         productTimeAndDate = date+" "+ time;
        productImageRef.child(categoryName).child(productTimeAndDate).child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        addProductToFireBase(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNewProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void addProductToFireBase(String uri) {

        DatabaseReference reference = productRef.child(categoryName).push();
        String key = reference.getKey();
        Products products = new Products(productTimeAndDate,pName,pDesc,pPrice,time,date,uri,categoryName,key);
        reference.setValue(products).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                allProductsRef.child(key).setValue(products).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(AddNewProductActivity.this, "added successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddNewProductActivity.this,AdminsPanelActivity.class));
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNewProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

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
                uri = result.getUri();
                 productImage.setImageURI(uri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                 Exception error = result.getError();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    openGallery();
                }
        }
    }
}