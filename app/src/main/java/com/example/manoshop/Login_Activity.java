package com.example.manoshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.manoshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {

    Button btn_to_signup, btn_forgot, btn_sign_in;
    private ImageView img_btn_login_back;
    TextInputLayout txt_ip_log_mail, txt_ip_log_pass;
    ProgressBar progressBar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_);
        //(Ánh xạ các Widget)
        //Text input layout
        txt_ip_log_mail = findViewById(R.id.txt_ip_log_mail);
        txt_ip_log_pass = findViewById(R.id.txt_ip_log_pass);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.sign_in_progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        //Button
        btn_sign_in = findViewById(R.id.btn_signin);
        btn_to_signup = (Button) findViewById(R.id.btn_to_signup);
        btn_forgot = (Button) findViewById(R.id.btn_to_forgot);
        //Image Button
        img_btn_login_back = findViewById(R.id.img_btn_login_back);

        //Nút chuyển hướng qua màn hình đăng ký nếu khách hàng chưa có tài khoản
        btn_to_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
            }
        });
        //Nút chuyển hướng qua màn hình quên mật khẩu
        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPass_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
            }
        });
        //Nút chuyển hướng về phần chọn đăng ký hoặc đăng nhập
        img_btn_login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), JoinUs_Activity.class));
                overridePendingTransition(R.anim.zoom_in_back_join, R.anim.zoom_out_back_join);
            }
        });

        //Nút xử lý sự kiện đăng nhập
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateMail())
                {
                    return;
                }
                else
                {
                    checkEmailAndPassword();
                }
            }
        });
    }
    //Validate (Phương thức bắt lỗi email trong lúc người dùng đăng nhập)
    private boolean validateMail() {
        String validate = txt_ip_log_mail.getEditText().getText().toString().trim();
        String mailForm = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (validate.isEmpty()) {
            txt_ip_log_mail.setError("Field can not empty!");
            return false;
        } else if (!validate.matches(mailForm)) {
            txt_ip_log_mail.setError("Invalid email!");
            return false;
        } else {
            txt_ip_log_mail.setError(null);
            txt_ip_log_mail.setErrorEnabled(false);
            return true;
        }
    }
    //Phương thức kiểm tra đầu vào Email và Password nếu hợp lệ sẽ lấy dữ liệu từ firebase để đăng nhập
    private void checkEmailAndPassword() {
        if(txt_ip_log_mail.getEditText().getText().toString().matches(emailPattern))
        {
            if(txt_ip_log_pass.getEditText().length() >= 8)
            {
                progressBar.setVisibility(View.VISIBLE);
                btn_sign_in.setEnabled(false);
                firebaseAuth.signInWithEmailAndPassword(txt_ip_log_mail.getEditText().getText().toString(), txt_ip_log_pass.getEditText().getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    //Thành công
                                    Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                    Toast.makeText(Login_Activity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    //Lỗi
                                    progressBar.setVisibility(View.INVISIBLE);
                                    btn_sign_in.setEnabled(true);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(Login_Activity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else
            {
                Toast.makeText(Login_Activity.this, "Wrong password, try again!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(Login_Activity.this, "Invalid email!", Toast.LENGTH_SHORT).show();
        }
    }
}
