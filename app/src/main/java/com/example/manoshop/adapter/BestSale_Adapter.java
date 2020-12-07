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
import com.example.manoshop.model.Sale;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BestSale_Adapter extends RecyclerView.Adapter<BestSale_Adapter.BestSaleViewHolder> {

    Context context;
    List<Sale> saleList;

    public BestSale_Adapter(Context context, List<Sale> saleList) {
        this.context = context;
        this.saleList = saleList;
    }

    @NonNull
    @Override
    public BestSaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inflater_sale, parent, false);
        return new BestSaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSaleViewHolder holder, final int position) {
        holder.txt_pro_sale_old_price.setText(String.valueOf(saleList.get(position).getProductPrice()));
        holder.txt_pro_sale_new_price.setText(String.valueOf(saleList.get(position).getProductNewPrice()));

        Picasso.get()
                .load(saleList.get(position).getImgUrl())
                .into(holder.img_product_sale);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductDetail_Activity.class);
                i.putExtra("productName", saleList.get(position).getProductName());
                i.putExtra("productPrice", saleList.get(position).getProductNewPrice());
                i.putExtra("productBrand", saleList.get(position).getProductBrand());
                i.putExtra("productDes", saleList.get(position).getProductDes());
                i.putExtra("productAmount", saleList.get(position).getProductAmount());
                i.putExtra("productID", saleList.get(position).getProductID());
                i.putExtra("imgUrl", saleList.get(position).getImgUrl());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return saleList.size();
    }

    public static class BestSaleViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_pro_sale_old_price, txt_pro_sale_new_price;
        private ImageView img_product_sale;

        public BestSaleViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_pro_sale_new_price = itemView.findViewById(R.id.txt_pro_sale_new_price);
            txt_pro_sale_old_price = itemView.findViewById(R.id.txt_pro_sale_old_price);
            img_product_sale = itemView.findViewById(R.id.img_product_sale);
        }
    }
}
