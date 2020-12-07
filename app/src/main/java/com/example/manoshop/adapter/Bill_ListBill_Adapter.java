package com.example.manoshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manoshop.BillDetail_Activity;
import com.example.manoshop.R;
import com.example.manoshop.model.Bill;

import java.util.List;

public class Bill_ListBill_Adapter extends RecyclerView.Adapter<Bill_ListBill_Adapter.BillViewHolder> {
    private Context context;
    List<Bill> bills;

    String currentUserID;

    public Bill_ListBill_Adapter(Context context, List<Bill> bills) {
        this.context = context;
        this.bills = bills;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inflater_bill_2, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, final int position) {
        holder.txt_bill_id.setText("Mã hóa đơn: " + bills.get(position).getBillID());
        holder.txt_bill_date.setText(bills.get(position).getDateOfPayment());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), BillDetail_Activity.class);
                intent.putExtra("billID", bills.get(position).getBillID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    public static final class BillViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_bill_id, txt_bill_date, txt_bill_total_price;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_bill_id = itemView.findViewById(R.id.txt_bill_id_2);
            txt_bill_date = itemView.findViewById(R.id.txt_date_2);

        }
    }
}
