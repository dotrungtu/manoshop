package com.example.manoshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.manoshop.R;
import com.example.manoshop.adapter.FeaturedLocations_Adapter;
import com.example.manoshop.adapter.Product_Adapter;
import com.example.manoshop.model.Featured;
import com.example.manoshop.model.Product;
import com.example.manoshop.model.Slider;
import com.example.manoshop.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final float END_SCALE = 0.7f;
    ConstraintLayout anim_navigation;

    ViewFlipper viewFlipper;
    Product_Adapter product_Adapter;
    FeaturedLocations_Adapter feature_Adapter;
    RecyclerView productRecycler;
    List<Slider> slideLists;
    private Button btn_to_toppicks, btn_to_new_products, btn_to_best_sales, btn_to_request_login, btn_dialog_check_out_cancel;
    DrawerLayout dLayout;
    NavigationView nView;
    private ImageView ic_show_navigation, ic_to_shopping_cart, ic_view_feature, img_btn_view_men, img_btn_view_women, img_btn_view_kid;
    Animation anim;

    RecyclerView recyclerView_Men;
    RecyclerView recyclerView_Women;
    RecyclerView recyclerView_Kid;
    RecyclerView recyclerView_Featured;

    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceUser;
    ChildEventListener childEventListener;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Lấy dữ liệu user từ Authen và Realtime Database (User)
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("User");

        //Ánh xạ
        recyclerView_Kid = findViewById(R.id.product_recycler_kid);
        recyclerView_Men = findViewById(R.id.product_recycler_men);
        recyclerView_Women = findViewById(R.id.product_recycler_women);
        recyclerView_Featured = findViewById(R.id.product_recycler_feature);

        // Navigation View
        dLayout = findViewById(R.id.drawerlayout);
        nView = findViewById(R.id.naviga_view);
        nView.bringToFront();
        nView.setNavigationItemSelectedListener(this);
        nView.setCheckedItem(R.id.nav_home);

        firebaseAuth = FirebaseAuth.getInstance();


        //Check nếu không có User thì trong Navigation View chỉ hiện những menu Home - WishList - Join Us
        if (firebaseUser != null) {
            Menu menuNav = nView.getMenu();
            MenuItem nav_join_us = menuNav.findItem(R.id.nav_joinus);
            nav_join_us.setVisible(false);
        }
        //Nếu có User thì trong Navigation View sẽ hiện tất cả menu Home - Bill - WishList - Information - Logout
        else {
            Menu menuNav = nView.getMenu();
            MenuItem nav_log_out = menuNav.findItem(R.id.nav_logout);
            MenuItem nav_info = menuNav.findItem(R.id.nav_profile);
            MenuItem nav_bill = menuNav.findItem(R.id.nav_bill);
            nav_log_out.setVisible(false);
            nav_info.setVisible(false);
            nav_bill.setVisible(false);
        }
        Menu menuNav = nView.getMenu();
        MenuItem nav_fav = menuNav.findItem(R.id.nav_fav);

        //Nav - WishList
        //Sau khi ánh xạ menu WishList ở trên thì ở đây ta sẽ set sự kiện nếu không có user sẽ cho hiện 1 dialog yêu cầu đăng nhập
        //Và sau khi đăng nhập xong sẽ cho phép click vào xem các mặt hàng yêu thích
        nav_fav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (firebaseUser == null) {
                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_request, null);
                    btn_to_request_login = view.findViewById(R.id.btn_res_to_sign_in);
                    btn_dialog_check_out_cancel = view.findViewById(R.id.btn_dialog_check_out_cancel);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(view);

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
                    overridePendingTransition(R.anim.zoom_in_to_activity, R.anim.zoom_out_to_activity);
                }
                return true;
            }
        });

        //Ánh xạ dữ liệu Header của phần Navigation View và truyền dữ liệu User mới đăng nhập vào những textView tương ứng
        View header = nView.getHeaderView(0);
        final TextView tvEmailUser = header.findViewById(R.id.txt_email_user);
        final TextView tvUserName = header.findViewById(R.id.txt_nav_name);
        final ImageView imgUser = header.findViewById(R.id.img_nav_user);

        //Nếu có User thì sẽ so sánh phần Email trong Realtime Database và Authencation
        //Nếu email của User đó giống nhau sẽ cho phép lấy dữ liệu từ Realtime Database của User đó
        if(firebaseUser != null)
        {
            Query query = databaseReferenceUser.orderByChild("email").equalTo(firebaseUser.getEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check until required data get

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get data
                        String fullName = "" + ds.child("fullName").getValue();
                        String email = "" + ds.child("email").getValue();
                        String imgUrl = "" + ds.child("imgUrl").getValue();

                        //Set data
                        tvEmailUser.setText(email);
                        tvUserName.setText(fullName);

                        //Avatar
                        try {
                            Picasso.get()
                                    .load(imgUrl)
                                    .into(imgUser);
                        } catch (Exception e) {
                            Picasso.get()
                                    .load(R.drawable.lollipop_girl_aes)
                                    .into(imgUser);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            Toast.makeText(MainActivity.this, "No have user", Toast.LENGTH_SHORT).show();
        }


        //Show navigation
        ic_show_navigation = (ImageView) findViewById(R.id.ic_show_navigation);
        ic_show_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dLayout.isDrawerOpen(GravityCompat.START)) {
                    dLayout.closeDrawer(GravityCompat.START);
                } else {
                    dLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        ic_view_feature = findViewById(R.id.img_btn_view_all_feature);
        ic_view_feature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Feature_Activity.class));
            }
        });

        //Animation navigation view
