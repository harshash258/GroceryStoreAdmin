package com.mycompany.grocerystoreadmin.Acitivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mycompany.grocerystoreadmin.Adapter.CurrentOrderAdapter;
import com.mycompany.grocerystoreadmin.Model.OrderDetails;
import com.mycompany.grocerystoreadmin.R;

import java.util.ArrayList;
import java.util.List;

public class CurrentOrders extends AppCompatActivity {

    RecyclerView recyclerView;
    List<OrderDetails> mList;
    CurrentOrderAdapter adapter;
    DatabaseReference databaseReference;
    ProgressBar bar;
    int size;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);

        bar = findViewById(R.id.progressBar2);
        relativeLayout = findViewById(R.id.relativeLayout);
        recyclerView = findViewById(R.id.orderRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin Order")
                .child("Current Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                size = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (size >= 0){
            bar.setVisibility(View.INVISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    OrderDetails orderDetails = childSnapshot.child("Order Details").getValue(OrderDetails.class);
                    mList.add(orderDetails);
                    bar.setVisibility(View.INVISIBLE);
                    relativeLayout.setVisibility(View.INVISIBLE);
                }
                adapter = new CurrentOrderAdapter(CurrentOrders.this, mList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}