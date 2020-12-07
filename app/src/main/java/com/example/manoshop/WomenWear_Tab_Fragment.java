package com.example.manoshop;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manoshop.R;
import com.example.manoshop.adapter.ProductList_Adapter;
import com.example.manoshop.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WomenWear_Tab_Fragment extends Fragment {
    RecyclerView women_recycler;
    ProductList_Adapter women_adapter;
    List<Product> women_list;
    DatabaseReference databaseReference;
    ChildEventListener childEventListener;
    EditText edt_tab_women_search;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.tab_women_wear, container, false);
        edt_tab_women_search = root.findViewById(R.id.edt_tab_women_search);
        women_recycler = root.findViewById(R.id.tab_recycler_women_wear);
        women_recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        women_recycler.setLayoutManager(layoutManager);

        women_list = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("WomenClothes");

        women_adapter = new ProductList_Adapter(getActivity(), women_list);

        women_recycler.setAdapter(women_adapter);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product womenClothes = snapshot.getValue(Product.class);
                womenClothes.setProductName(snapshot.child("productName").getValue().toString());
                women_list.add(womenClothes);
                women_adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Product womenClothes = snapshot.getValue(Product.class);
                womenClothes.setProductName(snapshot.child("productName").getValue().toString());
                women_list.remove(womenClothes);
                women_adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);

        //Edit cho phép người dùng nhập vào và tìm kiếm sản phẩm theo tên sản phẩm
        edt_tab_women_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleDataWomenWear();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    //Phương thức cho phép người dùng tìm kiểm sản phẩm theo tên sản phẩm
    private void handleDataWomenWear() {
        Log.e("Error", "THISSS");
        String searchText = edt_tab_women_search.getText().toString().toLowerCase();
        if (women_list.isEmpty())
            return;
        List<Product> womenData = women_list.stream()
                .filter(p -> {
                    if (p.getProductName() == null || p.getProductName().isEmpty()) return false;
                    return p.getProductName().toLowerCase().contains(searchText);
                }).collect(Collectors.toList());
        women_adapter.setData(womenData);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
