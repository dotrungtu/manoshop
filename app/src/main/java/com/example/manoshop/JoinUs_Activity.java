package com.example.manoshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.manoshop.R;

public class JoinUs_Activity extends AppCompatActivity {

    Button btn_wlc_sign_in, btn_wlc_sign_up;
    private ImageView img_wlc_logo, img_ic_close_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join_us_);


        //Button
        btn_wlc_sign_in = findViewById(R.id.btn_wlc_sign_in);
        btn_wlc_sign_up = findViewById(R.id.btn_wlc_sign_up);

        //Image View
        img_wlc_logo = findViewById(R.id.img_wlc_logo);
        img_ic_close_join = findViewById(R.id.img_btn_close_join);
        img_ic_close_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right_back, R.anim.slide_in_left_back);
            }
        });


        //Animation
        btn_wlc_sign_up.setTranslationX(300);
        btn_wlc_sign_in.setTranslationX(-300);
        img_wlc_logo.setTranslationY(-300);
        btn_wlc_sign_in.animate().translationX(0).setDuration(1000).setStartDelay(300).start();
        btn_wlc_sign_up.animate().translationX(0).setDuration(1000).setStartDelay(300).start();
        img_wlc_logo.animate().translationY(0).setDuration(1000).setStartDelay(300).start();


    }

    public void callSignInScreen(View view) {
        startActivity(new Intent(getApplicationContext(), Login_Activity.class));
        overridePendingTransition(R.anim.zoom_in_join_call_sign_in, R.anim.zoom_out_join_call_sign_in);
     }

    public void callSigUpnScreen(View view) {
        startActivity(new Intent(getApplicationContext(), Register_Activity.class));
        overridePendingTransition(R.anim.zoom_in_join_call_sign_up, R.anim.zoom_out_join_call_sign_up);
    }
}
