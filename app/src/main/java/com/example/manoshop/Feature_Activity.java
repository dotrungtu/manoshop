package com.example.manoshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manoshop.R;
import com.example.manoshop.adapter.Featured_Adapter;
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

public class Feature_Activity extends AppCompatActivity {

    RecyclerView featured_recycler;
    Featured_Adapter featured_de_adapter;
    private ImageView img_ic_clsoe, img_btn_fea_to_cart, img_btn_fea_to_fav;
    ChildEventListener childEventListener;
    DatabaseReference databaseReferenceUser;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    String currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("User");

        img_ic_clsoe = findViewById(R.id.img_btn_featured_back);
        img_ic_clsoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        featured_recycler = findViewById(R.id.recycler_featured);
        img_btn_fea_to_cart = findViewById(R.id.img_btn_featured_to_cart);
        img_btn_fea_to_fav = findViewById(R.id.img_btn_featured_to_fav);

        img_btn_fea_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShoppingCart_Activity.class));
                overridePendingTransition(R.anim.zoom_in_to_activity, R.anim.zoom_out_to_activity);
            }
        });

        img_btn_fea_to_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WishList_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_back, R.anim.slide_in_left_back);
            }
        });

        final List<Product> featuredList;

        featuredList = new ArrayList<>();

        StaggeredGridLayoutManager viewlayout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        featured_recycler = findViewById(R.id.recycler_featured);
        featured_recycler.setHasFixedSize(true);
        featured_recycler.setLayoutManager(viewlayout);


        databaseReference = FirebaseDatabase.getInstance().getReference("FeaturedClothes");

        featured_de_adapter = new Featured_Adapter(Feature_Activity.this, featuredList);

        featured_recycler.setAdapter(featured_de_adapter);

        if (user != null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Product shoppingCart = snapshot.getValue(Product.class);
                    shoppingCart.setProductID(snapshot.child("productID").getValue().toString());
                    featuredList.add(shoppingCart);
                    featured_de_adapter.notifyItemInserted(featuredList.size());

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Product shoppingCart = snapshot.getValue(Product.class);
                    shoppingCart.setProductID(snapshot.child("productID").getValue().toString());
                    featuredList.remove(shoppingCart);
                    featured_de_adapter.notifyDataSetChanged();
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
        else {
            Toast.makeText(this, "Vui lòng đăng nhập để xem", Toast.LENGTH_SHORT).show();
        }

    }

    private void setFeaturedRecyler(List<Product> womenList) {
        StaggeredGridLayoutManager viewlayout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        featured_recycler = findViewById(R.id.recycler_featured);
        featured_recycler.setHasFixedSize(true);
        featured_recycler.setLayoutManager(viewlayout);
        featured_de_adapter = new Featured_Adapter(this, womenList);
        featured_recycler.setAdapter(featured_de_adapter);
    }
}
