//package com.example.manoshop;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import BillDetail_Adapter;
//import Bill;
//import BillDetails;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Bill_ListProduct_Activity extends AppCompatActivity {
//
//    BillDetail_Adapter bill_adapter;
//    RecyclerView recyclerBill;
//    private ImageView btn_bill_back;
//
//
//    DatabaseReference databaseReference;
//    DatabaseReference databaseReferenceUser;
//    ChildEventListener childEventListener;
//
//    FirebaseAuth firebaseAuth;
//    FirebaseUser user;
//    FirebaseDatabase firebaseDatabase;
//    String currentUserID;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bill_detail_);
//        final String billID = getIntent().getStringExtra("billID");
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        user = firebaseAuth.getCurrentUser();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReferenceUser = firebaseDatabase.getReference("User");
//        DatabaseReference bill = FirebaseDatabase.getInstance().getReference("BillList");
//
//
//        recyclerBill = findViewById(R.id.recycler_bill);
//
//        //Thêm item vào recycler của bill
//
//
//        //Button back
//        btn_bill_back = findViewById(R.id.btn_bill_back_home);
//        btn_bill_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), Bill_ListBill_Activity.class));
//                overridePendingTransition(R.anim.slide_in_right_back, R.anim.slide_in_left_back);
//            }
//        });
//        final List<BillDetails> billList;
//
//        recyclerBill.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManagerWomen = new LinearLayoutManager(Bill_ListProduct_Activity.this, RecyclerView.VERTICAL, false);
//        recyclerBill.setLayoutManager(layoutManagerWomen);
//
//        billList = new ArrayList<>();
//
//
//        bill_adapter = new BillDetail_Adapter(Bill_ListProduct_Activity.this, billList);
//
//        recyclerBill.setAdapter(bill_adapter);
//
//        if (user != null) {
//            currentUserID = firebaseAuth.getCurrentUser().getUid();
//            childEventListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                    BillDetails bill = snapshot.getValue(BillDetails.class);
//                    bill.setProductID(snapshot.child("productID").getValue().toString());
//                    billList.add(bill);
//                    bill_adapter.notifyItemInserted(billList.size());
//
//
//                }
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                    BillDetails bill = snapshot.getValue(BillDetails.class);
//                    bill.setProductID(snapshot.child("productID").getValue().toString());
//                    billList.remove(bill);
//                    bill_adapter.notifyDataSetChanged();
//
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            };
//            bill.child(currentUserID).child(billID).child("Products").addChildEventListener(childEventListener);
//
//        } else {
//            Toast.makeText(this, "Vui lòng đăng nhập để xem hóa đơn", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
