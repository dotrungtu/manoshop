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
import com.example.manoshop.model.Featured;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FeaturedLocations_Adapter extends RecyclerView.Adapter<FeaturedLocations_Adapter.ProductViewHolder> {

    Context context;
    List<Featured> productsList;
    private OnClickItemListener mListener;

    public interface OnClickItemListener {
        void onItemClick(int position);
    }

    public FeaturedLocations_Adapter(Context context, List<Featured> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewProductList = LayoutInflater.from(context).inflate(R.layout.inflater_single_product,parent,false);
        return new ProductViewHolder(viewProductList, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        holder.txtProductName.setText(productsList.get(position).getProductName());
        holder.txtProductPrice.setText(String.valueOf(productsList.get(position).getProductPrice()));

        Picasso.get()
                .load(productsList.get(position).getImgUrl())
                .into(holder.imgProduct);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetail_Activity.class);
                i.putExtra("productName", productsList.get(position).getProductName());
                i.putExtra("productPrice", productsList.get(position).getProductPrice());
                i.putExtra("productBrand", productsList.get(position).getProductBrand());
                i.putExtra("productDes", productsList.get(position).getProductDes());
                i.putExtra("productAmount", productsList.get(position).getProductAmount());
                i.putExtra("productID", productsList.get(position).getProductID());
                i.putExtra("imgUrl", productsList.get(position).getImgUrl());
                context.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static final class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView txtProductName, txtProductPrice;
        public ProductViewHolder(@NonNull View itemView, final OnClickItemListener listener) {
            super(itemView);

            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
