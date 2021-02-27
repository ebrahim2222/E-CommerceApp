package com.example.ecommerce.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Adapters.ProductsAdapter;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView rv;
    ProductsAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference productsRef;
    List<Products> productsList;
    private static final String TAG = "HomeFragment";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rv = view.findViewById(R.id.home_rv);
        firebaseDatabase = FirebaseDatabase.getInstance();
        productsRef = firebaseDatabase.getReference();
        setUpRecycler();
        return view;
    }

    private void setUpRecycler() {
        adapter = new ProductsAdapter();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        loadAllProducts();
    }

    private void loadAllProducts() {
        productsList = new ArrayList<>();
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productsList.clear();
                if(snapshot.hasChild("All Products"))
                {
                    productsRef.child("All Products").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Iterable<DataSnapshot> children = snapshot.getChildren();
                            for(DataSnapshot ds : children)
                            {
                                Products products = ds.getValue(Products.class);
                                productsList.add(products);
                            }
                            Log.d(TAG, "hema onDataChange: "+productsList.get(0).getpName());
                            adapter.setData(getActivity() , productsList);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
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