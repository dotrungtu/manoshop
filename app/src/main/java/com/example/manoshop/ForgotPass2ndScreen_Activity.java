package com.example.manoshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manoshop.R;

public class ForgotPass2ndScreen_Activity extends AppCompatActivity {

    TextView txt_phone_number, txt_mail;
    Button btn_via_sms, btn_via_mail;
    private ImageView img_ic_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass2nd_screen_);

        //Button
        btn_via_mail = findViewById(R.id.btn_via_mail);
        btn_via_sms = findViewById(R.id.btn_via_sms);

        //Text View
        txt_mail = findViewById(R.id.txt_via_mail);
        txt_phone_number = findViewById(R.id.txt_via_sms);

        //Image View
        img_ic_close = findViewById(R.id.img_btn_back_2nd);
        img_ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotPass_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_back, R.anim.slide_in_left_back);
            }
        });

        btn_via_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Verify_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
            }
        });

        btn_via_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Verify_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
            }
        });

    }
}
