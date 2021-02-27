package com.example.ecommerce.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Model.Products;
import com.example.ecommerce.ProductActivityDetails;
import com.example.ecommerce.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.paperdb.Paper;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyHolder> {


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Paper.init(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.products_raw, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Products products = productsList.get(position);
        holder.productName.setText(products.getpName());
        holder.productPrice.setText(products.getpPrice());
        holder.productDesc.setText(products.getpDesc());
        Picasso.get().load(products.getpImage()).placeholder(R.drawable.profile).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productsList!=null?productsList.size():0;
    }
    Context context;
    List<Products> productsList;
    public void setData(Context context, List<Products> productsList) {
        this.context = context;
        this.productsList = productsList;
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
                    Products products = productsList.get(getAdapterPosition());
                    Intent intent = new Intent(context, ProductActivityDetails.class);
                    intent.putExtra("products",products);
                    Paper.book().write("detailKey",products.getKey());
                    context.startActivity(intent);
                }
            });
        }
    }
}
