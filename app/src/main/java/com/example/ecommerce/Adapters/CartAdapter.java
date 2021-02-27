package com.example.ecommerce.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.CartActivity;
import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.ProductActivityDetails;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.paperdb.Paper;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference cartRef;
    private Cart cart;
    private AlertDialog dialog;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        cartRef = firebaseDatabase.getReference().child("Cart Products");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.products_raw, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Cart cart = cartList.get(position);
        Picasso.get().load(cart.getpImage()).into(holder.productImage);
        holder.productName.setText(cart.getpName());
        holder.productDesc.setText(cart.getpDesc());
        holder.productPrice.setText(cart.getpPrice());

    }

    @Override
    public int getItemCount() {
        return cartList!=null?cartList.size():0;
    }
    Context context;
    List<Cart> cartList;
    public void setData(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView productName,productDesc,productPrice;
        ImageView productImage;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.home_product_name);
            productImage = itemView.findViewById(R.id.home_product_image);
            productDesc = itemView.findViewById(R.id.home_product_desc);
            productPrice = itemView.findViewById(R.id.home_product_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cart = cartList.get(getAdapterPosition());
                    String arr[] = {"Remove"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setItems(arr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == 0) {
                                removeData();
                            }
                        }
                    });
                     dialog = builder.show();
                }
            });
        }
    }

    private void removeData() {
        String key = Paper.book().read("key");
        cartRef.child(key).child(cart.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "removed Successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
