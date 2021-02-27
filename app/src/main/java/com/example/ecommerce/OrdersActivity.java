package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.ecommerce.Adapters.OrdersAdapter;
import com.example.ecommerce.Model.Orders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class OrdersActivity extends AppCompatActivity {
    RecyclerView orederRv;
    OrdersAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ordersRef;
    List<Orders> ordersList;
    private String key;
    private static final String TAG = "OrdersActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        firebaseDatabase = FirebaseDatabase.getInstance();
        ordersRef = firebaseDatabase.getReference().child("Orders");
        Paper.init(OrdersActivity.this);
        key = Paper.book().read("key").toString();
        setUpRecycler();
    }

    private void setUpRecycler() {
        orederRv = findViewById(R.id.orders_act_rv);
        orederRv.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));
        adapter = new OrdersAdapter();
        orederRv.setAdapter(adapter);
        setData();
    }

    private void setData() {
        ordersList = new ArrayList<>();
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(key))
                {
                    ordersRef.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ordersList.clear();
                            Iterable<DataSnapshot> children = snapshot.getChildren();
                            for(DataSnapshot ds : children)
                            {
                                Orders value = ds.getValue(Orders.class);
                                Log.d(TAG, "hema onDataChange: "+value.getUserKey());
                                ordersList.add(value);
                            }
                            adapter.setData(OrdersActivity.this,ordersList);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(OrdersActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}