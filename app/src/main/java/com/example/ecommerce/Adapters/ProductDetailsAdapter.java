package com.example.ecommerce.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.R;
import com.example.ecommerce.UserProductsDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.MyHolder> {
    Context context;
    List<Cart> cartList;
    public void setData(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.details_raw, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Cart cart = cartList.get(position);
        Picasso.get().load(cart.getpImage()).into(holder.proImage);
        holder.proName.setText(cart.getpName());
        holder.proQuantity.setText(cart.getQuantity());
        holder.proPrice.setText(cart.getpPrice());
        holder.proDesc.setText(cart.getpDesc());
    }

    @Override
    public int getItemCount() {
        return cartList!=null?cartList.size():0;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView proImage;
        TextView proName,proPrice,proDesc,proQuantity;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            proImage = itemView.findViewById(R.id.details_pro_image);
            proName = itemView.findViewById(R.id.details_pro_name);
            proDesc = itemView.findViewById(R.id.details_pro_desc);
            proPrice = itemView.findViewById(R.id.details_pro_price);
            proQuantity = itemView.findViewById(R.id.details_pro_quantity);
        }
    }
}
