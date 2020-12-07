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

public class NewProduct_Adapter extends RecyclerView.Adapter<NewProduct_Adapter.NewProductViewHolder> {

    Context context;
    List<Product> newList;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    public NewProduct_Adapter(Context context, List<Product> newList) {
        this.context = context;
        this.newList = newList;
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
    }

    @NonNull
    @Override
    public NewProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inflater_new_product,parent,false);
        return new NewProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewProductViewHolder holder, final int position) {
        holder.txt_new_pro_name.setText(newList.get(position).getProductName());
        holder.txt_new_pro_price.setText(String.valueOf(newList.get(position).getProductPrice()));

        Picasso.get()
                .load(newList.get(position).getImgUrl())
                .into(holder.img_new_product);

        //Add item vào wish list khi bấm vào nút fav
        if (user == null) {
            holder.img_btn_new_pro_fav.setVisibility(View.INVISIBLE);
        } else if (user != null) {
            holder.img_btn_new_pro_fav.setVisibility(View.VISIBLE);
            holder.img_btn_new_pro_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.img_btn_new_pro_fav.isEnabled()) {
                        String productName = newList.get(position).getProductName();
                        String productID = newList.get(position).getProductID();
                        String productBrand = newList.get(position).getProductBrand();
                        String productDes = newList.get(position).getProductDes();
                        int productAmount = Integer.parseInt(String.valueOf(newList.get(position).getProductAmount()));
                        int productPrice = Integer.parseInt(String.valueOf(newList.get(position).getProductPrice()));
                        String imgUrl = newList.get(position).getImgUrl();
                        addToWishList(productName, productAmount, productPrice, imgUrl, productID, productBrand, productDes);
                        holder.img_btn_new_pro_fav.setImageResource(R.drawable.gradient_ic_fav_filled);

                    } else {
                        holder.img_btn_new_pro_fav.setImageResource(R.drawable.gradient_ic_fav_nc);
                    }
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductDetail_Activity.class);
                i.putExtra("productName", newList.get(position).getProductName());
                i.putExtra("productPrice", newList.get(position).getProductPrice());
                i.putExtra("productBrand", newList.get(position).getProductBrand());
                i.putExtra("productDes", newList.get(position).getProductDes());
                i.putExtra("productAmount", newList.get(position).getProductAmount());
                i.putExtra("productID", newList.get(position).getProductID());
                i.putExtra("imgUrl", newList.get(position).getImgUrl());
                context.startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return newList.size();
    }

    public class NewProductViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_new_pro_name, txt_new_pro_price;
        private ImageView img_new_product, img_btn_new_pro_fav;

        public NewProductViewHolder(@NonNull final View itemView) {
            super(itemView);

            txt_new_pro_name = itemView.findViewById(R.id.txt_new_product_name);
            txt_new_pro_price = itemView.findViewById(R.id.txt_new_product_price);
            img_new_product = itemView.findViewById(R.id.img_new_product);
            img_btn_new_pro_fav = itemView.findViewById(R.id.img_btn_new_product_fav);

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
