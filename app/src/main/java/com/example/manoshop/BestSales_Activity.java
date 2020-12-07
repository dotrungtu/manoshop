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
import android.widget.TextView;
import android.widget.Toast;

import com.example.manoshop.R;
import com.example.manoshop.adapter.BestSale_Adapter;
import com.example.manoshop.model.Sale;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BestSales_Activity extends AppCompatActivity {

    private ImageView img_ic_back;
    TextView txt_pro_sale_new_price, txt_pro_sale_old_price;
    RecyclerView recycler_Sale;
    BestSale_Adapter sale_adapter;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceUser;
    ChildEventListener childEventListener;
    List<Sale> bestSale_List;
    Button btn_to_request_login, btn_dialog_check_out_cancel;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    ImageView img_btn_sale_to_cart, img_btn_sale_to_fav;
    String currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_sales_);

        //Lấy dữ liệu từ Authencation và Realtime Datbase của User vừa đăng nhập
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("User");

        //Ánh xạ và xử lý sự kiện quay về activity trước đó
        img_ic_back = findViewById(R.id.img_btn_sale_back);
        img_ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Ánh xạ
        txt_pro_sale_new_price = findViewById(R.id.txt_pro_sale_new_price);
        txt_pro_sale_new_price = findViewById(R.id.txt_pro_sale_new_price);
        img_btn_sale_to_cart = findViewById(R.id.img_btn_sale_to_cart);
        img_btn_sale_to_fav = findViewById(R.id.img_btn_sale_to_fav);

        //Xử lý click vào hình ảnh vào phần Shopping Cart
        img_btn_sale_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Nếu không có user sẽ cho show lên dialog yêu cầu đăng nhập
                // và khi click vào btn_to_request_login sẽ chuyển hướng qua màn hình đăng nhập
                if (user == null) {
                    View view2 = LayoutInflater.from(BestSales_Activity.this).inflate(R.layout.dialog_request, null);
                    btn_to_request_login = view2.findViewById(R.id.btn_res_to_sign_in);
                    btn_dialog_check_out_cancel = view2.findViewById(R.id.btn_dialog_check_out_cancel);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(BestSales_Activity.this);
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
                //Nếu đã có user sẽ cho phép chuyển hướng thẳng tới phần ShoppingCart_Activity
                else {
                    startActivity(new Intent(getApplicationContext(), ShoppingCart_Activity.class));
                    overridePendingTransition(R.anim.zoom_in_to_activity, R.anim.zoom_out_to_activity);
                }
            }
        });

        //Xử lý click vào hình ảnh cho phép chuyển hướng qua WishList của User tương ứng
        img_btn_sale_to_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Nếu không có user sẽ show dialog bắt người dùng đăng nhập
                if (user == null) {
                    View view2 = LayoutInflater.from(BestSales_Activity.this).inflate(R.layout.dialog_request, null);
                    btn_to_request_login = view2.findViewById(R.id.btn_res_to_sign_in);
                    btn_dialog_check_out_cancel = view2.findViewById(R.id.btn_dialog_check_out_cancel);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(BestSales_Activity.this);
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
                //Nếu đã có user sẽ cho phép chuyển hướng thẳng tới phần WishList_Activity
                else {
                    startActivity(new Intent(getApplicationContext(), WishList_Activity.class));
                    overridePendingTransition(R.anim.zoom_in_to_activity, R.anim.zoom_out_to_activity);
                }
            }
        });
        //Ánh xạ Recycler chứa dữ liệu các mặt hàng Sale và set cho phép người dùng kéo ngang để xem các sản phẩm có trong RecycleView
        recycler_Sale = findViewById(R.id.recycler_best_sale);
        recycler_Sale.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerSale = new LinearLayoutManager(BestSales_Activity.this, RecyclerView.HORIZONTAL, false);
        recycler_Sale.setLayoutManager(layoutManagerSale);

        bestSale_List = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("BestSale");

        sale_adapter = new BestSale_Adapter(BestSales_Activity.this, bestSale_List);

        recycler_Sale.setAdapter(sale_adapter);

        if(user == null)
        {
            Toast.makeText(this, "Please login to view best sale products", Toast.LENGTH_SHORT).show();
        }
        else
        {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Sale shoppingCart = snapshot.getValue(Sale.class);
                    shoppingCart.setProductName(snapshot.child("productName").getValue().toString());
                    bestSale_List.add(shoppingCart);
                    sale_adapter.notifyItemInserted(bestSale_List.size());
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Sale shoppingCart = snapshot.getValue(Sale.class);
                    shoppingCart.setProductName(snapshot.child("productName").getValue().toString());
                    bestSale_List.remove(shoppingCart);
                    sale_adapter.notifyDataSetChanged();
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