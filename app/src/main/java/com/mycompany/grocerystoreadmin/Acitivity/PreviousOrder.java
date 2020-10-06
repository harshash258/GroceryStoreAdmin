package com.mycompany.grocerystoreadmin.Acitivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mycompany.grocerystoreadmin.R;

public class PreviousOrder extends AppCompatActivity {

    Button success, rejected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_order);

        success = findViewById(R.id.success);
        rejected = findViewById(R.id.rejected);

        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviousOrder.this, SuccessOrder.class);
                startActivity(intent);
            }
        });

        rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviousOrder.this, RejectedOrder.class);
                startActivity(intent);
            }
        });
    }
}