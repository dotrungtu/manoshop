package com.example.manoshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manoshop.R;
import com.example.manoshop.adapter.NewProduct_Adapter;
import com.example.manoshop.adapter.Recommend_Adapter;
import com.example.manoshop.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NewProducts_Activity extends AppCompatActivity {

    RecyclerView recycler_new_product;
    RecyclerView recycler_recommend;
    NewProduct_Adapter newProduct_adapter;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceUser;
    Recommend_Adapter recommend_adapter;
    private ImageView img_ic_new_pro_back, img_btn_new_pro_to_cart, img_btn_new_pro_to_wish_list;

    Button btn_to_request_login, btn_dialog_check_out_cancel;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ChildEventListener childEventListener;
    FirebaseDatabase firebaseDatabase;

    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_products_);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("User");

        recycler_new_product = findViewById(R.id.recycler_new_product);
        recycler_recommend = findViewById(R.id.recycler_new_product_recommend);

        img_ic_new_pro_back = findViewById(R.id.img_btn_new_product_back);
        img_btn_new_pro_to_cart = findViewById(R.id.img_btn_new_pro_cart);
        img_btn_new_pro_to_wish_list = findViewById(R.id.img_btn_new_pro_to_fav);

        img_btn_new_pro_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    View view2 = LayoutInflater.from(NewProducts_Activity.this).inflate(R.layout.dialog_request, null);
                    btn_to_request_login = view2.findViewById(R.id.btn_res_to_sign_in);
                    btn_dialog_check_out_cancel = view2.findViewById(R.id.btn_dialog_check_out_cancel);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(NewProducts_Activity.this);
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
                } else {
                    startActivity(new Intent(getApplicationContext(), ShoppingCart_Activity.class));
                    overridePendingTransition(R.anim.zoom_in_to_activity, R.anim.zoom_out_to_activity);
                }
            }
        });

        img_btn_new_pro_to_wish_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    View view2 = LayoutInflater.from(NewProducts_Activity.this).inflate(R.layout.dialog_request, null);
                    btn_to_request_login = view2.findViewById(R.id.btn_res_to_sign_in);
                    btn_dialog_check_out_cancel = view2.findViewById(R.id.btn_dialog_check_out_cancel);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(NewProducts_Activity.this);
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
                } else {
                    startActivity(new Intent(getApplicationContext(), WishList_Activity.class));
                    overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
                }

            }
        });

        img_ic_new_pro_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Firebase newProductList
        final List<Product> newProductList;

        recycler_new_product.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerWomen = new LinearLayoutManager(NewProducts_Activity.this, RecyclerView.HORIZONTAL, false);
        recycler_new_product.setLayoutManager(layoutManagerWomen);

        newProductList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("NewProducts");

        newProduct_adapter = new NewProduct_Adapter(NewProducts_Activity.this, newProductList);

        recycler_new_product.setAdapter(newProduct_adapter);

        if (user == null) {
            Toast.makeText(this, "Please login to view new products", Toast.LENGTH_SHORT).show();
        } else {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Product newProduct = snapshot.getValue(Product.class);
                    newProduct.setProductID(snapshot.child("productID").getValue().toString());
                    newProductList.add(newProduct);
                    newProduct_adapter.notifyItemInserted(newProductList.size());

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Product newProduct = snapshot.getValue(Product.class);
                    newProduct.setProductID(snapshot.child("productID").getValue().toString());
                    newProductList.remove(newProduct);
                    newProduct_adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            databaseReference.addChildEventListener(childEventListener);
        }


        //Firebase recommendList
        final List<Product> recommendList;

        recycler_recommend.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerRecommend = new LinearLayoutManager(NewProducts_Activity.this, RecyclerView.HORIZONTAL, false);
        recycler_recommend.setLayoutManager(layoutManagerRecommend);

        recommendList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("FeaturedClothes");

        recommend_adapter = new Recommend_Adapter(NewProducts_Activity.this, recommendList);

        recycler_recommend.setAdapter(recommend_adapter);

        if (user == null) {
            Toast.makeText(this, "Please login to view new products", Toast.LENGTH_SHORT).show();
        } else {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Product recommendData = snapshot.getValue(Product.class);
                    recommendData.setProductID(snapshot.child("productID").getValue().toString());
                    recommendList.add(recommendData);
                    recommend_adapter.notifyItemInserted(recommendList.size());

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Product recommendData = snapshot.getValue(Product.class);
                    recommendData.setProductID(snapshot.child("productID").getValue().toString());
                    recommendList.remove(recommendData);
                    recommend_adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }
}
