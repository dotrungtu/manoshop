package com.example.manoshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.manoshop.R;
import com.google.android.material.textfield.TextInputLayout;


public class SetNewPassword_Activity extends AppCompatActivity {

    Button btn_update_password;
    TextInputLayout txt_set_new_pass, txt_set_re_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password_);

        txt_set_new_pass = findViewById(R.id.txt_ip_set_new_pass);
        txt_set_re_pass = findViewById(R.id.txt_ip_new_confirm_pass);

        btn_update_password = findViewById(R.id.btn_update_password);
        btn_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validatePassword() | !validateRepass()) {
                    return;
                }
                startActivity(new Intent(getApplicationContext(), Login_Activity.class));
            }
        });
    }

    private boolean validateRepass() {
        String validate = txt_set_re_pass.getEditText().getText().toString().trim();
        String checkMatch = txt_set_new_pass.getEditText().getText().toString().trim();

        if (validate.isEmpty()) {
            txt_set_re_pass.setError("Field can not empty!");
            return false;
        } else if (!validate.matches(checkMatch)) {
            txt_set_re_pass.setError("Password do not match!");
            return false;
        } else {
            txt_set_re_pass.setError(null);
            txt_set_re_pass.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String validate = txt_set_new_pass.getEditText().getText().toString().trim();
        String checkPass = "^" +
                "(?=.*[a-zA-Z])" + //bất kì ký tự nào
                "(?=.*[!@#$%^&+=])" + //ít nhất một ký tự đặc biệt
                "(?=\\S+$)" + //không khoảng trắng
                ".{4,}" + //ít nhất 4 ký tự
                "$";

        if (validate.isEmpty()) {
            txt_set_new_pass.setError("Field can not empty!");
            return false;
        } else if (!validate.matches(checkPass)) {
            txt_set_new_pass.setError("Pass must contain at least 4 character, 1 special character and no white spaces");
            return false;
        } else {
            txt_set_new_pass.setError(null);
            txt_set_new_pass.setErrorEnabled(false);
            return true;
        }
    }

}
