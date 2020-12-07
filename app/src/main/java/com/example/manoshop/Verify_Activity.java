package com.example.manoshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.example.manoshop.R;

public class Verify_Activity extends AppCompatActivity {

    PinView verify_OTP;
    ImageView ic_close_verify;
    Button btn_verify_code;
    TextView txt_phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_);

        //Text view phone number
        txt_phoneNumber = findViewById(R.id.txt_phone_number_verify);

        //Pin View Verify
        verify_OTP = findViewById(R.id.pin_verify);

        String _getMail = getIntent().getStringExtra("emal");
        String _getName = getIntent().getStringExtra("fullName");
        String _getPass = getIntent().getStringExtra("password");
        String _getPhoneEntered =  getIntent().getStringExtra("phoneNumber");

        txt_phoneNumber.setText(_getPhoneEntered);

        //Gửi OTP đến số điện thoại
//        sendVerificationCode(_getPhoneEntered);

        //Button
        btn_verify_code = findViewById(R.id.btn_verify_code);
        btn_verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SetNewPassword_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
            }
        });

        //Image View ic close
        ic_close_verify = findViewById(R.id.img_btn_close_verify);
        ic_close_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right_back, R.anim.slide_in_left_back);
            }
        });
    }

    //Firebase Phone Number

}
