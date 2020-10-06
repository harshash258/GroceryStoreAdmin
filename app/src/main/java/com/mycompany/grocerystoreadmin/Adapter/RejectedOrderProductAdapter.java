package com.mycompany.grocerystoreadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mycompany.grocerystoreadmin.Model.OrderProducts;
import com.mycompany.grocerystoreadmin.R;

import java.util.List;

public class RejectedOrderProductAdapter extends RecyclerView.Adapter<RejectedOrderProductAdapter.ViehHolder> {

    Context mContext;
    List<OrderProducts> mList;

    public RejectedOrderProductAdapter(Context mContext, List<OrderProducts> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViehHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.order_product, parent, false);
        return new ViehHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViehHolder holder, int position) {
        OrderProducts products = mList.get(position);
        holder.name.setText(products.getName());
        holder.price.setText(products.getPrice());
        holder.qty.setText(String.valueOf(products.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViehHolder extends RecyclerView.ViewHolder {
        TextView name, price, qty;
        public ViehHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Orderproduct_name);
            price = itemView.findViewById(R.id.Orderproduct_price);
            qty = itemView.findViewById(R.id.orderProductqty);
        }
    }
}
