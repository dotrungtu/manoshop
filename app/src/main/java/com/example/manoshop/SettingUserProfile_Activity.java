package com.example.manoshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manoshop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingUserProfile_Activity extends AppCompatActivity {

    private ImageView img_ic_back, img_show_dia_setting, img_user_profile;
    TextView txt_profile_phoneNumber, txt_profile_email, txt_profile_fullName, txt_profile_address;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    private static final int PICK_IMAGE_REQUEST = 1;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //storage
    StorageReference storageReference;
    StorageReference uploadingFileRef;


    StorageTask uploadTask;
    String cameraPermission[];
    String storagePermission[];

    Uri imageUri;
    //for checking profile or cover photo
    String profileOrCoverPhoto;

    Button btn_dia_change_name, btn_dia_change_address, btn_dia_change_phone;
    String storagePath = "men_wear/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user_profile_);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        storageReference = FirebaseStorage.getInstance().getReference();
//        //gán chuỗi cho quyền truy cập
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        img_ic_back = findViewById(R.id.img_btn_profile_back);
        img_ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right_back, R.anim.slide_in_left_back);
            }
        });

        img_show_dia_setting = findViewById(R.id.img_profile_drop_menu);
        img_show_dia_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSetting();
            }
        });

        txt_profile_email = findViewById(R.id.txt_profile_mail);
        txt_profile_phoneNumber = findViewById(R.id.txt_profile_phone);
        txt_profile_fullName = findViewById(R.id.txt_profile_user_name);
        txt_profile_address = findViewById(R.id.txt_profile_address);

        img_user_profile = findViewById(R.id.img_user_profile);


        progressBar = findViewById(R.id.progressBar_SettingUser);
        progressBar.setVisibility(View.INVISIBLE);
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data get

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String fullName = "" + ds.child("fullName").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phoneNumber = "" + ds.child("phoneNumber").getValue();
                    String imgUrl = "" + ds.child("imgUrl").getValue();
                    String address = "" + ds.child("address").getValue();

                    //Set data
                    txt_profile_email.setText(email);
                    txt_profile_fullName.setText(fullName);
                    txt_profile_phoneNumber.setText(phoneNumber);
                    txt_profile_address.setText(address);

                    //Avatar
                    try {
                        Picasso.get()
                                .load(imgUrl)
                                .into(img_user_profile);
                    } catch (Exception e) {
                        Picasso.get()
                                .load(R.drawable.lollipop_girl_aes)
                                .into(img_user_profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //Phương thức show các dialog để lựa chọn thay đổi thông tin cá nhân
    private void showDialogSetting() {
        Dialog showDia = new Dialog(this);
        showDia.setContentView(R.layout.dialog_setting_option);

        Button btn_setting_change_ava = showDia.findViewById(R.id.btn_dia_change_ava);
        Button btn_setting_change_name = showDia.findViewById(R.id.btn_dia_change_name);
        Button btn_setting_change_phone = showDia.findViewById(R.id.btn_dia_change_phone);
        Button btn_setting_change_address = showDia.findViewById(R.id.btn_dia_change_address);
        Button btn_dia_change_password = showDia.findViewById(R.id.btn_dia_change_password);

        //Vào thư viện cho phép người dùng chọn ảnh mới
        btn_setting_change_ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileOrCoverPhoto = "imgUrl";
                showImageChange();
            }
        });

        //Show 1 dialog mới để nhập tên
        btn_setting_change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangeName("fullName");
            }
        });

        //Show 1 dialog để nhập đố điện thoại
        btn_setting_change_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangePhone("phoneNumber");
            }
        });

        //Show 1 dialog để nhập địa chỉ (có gợi ý địa chỉ khi người dùng nhập)
        btn_setting_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangeAddress("address");
            }
        });
        btn_dia_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword("password");
            }
        });

        showDia.show();
    }

    //Phương thức thay đổi tên của khách hàng (USER)
    private void showDialogChangeName(final String key) {
        View view = LayoutInflater.from(SettingUserProfile_Activity.this).inflate(R.layout.dialog_change_name, null);
        final TextInputLayout textInputLayoutChangName = view.findViewById(R.id.txt_ip_profile_change_fullname);
        btn_dia_change_name = view.findViewById(R.id.btn_dia_change_name_done);

        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingUserProfile_Activity.this);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        btn_dia_change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = textInputLayoutChangName.getEditText().getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    progressBar.setVisibility(View.VISIBLE);
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);
                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SettingUserProfile_Activity.this, "Changed name!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SettingUserProfile_Activity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(SettingUserProfile_Activity.this, "Please enter your name " + key, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Phương thức đổi mật khẩu của khách hàng (USER)
    private void changePassword(final String key) {
        Intent intent = new Intent(SettingUserProfile_Activity.this, ChangePassword_Activity.class);
        startActivity(intent);
    }

    //Phương thức thay đổi số điện thoại của khách hàng (USER)
    private void showDialogChangePhone(final String key) {
        View view = LayoutInflater.from(SettingUserProfile_Activity.this).inflate(R.layout.dialog_change_phone, null);

        final TextInputLayout textInputLayoutChangPhone = view.findViewById(R.id.txt_ip_profile_change_phonenumber);
        btn_dia_change_phone = view.findViewById(R.id.btn_dia_change_phone_done);

        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingUserProfile_Activity.this);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        btn_dia_change_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = textInputLayoutChangPhone.getEditText().getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    progressBar.setVisibility(View.VISIBLE);
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SettingUserProfile_Activity.this, "Changed phone number", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SettingUserProfile_Activity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(SettingUserProfile_Activity.this, "Please enter your phone number " + key, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Phương thức thay đổi địa chỉ của khách hàng (USER)
    private void showDialogChangeAddress(final String key) {
        View view = LayoutInflater.from(SettingUserProfile_Activity.this).inflate(R.layout.dialog_change_address, null);

        final TextInputLayout textInputLayoutChangAddress = view.findViewById(R.id.txt_ip_profile_change_address);
        btn_dia_change_address = view.findViewById(R.id.btn_dia_change_address_done);
        ;

        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingUserProfile_Activity.this);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        btn_dia_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = textInputLayoutChangAddress.getEditText().getText().toString().trim();

                if (!TextUtils.isEmpty(value)) {
                    progressBar.setVisibility(View.VISIBLE);
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SettingUserProfile_Activity.this, "Changed address", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SettingUserProfile_Activity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(SettingUserProfile_Activity.this, "Please enter your address " + key, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //Các phương thức phía dưới cho phép truy cập vào camera máy ảo hoặc điện thoại của người dùng để lấy hình ảnh
    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(SettingUserProfile_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(SettingUserProfile_Activity.this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(SettingUserProfile_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void showImageChange() {
        String options[] = {"Camera" + "\n", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingUserProfile_Activity.this);
        builder.setTitle("Chọn hình ảnh từ");
        //set items cho dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //handle dialog item clicks
                if (which == 0) {
                    //Edit Avatar Click
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    //Edit Name Click
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGalerry();
                    }
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                //Chọn hình trong camera, kiểm tra xem camera có được phép truy cập hay không
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        //permission enable
                        pickFromCamera();
                    } else {
                        Toast.makeText(SettingUserProfile_Activity.this, "Vui lòng cho phép Camera và Bộ nhớ quyền truy cập", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                //Chọn hình trong storage kiểm tra xem storage có được phép truy cập hay không
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        //permission enable
                        pickFromGalerry();
                    } else {
                        Toast.makeText(SettingUserProfile_Activity.this, "Vui lòng cho phép bộ nhớ quyền truy cập", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                imageUri = data.getData();

                uploadProfileCoverPhoto(imageUri);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                uploadProfileCoverPhoto(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {
        String filePathAndName = storagePath + " " + profileOrCoverPhoto + " _ " + user.getUid();
        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        progressBar.setVisibility(View.VISIBLE);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri dowloadUri = uriTask.getResult();
                        //check if image is upload or not
                        if (uriTask.isSuccessful()) {
                            //image uploaded
                            HashMap<String, Object> results = new HashMap<>();
                            results.put(profileOrCoverPhoto, dowloadUri.toString());
                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(SettingUserProfile_Activity.this, "Ảnh đã được cập nhật", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(SettingUserProfile_Activity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            //error
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(SettingUserProfile_Activity.this, "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SettingUserProfile_Activity.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        //put image uri
        imageUri = SettingUserProfile_Activity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGalerry() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    public void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

        } else {
            Intent intent = new Intent(SettingUserProfile_Activity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        checkUserStatus();
        super.onStart();
    }
}
