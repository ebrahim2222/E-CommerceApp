package com.example.ecommerce.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Model.Orders;
import com.example.ecommerce.OrdersActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.UserProductsDetailsActivity;

import java.util.List;

import io.paperdb.Paper;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyHolder> {
    Context context;
    private static final String TAG = "OrdersAdapter";
    List<Orders> ordersList;
    public void setData(Context context, List<Orders> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Paper.init(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.orders_raw, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Orders orders = ordersList.get(position);
        holder.orderName.setText(orders.getName());
        holder.orderPrice.setText(orders.getTotalPrice());
        holder.orderShipped.setText(orders.getShipped());
        holder.orderDate.setText(orders.getDate()+" "+orders.getTime());
        holder.orderAddress.setText(orders.getAddress()+" "+orders.getCity());
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

            ordersDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Orders orders = ordersList.get(getAdapterPosition());
                    Intent intent = new Intent(context, UserProductsDetailsActivity.class);
                    Paper.book().write("proKey",orders.getUserKey());
                    intent.putExtra("orders",orders);
                    context.startActivity(intent);
                }
            });
        }
    }
}
