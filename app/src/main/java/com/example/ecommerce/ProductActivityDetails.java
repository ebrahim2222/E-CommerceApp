package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Model.Products;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.paperdb.Paper;

public class ProductActivityDetails extends AppCompatActivity {

    private Products products;
    TextView proName,proDesc,proPrice;
    ImageView proImage;
    ElegantNumberButton numberButton;
    Button addToCart;
    private String number;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference cartRef,proRef,userRef;
    String calcPrice="";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        firebaseDatabase = FirebaseDatabase.getInstance();
        cartRef = firebaseDatabase.getReference().child("Cart Products");
        proRef = firebaseDatabase.getReference().child("All Products");
        userRef = firebaseDatabase.getReference().child("Users");
       // adminCartRef = firebaseDatabase.getReference().child("Admin Cart Product");
        proName = findViewById(R.id.product_act_name);
        proImage = findViewById(R.id.product_act_image);
        proDesc = findViewById(R.id.product_act_desc);
        proPrice = findViewById(R.id.product_act_price);
        numberButton = findViewById(R.id.elegantNumberButton);
        number = numberButton.getNumber();
        addToCart = findViewById(R.id.product_act_btn);
        progressDialog = new ProgressDialog(ProductActivityDetails.this);
        Paper.init(this);

        Intent intent = getIntent();
        if(intent.hasExtra("products"))
        {
            products = intent.getExtras().getParcelable("products");
        }
        loadData();


        numberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = numberButton.getNumber();
                int i1 = Integer.parseInt(number);
                int i = Integer.parseInt(products.getpPrice());
                calcPrice = String.valueOf(i*i1);
                proPrice.setText(calcPrice);
            }
        });



        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProducts();
            }
        });

    }

    private void loadData() {
        String detailKey = (String) Paper.book().read("detailKey");
        proRef.child(detailKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Products value = snapshot.getValue(Products.class);
                Picasso.get().load(value.getpImage()).placeholder(R.drawable.profile).into(proImage);
                proName.setText(value.getpName());
                proDesc.setText(value.getpDesc());
                proPrice.setText(value.getpPrice());
                calcPrice = value.getpPrice();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addProducts() {

        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String key = Paper.book().read("key");
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = dateFormat.format(new Date());
        String time = timeFormat.format(new Date());


        DatabaseReference push = cartRef.child(key).push();
        String pushKey = push.getKey();
       // Products pro = new Products(key,products.getpName(),products.getpDesc(),calcPrice,time,date,products.getpImage(),products.getCategory(),pushKey,number);
        Cart cart = new Cart(products.getpName(),products.getpDesc(),calcPrice,products.getpTime(),products.getpDate(),number,products.getpImage(),key,pushKey,products.getCategory());

        userRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("name"))
                {
                    push.setValue(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(ProductActivityDetails.this, "add successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ProductActivityDetails.this,CartActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProductActivityDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                {
                    progressDialog.dismiss();
                    Toast.makeText(ProductActivityDetails.this, "first go to setting and update profile", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductActivityDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}