//        dLayout.setScrimColor(getResources().getColor(R.color.anim_nagigation));
        anim_navigation = findViewById(R.id.anim_navigation);
        dLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final float diffScaleOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaleOffset;
                anim_navigation.setScaleX(offsetScale);
                anim_navigation.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = anim_navigation.getWidth() * diffScaleOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                anim_navigation.setTranslationX(xTranslation);
            }

        });

        //Intent to shopping cart
        ic_to_shopping_cart = findViewById(R.id.img_to_shopping_cart);
        ic_to_shopping_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser != null) {
                    startActivity(new Intent(getApplicationContext(), ShoppingCart_Activity.class));
                    overridePendingTransition(R.anim.zoom_in_to_activity, R.anim.zoom_out_to_activity);
                }
                else if(firebaseUser == null){
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_request, null);
                    btn_to_request_login = view.findViewById(R.id.btn_res_to_sign_in);
                    btn_dialog_check_out_cancel = view.findViewById(R.id.btn_dialog_check_out_cancel);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(view);

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

        // Category
        btn_to_toppicks = (Button) findViewById(R.id.btn_to_toppicks);
        btn_to_toppicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TopPicks_Activity.class));
            }
        });
        //Nút xử lý sự kiện chuyển tới các mặt hàng trong phần Best Sale
        btn_to_best_sales = findViewById(R.id.btn_to_sale);
        btn_to_best_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), BestSales_Activity.class));
            }
        });

        //Nút xử lý sự kiện chuyển tới các mặt hàng trong phần New Products
        btn_to_new_products = findViewById(R.id.btn_to_new_products);
        btn_to_new_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NewProducts_Activity.class));
            }
        });


        //Ánh xạ List<Product> slideLists = new ArrayList();
        slideLists = new ArrayList<>();

        //image view all products
        img_btn_view_men = findViewById(R.id.img_btn_view_more_men);
        img_btn_view_women = findViewById(R.id.img_btn_view_more_women);
        img_btn_view_kid = findViewById(R.id.img_btn_view_more_kid);

        //Xử lý sự kiện chuyển hướng tới các mặt hàng trong phần Product List
        img_btn_view_women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProductsList_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
            }
        });


        //Xử lý sự kiện chuyển hướng tới các mặt hàng trong phần Product List
        img_btn_view_kid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProductsList_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
            }
        });

        //Xử lý sự kiện chuyển hướng tới các mặt hàng trong phần Product List
        img_btn_view_men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProductsList_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
            }
        });

        //Slideshow load img từ Firebase
        viewFlipper = (ViewFlipper) findViewById(R.id.slideImg);

        //RecyclerView Featured trong MainACtivity(Show list product FeaturedClothes)
        final List<Featured> productList_Featured;

        recyclerView_Featured.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerFeatured = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
        recyclerView_Featured.setLayoutManager(layoutManagerFeatured);

        productList_Featured = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("FeaturedClothes");

        feature_Adapter = new FeaturedLocations_Adapter(MainActivity.this, productList_Featured);

        recyclerView_Featured.setAdapter(feature_Adapter);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Featured featured = snapshot.getValue(Featured.class);
                featured.setProductID(snapshot.child("productID").getValue().toString());
                productList_Featured.add(featured);
                feature_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Featured featured = snapshot.getValue(Featured.class);
                featured.setProductID(snapshot.child("productID").getValue().toString());
                productList_Featured.remove(featured);
                product_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);


        //RecyclerView Women trong MainACtivity(Show list product WomenClothes)
        final List<Product> productList_Women;

        recyclerView_Women.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerWomen = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
        recyclerView_Women.setLayoutManager(layoutManagerWomen);

        productList_Women = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("WomenClothes");

        product_Adapter = new Product_Adapter(MainActivity.this, productList_Women);

        recyclerView_Women.setAdapter(product_Adapter);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product womenClothes = snapshot.getValue(Product.class);
                womenClothes.setProductName(snapshot.child("productName").getValue().toString());
                productList_Women.add(womenClothes);
                product_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Product womenClothes = snapshot.getValue(Product.class);
                womenClothes.setProductName(snapshot.child("productName").getValue().toString());
                productList_Women.remove(womenClothes);
                product_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);

        //RecyclerView Men trong MainACtivity(Show list product MenClothes)
        final List<Product> productList_Men;

        recyclerView_Men.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerMen = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
        recyclerView_Men.setLayoutManager(layoutManagerMen);

        productList_Men = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("MenClothes");

        product_Adapter = new Product_Adapter(MainActivity.this, productList_Men);

        recyclerView_Men.setAdapter(product_Adapter);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product menClothes = snapshot.getValue(Product.class);
                menClothes.setProductName(snapshot.child("productName").getValue().toString());
                productList_Men.add(menClothes);
                product_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Product menClothes = snapshot.getValue(Product.class);
                menClothes.setProductName(snapshot.child("productName").getValue().toString());
                productList_Men.remove(menClothes);
                product_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);

        //RecyclerView Kid trong MainACtivity(Show list product KidClothes)
        final List<Product> productList_Kid;

        recyclerView_Kid.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerKid = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
        recyclerView_Kid.setLayoutManager(layoutManagerKid);

        productList_Kid = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("KidClothes");

        product_Adapter = new Product_Adapter(MainActivity.this, productList_Kid);

        recyclerView_Kid.setAdapter(product_Adapter);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product kidClothes = snapshot.getValue(Product.class);
                kidClothes.setProductName(snapshot.child("productName").getValue().toString());
                productList_Kid.add(kidClothes);
                product_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Product kidClothes = snapshot.getValue(Product.class);
                kidClothes.setProductName(snapshot.child("productName").getValue().toString());
                productList_Kid.remove(kidClothes);
                product_Adapter.notifyDataSetChanged();
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

    //Slider
    public void flipperImages(int slide) {
        ImageView imgView = new ImageView(this);
        imgView.setBackgroundResource(slide);

        viewFlipper.addView(imgView);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        //Animation

    }

    //Phương thức click vào từng menu có trong Navigation View
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                Toast.makeText(MainActivity.this, "You have logged out", Toast.LENGTH_SHORT).show();
                                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
                            }
                        });
                break;
            case R.id.nav_joinus:
                startActivity(new Intent(getApplicationContext(), JoinUs_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
                break;
            case R.id.nav_bill:
                startActivity(new Intent(getApplicationContext(), Bill_ListBill_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
                break;
            case R.id.nav_fav:
                startActivity(new Intent(getApplicationContext(), WishList_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
                break;
            case R.id.nav_profile:
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Nếu có user sẽ cho phép lấy dữ liệu User đó từ Realtime Database
                        //Và sau đó dùng Intent để chuyển dữ liệu của User từ MainActivity qua SettingUserProfile_Activity
                        if (firebaseUser != null) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                User user = dataSnapshot.getValue(User.class);
                                String email = user.getEmail();
                                String phoneNumber = user.getPhoneNumber();
                                String fullName = user.getFullName();
                                String address = user.getAddress();

                                Intent intentProfile = new Intent(MainActivity.this, SettingUserProfile_Activity.class);
                                intentProfile.putExtra("email", email);
                                intentProfile.putExtra("phoneNumber", phoneNumber);
                                intentProfile.putExtra("fullName", fullName);
                                intentProfile.putExtra("address", address);
                                startActivity(intentProfile);
                                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "No have user", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }

        dLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //Phương thức quay trở lại activity trước đó
    @Override
    public void onBackPressed() {

        if (dLayout.isDrawerOpen(GravityCompat.START)) {
            dLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
