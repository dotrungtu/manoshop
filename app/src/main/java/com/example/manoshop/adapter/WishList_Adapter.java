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
import com.example.manoshop.WishList_Activity;
import com.example.manoshop.model.Cart;
import com.example.manoshop.model.WishList;
import com.example.manoshop.network.Delete_Product_In_WishList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WishList_Adapter extends RecyclerView.Adapter<WishList_Adapter.WishListViewHolder> {

    private Context context;
    List<WishList> wishlist;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    String currentUserID;

    public WishList_Adapter(Context context, List<WishList> wishlist) {
        this.context = context;
        this.wishlist = wishlist;

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
    }

    @NonNull
    @Override
    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inflater_wish_list, parent, false);
        return new WishListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListViewHolder holder, final int position) {
        holder.txt_wish_product_name.setText(wishlist.get(position).getProductName());
        holder.txt_wish_product_brand.setText(wishlist.get(position).getProductBrand());
        holder.txt_wish_product_price.setText(String.valueOf(wishlist.get(position).getProductPrice()));

        Picasso.get()
                .load(wishlist.get(position).getImgUrl())
                .into(holder.img_wish_product);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductDetail_Activity.class);
                i.putExtra("productName", wishlist.get(position).getProductName());
                i.putExtra("productPrice", wishlist.get(position).getProductPrice());
                i.putExtra("productBrand", wishlist.get(position).getProductBrand());
                i.putExtra("productDes", wishlist.get(position).getProductDes());
                i.putExtra("productAmount", wishlist.get(position).getProductAmount());
                i.putExtra("productID", wishlist.get(position).getProductID());
                i.putExtra("imgUrl", wishlist.get(position).getImgUrl());
                context.startActivity(i);
            }
        });
        holder.img_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wishlist.get(position).getProductID() != null)
                {
                    String productNameWishList = wishlist.get(position).getProductName();
                    String productIDWishList = wishlist.get(position).getProductID();
                    String imgUrl = wishlist.get(position).getImgUrl();
                    String productBrandWishList = wishlist.get(position).getProductBrand();
                    String productDesWishList = wishlist.get(position).getProductDes();
                    int productAmountWishList = Integer.parseInt(String.valueOf(wishlist.get(position).getProductAmount()));
                    int productPriceWishList = Integer.parseInt(String.valueOf(wishlist.get(position).getProductPrice()));
                    int productTotalPriceWishList = Integer.parseInt(String.valueOf(wishlist.get(position).getProductPrice()));
                    String productSizeWishList = "M";

                    addToCart(productNameWishList, productAmountWishList, productTotalPriceWishList, productPriceWishList, imgUrl, productIDWishList, productSizeWishList, productBrandWishList, productDesWishList);
                }
                else
                {
                    Intent intent = new Intent(context.getApplicationContext(), WishList_Activity.class);
                    context.startActivity(intent);
                }
                String productID = wishlist.get(position).getProductID();
                Task<Void> voidTask = Delete_Product_In_WishList
                        .deleteProductInWishList(productID);
                voidTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
                wishlist.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, wishlist.size());
            }
        });

        holder.img_remove_from_wish_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String productID = wishlist.get(position).getProductID();
                Task<Void> voidTask = Delete_Product_In_WishList
                        .deleteProductInWishList(productID);
                voidTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Đã xóa mặt hàng yêu thích: " + productID, Toast.LENGTH_SHORT).show();
                    }
                });
                wishlist.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, wishlist.size());
            }
        });
}

    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    public static final class WishListViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_wish_product_name, txt_wish_product_price, txt_wish_product_brand;
        private ImageView img_wish_product, img_remove_from_wish_list, img_add_to_cart;
        ProductDetail_Activity productDetail_activity;



        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);
            productDetail_activity = new ProductDetail_Activity();
            txt_wish_product_name = itemView.findViewById(R.id.txt_wishlist_product_name);
            txt_wish_product_price = itemView.findViewById(R.id.txt_wishlist_price);
            txt_wish_product_brand = itemView.findViewById(R.id.txt_wishlist_brand);
            img_wish_product = itemView.findViewById(R.id.img_wishlist_product);
            img_add_to_cart = itemView.findViewById(R.id.img_add_to_cart);
            img_remove_from_wish_list = itemView.findViewById(R.id.img_remove_item_wishlist);

        }
    }
    public boolean addToCart(String productName, int productAmount, int totalPrice, int productPrice, String imgUrl, String productID, String productSize, String productBrand, String productDes) {
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ShoppingCart").child(currentUserID).child(productID);
        Cart cacPhongDaDat = new Cart(
                productName, productAmount, totalPrice, productPrice, imgUrl, productID, productSize, productBrand, productDes);
        databaseReference.setValue(cacPhongDaDat);
        Toast.makeText(context.getApplicationContext(), "Sản phẩm "+ productName + " đã chuyển sang giỏ hàng", Toast.LENGTH_SHORT).show();
        return true;
    }
}
