package com.mycompany.grocerystoreadmin.Acitivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mycompany.grocerystoreadmin.Adapter.CurrentOrderAdapter;
import com.mycompany.grocerystoreadmin.Adapter.OrderProductAdapter;
import com.mycompany.grocerystoreadmin.Model.OrderProducts;
import com.mycompany.grocerystoreadmin.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayOrderProducts extends AppCompatActivity {

    RecyclerView recyclerView;
    List<OrderProducts> list;
    OrderProductAdapter adapter;
    DatabaseReference databaseReference;
    TextView date, time, orderiD, totalPrice, consumer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_order_products);

        recyclerView = findViewById(R.id.recyclerView);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        orderiD = findViewById(R.id.orderId);
        totalPrice = findViewById(R.id.totalPrice);
        consumer = findViewById(R.id.consumerName);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        Intent intent = getIntent();
        String id = intent.getStringExtra(CurrentOrderAdapter.ORDER_ID);
        String dates = intent.getStringExtra("date");
        String times = intent.getStringExtra("time");
        String name = intent.getStringExtra("name");
        String total = intent.getStringExtra("total");
        orderiD.setText(id);
        date.setText(dates);
        time.setText(times);
        totalPrice.setText("Total: " + total);
        consumer.setText(name);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin Order")
                .child("Current Order").child(id).child("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    OrderProducts products =  child.getValue(OrderProducts.class);
                    list.add(products);
                }
                adapter = new OrderProductAdapter(DisplayOrderProducts.this, list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}