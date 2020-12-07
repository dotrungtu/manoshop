package com.example.manoshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manoshop.ProductDetail_Activity;
import com.example.manoshop.R;
import com.example.manoshop.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.Product_For_Wo_Ki_ViewHolder> {

    List<Product> productListWK;
    Context context;

    public Product_Adapter(Context context, List<Product> productListWK) {
        this.context = context;
        this.productListWK = productListWK;
    }

    @NonNull
    @Override
    public Product_For_Wo_Ki_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inflater_single_product_w_k, parent, false);
        return new Product_For_Wo_Ki_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Product_For_Wo_Ki_ViewHolder holder, final int position) {
        holder.txt_name_wk.setText(productListWK.get(position).getProductName());
        holder.txt_price_wk.setText(String.valueOf(productListWK.get(position).getProductPrice()));
        holder.txt_brand_wk.setText(productListWK.get(position).getProductBrand());

        Picasso.get().load(productListWK.get(position).getImgUrl()).into(holder.img_product_wk);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetail_Activity.class);
                i.putExtra("productName", productListWK.get(position).getProductName());
                i.putExtra("productPrice", productListWK.get(position).getProductPrice());
                i.putExtra("productBrand", productListWK.get(position).getProductBrand());
                i.putExtra("productDes", productListWK.get(position).getProductDes());
                i.putExtra("productAmount", productListWK.get(position).getProductAmount());
                i.putExtra("productID", productListWK.get(position).getProductID());
                i.putExtra("imgUrl", productListWK.get(position).getImgUrl());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productListWK.size();
    }

    public static class Product_For_Wo_Ki_ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_product_wk;
        TextView txt_name_wk, txt_price_wk, txt_brand_wk;

        public Product_For_Wo_Ki_ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product_wk = itemView.findViewById(R.id.imgProduct_WK);
            txt_name_wk = itemView.findViewById(R.id.txtProductName_WK);
            txt_brand_wk = itemView.findViewById(R.id.txtProductBrand_WK);
            txt_price_wk = itemView.findViewById(R.id.txtProductPrice_WK);
        }
    }
}
