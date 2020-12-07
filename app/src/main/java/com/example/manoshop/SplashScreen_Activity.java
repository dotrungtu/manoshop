package com.example.manoshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manoshop.R;

public class SplashScreen_Activity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView imgLogo;
    TextView txtLogo, hashtag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen_);

        imgLogo = findViewById(R.id.imgLogo);
        txtLogo = findViewById(R.id.txtLogo);
        hashtag = findViewById(R.id.txtHashtag);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_anim_logo);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim_logo);

        imgLogo.setAnimation(topAnim);
        txtLogo.setAnimation(bottomAnim);
        hashtag.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right_to_activity,R.anim.slide_in_left_to_activity);
                finish();
            }
        }, 3000);
    }
}
