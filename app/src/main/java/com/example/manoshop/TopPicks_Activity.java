package com.example.manoshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manoshop.R;
import com.example.manoshop.adapter.GridView_Adapter;
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

public class TopPicks_Activity extends AppCompatActivity {

    private ImageView img_ic_back, img_ic_cart;
    GridView_Adapter grid_adapter;
    RecyclerView recycler_top_picks;
    Button  btn_to_request_login, btn_dialog_check_out_cancel;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ChildEventListener childEventListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceUser;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_top_picks_);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("User");

        recycler_top_picks = findViewById(R.id.recycler_top_picks_view);

        img_ic_back = findViewById(R.id.img_btn_top_picks_back);
        img_ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img_ic_cart = findViewById(R.id.img_btn_top_picks_to_cart);
        img_ic_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    startActivity(new Intent(getApplicationContext(), ShoppingCart_Activity.class));
                    overridePendingTransition(R.anim.zoom_in_to_activity, R.anim.zoom_out_to_activity);
                }
                else if(user == null){
                    View view2 = LayoutInflater.from(TopPicks_Activity.this).inflate(R.layout.dialog_request, null);

                    btn_to_request_login = view2.findViewById(R.id.btn_res_to_sign_in);
                    btn_dialog_check_out_cancel = view2.findViewById(R.id.btn_dialog_check_out_cancel);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(TopPicks_Activity.this);
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

        //Add dữ liệu từ Firebase - Realtime Database vào RecycleView chứa các sản phẩm TopPick
        final List<Product> topPickList;

        StaggeredGridLayoutManager gridView = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recycler_top_picks.setHasFixedSize(true);
        recycler_top_picks.setLayoutManager(gridView);

        topPickList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("TopPick");

        grid_adapter = new GridView_Adapter(TopPicks_Activity.this, topPickList);

        recycler_top_picks.setAdapter(grid_adapter);

        if (user == null) {
            Toast.makeText(this, "Please login to view products top picks", Toast.LENGTH_SHORT).show();
        }
        else {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Product topPicks = snapshot.getValue(Product.class);
                    topPicks.setProductID(snapshot.child("productID").getValue().toString());
                    topPickList.add(topPicks);
                    grid_adapter.notifyItemInserted(topPickList.size());

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Product topPicks = snapshot.getValue(Product.class);
                    topPicks.setProductID(snapshot.child("productID").getValue().toString());
                    topPickList.remove(topPicks);
                    grid_adapter.notifyDataSetChanged();
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
