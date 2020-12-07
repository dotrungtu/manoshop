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

public class Recommend_Adapter extends RecyclerView.Adapter<Recommend_Adapter.RecommendViewHolder> {

    Context context;
    List<Product> recommendList;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public Recommend_Adapter(Context context, List<Product> recommendList) {
        this.context = context;
        this.recommendList = recommendList;


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
    }

    @NonNull
    @Override
    public RecommendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inflater_recommend, parent, false);
        return new RecommendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendViewHolder holder, int position) {
        holder.txt_re_product_name.setText(String.valueOf(recommendList.get(position).getProductPrice()));

        Picasso.get()
                .load(recommendList.get(position).getImgUrl())
                .into(holder.img_re_product);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductDetail_Activity.class);
                i.putExtra("productName", recommendList.get(position).getProductName());
                i.putExtra("productPrice", recommendList.get(position).getProductPrice());
                i.putExtra("productBrand", recommendList.get(position).getProductBrand());
                i.putExtra("productDes", recommendList.get(position).getProductDes());
                i.putExtra("productAmount", recommendList.get(position).getProductAmount());
                i.putExtra("productID", recommendList.get(position).getProductID());
                i.putExtra("imgUrl", recommendList.get(position).getImgUrl());
                context.startActivity(i);
            }
        });

        holder.img_re_add_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = recommendList.get(position).getProductName();
                String productID = recommendList.get(position).getProductID();
                String productBrand = recommendList.get(position).getProductBrand();
                String productDes = recommendList.get(position).getProductDes();
                int productAmount = Integer.parseInt(String.valueOf(recommendList.get(position).getProductAmount()));
                int productPrice = Integer.parseInt(String.valueOf(recommendList.get(position).getProductPrice()));
                String imgUrl = recommendList.get(position).getImgUrl();
                addToWishList(productName, productAmount, productPrice, imgUrl, productID, productBrand, productDes);
            }
        });
    }


    public boolean addToWishList(String productName, int productAmount, int productPrice, String imgUrl, String productID, String productBrand, String productDes) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("WishList").child(user.getUid()).child(productID);
        WishList cacPhongDaDat = new WishList(
                productName, productAmount, productPrice, imgUrl, productID, productBrand, productDes);
        databaseReference.setValue(cacPhongDaDat);
        Toast.makeText(context.getApplicationContext(), "Đã thêm vào mục yêu thích", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public int getItemCount() {
        return recommendList.size();
    }

    public static class RecommendViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_re_product_name;
        private ImageView img_re_add_wishlist, img_re_product;

        public RecommendViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_re_product_name = itemView.findViewById(R.id.txt_re_product_price);
            img_re_product = itemView.findViewById(R.id.img_re_product);
            img_re_add_wishlist = itemView.findViewById(R.id.img_btn_re_add_wish_list);
        }
    }
}
