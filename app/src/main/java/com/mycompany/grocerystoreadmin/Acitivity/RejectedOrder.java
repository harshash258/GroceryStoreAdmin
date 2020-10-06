package com.mycompany.grocerystoreadmin.Acitivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mycompany.grocerystoreadmin.Adapter.RejectedOrderAdapter;
import com.mycompany.grocerystoreadmin.Model.OrderDetails;
import com.mycompany.grocerystoreadmin.R;

import java.util.ArrayList;
import java.util.List;

public class RejectedOrder extends AppCompatActivity {

    RecyclerView recyclerView;
    List<OrderDetails> mList;
    RejectedOrderAdapter adapter;
    DatabaseReference databaseReference;

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_order);


        relativeLayout = findViewById(R.id.relativeLayout);
        recyclerView = findViewById(R.id.rejectedRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Admin Order").child("Previous Order").child("Order Cancelled");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    OrderDetails orderDetails = childSnapshot.child("Order Details").getValue(OrderDetails.class);
                    mList.add(orderDetails);
                    relativeLayout.setVisibility(View.INVISIBLE);
                }
                adapter = new RejectedOrderAdapter(RejectedOrder.this, mList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}