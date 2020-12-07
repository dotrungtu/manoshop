package com.example.manoshop.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.manoshop.R;
import com.example.manoshop.ShoppingCart_Activity;
import com.example.manoshop.model.Cart;
import com.example.manoshop.network.Delete_Product_In_Cart;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart_Adapter extends RecyclerView.Adapter<ShoppingCart_Adapter.ShoppingCartViewHolder> {

    Context context;
    List<Cart> shoppingList;

    DatabaseReference databaseReferenceUser;

    DatabaseReference databaseReferenceCart;
    DatabaseReference databaseReferenceBill;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;

    TextInputLayout txt_ip_dialog_check_out_show_inf_address;
    TextInputLayout txt_ip_dialog_check_out_show_inf_phone;
    Button btn_dialog_check_out_confirm, btn_dialog_check_out_cancel;

    TextView txt_dialog_check_out_total;
    onPassingTotalPrice onPassingTotalPrice;


    public ShoppingCart_Adapter(Context context, List<Cart> shoppingList, onPassingTotalPrice onPassingTotalPrice) {
        this.context = context;
        this.shoppingList = shoppingList;
        this.onPassingTotalPrice = onPassingTotalPrice;

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceUser = firebaseDatabase.getReference("User");

        databaseReferenceCart = FirebaseDatabase.getInstance().getReference("ShoppingCart");
        databaseReferenceBill = FirebaseDatabase.getInstance().getReference("BillList");
    }

    public interface onPassingTotalPrice {
        void onPassingTotalPrice(String productID, int totalPrice);
    }

    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inflater_shopping_cart, parent, false);
        return new ShoppingCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShoppingCartViewHolder holder, final int position) {
        holder.txtProductName.setText(shoppingList.get(position).getProductName());
        holder.txtPrice.setText(String.valueOf(shoppingList.get(position).getProductPrice()));
        holder.elg_btn_change.setNumber(String.valueOf(shoppingList.get(position).getProductAmount()));
        Log.e("ProductAmount", String.valueOf(shoppingList.get(position).getProductAmount()));
        holder.txtTotal.setText(String.valueOf(shoppingList.get(position).getTotalPrice()));
        Log.e("TotalPrice", String.valueOf(shoppingList.get(position).getTotalPrice()));
        holder.txt_size_cart.setText(String.valueOf(shoppingList.get(position).getProductSize()));
        Picasso.get()
                .load(shoppingList.get(position).getImgUrl())
                .into(holder.imgProductCart);
        holder.elg_btn_change.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                int count = Integer.parseInt(holder.elg_btn_change.getNumber());
                int oldPrice = shoppingList.get(position).getProductPrice();

                int sum = oldPrice * count;

                final Cart cart = shoppingList.get(position);

                holder.txtTotal.setText(String.valueOf(sum));

                final int totalPrice = Integer.parseInt(holder.txtTotal.getText().toString());

                holder.txtTotal.setText(String.valueOf(shoppingList.get(position).setTotalPrice(totalPrice)));
                holder.elg_btn_change.setNumber(String.valueOf(shoppingList.get(position).setProductAmount(count)));

                Log.e("TotalPrice", String.valueOf(shoppingList.get(position).setTotalPrice(totalPrice)));
                Log.e("ProductAmount", String.valueOf(shoppingList.get(position).setProductAmount(count)));

                HashMap<String, Object> result = new HashMap<>();
                result.put("totalPrice", totalPrice);
                result.put("productAmount", count);

                databaseReferenceCart.child(user.getUid()).child(shoppingList.get(position).getProductID()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                onPassingTotalPrice.onPassingTotalPrice(cart.getProductID(), totalPrice);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context.getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        final String billID = databaseReferenceBill.push().getKey();
        final String myDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                View view2 = LayoutInflater.from((ShoppingCart_Activity) context).inflate(R.layout.dialog_checkout, null);
                txt_ip_dialog_check_out_show_inf_address = view2.findViewById(R.id.txt_ip_dialog_check_out_show_inf_address);
                txt_ip_dialog_check_out_show_inf_phone = view2.findViewById(R.id.txt_ip_dialog_check_out_show_inf_phone);
                txt_dialog_check_out_total = view2.findViewById(R.id.txt_dialog_check_out_total);
                btn_dialog_check_out_confirm = view2.findViewById(R.id.btn_dialog_check_out_confirm);
                btn_dialog_check_out_cancel = view2.findViewById(R.id.btn_dialog_check_out_cancel);

                final AlertDialog.Builder builder = new AlertDialog.Builder((ShoppingCart_Activity) context);
                builder.setView(view2);

                final AlertDialog dialog = builder.create();
                dialog.show();

                txt_dialog_check_out_total.setText(holder.txtTotal.getText().toString());

                btn_dialog_check_out_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Query query = databaseReferenceUser.orderByChild("email").equalTo(user.getEmail());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //check until required data get
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    //get data
                                    String fullName = ds.child("fullName").getValue().toString();
                                    String statusBill = "Đang vận chuyển";

                                    final String productNameCart = shoppingList.get(position).getProductName();
                                    final String productIDCart = shoppingList.get(position).getProductID();
                                    final String imgUrl = shoppingList.get(position).getImgUrl();
                                    final String productBrandCart = shoppingList.get(position).getProductBrand();
                                    final String productDesCart = shoppingList.get(position).getProductDes();
                                    final int productAmountCart = Integer.parseInt(holder.elg_btn_change.getNumber());
                                    final String productSizeCart = shoppingList.get(position).getProductSize();
                                    final int totalPrice = Integer.parseInt(String.valueOf(holder.txtTotal.getText()));
                                    String address = txt_ip_dialog_check_out_show_inf_address.getEditText().getText().toString();
                                    String phoneNumber = txt_ip_dialog_check_out_show_inf_phone.getEditText().getText().toString();

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
                                    shoppingCartData.put("totalPrice", String.valueOf(totalPrice));
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
                                                    shoppingList.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, shoppingList.size());

                                                    Intent intent = new Intent(context.getApplicationContext(), ShoppingCart_Activity.class);
                                                    context.startActivity(intent);
                                                    ((ShoppingCart_Activity) context).finish();

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
                });

                btn_dialog_check_out_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });

        //Remove item from recyclerview
        holder.img_remove_item_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String productID = shoppingList.get(position).getProductID();

                Task<Void> voidTask = Delete_Product_In_Cart
                        .deleteProductInCart(productID);

                voidTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Removed: " + productID, Toast.LENGTH_SHORT).show();
                    }
                });
                shoppingList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, shoppingList.size());
            }
        });

    }


    @Override
    public int getItemCount() {
        return shoppingList.size();
    }


    public static final class ShoppingCartViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTotal, txtPrice, txtProductName, txt_size_cart;
        private ImageView imgProductCart, img_remove_item_cart;
        private ElegantNumberButton elg_btn_change;

        public ShoppingCartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPrice = itemView.findViewById(R.id.txtPrice_Cart);
            txt_size_cart = itemView.findViewById(R.id.txt_size_cart);
            txtTotal = itemView.findViewById(R.id.txtTotal_Cart);
            txtProductName = itemView.findViewById(R.id.txtProductName_Cart);
            imgProductCart = itemView.findViewById(R.id.imgProduct_Cart);
            img_remove_item_cart = itemView.findViewById(R.id.img_remove_item_cart);
            elg_btn_change = itemView.findViewById(R.id.elg_num_btn_cart_change);

        }
    }
}
