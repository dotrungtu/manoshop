package com.example.manoshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manoshop.R;
import com.example.manoshop.adapter.WishList_Adapter;
import com.example.manoshop.model.WishList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class WishList_Activity extends AppCompatActivity {

    WishList_Adapter wishlist_adapter;
    RecyclerView recycler_wishlist;
    private ImageView img_ic_close;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceUser;
    DatabaseReference databaseReferenceUserID;
    ChildEventListener childEventListener;
    List<WishList> wishLists;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    String currentUserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wish_list_);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("User");


        recycler_wishlist = findViewById(R.id.recycler_wishlist);
        recycler_wishlist.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerWomen = new LinearLayoutManager(WishList_Activity.this, RecyclerView.VERTICAL, false);
        recycler_wishlist.setLayoutManager(layoutManagerWomen);

        wishLists = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("WishList");

        wishlist_adapter = new WishList_Adapter(WishList_Activity.this, wishLists);

        recycler_wishlist.setAdapter(wishlist_adapter);

        if (user != null) {
            currentUserUid = firebaseAuth.getCurrentUser().getUid();
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    WishList wishlist = snapshot.getValue(WishList.class);
                    wishlist.setProductID(snapshot.child("productID").getValue().toString());
                    wishLists.add(wishlist);
                    wishlist_adapter.notifyItemInserted(wishLists.size());
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    WishList wishlist = snapshot.getValue(WishList.class);
                    wishlist.setProductID(snapshot.child("productID").getValue().toString());
                    wishLists.remove(wishlist);
                    wishlist_adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            databaseReference.child(currentUserUid).addChildEventListener(childEventListener);
        }
        else
        {
            Toast.makeText(this, "Vui lòng đăng nhập để xem các mặt hàng yêu thích", Toast.LENGTH_SHORT).show();
        }

        img_ic_close = findViewById(R.id.img_btn_close_wishlist);
        img_ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_right_back, R.anim.slide_in_left_back);
            }
        });


    }

}
