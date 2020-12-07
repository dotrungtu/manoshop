package com.example.manoshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.manoshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ChangePassword_Activity extends AppCompatActivity {

    TextInputLayout txt_ip_change_cur_pass, txt_ip_change_new_pass, txt_ip_change_confirm_pass;
    private ImageView img_ic_back;
    Button btn_change_password;
    ProgressBar progressBar_ChangePassword;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_);
        firebaseAuth = FirebaseAuth.getInstance();
        //Text input layout
        txt_ip_change_new_pass = findViewById(R.id.txt_ip_change_new_pass);
        txt_ip_change_confirm_pass = findViewById(R.id.txt_ip_change_confirm_pass);
        txt_ip_change_cur_pass = findViewById(R.id.txt_ip_change_current_pass);
        btn_change_password = findViewById(R.id.btn_change_password);
        progressBar_ChangePassword = findViewById(R.id.progressBar_ChangePassword);

        progressBar_ChangePassword.setVisibility(View.INVISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        //img back
        img_ic_back = findViewById(R.id.img_btn_change_back);
        img_ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate data
                String oldPassword = txt_ip_change_cur_pass.getEditText().getText().toString().trim();
                String newPassword = txt_ip_change_new_pass.getEditText().getText().toString().trim();
                String confirmPassword = txt_ip_change_confirm_pass.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(oldPassword)) {
                    Toast.makeText(ChangePassword_Activity.this, "Nhập vào mật khẩu hiện tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPassword.length() < 6) {
                    Toast.makeText(ChangePassword_Activity.this, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (confirmPassword.length() < 6) {
                    Toast.makeText(ChangePassword_Activity.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ChangePassword_Activity.this, "Mật khẩu không trùng khớp với mật khẩu mới!", Toast.LENGTH_SHORT).show();
                }
                updatePassword(oldPassword, newPassword);
            }
        });
    }
    private void updatePassword(String oldPassword, final String newPassword) {
        //get current user
        progressBar_ChangePassword.setVisibility(View.VISIBLE);
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        //before changing password re-authenticate the user
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                String newPass = txt_ip_change_new_pass.getEditText().getText().toString().trim();
                                HashMap<String, Object> result = new HashMap<>();
                                result.put("password", newPass);
                                databaseReference.child(user.getUid()).updateChildren(result)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressBar_ChangePassword.setVisibility(View.GONE);
                                                Toast.makeText(ChangePassword_Activity.this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressBar_ChangePassword.setVisibility(View.INVISIBLE);
                                                Toast.makeText(ChangePassword_Activity.this, "Lỗi " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                Intent intent = new Intent(ChangePassword_Activity.this, SettingUserProfile_Activity.class);
                                progressBar_ChangePassword.setVisibility(View.GONE);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right_back, R.anim.slide_in_left_back);
                                Toast.makeText(ChangePassword_Activity.this, "Đã thay đổi mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressBar_ChangePassword.setVisibility(View.INVISIBLE);
                                Toast.makeText(ChangePassword_Activity.this, "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    progressBar_ChangePassword.setVisibility(View.INVISIBLE);
                    Toast.makeText(ChangePassword_Activity.this, "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


