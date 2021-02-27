package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.example.ecommerce.Adapters.ProductDetailsAdapter;
import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Model.Orders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class UserProductsDetailsActivity extends AppCompatActivity {
    Intent intent;
    RecyclerView productDetailsRv;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ordersRef;
    private String key,admin,proKey;
    private Orders orders;
    ProductDetailsAdapter adapter;
    List<Cart> cartList;
    private static final String TAG = "UserProductsDetailsActi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_products_details);
        intent = getIntent();
        orders = intent.getExtras().getParcelable("orders");
        firebaseDatabase = FirebaseDatabase.getInstance();
        ordersRef = firebaseDatabase.getReference().child("Admin Cart Product");
        key = Paper.book().read("key").toString();
        proKey = Paper.book().read("proKey").toString();
        Log.d(TAG, "hema onCreate: "+proKey);
        admin = Paper.book().read("admin");
        setUpRecycler();
    }

    private void setUpRecycler() {
        productDetailsRv = findViewById(R.id.user_oro_details_rv);
        adapter = new ProductDetailsAdapter();
        productDetailsRv.setLayoutManager(new LinearLayoutManager(UserProductsDetailsActivity.this));
        productDetailsRv.setAdapter(adapter);
        loadDetails();

    }

    private void loadDetails() {
        cartList = new ArrayList<>();
        ordersRef.child(proKey).child(orders.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for(DataSnapshot ds : children)
                {
                    Cart cart = ds.getValue(Cart.class);
                    Log.d(TAG, "hema onDataChange: "+cart.getpName());
                    cartList.add(cart);
                }
                adapter.setData(UserProductsDetailsActivity.this,cartList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProductsDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}