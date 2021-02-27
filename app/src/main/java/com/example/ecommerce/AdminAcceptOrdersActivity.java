package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.ecommerce.Adapters.AcceptOrderAdapter;
import com.example.ecommerce.Model.Orders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminAcceptOrdersActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference allOrdersRef;
    RecyclerView acceptOrderRv;
    AcceptOrderAdapter adapter;
    List<Orders> ordersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_accept_orders);

        firebaseDatabase = FirebaseDatabase.getInstance();
        allOrdersRef = firebaseDatabase.getReference().child("All Orders");

        setUpRecycler();
    }

    private void setUpRecycler() {
        acceptOrderRv = findViewById(R.id.accept_orders_rv);
        adapter = new AcceptOrderAdapter();
        acceptOrderRv.setLayoutManager(new LinearLayoutManager(AdminAcceptOrdersActivity.this));
        acceptOrderRv.setAdapter(adapter);
        setData();
    }

    private void setData() {
        ordersList = new ArrayList<>();
        allOrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for(DataSnapshot ds : children)
                {
                    Orders value = ds.getValue(Orders.class);
                    ordersList.add(value);
                }
                adapter.setData(AdminAcceptOrdersActivity.this,ordersList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminAcceptOrdersActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}