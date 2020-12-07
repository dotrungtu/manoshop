package com.example.manoshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manoshop.ProductDetail_Activity;
import com.example.manoshop.R;
import com.example.manoshop.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridView_Adapter extends RecyclerView.Adapter<GridView_Adapter.TwoSpanColViewHolder> {

    Context context;
    List<Product> twospancolList;

    public GridView_Adapter(Context context, List<Product> twospancolList) {
        this.context = context;
        this.twospancolList = twospancolList;
    }

    @NonNull
    @Override
    public TwoSpanColViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.inflater_gird_view,parent,false);
        return new TwoSpanColViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TwoSpanColViewHolder holder,final int position) {

        Picasso.get()
                .load(twospancolList.get(position).getImgUrl())
                .into(holder.img_top_picks);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductDetail_Activity.class);
                i.putExtra("productName", twospancolList.get(position).getProductName());
                i.putExtra("productPrice", twospancolList.get(position).getProductPrice());
                i.putExtra("productBrand", twospancolList.get(position).getProductBrand());
                i.putExtra("productDes", twospancolList.get(position).getProductDes());
                i.putExtra("productAmount", twospancolList.get(position).getProductAmount());
                i.putExtra("productID", twospancolList.get(position).getProductID());
                i.putExtra("imgUrl", twospancolList.get(position).getImgUrl());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return twospancolList.size();
    }

    public static class TwoSpanColViewHolder extends RecyclerView.ViewHolder {

        ImageView img_top_picks;

        public TwoSpanColViewHolder(@NonNull View itemView) {
            super(itemView);

            img_top_picks = itemView.findViewById(R.id.img_top_picks);
        }
    }
}
