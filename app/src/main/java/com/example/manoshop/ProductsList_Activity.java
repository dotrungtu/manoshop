package com.example.manoshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.manoshop.R;
import com.example.manoshop.adapter.Tab_Layout_Adapter;
import com.google.android.material.tabs.TabLayout;

public class ProductsList_Activity extends AppCompatActivity {


    // Tab Layout
    ViewPager viewPager;
    TabLayout tabLayout;


    private ImageView img_btn_list_back, img_btn_product_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_products_list_);

        img_btn_list_back = findViewById(R.id.btnback_list);
        img_btn_list_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsList_Activity.super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_right_back, R.anim.slide_in_left_back);
            }
        });

        img_btn_product_cart = findViewById(R.id.imgCartProduct_List);
        img_btn_product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsList_Activity.this, ShoppingCart_Activity.class));
                overridePendingTransition(R.anim.zoom_in_to_activity, R.anim.zoom_out_to_activity);
            }
        });
        // Tab Layout item
        viewPager = findViewById(R.id.viewpager_list);
        tabLayout = findViewById(R.id.tablayout_list);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Tab_Layout_Adapter adapter = new Tab_Layout_Adapter(fragmentManager);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        //Tạo đường line màu đỏ cách giữa các tablayout
        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.color_line_tablayout));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
        // Animation tab layout
        tabLayout.setTranslationX(300);
        tabLayout.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(300).start();
    }
}
