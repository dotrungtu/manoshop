package com.example.manoshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class ProductList_Adapter extends RecyclerView.Adapter<ProductList_Adapter.ProductListViewHolder> {

    Context context;
    List<Product> productList;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public ProductList_Adapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
    }
    public void setData(List<Product> data) {
        this.productList = data;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewList = LayoutInflater.from(context).inflate(R.layout.inflater_product_list, parent, false);
        return new ProductListViewHolder(viewList);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductListViewHolder holder, final int position) {


        holder.txtName.setText(productList.get(position).getProductName());
        holder.txtPrice.setText(String.valueOf(productList.get(position).getProductPrice()));
        holder.txtBrand.setText(productList.get(position).getProductBrand());

        Picasso.get().load(productList.get(position).getImgUrl()).into(holder.imgProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetail_Activity.class);
                i.putExtra("productName", productList.get(position).getProductName());
                i.putExtra("productPrice", productList.get(position).getProductPrice());
                i.putExtra("productBrand", productList.get(position).getProductBrand());
                i.putExtra("productDes", productList.get(position).getProductDes());
                i.putExtra("productAmount", productList.get(position).getProductAmount());
                i.putExtra("productID", productList.get(position).getProductID());
                i.putExtra("imgUrl", productList.get(position).getImgUrl());
                context.startActivity(i);
            }
        });
        holder.btn_img_product_list_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = productList.get(position).getProductName();
                String productID = productList.get(position).getProductID();
                String productBrand = productList.get(position).getProductBrand();
                String productDes = productList.get(position).getProductDes();
                int productAmount = Integer.parseInt(String.valueOf(productList.get(position).getProductAmount()));
                int productPrice = Integer.parseInt(String.valueOf(productList.get(position).getProductPrice()));
                String imgUrl = productList.get(position).getImgUrl();
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
        return productList.size();
    }

    public static final class ProductListViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgProduct, btn_img_product_list_fav;
        private TextView txtName, txtPrice, txtBrand;
        private Spinner spinner;

        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtProductName_List);
            txtPrice = itemView.findViewById(R.id.txtPrice_List);
            txtBrand = itemView.findViewById(R.id.txtBrand_List);
            imgProduct = itemView.findViewById(R.id.imgProduct_List);
            btn_img_product_list_fav = itemView.findViewById(R.id.btn_img_product_list_fav);


        }
    }
}
