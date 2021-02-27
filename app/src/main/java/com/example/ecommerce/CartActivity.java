package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ecommerce.Adapters.CartAdapter;
import com.example.ecommerce.Model.Cart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CartAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference cartRef;
    Button cartConfirm;
    List<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Paper.init(CartActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        cartConfirm = findViewById(R.id.cart_confirm);
        cartRef = firebaseDatabase.getReference().child("Cart Products");

        setUpRecycler();
    }

    private void setUpRecycler() {
        adapter = new CartAdapter();
        recyclerView = findViewById(R.id.cart_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        recyclerView.setAdapter(adapter);

        setData();

        cartConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,ConfirmProductsActivity.class));
            }
        });
    }

    private void setData() {
        cartList = new ArrayList<>();
        String key = Paper.book().read("key");
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                if(snapshot.hasChild(key))
                {
                    cartRef.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Iterable<DataSnapshot> children = snapshot.getChildren();
                            for(DataSnapshot ds : children)
                            {
                                Cart value = ds.getValue(Cart.class);
                                cartList.add(value);
                            }
                            adapter.setData(CartActivity.this,cartList);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else
                {
                    cartConfirm.setVisibility(View.INVISIBLE);
                    cartConfirm.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}