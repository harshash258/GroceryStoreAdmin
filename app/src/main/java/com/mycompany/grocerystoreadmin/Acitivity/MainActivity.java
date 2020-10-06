package com.mycompany.grocerystoreadmin.Acitivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mycompany.grocerystoreadmin.Model.AboutShop;
import com.mycompany.grocerystoreadmin.Model.User;
import com.mycompany.grocerystoreadmin.R;


public class MainActivity extends AppCompatActivity {

    Button addProduct, viewProduct, about, currentOrder, previousorder, update;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addProduct = findViewById(R.id.btn_addProduct);
        viewProduct = findViewById(R.id.btn_viewProduct);
        currentOrder  = findViewById(R.id.btn_CurrentOrder);
        previousorder = findViewById(R.id.btn_previous_Order);
        about = findViewById(R.id.btn_About);
        update = findViewById(R.id.upload);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddProduct.class);
                startActivity(intent);
            }
        });

        viewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewandEditProduct.class);
                startActivity(intent);
            }
        });

        currentOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CurrentOrders.class);
                startActivity(intent);
            }
        });
        previousorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PreviousOrder.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageSlider.class);
                startActivity(intent);
            }
        });


    }

}