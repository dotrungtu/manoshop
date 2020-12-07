package com.example.manoshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manoshop.R;
import com.example.manoshop.adapter.BillDetail_Adapter;
import com.example.manoshop.model.BillDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BillDetail_Activity extends AppCompatActivity {
    TextView txt_bill_de_name, txt_bill_de_phone, txt_bill_de_address, txt_bill_de_date, txt_bill_de_total_price;
    RecyclerView recycler_bill_de;
    ImageView img_btn_bill_de_close;

    BillDetail_Adapter bill_adapter;

    DatabaseReference databaseReferenceUser;
    ChildEventListener childEventListener;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail_);

        txt_bill_de_address = findViewById(R.id.txt_bill_de_address);
        txt_bill_de_name = findViewById(R.id.txt_bill_de_name);
        txt_bill_de_phone = findViewById(R.id.txt_bill_de_phone);
        txt_bill_de_date = findViewById(R.id.txt_bill_de_date);
        txt_bill_de_total_price = findViewById(R.id.txt_bill_de_total_price);
        recycler_bill_de = findViewById(R.id.recycler_bill_de);

        final String billID = getIntent().getStringExtra("billID");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("User");
        DatabaseReference bill = FirebaseDatabase.getInstance().getReference("BillList");

        Query query = bill.child(user.getUid()).orderByChild("billID").equalTo(billID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String address = dataSnapshot.child("address").getValue().toString();
                    String dateOfPayment = dataSnapshot.child("dateOfPayment").getValue().toString();
                    String billTotalPrice = dataSnapshot.child("billTotalPrice").getValue().toString();
                    String fullName = dataSnapshot.child("fullName").getValue().toString();
                    String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();

                    txt_bill_de_address.setText(address);
                    txt_bill_de_phone.setText(phoneNumber);
                    txt_bill_de_date.setText(dateOfPayment);
                    txt_bill_de_total_price.setText(billTotalPrice);
                    txt_bill_de_name.setText(fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Button back
        img_btn_bill_de_close = findViewById(R.id.img_btn_bill_de_close);
        img_btn_bill_de_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Bill_ListBill_Activity.class));
                overridePendingTransition(R.anim.slide_in_right_back, R.anim.slide_in_left_back);
            }
        });

        //Add dữ liệu từ Firebase - Realtime Database vào RecycleView (Bill_Details)
        final List<BillDetails> billList;

        recycler_bill_de.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerWomen = new LinearLayoutManager(BillDetail_Activity.this, RecyclerView.VERTICAL, false);
        recycler_bill_de.setLayoutManager(layoutManagerWomen);

        billList = new ArrayList<>();

        bill_adapter = new BillDetail_Adapter(BillDetail_Activity.this, billList);

        recycler_bill_de.setAdapter(bill_adapter);

        if (user != null) {
            currentUserID = firebaseAuth.getCurrentUser().getUid();
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    BillDetails bill = snapshot.getValue(BillDetails.class);
                    bill.setProductID(snapshot.child("productID").getValue().toString());
                    billList.add(bill);
                    bill_adapter.notifyItemInserted(billList.size());


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    BillDetails bill = snapshot.getValue(BillDetails.class);
                    bill.setProductID(snapshot.child("productID").getValue().toString());
                    billList.remove(bill);
                    bill_adapter.notifyDataSetChanged();

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            bill.child(currentUserID).child(billID).child("Products").addChildEventListener(childEventListener);

        } else {
            Toast.makeText(this, "Please login to view invoice", Toast.LENGTH_SHORT).show();
        }
    }
}

