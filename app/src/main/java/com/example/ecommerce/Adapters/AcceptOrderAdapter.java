package com.example.ecommerce.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.AdminAcceptOrdersActivity;
import com.example.ecommerce.CartActivity;
import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Model.Orders;
import com.example.ecommerce.R;
import com.example.ecommerce.UserProductsDetailsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AcceptOrderAdapter extends RecyclerView.Adapter<AcceptOrderAdapter.MyHolder> {
    Context context;
    List<Orders> ordersList;
    private AlertDialog show;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ordersRef,allOrdersRef;

    public void setData(Context context, List<Orders> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        allOrdersRef = firebaseDatabase.getReference().child("All Orders");
        ordersRef = firebaseDatabase.getReference().child("Orders");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.orders_raw, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Orders orders = ordersList.get(position);
        holder.orderName.setText(orders.getName());
        holder.orderShipped.setText(orders.getShipped());
        holder.orderAddress.setText(orders.getAddress()+" " +orders.getCity());
        holder.orderDate.setText(orders.getDate()+" "+orders.getTime());
        holder.orderPrice.setText(orders.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return ordersList!=null?ordersList.size():0;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView orderName,orderPrice,orderShipped,orderDate,orderAddress;
        Button ordersDetails;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            orderAddress = itemView.findViewById(R.id.orders_pro_address);
            orderDate = itemView.findViewById(R.id.orders_pro_date);
            orderName = itemView.findViewById(R.id.orders_pro_name);
            orderPrice = itemView.findViewById(R.id.orders_pro_price);
            orderShipped = itemView.findViewById(R.id.orders_pro_shipped);
            ordersDetails = itemView.findViewById(R.id.orders_pro_details);
            String [] m = {"on the way","shipped","not shipped"};
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Orders orders = ordersList.get(getAdapterPosition());
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setItems(m, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == 0)
                            {
                                allOrdersRef.child(orders.getKey()).child("shipped").setValue("on the way").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        ordersRef.child(orders.getUserKey()).child(orders.getKey()).child("shipped").setValue("on the way").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "successfully", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else if (which == 1)
                            {
                                allOrdersRef.child(orders.getKey()).child("shipped").setValue("shipped").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        ordersRef.child(orders.getUserKey()).child(orders.getKey()).child("shipped").setValue("shipped").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "shipped successfully", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else
                            {
                                allOrdersRef.child(orders.getKey()).child("shipped").setValue("not shipped").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        ordersRef.child(orders.getUserKey()).child(orders.getKey()).child("shipped").setValue("not shipped").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "shipped successfully", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    show = builder.show();
                }
            });

            ordersDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Orders orders = ordersList.get(getAdapterPosition());
                    Intent intent = new Intent(context,UserProductsDetailsActivity.class);
                    intent.putExtra("orders",orders);
                    context.startActivity(intent);
                }
            });
        }
    }
}
