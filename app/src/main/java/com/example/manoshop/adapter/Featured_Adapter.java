
package com.example.manoshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manoshop.ProductDetail_Activity;
import com.example.manoshop.R;
import com.example.manoshop.model.Product;
import com.example.manoshop.model.WishList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Featured_Adapter extends RecyclerView.Adapter<Featured_Adapter.FeaturedDeViewHolder> {

    Context context;
    List<Product> featuredList;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public Featured_Adapter(Context context, List<Product> featuredList) {
        this.context = context;
        this.featuredList = featuredList;

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");


    }

    @NonNull
    @Override
    public FeaturedDeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inflater_featured, parent, false);
        return new FeaturedDeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedDeViewHolder holder, final int position) {
        holder.txt_featured_price.setText(String.valueOf(featuredList.get(position).getProductPrice()));

        Picasso.get()
                .load(featuredList.get(position).getImgUrl())
                .into(holder.img_featured);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetail_Activity.class);
                i.putExtra("productName", featuredList.get(position).getProductName());
                i.putExtra("productPrice", featuredList.get(position).getProductPrice());
                i.putExtra("productBrand", featuredList.get(position).getProductBrand());
                i.putExtra("productDes", featuredList.get(position).getProductDes());
                i.putExtra("productAmount", featuredList.get(position).getProductAmount());
                i.putExtra("productID", featuredList.get(position).getProductID());
                i.putExtra("imgUrl", featuredList.get(position).getImgUrl());
                context.startActivity(i);
            }
        });
        holder.img_btn_featured_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = featuredList.get(position).getProductName();
                String productID = featuredList.get(position).getProductID();
                String productBrand = featuredList.get(position).getProductBrand();
                String productDes = featuredList.get(position).getProductDes();
                int productAmount = Integer.parseInt(String.valueOf(featuredList.get(position).getProductAmount()));
                int productPrice = Integer.parseInt(String.valueOf(featuredList.get(position).getProductPrice()));
                String imgUrl = featuredList.get(position).getImgUrl();
                addToWishList(productName, productAmount, productPrice, imgUrl, productID, productBrand, productDes);
            }
        });
    }

    @Override
    public int getItemCount() {
        return featuredList.size();
    }

    public static class FeaturedDeViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_featured_price;
        private ImageView img_featured, img_btn_featured_fav;
        public FeaturedDeViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_featured_price = itemView.findViewById(R.id.txt_featured_de_price);
            img_featured = itemView.findViewById(R.id.img_featured_de);
            img_btn_featured_fav = itemView.findViewById(R.id.img_btn_featured_de_fav);

        }
    }
    public boolean addToWishList(String productName, int productAmount, int productPrice, String imgUrl, String productID, String productBrand, String productDes) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("WishList").child(user.getUid()).child(productID);
        WishList cacPhongDaDat = new WishList(
                productName, productAmount, productPrice, imgUrl, productID, productBrand, productDes);
        databaseReference.setValue(cacPhongDaDat);
        Toast.makeText(context.getApplicationContext(), "Đã thêm vào mục yêu thích", Toast.LENGTH_SHORT).show();
        return true;
    }
}
