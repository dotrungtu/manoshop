package com.example.manoshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.manoshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Register_Activity extends AppCompatActivity {

    Button btn_to_signin, btn_next_sign_up;
    TextInputLayout txt_ip_mail_sign_up, txt_ip_name, txt_ip_pass_sign_up, txt_ip_re_pass, txt_ip_phone;
    ProgressBar progressBarRegister;
    FirebaseAuth firebaseAuth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_);

        //Text input layout
        //Ánh xạ
        txt_ip_mail_sign_up = findViewById(R.id.txt_ip_email_sign_up);
        txt_ip_name = findViewById(R.id.txt_ip_name_sign_up);
        txt_ip_pass_sign_up = findViewById(R.id.txt_ip_pass_sign_up);
        txt_ip_re_pass = findViewById(R.id.txt_ip_re_pass_sign_up);
        txt_ip_phone = findViewById(R.id.txt_ip_phone_sign_up);

        progressBarRegister = findViewById(R.id.progressBar_Register);
        firebaseAuth = FirebaseAuth.getInstance();
        // Button
        //Nút xử lý đăng ký
        btn_next_sign_up = findViewById(R.id.btn_next_sign_up);
        btn_next_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateMail() | !validateName() | !validatePassword() | !validateRepass() | !validatePhone()) {
                    return;
                } else {
                    registerUser();
                }
            }
        });

        //Nút chuyển hướng qua phần đăng nhập
        btn_to_signin = (Button) findViewById(R.id.btn_to_signin);
        btn_to_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_back, R.anim.slide_in_left_back);
            }
        });


    }

    //Phương thức đăng ký User bằng cách sử dụng 2 trường EditText Email và Password
    public void registerUser() {
        if (txt_ip_mail_sign_up.getEditText().getText().toString().matches(emailPattern)) {
            if (txt_ip_pass_sign_up.getEditText().getText().toString().equals(txt_ip_re_pass.getEditText().getText().toString())) {
                progressBarRegister.setVisibility(View.VISIBLE);
                btn_next_sign_up.setEnabled(false);
                firebaseAuth.createUserWithEmailAndPassword(txt_ip_mail_sign_up.getEditText().getText().toString(),
                        txt_ip_pass_sign_up.getEditText().getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String myDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    String uID = firebaseUser.getUid();
                                    String email = txt_ip_mail_sign_up.getEditText().getText().toString();
                                    String fullName = txt_ip_name.getEditText().getText().toString();
                                    String password = txt_ip_pass_sign_up.getEditText().getText().toString();
                                    String phoneNumber = txt_ip_phone.getEditText().getText().toString();

                                    Map<Object, String> userData = new HashMap<>();
                                    userData.put("ID", uID);
                                    userData.put("email", email);
                                    userData.put("fullName", fullName);
                                    userData.put("password", password);
                                    userData.put("phoneNumber", phoneNumber);
                                    userData.put("imgUrl", " ");
                                    userData.put("dateOfCreate", myDate);

                                    //firebase database instance
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    //path to store user named "Users"
                                    DatabaseReference databaseReference = firebaseDatabase.getReference("User");
                                    //put data within hashmap in database
                                    databaseReference.child(uID).setValue(userData);
                                    Toast.makeText(Register_Activity.this, "Sign-up success", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
                                    progressBarRegister.setVisibility(View.GONE);
                                    btn_next_sign_up.setEnabled(false);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right_to_activity, R.anim.slide_in_left_to_activity);
                                    finish();
                                } else {
                                    progressBarRegister.setVisibility(View.INVISIBLE);
                                    btn_next_sign_up.setEnabled(true);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(Register_Activity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                txt_ip_pass_sign_up.getEditText().setError("Wrong password, try again!");
            }
        } else {
            txt_ip_mail_sign_up.getEditText().setError("Wrong email, try again!");
        }
    }

    //Validate
    private boolean validateName() {
        String validate = txt_ip_name.getEditText().getText().toString().trim();
        if (validate.isEmpty()) {
            txt_ip_name.setError("Field can not empty!");
            return false;
        } else {
            txt_ip_name.setError(null);
            txt_ip_name.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateMail() {
        String validate = txt_ip_mail_sign_up.getEditText().getText().toString().trim();
        String mailForm = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (validate.isEmpty()) {
            txt_ip_mail_sign_up.setError("Field can not empty!");
            return false;
        } else if (!validate.matches(mailForm)) {
            txt_ip_mail_sign_up.setError("Invalid email!");
            return false;
        } else {
            txt_ip_mail_sign_up.setError(null);
            txt_ip_mail_sign_up.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String validate = txt_ip_pass_sign_up.getEditText().getText().toString().trim();
        String checkPass = "^" +
                "(?=.*[a-zA-Z])" + //bất kì ký tự nào
                "(?=.*[!@#$%^&+=])" + //ít nhất một ký tự đặc biệt
                "(?=\\S+$)" + //không khoảng trắng
                ".{4,}" + //ít nhất 4 ký tự
                "$";

        if (validate.isEmpty()) {
            txt_ip_pass_sign_up.setError("Field can not empty!");
            return false;
        } else if (!validate.matches(checkPass)) {
            txt_ip_pass_sign_up.setError("Pass must contain at least 4 character, 1 special character and no white spaces");
            return false;
        } else {
            txt_ip_pass_sign_up.setError(null);
            txt_ip_pass_sign_up.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateRepass() {
        String validate = txt_ip_re_pass.getEditText().getText().toString().trim();
        String checkMatch = txt_ip_pass_sign_up.getEditText().getText().toString().trim();

        if (validate.isEmpty()) {
            txt_ip_re_pass.setError("Field can not empty!");
            return false;
        } else if (!validate.matches(checkMatch)) {
            txt_ip_re_pass.setError("Password do not match!");
            return false;
        } else {
            txt_ip_re_pass.setError(null);
            txt_ip_re_pass.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhone() {
        String validate = txt_ip_phone.getEditText().getText().toString().trim();

        if (validate.isEmpty()) {
            txt_ip_phone.setError("Field can not empty!");
            return false;
        } else {
            txt_ip_phone.setError(null);
            txt_ip_phone.setErrorEnabled(false);
            return true;
        }
    }

}
