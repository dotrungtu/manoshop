package com.example.manoshop.network;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Delete_Product_In_Cart {
    public static Task<Void> deleteProductInCart(String productID)
    {
        FirebaseAuth firebaseAuth;
        String currentUserID;
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();

        Task<Void> task = FirebaseDatabase.getInstance().getReference("ShoppingCart")
                .child(currentUserID)
                .child(productID)
                .setValue(null);
        return task;
    }
}
