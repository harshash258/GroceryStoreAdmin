package com.mycompany.grocerystoreadmin.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mycompany.grocerystoreadmin.Acitivity.DisplayOrderDetails;
import com.mycompany.grocerystoreadmin.Acitivity.RejectedOrderProduct;
import com.mycompany.grocerystoreadmin.Model.OrderDetails;
import com.mycompany.grocerystoreadmin.R;

import java.util.ArrayList;
import java.util.List;


public class RejectedOrderAdapter extends RecyclerView.Adapter<RejectedOrderAdapter.ViewHolder> {
    public static final String ORDER_ID = "orderid";
    Context mContext;
    List<OrderDetails> mList;
    ArrayList List;
    ArrayAdapter<String> arrayAdapter;

    public RejectedOrderAdapter(Context mContext, List<OrderDetails> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.previous_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final OrderDetails details = mList.get(position);
        holder.orderID.setText("OrderId: " + details.getOrderId());
        holder.username.setText("Name:" + details.getConsumer());
        holder.total.setText("Total:" + details.getTotal());
        holder.orderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RejectedOrderProduct.class);
                intent.putExtra(ORDER_ID, details.getOrderId());
                intent.putExtra("date", details.getDate());
                intent.putExtra("time", details.getTime());
                intent.putExtra("total",String.valueOf(details.getTotal()));
                intent.putExtra("name", details.getConsumer());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void Copypaste(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            Toast.makeText(mContext, "Error in Placing Order", Toast.LENGTH_LONG).show();
                        } else {
                            final ProgressDialog progress = new ProgressDialog(mContext);
                            progress.setTitle("Placing Order");
                            progress.setMessage("Please wait...");
                            progress.show();
                            Runnable progressRunnable = new Runnable() {
                                @Override
                                public void run() {

                                    progress.cancel();
                                    Toast.makeText(mContext, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                }
                            };
                            Handler pdCanceller = new Handler();
                            pdCanceller.postDelayed(progressRunnable, 2000);

                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderID, username, total;
        Button orderDetails;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.orderId);
            username = itemView.findViewById(R.id.order_UserName);
            total = itemView.findViewById(R.id.order_totalPrice);
            orderDetails = itemView.findViewById(R.id.orderDetails);

        }
    }
}
