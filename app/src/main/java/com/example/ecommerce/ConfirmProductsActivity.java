package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Model.Orders;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.paperdb.Paper;

public class ConfirmProductsActivity extends AppCompatActivity {
    TextView totalPriceText;
    EditText confirmName,confirmPhone,confirmAddress,confirmCity;
    Button confirmBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference cartRef,ordersRef,adminCartRef,allOrdersRef;
    int totalPrice=0,totalQuantity=0;
    private String key,ordersName="",ordersDesc="";
    private Cart value;
    ProgressDialog progressDialog;
    StringBuilder stringBuilder;
    List<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_products);
        totalPriceText = findViewById(R.id.confirm_total_price);
        firebaseDatabase = FirebaseDatabase.getInstance();
        cartRef = firebaseDatabase.getReference().child("Cart Products");
        ordersRef = firebaseDatabase.getReference().child("Orders");
        allOrdersRef = firebaseDatabase.getReference().child("All Orders");
        adminCartRef = firebaseDatabase.getReference().child("Admin Cart Product");
        confirmName = findViewById(R.id.confirm_name);
        confirmPhone = findViewById(R.id.confirm_phone);
        confirmAddress = findViewById(R.id.confirm_address);
        confirmCity = findViewById(R.id.confirm_city);
        confirmBtn = findViewById(R.id.confirm_btn);
        stringBuilder = new StringBuilder();
        progressDialog = new ProgressDialog(ConfirmProductsActivity.this);
        Paper.init(ConfirmProductsActivity.this);
        key = Paper.book().read("key");

        loadData();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmData();
            }
        });
    }

    private void confirmData() {
        String name = confirmName.getText().toString();
        String phone = confirmPhone.getText().toString();
        String address = confirmAddress.getText().toString();
        String city = confirmCity.getText().toString();

        if(name.isEmpty())
        {
            Toast.makeText(this, "please enter your name", Toast.LENGTH_SHORT).show();
        }else if(phone.isEmpty())
        {
            Toast.makeText(this, "please enter your phone", Toast.LENGTH_SHORT).show();
        }else if(address.isEmpty())
        {
            Toast.makeText(this, "please enter your address", Toast.LENGTH_SHORT).show();
        }else if(city.isEmpty())
        {
            Toast.makeText(this, "please enter your city", Toast.LENGTH_SHORT).show();
        }else
        {
            addDataToFirebase(name,phone,address,city);
        }
    }

    private void addDataToFirebase(String name, String phone, String address, String city) {
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = dateFormat.format(new Date());
        String time  = timeFormat.format(new Date());
        DatabaseReference push = ordersRef.child(key).push();
        String pushKey = push.getKey();
        Orders orders = new Orders(name,phone,address,city,date,time,totalPrice+"",totalQuantity+"",pushKey,ordersName,ordersDesc,"not shipped",key);
        push.setValue(orders).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                allOrdersRef.child(pushKey).setValue(orders).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adminCartRef.child(key).child(pushKey).setValue(cartList).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                cartRef.child(ConfirmProductsActivity.this.key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ConfirmProductsActivity.this, "confirmed Successfully", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        startActivity(new Intent(ConfirmProductsActivity.this,OrdersActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ConfirmProductsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ConfirmProductsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData() {
        cartList = new ArrayList<>();
        cartRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for(DataSnapshot ds : children)
                {
                    value = ds.getValue(Cart.class);
                    cartList.add(value);
                    String price = ds.child("pPrice").getValue().toString();
                    String quantity = ds.child("quantity").getValue().toString();
                    String pName = ds.child("pName").getValue().toString();
                    String pDesc = ds.child("pDesc").getValue().toString();
                    int i = Integer.parseInt(price);
                    int i1 = Integer.parseInt(quantity);
                    totalPrice+=i;
                    totalQuantity+=i1;

                   stringBuilder.append(ordersName);
                   stringBuilder.append(",");
                    String[] split = stringBuilder.toString().split(",");
                    if(split.length == 0)
                   {
                       ordersName = pName;
                   }else
                   {
                       ordersName = ordersName+" and "+pName;
                   }
                }
                totalPriceText.setText("Total Price : "+ totalPrice +"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConfirmProductsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}