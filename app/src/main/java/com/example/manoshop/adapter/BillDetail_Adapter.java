package com.example.manoshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manoshop.R;
import com.example.manoshop.model.BillDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BillDetail_Adapter extends RecyclerView.Adapter<BillDetail_Adapter.BillDetailViewHolder> {

    Context context;
    List<BillDetails> billdelist;

    public BillDetail_Adapter(Context context, List<BillDetails> billdelist) {
        this.context = context;
        this.billdelist = billdelist;
    }

    @NonNull
    @Override
    public BillDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inflater_product_in_bill, parent, false);
        return new BillDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillDetailViewHolder holder, int position) {
        holder.txt_name.setText(billdelist.get(position).getProductName());
        holder.txt_qty.setText(billdelist.get(position).getProductAmount());
        holder.txt_total_price.setText(billdelist.get(position).getTotalPrice());

        Picasso.get()
                .load(billdelist.get(position).getImgUrl())
                .into(holder.img_product_in_bill);
    }

    @Override
    public int getItemCount() {
        return billdelist.size();
    }

    public static final class BillDetailViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_product_in_bill;
        private TextView txt_name, txt_total_price, txt_qty;

        public BillDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product_in_bill = itemView.findViewById(R.id.img_product_in_bill);
            txt_name = itemView.findViewById(R.id.txt_product_name_in_bill);
            txt_qty = itemView.findViewById(R.id.txt_product_qty_in_bill);
            txt_total_price = itemView.findViewById(R.id.txt_total_price_in_bill);
        }
    }
}


