package com.example.manoshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manoshop.R;
import com.example.manoshop.adapter.ShoppingCart_Adapter;
import com.example.manoshop.model.Cart;
import com.example.manoshop.network.Delete_Product_In_Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart_Activity extends AppCompatActivity implements ShoppingCart_Adapter.onPassingTotalPrice{

    private Button btn_confirm;
    private TextView txt_total;
    private ImageView img_ic_cart_back;
    ShoppingCart_Adapter shoppingCartAdapter;
    RecyclerView recyclerCart;
    DatabaseReference refchildPhone;
    DatabaseReference refUser;
    DatabaseReference refchildAddress;

    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceBill;
    DatabaseReference databaseReferenceUser;
    ChildEventListener childEventListener;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    String currentUserID;

    List<Cart> shoppingList;
    int totalPresent = 0;
    String productAmount;
    String productTotalPrice;
    TextInputLayout txt_ip_dialog_check_out_show_inf_address;
    TextInputLayout txt_ip_dialog_check_out_show_inf_phone;
    Button btn_dialog_check_out_confirm, btn_dialog_check_out_cancel;

    TextView txt_dialog_check_out_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shopping_cart_);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("User");
        recyclerCart = findViewById(R.id.shopping_cart_recycler);

        btn_confirm = (Button) findViewById(R.id.btn_check_out);
        txt_total = (TextView) findViewById(R.id.txt_total);

        txt_total.setText(String.valueOf(totalPresent));



        //Show dialog checkout
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchaseAllOfItems();
            }
        });


        img_ic_cart_back = findViewById(R.id.img_btn_cart_back);
        img_ic_cart_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.zoom_in_back, R.anim.zoom_out_back);
            }
        });


        //Add dữ liệu từ Firebase - Realtime Database vào RecyclerView
        recyclerCart.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerWomen = new LinearLayoutManager(ShoppingCart_Activity.this, RecyclerView.VERTICAL, false);
        recyclerCart.setLayoutManager(layoutManagerWomen);

        shoppingList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("ShoppingCart");

        shoppingCartAdapter = new ShoppingCart_Adapter(ShoppingCart_Activity.this, shoppingList, this::onPassingTotalPrice);


        recyclerCart.setAdapter(shoppingCartAdapter);

        if (user != null) {
            currentUserID = firebaseAuth.getCurrentUser().getUid();
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Cart shoppingCart = snapshot.getValue(Cart.class);
                    shoppingCart.setProductID(snapshot.child("productID").getValue().toString());
                    shoppingList.add(shoppingCart);
                    shoppingCartAdapter.notifyItemInserted(shoppingList.size());

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Cart shoppingCart = snapshot.getValue(Cart.class);
                    shoppingCart.setProductID(snapshot.child("productID").getValue().toString());
                    shoppingList.remove(shoppingCart);
                    shoppingCartAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            databaseReference.child(currentUserID).addChildEventListener(childEventListener);
        }
        else {
            Toast.makeText(this, "Please login to view cart", Toast.LENGTH_SHORT).show();
        }
    }

    //Phương thức cho phép lấy người dùng thanh toán tất cả sản phẩm có trong Giỏ hàng
    private void purchaseAllOfItems() {

        databaseReferenceBill = FirebaseDatabase.getInstance().getReference("BillList");
        final String billID = databaseReferenceBill.push().getKey();

        final String myDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        View view2 = LayoutInflater.from(ShoppingCart_Activity.this).inflate(R.layout.dialog_checkout, null);
        txt_ip_dialog_check_out_show_inf_address = view2.findViewById(R.id.txt_ip_dialog_check_out_show_inf_address);
        txt_ip_dialog_check_out_show_inf_phone = view2.findViewById(R.id.txt_ip_dialog_check_out_show_inf_phone);
        txt_dialog_check_out_total = view2.findViewById(R.id.txt_dialog_check_out_total);
        btn_dialog_check_out_confirm = view2.findViewById(R.id.btn_dialog_check_out_confirm);
        btn_dialog_check_out_cancel = view2.findViewById(R.id.btn_dialog_check_out_cancel);

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ShoppingCart_Activity.this);
        builder.setView(view2);

        final AlertDialog dialog = builder.create();
        dialog.show();

        txt_dialog_check_out_total.setText(txt_total.getText().toString());
        btn_dialog_check_out_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < shoppingList.size(); i++) {
                    Query query = databaseReferenceUser.orderByChild("email").equalTo(user.getEmail());
                    final int position = i;
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //check until required data get
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                //get data
                                String fullName = ds.child("fullName").getValue().toString();
                                String address = txt_ip_dialog_check_out_show_inf_address.getEditText().getText().toString();
                                String phoneNumber = txt_ip_dialog_check_out_show_inf_phone.getEditText().getText().toString();
                                String statusBill = "Đang vận chuyển";

                                final String productNameCart = shoppingList.get(position).getProductName();
                                final String productIDCart = shoppingList.get(position).getProductID();
                                final String imgUrl = shoppingList.get(position).getImgUrl();
                                final String productBrandCart = shoppingList.get(position).getProductBrand();
                                final String productDesCart = shoppingList.get(position).getProductDes();
                                final int productAmountCart = shoppingList.get(position).getProductAmount();
                                final String productSizeCart = shoppingList.get(position).getProductSize();
                                final int productPriceCart = shoppingList.get(position).getTotalPrice();
                                final int totalPrice = Integer.parseInt(txt_total.getText().toString());

                                Map<Object, String> billData = new HashMap<>();
                                billData.put("address", address);
                                billData.put("billID", billID);
                                billData.put("phoneNumber", phoneNumber);
                                billData.put("fullName", fullName);
                                billData.put("dateOfPayment", myDate);
                                billData.put("statusBill", statusBill);
                                billData.put("billTotalPrice", String.valueOf(totalPrice));

                                final Map<Object, String> shoppingCartData = new HashMap<>();
                                shoppingCartData.put("productID", productIDCart);
                                shoppingCartData.put("productName", productNameCart);
                                shoppingCartData.put("productBrand", productBrandCart);
                                shoppingCartData.put("productDes", productDesCart);
                                shoppingCartData.put("productAmount", String.valueOf(productAmountCart));
                                shoppingCartData.put("productSize", productSizeCart);
                                shoppingCartData.put("totalPrice", String.valueOf(productPriceCart));
                                shoppingCartData.put("imgUrl", imgUrl);

                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                //path to store user named "Bill"
                                final DatabaseReference databaseReferenceBill2 = firebaseDatabase.getReference("BillList");
                                databaseReferenceBill2.child(user.getUid()).child(billID).setValue(billData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        databaseReferenceBill2.child(user.getUid()).child(billID).child("Products").child(productIDCart).setValue(shoppingCartData);
                                        String productID = shoppingList.get(position).getProductID();
                                        Task<Void> voidTask = Delete_Product_In_Cart
                                                .deleteProductInCart(productID);
                                        voidTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                shoppingList.remove(shoppingList.get(position).getProductID());
                                                shoppingCartAdapter.notifyItemRemoved(position);
                                                shoppingCartAdapter.notifyItemRangeChanged(position, shoppingList.size());

                                                Intent intent = new Intent(ShoppingCart_Activity.this, MainActivity.class);
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.zoom_in_back, R.anim.zoom_out_back);
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        btn_dialog_check_out_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    //Phương thức lấy dữ liệu từ ShoppingCart_Adapter bằng cách dùng interface
    private Map<String, Integer> cartedProducts = new HashMap<>() ;

    @Override
    public void onPassingTotalPrice(String productID, int price) {
        cartedProducts.put(productID, price);

        int totalPrice = 0;
        for (Map.Entry<String, Integer> entry : cartedProducts.entrySet()) {
            totalPrice += entry.getValue();

        }
        txt_total.setText(String.valueOf(totalPrice));
    }

}
