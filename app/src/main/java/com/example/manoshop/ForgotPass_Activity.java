package com.example.manoshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manoshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass_Activity extends AppCompatActivity {
    private Button btn_next_forgot ;
    private ImageView img_ic_close;
    TextInputLayout txt_ip_mail_forgot;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_pass_);

        txt_ip_mail_forgot = findViewById(R.id.txt_ip_reset_mail);

        firebaseAuth = FirebaseAuth.getInstance();
        btn_next_forgot = findViewById(R.id.btn_next_forgot);
        btn_next_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateMail()) {
                    return;
                }
                String email = txt_ip_mail_forgot.getEditText().getText().toString();
                beginRecover(email);
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
            }
        });

        //Animation
        btn_next_forgot.setTranslationY(-280);
        btn_next_forgot.animate().translationY(0).setDuration(1000).setStartDelay(300).start();

        img_ic_close = findViewById(R.id.img_btn_close_forgot_pass);
        img_ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_back, R.anim.slide_in_left_back);
            }
        });
    }

    private boolean validateMail() {
        String validate = txt_ip_mail_forgot.getEditText().getText().toString().trim();
        String mailForm = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (validate.isEmpty()) {
            txt_ip_mail_forgot.setError("Field can not empty!");
            return false;
        } else if (!validate.matches(mailForm)) {
            txt_ip_mail_forgot.setError("Invalid email!");
            return false;
        } else {
            txt_ip_mail_forgot.setError(null);
            txt_ip_mail_forgot.setErrorEnabled(false);
            return true;
        }
    }

    private void beginRecover(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(ForgotPass_Activity.this, Login_Activity.class);
                    startActivity(intent);
                    Toast.makeText(ForgotPass_Activity.this, "Please check your mailbox", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPass_Activity.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //get and show lỗi riêng
                Toast.makeText(ForgotPass_Activity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
