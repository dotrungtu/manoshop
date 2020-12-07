package com.example.manoshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manoshop.R;
import com.example.manoshop.adapter.WishList_Adapter;
import com.example.manoshop.model.Cart;
import com.example.manoshop.model.WishList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductDetail_Activity extends AppCompatActivity {

    public ImageView img_product_de_back, img_product_de_to_cart, img_product_de_title, img_product_de_heart_addto_wishlist;
    TextView txt_product_de_name, txt_product_de_price, txt_product_de_des, txt_product_de_brand, tv_product_detail_email_user;
    Button btnAddProductToCart;
    Spinner spinner_de_size;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    String currentUserID;
    Button btn_to_request_login, btn_dialog_check_out_cancel;
    WishList_Adapter wishList_adapter;
    List<WishList> wishLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_detail_);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");


        wishLists = new ArrayList<>();
        wishList_adapter = new WishList_Adapter(ProductDetail_Activity.this, wishLists);


        String productName = getIntent().getStringExtra("productName");
        String productBrand = getIntent().getStringExtra("productBrand");
        int productPrice = getIntent().getIntExtra("productPrice", 0);
        String productDes = getIntent().getStringExtra("productDes");
        final int productAmount = getIntent().getIntExtra("productAmount", 1);
        final String productID = getIntent().getStringExtra("productID");
        final String imgUrl = getIntent().getStringExtra("imgUrl");

        txt_product_de_name = findViewById(R.id.txt_product_de_name);
        txt_product_de_des = findViewById(R.id.txt_product_de_des);
        txt_product_de_price = findViewById(R.id.txt_product_de_price);
        spinner_de_size = findViewById(R.id.spinner_detail_size);
        txt_product_de_brand = findViewById(R.id.txt_product_de_brand);

        img_product_de_title = findViewById(R.id.img_product_de);
        img_product_de_heart_addto_wishlist = findViewById(R.id.iconHeart);
        btnAddProductToCart = findViewById(R.id.btn_de_add_to_cart);


        //Đổ dữ liệu bằng intent từ product_adapter -> productdetail_activity
        txt_product_de_name.setText(productName);
        txt_product_de_des.setText(productDes);
        txt_product_de_price.setText(String.valueOf(productPrice));
        txt_product_de_brand.setText(productBrand);

        Picasso.get()
                .load(imgUrl)
                .into(img_product_de_title);

        final Spinner spinner_de_size = findViewById(R.id.spinner_detail_size);
        ArrayAdapter<CharSequence> de_size = ArrayAdapter.createFromResource(this, R.array.spinner_size, R.layout.support_simple_spinner_dropdown_item);
        de_size.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_de_size.setAdapter(de_size);

        img_product_de_back = findViewById(R.id.img_btn_product_de_back);
        img_product_de_to_cart = findViewById(R.id.img_btn_product_de_to_cart);
        img_product_de_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    startActivity(new Intent(getApplicationContext(), ShoppingCart_Activity.class));
                    overridePendingTransition(R.anim.zoom_in_to_activity, R.anim.zoom_out_to_activity);
                }
                else if(user == null){
                    View view2 = LayoutInflater.from(ProductDetail_Activity.this).inflate(R.layout.dialog_request, null);
                    btn_to_request_login = view2.findViewById(R.id.btn_res_to_sign_in);
                    btn_dialog_check_out_cancel = view2.findViewById(R.id.btn_dialog_check_out_cancel);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetail_Activity.this);
                    builder.setView(view2);

                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    btn_to_request_login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                            overridePendingTransition(R.anim.zoom_in_to_activity, R.anim.zoom_out_to_activity);
                            finish();
                        }
                    });
                    btn_dialog_check_out_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });


        // Click - Nút quay lại
        img_product_de_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Nút xử lý sự kiện thêm sản phẩm vào giỏ hàng
        btnAddProductToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    currentUserID = firebaseAuth.getCurrentUser().getUid();
                    String productName = txt_product_de_name.getText().toString();
                    String productBrand = txt_product_de_brand.getText().toString();
                    String productDes = txt_product_de_des.getText().toString();
                    int productPrice = Integer.parseInt(txt_product_de_price.getText().toString());
                    int totalPrice = Integer.parseInt(txt_product_de_price.getText().toString());
                    String productSize = spinner_de_size.getSelectedItem().toString();
                    addToCart(productName, productAmount, totalPrice, productPrice, imgUrl, productID, productSize, productBrand, productDes);
                } else {
                    Toast.makeText(ProductDetail_Activity.this, "Please login before", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Nếu người dùng chưa đăng nhập thì chưa cho phép thêm sản phẩm vào wislist
        //Nếu đã đăng nhập thì người dùng được phép thêm sản phẩm vào wishlist
        if (user == null) {
            img_product_de_heart_addto_wishlist.setVisibility(View.INVISIBLE);
        } else if (user != null) {
            img_product_de_heart_addto_wishlist.setVisibility(View.VISIBLE);
            img_product_de_heart_addto_wishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (img_product_de_heart_addto_wishlist.isEnabled()) {
                        currentUserID = firebaseAuth.getCurrentUser().getUid();
                        String productName = txt_product_de_name.getText().toString();
                        String productBrand = txt_product_de_brand.getText().toString();
                        String productDes = txt_product_de_des.getText().toString();
                        int productPrice = Integer.parseInt(txt_product_de_price.getText().toString());
                        addToWishList(productName, productAmount, productPrice, imgUrl, productID, productBrand, productDes);
                        img_product_de_heart_addto_wishlist.setImageResource(R.drawable.gradient_ic_fav_filled);
                    } else {
                        img_product_de_heart_addto_wishlist.setImageResource(R.drawable.gradient_ic_fav_nc);
                    }
                }
            });
        }
    }

    //Phương thức thêm sản phẩm vào giỏ hàng
    public boolean addToCart(String productName, int productAmount, int totalPrice, int productPrice, String imgUrl, String productID, String productSize, String productBrand, String productDes) {
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ShoppingCart").child(currentUserID).child(productID);
        Cart addProductToCart = new Cart(
                productName, productAmount, totalPrice, productPrice, imgUrl, productID, productSize, productBrand, productDes);
        databaseReference.setValue(addProductToCart);
        Toast.makeText(ProductDetail_Activity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        return true;
    }

    //Phương thức thêm sản phẩm vào wish list
    public boolean addToWishList(String productName, int productAmount, int productPrice, String imgUrl, String productID, String productBrand, String productDes) {
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("WishList").child(currentUserID).child(productID);
        WishList addProductToWishList = new WishList(
                productName, productAmount, productPrice, imgUrl, productID, productBrand, productDes);
        databaseReference.setValue(addProductToWishList);
        Toast.makeText(ProductDetail_Activity.this, "Đã thêm vào mục yêu thích", Toast.LENGTH_SHORT).show();
        return true;
    }
}