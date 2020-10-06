package com.mycompany.grocerystoreadmin.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mycompany.grocerystoreadmin.Acitivity.DisplayOrderDetails;
import com.mycompany.grocerystoreadmin.Acitivity.DisplayOrderProducts;
import com.mycompany.grocerystoreadmin.Model.OrderDetails;
import com.mycompany.grocerystoreadmin.R;

import java.util.ArrayList;
import java.util.List;


public class CurrentOrderAdapter extends RecyclerView.Adapter<CurrentOrderAdapter.ViewHolder> {
    public static final String ORDER_ID = "orderid";
    Context mContext;
    List<OrderDetails> mList;
    ArrayList List;
    ArrayAdapter<String> arrayAdapter;
    String userId;

    public CurrentOrderAdapter(Context mContext, List<OrderDetails> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.current_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final OrderDetails details = mList.get(position);
        holder.orderID.setText("OrderId: " + details.getOrderId());
        holder.username.setText("Name:" + details.getConsumer());
        holder.total.setText("Total:" + details.getTotal());
        final String orderID = details.getOrderId();

        List = new ArrayList<>();
        List.add("Choose an Option");
        List.add("Order Completed");
        List.add("Order Rejected");

        arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, List);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(arrayAdapter);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose an Option")) {

                    holder.done.setEnabled(false);
                    holder.done.setVisibility(View.INVISIBLE);
                } else if (parent.getItemAtPosition(position).equals("Order Completed")) {
                    holder.done.setEnabled(true);
                    holder.done.setVisibility(View.VISIBLE);
                    holder.done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                    .child("Admin Order").child("Previous Order").child("Order Completed");
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Admin Order")
                                    .child("Current Order").child(details.getOrderId());
                            Copypaste(reference, databaseReference);
                            reference.removeValue();
                        }
                    });
                }else if (parent.getItemAtPosition(position).equals("Order Rejected")){
                    holder.done.setEnabled(true);
                    holder.done.setVisibility(View.VISIBLE);
                    holder.done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                    .child("Admin Order").child("Previous Order").child("Order Cancelled");
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Admin Order")
                                    .child("Current Order").child(details.getOrderId());
                            Copypaste(reference, databaseReference);
                            reference.removeValue();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.orderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DisplayOrderProducts.class);
                intent.putExtra(ORDER_ID, orderID);
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
        Button orderDetails, done;
        Spinner spinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.orderId);
            username = itemView.findViewById(R.id.order_UserName);
            total = itemView.findViewById(R.id.order_totalPrice);
            orderDetails = itemView.findViewById(R.id.orderDetails);
            done = itemView.findViewById(R.id.hidden);
            spinner = itemView.findViewById(R.id.spinner2);
        }
    }
}
